package com.shnok.javaserver.gameserver;

import java.io.File;
import java.io.FileInputStream;
import java.io.InputStream;
import java.net.InetAddress;
import java.util.logging.LogManager;

import com.shnok.javaserver.commons.lang.StringUtil;
import com.shnok.javaserver.commons.logging.CLogger;
import com.shnok.javaserver.commons.mmocore.SelectorConfig;
import com.shnok.javaserver.commons.mmocore.SelectorThread;
import com.shnok.javaserver.commons.network.IPv4Filter;
import com.shnok.javaserver.commons.pool.ConnectionPool;
import com.shnok.javaserver.commons.pool.ThreadPool;
import com.shnok.javaserver.commons.util.SysUtil;

import com.shnok.javaserver.Config;
import com.shnok.javaserver.gameserver.communitybbs.CommunityBoard;
import com.shnok.javaserver.gameserver.data.SkillTable;
import com.shnok.javaserver.gameserver.data.cache.CrestCache;
import com.shnok.javaserver.gameserver.data.cache.HtmCache;
import com.shnok.javaserver.gameserver.data.manager.BufferManager;
import com.shnok.javaserver.gameserver.data.manager.BuyListManager;
import com.shnok.javaserver.gameserver.data.manager.CastleManager;
import com.shnok.javaserver.gameserver.data.manager.CastleManorManager;
import com.shnok.javaserver.gameserver.data.manager.ClanHallManager;
import com.shnok.javaserver.gameserver.data.manager.CoupleManager;
import com.shnok.javaserver.gameserver.data.manager.CursedWeaponManager;
import com.shnok.javaserver.gameserver.data.manager.DerbyTrackManager;
import com.shnok.javaserver.gameserver.data.manager.FestivalOfDarknessManager;
import com.shnok.javaserver.gameserver.data.manager.FishingChampionshipManager;
import com.shnok.javaserver.gameserver.data.manager.HeroManager;
import com.shnok.javaserver.gameserver.data.manager.LotteryManager;
import com.shnok.javaserver.gameserver.data.manager.PartyMatchRoomManager;
import com.shnok.javaserver.gameserver.data.manager.PetitionManager;
import com.shnok.javaserver.gameserver.data.manager.RaidPointManager;
import com.shnok.javaserver.gameserver.data.manager.SevenSignsManager;
import com.shnok.javaserver.gameserver.data.manager.SpawnManager;
import com.shnok.javaserver.gameserver.data.manager.ZoneManager;
import com.shnok.javaserver.gameserver.data.sql.BookmarkTable;
import com.shnok.javaserver.gameserver.data.sql.ClanTable;
import com.shnok.javaserver.gameserver.data.sql.PlayerInfoTable;
import com.shnok.javaserver.gameserver.data.sql.ServerMemoTable;
import com.shnok.javaserver.gameserver.data.xml.AdminData;
import com.shnok.javaserver.gameserver.data.xml.AnnouncementData;
import com.shnok.javaserver.gameserver.data.xml.ArmorSetData;
import com.shnok.javaserver.gameserver.data.xml.AugmentationData;
import com.shnok.javaserver.gameserver.data.xml.BoatData;
import com.shnok.javaserver.gameserver.data.xml.ClanHallDecoData;
import com.shnok.javaserver.gameserver.data.xml.DoorData;
import com.shnok.javaserver.gameserver.data.xml.FishData;
import com.shnok.javaserver.gameserver.data.xml.HealSpsData;
import com.shnok.javaserver.gameserver.data.xml.HennaData;
import com.shnok.javaserver.gameserver.data.xml.InstantTeleportData;
import com.shnok.javaserver.gameserver.data.xml.ItemData;
import com.shnok.javaserver.gameserver.data.xml.ManorAreaData;
import com.shnok.javaserver.gameserver.data.xml.MultisellData;
import com.shnok.javaserver.gameserver.data.xml.NewbieBuffData;
import com.shnok.javaserver.gameserver.data.xml.NpcData;
import com.shnok.javaserver.gameserver.data.xml.ObserverGroupData;
import com.shnok.javaserver.gameserver.data.xml.PlayerData;
import com.shnok.javaserver.gameserver.data.xml.PlayerLevelData;
import com.shnok.javaserver.gameserver.data.xml.RecipeData;
import com.shnok.javaserver.gameserver.data.xml.RestartPointData;
import com.shnok.javaserver.gameserver.data.xml.ScriptData;
import com.shnok.javaserver.gameserver.data.xml.SkillTreeData;
import com.shnok.javaserver.gameserver.data.xml.SoulCrystalData;
import com.shnok.javaserver.gameserver.data.xml.SpellbookData;
import com.shnok.javaserver.gameserver.data.xml.StaticObjectData;
import com.shnok.javaserver.gameserver.data.xml.SummonItemData;
import com.shnok.javaserver.gameserver.data.xml.TeleportData;
import com.shnok.javaserver.gameserver.data.xml.WalkerRouteData;
import com.shnok.javaserver.gameserver.geoengine.GeoEngine;
import com.shnok.javaserver.gameserver.handler.AdminCommandHandler;
import com.shnok.javaserver.gameserver.handler.ChatHandler;
import com.shnok.javaserver.gameserver.handler.ItemHandler;
import com.shnok.javaserver.gameserver.handler.SkillHandler;
import com.shnok.javaserver.gameserver.handler.TargetHandler;
import com.shnok.javaserver.gameserver.handler.UserCommandHandler;
import com.shnok.javaserver.gameserver.idfactory.IdFactory;
import com.shnok.javaserver.gameserver.model.World;
import com.shnok.javaserver.gameserver.model.memo.GlobalMemo;
import com.shnok.javaserver.gameserver.model.olympiad.Olympiad;
import com.shnok.javaserver.gameserver.model.olympiad.OlympiadGameManager;
import com.shnok.javaserver.gameserver.network.GameClient;
import com.shnok.javaserver.gameserver.network.GamePacketHandler;
import com.shnok.javaserver.gameserver.taskmanager.AiTaskManager;
import com.shnok.javaserver.gameserver.taskmanager.AttackStanceTaskManager;
import com.shnok.javaserver.gameserver.taskmanager.BoatTaskManager;
import com.shnok.javaserver.gameserver.taskmanager.DecayTaskManager;
import com.shnok.javaserver.gameserver.taskmanager.GameTimeTaskManager;
import com.shnok.javaserver.gameserver.taskmanager.InventoryUpdateTaskManager;
import com.shnok.javaserver.gameserver.taskmanager.ItemInstanceTaskManager;
import com.shnok.javaserver.gameserver.taskmanager.ItemsOnGroundTaskManager;
import com.shnok.javaserver.gameserver.taskmanager.PvpFlagTaskManager;
import com.shnok.javaserver.gameserver.taskmanager.ShadowItemTaskManager;
import com.shnok.javaserver.gameserver.taskmanager.WaterTaskManager;

