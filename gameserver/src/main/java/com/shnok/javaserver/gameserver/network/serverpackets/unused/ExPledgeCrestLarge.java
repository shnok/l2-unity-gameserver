package com.shnok.javaserver.gameserver.network.serverpackets.unused;

import com.shnok.javaserver.gameserver.network.serverpackets.L2GameServerPacket;

public class ExPledgeCrestLarge extends L2GameServerPacket
{
	private final int _crestId;
	private final byte[] _data;
	
	public ExPledgeCrestLarge(int crestId, byte[] data)
	{
		_crestId = crestId;
		_data = data;
	}
	
	@Override
	protected void writeImpl()
	{
		writeC(0xfe);
		writeH(0x28);
		
		writeD(0x00); // ???
		writeD(_crestId);
		
		if (_data.length > 0)
		{
			writeD(_data.length);
			writeB(_data);
		}
		else
			writeD(0x00);
	}
}