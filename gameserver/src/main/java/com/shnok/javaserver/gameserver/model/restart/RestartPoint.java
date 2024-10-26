package com.shnok.javaserver.gameserver.model.restart;

import java.awt.Color;
import java.util.List;

import net.sf.l2j.commons.data.StatSet;
import net.sf.l2j.commons.random.Rnd;

import net.sf.l2j.gameserver.enums.actors.ClassRace;
import net.sf.l2j.gameserver.model.holder.IntIntHolder;
import net.sf.l2j.gameserver.model.location.Location;
import net.sf.l2j.gameserver.network.serverpackets.ExServerPrimitive;

/**
 * A zone used as restart point when dead or scrolling out.<br>
 * <br>
 * It uses region-scale coordinates (eg. 22_11) in order to define which area you are set on.
 */
public class RestartPoint
{
	private final String _name;
	
	private final List<Location> _points;
	private final List<Location> _chaoPoints;
	private final List<IntIntHolder> _mapRegions;
	
	private final int _bbs;
	private final int _locName;
	
	private ClassRace _bannedRace;
	private String _bannedPoint;
	
	public RestartPoint(StatSet set)
	{
		_name = set.getString("name");
		
		_points = set.getList("points");
		_chaoPoints = set.getList("chaoPoints");
		_mapRegions = set.getList("mapRegions");
		
		_bbs = set.getInteger("bbs");
		_locName = set.getInteger("locName");
		
		if (set.containsKey("bannedRace"))
		{
			final String[] parsedBanned = set.getString("bannedRace").split(";");
			
			_bannedRace = Enum.valueOf(ClassRace.class, parsedBanned[0]);
			_bannedPoint = parsedBanned[1];
		}
	}
	
	public String getName()
	{
		return _name;
	}
	
	public Location getRandomPoint()
	{
		return Rnd.get(_points);
	}
	
	public Location getRandomChaoPoint()
	{
		return Rnd.get(_chaoPoints);
	}
	
	public List<IntIntHolder> getMapRegions()
	{
		return _mapRegions;
	}
	
	public int getBbs()
	{
		return _bbs;
	}
	
	public int getLocName()
	{
		return _locName;
	}
	
	public ClassRace getBannedRace()
	{
		return _bannedRace;
	}
	
	public String getBannedPoint()
	{
		return _bannedPoint;
	}
	
	public void visualizeZone(ExServerPrimitive debug)
	{
		for (Location point : _points)
			debug.addPoint("point", Color.GREEN, true, point);
		
		for (Location chaoPoint : _chaoPoints)
			debug.addPoint("chaoPoint", Color.RED, true, chaoPoint);
	}
}