package com.shnok.javaserver.commons.geometry;

import java.awt.Color;

import com.shnok.javaserver.commons.random.Rnd;

import com.shnok.javaserver.gameserver.model.location.Location;
import com.shnok.javaserver.gameserver.network.serverpackets.ExServerPrimitive;

public class Cube extends Square
{
	// cube origin coordinates
	private final int _z;
	
	/**
	 * Cube constructor.
	 * @param x : Bottom left lower X coordinate.
	 * @param y : Bottom left lower Y coordinate.
	 * @param z : Bottom left lower Z coordinate.
	 * @param a : Size of cube side.
	 */
	public Cube(int x, int y, int z, int a)
	{
		super(x, y, a);
		
		_z = z;
	}
	
	@Override
	public double getArea()
	{
		return 6 * _a * _a;
	}
	
	@Override
	public double getVolume()
	{
		return _a * _a * _a;
	}
	
	@Override
	public boolean isInside(int x, int y, int z)
	{
		int d = z - _z;
		if (d < 0 || d > _a)
			return false;
		
		d = x - _x;
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
		return new Location(_x + Rnd.get(_a), _y + Rnd.get(_a), _z + Rnd.get(_a));
	}
	
	@Override
	public void visualize(String info, ExServerPrimitive debug, int z)
	{
		final int x2 = _x + _a;
		final int y2 = _y + _a;
		z -= 32;
		final int z1 = _z - 32;
		final int z2 = _z + _a - 32;
		
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