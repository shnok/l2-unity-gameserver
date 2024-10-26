package com.shnok.javaserver.gameserver.network.serverpackets;

import com.shnok.javaserver.gameserver.model.location.ObserverLocation;

public class ObserverStart extends L2GameServerPacket
{
	private final int _x;
	private final int _y;
	private final int _z;
	private final int _yaw;
	private final int _pitch;
	
	public ObserverStart(ObserverLocation loc)
	{
		_x = loc.getX();
		_y = loc.getY();
		_z = loc.getZ();
		_yaw = loc.getYaw();
		_pitch = loc.getPitch();
	}
	
	@Override
	protected final void writeImpl()
	{
		writeC(0xdf);
		writeD(_x);
		writeD(_y);
		writeD(_z);
		writeD(_yaw);
		writeD(_pitch);
	}
}