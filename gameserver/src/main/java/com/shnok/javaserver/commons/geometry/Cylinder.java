package com.shnok.javaserver.commons.geometry;

import java.awt.Color;

import com.shnok.javaserver.commons.random.Rnd;

import com.shnok.javaserver.gameserver.model.location.Location;
import com.shnok.javaserver.gameserver.network.serverpackets.unused.ExServerPrimitive;

public class Cylinder extends Circle
{
	// min and max Z coorinates
	private final int _minZ;
	private final int _maxZ;
	
	/**
	 * Cylinder constructor
	 * @param x : Center X coordinate.
	 * @param y : Center X coordinate.
	 * @param r : Cylinder radius.
	 * @param minZ : Minimum Z coordinate.
	 * @param maxZ : Maximum Z coordinate.
	 */
	public Cylinder(int x, int y, int r, int minZ, int maxZ)
	{
		super(x, y, r);
		
		_minZ = minZ;
		_maxZ = maxZ;
	}
	
	@Override
	public final double getArea()
	{
		return 2 * Math.PI * _r * (_r + _maxZ - _minZ);
	}
	
	@Override
	public final double getVolume()
	{
		return Math.PI * _r * _r * (_maxZ - _minZ);
	}
	
	@Override
	public final boolean isInside(int x, int y, int z)
	{
		if (z < _minZ || z > _maxZ)
			return false;
		
		final int dx = x - _x;
		final int dy = y - _y;
		
		return (dx * dx + dy * dy) <= _r * _r;
	}
	
	@Override
	public final Location getRandomLocation()
	{
		// get uniform distance and angle
		final double distance = Math.sqrt(Rnd.nextDouble()) * _r;
		final double angle = Rnd.nextDouble() * Math.PI * 2;
		
		// calculate coordinates and return
		return new Location((int) (distance * Math.cos(angle)), (int) (distance * Math.sin(angle)), Rnd.get(_minZ, _maxZ));
	}
	
	@Override
	public void visualize(String info, ExServerPrimitive debug, int z)
	{
		final int count = (int) (2 * Math.PI * _r / STEP);
		final double angle = 2 * Math.PI / count;
		final int z1 = _minZ - 32;
		final int z2 = _maxZ - 32;
		z -= 32;
		
		int dX = _r;
		int dY = 0;
		
		for (int i = 1; i <= count; i++)
		{
			int nextX = (int) (Math.cos(angle * i) * _r);
			int nextY = (int) (Math.sin(angle * i) * _r);
			
			debug.addLine(info + " MinZ", Color.GREEN, true, _x + dX, _y + dY, z1, _x + nextX, _y + nextY, z1);
			debug.addLine(info, Color.YELLOW, true, _x + dX, _y + dY, z, _x + nextX, _y + nextY, z);
			debug.addLine(info + " MaxZ", Color.RED, true, _x + dX, _y + dY, z2, _x + nextX, _y + nextY, z2);
			
			dX = nextX;
			dY = nextY;
		}
		
		debug.addLine(info, Color.YELLOW, true, _x, _y + _r, z1, _x, _y + _r, z2);
		debug.addLine(info, Color.YELLOW, true, _x, _y - _r, z1, _x, _y - _r, z2);
		debug.addLine(info, Color.YELLOW, true, _x + _r, _y, z1, _x + _r, _y, z2);
		debug.addLine(info, Color.YELLOW, true, _x - _r, _y, z1, _x - _r, _y, z2);
	}
}