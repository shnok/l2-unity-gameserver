package com.shnok.javaserver.gameserver.model.olympiad;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ScheduledFuture;
import java.util.concurrent.atomic.AtomicInteger;

import com.shnok.javaserver.commons.logging.CLogger;
import com.shnok.javaserver.commons.pool.ConnectionPool;
import com.shnok.javaserver.commons.pool.ThreadPool;

import com.shnok.javaserver.Config;
import com.shnok.javaserver.gameserver.data.manager.HeroManager;
import com.shnok.javaserver.gameserver.data.manager.ZoneManager;
import com.shnok.javaserver.gameserver.data.sql.ServerMemoTable;
import com.shnok.javaserver.gameserver.enums.OlympiadState;
import com.shnok.javaserver.gameserver.enums.OlympiadType;
import com.shnok.javaserver.gameserver.model.World;
import com.shnok.javaserver.gameserver.model.actor.Player;
import com.shnok.javaserver.gameserver.model.actor.instance.OlympiadManagerNpc;
import com.shnok.javaserver.gameserver.model.zone.type.OlympiadStadiumZone;
import com.shnok.javaserver.gameserver.network.SystemMessageId;
import com.shnok.javaserver.gameserver.network.serverpackets.SystemMessage;

public class Olympiad
{
	private static final CLogger LOGGER = new CLogger(Olympiad.class.getName());
	
	private final Map<Integer, OlympiadNoble> _nobles = new ConcurrentHashMap<>();
	
	private static final String SELECT_OLYMPIAD_NOBLES = "SELECT olympiad_nobles.char_id, olympiad_nobles.class_id, characters.char_name, olympiad_nobles.olympiad_points, olympiad_nobles.competitions_done, olympiad_nobles.competitions_won, olympiad_nobles.competitions_lost, olympiad_nobles.competitions_drawn, olympiad_nobles.rewarded FROM olympiad_nobles, characters WHERE characters.obj_Id = olympiad_nobles.char_id";
	private static final String INSERT_OR_UPDATE_OLYMPIAD_NOBLES = "INSERT INTO olympiad_nobles (`char_id`,`class_id`,`olympiad_points`,`competitions_done`,`competitions_won`,`competitions_lost`, `competitions_drawn`, `rewarded`) VALUES (?,?,?,?,?,?,?,?) ON DUPLICATE KEY UPDATE olympiad_points=VALUES(olympiad_points), competitions_done=VALUES(competitions_done), competitions_won=VALUES(competitions_won), competitions_lost=VALUES(competitions_lost), competitions_drawn=VALUES(competitions_drawn), rewarded=VALUES(rewarded)";
	private static final String TRUNCATE_OLYMPIAD_NOBLES = "TRUNCATE olympiad_nobles";
	
	private static final String SELECT_CLASS_LEADER = "SELECT characters.char_name from olympiad_nobles_eom, characters WHERE characters.obj_Id = olympiad_nobles_eom.char_id AND olympiad_nobles_eom.class_id = ? AND olympiad_nobles_eom.competitions_done >= ? ORDER BY olympiad_nobles_eom.olympiad_points DESC, olympiad_nobles_eom.competitions_done DESC, olympiad_nobles_eom.competitions_won DESC LIMIT 10";
	
	private static final String INSERT_MONTH_OLYMPIAD = "INSERT INTO olympiad_nobles_eom SELECT char_id, class_id, olympiad_points, competitions_done, competitions_won, competitions_lost, competitions_drawn FROM olympiad_nobles";
	private static final String TRUNCATE_MONTH_OLYMPIAD = "TRUNCATE olympiad_nobles_eom";
	
	public static final String OLYMPIAD_HTML_PATH = "data/html/olympiad/";
	
	private OlympiadState _period = OlympiadState.COMPETITION;
	
	private AtomicInteger _counter = new AtomicInteger(0);
	
	private int _currentCycle;
	
	private long _olympiadEnd = 0;
	private long _periodEnd = 0;
	private long _nextNoblePointsUpdate = 0;
	private long _registrationEnd = -1;
	private long _pendingGamesCheck = -1;
	
	private OlympiadState _state;
	
	private ScheduledFuture<?> _task;
	private ScheduledFuture<?> _gameManagerTask;
	
