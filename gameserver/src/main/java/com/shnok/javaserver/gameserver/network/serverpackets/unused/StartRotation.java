package com.shnok.javaserver.gameserver.network.serverpackets.unused;

import com.shnok.javaserver.gameserver.network.serverpackets.L2GameServerPacket;

public class StartRotation extends L2GameServerPacket
{
	private final int _charObjId;
	private final int _degree;
	private final int _side;
	private final int _speed;
	
	public StartRotation(int objId, int degree, int side, int speed)
	{
		_charObjId = objId;
		_degree = degree;
		_side = side;
		_speed = speed;
	}
	
	@Override
	protected final void writeImpl()
	{
		writeC(0x62);
		writeD(_charObjId);
		writeD(_degree);
		writeD(_side);
		writeD(_speed);
	}
}