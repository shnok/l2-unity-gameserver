package com.shnok.javaserver.gameserver.skills.effects;

import net.sf.l2j.gameserver.data.SkillTable;
import net.sf.l2j.gameserver.enums.skills.EffectType;
import net.sf.l2j.gameserver.model.actor.Creature;
import net.sf.l2j.gameserver.skills.AbstractEffect;
import net.sf.l2j.gameserver.skills.L2Skill;

public class EffectFusion extends AbstractEffect
{
	public int _effect;
	public int _maxEffect;
	
	public EffectFusion(EffectTemplate template, L2Skill skill, Creature effected, Creature effector)
	{
		super(template, skill, effected, effector);
		
		_effect = getSkill().getLevel();
		_maxEffect = SkillTable.getInstance().getMaxLevel(getSkill().getId());
	}
	
	@Override
	public boolean onActionTime()
	{
		return true;
	}
	
	@Override
	public EffectType getEffectType()
	{
		return EffectType.FUSION;
	}
	
	public void increaseEffect()
	{
		if (_effect < _maxEffect)
		{
			_effect++;
			updateBuff();
		}
	}
	
	public void decreaseForce()
	{
		_effect--;
		if (_effect < 1)
			exit();
		else
			updateBuff();
	}
	
	private void updateBuff()
	{
		exit();
		SkillTable.getInstance().getInfo(getSkill().getId(), _effect).getEffects(getEffector(), getEffected());
	}
}