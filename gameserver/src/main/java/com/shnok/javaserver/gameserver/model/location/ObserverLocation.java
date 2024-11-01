package com.shnok.javaserver.gameserver.model.location;

import com.shnok.javaserver.commons.data.StatSet;

public class ObserverLocation extends Location
{
	private final int _locId;
	
	private final int _yaw;
	private final int _pitch;
	private final int _cost;
	
	private final int _castleId;
	
	public ObserverLocation(int locId, int x, int y, int z, int yaw, int pitch, int cost, int castleId)
	{
		super(x, y, z);
		
		_locId = locId;
		
		_yaw = yaw;
		_pitch = pitch;
		_cost = cost;
		_castleId = castleId;
	}
	
	public ObserverLocation(int locId, Location loc, int yaw, int pitch, int cost, int castleId)
	{
		this(locId, loc.getX(), loc.getY(), loc.getZ(), yaw, pitch, cost, castleId);
	}
	
	public ObserverLocation(StatSet set)
	{
		this(set.getInteger("locId"), set.getInteger("x"), set.getInteger("y"), set.getInteger("z"), set.getInteger("yaw"), set.getInteger("pitch"), set.getInteger("cost"), set.getInteger("castle"));
	}
	
	public int getLocId()
	{
		return _locId;
	}
	
	public int getYaw()
	{
		return _yaw;
	}
	
	public int getPitch()
	{
		return _pitch;
	}
	
	public int getCost()
	{
		return _cost;
	}
	
	public int getCastleId()
	{
		return _castleId;
	}
}