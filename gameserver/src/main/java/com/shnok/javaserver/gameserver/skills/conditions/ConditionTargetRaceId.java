package com.shnok.javaserver.gameserver.skills.conditions;

import java.util.List;

import net.sf.l2j.gameserver.model.actor.Creature;
import net.sf.l2j.gameserver.model.actor.Npc;
import net.sf.l2j.gameserver.model.item.kind.Item;
import net.sf.l2j.gameserver.skills.L2Skill;

public class ConditionTargetRaceId extends Condition
{
	private final List<Integer> _raceIds;
	
	public ConditionTargetRaceId(List<Integer> raceId)
	{
		_raceIds = raceId;
	}
	
	@Override
	public boolean testImpl(Creature effector, Creature effected, L2Skill skill, Item item)
	{
		return effected instanceof Npc targetNpc && _raceIds.contains(targetNpc.getTemplate().getRace().ordinal());
	}
}