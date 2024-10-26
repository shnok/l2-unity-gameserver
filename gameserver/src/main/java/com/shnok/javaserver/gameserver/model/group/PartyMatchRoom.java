package com.shnok.javaserver.gameserver.model.group;

import java.util.Collections;
import java.util.List;
import java.util.concurrent.CopyOnWriteArrayList;

import net.sf.l2j.gameserver.data.manager.PartyMatchRoomManager;
import net.sf.l2j.gameserver.data.manager.RelationManager;
import net.sf.l2j.gameserver.data.xml.RestartPointData;
import net.sf.l2j.gameserver.model.WorldObject;
import net.sf.l2j.gameserver.model.actor.Player;
import net.sf.l2j.gameserver.model.restart.RestartPoint;
import net.sf.l2j.gameserver.network.NpcStringId;
import net.sf.l2j.gameserver.network.SystemMessageId;
import net.sf.l2j.gameserver.network.serverpackets.CreatureSay;
import net.sf.l2j.gameserver.network.serverpackets.ExClosePartyRoom;
import net.sf.l2j.gameserver.network.serverpackets.ExManagePartyRoomMember;
import net.sf.l2j.gameserver.network.serverpackets.ExShowScreenMessage;
import net.sf.l2j.gameserver.network.serverpackets.L2GameServerPacket;
import net.sf.l2j.gameserver.network.serverpackets.SystemMessage;

public class PartyMatchRoom extends AbstractGroup
{
	private final List<Player> _members = new CopyOnWriteArrayList<>();
	
	private final int _id;
	
	private String _title;
	private int _lootType;
	private int _location;
	private int _minLvl;
	private int _maxLvl;
	private int _maxMembersCount;
	
	public PartyMatchRoom(int id, String title, int loot, int minLvl, int maxLvl, int maxMembersCount, Player leader)
	{
		super(leader);
		
		_id = id;
		_title = title;
		_lootType = loot;
		
		refreshLocation();
		
		_minLvl = minLvl;
		_maxLvl = maxLvl;
		_maxMembersCount = maxMembersCount;
		
		_members.add(leader);
	}
	
	@Override
	public List<Player> getMembers()
	{
		return _members;
	}
	
	@Override
	public int getMembersCount()
	{
		return _members.size();
	}
	
	@Override
	public boolean containsPlayer(WorldObject player)
	{
		return _members.contains(player);
	}
	
	@Override
	public void broadcastPacket(L2GameServerPacket packet)
	{
		for (Player member : _members)
			member.sendPacket(packet);
	}
	
	@Override
	public void broadcastCreatureSay(CreatureSay msg, Player broadcaster)
	{
		for (Player member : _members)
		{
			if (!RelationManager.getInstance().isInBlockList(member, broadcaster))
				member.sendPacket(msg);
		}
	}
	
	@Override
	public void broadcastOnScreen(int time, NpcStringId npcStringId)
	{
		broadcastPacket(new ExShowScreenMessage(npcStringId.getMessage(), time));
	}
	
	@Override
	public void broadcastOnScreen(int time, NpcStringId npcStringId, Object... params)
	{
		broadcastPacket(new ExShowScreenMessage(npcStringId.getMessage(params), time));
	}
	
	@Override
	public void recalculateLevel()
	{
		// Do nothing.
	}
	
	@Override
	public void disband()
	{
		for (Player member : _members)
		{
			member.sendPacket(ExClosePartyRoom.STATIC_PACKET);
			member.sendPacket(SystemMessageId.PARTY_ROOM_DISBANDED);
			
			member.setPartyRoom(0);
			member.broadcastUserInfo();
		}
		_members.clear();
	}
	
	public int getId()
	{
		return _id;
	}
	
	public int getLootType()
	{
		return _lootType;
	}
	
	public void setLootType(int lootType)
	{
		_lootType = lootType;
	}
	
	public int getMinLvl()
	{
		return _minLvl;
	}
	
	public void setMinLvl(int minLvl)
	{
		_minLvl = minLvl;
	}
	
	public int getMaxLvl()
	{
		return _maxLvl;
	}
	
	public void setMaxLvl(int maxLvl)
	{
		_maxLvl = maxLvl;
	}
	
	public int getLocation()
	{
		return _location;
	}
	
	public void refreshLocation()
	{
		final RestartPoint rp = RestartPointData.getInstance().getRestartPoint(getLeader());
		_location = (rp == null) ? 100 : rp.getBbs();
	}
	
