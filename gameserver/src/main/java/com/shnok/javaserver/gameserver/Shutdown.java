package com.shnok.javaserver.gameserver;

import com.shnok.javaserver.commons.lang.StringUtil;
import com.shnok.javaserver.commons.logging.CLogger;
import com.shnok.javaserver.commons.network.ServerType;
import com.shnok.javaserver.commons.pool.ConnectionPool;
import com.shnok.javaserver.commons.pool.ThreadPool;

import com.shnok.javaserver.Config;
import com.shnok.javaserver.gameserver.data.manager.BufferManager;
import com.shnok.javaserver.gameserver.data.manager.CastleManorManager;
import com.shnok.javaserver.gameserver.data.manager.ClanHallManager;
import com.shnok.javaserver.gameserver.data.manager.CoupleManager;
import com.shnok.javaserver.gameserver.data.manager.FestivalOfDarknessManager;
import com.shnok.javaserver.gameserver.data.manager.FishingChampionshipManager;
import com.shnok.javaserver.gameserver.data.manager.HeroManager;
import com.shnok.javaserver.gameserver.data.manager.PetitionManager;
import com.shnok.javaserver.gameserver.data.manager.RelationManager;
import com.shnok.javaserver.gameserver.data.manager.SevenSignsManager;
import com.shnok.javaserver.gameserver.data.manager.SpawnManager;
import com.shnok.javaserver.gameserver.data.manager.ZoneManager;
import com.shnok.javaserver.gameserver.data.sql.ServerMemoTable;
import com.shnok.javaserver.gameserver.data.xml.ScriptData;
import com.shnok.javaserver.gameserver.model.World;
import com.shnok.javaserver.gameserver.model.actor.Player;
import com.shnok.javaserver.gameserver.model.olympiad.Olympiad;
import com.shnok.javaserver.gameserver.network.GameClient;
import com.shnok.javaserver.gameserver.network.SystemMessageId;
import com.shnok.javaserver.gameserver.network.gameserverpackets.ServerStatus;
import com.shnok.javaserver.gameserver.network.serverpackets.auth.ServerClose;
import com.shnok.javaserver.gameserver.network.serverpackets.SystemMessage;
import com.shnok.javaserver.gameserver.taskmanager.ItemInstanceTaskManager;
import com.shnok.javaserver.gameserver.taskmanager.ItemsOnGroundTaskManager;

/**
 * This class provides functions for shutting down and restarting the server. It closes all client connections and saves data.
 */
public class Shutdown extends Thread
{
	private static final CLogger LOGGER = new CLogger(Shutdown.class.getName());
	
	private static Shutdown _counterInstance = null;
	
	private int _secondsShut;
	private int _shutdownMode;
	
	public static final int SIGTERM = 0;
	public static final int GM_SHUTDOWN = 1;
	public static final int GM_RESTART = 2;
	public static final int ABORT = 3;
	private static final String[] MODE_TEXT =
	{
		"SIGTERM",
		"shutting down",
		"restarting",
		"aborting"
	};
	
	protected Shutdown()
	{
		_secondsShut = -1;
		_shutdownMode = SIGTERM;
	}
	
	public Shutdown(int seconds, boolean restart)
	{
		_secondsShut = Math.max(0, seconds);
		_shutdownMode = (restart) ? GM_RESTART : GM_SHUTDOWN;
	}
	
