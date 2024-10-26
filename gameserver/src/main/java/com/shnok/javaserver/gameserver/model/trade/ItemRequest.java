package com.shnok.javaserver.gameserver.model.trade;

public class ItemRequest
{
	protected int _objectId;
	protected int _itemId;
	protected int _count;
	protected int _price;
	protected int _enchant;
	
	public ItemRequest(int objectId, int count, int price)
	{
		_objectId = objectId;
		_count = count;
		_price = price;
	}
	
	public ItemRequest(int objectId, int itemId, int count, int price, int enchant)
	{
		_objectId = objectId;
		_itemId = itemId;
		_count = count;
		_price = price;
		_enchant = enchant;
	}
	
	@Override
	public String toString()
	{
		return "ItemRequest [objectId=" + _objectId + ", itemId=" + _itemId + ", count=" + _count + ", price=" + _price + ", enchant=" + _enchant + "]";
	}
	
	public int getObjectId()
	{
		return _objectId;
	}
	
	public void setObjectId(int objectId)
	{
		_objectId = objectId;
	}
	
	public int getItemId()
	{
		return _itemId;
	}
	
	public int getCount()
	{
		return _count;
	}
	
	public void setCount(int count)
	{
		_count = count;
	}
	
	public int getPrice()
	{
		return _price;
	}
	
	public void setPrice(int price)
	{
		_price = price;
	}
	
	public int getEnchant()
	{
		return _enchant;
	}
	
	public void setEnchant(int enchant)
	{
		_enchant = enchant;
	}
}