package com.shnok.javaserver.gameserver.skills.conditions;

import net.sf.l2j.commons.random.Rnd;

import net.sf.l2j.gameserver.model.actor.Creature;
import net.sf.l2j.gameserver.model.item.kind.Item;
import net.sf.l2j.gameserver.skills.L2Skill;

public class ConditionGameChance extends Condition
{
	private final int _chance;
	
	public ConditionGameChance(int chance)
	{
		_chance = chance;
	}
	
	@Override
	public boolean testImpl(Creature effector, Creature effected, L2Skill skill, Item item)
	{
		return Rnd.get(100) < _chance;
	}
}