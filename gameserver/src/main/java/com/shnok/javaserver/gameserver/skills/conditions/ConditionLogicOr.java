package com.shnok.javaserver.gameserver.skills.conditions;

import com.shnok.javaserver.gameserver.model.actor.Creature;
import com.shnok.javaserver.gameserver.model.item.kind.Item;
import com.shnok.javaserver.gameserver.skills.L2Skill;

public class ConditionLogicOr extends Condition
{
	private static Condition[] _emptyConditions = new Condition[0];
	public Condition[] conditions = _emptyConditions;
	
	public void add(Condition condition)
	{
		if (condition == null)
			return;
		
		if (getListener() != null)
			condition.setListener(this);
		
		final int len = conditions.length;
		final Condition[] tmp = new Condition[len + 1];
		System.arraycopy(conditions, 0, tmp, 0, len);
		tmp[len] = condition;
		conditions = tmp;
	}
	
	@Override
	void setListener(ConditionListener listener)
	{
		if (listener != null)
		{
			for (Condition c : conditions)
				c.setListener(this);
		}
		else
		{
			for (Condition c : conditions)
				c.setListener(null);
		}
		super.setListener(listener);
	}
	
	@Override
	public boolean testImpl(Creature effector, Creature effected, L2Skill skill, Item item)
	{
		for (Condition c : conditions)
		{
			if (c.test(effector, effected, skill, item))
				return true;
		}
		return false;
	}
}