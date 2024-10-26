package com.shnok.javaserver.gameserver.model.itemcontainer;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.Collections;
import java.util.List;
import java.util.Set;
import java.util.concurrent.ConcurrentSkipListSet;

import com.shnok.javaserver.commons.logging.CLogger;
import com.shnok.javaserver.commons.pool.ConnectionPool;

import com.shnok.javaserver.Config;
import com.shnok.javaserver.gameserver.data.xml.ItemData;
import com.shnok.javaserver.gameserver.enums.items.ItemLocation;
import com.shnok.javaserver.gameserver.model.World;
import com.shnok.javaserver.gameserver.model.actor.Playable;
import com.shnok.javaserver.gameserver.model.actor.Player;
import com.shnok.javaserver.gameserver.model.item.instance.ItemInstance;
import com.shnok.javaserver.gameserver.model.item.kind.Item;
import com.shnok.javaserver.gameserver.taskmanager.ItemInstanceTaskManager;

public abstract class ItemContainer
{
	protected static final CLogger LOGGER = new CLogger(ItemContainer.class.getName());
	
	private static final String RESTORE_ITEMS = "SELECT * FROM items WHERE owner_id=? AND (loc=?)";
	
	protected final Set<ItemInstance> _items = new ConcurrentSkipListSet<>();
	
	protected ItemContainer()
	{
	}
	
	protected abstract Playable getOwner();
	
	protected abstract ItemLocation getBaseLocation();
	
	public String getName()
	{
		return "ItemContainer";
	}
	
	/**
	 * @return the owner objectId of the inventory.
	 */
	public int getOwnerId()
	{
		return (getOwner() == null) ? 0 : getOwner().getObjectId();
	}
	
	/**
	 * @return the quantity of items in the inventory.
	 */
	public int getSize()
	{
		return _items.size();
	}
	
	/**
	 * @return the list of items in inventory.
	 */
	public Set<ItemInstance> getItems()
	{
		return _items;
	}
	
	/**
	 * @param itemId : The item ID to check.
	 * @return True if the item id exists in this {@link ItemContainer}, false otherwise.
	 */
	public boolean hasItem(int itemId)
	{
		return _items.stream().anyMatch(i -> i.getItemId() == itemId);
	}
	
	/**
	 * @param itemIds : A list of item IDs to check.
	 * @return True if all item ids exist in this {@link ItemContainer}, false otherwise.
	 */
	public boolean hasItems(int... itemIds)
	{
		if (_items.isEmpty())
			return false;
		
		for (int itemId : itemIds)
		{
			if (!hasItem(itemId))
				return false;
		}
		return true;
	}
	
	/**
	 * @param itemIds : A list of item IDs to check.
	 * @return True if at least one item id exists in this {@link ItemContainer}, false otherwise.
	 */
	public boolean hasAtLeastOneItem(int... itemIds)
	{
		if (_items.isEmpty())
			return false;
		
		for (int itemId : itemIds)
		{
			if (hasItem(itemId))
				return true;
		}
		return false;
	}
	
	/**
	 * @param itemId : The item ID to check.
	 * @return A {@link List} of {@link ItemInstance}s by given item ID, or an empty {@link List} if none are found.
	 */
	public List<ItemInstance> getItemsByItemId(int itemId)
	{
		if (_items.isEmpty())
			return Collections.emptyList();
		
		return _items.stream().filter(i -> i.getItemId() == itemId).toList();
	}
	
	/**
	 * @param itemId : The item ID to check.
	 * @return An {@link ItemInstance} using its item ID, or null if not found in this {@link ItemContainer}.
	 */
	public ItemInstance getItemByItemId(int itemId)
	{
		return _items.stream().filter(i -> i.getItemId() == itemId).findFirst().orElse(null);
	}
	
