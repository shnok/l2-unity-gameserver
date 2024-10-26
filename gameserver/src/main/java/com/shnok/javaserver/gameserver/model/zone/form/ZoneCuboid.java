package com.shnok.javaserver.gameserver.model.zone.form;

import java.awt.Color;

import com.shnok.javaserver.gameserver.model.zone.ZoneForm;
import com.shnok.javaserver.gameserver.network.serverpackets.ExServerPrimitive;

public class ZoneCuboid extends ZoneForm
{
	private int _x1;
	private int _x2;
	private int _y1;
	private int _y2;
	private int _z1;
	private int _z2;
	
	public ZoneCuboid(int x1, int x2, int y1, int y2, int z1, int z2)
	{
		_x1 = x1;
		_x2 = x2;
		
		// switch them if alignment is wrong
		if (_x1 > _x2)
		{
			_x1 = x2;
			_x2 = x1;
		}
		
		_y1 = y1;
		_y2 = y2;
		
		// switch them if alignment is wrong
		if (_y1 > _y2)
		{
			_y1 = y2;
			_y2 = y1;
		}
		
		_z1 = z1;
		_z2 = z2;
		
		// switch them if alignment is wrong
		if (_z1 > _z2)
		{
			_z1 = z2;
			_z2 = z1;
		}
	}
	
	@Override
	public boolean isInsideZone(int x, int y, int z)
	{
		if (x < _x1 || x > _x2 || y < _y1 || y > _y2 || z < _z1 || z > _z2)
			return false;
		
		return true;
	}
	
	@Override
	public boolean intersectsRectangle(int ax1, int ax2, int ay1, int ay2)
	{
		// Check if any point inside this rectangle
		if (isInsideZone(ax1, ay1, (_z2 - 1)))
			return true;
		
		if (isInsideZone(ax1, ay2, (_z2 - 1)))
			return true;
		
		if (isInsideZone(ax2, ay1, (_z2 - 1)))
			return true;
		
		if (isInsideZone(ax2, ay2, (_z2 - 1)))
			return true;
		
		// Check if any point from this rectangle is inside the other one
		if (_x1 > ax1 && _x1 < ax2 && _y1 > ay1 && _y1 < ay2)
			return true;
		
		if (_x1 > ax1 && _x1 < ax2 && _y2 > ay1 && _y2 < ay2)
			return true;
		
		if (_x2 > ax1 && _x2 < ax2 && _y1 > ay1 && _y1 < ay2)
			return true;
		
		if (_x2 > ax1 && _x2 < ax2 && _y2 > ay1 && _y2 < ay2)
			return true;
		
		// Horizontal lines may intersect vertical lines
		if (lineSegmentsIntersect(_x1, _y1, _x2, _y1, ax1, ay1, ax1, ay2))
			return true;
		
		if (lineSegmentsIntersect(_x1, _y1, _x2, _y1, ax2, ay1, ax2, ay2))
			return true;
		
		if (lineSegmentsIntersect(_x1, _y2, _x2, _y2, ax1, ay1, ax1, ay2))
			return true;
		
		if (lineSegmentsIntersect(_x1, _y2, _x2, _y2, ax2, ay1, ax2, ay2))
			return true;
		
		// Vertical lines may intersect horizontal lines
		if (lineSegmentsIntersect(_x1, _y1, _x1, _y2, ax1, ay1, ax2, ay1))
			return true;
		
		if (lineSegmentsIntersect(_x1, _y1, _x1, _y2, ax1, ay2, ax2, ay2))
			return true;
		
		if (lineSegmentsIntersect(_x2, _y1, _x2, _y2, ax1, ay1, ax2, ay1))
			return true;
		
		if (lineSegmentsIntersect(_x2, _y1, _x2, _y2, ax1, ay2, ax2, ay2))
			return true;
		
		return false;
	}
	
	@Override
	public int getLowZ()
	{
		return _z1;
	}
	
	@Override
	public int getHighZ()
	{
		return _z2;
	}
	
	@Override
	public void visualizeZone(String info, ExServerPrimitive debug)
	{
		final int z1 = _z1 - 32;
		final int z2 = _z2 - 32;
		
		// Floors - min/max.
		debug.addRectangle(info, Color.GREEN, true, _x1, _y1, _x1, _y2, z1);
		debug.addRectangle(info, Color.RED, true, _x1, _y1, _x1, _y2, z2);
		
		// Vertical lines.
		debug.addLine("", Color.YELLOW, true, _x1, _y1, z1, _x1, _y1, z2);
		debug.addLine("", Color.YELLOW, true, _x1, _y2, z1, _x1, _y2, z2);
		debug.addLine("", Color.YELLOW, true, _x2, _y1, z1, _x2, _y1, z2);
		debug.addLine("", Color.YELLOW, true, _x2, _y2, z1, _x2, _y2, z2);
	}
}