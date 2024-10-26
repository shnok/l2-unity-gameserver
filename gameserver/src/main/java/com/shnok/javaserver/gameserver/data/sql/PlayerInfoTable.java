package com.shnok.javaserver.gameserver.data.sql;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.Map;
import java.util.Map.Entry;
import java.util.concurrent.ConcurrentHashMap;

import com.shnok.javaserver.commons.logging.CLogger;
import com.shnok.javaserver.commons.pool.ConnectionPool;

import com.shnok.javaserver.gameserver.model.actor.Player;

/**
 * This class caches few {@link Player}s informations. It keeps a link between objectId and a {@link PlayerInfo}.
 * <p>
 * It is notably used for any offline character check, such as friendlist, existing character name, etc.
 * </p>
 */
public final class PlayerInfoTable
{
	private static final CLogger LOGGER = new CLogger(PlayerInfoTable.class.getName());
	
	private static final String LOAD_DATA = "SELECT account_name, obj_Id, char_name, accesslevel FROM characters";
	
	private final Map<Integer, PlayerInfo> _infos = new ConcurrentHashMap<>();
	
	protected PlayerInfoTable()
	{
		try (Connection con = ConnectionPool.getConnection();
			PreparedStatement ps = con.prepareStatement(LOAD_DATA);
			ResultSet rs = ps.executeQuery())
		{
			while (rs.next())
				_infos.put(rs.getInt("obj_Id"), new PlayerInfo(rs.getString("account_name"), rs.getString("char_name"), rs.getInt("accesslevel")));
		}
		catch (Exception e)
		{
			LOGGER.error("Couldn't load player infos.", e);
		}
		
		LOGGER.info("Loaded {} player infos.", _infos.size());
	}
	
	/**
	 * Caches {@link Player} informations, but only if not already existing.
	 * @param objectId : The player's objectId.
	 * @param accountName : The player's account name.
	 * @param playerName : The player's name.
	 * @param accessLevel : The player's access level.
	 */
	public final void addPlayer(int objectId, String accountName, String playerName, int accessLevel)
	{
		_infos.putIfAbsent(objectId, new PlayerInfo(accountName, playerName, accessLevel));
	}
	
	/**
	 * Update the {@link Player} data. The informations must already exist. Used for name and access level edition.
	 * @param player : The player to update.
	 * @param onlyAccessLevel : If true, it will update the access level, otherwise, it will update the player name.
	 */
	public final void updatePlayerData(Player player, boolean onlyAccessLevel)
	{
		if (player == null)
			return;
		
		final PlayerInfo data = _infos.get(player.getObjectId());
		if (data != null)
		{
			if (onlyAccessLevel)
				data.setAccessLevel(player.getAccessLevel().getLevel());
			else
			{
				final String playerName = player.getName();
				if (!data.getPlayerName().equalsIgnoreCase(playerName))
					data.setPlayerName(playerName);
			}
		}
	}
	
	/**
	 * Remove a {@link Player} entry.
	 * @param objId : The objectId to check.
	 */
	public final void removePlayer(int objId)
	{
		if (_infos.containsKey(objId))
			_infos.remove(objId);
	}
	
	/**
	 * Get {@link Player} objectId by name (reversing call).
	 * @param playerName : The name to check.
	 * @return the player objectId.
	 */
	public final int getPlayerObjectId(String playerName)
	{
		if (playerName == null || playerName.isEmpty())
			return -1;
		
		return _infos.entrySet().stream().filter(m -> m.getValue().getPlayerName().equalsIgnoreCase(playerName)).map(Entry::getKey).findFirst().orElse(-1);
	}
	
	/**
	 * Get {@link Player} name by object id.
	 * @param objId : The objectId to check.
	 * @return the player name.
	 */
	public final String getPlayerName(int objId)
	{
		final PlayerInfo data = _infos.get(objId);
		return (data != null) ? data.getPlayerName() : null;
	}
	
	/**
	 * Get {@link Player} access level by object id.
	 * @param objId : The objectId to check.
	 * @return the access level.
	 */
	public final int getPlayerAccessLevel(int objId)
	{
		final PlayerInfo data = _infos.get(objId);
		return (data != null) ? data.getAccessLevel() : 0;
	}
	
	/**
	 * Retrieve characters amount from any account, by account name.
	 * @param accountName : The account name to check.
	 * @return the number of characters stored into this account.
	 */
	public final int getCharactersInAcc(String accountName)
	{
		return (int) _infos.entrySet().stream().filter(m -> m.getValue().getAccountName().equalsIgnoreCase(accountName)).count();
	}
	
	/**
	 * A datatype used to retain Player informations such as account name, player name and access level.
	 */
	private final class PlayerInfo
	{
		private final String _accountName;
		private String _playerName;
		private int _accessLevel;
		
		public PlayerInfo(String accountName, String playerName, int accessLevel)
		{
			_accountName = accountName;
			_playerName = playerName;
			_accessLevel = accessLevel;
		}
		
		public final String getAccountName()
		{
			return _accountName;
		}
		
		public final String getPlayerName()
		{
			return _playerName;
		}
		
		public final int getAccessLevel()
		{
			return _accessLevel;
		}
		
		public final void setPlayerName(String playerName)
		{
			_playerName = playerName;
		}
		
		public final void setAccessLevel(int accessLevel)
		{
			_accessLevel = accessLevel;
		}
	}
	
	public static final PlayerInfoTable getInstance()
	{
		return SingletonHolder.INSTANCE;
	}
	
	private static final class SingletonHolder
	{
		protected static final PlayerInfoTable INSTANCE = new PlayerInfoTable();
	}
}