	public int getMaxMembers()
	{
		return _maxMembersCount;
	}
	
	public void setMaxMembers(int maxmem)
	{
		_maxMembersCount = maxmem;
	}
	
	public String getTitle()
	{
		return _title;
	}
	
	public void setTitle(String title)
	{
		_title = title;
	}
	
	/**
	 * Add a {@link Player} on this {@link PartyMatchRoom}.
	 * @param player : The {@link Player} to add.
	 * @param roomId : The room id to link this {@link Player} with.
	 */
	public void addMember(Player player, int roomId)
	{
		// Add member on memory.
		_members.add(player);
		
		// Set the PartyMatchRoom room id.
		player.setPartyRoom(roomId);
		
		// Add player's LFP title.
		player.broadcastUserInfo();
	}
	
	/**
	 * Remove the {@link Player} set as parameter from this {@link PartyMatchRoom}.
	 * <ul>
	 * <li>If this {@link Player} isn't the leader, remove it properly.</li>
	 * <li>If it was the last {@link Player}, delete this {@link PartyMatchRoom}.</li>
	 * <li>If this {@link Player} is the leader, promote first a new leader and then remove that {@link Player} as a regular member.</li>
	 * </ul>
	 * @param player : The {@link Player} to remove.
	 */
	public void removeMember(Player player)
	{
		if (player == null || !_members.contains(player))
			return;
		
		if (!isLeader(player))
		{
			// Remove member from memory.
			_members.remove(player);
			
			// Inform and update other members.
			for (Player member : _members)
			{
				final SystemMessage sm = SystemMessage.getSystemMessage(SystemMessageId.S1_LEFT_PARTY_ROOM);
				sm.addCharName(player);
				
				member.sendPacket(sm);
				member.sendPacket(new ExManagePartyRoomMember(player, this, 2));
			}
			
			// Close the PartyRoom window.
			player.sendPacket(ExClosePartyRoom.STATIC_PACKET);
			
			// Set back the PartyMatchRoom room id to 0.
			player.setPartyRoom(0);
			
			// Clean player's LFP title.
			player.broadcastUserInfo();
		}
		// Single member room, disband.
		else if (_members.size() == 1)
		{
			PartyMatchRoomManager.getInstance().deleteRoom(_id);
		}
		// Leader is dropped ; change leader and reprocess this method one more time.
		else
		{
			// Retrieve a new valid candidate for room control leader.
			final Player newLeader = _members.stream().filter(m -> m != getLeader()).findFirst().orElse(null);
			
			// Change room leader.
			changeLeader(newLeader);
			
			// Remove the current leader, as a member.
			removeMember(player);
		}
	}
	
	/**
	 * Change this {@link PartyMatchRoom} leader.
	 * @param newLeader : The {@link Player} to promote as leader.
	 */
	public void changeLeader(Player newLeader)
	{
		// Return for following conditions.
		if (newLeader == null || !_members.contains(newLeader) || isLeader(newLeader) || _members.size() == 1)
			return;
		
		// Return if we can't find the new leader index.
		final int newLeaderIndex = _members.indexOf(newLeader);
		if (newLeaderIndex == -1)
			return;
		
		// Keep old leader reference.
		Player oldLeader = getLeader();
		
		// Swap positions.
		Collections.swap(_members, 0, newLeaderIndex);
		
		// Set new leader.
		setLeader(newLeader);
		
		// Broadcast change.
		for (Player member : _members)
		{
			member.sendPacket(new ExManagePartyRoomMember(newLeader, this, 1));
			member.sendPacket(new ExManagePartyRoomMember(oldLeader, this, 1));
			member.sendPacket(SystemMessageId.PARTY_ROOM_LEADER_CHANGED);
		}
	}
	
	/**
	 * @return True if this {@link PartyMatchRoom} is actually full.
	 */
	public boolean isFull()
	{
		return _members.size() >= _maxMembersCount;
	}
	
	/**
	 * @param player : The {@link Player} to test.
	 * @return True if the {@link Player} set as parameter can enter in this {@link PartyMatchRoom}.
	 */
	public boolean checkEntrance(Player player)
	{
		return player.getStatus().getLevel() >= _minLvl && player.getStatus().getLevel() <= _maxLvl && _members.size() < _maxMembersCount;
	}
}