package com.shnok.javaserver.gameserver.network.serverpackets.unused;

import com.shnok.javaserver.gameserver.model.actor.Player;
import com.shnok.javaserver.gameserver.model.location.SpawnLocation;
import com.shnok.javaserver.gameserver.network.serverpackets.L2GameServerPacket;

public class ValidateLocationInVehicle extends L2GameServerPacket
{
	private final int _objectId;
	private final int _boatId;
	private final SpawnLocation _loc;
	
	public ValidateLocationInVehicle(Player player)
	{
		_objectId = player.getObjectId();
		_boatId = player.getBoatInfo().getBoat().getObjectId();
		_loc = player.getBoatInfo().getBoatPosition().clone();
	}
	
	@Override
	protected final void writeImpl()
	{
		writeC(0x73);
		writeD(_objectId);
		writeD(_boatId);
		writeLoc(_loc);
		writeD(_loc.getHeading());
	}
}