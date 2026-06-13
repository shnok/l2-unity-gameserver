package com.shnok.javaserver.gameserver.network.serverpackets.unused;

import com.shnok.javaserver.gameserver.model.location.Location;
import com.shnok.javaserver.gameserver.network.serverpackets.L2GameServerPacket;

public class RadarControl extends L2GameServerPacket
{
	private final int _showRadar;
	private final int _type;
	private final int _x;
	private final int _y;
	private final int _z;
	
	public RadarControl(int showRadar, int type, int x, int y, int z)
	{
		_showRadar = showRadar;
		_type = type;
		_x = x;
		_y = y;
		_z = z;
	}
	
	public RadarControl(int showRadar, int type, Location loc)
	{
		this(showRadar, type, loc.getX(), loc.getY(), loc.getZ());
	}
	
	@Override
	protected final void writeImpl()
	{
		writeC(0xEB);
		writeD(_showRadar);
		writeD(_type);
		writeD(_x);
		writeD(_y);
		writeD(_z);
	}
}