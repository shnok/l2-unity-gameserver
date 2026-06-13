package com.shnok.javaserver.gameserver.handler.itemhandlers;

import com.shnok.javaserver.gameserver.data.xml.DoorData;
import com.shnok.javaserver.gameserver.handler.IItemHandler;
import com.shnok.javaserver.gameserver.model.WorldObject;
import com.shnok.javaserver.gameserver.model.actor.Npc;
import com.shnok.javaserver.gameserver.model.actor.Playable;
import com.shnok.javaserver.gameserver.model.actor.Player;
import com.shnok.javaserver.gameserver.model.actor.instance.Door;
import com.shnok.javaserver.gameserver.model.item.instance.ItemInstance;
import com.shnok.javaserver.gameserver.network.SystemMessageId;
import com.shnok.javaserver.gameserver.network.serverpackets.combat.ActionFailed;
import com.shnok.javaserver.gameserver.network.serverpackets.SystemMessage;

public class PaganKeys implements IItemHandler
{
	@Override
	public void useItem(Playable playable, ItemInstance item, boolean forceUse)
	{
		if (!(playable instanceof Player player))
			return;
		
		final WorldObject target = player.getTarget();
		
		if (!(target instanceof Door targetDoor))
		{
			player.sendPacket(SystemMessageId.INVALID_TARGET);
			player.sendPacket(ActionFailed.STATIC_PACKET);
			return;
		}
		
		if (!player.isIn3DRadius(targetDoor, Npc.INTERACTION_DISTANCE))
		{
			player.sendPacket(SystemMessageId.DIST_TOO_FAR_CASTING_STOPPED);
			player.sendPacket(ActionFailed.STATIC_PACKET);
			return;
		}
		
		if (!playable.destroyItem(item.getObjectId(), 1, true))
			return;
		
		final int doorId = targetDoor.getDoorId();
		
		switch (item.getItemId())
		{
			case 8056:
				if (doorId == 23150004 || doorId == 23150003)
				{
					DoorData.getInstance().getDoor(23150003).openMe();
					DoorData.getInstance().getDoor(23150004).openMe();
				}
				else
					player.sendPacket(SystemMessage.getSystemMessage(SystemMessageId.S1_CANNOT_BE_USED).addItemName(8056));
				break;
			
			case 8273:
				switch (doorId)
				{
					case 19160002, 19160003, 19160004, 19160005, 19160006, 19160007, 19160008, 19160009:
						DoorData.getInstance().getDoor(doorId).openMe();
						break;
					
					default:
						player.sendPacket(SystemMessage.getSystemMessage(SystemMessageId.S1_CANNOT_BE_USED).addItemName(8273));
						break;
				}
				break;
			
			case 8275:
				switch (doorId)
				{
					case 19160012, 19160013:
						DoorData.getInstance().getDoor(doorId).openMe();
						break;
					
					default:
						player.sendPacket(SystemMessage.getSystemMessage(SystemMessageId.S1_CANNOT_BE_USED).addItemName(8275));
						break;
				}
				break;
		}
	}
}