package com.shnok.javaserver.gameserver.handler.skillhandlers;

import com.shnok.javaserver.gameserver.enums.items.ShotType;
import com.shnok.javaserver.gameserver.enums.skills.ShieldDefense;
import com.shnok.javaserver.gameserver.enums.skills.SkillType;
import com.shnok.javaserver.gameserver.handler.ISkillHandler;
import com.shnok.javaserver.gameserver.model.WorldObject;
import com.shnok.javaserver.gameserver.model.actor.Creature;
import com.shnok.javaserver.gameserver.model.actor.Player;
import com.shnok.javaserver.gameserver.model.item.instance.ItemInstance;
import com.shnok.javaserver.gameserver.network.SystemMessageId;
import com.shnok.javaserver.gameserver.network.serverpackets.SystemMessage;
import com.shnok.javaserver.gameserver.skills.Formulas;
import com.shnok.javaserver.gameserver.skills.L2Skill;

public class CpDamPercent implements ISkillHandler
{
	private static final SkillType[] SKILL_IDS =
	{
		SkillType.CPDAMPERCENT
	};
	
	@Override
	public void useSkill(Creature creature, L2Skill skill, WorldObject[] targets, ItemInstance item)
	{
		if (creature.isAlikeDead())
			return;
		
		final boolean bsps = creature.isChargedShot(ShotType.BLESSED_SPIRITSHOT);
		
		for (WorldObject obj : targets)
		{
			if (!(obj instanceof Player targetPlayer))
				continue;
			
			if (targetPlayer.isDead() || targetPlayer.isInvul())
				continue;
			
			final ShieldDefense sDef = Formulas.calcShldUse(creature, targetPlayer, skill, false);
			
			final int damage = (int) (targetPlayer.getStatus().getCp() * (skill.getPower() / 100));
			
			// Manage cast break of the target (calculating rate, sending message...)
			Formulas.calcCastBreak(targetPlayer, damage);
			
			skill.getEffects(creature, targetPlayer, sDef, bsps);
			creature.sendDamageMessage(targetPlayer, damage, false, false, false);
			targetPlayer.getStatus().setCp(targetPlayer.getStatus().getCp() - damage);
			
			// Custom message to see Wrath damage on target
			targetPlayer.sendPacket(SystemMessage.getSystemMessage(SystemMessageId.S1_GAVE_YOU_S2_DMG).addCharName(creature).addNumber(damage));
		}
		creature.setChargedShot(ShotType.SOULSHOT, skill.isStaticReuse());
	}
	
	@Override
	public SkillType[] getSkillIds()
	{
		return SKILL_IDS;
	}
}