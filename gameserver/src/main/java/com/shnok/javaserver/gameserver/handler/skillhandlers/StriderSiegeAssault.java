package com.shnok.javaserver.gameserver.handler.skillhandlers;

import net.sf.l2j.gameserver.data.manager.CastleManager;
import net.sf.l2j.gameserver.enums.SiegeSide;
import net.sf.l2j.gameserver.enums.items.ShotType;
import net.sf.l2j.gameserver.enums.skills.ShieldDefense;
import net.sf.l2j.gameserver.enums.skills.SkillType;
import net.sf.l2j.gameserver.handler.ISkillHandler;
import net.sf.l2j.gameserver.model.WorldObject;
import net.sf.l2j.gameserver.model.actor.Creature;
import net.sf.l2j.gameserver.model.actor.Player;
import net.sf.l2j.gameserver.model.actor.instance.Door;
import net.sf.l2j.gameserver.model.item.instance.ItemInstance;
import net.sf.l2j.gameserver.model.residence.castle.Siege;
import net.sf.l2j.gameserver.network.SystemMessageId;
import net.sf.l2j.gameserver.network.serverpackets.SystemMessage;
import net.sf.l2j.gameserver.skills.Formulas;
import net.sf.l2j.gameserver.skills.L2Skill;

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