package com.shnok.javaserver.gameserver.model.item.instance;

import com.shnok.javaserver.gameserver.enums.items.ItemState;
import com.shnok.javaserver.gameserver.model.item.kind.Item;

/**
 * Get all information from ItemInstance to generate ItemInfo.
 */
public class ItemInfo
{
	private int _objectId;
	private int _enchant;
	private int _augmentation;
	private int _count;
	private int _type1;
	private int _type2;
	private int _equipped;
	private int _manaLeft;
	
	private Item _item;
	
	private ItemState _state;
	
	public ItemInfo(ItemInstance item, ItemState state)
	{
		if (item == null)
			return;
		
		_objectId = item.getObjectId();
		_enchant = item.getEnchantLevel();
		_augmentation = (item.isAugmented()) ? item.getAugmentation().getId() : 0;
		_count = item.getCount();
		_type1 = item.getCustomType1();
		_type2 = item.getCustomType2();
		_equipped = item.isEquipped() ? 1 : 0;
		_manaLeft = item.getManaLeft();
		
		_item = item.getItem();
		
		_state = state;
	}
	
	public int getObjectId()
	{
		return _objectId;
	}
	
	public Item getItem()
	{
		return _item;
	}
	
	public int getEnchant()
	{
		return _enchant;
	}
	
	public int getAugmentation()
	{
		return _augmentation;
	}
	
	public int getCount()
	{
		return _count;
	}
	
	public void setCount(int count)
	{
		_count = count;
	}
	
	public int getCustomType1()
	{
		return _type1;
	}
	
	public int getCustomType2()
	{
		return _type2;
	}
	
	public int getEquipped()
	{
		return _equipped;
	}
	
	public ItemState getState()
	{
		return _state;
	}
	
	public int getManaLeft()
	{
		return _manaLeft;
	}
	
	public int getDisplayedManaLeft()
	{
		return _manaLeft / 60;
	}
}