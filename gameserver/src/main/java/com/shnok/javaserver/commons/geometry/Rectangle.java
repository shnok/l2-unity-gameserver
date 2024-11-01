package com.shnok.javaserver.commons.geometry;

import java.awt.Color;

import com.shnok.javaserver.commons.random.Rnd;

import com.shnok.javaserver.gameserver.model.location.Location;
import com.shnok.javaserver.gameserver.model.location.Point2D;
import com.shnok.javaserver.gameserver.network.serverpackets.unused.ExServerPrimitive;

public class Rectangle extends AShape
{
	// rectangle origin coordinates
	protected final int _x;
	protected final int _y;
	
	// rectangle width and height
	protected final int _w;
	protected final int _h;
	
	/**
	 * Rectangle constructor.
	 * @param x : Bottom left X coordinate.
	 * @param y : Bottom left Y coordinate.
	 * @param w : Rectangle width.
	 * @param h : Rectangle height.
	 */
	public Rectangle(int x, int y, int w, int h)
	{
		_x = x;
		_y = y;
		
		_w = w;
		_h = h;
		
		_center = new Point2D(x + (w / 2), y + (h / 2));
	}
	
	@Override
	public final long getSize()
	{
		return (long) _w * _h;
	}
	
	@Override
	public double getArea()
	{
		return _w * _h;
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
		if (d < 0 || d > _w)
			return false;
		
		d = y - _y;
		if (d < 0 || d > _h)
			return false;
		
		return true;
	}
	
	@Override
	public boolean isInside(int x, int y, int z)
	{
		int d = x - _x;
		if (d < 0 || d > _w)
			return false;
		
		d = y - _y;
		if (d < 0 || d > _h)
			return false;
		
		return true;
	}
	
	@Override
	public Location getRandomLocation()
	{
		// calculate coordinates and return
		return new Location(_x + Rnd.get(_w), _y + Rnd.get(_h), 0);
	}
	
	@Override
	public void visualize(String info, ExServerPrimitive debug, int z)
	{
		final int x2 = _x + _w;
		final int y2 = _y + _h;
		z -= 32;
		
		debug.addLine(info, Color.YELLOW, true, _x, _y, z, _x, y2, z);
		debug.addLine(info, Color.YELLOW, true, _x, y2, z, x2, y2, z);
		debug.addLine(info, Color.YELLOW, true, x2, y2, z, x2, _y, z);
		debug.addLine(info, Color.YELLOW, true, x2, _y, z, _x, _y, z);
	}
}