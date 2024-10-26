package com.shnok.javaserver.gameserver.skills.conditions;

import net.sf.l2j.gameserver.model.actor.Creature;
import net.sf.l2j.gameserver.model.actor.Player;
import net.sf.l2j.gameserver.model.item.kind.Item;
import net.sf.l2j.gameserver.skills.L2Skill;

public class ConditionPlayerPkCount extends Condition
{
	public final int _pk;
	
	public ConditionPlayerPkCount(int pk)
	{
		_pk = pk;
	}
	
	@Override
	public boolean testImpl(Creature effector, Creature effected, L2Skill skill, Item item)
	{
		return effector instanceof Player player && player.getPkKills() <= _pk;
	}
}