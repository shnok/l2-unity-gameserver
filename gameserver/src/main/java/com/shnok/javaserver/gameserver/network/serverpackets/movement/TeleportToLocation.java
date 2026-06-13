package com.shnok.javaserver.gameserver.network.serverpackets.movement;

import com.shnok.javaserver.gameserver.model.WorldObject;
import com.shnok.javaserver.gameserver.network.serverpackets.L2GameServerPacket;

public class TeleportToLocation extends L2GameServerPacket
{
	private final int _objectId;
	private final int _x;
	private final int _y;
	private final int _z;
	private final boolean _isFastTeleport;
	
	public TeleportToLocation(WorldObject object, int x, int y, int z, boolean isFastTeleport)
	{
		_objectId = object.getObjectId();
		_x = x;
		_y = y;
		_z = z;
		_isFastTeleport = isFastTeleport;
	}
	
	@Override
	protected final void writeImpl()
	{
		writeC(0x28);
		writeD(_objectId);
		writeD(_x);
		writeD(_y);
		writeD(_z);
		writeD(_isFastTeleport ? 1 : 0); // 0 - with black screen, 1 - fast teleport (for correcting position)
	}
}