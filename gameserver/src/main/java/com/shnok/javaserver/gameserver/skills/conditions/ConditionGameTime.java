package com.shnok.javaserver.gameserver.skills.conditions;

import net.sf.l2j.gameserver.model.actor.Creature;
import net.sf.l2j.gameserver.model.item.kind.Item;
import net.sf.l2j.gameserver.skills.L2Skill;
import net.sf.l2j.gameserver.taskmanager.GameTimeTaskManager;

public class ConditionGameTime extends Condition
{
	private final boolean _night;
	
	public ConditionGameTime(boolean night)
	{
		_night = night;
	}
	
	@Override
	public boolean testImpl(Creature effector, Creature effected, L2Skill skill, Item item)
	{
		return GameTimeTaskManager.getInstance().isNight() == _night;
	}
}