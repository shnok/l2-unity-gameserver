package com.shnok.javaserver.gameserver.data.manager;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.HashSet;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;

import com.shnok.javaserver.commons.logging.CLogger;
import com.shnok.javaserver.commons.pool.ConnectionPool;

import com.shnok.javaserver.gameserver.data.sql.PlayerInfoTable;
import com.shnok.javaserver.gameserver.model.World;
import com.shnok.javaserver.gameserver.model.actor.Player;
import com.shnok.javaserver.gameserver.model.records.PlayerPair;
import com.shnok.javaserver.gameserver.network.SystemMessageId;
import com.shnok.javaserver.gameserver.network.serverpackets.unused.L2FriendStatus;
import com.shnok.javaserver.gameserver.network.serverpackets.SystemMessage;

public class RelationManager
{
	private static final CLogger LOGGER = new CLogger(RelationManager.class.getName());
	
	private static final String LOAD = "SELECT * FROM character_relations";
	private static final String ADD_OR_UPDATE = "INSERT INTO character_relations (char_id, friend_id, relation) VALUES (?,?,?) ON DUPLICATE KEY UPDATE relation=VALUES(relation)";
	private static final String DELETE = "DELETE FROM character_relations WHERE char_id=? AND friend_id=?";
	
	private static final int ARE_FRIENDS = 1;
	private static final int CHAR_BLOCKS_FRIEND = 2;
	private static final int FRIEND_BLOCKS_CHAR = 4;
	
	private final Map<PlayerPair, Integer> _relations = new ConcurrentHashMap<>();
	
	private RelationManager()
	{
		try (Connection con = ConnectionPool.getConnection();
			PreparedStatement ps = con.prepareStatement(LOAD);
			ResultSet rs = ps.executeQuery())
		{
			while (rs.next())
				_relations.putIfAbsent(new PlayerPair(rs.getInt("char_id"), rs.getInt("friend_id")), rs.getInt("relation"));
		}
		catch (Exception e)
		{
			LOGGER.error("Couldn't restore relations.", e);
		}
	}
	
	/**
	 * Add, update or delete entries. Lazily done on server shutdown.
	 */
	public void save()
	{
		if (_relations.isEmpty())
			return;
		
		try (Connection con = ConnectionPool.getConnection();
			PreparedStatement ps = con.prepareStatement(ADD_OR_UPDATE);
			PreparedStatement ps2 = con.prepareStatement(DELETE))
		{
			for (Entry<PlayerPair, Integer> entry : _relations.entrySet())
			{
				final PlayerPair pair = entry.getKey();
				final int relation = entry.getValue();
				
				if (relation == 0)
				{
					ps2.setInt(1, pair.id1());
					ps2.setInt(2, pair.id2());
					ps2.addBatch();
				}
				else
				{
					ps.setInt(1, pair.id1());
					ps.setInt(2, pair.id2());
					ps.setInt(3, relation);
					ps.addBatch();
				}
			}
			
			ps.executeBatch();
			ps2.executeBatch();
		}
		catch (Exception e)
		{
			LOGGER.error("Couldn't store players relations.", e);
		}
	}
	
	/**
	 * @param player : The {@link Player}'s blocklist to test.
	 * @param target : The {@link Player} target to check.
	 * @return True if the {@link Player} target is set on the tested {@link Player}'s blocklist, false otherwise.
	 */
	public boolean isInBlockList(Player player, Player target)
	{
		return isInBlockList(player, target.getObjectId());
	}
	
	/**
	 * @param player : The {@link Player}'s blocklist to test.
	 * @param targetId : The objectId to check.
	 * @return True if the objectId target is set on the tested {@link Player}'s blocklist, false otherwise.
	 */
	private boolean isInBlockList(Player player, int targetId)
	{
		final PlayerPair key = new PlayerPair(player.getObjectId(), targetId);
		
		final Integer relation = _relations.get(key);
		if (relation == null || relation == 0)
			return false;
		
		return (player.getObjectId() == key.id1() && (relation & CHAR_BLOCKS_FRIEND) != 0) || (player.getObjectId() == key.id2() && (relation & FRIEND_BLOCKS_CHAR) != 0);
	}
	
