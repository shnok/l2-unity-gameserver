package com.shnok.javaserver.gameserver.handler.itemhandlers;

import com.shnok.javaserver.gameserver.handler.IItemHandler;
import com.shnok.javaserver.gameserver.model.actor.Playable;
import com.shnok.javaserver.gameserver.model.actor.Player;
import com.shnok.javaserver.gameserver.model.item.instance.ItemInstance;
import com.shnok.javaserver.gameserver.network.serverpackets.unused.ShowXMasSeal;

public class SpecialXMas implements IItemHandler
{
	@Override
	public void useItem(Playable playable, ItemInstance item, boolean forceUse)
	{
		if (!(playable instanceof Player player))
			return;
		
		player.sendPacket(new ShowXMasSeal(item.getItemId()));
	}
}