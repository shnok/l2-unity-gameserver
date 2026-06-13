package com.shnok.javaserver.gameserver.taskmanager;

import java.util.Collections;
import java.util.Map;
import java.util.Map.Entry;
import java.util.concurrent.ConcurrentHashMap;

import com.shnok.javaserver.commons.pool.ThreadPool;

import com.shnok.javaserver.gameserver.enums.Paperdoll;
import com.shnok.javaserver.gameserver.enums.items.ItemState;
import com.shnok.javaserver.gameserver.model.actor.Playable;
import com.shnok.javaserver.gameserver.model.actor.Player;
import com.shnok.javaserver.gameserver.model.item.instance.ItemInstance;
import com.shnok.javaserver.gameserver.model.itemcontainer.listeners.OnEquipListener;
import com.shnok.javaserver.gameserver.network.SystemMessageId;
import com.shnok.javaserver.gameserver.network.serverpackets.SystemMessage;

/**
 * Manage {@link ItemInstance} as shadow item, temporary weapons with short lifespans.<br>
 * <br>
 * A Shadow Weapon has the same abilities and appearance as an ordinary weapon, but it has a limited lifespan.<br>
 * <br>
 * When a Shadow Weapon's total duration or mana reaches 0, it will be removed from inventory and disappear.<br>
 * <br>
 * The remaining duration or mana is consumed from the time the item is equipped, and the consumption ends when it is no longer equipped.<br>
 * <br>
 * Warning: If the item is equipped and unequipped multiple times, the remaining duration or mana is consumed faster than normal. Logging in and out of the game with the weapon equipped will also decrease its duration.<br>
 * <br>
 * As a short-term item, shadow weapons cannot be moved through trading, drop, or cargo, and it can only be stored in a private warehouse. Short-term items also cannot be enchanted, augmented, given a special ability or crystallized.<br>
 * <br>
 * A weapon that is obtained by using a Shadow Weapon Exchange Coupon has a lower total duration or mana than a Shadow Weapon bought at a weapon shop.
 */
public class ShadowItemTaskManager implements Runnable, OnEquipListener
{
	private final Map<ItemInstance, Player> _shadowItems = new ConcurrentHashMap<>();
	
	protected ShadowItemTaskManager()
	{
		// Run task each second.
		ThreadPool.scheduleAtFixedRate(this, 1000, 1000);
	}
	
	@Override
	public final void run()
	{
		// List is empty, skip.
		if (_shadowItems.isEmpty())
			return;
		
		// For all items.
		for (Entry<ItemInstance, Player> entry : _shadowItems.entrySet())
		{
			// Get item and player.
			final ItemInstance item = entry.getKey();
			final Player player = entry.getValue();
			
			// Decrease item mana.
			item.decreaseMana(1);
			
			// If not enough mana, destroy the item and inform the player.
			if (item.getManaLeft() <= 0)
			{
				// Unequip the item.
				if (item.isEquipped())
					player.useEquippableItem(item, false);
				
				// Destroy the item.
				player.destroyItem(item, true);
				
				// Remove the item from this task manager.
				_shadowItems.remove(item);
			}
			// Enough mana, show messages.
			else
			{
				if (item.getManaLeft() == 60)
					player.sendPacket(SystemMessage.getSystemMessage(SystemMessageId.S1S_REMAINING_MANA_IS_NOW_1).addItemName(item.getItemId()));
				else if (item.getManaLeft() == 300)
					player.sendPacket(SystemMessage.getSystemMessage(SystemMessageId.S1S_REMAINING_MANA_IS_NOW_5).addItemName(item.getItemId()));
				else if (item.getManaLeft() == 600)
					player.sendPacket(SystemMessage.getSystemMessage(SystemMessageId.S1S_REMAINING_MANA_IS_NOW_10).addItemName(item.getItemId()));
				
				// Update inventory every minute.
				if (item.getManaLeft() % 60 == 60)
					item.updateState(player, ItemState.MODIFIED);
			}
		}
	}
	
	@Override
	public final void onEquip(Paperdoll slot, ItemInstance item, Playable playable)
	{
		// Must be a shadow item.
		if (!item.isShadowItem())
			return;
		
		// Must be a player.
		if (!(playable instanceof Player player))
			return;
		
		// Decrease mana time by 1 minute for each equip action subsequent to the first one.
		if (item.getManaLeft() != item.getItem().getDuration() * 60)
			item.decreaseMana(60);
		
		_shadowItems.put(item, player);
	}
	
	@Override
	public final void onUnequip(Paperdoll slot, ItemInstance item, Playable actor)
	{
		// Must be a shadow item.
		if (!item.isShadowItem())
			return;
		
		_shadowItems.remove(item);
	}
	
	public final void remove(Player player)
	{
		// List is empty, skip.
		if (_shadowItems.isEmpty())
			return;
		
		// Remove ALL associated items.
		_shadowItems.values().removeAll(Collections.singleton(player));
	}
	
	public static final ShadowItemTaskManager getInstance()
	{
		return SingletonHolder.INSTANCE;
	}
	
	private static class SingletonHolder
	{
		protected static final ShadowItemTaskManager INSTANCE = new ShadowItemTaskManager();
	}
}