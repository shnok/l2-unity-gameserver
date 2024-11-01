package com.shnok.javaserver.gameserver.taskmanager;

import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;

import com.shnok.javaserver.commons.pool.ThreadPool;

import com.shnok.javaserver.gameserver.model.boat.BoatEngine;

public final class BoatTaskManager implements Runnable
{
	private final Set<BoatEngine> _engines = ConcurrentHashMap.newKeySet();
	
	protected BoatTaskManager()
	{
		// Run task each second.
		ThreadPool.scheduleAtFixedRate(this, 1000, 1000);
	}
	
	@Override
	public final void run()
	{
		// List is empty, skip.
		if (_engines.isEmpty())
			return;
		
		// Run each engine.
		for (BoatEngine engine : _engines)
		{
			if (engine.canRun())
				engine.run();
		}
	}
	
	public final void add(BoatEngine engine)
	{
		_engines.add(engine);
	}
	
	public final void remove(BoatEngine engine)
	{
		_engines.remove(engine);
	}
	
	public void clear()
	{
		_engines.clear();
	}
	
	public static final BoatTaskManager getInstance()
	{
		return SingletonHolder.INSTANCE;
	}
	
	private static class SingletonHolder
	{
		protected static final BoatTaskManager INSTANCE = new BoatTaskManager();
	}
}