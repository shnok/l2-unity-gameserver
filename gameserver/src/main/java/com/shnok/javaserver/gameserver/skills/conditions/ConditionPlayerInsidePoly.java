package com.shnok.javaserver.gameserver.skills.conditions;

import com.shnok.javaserver.gameserver.model.actor.Creature;
import com.shnok.javaserver.gameserver.model.item.kind.Item;
import com.shnok.javaserver.gameserver.model.zone.form.ZoneNPoly;
import com.shnok.javaserver.gameserver.skills.L2Skill;

public class ConditionPlayerInsidePoly extends Condition
{
	private final ZoneNPoly _zoneNPoly;
	private final boolean _checkInside;
	
	public ConditionPlayerInsidePoly(ZoneNPoly zoneNPoly, boolean checkInside)
	{
		_zoneNPoly = zoneNPoly;
		_checkInside = checkInside;
	}
	
	@Override
	public boolean testImpl(Creature effector, Creature effected, L2Skill skill, Item item)
	{
		final boolean isInside = _zoneNPoly.isInsideZone(effector.getX(), effector.getY(), effector.getZ());
		return _checkInside ? isInside : !isInside;
	}
}