	private Olympiad()
	{
		load();
		setNewOlympiadEnd();
		init();
		reschedule();
	}
	
	private void resetVars()
	{
		_olympiadEnd = 0;
		_periodEnd = 0;
		_nextNoblePointsUpdate = 0;
		_registrationEnd = -1;
		_pendingGamesCheck = -1;
	}
	
	private void cancelTasks()
	{
		if (_task != null)
		{
			_task.cancel(true);
			_task = null;
		}
		
		if (_gameManagerTask != null)
		{
			_gameManagerTask.cancel(true);
			_gameManagerTask = null;
		}
	}
	
	private void reschedule()
	{
		OlympiadState minState = null;
		long minDelay = Long.MAX_VALUE;
		
		for (OlympiadState state : OlympiadState.values())
		{
			if ((state == OlympiadState.COMPETITION && !isInCompetitionPeriod()) || (state == OlympiadState.VALIDATION && isInCompetitionPeriod()))
				continue;
			
			long delay = getDelay(state);
			
			if (delay >= 0 && delay < minDelay)
			{
				minState = state;
				minDelay = delay;
			}
		}
		
		if (minState != null && (_task == null || _task.isDone() || _task.isCancelled()))
		{
			_task = ThreadPool.schedule(this::executeNextTask, minDelay);
			_state = minState;
		}
		else
			LOGGER.warn("Olympiad: No tasks available to be schedule.");
	}
	
	private long getDelay(OlympiadState state)
	{
		return switch (state)
		{
			case COMPETITION, VALIDATION -> getMillisToPeriodEnd();
			case END_OLYMPIAD -> getMillisToOlympiadEnd();
			case SAVE_NOBLE_POINTS -> getMillisToNextNoblePointUpdate();
			case REGISTRATION -> _registrationEnd;
			case CHECK_PENDING_GAMES -> _pendingGamesCheck;
		};
	}
	
	private void executeNextTask()
	{
		if (_state == null)
			return;
		
		executeTask();
		_task = null;
		reschedule();
	}
	
	private void executeTask()
	{
		switch (_state)
		{
			case COMPETITION:
				World.toAllOnlinePlayers(SystemMessage.getSystemMessage(SystemMessageId.THE_OLYMPIAD_GAME_HAS_ENDED));
				checkPendingGames();
				break;
			
			case VALIDATION:
				startNewCycle();
				deleteNobles();
				init();
				break;
			
			case SAVE_NOBLE_POINTS:
				setNextNoblePointsUpdate();
				
				// Add weekly points to everyone.
				_nobles.values().forEach(n -> n.updatePoints(Config.OLY_WEEKLY_POINTS));
				break;
			
			case REGISTRATION:
				World.toAllOnlinePlayers(SystemMessage.getSystemMessage(SystemMessageId.OLYMPIAD_REGISTRATION_PERIOD_ENDED));
				
				_registrationEnd = -1;
				break;
			
			case CHECK_PENDING_GAMES:
				checkPendingGames();
				break;
			
			case END_OLYMPIAD:
				endOlympiad();
				break;
		}
	}
	
	private void endOlympiad()
	{
		World.toAllOnlinePlayers(SystemMessage.getSystemMessage(SystemMessageId.OLYMPIAD_PERIOD_S1_HAS_ENDED).addNumber(_currentCycle));
		
		saveNobleData();
		
		_period = OlympiadState.VALIDATION;
		
		HeroManager.getInstance().resetData();
		HeroManager.getInstance().computeNewHeroes();
		
		// Save current Olympiad status.
		saveOlympiadStatus();
		
		// Update monthly data.
		try (Connection con = ConnectionPool.getConnection();
			PreparedStatement ps = con.prepareStatement(TRUNCATE_MONTH_OLYMPIAD);
			PreparedStatement ps2 = con.prepareStatement(INSERT_MONTH_OLYMPIAD))
		{
			ps.execute();
			ps2.execute();
		}
		catch (Exception e)
		{
			LOGGER.error("Couldn't update monthly Olympiad nobles.", e);
		}
		init();
	}
	
