package com.shnok.javaserver.gameserver.skills.conditions;

import net.sf.l2j.gameserver.model.actor.Creature;
import net.sf.l2j.gameserver.model.item.kind.Item;
import net.sf.l2j.gameserver.skills.L2Skill;

public class ConditionLogicNot extends Condition
{
	private final Condition _condition;
	
	public ConditionLogicNot(Condition condition)
	{
		_condition = condition;
		
		if (getListener() != null)
			_condition.setListener(this);
	}
	
	@Override
	void setListener(ConditionListener listener)
	{
		if (listener != null)
			_condition.setListener(this);
		else
			_condition.setListener(null);
		
		super.setListener(listener);
	}
	
	@Override
	public boolean testImpl(Creature effector, Creature effected, L2Skill skill, Item item)
	{
		return !_condition.test(effector, effected, skill, item);
	}
}