	/**
	 * @param objectId : The object ID to check.
	 * @return An {@link ItemInstance} using its object ID, or null if not found in this {@link ItemContainer}.
	 */
	public ItemInstance getItemByObjectId(int objectId)
	{
		return _items.stream().filter(i -> i.getObjectId() == objectId).findFirst().orElse(null);
	}
	
	/**
	 * @param itemId : The item ID to check.
	 * @return The quantity of items hold by this {@link ItemContainer} (item enchant level does not matter, including equipped items).
	 */
	public int getItemCount(int itemId)
	{
		return getItemCount(itemId, -1, true);
	}
	
	/**
	 * @param itemId : The item ID to check.
	 * @param enchantLevel : The enchant level to match on (-1 for ANY enchant level).
	 * @return The quantity of items hold by this {@link ItemContainer} (including equipped items).
	 */
	public int getItemCount(int itemId, int enchantLevel)
	{
		return getItemCount(itemId, enchantLevel, true);
	}
	
	/**
	 * @param itemId : The item ID to check.
	 * @param enchantLevel : The enchant level to match on (-1 for ANY enchant level).
	 * @param includeEquipped : Include equipped items.
	 * @return The quantity of items hold by this {@link ItemContainer}.
	 */
	public int getItemCount(int itemId, int enchantLevel, boolean includeEquipped)
	{
		if (_items.isEmpty())
			return 0;
		
		int count = 0;
		
		for (ItemInstance item : _items)
		{
			if (item.getItemId() == itemId && (item.getEnchantLevel() == enchantLevel || enchantLevel < 0) && (includeEquipped || !item.isEquipped()))
			{
				if (item.isStackable())
					return item.getCount();
				
				count++;
			}
		}
		return count;
	}
	
	/**
	 * Adds item to inventory
	 * @param item : ItemInstance to add.
	 * @return the ItemInstance corresponding to the new or updated item.
	 */
	public ItemInstance addItem(ItemInstance item)
	{
		// If existing stackable item is found.
		final ItemInstance oldItem = getItemByItemId(item.getItemId());
		if (oldItem != null && oldItem.isStackable())
		{
			// Add to current ItemInstance the requested item quantity.
			oldItem.changeCount(item.getCount(), getOwner());
			
			// Destroy the item.
			item.destroyMe();
			
			// Return the existing ItemInstance.
			return oldItem;
		}
		
		// If item hasn't be found in inventory, set ownership and location.
		item.setOwnerId(getOwnerId());
		item.setLocation(getBaseLocation());
		
		// Add item in inventory.
		addBasicItem(item);
		
		return item;
	}
	
	/**
	 * Adds an item to inventory.
	 * @param itemId : The itemId of the ItemInstance to add.
	 * @param count : The quantity of items to add.
	 * @return the ItemInstance corresponding to the new or updated item.
	 */
	public ItemInstance addItem(int itemId, int count)
	{
		ItemInstance item = getItemByItemId(itemId);
		
		// If existing stackable item is found, add to current ItemInstance the requested item quantity.
		if (item != null && item.isStackable())
			item.changeCount(count, getOwner());
		// If item hasn't be found in inventory, create new one
		else
		{
			final Item template = ItemData.getInstance().getTemplate(itemId);
			if (template == null)
				return null;
			
			for (int i = 0; i < count; i++)
			{
				item = ItemInstance.create(itemId, template.isStackable() ? count : 1);
				item.setOwnerId(getOwnerId());
				item.setLocation(getBaseLocation());
				
				// Add item in inventory
				addBasicItem(item);
				
				// If stackable, end loop as entire count is included in 1 instance of item
				if (template.isStackable() || !Config.MULTIPLE_ITEM_DROP)
					break;
			}
		}
		return item;
	}
	