	private void checkPendingGames()
	{
		if (OlympiadGameManager.getInstance().isBattleStarted())
			_pendingGamesCheck = 60000;
		else
		{
			_pendingGamesCheck = -1;
			
			if (_gameManagerTask != null)
				_gameManagerTask.cancel(false);
			
			// Save current Olympiad status
			saveOlympiadStatus();
			init();
		}
	}
	
	protected boolean isInCompetitionPeriod()
	{
		return _period == OlympiadState.COMPETITION;
	}
	
	private void load()
	{
		_currentCycle = ServerMemoTable.getInstance().getInteger("olympiad_cycle", 1);
		
		try (Connection con = ConnectionPool.getConnection();
			PreparedStatement ps = con.prepareStatement(SELECT_OLYMPIAD_NOBLES);
			ResultSet rs = ps.executeQuery())
		{
			while (rs.next())
				addNoble(rs.getInt("char_id"), new OlympiadNoble(rs.getInt("class_id"), rs.getString("char_name"), rs.getInt("olympiad_points"), rs.getInt("competitions_done"), rs.getInt("competitions_won"), rs.getInt("competitions_lost"), rs.getInt("competitions_drawn"), rs.getBoolean("rewarded")));
		}
		catch (Exception e)
		{
			LOGGER.error("Couldn't load nobles.", e);
		}
		
		LOGGER.info("Loaded {} nobles.", _nobles.size());
	}
	
	private void schedulePeriodEnd()
	{
		if (isInCompetitionPeriod())
		{
			startCompetition();
			return;
		}
		
		if (_gameManagerTask != null)
		{
			_gameManagerTask.cancel(false);
			_gameManagerTask = null;
		}
		
		if (_periodEnd <= System.currentTimeMillis())
		{
			startNewCycle();
			startCompetition();
		}
	}
	
	private void revalidatePeriod()
	{
		final long currentTime = System.currentTimeMillis();
		
		final Calendar cal = Calendar.getInstance();
		cal.setTimeInMillis(currentTime);
		cal.set(Calendar.HOUR_OF_DAY, Config.OLY_START_TIME);
		cal.set(Calendar.MINUTE, Config.OLY_MIN);
		cal.set(Calendar.SECOND, 0);
		cal.set(Calendar.MILLISECOND, 0);
		
		final long olyCompPeriod = Math.min(Config.OLY_CPERIOD, 86390000L);
		
		final long compStartTimeToday = cal.getTimeInMillis();
		final long compStartTimeYesterday = compStartTimeToday - 86400000L;
		final long compEndTimeToday = compStartTimeToday + olyCompPeriod;
		final long compEndTimeYesterday = compStartTimeYesterday + olyCompPeriod;
		
		if (currentTime >= compStartTimeToday && currentTime < compEndTimeToday)
		{
			_period = OlympiadState.COMPETITION;
			_periodEnd = compEndTimeToday;
		}
		else if (currentTime >= compStartTimeYesterday && currentTime < compEndTimeYesterday)
		{
			_period = OlympiadState.COMPETITION;
			_periodEnd = compEndTimeYesterday;
		}
		else
		{
			_period = OlympiadState.VALIDATION;
			_registrationEnd = -1;
			
			if (compStartTimeToday > currentTime)
				_periodEnd = compStartTimeToday;
			else
			{
				cal.add(Calendar.DAY_OF_MONTH, 1);
				
				_periodEnd = cal.getTimeInMillis();
			}
		}
		
		if (_periodEnd > _olympiadEnd)
			_periodEnd = _olympiadEnd;
		
		schedulePeriodEnd();
	}
	
	private void init()
	{
		final long currentTime = System.currentTimeMillis();
		
		if (_olympiadEnd == 0 || _olympiadEnd < currentTime)
			setNewOlympiadEnd();
		
		if (_nextNoblePointsUpdate == 0 || _nextNoblePointsUpdate < currentTime)
			setNextNoblePointsUpdate();
		
		revalidatePeriod();
	}
	
	private void startCompetition()
	{
		World.toAllOnlinePlayers(SystemMessage.getSystemMessage(SystemMessageId.THE_OLYMPIAD_GAME_HAS_STARTED));
		
		startGameManager();
		
		_registrationEnd = getMillisToPeriodEnd() - 600000;
	}
	
