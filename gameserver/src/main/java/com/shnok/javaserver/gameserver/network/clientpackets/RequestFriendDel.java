package com.shnok.javaserver.gameserver.network.clientpackets;

import net.sf.l2j.gameserver.data.manager.RelationManager;
import net.sf.l2j.gameserver.data.sql.PlayerInfoTable;
import net.sf.l2j.gameserver.model.World;
import net.sf.l2j.gameserver.model.actor.Player;
import net.sf.l2j.gameserver.network.SystemMessageId;
import net.sf.l2j.gameserver.network.serverpackets.L2Friend;

public final class RequestFriendDel extends L2GameClientPacket
{
	private String _targetName;
	
	@Override
	protected void readImpl()
	{
		_targetName = readS();
	}
	
	@Override
	protected void runImpl()
	{
		final Player player = getClient().getPlayer();
		if (player == null)
			return;
		
		final int playerId = player.getObjectId();
		final int targetId = PlayerInfoTable.getInstance().getPlayerObjectId(_targetName);
		
		if (targetId == -1 || !RelationManager.getInstance().areFriends(playerId, targetId))
		{
			player.sendPacket(SystemMessageId.THE_USER_NOT_IN_FRIENDS_LIST);
			return;
		}
		
		RelationManager.getInstance().removeFromFriendList(player, targetId);
		
		final Player target = World.getInstance().getPlayer(_targetName);
		if (target != null)
		{
			player.sendPacket(new L2Friend(target, 3));
			target.sendPacket(new L2Friend(player, 3));
			RelationManager.getInstance().removeFromFriendList(target, playerId);
		}
		else
			player.sendPacket(new L2Friend(_targetName, 3));
	}
}