	public ItemInstance transferItem(int objectId, int count, ItemContainer target)
	{
		if (target == null)
			return null;
		
		ItemInstance sourceItem = getItemByObjectId(objectId);
		if (sourceItem == null)
			return null;
		
		ItemInstance targetItem = sourceItem.isStackable() ? target.getItemByItemId(sourceItem.getItemId()) : null;
		
		synchronized (sourceItem)
		{
			// check if this item still present in this container
			if (getItemByObjectId(objectId) != sourceItem)
				return null;
			
			// Check if requested quantity is available
			if (count > sourceItem.getCount())
				count = sourceItem.getCount();
			
			// If possible, move entire item object
			if (sourceItem.getCount() == count && targetItem == null)
			{
				removeItem(sourceItem, false);
				
				target.addItem(sourceItem);
				targetItem = sourceItem;
			}
			else
			{
				// If possible, only update counts
				if (sourceItem.getCount() > count)
					sourceItem.changeCount(-count, getOwner());
				else
				// Otherwise destroy old item
				{
					removeItem(sourceItem, false);
					
					sourceItem.destroyMe();
				}
				
				// If possible, only update counts
				if (targetItem != null)
					targetItem.changeCount(count, getOwner());
				// Otherwise add new item
				else
					targetItem = target.addItem(sourceItem.getItemId(), count);
			}
			
			if (sourceItem.isAugmented() && getOwner() instanceof Player player)
				sourceItem.getAugmentation().removeBonus(player);
		}
		return targetItem;
	}
	
	public ItemInstance transferItem(int objectId, int amount, Playable target)
	{
		if (target == null)
			return null;
		
		ItemInstance sourceitem = getItemByObjectId(objectId);
		if (sourceitem == null)
			return null;
		
		Inventory inventory = target.getInventory();
		ItemInstance targetitem = sourceitem.isStackable() ? inventory.getItemByItemId(sourceitem.getItemId()) : null;
		
		synchronized (sourceitem)
		{
			// check if this item still present in this container
			if (getItemByObjectId(objectId) != sourceitem)
				return null;
			
			// Check if requested quantity is available
			if (amount > sourceitem.getCount())
				amount = sourceitem.getCount();
			
			// If possible, move entire item object
			if (sourceitem.getCount() == amount && targetitem == null)
			{
				removeItem(sourceitem, false);
				
				inventory.addItem(sourceitem);
				targetitem = sourceitem;
			}
			else
			{
				// If possible, only update counts
				if (sourceitem.getCount() > amount)
					sourceitem.changeCount(-amount, getOwner());
				// Otherwise destroy old item
				else
				{
					removeItem(sourceitem, false);
					
					sourceitem.destroyMe();
				}
				
				// If possible, only update counts
				if (targetitem != null)
					targetitem.changeCount(amount, target);
				// Otherwise add new item
				else
					targetitem = inventory.addItem(sourceitem.getItemId(), amount);
			}
		}
		return targetitem;
	}
	
	/**
	 * Destroy item from inventory and updates database
	 * @param item : ItemInstance to be destroyed
	 * @return ItemInstance corresponding to the destroyed item or the updated item in inventory
	 */
	public ItemInstance destroyItem(ItemInstance item)
	{
		return destroyItem(item, item.getCount());
	}
	
	/**
	 * Destroy item from inventory and updates database
	 * @param item : ItemInstance to be destroyed
	 * @param count
	 * @return ItemInstance corresponding to the destroyed item or the updated item in inventory
	 */
	public ItemInstance destroyItem(ItemInstance item, int count)
	{
		synchronized (item)
		{
			// Adjust item quantity
			if (item.getCount() > count)
			{
				item.changeCount(-count, getOwner());
				
				return item;
			}
			
			if (item.getCount() < count)
				return null;
			
			if (!removeItem(item, false))
				return null;
			
			item.destroyMe();
		}
		return item;
	}
	
	/**
	 * Destroy item from inventory by using its <B>objectID</B> and updates database
	 * @param objectId : int Item Instance identifier of the item to be destroyed
	 * @param count : int Quantity of items to be destroyed
	 * @return ItemInstance corresponding to the destroyed item or the updated item in inventory
	 */
	public ItemInstance destroyItem(int objectId, int count)
	{
		ItemInstance item = getItemByObjectId(objectId);
		if (item == null)
			return null;
		
		return destroyItem(item, count);
	}
	