	/**
	 * This function is called, when a new thread starts if this thread is the thread of getInstance, then this is the shutdown hook and we save all data and disconnect all clients.<br>
	 * <br>
	 * After this thread ends, the server will completely exit if this is not the thread of getInstance, then this is a countdown thread.<br>
	 * <br>
	 * We start the countdown, and when we finished it, and it was not aborted, we tell the shutdown-hook why we call exit, and then call exit when the exit status of the server is 1, startServer.sh / startServer.bat will restart the server.
	 */
	@Override
	public void run()
	{
		if (this == SingletonHolder.INSTANCE)
		{
			StringUtil.printSection("Under " + MODE_TEXT[_shutdownMode] + " process");
			
			// Disconnect players.
			try
			{
				disconnectAllPlayers();
				LOGGER.info("Players have been disconnected.");
			}
			catch (Exception e)
			{
				// Silent catch.
			}
			
			// Close communication with LoginServerThread.
			try
			{
				LoginServerThread.getInstance().interrupt();
			}
			catch (Exception e)
			{
				// Silent catch.
			}
			
			// Save Festival of Darkness.
			if (!SevenSignsManager.getInstance().isSealValidationPeriod())
			{
				FestivalOfDarknessManager.getInstance().saveFestivalData(false);
				LOGGER.info("FestivalOfDarknessManager has been saved.");
			}
			
			// Save Seven Signs.
			SevenSignsManager.getInstance().saveSevenSignsData();
			SevenSignsManager.getInstance().saveSevenSignsStatus();
			LOGGER.info("SevenSignsManager has been saved.");
			
			// Stop all running scripts.
			ScriptData.getInstance().stopAllScripts();
			LOGGER.info("Running scripts have been stopped.");
			
			// Save allowed Players on BossZone.
			ZoneManager.getInstance().save();
			LOGGER.info("ZoneManager has been saved.");
			
			// Save SpawnDatas.
			SpawnManager.getInstance().save();
			LOGGER.info("SpawnManager has been saved.");
			
			// Save Olympiads.
			Olympiad.getInstance().saveOlympiadStatus();
			LOGGER.info("Olympiad has been saved.");
			
			// Save Hero data.
			HeroManager.getInstance().shutdown();
			LOGGER.info("HeroManager has been saved.");
			
			// Save manor data.
			CastleManorManager.getInstance().storeMe();
			LOGGER.info("CastleManorManager has been saved.");
			
			// Save Fishing tournament data.
			FishingChampionshipManager.getInstance().shutdown();
			LOGGER.info("FishingChampionshipManager has been saved.");
			
			// Save schemes.
			BufferManager.getInstance().saveSchemes();
			LOGGER.info("BufferManager has been saved.");
			
			// Save Petitions.
			PetitionManager.getInstance().store();
			LOGGER.info("PetitionManager has been saved.");
			
			// Save ClanHall attackers.
			ClanHallManager.getInstance().save();
			LOGGER.info("ClanHallManager has been saved.");
			
			// Save Relations.
			RelationManager.getInstance().save();
			LOGGER.info("RelationManager has been saved.");
			
			// Save Couples.
			CoupleManager.getInstance().save();
			LOGGER.info("CoupleManager has been saved.");
			
			// Enforce ItemInstanceTaskManager update.
			ItemInstanceTaskManager.getInstance().save();
			LOGGER.info("ItemInstanceTaskManager has been saved.");
			
			// Save items on ground.
			ItemsOnGroundTaskManager.getInstance().save();
			LOGGER.info("ItemsOnGroundTaskManager has been saved.");
			
			// Store the actual server state.
			ServerMemoTable.getInstance().set("server_crash", false);
			
			try
			{
				Thread.sleep(5000);
			}
			catch (Exception e)
			{
				// Silent catch.
			}
			
			// Stop the ThreadPool.
			ThreadPool.shutdown();
			
			try
			{
				GameServer.getInstance().getSelectorThread().shutdown();
			}
			catch (Exception e)
			{
				// Silent catch.
			}
			
			try
			{
				ConnectionPool.shutdown();
			}
			catch (Exception e)
			{
				// Silent catch.
			}
			
			Runtime.getRuntime().halt((SingletonHolder.INSTANCE._shutdownMode == GM_RESTART) ? 2 : 0);
		}
		else
		{
			countdown();
			
			switch (_shutdownMode)
			{
				case GM_SHUTDOWN:
					SingletonHolder.INSTANCE.setMode(GM_SHUTDOWN);
					SingletonHolder.INSTANCE.run();
					System.exit(0);
					break;
				
				case GM_RESTART:
					SingletonHolder.INSTANCE.setMode(GM_RESTART);
					SingletonHolder.INSTANCE.run();
					System.exit(2);
					break;
			}
		}
	}
	
