package com.shnok.javaserver.gameserver.model.zone.type;

import com.shnok.javaserver.gameserver.enums.ZoneId;
import com.shnok.javaserver.gameserver.model.actor.Creature;
import com.shnok.javaserver.gameserver.model.actor.Player;
import com.shnok.javaserver.gameserver.model.zone.type.subtype.ZoneType;

/**
 * A zone extending {@link ZoneType} where store isn't allowed.
 */
public class NoStoreZone extends ZoneType
{
	public NoStoreZone(final int id)
	{
		super(id);
	}
	
	@Override
	protected void onEnter(final Creature creature)
	{
		if (creature instanceof Player player)
			player.setInsideZone(ZoneId.NO_STORE, true);
	}
	
	@Override
	protected void onExit(final Creature creature)
	{
		if (creature instanceof Player player)
			player.setInsideZone(ZoneId.NO_STORE, false);
	}
}