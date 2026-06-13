package com.shnok.javaserver.commons.geometry;

import java.awt.Color;

import com.shnok.javaserver.commons.random.Rnd;

import com.shnok.javaserver.gameserver.model.location.Location;
import com.shnok.javaserver.gameserver.model.location.Point2D;
import com.shnok.javaserver.gameserver.network.serverpackets.unused.ExServerPrimitive;

public class Square extends AShape
{
	// square origin coordinates
	protected final int _x;
	protected final int _y;
	
	// square side
	protected final int _a;
	
	/**
	 * Square constructor.
	 * @param x : Bottom left X coordinate.
	 * @param y : Bottom left Y coordinate.
	 * @param a : Size of square side.
	 */
	public Square(int x, int y, int a)
	{
		_x = x;
		_y = y;
		
		_a = a;
		
		_center = new Point2D(x + (a / 2), y + (a / 2));
	}
	
	@Override
	public final long getSize()
	{
		return (long) _a * _a;
	}
	
	@Override
	public double getArea()
	{
		return _a * _a;
	}
	
	@Override
	public double getVolume()
	{
		return 0;
	}
	
	@Override
	public boolean isInside(int x, int y)
	{
		int d = x - _x;
		if (d < 0 || d > _a)
			return false;
		
		d = y - _y;
		if (d < 0 || d > _a)
			return false;
		
		return true;
	}
	
	@Override
	public boolean isInside(int x, int y, int z)
	{
		int d = x - _x;
		if (d < 0 || d > _a)
			return false;
		
		d = y - _y;
		if (d < 0 || d > _a)
			return false;
		
		return true;
	}
	
	@Override
	public Location getRandomLocation()
	{
		// calculate coordinates and return
		return new Location(_x + Rnd.get(_a), _y + Rnd.get(_a), 0);
	}
	
	@Override
	public void visualize(String info, ExServerPrimitive debug, int z)
	{
		final int x2 = _x + _a;
		final int y2 = _y + _a;
		z -= 32;
		
		debug.addLine(info, Color.YELLOW, true, _x, _y, z, _x, y2, z);
		debug.addLine(info, Color.YELLOW, true, _x, y2, z, x2, y2, z);
		debug.addLine(info, Color.YELLOW, true, x2, y2, z, x2, _y, z);
		debug.addLine(info, Color.YELLOW, true, x2, _y, z, _x, _y, z);
	}
}