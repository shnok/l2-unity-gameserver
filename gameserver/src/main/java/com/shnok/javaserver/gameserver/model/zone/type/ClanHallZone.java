package com.shnok.javaserver.gameserver.model.zone.type;

import com.shnok.javaserver.gameserver.data.manager.ClanHallManager;
import com.shnok.javaserver.gameserver.enums.SpawnType;
import com.shnok.javaserver.gameserver.enums.ZoneId;
import com.shnok.javaserver.gameserver.model.actor.Creature;
import com.shnok.javaserver.gameserver.model.actor.Player;
import com.shnok.javaserver.gameserver.model.residence.clanhall.ClanHall;
import com.shnok.javaserver.gameserver.model.zone.type.subtype.ResidenceZoneType;
import com.shnok.javaserver.gameserver.network.serverpackets.unused.ClanHallDecoration;

/**
 * A zone extending {@link ResidenceZoneType} used by {@link ClanHall}s.
 */
public class ClanHallZone extends ResidenceZoneType
{
	public ClanHallZone(int id)
	{
		super(id);
	}
	
	@Override
	public void banishForeigners(int clanId)
	{
		final ClanHall ch = ClanHallManager.getInstance().getClanHall(getResidenceId());
		if (ch == null)
			return;
		
		for (Player player : getKnownTypeInside(Player.class, p -> p.getClanId() != clanId))
			player.teleportTo(ch.getRndSpawn(SpawnType.BANISH), 20);
	}
	
	@Override
	public void setParameter(String name, String value)
	{
		if (name.equals("clanHallId"))
			setResidenceId(Integer.parseInt(value));
		else
			super.setParameter(name, value);
	}
	
	@Override
	protected void onEnter(Creature creature)
	{
		if (creature instanceof Player player)
		{
			// Set as in clan hall
			player.setInsideZone(ZoneId.CLAN_HALL, true);
			
			final ClanHall ch = ClanHallManager.getInstance().getClanHall(getResidenceId());
			if (ch == null)
				return;
			
			// Send decoration packet
			player.sendPacket(new ClanHallDecoration(ch));
		}
	}
	
	@Override
	protected void onExit(Creature creature)
	{
		if (creature instanceof Player player)
			player.setInsideZone(ZoneId.CLAN_HALL, false);
	}
}