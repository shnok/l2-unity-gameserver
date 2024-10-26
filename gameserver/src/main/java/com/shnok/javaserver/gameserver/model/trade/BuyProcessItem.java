package com.shnok.javaserver.gameserver.model.trade;

public record BuyProcessItem(int itemId, int count, int price, int enchant)
{
	public long getCost()
	{
		return (long) count * price;
	}
	
	public boolean addToTradeList(TradeList list)
	{
		return list.addItemByItemId(itemId, count, price, enchant) != null;
	}
}