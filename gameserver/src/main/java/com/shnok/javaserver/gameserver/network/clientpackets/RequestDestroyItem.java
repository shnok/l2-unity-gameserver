package com.shnok.javaserver.gameserver.network.clientpackets;

import java.sql.Connection;
import java.sql.PreparedStatement;

import com.shnok.javaserver.commons.pool.ConnectionPool;

import com.shnok.javaserver.gameserver.data.manager.CursedWeaponManager;
import com.shnok.javaserver.gameserver.model.actor.Player;
import com.shnok.javaserver.gameserver.model.item.instance.ItemInstance;
import com.shnok.javaserver.gameserver.network.SystemMessageId;

public final class RequestDestroyItem extends L2GameClientPacket
{
	private static final String DELETE_PET = "DELETE FROM pets WHERE item_obj_id=?";
	
	private int _objectId;
	private int _count;
	
	@Override
	protected void readImpl()
	{
		_objectId = readD();
		_count = readD();
	}
	
	@Override
	protected void runImpl()
	{
		final Player player = getClient().getPlayer();
		if (player == null)
			return;
		
		if (player.isProcessingTransaction() || player.isOperating())
		{
			player.sendPacket(SystemMessageId.CANNOT_TRADE_DISCARD_DROP_ITEM_WHILE_IN_SHOPMODE);
			return;
		}
		
		final ItemInstance itemToRemove = player.getInventory().getItemByObjectId(_objectId);
		if (itemToRemove == null)
			return;
		
		if (_count < 1 || _count > itemToRemove.getCount())
		{
			player.sendPacket(SystemMessageId.CANNOT_DESTROY_NUMBER_INCORRECT);
			return;
		}
		
		if (!itemToRemove.isStackable() && _count > 1)
			return;
		
		final int itemId = itemToRemove.getItemId();
		if (!itemToRemove.isDestroyable() || CursedWeaponManager.getInstance().isCursed(itemId))
		{
			player.sendPacket((itemToRemove.isHeroItem()) ? SystemMessageId.HERO_WEAPONS_CANT_DESTROYED : SystemMessageId.CANNOT_DISCARD_THIS_ITEM);
			return;
		}
		
		if (itemToRemove.isEquipped() && (!itemToRemove.isStackable() || (itemToRemove.isStackable() && _count >= itemToRemove.getCount())))
			player.useEquippableItem(itemToRemove, false);
		
		// if it's a pet control item.
		if (itemToRemove.isSummonItem())
		{
			// See if pet or mount is active ; can't destroy item linked to that pet.
			if ((player.getSummon() != null && player.getSummon().getControlItemId() == _objectId) || (player.isMounted() && player.getMountObjectId() == _objectId))
			{
				player.sendPacket(SystemMessageId.PET_SUMMONED_MAY_NOT_DESTROYED);
				return;
			}
			
			try (Connection con = ConnectionPool.getConnection();
				PreparedStatement ps = con.prepareStatement(DELETE_PET))
			{
				ps.setInt(1, _objectId);
				ps.execute();
			}
			catch (Exception e)
			{
				LOGGER.error("Couldn't delete pet item with objectid {}.", e, _objectId);
			}
		}
		
		player.destroyItem(_objectId, _count, true);
	}
}