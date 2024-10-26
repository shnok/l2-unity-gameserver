package com.shnok.javaserver.gameserver.network.serverpackets;

public class ShowPCCafeCouponShowUI extends L2GameServerPacket
{
	public static final ShowPCCafeCouponShowUI STATIC_PACKET = new ShowPCCafeCouponShowUI();
	
	private ShowPCCafeCouponShowUI()
	{
	}
	
	@Override
	protected void writeImpl()
	{
		writeC(0xfe);
		writeH(0x43);
	}
}