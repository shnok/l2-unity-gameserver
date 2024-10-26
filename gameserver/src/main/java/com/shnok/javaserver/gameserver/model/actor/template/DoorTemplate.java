package com.shnok.javaserver.gameserver.model.actor.template;

import java.awt.Color;

import com.shnok.javaserver.commons.data.StatSet;

import com.shnok.javaserver.gameserver.enums.DoorType;
import com.shnok.javaserver.gameserver.enums.OpenType;
import com.shnok.javaserver.gameserver.model.location.Point2D;
import com.shnok.javaserver.gameserver.network.serverpackets.ExServerPrimitive;

public class DoorTemplate extends CreatureTemplate
{
	private final Point2D[] _coords;
	private final String _name;
	private final int _id;
	private final DoorType _type;
	private final int _level;
	
	// coordinates can be part of template, since we spawn 1 instance of door at fixed position
	private final int _x;
	private final int _y;
	private final int _z;
	
	// geodata description of the door
	private final int _geoX;
	private final int _geoY;
	private final int _geoZ;
	private final byte[][] _geoData;
	
	private final int _castleId;
	private final int _clanHallId;
	private final int _triggeredId;
	private final boolean _opened;
	
	private final OpenType _openType;
	private final int _openTime;
	private final int _randomTime;
	private final int _closeTime;
	
	public DoorTemplate(StatSet set)
	{
		super(set);
		
		_name = set.getString("name");
		_id = set.getInteger("id");
		_type = set.getEnum("type", DoorType.class);
		_level = set.getInteger("level");
		
		_x = set.getInteger("posX");
		_y = set.getInteger("posY");
		_z = set.getInteger("posZ");
		
		_geoX = set.getInteger("geoX");
		_geoY = set.getInteger("geoY");
		_geoZ = set.getInteger("geoZ");
		_geoData = set.getObject("geoData", byte[][].class);
		_coords = set.getObject("coords", Point2D[].class);
		
		_castleId = set.getInteger("castle", 0);
		_clanHallId = set.getInteger("clanHall", 0);
		_triggeredId = set.getInteger("triggeredId", 0);
		_opened = set.getBool("opened", false);
		
		_openType = set.getEnum("openType", OpenType.class, OpenType.NPC);
		_openTime = set.getInteger("openTime", 0);
		_randomTime = set.getInteger("randomTime", 0);
		_closeTime = set.getInteger("closeTime", 0);
	}
	
	public final String getName()
	{
		return _name;
	}
	
	public final int getId()
	{
		return _id;
	}
	
	public final DoorType getType()
	{
		return _type;
	}
	
	public final int getLevel()
	{
		return _level;
	}
	
	public final int getPosX()
	{
		return _x;
	}
	
	public final int getPosY()
	{
		return _y;
	}
	
	public final int getPosZ()
	{
		return _z;
	}
	
	public final int getGeoX()
	{
		return _geoX;
	}
	
	public final int getGeoY()
	{
		return _geoY;
	}
	
	public final int getGeoZ()
	{
		return _geoZ;
	}
	
	public final byte[][] getGeoData()
	{
		return _geoData;
	}
	
	public final int getCastleId()
	{
		return _castleId;
	}
	
	public final int getClanHallId()
	{
		return _clanHallId;
	}
	
	public final int getTriggerId()
	{
		return _triggeredId;
	}
	
	public final boolean isOpened()
	{
		return _opened;
	}
	
	public final OpenType getOpenType()
	{
		return _openType;
	}
	
	public final int getOpenTime()
	{
		return _openTime;
	}
	
	public final int getRandomTime()
	{
		return _randomTime;
	}
	
	public final int getCloseTime()
	{
		return _closeTime;
	}
	
	public void visualizeDoor(ExServerPrimitive debug)
	{
		final int z1 = _z - 32;
		final int z2 = _z + 32;
		
		for (int i = 0; i < _coords.length; i++)
		{
			int nextIndex = i + 1;
			
			// ending point to first one
			if (nextIndex == _coords.length)
				nextIndex = 0;
			
			final Point2D curPoint = _coords[i];
			final Point2D nextPoint = _coords[nextIndex];
			
			debug.addLine(_name + " MinZ", Color.GREEN, true, curPoint.getX(), curPoint.getY(), z1, nextPoint.getX(), nextPoint.getY(), z1);
			debug.addLine(_name, Color.YELLOW, true, curPoint.getX(), curPoint.getY(), _z, nextPoint.getX(), nextPoint.getY(), _z);
			debug.addLine(_name + " MaxZ", Color.RED, true, curPoint.getX(), curPoint.getY(), z2, nextPoint.getX(), nextPoint.getY(), z2);
		}
	}
}