package com.shnok.javaserver.gameserver.network.clientpackets;

import com.shnok.javaserver.gameserver.data.xml.HennaData;
import com.shnok.javaserver.gameserver.model.actor.Player;
import com.shnok.javaserver.gameserver.model.item.instance.ItemInstance;
import com.shnok.javaserver.gameserver.model.records.Henna;
import com.shnok.javaserver.gameserver.network.SystemMessageId;
import com.shnok.javaserver.gameserver.network.serverpackets.HennaInfo;
import com.shnok.javaserver.gameserver.network.serverpackets.UserInfo;

public final class RequestHennaEquip extends L2GameClientPacket
{
	private int _symbolId;
	
	@Override
	protected void readImpl()
	{
		_symbolId = readD();
	}
	
	@Override
	protected void runImpl()
	{
		final Player player = getClient().getPlayer();
		if (player == null)
			return;
		
		final Henna henna = HennaData.getInstance().getHenna(_symbolId);
		if (henna == null)
			return;
		
		if (!henna.canBeUsedBy(player))
		{
			player.sendPacket(SystemMessageId.CANT_DRAW_SYMBOL);
			return;
		}
		
		if (player.getHennaList().isFull())
		{
			player.sendPacket(SystemMessageId.SYMBOLS_FULL);
			return;
		}
		
		final ItemInstance ownedDyes = player.getInventory().getItemByItemId(henna.dyeId());
		final int count = (ownedDyes == null) ? 0 : ownedDyes.getCount();
		
		if (count < Henna.DRAW_AMOUNT)
		{
			player.sendPacket(SystemMessageId.CANT_DRAW_SYMBOL);
			return;
		}
		
		// reduceAdena sends a message.
		if (!player.reduceAdena(henna.drawPrice(), true))
			return;
		
		// destroyItemByItemId sends a message.
		if (!player.destroyItemByItemId(henna.dyeId(), Henna.DRAW_AMOUNT, true))
			return;
		
		final boolean success = player.getHennaList().add(henna);
		if (success)
		{
			player.sendPacket(new HennaInfo(player));
			player.sendPacket(new UserInfo(player));
			player.sendPacket(SystemMessageId.SYMBOL_ADDED);
		}
	}
}