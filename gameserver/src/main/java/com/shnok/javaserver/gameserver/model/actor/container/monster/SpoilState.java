package com.shnok.javaserver.gameserver.model.actor.container.monster;

import java.util.HashMap;
import java.util.List;

import net.sf.l2j.gameserver.model.actor.Player;
import net.sf.l2j.gameserver.model.actor.instance.Monster;

/**
 * A container holding all related informations of a {@link Monster} spoil state.<br>
 * <br>
 * A spoil occurs when a {@link Player} procs a spoil skill over a Monster.
 */
public class SpoilState extends HashMap<Integer, Integer>
{
	private static final long serialVersionUID = 1L;
	
	private int _spoilerId;
	
	public int getSpoilerId()
	{
		return _spoilerId;
	}
	
	public void setSpoilerId(int value)
	{
		_spoilerId = value;
	}
	
	/**
	 * @return true if the spoiler objectId is set.
	 */
	public boolean isSpoiled()
	{
		return _spoilerId > 0;
	}
	
	/**
	 * @param player : The Player to test.
	 * @return true if the given {@link Player} set as parameter is the actual spoiler.
	 */
	public boolean isActualSpoiler(Player player)
	{
		return player != null && player.getObjectId() == _spoilerId;
	}
	
	/**
	 * @return true if _sweepItems {@link List} is filled.
	 */
	public boolean isSweepable()
	{
		return !isEmpty();
	}
	
	/**
	 * Clear all spoil related variables.
	 */
	@Override
	public void clear()
	{
		_spoilerId = 0;
		
		super.clear();
	}
}