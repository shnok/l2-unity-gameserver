package com.shnok.javaserver.gameserver.model.zone.type;

import com.shnok.javaserver.gameserver.model.actor.Creature;
import com.shnok.javaserver.gameserver.model.zone.type.subtype.ZoneType;

/**
 * A zone extending {@link ZoneType}, used for fish points.
 */
public class FishingZone extends ZoneType
{
	public FishingZone(int id)
	{
		super(id);
	}
	
	@Override
	protected void onEnter(Creature creature)
	{
		// Do nothing.
	}
	
	@Override
	protected void onExit(Creature creature)
	{
		// Do nothing.
	}
	
	public int getWaterZ()
	{
		return getZone().getHighZ();
	}
}