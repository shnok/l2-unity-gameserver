package com.shnok.javaserver.gameserver.taskmanager;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;

import com.shnok.javaserver.commons.logging.CLogger;
import com.shnok.javaserver.commons.pool.ConnectionPool;
import com.shnok.javaserver.commons.pool.ThreadPool;

import com.shnok.javaserver.gameserver.enums.items.ItemLocation;
import com.shnok.javaserver.gameserver.model.Augmentation;
import com.shnok.javaserver.gameserver.model.item.instance.ItemInstance;
import com.shnok.javaserver.gameserver.model.item.kind.Weapon;

/**
 * Lazy save items upon database. Delete old items.
 */
public class ItemInstanceTaskManager implements Runnable
{
	private static final CLogger LOGGER = new CLogger(ItemInstanceTaskManager.class.getName());
	
	private static final String INSERT_ITEM = "INSERT INTO items (owner_id,object_id,item_id,count,enchant_level,loc,loc_data,custom_type1,custom_type2,mana_left,time) VALUES (?,?,?,?,?,?,?,?,?,?,?) ON DUPLICATE KEY UPDATE owner_id=VALUES(owner_id),count=VALUES(count),loc=VALUES(loc),loc_data=VALUES(loc_data),enchant_level=VALUES(enchant_level),custom_type1=VALUES(custom_type1),custom_type2=VALUES(custom_type2),mana_left=VALUES(mana_left),time=VALUES(time)";
	private static final String DELETE_ITEM = "DELETE FROM items WHERE object_id=?";
	
	private static final String DELETE_PET_ITEM = "DELETE FROM pets WHERE item_obj_id=?";
	
	private static final String UPDATE_AUGMENTATION = "INSERT INTO augmentations (item_oid,attributes,skill_id,skill_level) VALUES (?,?,?,?) ON DUPLICATE KEY UPDATE attributes=VALUES(attributes),skill_id=VALUES(skill_id),skill_level=VALUES(skill_level)";
	private static final String DELETE_AUGMENTATION = "DELETE FROM augmentations WHERE item_oid=?";
	
	private final Set<ItemInstance> _items = ConcurrentHashMap.newKeySet();
	
	protected ItemInstanceTaskManager()
	{
		// Run task each minute.
		ThreadPool.scheduleAtFixedRate(this, 60000L, 60000L);
	}
	
	@Override
	public final void run()
	{
		// Run the database process, and clear the Set.
		updateItems(_items);
	}
	
	/**
	 * Add an {@link ItemInstance} into the {@link Set}.
	 * @param item : The {@link ItemInstance} to add.
	 */
	public void add(ItemInstance item)
	{
		_items.add(item);
	}
	
	/**
	 * @param item : The {@link ItemInstance} to check.
	 * @return True if the items {@link Set} contains the specified element, or false otherwise.
	 */
	public boolean contains(ItemInstance item)
	{
		return _items.contains(item);
	}
	
	/**
	 * Remove {@link ItemInstance}s based on the set parameter.
	 * @param items : The {@link Set} of {@link ItemInstance}s to remove.
	 */
	public void removeItems(Set<ItemInstance> items)
	{
		_items.removeAll(items);
	}
	
	/**
	 * Run the database process for the specific {@link ItemInstance} {@link Set}.<br>
	 * <br>
	 * Clear the {@link Set} afterwards.
	 * @param items : The {@link Set} of {@link ItemInstance}s to affect.
	 */
	public void updateItems(Set<ItemInstance> items)
	{
		if (items.isEmpty())
			return;
		
		try (Connection con = ConnectionPool.getConnection();
			PreparedStatement ps1 = con.prepareStatement(INSERT_ITEM);
			PreparedStatement ps2 = con.prepareStatement(UPDATE_AUGMENTATION);
			PreparedStatement ps3 = con.prepareStatement(DELETE_ITEM);
			PreparedStatement ps4 = con.prepareStatement(DELETE_AUGMENTATION);
			PreparedStatement ps5 = con.prepareStatement(DELETE_PET_ITEM))
		{
			// Loop all items.
			for (ItemInstance item : items)
			{
				// Retain variable for Augmentation checks.
				final boolean isWeapon = item.getItem() instanceof Weapon;
				
				// If the item got no amount or is under VOID ItemLocation, delete it.
				if (item.getCount() <= 0 || item.getLocation() == ItemLocation.VOID)
				{
					// Delete the item.
					ps3.setInt(1, item.getObjectId());
					ps3.addBatch();
					
					// If the item got no amount ONLY (can be VOID).
					if (item.getCount() <= 0)
					{
						// Delete the augmentation.
						if (isWeapon)
						{
							ps4.setInt(1, item.getObjectId());
							ps4.addBatch();
						}
						
						// Delete the pet related to the item.
						if (item.isSummonItem())
						{
							ps5.setInt(1, item.getObjectId());
							ps5.addBatch();
						}
					}
					continue;
				}
				
				// Add or update the item.
				ps1.setInt(1, item.getOwnerId());
				ps1.setInt(2, item.getObjectId());
				ps1.setInt(3, item.getItemId());
				ps1.setInt(4, item.getCount());
				ps1.setInt(5, item.getEnchantLevel());
				ps1.setString(6, item.getLocation().name());
				ps1.setInt(7, item.getLocationSlot());
				ps1.setInt(8, item.getCustomType1());
				ps1.setInt(9, item.getCustomType2());
				ps1.setInt(10, item.getManaLeft());
				ps1.setLong(11, item.getTime());
				ps1.addBatch();
				
				// For Augmentation, verify only Weapons.
				if (isWeapon)
				{
					// Delete the augmentation.
					final Augmentation aug = item.getAugmentation();
					if (item.getAugmentation() == null)
					{
						ps4.setInt(1, item.getObjectId());
						ps4.addBatch();
					}
					// Add the augmentation.
					else
					{
						ps2.setInt(1, item.getObjectId());
						ps2.setInt(2, aug.getId());
						
						if (aug.getSkill() == null)
						{
							ps2.setInt(3, 0);
							ps2.setInt(4, 0);
						}
						else
						{
							ps2.setInt(3, aug.getSkill().getId());
							ps2.setInt(4, aug.getSkill().getLevel());
						}
						ps2.addBatch();
					}
				}
			}
			
			ps1.executeBatch();
			ps2.executeBatch();
			ps3.executeBatch();
			ps4.executeBatch();
			ps5.executeBatch();
		}
		catch (Exception e)
		{
			LOGGER.error("Couldn't manage items.", e);
		}
		
		// Release all items.
		items.clear();
	}
	
	/**
	 * Manually trigger the task. Used by shutdown process.
	 */
	public void save()
	{
		updateItems(_items);
	}
	
	public static final ItemInstanceTaskManager getInstance()
	{
		return SingletonHolder.INSTANCE;
	}
	
	private static class SingletonHolder
	{
		protected static final ItemInstanceTaskManager INSTANCE = new ItemInstanceTaskManager();
	}
}