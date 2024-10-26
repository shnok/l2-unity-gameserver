package com.shnok.javaserver.gameserver.network.serverpackets;

import com.shnok.javaserver.gameserver.model.actor.container.player.BoatInfo;
import com.shnok.javaserver.gameserver.model.location.Location;
import com.shnok.javaserver.gameserver.model.location.SpawnLocation;

public class GetOnVehicle extends L2GameServerPacket
{
	private final int _objectId;
	private final int _boatId;
	private final Location _location = new Location(0, 0, 0);
	
	public GetOnVehicle(int objectId, int boatId, int x, int y, int z)
	{
		_objectId = objectId;
		_boatId = boatId;
		_location.set(x, y, z);
	}
	
	public GetOnVehicle(int objectId, int boatId, SpawnLocation loc)
	{
		_objectId = objectId;
		_boatId = boatId;
		_location.set(loc);
	}
	
	public GetOnVehicle(BoatInfo info)
	{
		_objectId = info.getPlayer().getObjectId();
		_boatId = info.getBoat().getObjectId();
		_location.set(info.getBoatPosition());
	}
	
	@Override
	protected void writeImpl()
	{
		writeC(0x5C);
		writeD(_objectId);
		writeD(_boatId);
		writeLoc(_location);
	}
}