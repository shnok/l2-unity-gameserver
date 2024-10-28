package com.shnok.javaserver.gameserver.network.serverpackets.unused;

import com.shnok.javaserver.gameserver.model.actor.Player;
import com.shnok.javaserver.gameserver.network.serverpackets.L2GameServerPacket;

public class L2FriendStatus extends L2GameServerPacket
{
	private final int _isOnline;
	private final String _name;
	private final int _objectId;
	
	public L2FriendStatus(Player player, boolean isOnline)
	{
		_isOnline = isOnline ? 1 : 0;
		_name = player.getName();
		_objectId = player.getObjectId();
	}
	
	@Override
	protected final void writeImpl()
	{
		writeC(0xfc);
		writeD(_isOnline);
		writeS(_name);
		writeD(_objectId);
	}
}