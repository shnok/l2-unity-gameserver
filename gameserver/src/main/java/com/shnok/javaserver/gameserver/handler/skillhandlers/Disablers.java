package com.shnok.javaserver.gameserver.handler.skillhandlers;

import net.sf.l2j.gameserver.enums.AiEventType;
import net.sf.l2j.gameserver.enums.items.ShotType;
import net.sf.l2j.gameserver.enums.skills.EffectType;
import net.sf.l2j.gameserver.enums.skills.ShieldDefense;
import net.sf.l2j.gameserver.enums.skills.SkillTargetType;
import net.sf.l2j.gameserver.enums.skills.SkillType;
import net.sf.l2j.gameserver.enums.skills.Stats;
import net.sf.l2j.gameserver.handler.ISkillHandler;
import net.sf.l2j.gameserver.model.WorldObject;
import net.sf.l2j.gameserver.model.actor.Attackable;
import net.sf.l2j.gameserver.model.actor.Creature;
import net.sf.l2j.gameserver.model.actor.Player;
import net.sf.l2j.gameserver.model.actor.Summon;
import net.sf.l2j.gameserver.model.actor.instance.SiegeSummon;
import net.sf.l2j.gameserver.model.item.instance.ItemInstance;
import net.sf.l2j.gameserver.network.SystemMessageId;
import net.sf.l2j.gameserver.network.serverpackets.SystemMessage;
import net.sf.l2j.gameserver.skills.AbstractEffect;
import net.sf.l2j.gameserver.skills.Formulas;
import net.sf.l2j.gameserver.skills.L2Skill;

public class Disablers implements ISkillHandler
{
	private static final SkillType[] SKILL_IDS =
	{
		SkillType.STUN,
		SkillType.ROOT,
		SkillType.SLEEP,
		SkillType.CONFUSION,
		SkillType.AGGDAMAGE,
		SkillType.AGGREDUCE,
		SkillType.AGGREDUCE_CHAR,
		SkillType.AGGREMOVE,
		SkillType.MUTE,
		SkillType.FAKE_DEATH,
		SkillType.NEGATE,
		SkillType.CANCEL_DEBUFF,
		SkillType.PARALYZE,
		SkillType.ERASE,
		SkillType.BETRAY
	};
	