	private void startGameManager()
	{
		if (!Config.OLY_ANNOUNCE_GAMES)
		{
			_gameManagerTask = ThreadPool.scheduleAtFixedRate(() -> OlympiadGameManager.getInstance().run(), 30000, 30000);
			return;
		}
		
		_gameManagerTask = ThreadPool.scheduleAtFixedRate(() ->
		{
			if (_counter.incrementAndGet() % 60 == 0)
				OlympiadGameManager.getInstance().run();
			
			for (OlympiadGameTask task : OlympiadGameManager.getInstance().getOlympiadTasks())
			{
				if (!task.needAnnounce())
					continue;
				
				final AbstractOlympiadGame game = task.getGame();
				if (game == null)
					continue;
				
				final String announcement = "Olympiad class" + ((game.getType() == OlympiadType.NON_CLASSED) ? "-free" : "") + " individual match is going to begin in Arena " + (game.getStadiumId() + 1) + " in a moment.";
				
				for (OlympiadManagerNpc manager : OlympiadManagerNpc.getInstances())
					manager.broadcastNpcShout(announcement);
			}
		}, 30000, 500);
	}
	
	public void manualSelectHeroes()
	{
		cancelTasks();
		resetVars();
		endOlympiad();
	}
	
	private void setNextNoblePointsUpdate()
	{
		final Calendar cal = Calendar.getInstance();
		cal.set(Calendar.DAY_OF_MONTH, 1);
		
		final int firstDayOfWeek = cal.get(Calendar.DAY_OF_WEEK);
		
		cal.setTimeInMillis(System.currentTimeMillis());
		final int dayOfWeek = cal.get(Calendar.DAY_OF_WEEK);
		
		int daysUntilNextSameDay = firstDayOfWeek - dayOfWeek;
		if (daysUntilNextSameDay < 0 || (daysUntilNextSameDay == 0 && cal.get(Calendar.HOUR_OF_DAY) > Config.OLY_START_TIME || (cal.get(Calendar.HOUR_OF_DAY) == Config.OLY_START_TIME && cal.get(Calendar.MINUTE) >= Config.OLY_MIN)))
			daysUntilNextSameDay += 7;
		
		if (cal.get(Calendar.DAY_OF_MONTH) == 1)
			daysUntilNextSameDay += 7;
		
		cal.add(Calendar.DAY_OF_YEAR, daysUntilNextSameDay);
		
		cal.set(Calendar.HOUR_OF_DAY, Config.OLY_START_TIME);
		cal.set(Calendar.MINUTE, Config.OLY_MIN);
		cal.set(Calendar.SECOND, 0);
		cal.set(Calendar.MILLISECOND, 0);
		
		_nextNoblePointsUpdate = cal.getTimeInMillis();
	}
	
	private void setNewOlympiadEnd()
	{
		final Calendar cal = Calendar.getInstance();
		cal.add(Calendar.MONTH, 1);
		cal.set(Calendar.DAY_OF_MONTH, 1);
		cal.set(Calendar.HOUR_OF_DAY, 12);
		cal.set(Calendar.MINUTE, 0);
		cal.set(Calendar.SECOND, 0);
		cal.set(Calendar.MILLISECOND, 0);
		
		_olympiadEnd = cal.getTimeInMillis();
	}
	
	public long getMillisToPeriodEnd()
	{
		return _periodEnd - System.currentTimeMillis();
	}
	
	public long getMillisToOlympiadEnd()
	{
		return _olympiadEnd - System.currentTimeMillis();
	}
	
	public long getMillisToNextNoblePointUpdate()
	{
		return _nextNoblePointsUpdate - System.currentTimeMillis();
	}
	
	public boolean playerInStadia(Player player)
	{
		return ZoneManager.getInstance().getZone(player, OlympiadStadiumZone.class) != null;
	}
	
