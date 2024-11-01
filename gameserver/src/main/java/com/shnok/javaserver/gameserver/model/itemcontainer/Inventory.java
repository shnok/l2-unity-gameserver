package com.shnok.javaserver.gameserver.model.itemcontainer;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.Queue;
import java.util.concurrent.ConcurrentLinkedQueue;
import java.util.logging.Level;
import java.util.logging.LogRecord;
import java.util.logging.Logger;
import java.util.stream.Stream;

import com.shnok.javaserver.commons.pool.ConnectionPool;

import com.shnok.javaserver.Config;
import com.shnok.javaserver.gameserver.data.manager.HeroManager;
import com.shnok.javaserver.gameserver.enums.Paperdoll;
import com.shnok.javaserver.gameserver.enums.items.ArmorType;
import com.shnok.javaserver.gameserver.enums.items.EtcItemType;
import com.shnok.javaserver.gameserver.enums.items.ItemLocation;
import com.shnok.javaserver.gameserver.enums.items.ItemState;
import com.shnok.javaserver.gameserver.enums.items.ItemType;
import com.shnok.javaserver.gameserver.enums.items.WeaponType;
import com.shnok.javaserver.gameserver.model.World;
import com.shnok.javaserver.gameserver.model.actor.Playable;
import com.shnok.javaserver.gameserver.model.actor.Player;
import com.shnok.javaserver.gameserver.model.item.instance.ItemInfo;
import com.shnok.javaserver.gameserver.model.item.instance.ItemInstance;
import com.shnok.javaserver.gameserver.model.item.kind.Item;
import com.shnok.javaserver.gameserver.model.itemcontainer.listeners.ChangeRecorderListener;
import com.shnok.javaserver.gameserver.model.itemcontainer.listeners.OnEquipListener;
import com.shnok.javaserver.gameserver.model.itemcontainer.listeners.StatsListener;
import com.shnok.javaserver.gameserver.taskmanager.InventoryUpdateTaskManager;
import com.shnok.javaserver.gameserver.taskmanager.ItemInstanceTaskManager;

/**
 * This class manages a {@link Playable}'s inventory.<br>
 * <br>
 * It extends {@link ItemContainer}.
 */
public abstract class Inventory extends ItemContainer
{
	private static final Logger ITEM_LOG = Logger.getLogger("item");
	
	private static final String RESTORE_INVENTORY = "SELECT * FROM items WHERE owner_id=? AND (loc=? OR loc=?) ORDER BY loc_data";
	
	protected Playable _owner;
	
	private final ItemInstance[] _paperdoll = new ItemInstance[Paperdoll.TOTAL_SLOTS];
	
	protected final List<OnEquipListener> _paperdollListeners = new ArrayList<>();
	protected final Queue<ItemInfo> _updateList = new ConcurrentLinkedQueue<>();
	
	protected int _totalWeight;
	private int _wornMask;
	
	protected Inventory(Playable owner)
	{
		_owner = owner;
		
		addPaperdollListener(StatsListener.getInstance());
	}
	
	protected abstract ItemLocation getEquipLocation();
	
	@Override
	public Playable getOwner()
	{
		return _owner;
	}
	
	@Override
	protected void addBasicItem(ItemInstance item)
	{
		super.addBasicItem(item);
		
		addUpdate(item, ItemState.ADDED);
	}
	
	@Override
	protected boolean removeItem(ItemInstance item, boolean isDrop)
	{
		if (!super.removeItem(item, isDrop))
			return false;
		
		// Unequip item if equipped.
		for (int i = 0; i < _paperdoll.length; i++)
		{
			if (_paperdoll[i] == item)
				unequipItemInSlot(i);
		}
		
		// Reset item's ownership and location if parameter isDrop is set.
		if (isDrop)
		{
			item.setOwnerId(0);
			item.setLocation(ItemLocation.VOID);
		}
		
		addUpdate(item, ItemState.REMOVED);
		return true;
	}
	
