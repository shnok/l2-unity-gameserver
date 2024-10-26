package com.shnok.javaserver.gameserver.model.multisell;

import net.sf.l2j.commons.data.StatSet;

import net.sf.l2j.gameserver.data.xml.ItemData;
import net.sf.l2j.gameserver.model.item.kind.Armor;
import net.sf.l2j.gameserver.model.item.kind.Item;
import net.sf.l2j.gameserver.model.item.kind.Weapon;

/**
 * A datatype which is part of multisell system. It is either the "result" or the "required part" of a multisell action.
 */
public class Ingredient
{
	private int _itemId;
	private int _itemCount;
	private int _enchantmentLevel;
	
	private boolean _isTaxIngredient;
	private boolean _maintainIngredient;
	
	private Item _template = null;
	
	public Ingredient(StatSet set)
	{
		this(set.getInteger("id"), set.getInteger("count"), set.getBool("isTaxIngredient", false), set.getBool("maintainIngredient", false));
	}
	
	public Ingredient(int itemId, int itemCount, boolean isTaxIngredient, boolean maintainIngredient)
	{
		_itemId = itemId;
		_itemCount = itemCount;
		_isTaxIngredient = isTaxIngredient;
		_maintainIngredient = maintainIngredient;
		
		if (_itemId > 0)
			_template = ItemData.getInstance().getTemplate(_itemId);
	}
	
	/**
	 * @return a new Ingredient instance with the same values as this.
	 */
	public Ingredient getCopy()
	{
		return new Ingredient(_itemId, _itemCount, _isTaxIngredient, _maintainIngredient);
	}
	
	public final int getItemId()
	{
		return _itemId;
	}
	
	public final void setItemId(int itemId)
	{
		_itemId = itemId;
	}
	
	public final int getItemCount()
	{
		return _itemCount;
	}
	
	public final void setItemCount(int itemCount)
	{
		_itemCount = itemCount;
	}
	
	public final int getEnchantLevel()
	{
		return _enchantmentLevel;
	}
	
	public final void setEnchantLevel(int enchantmentLevel)
	{
		_enchantmentLevel = enchantmentLevel;
	}
	
	public final boolean isTaxIngredient()
	{
		return _isTaxIngredient;
	}
	
	public final void setIsTaxIngredient(boolean isTaxIngredient)
	{
		_isTaxIngredient = isTaxIngredient;
	}
	
	public final boolean getMaintainIngredient()
	{
		return _maintainIngredient;
	}
	
	public final void setMaintainIngredient(boolean maintainIngredient)
	{
		_maintainIngredient = maintainIngredient;
	}
	
	public final Item getTemplate()
	{
		return _template;
	}
	
	public final boolean isStackable()
	{
		return _template == null || _template.isStackable();
	}
	
	public final boolean isArmorOrWeapon()
	{
		return _template instanceof Armor || _template instanceof Weapon;
	}
	
	public final int getWeight()
	{
		return (_template == null) ? 0 : _template.getWeight();
	}
}