package com.shnok.javaserver.gameserver.handler.itemhandlers;

import com.shnok.javaserver.commons.random.Rnd;

import com.shnok.javaserver.gameserver.enums.FloodProtector;
import com.shnok.javaserver.gameserver.handler.IItemHandler;
import com.shnok.javaserver.gameserver.model.actor.Playable;
import com.shnok.javaserver.gameserver.model.actor.Player;
import com.shnok.javaserver.gameserver.model.item.instance.ItemInstance;
import com.shnok.javaserver.gameserver.network.SystemMessageId;
import com.shnok.javaserver.gameserver.network.serverpackets.Dice;
import com.shnok.javaserver.gameserver.network.serverpackets.SystemMessage;

public class RollingDices implements IItemHandler
{
	@Override
	public void useItem(Playable playable, ItemInstance item, boolean forceUse)
	{
		if (!(playable instanceof Player player))
			return;
		
		if (!player.getClient().performAction(FloodProtector.ROLL_DICE))
		{
			player.sendPacket(SystemMessageId.YOU_MAY_NOT_THROW_THE_DICE_AT_THIS_TIME_TRY_AGAIN_LATER);
			return;
		}
		
		final int number = Rnd.get(1, 6);
		
		player.broadcastPacket(new Dice(player, item.getItemId(), number));
		player.broadcastPacket(SystemMessage.getSystemMessage(SystemMessageId.S1_ROLLED_S2).addCharName(player).addNumber(number));
	}
}