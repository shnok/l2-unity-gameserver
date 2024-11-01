package com.shnok.javaserver.gameserver.data.manager;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.function.Predicate;
import java.util.stream.Stream;

import com.shnok.javaserver.gameserver.model.actor.Player;
import com.shnok.javaserver.gameserver.model.group.PartyMatchRoom;

public class PartyMatchRoomManager
{
	private final Map<Integer, PartyMatchRoom> _rooms = new ConcurrentHashMap<>();
	
	private final Set<Player> _waitingMembers = ConcurrentHashMap.newKeySet();
	
	private AtomicInteger _currentId = new AtomicInteger();
	
	protected PartyMatchRoomManager()
	{
	}
	
	public int getNewRoomId()
	{
		return _currentId.incrementAndGet();
	}
	
	public PartyMatchRoom getRoom(int id)
	{
		return _rooms.get(id);
	}
	
	public void addRoom(int id, PartyMatchRoom room)
	{
		_rooms.put(id, room);
	}
	
	public void deleteRoom(int id)
	{
		final PartyMatchRoom room = _rooms.remove(id);
		if (room != null)
			room.disband();
	}
	
	/**
	 * @param player : The {@link Player} to test.
	 * @param bbs : The bbs region to test.
	 * @param levelMode : The level mode to test.
	 * @return The first available {@link PartyMatchRoom} for the {@link Player} set as parameter.
	 */
	public PartyMatchRoom getFirstAvailableRoom(Player player, int bbs, int levelMode)
	{
		return getRooms(player, bbs, levelMode).findFirst().orElse(null);
	}
	
	/**
	 * @param player : The {@link Player} to test.
	 * @param bbs : The bbs region to test.
	 * @param levelMode : The level mode to test.
	 * @return The {@link Collection} of {@link PartyMatchRoom}s for the {@link Player} set as parameter.
	 */
	public Collection<PartyMatchRoom> getAvailableRooms(Player player, int bbs, int levelMode)
	{
		return getRooms(player, bbs, levelMode).toList();
	}
	
	/**
	 * @param player : The {@link Player} to test.
	 * @param bbs : The bbs region to test.
	 * @param levelMode : The level mode to test.
	 * @return A {@link Stream} of {@link PartyMatchRoom}s based on parameters.
	 */
	private Stream<PartyMatchRoom> getRooms(Player player, int bbs, int levelMode)
	{
		Predicate<PartyMatchRoom> predicate = r -> !r.isFull();
		
		// -2 is Near Me, -1 is All, leftover is regular location check. 0 is offline. 100 is for Change.
		if (bbs == -2)
			predicate = predicate.and(r -> r.getLeader().getRegion() == player.getRegion());
		else if (bbs != -1)
			predicate = predicate.and(r -> r.getLocation() == bbs);
		
		// 0 is My level range, 1 is All.
		if (levelMode == 0)
			predicate = predicate.and(r -> player.getStatus().getLevel() >= r.getMinLvl() && player.getStatus().getLevel() <= r.getMaxLvl());
		
		return _rooms.values().stream().filter(predicate);
	}
	
	public int getRoomsCount()
	{
		return _rooms.size();
	}
	
	public Set<Player> getWaitingPlayers()
	{
		return _waitingMembers;
	}
	
	public void addWaitingPlayer(Player player)
	{
		_waitingMembers.add(player);
	}
	
	public boolean removeWaitingPlayer(Player player)
	{
		return _waitingMembers.remove(player);
	}
	
	public List<Player> getAvailableWaitingMembers(Player player, int minLvl, int maxLvl)
	{
		final List<Player> members = new ArrayList<>();
		
		for (Player member : _waitingMembers)
		{
			if (member == player)
				continue;
			
			if (member.getStatus().getLevel() < minLvl || member.getStatus().getLevel() > maxLvl)
				continue;
			
			members.add(member);
		}
		return members;
	}
	
	public static PartyMatchRoomManager getInstance()
	{
		return SingletonHolder.INSTANCE;
	}
	
	private static class SingletonHolder
	{
		protected static final PartyMatchRoomManager INSTANCE = new PartyMatchRoomManager();
	}
}