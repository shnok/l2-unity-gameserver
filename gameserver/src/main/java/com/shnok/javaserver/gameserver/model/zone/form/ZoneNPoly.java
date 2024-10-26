package com.shnok.javaserver.gameserver.model.zone.form;

import java.awt.Color;

import com.shnok.javaserver.gameserver.model.zone.ZoneForm;
import com.shnok.javaserver.gameserver.network.serverpackets.ExServerPrimitive;

public class ZoneNPoly extends ZoneForm
{
	private final int[] _x;
	private final int[] _y;
	private final int _z1;
	private final int _z2;
	
	public ZoneNPoly(int[] x, int[] y, int z1, int z2)
	{
		_x = x;
		_y = y;
		_z1 = z1;
		_z2 = z2;
	}
	
	@Override
	public boolean isInsideZone(int x, int y, int z)
	{
		if (z < _z1 || z > _z2)
			return false;
		
		boolean inside = false;
		for (int i = 0, j = _x.length - 1; i < _x.length; j = i++)
		{
			if ((((_y[i] <= y) && (y < _y[j])) || ((_y[j] <= y) && (y < _y[i]))) && (x < (_x[j] - _x[i]) * (y - _y[i]) / (_y[j] - _y[i]) + _x[i]))
				inside = !inside;
		}
		return inside;
	}
	
	@Override
	public boolean intersectsRectangle(int ax1, int ax2, int ay1, int ay2)
	{
		int tX;
		int tY;
		int uX;
		int uY;
		
		// First check if a point of the polygon lies inside the rectangle
		if (_x[0] > ax1 && _x[0] < ax2 && _y[0] > ay1 && _y[0] < ay2)
			return true;
		
		// Or a point of the rectangle inside the polygon
		if (isInsideZone(ax1, ay1, (_z2 - 1)))
			return true;
		
		// Check every possible line of the polygon for a collision with any of the rectangles side
		for (int i = 0; i < _y.length; i++)
		{
			tX = _x[i];
			tY = _y[i];
			uX = _x[(i + 1) % _x.length];
			uY = _y[(i + 1) % _x.length];
			
			// Check if this line intersects any of the four sites of the rectangle
			if (lineSegmentsIntersect(tX, tY, uX, uY, ax1, ay1, ax1, ay2))
				return true;
			
			if (lineSegmentsIntersect(tX, tY, uX, uY, ax1, ay1, ax2, ay1))
				return true;
			
			if (lineSegmentsIntersect(tX, tY, uX, uY, ax2, ay2, ax1, ay2))
				return true;
			
			if (lineSegmentsIntersect(tX, tY, uX, uY, ax2, ay2, ax2, ay1))
				return true;
		}
		
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
		
		for (int i = 0; i < _x.length; i++)
		{
			int nextIndex = i + 1;
			
			// Ending point to first one.
			if (nextIndex == _x.length)
				nextIndex = 0;
			
			// Floors - min/max.
			debug.addLine(info, Color.GREEN, true, _x[i], _y[i], z1, _x[nextIndex], _y[nextIndex], z1);
			debug.addLine(info, Color.RED, true, _x[i], _y[i], z2, _x[nextIndex], _y[nextIndex], z2);
			
			// Vertical line.
			debug.addLine("", Color.YELLOW, true, _x[i], _y[i], z1, _x[i], _y[i], z2);
		}
	}
}