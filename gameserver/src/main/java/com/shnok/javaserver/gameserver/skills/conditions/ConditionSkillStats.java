package com.shnok.javaserver.gameserver.skills.conditions;

import net.sf.l2j.gameserver.enums.skills.Stats;
import net.sf.l2j.gameserver.model.actor.Creature;
import net.sf.l2j.gameserver.model.item.kind.Item;
import net.sf.l2j.gameserver.skills.L2Skill;

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