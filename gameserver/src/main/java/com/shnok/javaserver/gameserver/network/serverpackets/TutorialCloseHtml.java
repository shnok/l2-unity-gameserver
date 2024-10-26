package com.shnok.javaserver.gameserver.network.serverpackets;

public class TutorialCloseHtml extends L2GameServerPacket
{
	public static final TutorialCloseHtml STATIC_PACKET = new TutorialCloseHtml();
	
	private TutorialCloseHtml()
	{
	}
	
	@Override
	protected void writeImpl()
	{
		writeC(0xa3);
	}
}