	/**
	 * @param playerId : The objectId to test.
	 * @return A {@link Set} of blocked objectIds related to the objectId set as parameter.
	 */
	public Set<Integer> getBlockList(int playerId)
	{
		final Set<Integer> blockList = new HashSet<>();
		
		_relations.forEach((playerPair, relation) ->
		{
			if (playerPair.contains(playerId))
			{
				if (playerId == playerPair.id1() && (relation & CHAR_BLOCKS_FRIEND) != 0)
					blockList.add(playerPair.id2());
				else if (playerId == playerPair.id2() && (relation & FRIEND_BLOCKS_CHAR) != 0)
					blockList.add(playerPair.id1());
			}
		});
		
		return blockList;
	}
	
	/**
	 * Add the objectId set as parameter into the {@link Player}'s blocklist. Send related {@link SystemMessage}s to both {@link Player}s.
	 * @param player : The {@link Player}'s blocklist to edit.
	 * @param targetId : The objectId to add.
	 */
	public void addToBlockList(Player player, int targetId)
	{
		final int playerId = player.getObjectId();
		if (playerId == targetId)
			return;
		
		player.sendPacket(SystemMessage.getSystemMessage(SystemMessageId.S1_WAS_ADDED_TO_YOUR_IGNORE_LIST).addCharName(targetId));
		
		final Player targetPlayer = World.getInstance().getPlayer(targetId);
		if (targetPlayer != null)
			targetPlayer.sendPacket(SystemMessage.getSystemMessage(SystemMessageId.S1_HAS_ADDED_YOU_TO_IGNORE_LIST).addString(player.getName()));
		
		updateRelation(playerId, targetId, (playerId < targetId ? CHAR_BLOCKS_FRIEND : FRIEND_BLOCKS_CHAR), true);
	}
	
	/**
	 * Remove the objectId set as parameter from the {@link Player}'s blocklist. Send related {@link SystemMessage}s to both {@link Player}s.
	 * @param player : The {@link Player}'s blocklist to edit.
	 * @param targetId : The objectId to remove.
	 */
	public void removeFromBlockList(Player player, int targetId)
	{
		if (!isInBlockList(player, targetId))
			return;
		
		player.sendPacket(SystemMessage.getSystemMessage(SystemMessageId.S1_WAS_REMOVED_FROM_YOUR_IGNORE_LIST).addCharName(targetId));
		
		updateRelation(player.getObjectId(), targetId, (player.getObjectId() < targetId ? CHAR_BLOCKS_FRIEND : FRIEND_BLOCKS_CHAR), false);
	}
	
	/**
	 * @param playerId : The first objectId to test.
	 * @param targetId : The second objectId to test.
	 * @return True if an existing relation involving both objectIds matches with ARE_FRIENDS flag, false otherwise.
	 */
	public boolean areFriends(int playerId, int targetId)
	{
		final PlayerPair key = new PlayerPair(playerId, targetId);
		
		final Integer relation = _relations.get(key);
		if (relation == null || relation == 0)
			return false;
		
		return (relation & ARE_FRIENDS) != 0;
	}
	
	/**
	 * @param playerId : The objectId to test.
	 * @return A {@link Set} of friendly objectIds related to the objectId set as parameter.
	 */
	public Set<Integer> getFriendList(int playerId)
	{
		final Set<Integer> friendList = new HashSet<>();
		
		_relations.forEach((playerPair, relation) ->
		{
			if (playerPair.contains(playerId) && (relation & ARE_FRIENDS) != 0)
				friendList.add((playerId == playerPair.id1()) ? playerPair.id2() : playerPair.id1());
		});
		
		return friendList;
	}
	
