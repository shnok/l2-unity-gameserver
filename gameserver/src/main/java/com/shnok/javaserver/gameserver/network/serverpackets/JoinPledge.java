package com.shnok.javaserver.gameserver.network.serverpackets;

public class JoinPledge extends L2GameServerPacket
{
	private final int _pledgeId;
	
	public JoinPledge(int pledgeId)
	{
		_pledgeId = pledgeId;
	}
	
	@Override
	protected final void writeImpl()
	{
		writeC(0x33);
		writeD(_pledgeId);
	}
}