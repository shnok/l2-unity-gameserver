package com.shnok.javaserver.gameserver.skills.conditions;

import net.sf.l2j.gameserver.model.actor.Creature;
import net.sf.l2j.gameserver.model.item.kind.Item;
import net.sf.l2j.gameserver.skills.AbstractEffect;
import net.sf.l2j.gameserver.skills.L2Skill;

public class ConditionPlayerActiveEffectId extends Condition
{
	private final int _effectId;
	private final int _effectLvl;
	
	public ConditionPlayerActiveEffectId(int effectId)
	{
		_effectId = effectId;
		_effectLvl = -1;
	}
	
	public ConditionPlayerActiveEffectId(int effectId, int effectLevel)
	{
		_effectId = effectId;
		_effectLvl = effectLevel;
	}
	
	@Override
	public boolean testImpl(Creature effector, Creature effected, L2Skill skill, Item item)
	{
		final AbstractEffect effect = effector.getFirstEffect(_effectId);
		return effect != null && (_effectLvl == -1 || _effectLvl <= effect.getSkill().getLevel());
	}
}