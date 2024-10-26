package com.shnok.javaserver.gameserver.skills.conditions;

import net.sf.l2j.gameserver.enums.skills.PlayerState;
import net.sf.l2j.gameserver.model.actor.Creature;
import net.sf.l2j.gameserver.model.actor.Player;
import net.sf.l2j.gameserver.model.item.kind.Item;
import net.sf.l2j.gameserver.skills.L2Skill;

public class ConditionPlayerState extends Condition
{
	private final PlayerState _check;
	private final boolean _required;
	
	public ConditionPlayerState(PlayerState check, boolean required)
	{
		_check = check;
		_required = required;
	}
	
	@Override
	public boolean testImpl(Creature effector, Creature effected, L2Skill skill, Item item)
	{
		switch (_check)
		{
			case RESTING:
				return (effector instanceof Player player) ? player.isSitting() == _required : !_required;
			
			case MOVING:
				return effector.isMoving() == _required;
			
			case RUNNING:
				return effector.isMoving() == _required && effector.isRunning() == _required;
			
			case RIDING:
				return effector.isRiding() == _required;
			
			case FLYING:
				return effector.isFlying() == _required;
			
			case BEHIND:
				return effector.isBehind(effected) == _required;
			
			case FRONT:
				return effector.isInFrontOf(effected) == _required;
			
			case OLYMPIAD:
				return (effector instanceof Player player) ? player.isInOlympiadMode() == _required : !_required;
		}
		return !_required;
	}
}