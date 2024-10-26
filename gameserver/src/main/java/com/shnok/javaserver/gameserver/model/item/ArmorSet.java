package com.shnok.javaserver.gameserver.model.item;

import com.shnok.javaserver.commons.data.StatSet;

import com.shnok.javaserver.gameserver.enums.Paperdoll;
import com.shnok.javaserver.gameserver.model.actor.Player;
import com.shnok.javaserver.gameserver.model.item.instance.ItemInstance;

public final class ArmorSet
{
	private final String _name;
	
	private final int[] _set = new int[5];
	
	private final int _skillId;
	private final int _shield;
	private final int _shieldSkillId;
	private final int _enchant6Skill;
	
	public ArmorSet(StatSet set)
	{
		_name = set.getString("name");
		
		_set[0] = set.getInteger("chest");
		_set[1] = set.getInteger("legs");
		_set[2] = set.getInteger("head");
		_set[3] = set.getInteger("gloves");
		_set[4] = set.getInteger("feet");
		
		_skillId = set.getInteger("skillId");
		_shield = set.getInteger("shield");
		_shieldSkillId = set.getInteger("shieldSkillId");
		_enchant6Skill = set.getInteger("enchant6Skill");
	}
	
	@Override
	public String toString()
	{
		return _name;
		
	}
	
	public int[] getSetItemsId()
	{
		return _set;
	}
	
	public int getShield()
	{
		return _shield;
	}
	
	public int getSkillId()
	{
		return _skillId;
	}
	
	public int getShieldSkillId()
	{
		return _shieldSkillId;
	}
	
	public int getEnchant6skillId()
	{
		return _enchant6Skill;
	}
	
	/**
	 * @param player : The {@link Player} to test.
	 * @return True if the {@link Player} equipped the basic {@link ArmorSet} (without shield).
	 */
	public boolean containsAll(Player player)
	{
		int legs = 0;
		final ItemInstance legsItem = player.getInventory().getItemFrom(Paperdoll.LEGS);
		if (legsItem != null)
			legs = legsItem.getItemId();
		
		if (_set[1] != 0 && _set[1] != legs)
			return false;
		
		int head = 0;
		final ItemInstance headItem = player.getInventory().getItemFrom(Paperdoll.HEAD);
		if (headItem != null)
			head = headItem.getItemId();
		
		if (_set[2] != 0 && _set[2] != head)
			return false;
		
		int gloves = 0;
		final ItemInstance glovesItem = player.getInventory().getItemFrom(Paperdoll.GLOVES);
		if (glovesItem != null)
			gloves = glovesItem.getItemId();
		
		if (_set[3] != 0 && _set[3] != gloves)
			return false;
		
		int feet = 0;
		final ItemInstance feetItem = player.getInventory().getItemFrom(Paperdoll.FEET);
		if (feetItem != null)
			feet = feetItem.getItemId();
		
		if (_set[4] != 0 && _set[4] != feet)
			return false;
		
		return true;
	}
	
	public boolean containsItem(Paperdoll slot, int itemId)
	{
		switch (slot)
		{
			case CHEST:
				return _set[0] == itemId;
			
			case LEGS:
				return _set[1] == itemId;
			
			case HEAD:
				return _set[2] == itemId;
			
			case GLOVES:
				return _set[3] == itemId;
			
			case FEET:
				return _set[4] == itemId;
			
			default:
				return false;
		}
	}
	
	public boolean containsShield(Player player)
	{
		final ItemInstance item = player.getSecondaryWeaponInstance();
		return item != null && item.getItemId() == _shield;
	}
	
	public boolean containsShield(int shieldId)
	{
		return _shield != 0 && _shield == shieldId;
	}
	
	/**
	 * @param player : The {@link Player} to test.
	 * @return True if all parts of this {@link ArmorSet} are enchanted to +6 or more.
	 */
	public boolean isEnchanted6(Player player)
	{
		final ItemInstance chestItem = player.getInventory().getItemFrom(Paperdoll.CHEST);
		if (chestItem.getEnchantLevel() < 6)
			return false;
		
		int legs = 0;
		final ItemInstance legsItem = player.getInventory().getItemFrom(Paperdoll.LEGS);
		if (legsItem != null && legsItem.getEnchantLevel() > 5)
			legs = legsItem.getItemId();
		
		if (_set[1] != 0 && _set[1] != legs)
			return false;
		
		int head = 0;
		final ItemInstance headItem = player.getInventory().getItemFrom(Paperdoll.HEAD);
		if (headItem != null && headItem.getEnchantLevel() > 5)
			head = headItem.getItemId();
		
		if (_set[2] != 0 && _set[2] != head)
			return false;
		
		int gloves = 0;
		final ItemInstance glovesItem = player.getInventory().getItemFrom(Paperdoll.GLOVES);
		if (glovesItem != null && glovesItem.getEnchantLevel() > 5)
			gloves = glovesItem.getItemId();
		
		if (_set[3] != 0 && _set[3] != gloves)
			return false;
		
		int feet = 0;
		final ItemInstance feetItem = player.getInventory().getItemFrom(Paperdoll.FEET);
		if (feetItem != null && feetItem.getEnchantLevel() > 5)
			feet = feetItem.getItemId();
		
		if (_set[4] != 0 && _set[4] != feet)
			return false;
		
		return true;
	}
}