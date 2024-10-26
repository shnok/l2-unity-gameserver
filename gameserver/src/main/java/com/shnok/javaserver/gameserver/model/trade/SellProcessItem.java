package net.sf.l2j.gameserver.model.trade;

public record SellProcessItem(int objectId, int count, int price)
{
	public long getPrice()
	{
		return (long) count * price;
	}
	
	public boolean addToTradeList(TradeList list)
	{
		return list.addItem(objectId, count, price) != null;
	}
}