public class GameServer
{
	private static final CLogger LOGGER = new CLogger(GameServer.class.getName());
	
	private final SelectorThread<GameClient> _selectorThread;
	private final boolean _isServerCrash;
	
	private static GameServer _gameServer;
	
	public static void main(String[] args) throws Exception
	{
		_gameServer = new GameServer();
	}
	
	public GameServer() throws Exception
	{
		// Create log folder
		new File("./log").mkdir();
		new File("./log/chat").mkdir();
		new File("./log/console").mkdir();
		new File("./log/error").mkdir();
		new File("./log/gmaudit").mkdir();
		new File("./log/item").mkdir();
		new File("data/crests").mkdirs();
		
		// Create input stream for log file -- or store file data into memory
		try (InputStream is = new FileInputStream(new File("conf/logging.properties")))
		{
			LogManager.getLogManager().readConfiguration(is);
		}
		
		StringUtil.printSection("Config");
		Config.loadGameServer();
		
		StringUtil.printSection("Poolers");
		ConnectionPool.init();
		ThreadPool.init();
		
		StringUtil.printSection("IdFactory");
		IdFactory.getInstance();
		
		StringUtil.printSection("Cache");
		HtmCache.getInstance();
		CrestCache.getInstance();
		
		StringUtil.printSection("World");
		World.getInstance();
		AnnouncementData.getInstance();
		ServerMemoTable.getInstance();
		GlobalMemo.getInstance();
		
		// Fill variable after ServerMemoTable loading.
		_isServerCrash = ServerMemoTable.getInstance().getBool("server_crash", false);
		
		StringUtil.printSection("Skills");
		SkillTable.getInstance();
		SkillTreeData.getInstance();
		
		StringUtil.printSection("Items");
		ItemData.getInstance();
		SummonItemData.getInstance();
		HennaData.getInstance();
		BuyListManager.getInstance();
		MultisellData.getInstance();
		RecipeData.getInstance();
		ArmorSetData.getInstance();
		FishData.getInstance();
		SpellbookData.getInstance();
		SoulCrystalData.getInstance();
		AugmentationData.getInstance();
		CursedWeaponManager.getInstance();
		
		StringUtil.printSection("Admins");
		AdminData.getInstance();
		BookmarkTable.getInstance();
		PetitionManager.getInstance();
		
		StringUtil.printSection("Characters");
		PlayerData.getInstance();
		PlayerInfoTable.getInstance();
		PlayerLevelData.getInstance();
		PartyMatchRoomManager.getInstance();
		RaidPointManager.getInstance();
		HealSpsData.getInstance();
		RestartPointData.getInstance();
		
		StringUtil.printSection("Community server");
		CommunityBoard.getInstance();
		
		StringUtil.printSection("Clans");
		ClanTable.getInstance();
		
		StringUtil.printSection("Geodata & Pathfinding");
		GeoEngine.getInstance();
		
		StringUtil.printSection("Zones");
		ZoneManager.getInstance();
		
		StringUtil.printSection("Doors");
		DoorData.getInstance().spawn();
		
		StringUtil.printSection("Castles & Clan Halls");
		CastleManager.getInstance();
		ClanHallDecoData.getInstance();
		ClanHallManager.getInstance();
		
		StringUtil.printSection("Task Managers");
		AiTaskManager.getInstance();
		AttackStanceTaskManager.getInstance();
		BoatTaskManager.getInstance();
		DecayTaskManager.getInstance();
		GameTimeTaskManager.getInstance();
		ItemsOnGroundTaskManager.getInstance();
		PvpFlagTaskManager.getInstance();
		ShadowItemTaskManager.getInstance();
		WaterTaskManager.getInstance();
		InventoryUpdateTaskManager.getInstance();
		ItemInstanceTaskManager.getInstance();

		if(Config.SEVEN_SIGNS_ENABLED) {
			StringUtil.printSection("Seven Signs");
			SevenSignsManager.getInstance();
			FestivalOfDarknessManager.getInstance();
		}

		StringUtil.printSection("Manor Manager");
		ManorAreaData.getInstance();
		CastleManorManager.getInstance();
		
		StringUtil.printSection("NPCs");
		BufferManager.getInstance();
		NpcData.getInstance();
		WalkerRouteData.getInstance();
		StaticObjectData.getInstance();
		SpawnManager.getInstance();
		NewbieBuffData.getInstance();
		InstantTeleportData.getInstance();
		TeleportData.getInstance();
		ObserverGroupData.getInstance();
		
		CastleManager.getInstance().spawnEntities();

		if(Config.OLY_ENABLED) {
			StringUtil.printSection("Olympiads & Heroes");
			OlympiadGameManager.getInstance();
			Olympiad.getInstance();
			HeroManager.getInstance();
		}

		StringUtil.printSection("Quests & Scripts");
		ScriptData.getInstance();
		
		if (Config.ALLOW_BOAT)
			BoatData.getInstance().load();
		
		StringUtil.printSection("Events");
		DerbyTrackManager.getInstance();
		LotteryManager.getInstance();
		CoupleManager.getInstance();
		
		if (Config.ALLOW_FISH_CHAMPIONSHIP)
			FishingChampionshipManager.getInstance();
		
		StringUtil.printSection("Spawns");
		SpawnManager.getInstance().spawn();
		
		StringUtil.printSection("Handlers");
		LOGGER.info("Loaded {} admin command handlers.", AdminCommandHandler.getInstance().size());
		LOGGER.info("Loaded {} chat handlers.", ChatHandler.getInstance().size());
		LOGGER.info("Loaded {} item handlers.", ItemHandler.getInstance().size());
		LOGGER.info("Loaded {} skill handlers.", SkillHandler.getInstance().size());
		LOGGER.info("Loaded {} target handlers.", TargetHandler.getInstance().size());
		LOGGER.info("Loaded {} user command handlers.", UserCommandHandler.getInstance().size());
		
		StringUtil.printSection("System");
		Runtime.getRuntime().addShutdownHook(Shutdown.getInstance());
		
		if (_isServerCrash)
			LOGGER.info("Server crashed on last session!");
		else
			ServerMemoTable.getInstance().set("server_crash", true);
		
		LOGGER.info("Gameserver has started, used memory: {} / {} Mo.", SysUtil.getUsedMemory(), SysUtil.getMaxMemory());
		LOGGER.info("Maximum allowed players: {}.", Config.MAXIMUM_ONLINE_USERS);
		
		StringUtil.printSection("Login");
		LoginServerThread.getInstance().start();
		
		final SelectorConfig sc = new SelectorConfig();
		sc.MAX_READ_PER_PASS = Config.MMO_MAX_READ_PER_PASS;
		sc.MAX_SEND_PER_PASS = Config.MMO_MAX_SEND_PER_PASS;
		sc.SLEEP_TIME = Config.MMO_SELECTOR_SLEEP_TIME;
		sc.HELPER_BUFFER_COUNT = Config.MMO_HELPER_BUFFER_COUNT;
		
		final GamePacketHandler handler = new GamePacketHandler();
		_selectorThread = new SelectorThread<>(sc, handler, handler, handler, new IPv4Filter());
		
		InetAddress bindAddress = null;
		if (!Config.GAMESERVER_HOSTNAME.equals("*"))
		{
			try
			{
				bindAddress = InetAddress.getByName(Config.GAMESERVER_HOSTNAME);
			}
			catch (Exception e)
			{
				LOGGER.error("The GameServer bind address is invalid, using all available IPs.", e);
			}
		}
		
		try
		{
			_selectorThread.openServerSocket(bindAddress, Config.GAMESERVER_PORT);
		}
		catch (Exception e)
		{
			LOGGER.error("Failed to open server socket.", e);
			System.exit(1);
		}
		_selectorThread.start();
	}
	
	public SelectorThread<GameClient> getSelectorThread()
	{
		return _selectorThread;
	}
	
	public boolean isServerCrash()
	{
		return _isServerCrash;
	}
	
	public static GameServer getInstance()
	{
		return _gameServer;
	}
}