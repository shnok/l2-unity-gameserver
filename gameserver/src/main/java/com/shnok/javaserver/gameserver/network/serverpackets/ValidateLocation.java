package com.shnok.javaserver.gameserver.network.serverpackets;

import net.sf.l2j.gameserver.model.actor.Creature;
import net.sf.l2j.gameserver.model.location.Location;

public class ValidateLocation extends L2GameServerPacket
{
	private final int _objectId;
	private final Location _loc;
	private final int _heading;
	
	public ValidateLocation(Creature creature)
	{
		_objectId = creature.getObjectId();
		_loc = creature.getPosition();
		_heading = creature.getHeading();
	}
	
	@Override
	protected final void writeImpl()
	{
		writeC(0x61);
		writeD(_objectId);
		writeLoc(_loc);
		writeD(_heading);
	}
}