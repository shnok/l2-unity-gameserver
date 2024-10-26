package com.shnok.javaserver.gameserver.network.serverpackets;

import net.sf.l2j.gameserver.data.sql.PlayerInfoTable;
import net.sf.l2j.gameserver.model.World;

public class FriendStatus extends L2GameServerPacket
{
	private final boolean _isOnline;
	private final String _name;
	private final int _objectId;
	
	public FriendStatus(int objectId)
	{
		_isOnline = World.getInstance().getPlayer(objectId) != null;
		_name = PlayerInfoTable.getInstance().getPlayerName(objectId);
		_objectId = objectId;
	}
	
	@Override
	protected final void writeImpl()
	{
		writeC(0x7b);
		writeD((_isOnline) ? 1 : 0);
		writeS(_name);
		writeD(_objectId);
	}
}