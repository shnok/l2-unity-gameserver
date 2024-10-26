package com.shnok.javaserver.gameserver.model.zone.type;

import com.shnok.javaserver.Config;
import com.shnok.javaserver.gameserver.enums.ZoneId;
import com.shnok.javaserver.gameserver.model.actor.Creature;
import com.shnok.javaserver.gameserver.model.actor.Player;
import com.shnok.javaserver.gameserver.model.zone.type.subtype.SpawnZoneType;
import com.shnok.javaserver.gameserver.model.zone.type.subtype.ZoneType;

/**
 * A zone extending {@link SpawnZoneType}, used by towns. A town zone is generally associated to a castle for taxes.
 */
public class TownZone extends ZoneType
{
	private int _townId;
	private int _castleId;
	
	private boolean _isPeaceZone = true;
	
	public TownZone(int id)
	{
		super(id);
	}
	
	@Override
	public void setParameter(String name, String value)
	{
		if (name.equals("townId"))
			_townId = Integer.parseInt(value);
		else if (name.equals("castleId"))
			_castleId = Integer.parseInt(value);
		else if (name.equals("isPeaceZone"))
			_isPeaceZone = Boolean.parseBoolean(value);
		else
			super.setParameter(name, value);
	}
	
	@Override
	protected void onEnter(Creature creature)
	{
		if (Config.ZONE_TOWN == 1 && creature instanceof Player player && player.getSiegeState() != 0)
			return;
		
		if (_isPeaceZone && Config.ZONE_TOWN != 2)
			creature.setInsideZone(ZoneId.PEACE, true);
		
		creature.setInsideZone(ZoneId.TOWN, true);
	}
	
	@Override
	protected void onExit(Creature creature)
	{
		if (_isPeaceZone)
			creature.setInsideZone(ZoneId.PEACE, false);
		
		creature.setInsideZone(ZoneId.TOWN, false);
	}
	
	public int getTownId()
	{
		return _townId;
	}
	
	public final int getCastleId()
	{
		return _castleId;
	}
	
	public final boolean isPeaceZone()
	{
		return _isPeaceZone;
	}
}