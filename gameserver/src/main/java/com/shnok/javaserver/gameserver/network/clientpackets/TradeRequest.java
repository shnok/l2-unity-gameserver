package com.shnok.javaserver.gameserver.network.clientpackets;

import net.sf.l2j.Config;
import net.sf.l2j.gameserver.data.manager.RelationManager;
import net.sf.l2j.gameserver.model.World;
import net.sf.l2j.gameserver.model.actor.Player;
import net.sf.l2j.gameserver.network.SystemMessageId;
import net.sf.l2j.gameserver.network.serverpackets.SendTradeRequest;
import net.sf.l2j.gameserver.network.serverpackets.SystemMessage;

public final class TradeRequest extends L2GameClientPacket
{
	private int _objectId;
	
	@Override
	protected void readImpl()
	{
		_objectId = readD();
	}
	
	@Override
	protected void runImpl()
	{
		final Player player = getClient().getPlayer();
		if (player == null)
			return;
		
		if (!player.getAccessLevel().allowTransaction())
		{
			player.sendPacket(SystemMessageId.YOU_ARE_NOT_AUTHORIZED_TO_DO_THAT);
			return;
		}
		
		final Player target = World.getInstance().getPlayer(_objectId);
		if (target == null)
			return;
		
		if (!player.knows(target) || target == player)
		{
			player.sendPacket(SystemMessageId.TARGET_IS_INCORRECT);
			return;
		}
		
		if (target.isInOlympiadMode() || player.isInOlympiadMode())
		{
			player.sendMessage("You cannot trade during Olympiad.");
			return;
		}
		
		if (!Config.KARMA_PLAYER_CAN_TRADE && (player.getKarma() > 0 || target.getKarma() > 0))
		{
			player.sendMessage("You cannot trade in a chaotic state.");
			return;
		}
		
		if (player.isInManageStoreMode() || target.isInManageStoreMode())
		{
			player.sendPacket(SystemMessageId.PRIVATE_STORE_UNDER_WAY);
			return;
		}
		
		if (player.isInStoreMode() || target.isInStoreMode())
		{
			player.sendPacket(SystemMessageId.CANNOT_TRADE_DISCARD_DROP_ITEM_WHILE_IN_SHOPMODE);
			return;
		}
		
		if (player.isProcessingTransaction())
		{
			player.sendPacket(SystemMessageId.ALREADY_TRADING);
			return;
		}
		
		if (target.isProcessingRequest() || target.isProcessingTransaction())
		{
			player.sendPacket(SystemMessage.getSystemMessage(SystemMessageId.S1_IS_BUSY_TRY_LATER).addCharName(target));
			return;
		}
		
		if (target.isBlockingAll())
		{
			player.sendPacket(SystemMessage.getSystemMessage(SystemMessageId.S1_BLOCKED_EVERYTHING).addCharName(target));
			return;
		}
		
		if (RelationManager.getInstance().isInBlockList(target, player))
		{
			player.sendPacket(SystemMessage.getSystemMessage(SystemMessageId.S1_HAS_ADDED_YOU_TO_IGNORE_LIST).addCharName(target));
			return;
		}
		
		player.onTransactionRequest(target);
		target.sendPacket(new SendTradeRequest(player.getObjectId()));
		player.sendPacket(SystemMessage.getSystemMessage(SystemMessageId.REQUEST_S1_FOR_TRADE).addCharName(target));
	}
}