	/**
	 * This functions starts a shutdown countdown.
	 * @param player : The {@link Player} who issued the shutdown command.
	 * @param ghostEntity : The entity who issued the shutdown command.
	 * @param seconds : The number of seconds until shutdown.
	 * @param restart : If true, the server will restart after shutdown.
	 */
	public void startShutdown(Player player, String ghostEntity, int seconds, boolean restart)
	{
		_shutdownMode = (restart) ? GM_RESTART : GM_SHUTDOWN;
		
		if (player != null)
			LOGGER.info("GM: {} issued {} process in {} seconds.", player.toString(), MODE_TEXT[_shutdownMode], seconds);
		else if (!ghostEntity.isEmpty())
			LOGGER.info("Entity: {} issued {} process in {} seconds.", ghostEntity, MODE_TEXT[_shutdownMode], seconds);
		
		if (_shutdownMode > 0)
		{
			switch (seconds)
			{
				case 540, 480, 420, 360, 300, 240, 180, 120, 60, 30, 10, 5, 4, 3, 2, 1:
					break;
				
				default:
					sendServerQuit(seconds);
			}
		}
		
		if (_counterInstance != null)
			_counterInstance.setMode(ABORT);
		
		// the main instance should only run for shutdown hook, so we start a new instance
		_counterInstance = new Shutdown(seconds, restart);
		_counterInstance.start();
	}
	
	/**
	 * This function aborts a running countdown.
	 * @param player : The {@link Player} who issued the abort process.
	 */
	public void abort(Player player)
	{
		if (_counterInstance != null)
		{
			LOGGER.info("GM: {} aborted {} process.", player.toString(), MODE_TEXT[_shutdownMode]);
			_counterInstance.setMode(ABORT);
			
			World.announceToOnlinePlayers("Server aborted " + MODE_TEXT[_shutdownMode] + " process and continues normal operation.");
		}
	}
	
	/**
	 * Set the shutdown mode.
	 * @param mode : what mode shall be set.
	 */
	private void setMode(int mode)
	{
		_shutdownMode = mode;
	}
	
	/**
	 * Report the current countdown to all players. Flag the server as "down" when reaching 60sec. Rehabilitate the server status if ABORT {@link ServerStatus} is seen.
	 */
	private void countdown()
	{
		try
		{
			while (_secondsShut > 0)
			{
				// Rehabilitate previous server status if shutdown is aborted.
				if (_shutdownMode == ABORT)
				{
					if (LoginServerThread.getInstance().getServerType() == ServerType.DOWN)
						LoginServerThread.getInstance().setServerType((Config.SERVER_GMONLY) ? ServerType.GM_ONLY : ServerType.AUTO);
					
					break;
				}
				
				switch (_secondsShut)
				{
					case 540, 480, 420, 360, 300, 240, 180, 120, 60, 30, 10, 5, 4, 3, 2, 1:
						sendServerQuit(_secondsShut);
						break;
				}
				
				// avoids new players from logging in
				if (_secondsShut <= 60 && LoginServerThread.getInstance().getServerType() != ServerType.DOWN)
					LoginServerThread.getInstance().setServerType(ServerType.DOWN);
				
				_secondsShut--;
				
				Thread.sleep(1000);
			}
		}
		catch (InterruptedException ie)
		{
			// Do nothing.
		}
	}
	
	private static void sendServerQuit(int seconds)
	{
		World.toAllOnlinePlayers(SystemMessage.getSystemMessage(SystemMessageId.THE_SERVER_WILL_BE_COMING_DOWN_IN_S1_SECONDS).addNumber(seconds));
	}
	
	/**
	 * Disconnect all {@link Player}s from the server.
	 */
	private static void disconnectAllPlayers()
	{
		for (Player player : World.getInstance().getPlayers())
		{
			final GameClient client = player.getClient();
			if (client != null && !client.isDetached())
			{
				client.close(ServerClose.STATIC_PACKET);
				client.setPlayer(null);
				
				player.setClient(null);
			}
			player.deleteMe();
		}
	}
	
	public static Shutdown getInstance()
	{
		return SingletonHolder.INSTANCE;
	}
	
	private static class SingletonHolder
	{
		protected static final Shutdown INSTANCE = new Shutdown();
	}
}