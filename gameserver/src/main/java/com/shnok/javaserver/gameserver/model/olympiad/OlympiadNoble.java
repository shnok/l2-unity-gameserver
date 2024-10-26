package com.shnok.javaserver.gameserver.model.olympiad;

import net.sf.l2j.Config;
import net.sf.l2j.gameserver.model.actor.Player;

public class OlympiadNoble
{
	private int _classId;
	private String _name;
	private int _points;
	private int _compDone;
	private int _compWon;
	private int _compLost;
	private int _compDrawn;
	private boolean _isRewarded;
	
	public OlympiadNoble(int classId, String name, int points, int compDone, int compWon, int compLost, int compDrawn, boolean isRewarded)
	{
		_classId = classId;
		_name = name;
		_points = points;
		_compDone = compDone;
		_compWon = compWon;
		_compLost = compLost;
		_compDrawn = compDrawn;
		_isRewarded = isRewarded;
	}
	
	public OlympiadNoble(Player player)
	{
		this(player.getBaseClass(), player.getName(), Config.OLY_START_POINTS, 0, 0, 0, 0, false);
	}
	
	public int getClassId()
	{
		return _classId;
	}
	
	public void setClassId(int classId)
	{
		_classId = classId;
	}
	
	public String getName()
	{
		return _name;
	}
	
	public void setName(String name)
	{
		_name = name;
	}
	
	public int getPoints()
	{
		return _points;
	}
	
	public void setPoints(int points)
	{
		_points = points;
	}
	
	public void updatePoints(int amount)
	{
		_points = Math.max(0, _points + amount);
	}
	
	public int getCompDone()
	{
		return _compDone;
	}
	
	public void setCompDone(int compDone)
	{
		_compDone = compDone;
	}
	
	public void updateCompDone(int amount)
	{
		_compDone += amount;
	}
	
	public int getCompWon()
	{
		return _compWon;
	}
	
	public void setCompWon(int compWon)
	{
		_compWon = compWon;
	}
	
	public void updateCompWon(int amount)
	{
		_compWon += amount;
	}
	
	public int getCompLost()
	{
		return _compLost;
	}
	
	public void setCompLost(int compLost)
	{
		_compLost = compLost;
	}
	
	public void updateCompLost(int amount)
	{
		_compLost += amount;
	}
	
	public int getCompDrawn()
	{
		return _compDrawn;
	}
	
	public void setCompDrawn(int compDrawn)
	{
		_compDrawn = compDrawn;
	}
	
	public void updateCompDrawn(int amount)
	{
		_compDrawn += amount;
	}
	
	public boolean isRewarded()
	{
		return _isRewarded;
	}
	
	public void setRewarded(boolean isRewarded)
	{
		_isRewarded = isRewarded;
	}
}