	/**
	 * Add the objectId set as parameter into the {@link Player}'s friendlist.
	 * @param player : The {@link Player}'s friendlist to edit.
	 * @param targetId : The objectId to add.
	 */
	public void addToFriendList(Player player, int targetId)
	{
		final int playerId = player.getObjectId();
		if (playerId == targetId)
			return;
		
		updateRelation(playerId, targetId, ARE_FRIENDS, true);
	}
	
	/**
	 * Remove the objectId set as parameter from the {@link Player}'s friendlist. Send related {@link SystemMessage}s to both {@link Player}s.
	 * @param player : The {@link Player}'s friendlist to edit.
	 * @param targetId : The objectId to remove.
	 */
	public void removeFromFriendList(Player player, int targetId)
	{
		if (!areFriends(player.getObjectId(), targetId))
			return;
		
		player.sendPacket(SystemMessage.getSystemMessage(SystemMessageId.S1_HAS_BEEN_DELETED_FROM_YOUR_FRIENDS_LIST).addCharName(targetId));
		
		updateRelation(player.getObjectId(), targetId, ARE_FRIENDS, false);
	}
	
	/**
	 * Notify all online friends the {@link Player} set as parameter logged in.
	 * @param player : The {@link Player}'s friendlist to operate on.
	 * @param isOnline : If True, we send a message to each friend.
	 */
	public void notifyFriends(Player player, boolean isOnline)
	{
		for (final int id : getFriendList(player.getObjectId()))
		{
			final Player friend = World.getInstance().getPlayer(id);
			if (friend != null)
			{
				friend.sendPacket(new L2FriendStatus(player, isOnline));
				
				if (isOnline)
					friend.sendPacket(SystemMessage.getSystemMessage(SystemMessageId.FRIEND_S1_HAS_LOGGED_IN).addCharName(player));
			}
		}
	}
	
	/**
	 * Send the blocklist, called by /blocklist usercommand, to the {@link Player} set as parameter.
	 * @param player : The {@link Player} calling the usercommand.
	 */
	public void sendBlockList(Player player)
	{
		int i = 1;
		player.sendPacket(SystemMessageId.BLOCK_LIST_HEADER);
		
		for (int playerId : getBlockList(player.getObjectId()))
			player.sendMessage((i++) + ". " + PlayerInfoTable.getInstance().getPlayerName(playerId));
		
		player.sendPacket(SystemMessageId.FRIEND_LIST_FOOTER);
	}
	
	/**
	 * Remove all existing friends from the {@link Player} set as parameter.
	 * @param player : The {@link Player} to affect.
	 */
	public void removeAllFromFriendList(Player player)
	{
		getFriendList(player.getObjectId()).forEach(targetId -> removeFromFriendList(player, targetId));
	}
	
	/**
	 * Remove all existing blocked people from the {@link Player} set as parameter.
	 * @param player : The {@link Player} to affect.
	 */
	public void removeAllFromBlockList(Player player)
	{
		getBlockList(player.getObjectId()).forEach(targetId -> removeFromBlockList(player, targetId));
	}
	
	/**
	 * Update existing relation flags, or add a new entry. If relation is set to 0, the entry will be deleted upon {@link RelationManager#save()}.
	 * @param id1 : The first objectId.
	 * @param id2 : The second objectId.
	 * @param flag : The flag to add or remove.
	 * @param add : If True, we add the related flag, otherwise we delete it from the relation.
	 */
	private void updateRelation(int id1, int id2, int flag, boolean add)
	{
		if (id1 == id2)
			return;
		
		final PlayerPair key = new PlayerPair(id1, id2);
		
		_relations.compute(key, (k, oldRelation) ->
		{
			int relation = (oldRelation != null) ? oldRelation : 0;
			if (add)
				relation = relation | flag;
			else
				relation = relation & ~flag;
			
			return relation;
		});
	}
	
	public static final RelationManager getInstance()
	{
		return SingletonHolder.INSTANCE;
	}
	
	private static class SingletonHolder
	{
		protected static final RelationManager INSTANCE = new RelationManager();
	}
}