	@Override
	public void useSkill(Creature creature, L2Skill skill, WorldObject[] targets, ItemInstance item)
	{
		final SkillType type = skill.getSkillType();
		
		final boolean bsps = creature.isChargedShot(ShotType.BLESSED_SPIRITSHOT);
		
		for (WorldObject obj : targets)
		{
			if (!(obj instanceof Creature targetCreature))
				continue;
			
			// Bypass if target is dead or invul (excluding invul from Petrification)
			if (targetCreature.isDead() || (targetCreature.isInvul() && !targetCreature.isParalyzed()))
				continue;
			
			if (skill.isOffensive() && targetCreature.getFirstEffect(EffectType.BLOCK_DEBUFF) != null)
				continue;
			
			final ShieldDefense sDef = Formulas.calcShldUse(creature, targetCreature, skill, false);
			
			switch (type)
			{
				case BETRAY:
					if (Formulas.calcSkillSuccess(creature, targetCreature, skill, sDef, bsps))
						skill.getEffects(creature, targetCreature, sDef, bsps);
					else if (creature instanceof Player player)
						player.sendPacket(SystemMessage.getSystemMessage(SystemMessageId.S1_RESISTED_YOUR_S2).addCharName(targetCreature).addSkillName(skill));
					break;
				
				case FAKE_DEATH:
					skill.getEffects(creature, targetCreature, sDef, bsps);
					break;
				
				case ROOT, STUN, SLEEP, PARALYZE:
					if (Formulas.calcSkillReflect(targetCreature, skill) == Formulas.SKILL_REFLECT_SUCCEED)
						targetCreature = creature;
					
					if (Formulas.calcSkillSuccess(creature, targetCreature, skill, sDef, bsps))
						skill.getEffects(creature, targetCreature, sDef, bsps);
					else if (creature instanceof Player player)
						player.sendPacket(SystemMessage.getSystemMessage(SystemMessageId.S1_RESISTED_YOUR_S2).addCharName(targetCreature).addSkillName(skill.getId()));
					break;
				
				case MUTE:
					if (Formulas.calcSkillReflect(targetCreature, skill) == Formulas.SKILL_REFLECT_SUCCEED)
						targetCreature = creature;
					
					if (Formulas.calcSkillSuccess(creature, targetCreature, skill, sDef, bsps))
					{
						// stop same type effect if available
						for (AbstractEffect effect : targetCreature.getAllEffects())
						{
							if (effect.getTemplate().getStackOrder() == 99)
								continue;
							
							if (effect.getSkill().getSkillType() == type)
								effect.exit();
						}
						skill.getEffects(creature, targetCreature, sDef, bsps);
					}
					else if (creature instanceof Player player)
						player.sendPacket(SystemMessage.getSystemMessage(SystemMessageId.S1_RESISTED_YOUR_S2).addCharName(targetCreature).addSkillName(skill.getId()));
					break;
				
				case CONFUSION:
					// do nothing if not on mob
					if (targetCreature instanceof Attackable targetAttackable)
					{
						if (Formulas.calcSkillSuccess(creature, targetAttackable, skill, sDef, bsps))
						{
							for (AbstractEffect effect : targetAttackable.getAllEffects())
							{
								if (effect.getTemplate().getStackOrder() == 99)
									continue;
								
								if (effect.getSkill().getSkillType() == type)
									effect.exit();
							}
							skill.getEffects(creature, targetAttackable, sDef, bsps);
						}
						else if (creature instanceof Player player)
							player.sendPacket(SystemMessage.getSystemMessage(SystemMessageId.S1_RESISTED_YOUR_S2).addCharName(targetAttackable).addSkillName(skill));
					}
					else if (creature instanceof Player player)
						player.sendPacket(SystemMessage.getSystemMessage(SystemMessageId.INVALID_TARGET));
					break;
				
				case AGGDAMAGE:
					if (targetCreature instanceof Attackable targetAttackable)
						targetAttackable.getAI().notifyEvent(AiEventType.AGGRESSION, creature, (int) (skill.getPower() / (targetAttackable.getStatus().getLevel() + 7) * 150));
					
					skill.getEffects(creature, targetCreature, sDef, bsps);
					break;
				
				case AGGREDUCE:
					// TODO these skills needs to be rechecked
					if (targetCreature instanceof Attackable targetAttackable)
					{
						skill.getEffects(creature, targetAttackable, sDef, bsps);
						
						if (skill.getPower() > 0)
							targetAttackable.getAI().getAggroList().reduceAllHate((int) skill.getPower());
						else
						{
							final double hate = targetAttackable.getAI().getAggroList().getHate(creature);
							final double diff = hate - targetAttackable.getStatus().calcStat(Stats.AGGRESSION, hate, targetAttackable, skill);
							if (diff > 0)
								targetAttackable.getAI().getAggroList().reduceAllHate((int) diff);
						}
					}
					break;
				
				case AGGREDUCE_CHAR:
					// TODO these skills need to be rechecked
					if (Formulas.calcSkillSuccess(creature, targetCreature, skill, sDef, bsps))
					{
						if (targetCreature instanceof Attackable targetAttackable)
						{
							targetAttackable.getAI().getAggroList().stopHate(creature);
							targetAttackable.getAI().getHateList().stopHate(creature);
						}
						
						skill.getEffects(creature, targetCreature, sDef, bsps);
					}
					else if (creature instanceof Player player)
						player.sendPacket(SystemMessage.getSystemMessage(SystemMessageId.S1_RESISTED_YOUR_S2).addCharName(targetCreature).addSkillName(skill));
					break;
				
				case AGGREMOVE:
					// TODO these skills needs to be rechecked
					if (targetCreature instanceof Attackable targetAttackable && !targetCreature.isRaidRelated())
					{
						if (Formulas.calcSkillSuccess(creature, targetCreature, skill, sDef, bsps))
						{
							if (skill.getTargetType() == SkillTargetType.UNDEAD)
							{
								if (targetCreature.isUndead())
								{
									targetAttackable.getAI().getAggroList().cleanAllHate();
									targetAttackable.getAI().getHateList().cleanAllHate();
								}
							}
							else
							{
								targetAttackable.getAI().getAggroList().cleanAllHate();
								targetAttackable.getAI().getHateList().cleanAllHate();
							}
						}
						else if (creature instanceof Player player)
							player.sendPacket(SystemMessage.getSystemMessage(SystemMessageId.S1_RESISTED_YOUR_S2).addCharName(targetCreature).addSkillName(skill));
					}
					break;
				
				case ERASE:
					if (Formulas.calcSkillSuccess(creature, targetCreature, skill, sDef, bsps))
					{
						// Doesn't affect anything, except Summons which aren't Siege Summons.
						if (targetCreature instanceof Summon targetSummon && !(targetCreature instanceof SiegeSummon))
						{
							final Player summonOwner = targetCreature.getActingPlayer();
							if (summonOwner != null)
							{
								targetSummon.unSummon(summonOwner);
								
								summonOwner.sendPacket(SystemMessageId.YOUR_SERVITOR_HAS_VANISHED);
							}
						}
					}
					else if (creature instanceof Player player)
						player.sendPacket(SystemMessage.getSystemMessage(SystemMessageId.S1_RESISTED_YOUR_S2).addCharName(targetCreature).addSkillName(skill));
					break;
				
				case CANCEL_DEBUFF:
					final AbstractEffect[] effects = targetCreature.getAllEffects();
					if (effects == null || effects.length == 0)
						break;
					
					int count = (skill.getMaxNegatedEffects() > 0) ? 0 : -2;
					for (AbstractEffect effect : effects)
					{
						if (!effect.getSkill().isDebuff() || !effect.getSkill().canBeDispeled() || effect.getTemplate().getStackOrder() == 99)
							continue;
						
						effect.exit();
						
						if (count > -1)
						{
							count++;
							if (count >= skill.getMaxNegatedEffects())
								break;
						}
					}
					break;
				
				case NEGATE:
					if (Formulas.calcSkillReflect(targetCreature, skill) == Formulas.SKILL_REFLECT_SUCCEED)
						targetCreature = creature;
					
					// Skills with negateId (skillId)
					if (skill.getNegateId().length != 0)
					{
						for (int id : skill.getNegateId())
						{
							if (id != 0)
								targetCreature.stopSkillEffects(id);
						}
					}
					// All others negate type skills
					else
					{
						for (AbstractEffect effect : targetCreature.getAllEffects())
						{
							if (effect.getTemplate().getStackOrder() == 99)
								continue;
							
							final L2Skill effectSkill = effect.getSkill();
							for (SkillType skillType : skill.getNegateStats())
							{
								// If power is -1 the effect is always removed without lvl check
								if (skill.getNegateLvl() == -1)
								{
									if (effectSkill.getSkillType() == skillType || (effectSkill.getEffectType() != null && effectSkill.getEffectType() == skillType))
										effect.exit();
								}
								// Remove the effect according to its power.
								else
								{
									if (effectSkill.getEffectType() != null && effectSkill.getEffectAbnormalLvl() >= 0)
									{
										if (effectSkill.getEffectType() == skillType && effectSkill.getEffectAbnormalLvl() <= skill.getNegateLvl())
											effect.exit();
									}
									else if (effectSkill.getSkillType() == skillType && effectSkill.getAbnormalLvl() <= skill.getNegateLvl())
										effect.exit();
								}
							}
						}
					}
					skill.getEffects(creature, targetCreature, sDef, bsps);
					break;
			}
		}
		
		if (skill.hasSelfEffects())
		{
			final AbstractEffect effect = creature.getFirstEffect(skill.getId());
			if (effect != null && effect.isSelfEffect())
				effect.exit();
			
			skill.getEffectsSelf(creature);
		}
		creature.setChargedShot(bsps ? ShotType.BLESSED_SPIRITSHOT : ShotType.SPIRITSHOT, skill.isStaticReuse());
	}
	
	@Override
	public SkillType[] getSkillIds()
	{
		return SKILL_IDS;
	}
}