package com.shnok.javaserver.gameserver.network.serverpackets.unused;

import com.shnok.javaserver.gameserver.model.actor.Creature;
import com.shnok.javaserver.gameserver.network.serverpackets.L2GameServerPacket;

public class TitleUpdate extends L2GameServerPacket
{
	private final String _title;
	private final int _objectId;
	
	public TitleUpdate(Creature cha)
	{
		_objectId = cha.getObjectId();
		_title = cha.getTitle();
	}
	
	@Override
	protected void writeImpl()
	{
		writeC(0xcc);
		writeD(_objectId);
		writeS(_title);
	}
}