package com.shnok.javaserver.gameserver.model.zone.type;

import com.shnok.javaserver.gameserver.enums.ZoneId;
import com.shnok.javaserver.gameserver.model.actor.Creature;
import com.shnok.javaserver.gameserver.model.actor.Player;
import com.shnok.javaserver.gameserver.model.zone.type.subtype.ZoneType;

/**
 * A zone extending {@link ZoneType} where 'Build Headquarters' is allowed.
 */
public class HqZone extends ZoneType
{
	public HqZone(final int id)
	{
		super(id);
	}
	
	@Override
	protected void onEnter(final Creature creature)
	{
		if (creature instanceof Player player)
			player.setInsideZone(ZoneId.HQ, true);
	}
	
	@Override
	protected void onExit(final Creature creature)
	{
		if (creature instanceof Player player)
			player.setInsideZone(ZoneId.HQ, false);
	}
}