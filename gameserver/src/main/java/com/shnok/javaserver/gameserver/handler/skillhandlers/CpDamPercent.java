package com.shnok.javaserver.gameserver.handler.skillhandlers;

import net.sf.l2j.gameserver.enums.items.ShotType;
import net.sf.l2j.gameserver.enums.skills.ShieldDefense;
import net.sf.l2j.gameserver.enums.skills.SkillType;
import net.sf.l2j.gameserver.handler.ISkillHandler;
import net.sf.l2j.gameserver.model.WorldObject;
import net.sf.l2j.gameserver.model.actor.Creature;
import net.sf.l2j.gameserver.model.actor.Player;
import net.sf.l2j.gameserver.model.item.instance.ItemInstance;
import net.sf.l2j.gameserver.network.SystemMessageId;
import net.sf.l2j.gameserver.network.serverpackets.SystemMessage;
import net.sf.l2j.gameserver.skills.Formulas;
import net.sf.l2j.gameserver.skills.L2Skill;

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