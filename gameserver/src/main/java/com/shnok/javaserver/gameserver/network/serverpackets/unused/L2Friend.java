package com.shnok.javaserver.gameserver.network.serverpackets.unused;

import com.shnok.javaserver.gameserver.model.actor.Player;
import com.shnok.javaserver.gameserver.network.serverpackets.L2GameServerPacket;

public class L2Friend extends L2GameServerPacket
{
	private int _action; // 1 - add, 2 - modify, 3 - remove.
	private String _name;
	private int _objectId;
	private int _isOnline;
	
	public L2Friend(Player player, int action)
	{
		_action = action;
		_name = player.getName();
		_objectId = player.getObjectId();
		_isOnline = player.isOnline() ? 1 : 0;
	}
	
	public L2Friend(String name, int action)
	{
		_action = action;
		_name = name;
		_objectId = 0;
		_isOnline = 0;
	}
	
	@Override
	protected final void writeImpl()
	{
		writeC(0xfb);
		writeD(_action);
		writeD(0x00); // (Character PK) Not zero. Unknown.
		writeS(_name);
		writeD(_isOnline);
		writeD(_objectId);
	}
}