	@Override
	public void restore()
	{
		try (Connection con = ConnectionPool.getConnection();
			PreparedStatement ps = con.prepareStatement(RESTORE_INVENTORY))
		{
			ps.setInt(1, getOwnerId());
			ps.setString(2, getBaseLocation().name());
			ps.setString(3, getEquipLocation().name());
			
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
					
					// If the item is an hero item and inventory's owner is a player who isn't an hero, then set it to inventory.
					if (getOwner() instanceof Player && item.isHeroItem() && !HeroManager.getInstance().isActiveHero(getOwnerId()))
						item.setLocation(ItemLocation.INVENTORY);
					
					// Add the item to world objects list.
					World.getInstance().addObject(item);
					
					// If stackable item is found in inventory just add to current quantity
					if (item.isStackable() && getItemByItemId(item.getItemId()) != null)
						addItem(item);
					// Don't trigger IU.
					else
						super.addBasicItem(item);
					
					// Equip the item.
					if (item.isEquipped())
						equipItem(item);
				}
			}
		}
		catch (Exception e)
		{
			LOGGER.error("Couldn't restore inventory for {}.", e, getOwnerId());
		}
	}
	
	public int getTotalWeight()
	{
		return _totalWeight;
	}
	
	public boolean updateWeight()
	{
		// Calculate weight.
		int weight = 0;
		for (ItemInstance item : _items)
			weight += item.getItem().getWeight() * item.getCount();
		
		// Calculated value is identical, don't send any update.
		if (_totalWeight == weight)
			return false;
		
		// Keep value for further usage.
		_totalWeight = weight;
		return true;
	}
	
	/**
	 * @param type : The {@link ItemType} to check.
	 * @return True if the given {@link ItemType} is worn, false otherwise.
	 */
	public boolean isWearingType(ItemType type)
	{
		return isWearingType(type.mask());
	}
	
	/**
	 * @param mask : The mask to check.
	 * @return True if the given {@link ItemType} mask is worn, false otherwise.
	 */
	public boolean isWearingType(int mask)
	{
		return (mask & _wornMask) != 0;
	}
	
	/**
	 * Drop an item from this {@link Inventory} and update database.
	 * @param item : The {@link ItemInstance} to drop.
	 * @return The {@link ItemInstance} corresponding to the destroyed item or the updated item in {@link Inventory}.
	 */
	public ItemInstance dropItem(ItemInstance item)
	{
		return (removeItem(item, true)) ? item : null;
	}
	
	/**
	 * Drop an item using its objectIdfrom this {@link Inventory} and update database.
	 * @param objectId : The {@link ItemInstance} objectId to drop.
	 * @param count : The amount to drop.
	 * @return The {@link ItemInstance} corresponding to the destroyed item or the updated item in {@link Inventory}.
	 */
	public ItemInstance dropItem(int objectId, int count)
	{
		ItemInstance item = getItemByObjectId(objectId);
		if (item == null)
			return null;
		
		synchronized (item)
		{
			if (!_items.contains(item))
				return null;
			
			if (item.getCount() > count)
			{
				item.changeCount(-count, getOwner());
				
				item = ItemInstance.create(item.getItemId(), count);
				return item;
			}
		}
		return dropItem(item);
	}
	
	/**
	 * @param slot : The {@link Paperdoll} slot to check.
	 * @return The {@link ItemInstance} associated to the {@link Paperdoll} slot.
	 */
	public ItemInstance getItemFrom(Paperdoll slot)
	{
		return _paperdoll[slot.getId()];
	}
	
	/**
	 * @param slot : The {@link Paperdoll} slot to check.
	 * @return True if an {@link ItemInstance} is associated to the {@link Paperdoll} slot, false otherwise.
	 */
	public boolean hasItemIn(Paperdoll slot)
	{
		return _paperdoll[slot.getId()] != null;
	}
	
	/**
	 * @param slot : The {@link Paperdoll} slot to test.
	 * @return The id of the {@link ItemInstance} in the {@link Paperdoll} slot, or 0 if not found.
	 */
	public int getItemIdFrom(Paperdoll slot)
	{
		final ItemInstance item = getItemFrom(slot);
		return (item == null) ? 0 : item.getItemId();
	}
	
	/**
	 * @param slot : The {@link Paperdoll} slot to test.
	 * @return The augment id of the {@link ItemInstance} in the {@link Paperdoll} slot, or 0 if not found.
	 */
	public int getAugmentationIdFrom(Paperdoll slot)
	{
		final ItemInstance item = getItemFrom(slot);
		return (item == null || item.getAugmentation() == null) ? 0 : item.getAugmentation().getId();
	}
	
	/**
	 * @param slot : The {@link Paperdoll} slot to test.
	 * @return The object id of the {@link ItemInstance} in the {@link Paperdoll} slot, or 0 if not found.
	 */
	public int getItemObjectIdFrom(Paperdoll slot)
	{
		final ItemInstance item = getItemFrom(slot);
		return (item == null) ? 0 : item.getObjectId();
	}
	
	/**
	 * @param itemSlot : The item slot to check.
	 * @return The {@link ItemInstance} associated to the item slot.
	 */
	public ItemInstance getItemFrom(int itemSlot)
	{
		return getItemFrom(getPaperdollIndex(itemSlot));
	}
	
	/**
	 * @return The {@link List} of equipped {@link ItemInstance}s.
	 */
	public List<ItemInstance> getPaperdollItems()
	{
		return Stream.of(_paperdoll).filter(Objects::nonNull).toList();
	}
	
	/**
	 * @param slot : The item slot to test.
	 * @return The {@link Paperdoll} associated to an item slot.
	 */
	public static Paperdoll getPaperdollIndex(int slot)
	{
		switch (slot)
		{
			case Item.SLOT_UNDERWEAR:
				return Paperdoll.UNDER;
			
			case Item.SLOT_R_EAR:
				return Paperdoll.REAR;
			
			case Item.SLOT_L_EAR:
				return Paperdoll.LEAR;
			
			case Item.SLOT_NECK:
				return Paperdoll.NECK;
			
			case Item.SLOT_R_FINGER:
				return Paperdoll.RFINGER;
			
			case Item.SLOT_L_FINGER:
				return Paperdoll.LFINGER;
			
			case Item.SLOT_HEAD:
				return Paperdoll.HEAD;
			
			case Item.SLOT_R_HAND, Item.SLOT_LR_HAND:
				return Paperdoll.RHAND;
			
			case Item.SLOT_L_HAND:
				return Paperdoll.LHAND;
			
			case Item.SLOT_GLOVES:
				return Paperdoll.GLOVES;
			
			case Item.SLOT_CHEST, Item.SLOT_FULL_ARMOR, Item.SLOT_ALLDRESS:
				return Paperdoll.CHEST;
			
			case Item.SLOT_LEGS:
				return Paperdoll.LEGS;
			
			case Item.SLOT_FEET:
				return Paperdoll.FEET;
			
			case Item.SLOT_BACK:
				return Paperdoll.CLOAK;
			
			case Item.SLOT_FACE, Item.SLOT_HAIRALL:
				return Paperdoll.FACE;
			
			case Item.SLOT_HAIR:
				return Paperdoll.HAIR;
		}
		return Paperdoll.NULL;
	}
	
	/**
	 * Register a new {@link OnEquipListener} on paperdoll listeners.
	 * @param listener : The {@link OnEquipListener} to add.
	 */
	public synchronized void addPaperdollListener(OnEquipListener listener)
	{
		_paperdollListeners.add(listener);
	}
	
	/**
	 * Unregister an existing {@link OnEquipListener} from paperdoll listeners.
	 * @param listener : The {@link OnEquipListener} to remove.
	 */
	public synchronized void removePaperdollListener(OnEquipListener listener)
	{
		_paperdollListeners.remove(listener);
	}
	
	/**
	 * Equip an {@link ItemInstance} in the given {@link Paperdoll} slot.
	 * @param slot : The {@link Paperdoll} slot to edit.
	 * @param item : The {@link ItemInstance} to add.
	 * @return The previous {@link ItemInstance} set in given {@link Paperdoll}, or null if unequipped.
	 */
	public synchronized ItemInstance setPaperdollItem(Paperdoll slot, ItemInstance item)
	{
		ItemInstance old = getItemFrom(slot);
		if (old != item)
		{
			if (old != null)
			{
				_paperdoll[slot.getId()] = null;
				
				// Put old item from paperdoll slot to base location.
				old.setLocation(getBaseLocation(), findNextAvailableSlot());
				addUpdate(old, ItemState.MODIFIED);
				
				// Delete armor mask flag (in case of two-piece armor it does not matter, we need to deactivate mask too).
				_wornMask &= ~old.getItem().getItemMask();
				
				// Notify all paperdoll listener in order to unequip old item in slot.
				for (OnEquipListener listener : _paperdollListeners)
					listener.onUnequip(slot, old, getOwner());
			}
			
			if (item != null)
			{
				_paperdoll[slot.getId()] = item;
				
				// Add new item in slot of paperdoll.
				item.setLocation(getEquipLocation(), slot.getId());
				addUpdate(item, ItemState.MODIFIED);
				
				// Activate mask (check 2nd armor part for two-piece armors).
				final Item itm = item.getItem();
				if (itm.getBodyPart() == Item.SLOT_CHEST)
				{
					final ItemInstance legs = getItemFrom(Paperdoll.LEGS);
					if (legs != null && legs.getItem().getItemMask() == itm.getItemMask())
						_wornMask |= itm.getItemMask();
				}
				else if (itm.getBodyPart() == Item.SLOT_LEGS)
				{
					final ItemInstance legs = getItemFrom(Paperdoll.CHEST);
					if (legs != null && legs.getItem().getItemMask() == itm.getItemMask())
						_wornMask |= itm.getItemMask();
				}
				else
					_wornMask |= itm.getItemMask();
				
				for (OnEquipListener listener : _paperdollListeners)
					listener.onEquip(slot, item, getOwner());
			}
		}
		return old;
	}
	
	/**
	 * @param item : The {@link ItemInstance} to test.
	 * @return The item slot associated to a given {@link Paperdoll}.
	 */
	public int getSlotFromItem(ItemInstance item)
	{
		switch (Paperdoll.getEnumById(item.getLocationSlot()))
		{
			case UNDER:
				return Item.SLOT_UNDERWEAR;
			
			case LEAR:
				return Item.SLOT_L_EAR;
			
			case REAR:
				return Item.SLOT_R_EAR;
			
			case NECK:
				return Item.SLOT_NECK;
			
			case RFINGER:
				return Item.SLOT_R_FINGER;
			
			case LFINGER:
				return Item.SLOT_L_FINGER;
			
			case HAIR:
				return Item.SLOT_HAIR;
			
			case FACE:
				return Item.SLOT_FACE;
			
			case HEAD:
				return Item.SLOT_HEAD;
			
			case RHAND:
				return Item.SLOT_R_HAND;
			
			case LHAND:
				return Item.SLOT_L_HAND;
			
			case GLOVES:
				return Item.SLOT_GLOVES;
			
			case CHEST:
				return item.getItem().getBodyPart();
			
			case LEGS:
				return Item.SLOT_LEGS;
			
			case CLOAK:
				return Item.SLOT_BACK;
			
			case FEET:
				return Item.SLOT_FEET;
			
			default:
				return -1;
		}
	}
	
	/**
	 * Equip an {@link ItemInstance} in {@link Paperdoll} slot.
	 * @param item : The {@link ItemInstance} to set.
	 */
	public void equipItem(ItemInstance item)
	{
		switch (item.getItem().getBodyPart())
		{
			case Item.SLOT_LR_HAND:
				setPaperdollItem(Paperdoll.LHAND, null);
				setPaperdollItem(Paperdoll.RHAND, item);
				break;
			
			case Item.SLOT_L_HAND:
				ItemInstance rh = getItemFrom(Paperdoll.RHAND);
				if (rh != null && rh.getItem().getBodyPart() == Item.SLOT_LR_HAND && !((rh.getItemType() == WeaponType.BOW && item.getItemType() == EtcItemType.ARROW) || (rh.getItemType() == WeaponType.FISHINGROD && item.getItemType() == EtcItemType.LURE)))
					setPaperdollItem(Paperdoll.RHAND, null);
				
				setPaperdollItem(Paperdoll.LHAND, item);
				break;
			
			case Item.SLOT_R_HAND:
				setPaperdollItem(Paperdoll.RHAND, item);
				break;
			
			case Item.SLOT_L_EAR, Item.SLOT_R_EAR, Item.SLOT_L_EAR | Item.SLOT_R_EAR:
				if (getItemFrom(Paperdoll.LEAR) == null)
					setPaperdollItem(Paperdoll.LEAR, item);
				else if (getItemFrom(Paperdoll.REAR) == null)
					setPaperdollItem(Paperdoll.REAR, item);
				else
				{
					if (getItemIdFrom(Paperdoll.REAR) == item.getItemId())
						setPaperdollItem(Paperdoll.LEAR, item);
					else if (getItemIdFrom(Paperdoll.LEAR) == item.getItemId())
						setPaperdollItem(Paperdoll.REAR, item);
					else
						setPaperdollItem(Paperdoll.LEAR, item);
				}
				break;
			
			case Item.SLOT_L_FINGER, Item.SLOT_R_FINGER, Item.SLOT_L_FINGER | Item.SLOT_R_FINGER:
				if (getItemFrom(Paperdoll.LFINGER) == null)
					setPaperdollItem(Paperdoll.LFINGER, item);
				else if (getItemFrom(Paperdoll.RFINGER) == null)
					setPaperdollItem(Paperdoll.RFINGER, item);
				else
				{
					if (getItemIdFrom(Paperdoll.RFINGER) == item.getItemId())
						setPaperdollItem(Paperdoll.LFINGER, item);
					else if (getItemIdFrom(Paperdoll.LFINGER) == item.getItemId())
						setPaperdollItem(Paperdoll.RFINGER, item);
					else
						setPaperdollItem(Paperdoll.LFINGER, item);
				}
				break;
			
			case Item.SLOT_NECK:
				setPaperdollItem(Paperdoll.NECK, item);
				break;
			
			case Item.SLOT_FULL_ARMOR:
				setPaperdollItem(Paperdoll.LEGS, null);
				setPaperdollItem(Paperdoll.CHEST, item);
				break;
			
			case Item.SLOT_CHEST:
				setPaperdollItem(Paperdoll.CHEST, item);
				break;
			
			case Item.SLOT_LEGS:
				// handle full armor
				final ItemInstance chest = getItemFrom(Paperdoll.CHEST);
				if (chest != null && chest.getItem().getBodyPart() == Item.SLOT_FULL_ARMOR)
					setPaperdollItem(Paperdoll.CHEST, null);
				
				setPaperdollItem(Paperdoll.LEGS, item);
				break;
			
			case Item.SLOT_FEET:
				setPaperdollItem(Paperdoll.FEET, item);
				break;
			
			case Item.SLOT_GLOVES:
				setPaperdollItem(Paperdoll.GLOVES, item);
				break;
			
			case Item.SLOT_HEAD:
				setPaperdollItem(Paperdoll.HEAD, item);
				break;
			
			case Item.SLOT_FACE:
				final ItemInstance hair = getItemFrom(Paperdoll.HAIR);
				if (hair != null && hair.getItem().getBodyPart() == Item.SLOT_HAIRALL)
					setPaperdollItem(Paperdoll.HAIR, null);
				
				setPaperdollItem(Paperdoll.FACE, item);
				break;
			
			case Item.SLOT_HAIR:
				final ItemInstance face = getItemFrom(Paperdoll.FACE);
				if (face != null && face.getItem().getBodyPart() == Item.SLOT_HAIRALL)
					setPaperdollItem(Paperdoll.FACE, null);
				
				setPaperdollItem(Paperdoll.HAIR, item);
				break;
			
			case Item.SLOT_HAIRALL:
				setPaperdollItem(Paperdoll.FACE, null);
				setPaperdollItem(Paperdoll.HAIR, item);
				break;
			
			case Item.SLOT_UNDERWEAR:
				setPaperdollItem(Paperdoll.UNDER, item);
				break;
			
			case Item.SLOT_BACK:
				setPaperdollItem(Paperdoll.CLOAK, item);
				break;
			
			case Item.SLOT_ALLDRESS:
				setPaperdollItem(Paperdoll.LEGS, null);
				setPaperdollItem(Paperdoll.LHAND, null);
				setPaperdollItem(Paperdoll.RHAND, null);
				setPaperdollItem(Paperdoll.HEAD, null);
				setPaperdollItem(Paperdoll.FEET, null);
				setPaperdollItem(Paperdoll.GLOVES, null);
				setPaperdollItem(Paperdoll.CHEST, item);
				break;
			
			default:
				LOGGER.warn("Unknown body slot {} for itemId {}.", item.getItem().getBodyPart(), item.getItemId());
		}
	}
	
	/**
	 * Equip an {@link ItemInstance} and return alterations.<br>
	 * <br>
	 * <b>If you dont need return value use {@link Inventory#equipItem(ItemInstance)} instead.</b>
	 * @param item : The {@link ItemInstance} to equip.
	 * @return The array of altered {@link ItemInstance}s.
	 */
	public ItemInstance[] equipItemAndRecord(ItemInstance item)
	{
		final ChangeRecorderListener recorder = new ChangeRecorderListener(this);
		
		try
		{
			equipItem(item);
		}
		finally
		{
			removePaperdollListener(recorder);
		}
		return recorder.getChangedItems();
	}
	
	/**
	 * Equip an {@link ItemInstance}.<br>
	 * <br>
	 * Concerning pets, armors go to Paperdoll.CHEST and weapon to Paperdoll.RHAND.
	 * @param item : The {@link ItemInstance} to equip.
	 */
	public void equipPetItem(ItemInstance item)
	{
		// Verify first if item is a pet item.
		if (item.isPetItem())
		{
			// Check then about type of item : armor or weapon. Feed the correct slot.
			if (item.getItemType() == WeaponType.PET)
				setPaperdollItem(Paperdoll.RHAND, item);
			else if (item.getItemType() == ArmorType.PET)
				setPaperdollItem(Paperdoll.CHEST, item);
		}
	}
	
	/**
	 * Unequip an {@link ItemInstance} and return alterations.
	 * @param item : The {@link ItemInstance} used to find the slot back.
	 * @return The array of altered {@link ItemInstance}s.
	 */
	public ItemInstance[] unequipItemInBodySlotAndRecord(ItemInstance item)
	{
		final ChangeRecorderListener recorder = new ChangeRecorderListener(this);
		
		try
		{
			unequipItemInBodySlot(getSlotFromItem(item));
		}
		finally
		{
			removePaperdollListener(recorder);
		}
		return recorder.getChangedItems();
	}
	
	/**
	 * Unequip an {@link ItemInstance} and return alterations.
	 * @param itemSlot : The item slot to test.
	 * @return The array of altered {@link ItemInstance}s.
	 */
	public ItemInstance[] unequipItemInBodySlotAndRecord(int itemSlot)
	{
		final ChangeRecorderListener recorder = new ChangeRecorderListener(this);
		
		try
		{
			unequipItemInBodySlot(itemSlot);
		}
		finally
		{
			removePaperdollListener(recorder);
		}
		return recorder.getChangedItems();
	}
	
	/**
	 * Unequip an {@link ItemInstance} by its {@link Paperdoll} id.
	 * @param slot : The {@link Paperdoll} id.
	 * @return The unequipped {@link ItemInstance}, or null if already unequipped.
	 */
	public ItemInstance unequipItemInSlot(int slot)
	{
		return setPaperdollItem(Paperdoll.getEnumById(slot), null);
	}
	
	/**
	 * Unequip an {@link ItemInstance} and return alterations.
	 * @param slot : The slot to test.
	 * @return The array of altered {@link ItemInstance}s.
	 */
	public ItemInstance[] unequipItemInSlotAndRecord(int slot)
	{
		final ChangeRecorderListener recorder = new ChangeRecorderListener(this);
		
		try
		{
			unequipItemInSlot(slot);
			if (getOwner() instanceof Player player)
				player.refreshExpertisePenalty();
		}
		finally
		{
			removePaperdollListener(recorder);
		}
		return recorder.getChangedItems();
	}
	
	/**
	 * Unequip an {@link ItemInstance} using its item slot.
	 * @param itemSlot : The item slot used to find the {@link Paperdoll} slot.
	 * @return The unequipped {@link ItemInstance}, or null if already unequipped.
	 */
	public ItemInstance unequipItemInBodySlot(int itemSlot)
	{
		Paperdoll slot = Paperdoll.NULL;
		
		switch (itemSlot)
		{
			case Item.SLOT_L_EAR:
				slot = Paperdoll.LEAR;
				break;
			
			case Item.SLOT_R_EAR:
				slot = Paperdoll.REAR;
				break;
			
			case Item.SLOT_NECK:
				slot = Paperdoll.NECK;
				break;
			
			case Item.SLOT_R_FINGER:
				slot = Paperdoll.RFINGER;
				break;
			
			case Item.SLOT_L_FINGER:
				slot = Paperdoll.LFINGER;
				break;
			
			case Item.SLOT_HAIR:
				slot = Paperdoll.HAIR;
				break;
			
			case Item.SLOT_FACE:
				slot = Paperdoll.FACE;
				break;
			
			case Item.SLOT_HAIRALL:
				setPaperdollItem(Paperdoll.FACE, null);
				slot = Paperdoll.FACE;
				break;
			
			case Item.SLOT_HEAD:
				slot = Paperdoll.HEAD;
				break;
			
			case Item.SLOT_R_HAND, Item.SLOT_LR_HAND:
				slot = Paperdoll.RHAND;
				break;
			
			case Item.SLOT_L_HAND:
				slot = Paperdoll.LHAND;
				break;
			
			case Item.SLOT_GLOVES:
				slot = Paperdoll.GLOVES;
				break;
			
			case Item.SLOT_CHEST, Item.SLOT_FULL_ARMOR, Item.SLOT_ALLDRESS:
				slot = Paperdoll.CHEST;
				break;
			
			case Item.SLOT_LEGS:
				slot = Paperdoll.LEGS;
				break;
			
			case Item.SLOT_BACK:
				slot = Paperdoll.CLOAK;
				break;
			
			case Item.SLOT_FEET:
				slot = Paperdoll.FEET;
				break;
			
			case Item.SLOT_UNDERWEAR:
				slot = Paperdoll.UNDER;
				break;
			
			default:
				LOGGER.warn("Slot type {} is unhandled.", slot);
		}
		
		return (slot == Paperdoll.NULL) ? null : setPaperdollItem(slot, null);
	}
	
	public void addUpdate(ItemInstance item, ItemState state)
	{
		if (item == null)
			return;
		
		// Only log worthy items.
		if (Config.LOG_ITEMS && !item.isStackable())
		{
			final LogRecord logRecord = new LogRecord(Level.INFO, state.toString());
			logRecord.setLoggerName("item");
			logRecord.setParameters(new Object[]
			{
				getOwner(),
				item
			});
			ITEM_LOG.log(logRecord);
		}
		
		// Check if _updateList is filled and if item is stackable.
		if (!_updateList.isEmpty() && item.isStackable())
		{
			// Verify if an ItemInfo holding the exact same ItemState and objectId exists ; if yes, edit the existing count to avoid to generate garbage.
			final ItemInfo info = _updateList.stream().filter(i -> i.getObjectId() == item.getObjectId() && i.getState() == state).findAny().orElse(null);
			if (info != null)
			{
				info.setCount(item.getCount());
				return;
			}
		}
		
		// Generate a new ItemInfo to reflect Inventory change.
		_updateList.add(new ItemInfo(item, state));
		
		// List this inventory as IU-friendly.
		InventoryUpdateTaskManager.getInstance().add(this);
	}
	
	public Queue<ItemInfo> getUpdateList()
	{
		return _updateList;
	}
	
	public void clearUpdateList()
	{
		_updateList.clear();
	}
	
	/**
	 * @param bow : The {@link Item} designating the bow.
	 * @return The {@link ItemInstance} pointing out arrows.
	 */
	public ItemInstance findArrowForBow(Item bow)
	{
		if (bow == null)
			return null;
		
		// Get the ItemInstance corresponding to the item identifier and return it.
		switch (bow.getCrystalType())
		{
			case NONE:
				return getItemByItemId(17); // Wooden arrow
				
			case D:
				return getItemByItemId(1341); // Bone arrow
				
			case C:
				return getItemByItemId(1342); // Fine steel arrow
				
			case B:
				return getItemByItemId(1343); // Silver arrow
				
			case A:
				return getItemByItemId(1344); // Mithril arrow
				
			case S:
				return getItemByItemId(1345); // Shining arrow
				
			default:
				return null;
		}
	}
}