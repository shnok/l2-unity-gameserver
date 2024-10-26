package com.shnok.javaserver.gameserver.skills.conditions;

import net.sf.l2j.gameserver.model.actor.Creature;
import net.sf.l2j.gameserver.model.actor.Player;
import net.sf.l2j.gameserver.model.item.kind.Item;
import net.sf.l2j.gameserver.skills.L2Skill;

public final class ConditionUsingItemType extends Condition
{
	private final int _mask;
	
	public ConditionUsingItemType(int mask)
	{
		_mask = mask;
	}
	
	@Override
	public boolean testImpl(Creature effector, Creature effected, L2Skill skill, Item item)
	{
		return effector instanceof Player player && player.getInventory().isWearingType(_mask);
	}
}