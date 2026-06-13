package com.shnok.javaserver.gameserver.network.serverpackets.unused;

import java.util.List;

import com.shnok.javaserver.commons.lang.StringUtil;
import com.shnok.javaserver.gameserver.network.serverpackets.L2GameServerPacket;

public class ShowBoard extends L2GameServerPacket
{
	public static final ShowBoard STATIC_SHOWBOARD_102 = new ShowBoard(null, "102");
	public static final ShowBoard STATIC_SHOWBOARD_103 = new ShowBoard(null, "103");
	
	public static final ShowBoard STATIC_CLOSE = new ShowBoard();
	
	private static final String TOP = "bypass _bbshome";
	private static final String FAV = "bypass _bbsgetfav";
	private static final String REGION = "bypass _bbsloc";
	private static final String CLAN = "bypass _bbsclan";
	private static final String MEMO = "bypass _bbsmemo";
	private static final String MAIL = "bypass _maillist_0_1_0_";
	private static final String FRIENDS = "bypass _friendlist_0_";
	private static final String ADDFAV = "bypass _bbsgetfav_add";
	
	private final StringBuilder _htmlCode = new StringBuilder();
	
	private boolean _canShow;
	
	public ShowBoard(String htmlCode, String id)
	{
		_canShow = true;
		
		StringUtil.append(_htmlCode, id, "\u0008", htmlCode);
	}
	
	public ShowBoard(List<String> arg)
	{
		_canShow = true;
		
		_htmlCode.append("1002\u0008");
		for (String str : arg)
			StringUtil.append(_htmlCode, str, " \u0008");
	}
	
	public ShowBoard()
	{
	}
	
	@Override
	protected final void writeImpl()
	{
		writeC(0x6e);
		writeC(_canShow ? 0x01 : 0x00);
		
		if (_canShow)
		{
			writeS(TOP);
			writeS(FAV);
			writeS(REGION);
			writeS(CLAN);
			writeS(MEMO);
			writeS(MAIL);
			writeS(FRIENDS);
			writeS(ADDFAV);
			writeS(_htmlCode.toString());
		}
	}
}