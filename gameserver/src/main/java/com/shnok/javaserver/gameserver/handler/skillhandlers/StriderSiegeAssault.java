package com.shnok.javaserver.gameserver.handler.skillhandlers;

import com.shnok.javaserver.gameserver.data.manager.CastleManager;
import com.shnok.javaserver.gameserver.enums.SiegeSide;
import com.shnok.javaserver.gameserver.enums.items.ShotType;
import com.shnok.javaserver.gameserver.enums.skills.ShieldDefense;
import com.shnok.javaserver.gameserver.enums.skills.SkillType;
import com.shnok.javaserver.gameserver.handler.ISkillHandler;
import com.shnok.javaserver.gameserver.model.WorldObject;
import com.shnok.javaserver.gameserver.model.actor.Creature;
import com.shnok.javaserver.gameserver.model.actor.Player;
import com.shnok.javaserver.gameserver.model.actor.instance.Door;
import com.shnok.javaserver.gameserver.model.item.instance.ItemInstance;
import com.shnok.javaserver.gameserver.model.residence.castle.Siege;
import com.shnok.javaserver.gameserver.network.SystemMessageId;
import com.shnok.javaserver.gameserver.network.serverpackets.SystemMessage;
import com.shnok.javaserver.gameserver.skills.Formulas;
import com.shnok.javaserver.gameserver.skills.L2Skill;

public class StriderSiegeAssault implements ISkillHandler
{
	private static final SkillType[] SKILL_IDS =
	{
		SkillType.STRIDER_SIEGE_ASSAULT
	};
	
	@Override
	public void useSkill(Creature creature, L2Skill skill, WorldObject[] targets, ItemInstance item)
	{
		// Must be called by a Player.
		if (!(creature instanceof Player player))
			return;
		
		// Do various checks, and return the Door.
		final Door doorTarget = check(player, targets[0], skill);
		if (doorTarget == null)
			return;
		
		// The Door must be alive.
		if (doorTarget.isAlikeDead())
			return;
		
		final boolean isCrit = skill.getBaseCritRate() > 0 && Formulas.calcCrit(skill.getBaseCritRate() * 10 * Formulas.getSTRBonus(player));
		final boolean ss = player.isChargedShot(ShotType.SOULSHOT);
		final ShieldDefense sDef = Formulas.calcShldUse(player, doorTarget, skill, isCrit);
		
		final int damage = (int) Formulas.calcPhysicalSkillDamage(player, doorTarget, skill, sDef, isCrit, ss);
		if (damage > 0)
		{
			player.sendDamageMessage(doorTarget, damage, false, false, false);
			doorTarget.reduceCurrentHp(damage, player, skill);
		}
		else
			player.sendPacket(SystemMessageId.ATTACK_FAILED);
		
		player.setChargedShot(ShotType.SOULSHOT, skill.isStaticReuse());
	}
	
	@Override
	public SkillType[] getSkillIds()
	{
		return SKILL_IDS;
	}
	
	/**
	 * @param player : The {@link Player} to test.
	 * @param target : The {@link WorldObject} to test.
	 * @param skill : The {@link L2Skill} to test.
	 * @return The {@link Door} if the {@link Player} can cast the {@link L2Skill} on the {@link WorldObject} set as target.
	 */
	public static Door check(Player player, WorldObject target, L2Skill skill)
	{
		// Player must be riding.
		if (!player.isRiding())
		{
			player.sendPacket(SystemMessage.getSystemMessage(SystemMessageId.S1_CANNOT_BE_USED).addSkillName(skill));
			return null;
		}
		
		// Target must be a Door.
		if (!(target instanceof Door doorTarget))
		{
			player.sendPacket(SystemMessageId.INVALID_TARGET);
			return null;
		}
		
		// An active siege must be running, and the Player must be from attacker side.
		final Siege siege = CastleManager.getInstance().getActiveSiege(player);
		if (siege == null || !siege.checkSide(player.getClan(), SiegeSide.ATTACKER))
		{
			player.sendPacket(SystemMessage.getSystemMessage(SystemMessageId.S1_CANNOT_BE_USED).addSkillName(skill));
			return null;
		}
		
		return doorTarget;
	}
}