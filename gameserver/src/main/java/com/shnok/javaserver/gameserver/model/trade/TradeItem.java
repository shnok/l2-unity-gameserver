package com.shnok.javaserver.gameserver.model.trade;

import com.shnok.javaserver.gameserver.model.item.instance.ItemInstance;
import com.shnok.javaserver.gameserver.model.item.kind.Item;

public class TradeItem extends ItemRequest
{
	private Item _item;
	private int _quantity;
	
	public TradeItem(ItemInstance item, int count, int price)
	{
		super(item.getObjectId(), item.getItem().getItemId(), count, price, item.getEnchantLevel());
		
		_item = item.getItem();
		_quantity = count;
	}
	
	public TradeItem(Item item, int count, int price, int enchant)
	{
		super(0, item.getItemId(), count, price, enchant);
		
		_item = item;
		_quantity = count;
	}
	
	public TradeItem(TradeItem item, int count, int price)
	{
		super(item.getObjectId(), item.getItemId(), count, price, item.getEnchant());
		
		_item = item.getItem();
		_quantity = count;
	}
	
	@Override
	public String toString()
	{
		return "TradeItem [item=" + _item + ", quantity=" + _quantity + ", objectId=" + _objectId + ", itemId=" + _itemId + ", count=" + _count + ", price=" + _price + ", enchant=" + _enchant + "]";
	}
	
	public Item getItem()
	{
		return _item;
	}
	
	public int getQuantity()
	{
		return _quantity;
	}
	
	public void setQuantity(int quantity)
	{
		_quantity = quantity;
	}
}