package com.shnok.javaserver.commons.network;

import java.net.Socket;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

import com.shnok.javaserver.commons.mmocore.IAcceptFilter;

public class IPv4Filter implements IAcceptFilter, Runnable
{
	private static final long SLEEP_TIME = 5000;
	
	private final Map<Integer, FloodHolder> _floods = new ConcurrentHashMap<>();
	
	public IPv4Filter()
	{
		final Thread t = new Thread(this);
		t.setName(getClass().getSimpleName());
		t.setDaemon(true);
		t.start();
	}
	
	@Override
	public boolean accept(Socket socket)
	{
		final int hash = hash(socket.getInetAddress().getAddress());
		
		final FloodHolder flood = _floods.get(hash);
		if (flood != null)
		{
			final long currentTime = System.currentTimeMillis();
			
			if (flood.tries == -1)
			{
				flood.lastAccess = currentTime;
				return false;
			}
			
			if (flood.lastAccess + 1000 > currentTime)
			{
				flood.lastAccess = currentTime;
				
				if (flood.tries >= 3)
				{
					flood.tries = -1;
					return false;
				}
				
				flood.tries++;
			}
			else
				flood.lastAccess = currentTime;
		}
		else
			_floods.put(hash, new FloodHolder());
		
		return true;
	}
	
	@Override
	public void run()
	{
		while (true)
		{
			final long referenceTime = System.currentTimeMillis() - (1000 * 300);
			
			_floods.values().removeIf(f -> f.lastAccess < referenceTime);
			
			try
			{
				Thread.sleep(SLEEP_TIME);
			}
			catch (InterruptedException e)
			{
				return;
			}
		}
	}
	
	protected final class FloodHolder
	{
		protected long lastAccess = System.currentTimeMillis();
		protected int tries;
	}
	
	private static final int hash(byte[] ip)
	{
		return ip[0] & 0xFF | ip[1] << 8 & 0xFF00 | ip[2] << 16 & 0xFF0000 | ip[3] << 24 & 0xFF000000;
	}
}