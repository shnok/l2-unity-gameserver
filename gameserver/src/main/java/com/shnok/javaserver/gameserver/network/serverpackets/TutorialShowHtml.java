package com.shnok.javaserver.gameserver.network.serverpackets;

public final class TutorialShowHtml extends L2GameServerPacket
{
	private final String _html;
	
	public TutorialShowHtml(String html)
	{
		_html = html;
	}
	
	@Override
	protected void writeImpl()
	{
		writeC(0xa0);
		writeS(_html);
	}
}