	/**
	 * Destroy item from inventory by using its <B>itemId</B> and updates database
	 * @param itemId : int Item identifier of the item to be destroyed
	 * @param count : int Quantity of items to be destroyed
	 * @return ItemInstance corresponding to the destroyed item or the updated item in inventory
	 */
	public ItemInstance destroyItemByItemId(int itemId, int count)
	{
		ItemInstance item = getItemByItemId(itemId);
		if (item == null)
			return null;
		
		return destroyItem(item, count);
	}
	
	/**
	 * Destroy all items from inventory and updates database
	 */
	public void destroyAllItems()
	{
		for (ItemInstance item : _items)
			destroyItem(item);
	}
	
	/**
	 * @return the amount of adena (itemId 57)
	 */
	public int getAdena()
	{
		for (ItemInstance item : _items)
		{
			if (item.getItemId() == 57)
				return item.getCount();
		}
		return 0;
	}
	
	/**
	 * Add the {@link ItemInstance} set as parameter to inventory.
	 * @param item : The {@link ItemInstance} to add.
	 */
	protected void addBasicItem(ItemInstance item)
	{
		item.actualizeTime();
		
		_items.add(item);
	}
	
	/**
	 * @param item : The {@link ItemInstance} to remove.
	 * @param isDrop : If true, we also reset {@link ItemInstance}'s ownership and location.
	 * @return True if the {@link ItemInstance} set as parameter was successfully removed, or false otherwise.
	 */
	protected boolean removeItem(ItemInstance item, boolean isDrop)
	{
		return _items.remove(item);
	}
	
	/**
	 * Delete this {@link ItemContainer}, aswell as contained {@link ItemInstance}s, from {@link World}.<br>
	 * <br>
	 * Before deletion, {@link ItemInstance}s are saved in database.
	 */
	public void deleteMe()
	{
		if (getOwner() != null)
		{
			// Delete all related items from World.
			World.getInstance().removeObjects(_items);
			
			// Remove all ItemContainer items from ItemInstanceTaskManager to avoid them to be gathered and processed automatically by the delayed task.
			ItemInstanceTaskManager.getInstance().removeItems(_items);
			
			// Instantly save all ItemContainer items current state, _items is cleared from the method.
			ItemInstanceTaskManager.getInstance().updateItems(_items);
		}
		// Clear items.
		else
			_items.clear();
	}
	
	/**
	 * Get back items in container from database
	 */
	public void restore()
	{
		try (Connection con = ConnectionPool.getConnection();
			PreparedStatement ps = con.prepareStatement(RESTORE_ITEMS))
		{
			ps.setInt(1, getOwnerId());
			ps.setString(2, getBaseLocation().name());
			
			try (ResultSet rs = ps.executeQuery())
			{
				while (rs.next())
				{
					// Restore the item.
					final ItemInstance item = ItemInstance.restoreFromDb(rs);
					if (item == null)
						continue;
					
					// ItemInstanceTaskManager didn't yet process the item, which means the item wasn't anymore part of this ItemContainer - don't reload it.
					if (ItemInstanceTaskManager.getInstance().contains(item))
						continue;
					
					// Add the item to world objects list.
					World.getInstance().addObject(item);
					
					// If stackable item is found in inventory just add to current quantity
					if (item.isStackable() && getItemByItemId(item.getItemId()) != null)
						addItem(item);
					else
						addBasicItem(item);
				}
			}
		}
		catch (Exception e)
		{
			LOGGER.error("Couldn't restore container for {}.", e, getOwnerId());
		}
	}
	
	public boolean validateCapacity(int slotCount)
	{
		return true;
	}
	
	public boolean validateWeight(int weight)
	{
		return true;
	}
}