package com.shnok.javaserver.gameserver.model.zone.type;

import com.shnok.javaserver.gameserver.enums.ZoneId;
import com.shnok.javaserver.gameserver.model.actor.Creature;
import com.shnok.javaserver.gameserver.model.actor.Player;
import com.shnok.javaserver.gameserver.model.zone.type.subtype.ZoneType;

/**
 * A zone extending {@link ZoneType}, used to restrict {@link Player}s to enter mounted on wyverns.<br>
 * <br>
 * A task and a message is called if event is triggered. If the player didn't leave after 5 seconds, he will be dismounted.
 */
public class NoLandingZone extends ZoneType
{
	public NoLandingZone(int id)
	{
		super(id);
	}
	
	@Override
	protected void onEnter(Creature creature)
	{
		if (creature instanceof Player player)
		{
			player.setInsideZone(ZoneId.NO_LANDING, true);
			player.enterOnNoLandingZone();
		}
	}
	
	@Override
	protected void onExit(Creature creature)
	{
		if (creature instanceof Player player)
		{
			player.setInsideZone(ZoneId.NO_LANDING, false);
			player.exitOnNoLandingZone();
		}
	}
}