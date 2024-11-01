package com.shnok.javaserver.gameserver.data.manager;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.Collections;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.atomic.AtomicInteger;

import com.shnok.javaserver.commons.logging.CLogger;
import com.shnok.javaserver.commons.pool.ConnectionPool;

import com.shnok.javaserver.gameserver.model.actor.Player;

/**
 * Loads and stores Raid Points.<br>
 * <br>
 * {@link Player}s obtain Raid Points by killing Raid Bosses within the world of Lineage 2. Each week the Top ranked players clans get rewarded with Clan Reputation Points in relation to their members ranking.
 */
public class RaidPointManager
{
	private static final CLogger LOGGER = new CLogger(RaidPointManager.class.getName());
	
	private static final String LOAD_DATA = "SELECT * FROM character_raid_points";
	private static final String INSERT_DATA = "REPLACE INTO character_raid_points (char_id,boss_id,points) VALUES (?,?,?)";
	private static final String TRUNCATE_DATA = "TRUNCATE character_raid_points";
	
	private final Map<Integer, Map<Integer, Integer>> _entries = new ConcurrentHashMap<>();
	
	public RaidPointManager()
	{
		try (Connection con = ConnectionPool.getConnection();
			PreparedStatement ps = con.prepareStatement(LOAD_DATA);
			ResultSet rs = ps.executeQuery())
		{
			while (rs.next())
			{
				final int objectId = rs.getInt("char_id");
				final int bossId = rs.getInt("boss_id");
				final int points = rs.getInt("points");
				
				final Map<Integer, Integer> playerData = _entries.computeIfAbsent(objectId, m -> new HashMap<>());
				playerData.put(bossId, points);
			}
		}
		catch (Exception e)
		{
			LOGGER.error("Couldn't load RaidPoints entries.", e);
		}
		LOGGER.info("Loaded {} RaidPoints entries.", _entries.size());
	}
	
	public final Map<Integer, Integer> getList(Player player)
	{
		return _entries.get(player.getObjectId());
	}
	
	/**
	 * Add points for a given {@link Player} and a given boss npcId.
	 * @param player : The {@link Player} used for objectId.
	 * @param bossId : The boss npcId to register.
	 * @param points : The points to add.
	 */
	public final void addPoints(Player player, int bossId, int points)
	{
		if (points < 0)
			return;
		
		final int objectId = player.getObjectId();
		final Map<Integer, Integer> playerData = _entries.computeIfAbsent(objectId, m -> new HashMap<>());
		
		points = playerData.merge(bossId, points, Integer::sum);
		
		try (Connection con = ConnectionPool.getConnection();
			PreparedStatement ps = con.prepareStatement(INSERT_DATA))
		{
			ps.setInt(1, objectId);
			ps.setInt(2, bossId);
			ps.setInt(3, points);
			ps.executeUpdate();
		}
		catch (Exception e)
		{
			LOGGER.error("Couldn't update RaidPoints entries.", e);
		}
	}
	
	/**
	 * @param objectId : The objectId of the {@link Player} to check.
	 * @return the cumulated amount of points for a player, or 0 if no entry is found.
	 */
	public final int getPointsByOwnerId(int objectId)
	{
		final Map<Integer, Integer> playerData = _entries.get(objectId);
		if (playerData == null || playerData.isEmpty())
			return 0;
		
		return playerData.values().stream().mapToInt(Number::intValue).sum();
	}
	
	/**
	 * Clean both database and memory from content.
	 */
	public final void cleanUp()
	{
		_entries.clear();
		
		try (Connection con = ConnectionPool.getConnection();
			PreparedStatement ps = con.prepareStatement(TRUNCATE_DATA))
		{
			ps.executeUpdate();
		}
		catch (Exception e)
		{
			LOGGER.error("Couldn't delete RaidPoints entries.", e);
		}
	}
	
	/**
	 * @param objectId : The objectId of the {@link Player} to check.
	 * @return the current rank of a {@link Player} based on its objectId, or 0 if no entry is found.
	 */
	public final int calculateRanking(int objectId)
	{
		// Iterate the global list, retrieve players and associated cumulated points. Store them if > 0.
		final Map<Integer, Integer> playersData = new HashMap<>();
		for (int ownerId : _entries.keySet())
		{
			final int points = getPointsByOwnerId(ownerId);
			if (points > 0)
				playersData.put(ownerId, points);
		}
		
		final AtomicInteger counter = new AtomicInteger(1);
		
		final Map<Integer, Integer> rankMap = new LinkedHashMap<>();
		
		// Sort the temporary points map, then feed rankMap with objectId as key and ranking as value.
		playersData.entrySet().stream().sorted(Collections.reverseOrder(Map.Entry.comparingByValue())).forEachOrdered(e -> rankMap.put(e.getKey(), counter.getAndIncrement()));
		
		final Integer rank = rankMap.get(objectId);
		return (rank == null) ? 0 : rank;
	}
	
	/**
	 * A method used to generate the ranking map. Limited to the first 100 {@link Player}s.
	 * <ul>
	 * <li>Retrieve {@link Player} data, compute their points and store the entry on a temporary {@link Map} if the points are > 0.</li>
	 * <li>Sort the temporary {@link Map} based on points and feed a second {@link Map} with objectId<>ranking.</li>
	 * </ul>
	 * @return a {@link Map} consisting of {@link Player} objectId for the key, and ranking for the value.
	 */
	public Map<Integer, Integer> getWinners()
	{
		// Iterate the global list, retrieve players and associated cumulated points. Store them if > 0.
		final Map<Integer, Integer> playersData = new HashMap<>();
		for (int objectId : _entries.keySet())
		{
			final int points = getPointsByOwnerId(objectId);
			if (points > 0)
				playersData.put(objectId, points);
		}
		
		final AtomicInteger counter = new AtomicInteger(1);
		
		final Map<Integer, Integer> rankMap = new LinkedHashMap<>();
		
		// Sort the temporary points map, then feed rankMap with objectId as key and ranking as value.
		playersData.entrySet().stream().sorted(Collections.reverseOrder(Map.Entry.comparingByValue())).limit(100).forEachOrdered(e -> rankMap.put(e.getKey(), counter.getAndIncrement()));
		
		return rankMap;
	}
	
	public static final RaidPointManager getInstance()
	{
		return SingletonHolder.INSTANCE;
	}
	
	private static class SingletonHolder
	{
		protected static final RaidPointManager INSTANCE = new RaidPointManager();
	}
}