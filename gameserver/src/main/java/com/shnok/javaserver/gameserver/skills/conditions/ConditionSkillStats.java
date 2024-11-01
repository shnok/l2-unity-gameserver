package com.shnok.javaserver.gameserver.skills.conditions;

import com.shnok.javaserver.gameserver.enums.skills.Stats;
import com.shnok.javaserver.gameserver.model.actor.Creature;
import com.shnok.javaserver.gameserver.model.item.kind.Item;
import com.shnok.javaserver.gameserver.skills.L2Skill;

public class ConditionSkillStats extends Condition
{
	private final Stats _stat;
	
	public ConditionSkillStats(Stats stat)
	{
		super();
		
		_stat = stat;
	}
	
	@Override
	public boolean testImpl(Creature effector, Creature effected, L2Skill skill, Item item)
	{
		return skill != null && skill.getStat() == _stat;
	}
}