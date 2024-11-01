package com.shnok.javaserver.gameserver.network.serverpackets.combat;

import com.shnok.javaserver.gameserver.network.serverpackets.L2GameServerPacket;

public class TargetSelected extends L2GameServerPacket
{
	private final int _objectId;
	private final int _targetObjId;
	private final int _x;
	private final int _y;
	private final int _z;
	
	public TargetSelected(int objectId, int targetId, int x, int y, int z)
	{
		_objectId = objectId;
		_targetObjId = targetId;
		_x = x;
		_y = y;
		_z = z;
	}
	
	@Override
	protected final void writeImpl()
	{
		writeC(0x29);
		writeD(_objectId);
		writeD(_targetObjId);
		writeD(_x);
		writeD(_y);
		writeD(_z);
	}
}