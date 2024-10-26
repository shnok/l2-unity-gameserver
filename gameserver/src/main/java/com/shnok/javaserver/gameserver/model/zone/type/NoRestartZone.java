package com.shnok.javaserver.gameserver.model.zone.type;

import com.shnok.javaserver.gameserver.enums.ZoneId;
import com.shnok.javaserver.gameserver.model.actor.Creature;
import com.shnok.javaserver.gameserver.model.actor.Player;
import com.shnok.javaserver.gameserver.model.zone.type.subtype.ZoneType;

/**
 * A zone extending {@link ZoneType} where restart isn't allowed.
 */
public class NoRestartZone extends ZoneType
{
	public NoRestartZone(final int id)
	{
		super(id);
	}
	
	@Override
	protected void onEnter(final Creature creature)
	{
		if (creature instanceof Player player)
			player.setInsideZone(ZoneId.NO_RESTART, true);
	}
	
	@Override
	protected void onExit(final Creature creature)
	{
		if (creature instanceof Player player)
			player.setInsideZone(ZoneId.NO_RESTART, false);
	}
}