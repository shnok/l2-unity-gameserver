package com.shnok.javaserver.gameserver.model;

import java.util.List;
import java.util.concurrent.atomic.AtomicBoolean;

import com.shnok.javaserver.gameserver.model.group.Party;
import com.shnok.javaserver.gameserver.taskmanager.GameTimeTaskManager;

public class RoomInfo
{
	private final int _index;
	private final AtomicBoolean _isLocked = new AtomicBoolean();
	
	private int _time;
	private Party _party;
	private List<Integer> _memberIds;
	
	public RoomInfo(int index)
	{
		_index = index;
	}
	
	public int getIndex()
	{
		return _index;
	}
	
	public int getTime()
	{
		return _time;
	}
	
	public Party getParty()
	{
		return _party;
	}
	
	public void setParty(Party party)
	{
		_party = party;
		_memberIds = party.getMembers().stream().map(pm -> pm.getObjectId()).toList();
	}
	
	/**
	 * @return True if the {@link Party} evolved, being an edition of members amount or Player objectIds - or false otherwise.
	 */
	public boolean isPartyChanged()
	{
		if (_party == null)
			return true;
		
		if (_memberIds.size() != _party.getMembers().size())
			return true;
		
		return !_party.getMembers().stream().allMatch(m -> _memberIds.contains(m.getObjectId()));
	}
	
	public List<Integer> getMemberIds()
	{
		return _memberIds;
	}
	
	public boolean isLocked()
	{
		return _isLocked.get();
	}
	
	public void setLock(boolean isLocked)
	{
		_time = GameTimeTaskManager.getInstance().getCurrentTick();
		_isLocked.set(isLocked);
	}
	
	/**
	 * Reset {@link Party} and {@link List} member ids references, and unlock the room.
	 */
	public void clear()
	{
		_party = null;
		_memberIds = null;
		
		setLock(false);
	}
}