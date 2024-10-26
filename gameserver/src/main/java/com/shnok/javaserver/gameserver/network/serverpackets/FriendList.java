package com.shnok.javaserver.gameserver.network.serverpackets;

import java.util.ArrayList;
import java.util.List;

import com.shnok.javaserver.gameserver.data.manager.RelationManager;
import com.shnok.javaserver.gameserver.data.sql.PlayerInfoTable;
import com.shnok.javaserver.gameserver.model.World;
import com.shnok.javaserver.gameserver.model.actor.Player;

public class FriendList extends L2GameServerPacket
{
	private final List<FriendInfo> _info = new ArrayList<>(0);
	
	private static class FriendInfo
	{
		private final int _objId;
		private final String _name;
		private final boolean _online;
		
		public FriendInfo(int objId, String name, boolean online)
		{
			_objId = objId;
			_name = name;
			_online = online;
		}
	}
	
	public FriendList(Player player)
	{
		for (int objId : RelationManager.getInstance().getFriendList(player.getObjectId()))
		{
			final String name = PlayerInfoTable.getInstance().getPlayerName(objId);
			final Player player1 = World.getInstance().getPlayer(objId);
			
			_info.add(new FriendInfo(objId, name, (player1 != null && player1.isOnline())));
		}
	}
	
	@Override
	protected final void writeImpl()
	{
		writeC(0xfa);
		writeD(_info.size());
		for (FriendInfo info : _info)
		{
			writeD(info._objId);
			writeS(info._name);
			writeD(info._online ? 0x01 : 0x00);
			writeD(info._online ? info._objId : 0x00);
		}
	}
}