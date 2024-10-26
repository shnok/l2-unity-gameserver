package com.shnok.javaserver.gameserver.network.serverpackets;

public class ExShowVariationCancelWindow extends L2GameServerPacket
{
	public static final ExShowVariationCancelWindow STATIC_PACKET = new ExShowVariationCancelWindow();
	
	private ExShowVariationCancelWindow()
	{
	}
	
	@Override
	protected void writeImpl()
	{
		writeC(0xfe);
		writeH(0x51);
	}
}