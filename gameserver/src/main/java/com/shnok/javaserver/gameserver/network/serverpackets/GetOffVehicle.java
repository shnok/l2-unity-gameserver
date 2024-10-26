package com.shnok.javaserver.gameserver.network.serverpackets;

public class GetOffVehicle extends L2GameServerPacket
{
	private final int _charObjId;
	private final int _boatObjId;
	private final int _x;
	private final int _y;
	private final int _z;
	
	public GetOffVehicle(int charObjId, int boatObjId, int x, int y, int z)
	{
		_charObjId = charObjId;
		_boatObjId = boatObjId;
		_x = x;
		_y = y;
		_z = z;
	}
	
	@Override
	protected void writeImpl()
	{
		writeC(0x5D);
		writeD(_charObjId);
		writeD(_boatObjId);
		writeD(_x);
		writeD(_y);
		writeD(_z);
	}
}