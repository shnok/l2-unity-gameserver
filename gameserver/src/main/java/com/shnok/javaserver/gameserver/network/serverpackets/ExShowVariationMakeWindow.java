package com.shnok.javaserver.gameserver.network.serverpackets;

public class ExShowVariationMakeWindow extends L2GameServerPacket
{
	public static final ExShowVariationMakeWindow STATIC_PACKET = new ExShowVariationMakeWindow();
	
	private ExShowVariationMakeWindow()
	{
	}
	
	@Override
	protected void writeImpl()
	{
		writeC(0xfe);
		writeH(0x50);
	}
}