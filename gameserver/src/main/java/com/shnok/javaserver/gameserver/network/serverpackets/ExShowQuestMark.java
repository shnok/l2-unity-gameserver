package com.shnok.javaserver.gameserver.network.serverpackets;

public class ExShowQuestMark extends L2GameServerPacket
{
	private final int _questId;
	
	public ExShowQuestMark(int questId)
	{
		_questId = questId;
	}
	
	@Override
	protected void writeImpl()
	{
		writeC(0xfe);
		writeH(0x1a);
		writeD(_questId);
	}
}