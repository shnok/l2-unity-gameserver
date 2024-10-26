package com.shnok.javaserver.gameserver.network.serverpackets;

import net.sf.l2j.gameserver.model.actor.Player;
import net.sf.l2j.gameserver.model.location.SpawnLocation;

public class StopMoveInVehicle extends L2GameServerPacket
{
	private final int _objectId;
	private final int _boatId;
	private final SpawnLocation _loc;
	
	public StopMoveInVehicle(Player player, int boatId)
	{
		_objectId = player.getObjectId();
		_boatId = boatId;
		_loc = player.getBoatInfo().getBoatPosition().clone();
	}
	
	@Override
	protected void writeImpl()
	{
		writeC(0x72);
		writeD(_objectId);
		writeD(_boatId);
		writeLoc(_loc);
		writeD(_loc.getHeading());
	}
}