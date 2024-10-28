package com.shnok.javaserver.gameserver.network.serverpackets.unused;

import com.shnok.javaserver.gameserver.model.actor.Player;
import com.shnok.javaserver.gameserver.network.serverpackets.L2GameServerPacket;

public class PrivateStoreMsgBuy extends L2GameServerPacket
{
	private final Player _player;
	private String _message;
	
	public PrivateStoreMsgBuy(Player player)
	{
		_player = player;
		
		if (_player.getBuyList() != null)
			_message = _player.getBuyList().getTitle();
	}
	
	@Override
	protected final void writeImpl()
	{
		writeC(0xb9);
		
		writeD(_player.getObjectId());
		writeS(_message);
	}
}