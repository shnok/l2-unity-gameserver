package com.shnok.javaserver.gameserver.handler.skillhandlers;

import net.sf.l2j.gameserver.enums.items.ShotType;
import net.sf.l2j.gameserver.enums.skills.SkillType;
import net.sf.l2j.gameserver.enums.skills.Stats;
import net.sf.l2j.gameserver.handler.ISkillHandler;
import net.sf.l2j.gameserver.handler.SkillHandler;
import net.sf.l2j.gameserver.model.WorldObject;
import net.sf.l2j.gameserver.model.actor.Creature;
import net.sf.l2j.gameserver.model.actor.Player;
import net.sf.l2j.gameserver.model.item.instance.ItemInstance;
import net.sf.l2j.gameserver.network.SystemMessageId;
import net.sf.l2j.gameserver.network.serverpackets.SystemMessage;
import net.sf.l2j.gameserver.skills.Formulas;
import net.sf.l2j.gameserver.skills.L2Skill;

public class Heal implements ISkillHandler
{
	private static final SkillType[] SKILL_IDS =
	{
		SkillType.HEAL,
		SkillType.HEAL_STATIC
	};
	
	@Override
	public void useSkill(Creature creature, L2Skill skill, WorldObject[] targets, ItemInstance item)
	{
		final boolean sps = creature.isChargedShot(ShotType.SPIRITSHOT);
		final boolean bsps = creature.isChargedShot(ShotType.BLESSED_SPIRITSHOT);
		
		final ISkillHandler handler = SkillHandler.getInstance().getHandler(SkillType.BUFF);
		if (handler != null)
			handler.useSkill(creature, skill, targets, item);
		
		final double healAmount = Formulas.calcHealAmount(creature, skill, sps, bsps);
		
		for (WorldObject target : targets)
		{
			if (!(target instanceof Creature targetCreature))
				continue;
			
			if (!targetCreature.canBeHealed())
				continue;
			
			final double amount = targetCreature.getStatus().addHp(healAmount * targetCreature.getStatus().calcStat(Stats.HEAL_EFFECTIVNESS, 100, null, null) / 100.);
			
			if (target instanceof Player targetPlayer)
			{
				if (creature != targetPlayer)
					targetPlayer.sendPacket(SystemMessage.getSystemMessage(SystemMessageId.S2_HP_RESTORED_BY_S1).addCharName(creature).addNumber((int) amount));
				else
					targetPlayer.sendPacket(SystemMessage.getSystemMessage(SystemMessageId.S1_HP_RESTORED).addNumber((int) amount));
			}
		}
		
		if (skill.getSkillType() != SkillType.HEAL_STATIC && !skill.isPotion())
			creature.setChargedShot(bsps ? ShotType.BLESSED_SPIRITSHOT : ShotType.SPIRITSHOT, skill.isStaticReuse());
	}
	
	@Override
	public SkillType[] getSkillIds()
	{
		return SKILL_IDS;
	}
}