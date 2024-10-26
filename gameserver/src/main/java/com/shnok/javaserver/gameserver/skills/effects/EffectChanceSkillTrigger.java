package com.shnok.javaserver.gameserver.skills.effects;

import net.sf.l2j.gameserver.enums.skills.EffectType;
import net.sf.l2j.gameserver.model.actor.Creature;
import net.sf.l2j.gameserver.skills.AbstractEffect;
import net.sf.l2j.gameserver.skills.ChanceCondition;
import net.sf.l2j.gameserver.skills.IChanceSkillTrigger;
import net.sf.l2j.gameserver.skills.L2Skill;

public class EffectChanceSkillTrigger extends AbstractEffect implements IChanceSkillTrigger
{
	private final int _triggeredId;
	private final int _triggeredLevel;
	private final ChanceCondition _chanceCondition;
	
	public EffectChanceSkillTrigger(EffectTemplate template, L2Skill skill, Creature effected, Creature effector)
	{
		super(template, skill, effected, effector);
		
		_triggeredId = template.getTriggeredId();
		_triggeredLevel = template.getTriggeredLevel();
		_chanceCondition = template.getChanceCondition();
	}
	
	@Override
	public EffectType getEffectType()
	{
		return EffectType.CHANCE_SKILL_TRIGGER;
	}
	
	@Override
	public boolean onStart()
	{
		getEffected().addChanceTrigger(this);
		return super.onStart();
	}
	
	@Override
	public boolean onActionTime()
	{
		return false;
	}
	
	@Override
	public void onExit()
	{
		getEffected().removeChanceEffect(this);
		super.onExit();
	}
	
	@Override
	public int getTriggeredChanceId()
	{
		return _triggeredId;
	}
	
	@Override
	public int getTriggeredChanceLevel()
	{
		return _triggeredLevel;
	}
	
	@Override
	public boolean triggersChanceSkill()
	{
		return _triggeredId > 1;
	}
	
	@Override
	public ChanceCondition getTriggeredChanceCondition()
	{
		return _chanceCondition;
	}
}