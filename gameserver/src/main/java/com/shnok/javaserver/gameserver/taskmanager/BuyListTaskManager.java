package com.shnok.javaserver.gameserver.taskmanager;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

import com.shnok.javaserver.commons.pool.ThreadPool;

import com.shnok.javaserver.gameserver.model.buylist.Product;

/**
 * Handles individual {@link Product} restock timers.<br>
 * A timer is set, then on activation it restocks and releases it from the map. Additionally, some SQL action is done.
 */
public final class BuyListTaskManager implements Runnable
{
	private final Map<Product, Long> _products = new ConcurrentHashMap<>();
	
	protected BuyListTaskManager()
	{
		// Run task each second.
		ThreadPool.scheduleAtFixedRate(this, 1000, 1000);
	}
	
	@Override
	public final void run()
	{
		// List is empty, skip.
		if (_products.isEmpty())
			return;
		
		// Get current time.
		final long time = System.currentTimeMillis();
		
		// Loop all characters.
		for (Map.Entry<Product, Long> entry : _products.entrySet())
		{
			// Time hasn't passed yet, skip.
			if (time < entry.getValue())
				continue;
			
			final Product product = entry.getKey();
			product.setCount(product.getMaxCount());
			product.delete();
			
			_products.remove(product);
		}
	}
	
	/**
	 * Adds a {@link Product} to the task. A product can't be added twice.
	 * @param product : {@link Product} to be added.
	 * @param interval : Interval in minutes, after which the task is triggered.
	 */
	public final void add(Product product, long interval)
	{
		final long newRestockTime = System.currentTimeMillis() + interval;
		if (_products.putIfAbsent(product, newRestockTime) == null)
			product.save(newRestockTime);
	}
	
	/**
	 * Test the timer : if already gone, reset the count without adding the {@link Product} to the task. A product can't be added twice.
	 * @param product : {@link Product} to be added.
	 * @param currentCount : the amount to set, if remaining time succeeds.
	 * @param nextRestockTime : time in milliseconds.
	 */
	public final void test(Product product, int currentCount, long nextRestockTime)
	{
		if (nextRestockTime - System.currentTimeMillis() > 0)
		{
			product.setCount(currentCount);
			_products.putIfAbsent(product, nextRestockTime);
		}
		else
		{
			product.setCount(product.getMaxCount());
			product.delete();
		}
	}
	
	public static final BuyListTaskManager getInstance()
	{
		return SingletonHolder.INSTANCE;
	}
	
	private static final class SingletonHolder
	{
		protected static final BuyListTaskManager INSTANCE = new BuyListTaskManager();
	}
}