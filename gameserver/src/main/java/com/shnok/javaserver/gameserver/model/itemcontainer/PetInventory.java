package com.shnok.javaserver.gameserver.model.itemcontainer;

import com.shnok.javaserver.gameserver.enums.items.EtcItemType;
import com.shnok.javaserver.gameserver.enums.items.ItemLocation;
import com.shnok.javaserver.gameserver.model.actor.Player;
import com.shnok.javaserver.gameserver.model.actor.instance.Pet;
import com.shnok.javaserver.gameserver.model.item.instance.ItemInstance;

public class PetInventory extends Inventory
{
	public PetInventory(Pet owner)
	{
		super(owner);
	}
	
	@Override
	public Pet getOwner()
	{
		return (Pet) _owner;
	}
	
	@Override
	public int getOwnerId()
	{
		int id;
		try
		{
			id = getOwner().getOwner().getObjectId();
		}
		catch (NullPointerException e)
		{
			return 0;
		}
		return id;
	}
	
	@Override
	public boolean updateWeight()
	{
		if (!super.updateWeight())
			return false;
		
		getOwner().updateAndBroadcastStatus(1);
		getOwner().sendPetInfosToOwner();
		return true;
	}
	
	public boolean validateCapacity(ItemInstance item)
	{
		int slots = 0;
		
		if (!(item.isStackable() && getItemByItemId(item.getItemId()) != null) && item.getItemType() != EtcItemType.HERB)
			slots++;
		
		return validateCapacity(slots);
	}
	
	@Override
	public boolean validateCapacity(int slotCount)
	{
		if (slotCount == 0)
			return true;
		
		return _items.size() + slotCount <= getOwner().getInventoryLimit();
	}
	
	public boolean validateWeight(ItemInstance item, int count)
	{
		return validateWeight(count * item.getItem().getWeight());
	}
	
	@Override
	public boolean validateWeight(int weight)
	{
		return _totalWeight + weight <= _owner.getWeightLimit();
	}
	
	@Override
	protected ItemLocation getBaseLocation()
	{
		return ItemLocation.PET;
	}
	
	@Override
	protected ItemLocation getEquipLocation()
	{
		return ItemLocation.PET_EQUIP;
	}
	
	@Override
	public void deleteMe()
	{
		final Player petOwner = getOwner().getOwner();
		if (petOwner != null)
		{
			for (ItemInstance item : _items)
			{
				if (petOwner.getInventory().validateCapacity(1))
					getOwner().transferItem(item.getObjectId(), item.getCount(), petOwner);
				else
				{
					final ItemInstance droppedItem = dropItem(item.getObjectId(), item.getCount());
					droppedItem.dropMe(getOwner(), 70);
				}
			}
		}
		
		_items.clear();
	}
}