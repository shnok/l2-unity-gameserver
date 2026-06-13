package com.shnok.javaserver.gameserver.network.serverpackets.unused;

import com.shnok.javaserver.gameserver.network.serverpackets.L2GameServerPacket;

public class PledgePowerGradeList extends L2GameServerPacket
{
	private final int[] _membersPerRank;
	
	public PledgePowerGradeList(int[] membersPerRank)
	{
		_membersPerRank = membersPerRank;
	}
	
	@Override
	protected final void writeImpl()
	{
		writeC(0xFE);
		writeH(0x3b);
		
		writeD(9);
		for (int i = 1; i < 10; i++)
		{
			writeD(i);
			writeD(_membersPerRank[i]);
		}
	}
}