package com.shnok.javaserver.gameserver.handler.itemhandlers;

import net.sf.l2j.gameserver.data.manager.CastleManager;
import net.sf.l2j.gameserver.data.manager.SevenSignsManager;
import net.sf.l2j.gameserver.enums.SealType;
import net.sf.l2j.gameserver.handler.IItemHandler;
import net.sf.l2j.gameserver.model.actor.Playable;
import net.sf.l2j.gameserver.model.actor.Player;
import net.sf.l2j.gameserver.model.item.MercenaryTicket;
import net.sf.l2j.gameserver.model.item.instance.ItemInstance;
import net.sf.l2j.gameserver.model.residence.castle.Castle;
import net.sf.l2j.gameserver.network.SystemMessageId;
import net.sf.l2j.gameserver.network.serverpackets.SystemMessage;

/**
 * Handler to use mercenary tickets.<br>
 * <br>
 * Check constraints:
 * <ul>
 * <li>Only specific tickets may be used in each castle (different tickets for each castle)</li>
 * <li>Only the owner of that castle may use them</li>
 * <li>tickets cannot be used during siege</li>
 * <li>Check if max number of tickets from this ticket's TYPE has been reached</li>
 * </ul>
 * If allowed, spawn the item in the world and remove it from the player's inventory.
 */
public class MercenaryTickets implements IItemHandler
{
	@Override
	public void useItem(Playable playable, ItemInstance item, boolean forceUse)
	{
		final Player player = (Player) playable;
		if (player == null)
			return;
		
		final Castle castle = CastleManager.getInstance().getCastle(player);
		if (castle == null)
			return;
		
		// Castle lord check.
		if (!player.isCastleLord(castle.getId()))
		{
			player.sendPacket(SystemMessageId.YOU_DO_NOT_HAVE_AUTHORITY_TO_POSITION_MERCENARIES);
			return;
		}
		
		final int itemId = item.getItemId();
		final MercenaryTicket ticket = castle.getTicket(itemId);
		
		// Valid ticket for castle check.
		if (ticket == null)
		{
			player.sendPacket(SystemMessageId.MERCENARIES_CANNOT_BE_POSITIONED_HERE);
			return;
		}
		
		// Siege in progress check.
		if (castle.getSiege().isInProgress())
		{
			player.sendPacket(SystemMessageId.THIS_MERCENARY_CANNOT_BE_POSITIONED_ANYMORE);
			return;
		}
		
		// Seal validation check.
		if (!SevenSignsManager.getInstance().isSealValidationPeriod())
		{
			player.sendPacket(SystemMessageId.MERC_CAN_BE_ASSIGNED);
			return;
		}
		
		// Seal of Strife owner check.
		if (!ticket.isSsqType(SevenSignsManager.getInstance().getSealOwner(SealType.STRIFE)))
		{
			player.sendPacket(SystemMessageId.MERC_CANT_BE_ASSIGNED_USING_STRIFE);
			return;
		}
		
		// Max amount check.
		if (castle.getDroppedTicketsCount(itemId) >= ticket.getMaxAmount())
		{
			player.sendPacket(SystemMessageId.THIS_MERCENARY_CANNOT_BE_POSITIONED_ANYMORE);
			return;
		}
		
		// Distance check.
		if (castle.isTooCloseFromDroppedTicket(player.getX(), player.getY(), player.getZ()))
		{
			player.sendPacket(SystemMessageId.POSITIONING_CANNOT_BE_DONE_BECAUSE_DISTANCE_BETWEEN_MERCENARIES_TOO_SHORT);
			return;
		}
		
		final ItemInstance droppedTicket = player.dropItem(item.getObjectId(), 1, player.getX(), player.getY(), player.getZ(), false);
		if (droppedTicket == null)
			return;
		
		castle.addDroppedTicket(droppedTicket);
		
		player.sendPacket(SystemMessage.getSystemMessage(SystemMessageId.PLACE_S1_IN_CURRENT_LOCATION_AND_DIRECTION).addItemName(itemId));
	}
}