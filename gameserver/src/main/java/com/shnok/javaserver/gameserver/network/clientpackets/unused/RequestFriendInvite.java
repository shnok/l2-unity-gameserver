package com.shnok.javaserver.gameserver.network.clientpackets.unused;

import com.shnok.javaserver.gameserver.data.manager.RelationManager;
import com.shnok.javaserver.gameserver.model.World;
import com.shnok.javaserver.gameserver.model.actor.Player;
import com.shnok.javaserver.gameserver.network.SystemMessageId;
import com.shnok.javaserver.gameserver.network.clientpackets.L2GameClientPacket;
import com.shnok.javaserver.gameserver.network.serverpackets.unused.FriendAddRequest;
import com.shnok.javaserver.gameserver.network.serverpackets.unused.FriendAddRequestResult;
import com.shnok.javaserver.gameserver.network.serverpackets.SystemMessage;

public final class RequestFriendInvite extends L2GameClientPacket
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
		
		final Player target = World.getInstance().getPlayer(_targetName);
		if (target == null || !target.isOnline())
		{
			player.sendPacket(SystemMessageId.TARGET_IS_NOT_FOUND_IN_THE_GAME);
			player.sendPacket(FriendAddRequestResult.STATIC_FAIL);
			return;
		}
		
		if (target == player)
		{
			player.sendPacket(SystemMessageId.YOU_CANNOT_ADD_YOURSELF_TO_YOUR_OWN_FRIENDS_LIST);
			player.sendPacket(FriendAddRequestResult.STATIC_FAIL);
			return;
		}
		
		if (target.isBlockingAll())
		{
			player.sendPacket(SystemMessage.getSystemMessage(SystemMessageId.S1_BLOCKED_EVERYTHING).addString(_targetName));
			player.sendPacket(FriendAddRequestResult.STATIC_FAIL);
			return;
		}
		
		if (!player.isGM() && target.isGM())
		{
			player.sendPacket(SystemMessageId.THE_PLAYER_IS_REJECTING_FRIEND_INVITATIONS);
			player.sendPacket(FriendAddRequestResult.STATIC_FAIL);
			return;
		}
		
		if (RelationManager.getInstance().isInBlockList(target, player))
		{
			player.sendPacket(SystemMessage.getSystemMessage(SystemMessageId.S1_HAS_ADDED_YOU_TO_IGNORE_LIST2).addString(_targetName));
			player.sendPacket(FriendAddRequestResult.STATIC_FAIL);
			return;
		}
		
		if (RelationManager.getInstance().areFriends(player.getObjectId(), target.getObjectId()))
		{
			player.sendPacket(SystemMessage.getSystemMessage(SystemMessageId.S1_ALREADY_IN_FRIENDS_LIST).addString(_targetName));
			player.sendPacket(FriendAddRequestResult.STATIC_FAIL);
			return;
		}
		
		if (target.isProcessingRequest())
		{
			player.sendPacket(SystemMessageId.WAITING_FOR_ANOTHER_REPLY);
			player.sendPacket(FriendAddRequestResult.STATIC_FAIL);
			return;
		}
		
		player.onTransactionRequest(target);
		target.sendPacket(new FriendAddRequest(player.getName()));
	}
}