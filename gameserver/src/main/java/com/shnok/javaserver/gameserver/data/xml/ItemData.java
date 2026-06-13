package com.shnok.javaserver.gameserver.data.xml;

import java.io.File;
import java.util.HashMap;
import java.util.Map;

import com.shnok.javaserver.commons.logging.CLogger;

import com.shnok.javaserver.gameserver.data.DocumentItem;
import com.shnok.javaserver.gameserver.model.item.kind.Armor;
import com.shnok.javaserver.gameserver.model.item.kind.EtcItem;
import com.shnok.javaserver.gameserver.model.item.kind.Item;
import com.shnok.javaserver.gameserver.model.item.kind.Weapon;

/**
 * This class loads and stores all {@link Item} templates.
 */
public class ItemData
{
	private static final CLogger LOGGER = new CLogger(ItemData.class.getName());
	
	private Item[] _templates;
	
	protected ItemData()
	{
		load();
	}
	
	public void reload()
	{
		load();
	}
	
	private void load()
	{
		final File dir = new File("data/xml/items");
		
		final Map<Integer, Armor> armors = new HashMap<>();
		final Map<Integer, EtcItem> etcItems = new HashMap<>();
		final Map<Integer, Weapon> weapons = new HashMap<>();
		
		int highest = 0;
		for (File file : dir.listFiles())
		{
			DocumentItem document = new DocumentItem(file);
			document.parse();
			
			for (Item item : document.getItemList())
			{
				if (highest < item.getItemId())
					highest = item.getItemId();
				
				if (item instanceof EtcItem etcItem)
					etcItems.put(item.getItemId(), etcItem);
				else if (item instanceof Armor armor)
					armors.put(item.getItemId(), armor);
				else if (item instanceof Weapon weapon)
					weapons.put(item.getItemId(), weapon);
			}
		}
		
		// Feed an array with all items templates.
		_templates = new Item[highest + 1];
		
		for (Armor item : armors.values())
			_templates[item.getItemId()] = item;
		
		for (Weapon item : weapons.values())
			_templates[item.getItemId()] = item;
		
		for (EtcItem item : etcItems.values())
			_templates[item.getItemId()] = item;
		
		LOGGER.info("Loaded items.");
	}
	
	/**
	 * @param id : the item id to check.
	 * @return the {@link Item} corresponding to the item id.
	 */
	public Item getTemplate(int id)
	{
		return (id >= _templates.length) ? null : _templates[id];
	}
	
	/**
	 * @return The array of all {@link Item} templates.
	 */
	public Item[] getTemplates()
	{
		return _templates;
	}
	
	public static ItemData getInstance()
	{
		return SingletonHolder.INSTANCE;
	}
	
	private static class SingletonHolder
	{
		protected static final ItemData INSTANCE = new ItemData();
	}
}