	/**
	 * Save noblesse data to database
	 */
	private void saveNobleData()
	{
		if (_nobles.isEmpty())
			return;
		
		try (Connection con = ConnectionPool.getConnection();
			PreparedStatement ps = con.prepareStatement(INSERT_OR_UPDATE_OLYMPIAD_NOBLES))
		{
			for (Map.Entry<Integer, OlympiadNoble> entry : _nobles.entrySet())
			{
				final OlympiadNoble noble = entry.getValue();
				if (noble == null)
					continue;
				
				ps.setInt(1, entry.getKey());
				ps.setInt(2, noble.getClassId());
				ps.setInt(3, noble.getPoints());
				ps.setInt(4, noble.getCompDone());
				ps.setInt(5, noble.getCompWon());
				ps.setInt(6, noble.getCompLost());
				ps.setInt(7, noble.getCompDrawn());
				ps.setBoolean(8, noble.isRewarded());
				
				ps.addBatch();
			}
			ps.executeBatch();
		}
		catch (Exception e)
		{
			LOGGER.error("Couldn't save Olympiad nobles data.", e);
		}
	}
	
	/**
	 * Save current olympiad status and update noblesse table in database
	 */
	public void saveOlympiadStatus()
	{
		ServerMemoTable.getInstance().set("olympiad_cycle", _currentCycle);
		saveNobleData();
	}
	
	public List<String> getClassLeaderBoard(int classId)
	{
		final List<String> names = new ArrayList<>();
		
		try (Connection con = ConnectionPool.getConnection();
			PreparedStatement ps = con.prepareStatement(SELECT_CLASS_LEADER))
		{
			ps.setInt(1, classId);
			ps.setInt(2, Config.OLY_MIN_MATCHES);
			
			try (ResultSet rs = ps.executeQuery())
			{
				while (rs.next())
					names.add(rs.getString("char_name"));
			}
		}
		catch (Exception e)
		{
			LOGGER.error("Couldn't load Olympiad leaders.", e);
		}
		return names;
	}
	
	public int getNoblessePasses(Player player)
	{
		if (player == null || isInCompetitionPeriod())
			return 0;
		
		final OlympiadNoble noble = _nobles.get(player.getObjectId());
		if (noble == null)
			return 0;
		
		if (noble.isRewarded())
			return 0;
		
		// Calculate points. They can't go upper 1000, and if lower than 50 it goes to 0.
		int points = Math.min(1000, noble.getPoints());
		if (points < 50)
			points = 0;
		
		// Add hero reward on top of it.
		points += (player.isHero() || HeroManager.getInstance().isInactiveHero(player.getObjectId())) ? Config.OLY_HERO_POINTS : 0;
		
		// Flag the player as rewarded.
		noble.setRewarded(true);
		
		// Reset points.
		noble.setPoints(0);
		
		return points * Config.OLY_GP_PER_POINT;
	}
	
	private void startNewCycle()
	{
		_period = OlympiadState.COMPETITION;
		_currentCycle++;
		
		World.toAllOnlinePlayers(SystemMessage.getSystemMessage(SystemMessageId.OLYMPIAD_PERIOD_S1_HAS_STARTED).addNumber(_currentCycle));
	}
	
	private void deleteNobles()
	{
		try (Connection con = ConnectionPool.getConnection();
			PreparedStatement ps = con.prepareStatement(TRUNCATE_OLYMPIAD_NOBLES))
		{
			ps.execute();
		}
		catch (Exception e)
		{
			LOGGER.error("Couldn't delete Olympiad nobles.", e);
		}
		_nobles.clear();
	}
	
	public OlympiadNoble getNoble(int objectId)
	{
		return _nobles.get(objectId);
	}
	
	/**
	 * @param objectId : The {@link Player} objectId to affect.
	 * @param noble : The {@link OlympiadNoble} to set.
	 * @return The old {@link OlympiadNoble} if the {@link Player} objectId was already present, or null otherwise.
	 */
	public OlympiadNoble addNoble(int objectId, OlympiadNoble noble)
	{
		return _nobles.put(objectId, noble);
	}
	
	public int getNoblePoints(int objId)
	{
		final OlympiadNoble noble = _nobles.get(objId);
		return (noble == null) ? 0 : noble.getPoints();
	}
	
	public OlympiadState getCurrentPeriod()
	{
		return _period;
	}
	
	public static final Olympiad getInstance()
	{
		return SingletonHolder.INSTANCE;
	}
	
	private static class SingletonHolder
	{
		protected static final Olympiad INSTANCE = new Olympiad();
	}
}