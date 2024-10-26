package com.shnok.javaserver.gameserver.network.clientpackets;

import net.sf.l2j.gameserver.model.World;
import net.sf.l2j.gameserver.model.actor.Player;
import net.sf.l2j.gameserver.model.item.instance.ItemInstance;
import net.sf.l2j.gameserver.model.trade.TradeItem;
import net.sf.l2j.gameserver.model.trade.TradeList;
import net.sf.l2j.gameserver.network.SystemMessageId;
import net.sf.l2j.gameserver.network.serverpackets.TradeItemUpdate;
import net.sf.l2j.gameserver.network.serverpackets.TradeOtherAdd;
import net.sf.l2j.gameserver.network.serverpackets.TradeOwnAdd;
import net.sf.l2j.gameserver.network.serverpackets.TradeUpdate;

public final class AddTradeItem extends L2GameClientPacket
{
	@SuppressWarnings("unused")
	private int _tradeId;
	private int _objectId;
	private int _count;
	
	@Override
	protected void readImpl()
	{
		_tradeId = readD();
		_objectId = readD();
		_count = readD();
	}
	
	@Override
	protected void runImpl()
	{
		final Player player = getClient().getPlayer();
		if (player == null)
			return;
		
		final TradeList tradeList = player.getActiveTradeList();
		if (tradeList == null)
			return;
		
		final Player partner = tradeList.getPartner();
		if (partner == null || World.getInstance().getPlayer(partner.getObjectId()) == null || partner.getActiveTradeList() == null)
		{
			player.sendPacket(SystemMessageId.TARGET_IS_NOT_FOUND_IN_THE_GAME);
			player.cancelActiveTrade();
			return;
		}
		
		if (!player.getAccessLevel().allowTransaction())
		{
			player.sendPacket(SystemMessageId.YOU_ARE_NOT_AUTHORIZED_TO_DO_THAT);
			player.cancelActiveTrade();
			return;
		}
		
		if (tradeList.isConfirmed())
		{
			player.sendPacket(SystemMessageId.ONCE_THE_TRADE_IS_CONFIRMED_THE_ITEM_CANNOT_BE_MOVED_AGAIN);
			return;
		}
		
		if (partner.getActiveTradeList().isConfirmed())
		{
			player.sendPacket(SystemMessageId.CANNOT_ADJUST_ITEMS_AFTER_TRADE_CONFIRMED);
			return;
		}
		
		final ItemInstance item = player.validateItemManipulation(_objectId);
		if (item == null)
		{
			player.sendPacket(SystemMessageId.NOTHING_HAPPENED);
			return;
		}
		
		final TradeItem tradeItem = tradeList.addItem(_objectId, _count, 0);
		if (tradeItem == null)
			return;
		
		player.sendPacket(new TradeOwnAdd(tradeItem, _count));
		player.sendPacket(new TradeUpdate(tradeItem, item.getCount() - tradeItem.getCount()));
		player.sendPacket(new TradeItemUpdate(tradeList, player));
		
		tradeList.getPartner().sendPacket(new TradeOtherAdd(tradeItem, _count));
	}
}