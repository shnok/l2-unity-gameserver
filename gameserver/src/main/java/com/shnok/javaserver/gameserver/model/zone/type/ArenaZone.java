package com.shnok.javaserver.gameserver.model.zone.type;

import com.shnok.javaserver.gameserver.enums.ZoneId;
import com.shnok.javaserver.gameserver.model.actor.Creature;
import com.shnok.javaserver.gameserver.model.actor.Player;
import com.shnok.javaserver.gameserver.model.zone.type.subtype.ZoneType;
import com.shnok.javaserver.gameserver.network.SystemMessageId;

/**
 * A zone extending {@link ZoneType}, where summoning is forbidden. The place is considered a pvp zone (no flag, no karma). It is used for arenas.
 */
public class ArenaZone extends ZoneType
{
	public ArenaZone(int id)
	{
		super(id);
	}
	
	@Override
	protected void onEnter(Creature creature)
	{
		if (creature instanceof Player player)
			player.sendPacket(SystemMessageId.ENTERED_COMBAT_ZONE);
		
		creature.setInsideZone(ZoneId.PVP, true);
		creature.setInsideZone(ZoneId.NO_SUMMON_FRIEND, true);
	}
	
	@Override
	protected void onExit(Creature creature)
	{
		creature.setInsideZone(ZoneId.PVP, false);
		creature.setInsideZone(ZoneId.NO_SUMMON_FRIEND, false);
		
		if (creature instanceof Player player)
			player.sendPacket(SystemMessageId.LEFT_COMBAT_ZONE);
	}
}