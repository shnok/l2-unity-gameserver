package com.shnok.javaserver.gameserver.skills.conditions;

import net.sf.l2j.gameserver.model.actor.Creature;
import net.sf.l2j.gameserver.model.item.kind.Item;
import net.sf.l2j.gameserver.model.zone.form.ZoneNPoly;
import net.sf.l2j.gameserver.skills.L2Skill;

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
