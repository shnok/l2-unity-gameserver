package com.shnok.javaserver.gameserver.network.serverpackets;

public class ExSendManorList extends L2GameServerPacket
{
	public static final ExSendManorList STATIC_PACKET = new ExSendManorList();
	
	private ExSendManorList()
	{
	}
	
	private static final String[] MANORS =
	{
		"gludio",
		"dion",
		"giran",
		"oren",
		"aden",
		"innadril",
		"goddard",
		"rune",
		"schuttgart"
	};
	
	@Override
	protected void writeImpl()
	{
		writeC(0xFE);
		writeH(0x1B);
		writeD(MANORS.length);
		for (int i = 0; i < MANORS.length; i++)
		{
			writeD(i + 1);
			writeS(MANORS[i]);
		}
	}
}