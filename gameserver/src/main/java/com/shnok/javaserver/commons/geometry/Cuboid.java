package com.shnok.javaserver.commons.geometry;

import java.awt.Color;

import com.shnok.javaserver.commons.random.Rnd;

import com.shnok.javaserver.gameserver.model.location.Location;
import com.shnok.javaserver.gameserver.network.serverpackets.ExServerPrimitive;

public class Cuboid extends Rectangle
{
	// min and max Z coorinates
	private final int _minZ;
	private final int _maxZ;
	
	/**
	 * Cuboid constructor.
	 * @param x : Bottom left lower X coordinate.
	 * @param y : Bottom left lower Y coordinate.
	 * @param minZ : Minimum Z coordinate.
	 * @param maxZ : Maximum Z coordinate.
	 * @param w : Cuboid width.
	 * @param h : Cuboid height.
	 */
	public Cuboid(int x, int y, int minZ, int maxZ, int w, int h)
	{
		super(x, y, w, h);
		
		_minZ = minZ;
		_maxZ = maxZ;
	}
	
	@Override
	public final double getArea()
	{
		return 2 * (_w * _h + (_w + _h) * (_maxZ - _minZ));
	}
	
	@Override
	public final double getVolume()
	{
		return _w * _h * (_maxZ - _minZ);
	}
	
	@Override
	public boolean isInside(int x, int y, int z)
	{
		if (z < _minZ || z > _maxZ)
			return false;
		
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
		return new Location(_x + Rnd.get(_w), _y + Rnd.get(_h), Rnd.get(_minZ, _maxZ));
	}
	
	@Override
	public void visualize(String info, ExServerPrimitive debug, int z)
	{
		final int x2 = _x + _w;
		final int y2 = _y + _h;
		final int z1 = _minZ - 32;
		final int z2 = _maxZ - 32;
		
		debug.addLine(info + " MinZ", Color.GREEN, true, _x, _y, z1, _x, y2, z1);
		debug.addLine(info, Color.YELLOW, true, _x, _y, z, _x, y2, z);
		debug.addLine(info + " MaxZ", Color.RED, true, _x, _y, z2, _x, y2, z2);
		
		debug.addLine(info + " MinZ", Color.GREEN, true, _x, y2, z1, x2, y2, z1);
		debug.addLine(info, Color.YELLOW, true, _x, y2, z, x2, y2, z);
		debug.addLine(info + " MaxZ", Color.RED, true, _x, y2, z2, x2, y2, z2);
		
		debug.addLine(info + " MinZ", Color.GREEN, true, x2, y2, z1, x2, _y, z1);
		debug.addLine(info, Color.YELLOW, true, x2, y2, z, x2, _y, z);
		debug.addLine(info + " MaxZ", Color.RED, true, x2, y2, z2, x2, _y, z2);
		
		debug.addLine(info + " MinZ", Color.GREEN, true, x2, _y, z1, _x, _y, z1);
		debug.addLine(info, Color.YELLOW, true, x2, _y, z, _x, _y, z);
		debug.addLine(info + " MaxZ", Color.RED, true, x2, _y, z2, _x, _y, z2);
		
		debug.addLine(info, Color.YELLOW, true, _x, _y, z1, _x, _y, z2);
		debug.addLine(info, Color.YELLOW, true, x2, _y, z1, x2, _y, z2);
		debug.addLine(info, Color.YELLOW, true, x2, y2, z1, x2, y2, z2);
		debug.addLine(info, Color.YELLOW, true, _x, y2, z1, _x, y2, z2);
	}
}