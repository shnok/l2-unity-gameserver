package com.shnok.javaserver.gameserver.handler.skillhandlers;

import net.sf.l2j.gameserver.enums.items.ShotType;
import net.sf.l2j.gameserver.enums.skills.EffectType;
import net.sf.l2j.gameserver.enums.skills.ShieldDefense;
import net.sf.l2j.gameserver.enums.skills.SkillType;
import net.sf.l2j.gameserver.handler.ISkillHandler;
import net.sf.l2j.gameserver.model.WorldObject;
import net.sf.l2j.gameserver.model.actor.Creature;
import net.sf.l2j.gameserver.model.item.instance.ItemInstance;
import net.sf.l2j.gameserver.network.SystemMessageId;
import net.sf.l2j.gameserver.network.serverpackets.SystemMessage;
import net.sf.l2j.gameserver.skills.AbstractEffect;
import net.sf.l2j.gameserver.skills.Formulas;
import net.sf.l2j.gameserver.skills.L2Skill;

public class Mdam implements ISkillHandler
{
	private static final SkillType[] SKILL_IDS =
	{
		SkillType.MDAM,
		SkillType.DEATHLINK
	};
	
	@Override
	public void useSkill(Creature creature, L2Skill skill, WorldObject[] targets, ItemInstance item)
	{
		if (creature.isAlikeDead())
			return;
		
		final boolean sps = creature.isChargedShot(ShotType.SPIRITSHOT);
		final boolean bsps = creature.isChargedShot(ShotType.BLESSED_SPIRITSHOT);
		
		for (WorldObject target : targets)
		{
			if (!(target instanceof Creature targetCreature))
				continue;
			
			if (targetCreature.isDead())
				continue;
			
			final boolean isCrit = Formulas.calcMCrit(creature, targetCreature, skill);
			final ShieldDefense sDef = Formulas.calcShldUse(creature, targetCreature, skill, false);
			final byte reflect = Formulas.calcSkillReflect(targetCreature, skill);
			
			int damage = (int) Formulas.calcMagicDam(creature, targetCreature, skill, sDef, sps, bsps, isCrit);
			if (damage > 0)
			{
				// Manage cast break of the target (calculating rate, sending message...)
				Formulas.calcCastBreak(targetCreature, damage);
				
				creature.sendDamageMessage(targetCreature, damage, isCrit, false, false);
				targetCreature.reduceCurrentHp(damage, creature, skill);
				
				if (skill.hasEffects() && targetCreature.getFirstEffect(EffectType.BLOCK_DEBUFF) == null)
				{
					if ((reflect & Formulas.SKILL_REFLECT_SUCCEED) != 0) // reflect skill effects
					{
						creature.stopSkillEffects(skill.getId());
						skill.getEffects(targetCreature, creature);
					}
					else
					{
						// activate attacked effects, if any
						targetCreature.stopSkillEffects(skill.getId());
						if (Formulas.calcSkillSuccess(creature, targetCreature, skill, sDef, bsps))
							skill.getEffects(creature, targetCreature, sDef, bsps);
						else
							creature.sendPacket(SystemMessage.getSystemMessage(SystemMessageId.S1_RESISTED_YOUR_S2).addCharName(targetCreature).addSkillName(skill.getId()));
					}
				}
			}
		}
		
		if (skill.hasSelfEffects())
		{
			final AbstractEffect effect = creature.getFirstEffect(skill.getId());
			if (effect != null && effect.isSelfEffect())
				effect.exit();
			
			skill.getEffectsSelf(creature);
		}
		
		if (skill.isSuicideAttack())
			creature.doDie(creature);
		
		creature.setChargedShot(bsps ? ShotType.BLESSED_SPIRITSHOT : ShotType.SPIRITSHOT, skill.isStaticReuse());
	}
	
	@Override
	public SkillType[] getSkillIds()
	{
		return SKILL_IDS;
	}
}