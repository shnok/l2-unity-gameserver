package com.shnok.javaserver.gameserver.network.serverpackets.combat;

import com.shnok.javaserver.gameserver.model.WorldObject;
import com.shnok.javaserver.gameserver.network.serverpackets.L2GameServerPacket;

/**
 * format d
 */
public class Revive extends L2GameServerPacket
{
	private final int _objectId;
	
	public Revive(WorldObject obj)
	{
		_objectId = obj.getObjectId();
	}
	
	@Override
	protected final void writeImpl()
	{
		writeC(0x07);
		writeD(_objectId);
	}
}