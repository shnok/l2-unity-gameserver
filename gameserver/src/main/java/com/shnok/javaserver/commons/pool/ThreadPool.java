package com.shnok.javaserver.commons.pool;

import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.ScheduledFuture;
import java.util.concurrent.TimeUnit;

import com.shnok.javaserver.commons.logging.CLogger;

/**
 * This class handles threads, using {@link Executors} backed by virtual threads :
 * <ul>
 * <li>Scheduled pool keeps a track about incoming, future events.</li>
 * <li>Instant pool handles short-life events.</li>
 * </ul>
 */
public final class ThreadPool
{
	private ThreadPool()
	{
		throw new IllegalStateException("Utility class");
	}
	
	private static final CLogger LOGGER = new CLogger(ThreadPool.class.getName());
	
	private static ScheduledExecutorService _scheduledFactory;
	
	/**
	 * Initialize the factories, based on available processors.
	 */
	public static void init()
	{
		_scheduledFactory = Executors.newScheduledThreadPool(Runtime.getRuntime().availableProcessors(), Thread.ofVirtual().name("scheduled-", 0).factory());
		
		LOGGER.info("Initializing ThreadPool.");
	}
	
	/**
	 * Schedule a one-shot action activating after a delay.
	 * @param r : the {@link Runnable} to execute.
	 * @param delay : the time from now to delay execution.
	 * @return a {@link ScheduledFuture} representing pending completion of the task and whose get() method will return null upon completion.
	 */
	public static ScheduledFuture<?> schedule(Runnable r, long delay)
	{
		try
		{
			return _scheduledFactory.schedule(new TaskWrapper(r), delay, TimeUnit.MILLISECONDS);
		}
		catch (Exception e)
		{
			LOGGER.error("ThreadPool#schedule failed upon {}.", e, r.toString());
			return null;
		}
	}
	
	/**
	 * Schedule a periodic action activating after a delay.
	 * @param r : the {@link Runnable} to execute.
	 * @param delay : the time from now to delay execution.
	 * @param period : the period between successive executions.
	 * @return a {@link ScheduledFuture} representing pending completion of the task and whose get() method will throw an exception upon cancellation.
	 */
	public static ScheduledFuture<?> scheduleAtFixedRate(Runnable r, long delay, long period)
	{
		try
		{
			return _scheduledFactory.scheduleAtFixedRate(new TaskWrapper(r), delay, period, TimeUnit.MILLISECONDS);
		}
		catch (Exception e)
		{
			LOGGER.error("ThreadPool#scheduleAtFixedRate failed upon {}.", e, r.toString());
			return null;
		}
	}
	
	/**
	 * Execute the given task.
	 * @param r : the {@link Runnable} to execute.
	 */
	public static void execute(Runnable r)
	{
		try
		{
			Thread.ofVirtual().start(new TaskWrapper(r));
		}
		catch (Exception e)
		{
			LOGGER.error("ThreadPool#execute failed upon {}.", e, r.toString());
		}
	}
	
	/**
	 * Shutdown factories instantly.
	 */
	public static void shutdown()
	{
		try
		{
			LOGGER.info("ThreadPool: Shutting down.");
			
			_scheduledFactory.shutdownNow();
		}
		catch (Exception e)
		{
			LOGGER.error("Exception during shutdown.", e);
		}
	}
	
	private static final class TaskWrapper implements Runnable
	{
		private final Runnable _runnable;
		
		private TaskWrapper(Runnable runnable)
		{
			_runnable = runnable;
		}
		
		@Override
		public void run()
		{
			try
			{
				_runnable.run();
			}
			catch (RuntimeException e)
			{
				LOGGER.error("Exception in a ThreadPool task execution.", e);
			}
		}
	}
}