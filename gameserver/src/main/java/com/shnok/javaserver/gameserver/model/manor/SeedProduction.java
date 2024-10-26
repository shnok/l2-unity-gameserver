package com.shnok.javaserver.gameserver.model.manor;

import java.util.concurrent.atomic.AtomicInteger;

public class SeedProduction
{
	private final int _seedId;
	private final int _price;
	private final int _startAmount;
	private final AtomicInteger _amount;
	
	public SeedProduction(int id, int amount, int price, int startAmount)
	{
		_seedId = id;
		_amount = new AtomicInteger(amount);
		_price = price;
		_startAmount = startAmount;
	}
	
	public final int getId()
	{
		return _seedId;
	}
	
	public final int getAmount()
	{
		return _amount.get();
	}
	
	public final int getPrice()
	{
		return _price;
	}
	
	public final int getStartAmount()
	{
		return _startAmount;
	}
	
	public final void setAmount(int amount)
	{
		_amount.set(amount);
	}
	
	public final boolean decreaseAmount(int val)
	{
		int current;
		int next;
		
		do
		{
			current = _amount.get();
			next = current - val;
			
			if (next < 0)
				return false;
		}
		while (!_amount.compareAndSet(current, next));
		
		return true;
	}
}