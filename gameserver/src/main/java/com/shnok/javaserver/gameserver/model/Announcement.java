package com.shnok.javaserver.gameserver.model;

import java.util.concurrent.ScheduledFuture;

import net.sf.l2j.commons.pool.ThreadPool;

/**
 * A datatype used to retain informations for announcements. It notably holds a {@link ScheduledFuture}.
 */
public class Announcement implements Runnable
{
	protected final String _message;
	
	protected boolean _critical;
	protected boolean _auto;
	protected boolean _unlimited;
	
	protected int _initialDelay;
	protected int _delay;
	protected int _limit;
	protected int _tempLimit; // Temporary limit, used by current timer.
	
	protected ScheduledFuture<?> _task;
	
	public Announcement(String message, boolean critical)
	{
		_message = message;
		_critical = critical;
	}
	
	public Announcement(String message, boolean critical, boolean auto, int initialDelay, int delay, int limit)
	{
		_message = message;
		_critical = critical;
		_auto = auto;
		_initialDelay = initialDelay;
		_delay = delay;
		_limit = limit;
		
		if (_auto)
		{
			switch (_limit)
			{
				case 0: // unlimited
					_task = ThreadPool.scheduleAtFixedRate(this, _initialDelay * 1000L, _delay * 1000L); // self schedule at fixed rate
					_unlimited = true;
					break;
				
				default:
					_task = ThreadPool.schedule(this, _initialDelay * 1000L); // self schedule (initial)
					_tempLimit = _limit;
					break;
			}
		}
	}
	
	@Override
	public void run()
	{
		if (!_unlimited)
		{
			if (_tempLimit == 0)
				return;
			
			_task = ThreadPool.schedule(this, _delay * 1000L); // self schedule (worker)
			_tempLimit--;
		}
		World.announceToOnlinePlayers(_message, _critical);
	}
	
	public String getMessage()
	{
		return _message;
	}
	
	public boolean isCritical()
	{
		return _critical;
	}
	
	public boolean isAuto()
	{
		return _auto;
	}
	
	public int getInitialDelay()
	{
		return _initialDelay;
	}
	
	public int getDelay()
	{
		return _delay;
	}
	
	public int getLimit()
	{
		return _limit;
	}
	
	public void stopTask()
	{
		if (_task != null)
		{
			_task.cancel(true);
			_task = null;
		}
	}
	
	public void reloadTask()
	{
		stopTask();
		
		if (_auto)
		{
			switch (_limit)
			{
				case 0: // unlimited
					_task = ThreadPool.scheduleAtFixedRate(this, _initialDelay * 1000L, _delay * 1000L); // self schedule at fixed rate
					_unlimited = true;
					break;
				
				default:
					_task = ThreadPool.schedule(this, _initialDelay * 1000L); // self schedule (initial)
					_tempLimit = _limit;
					break;
			}
		}
	}
}