package com.shnok.javaserver.commons.geometry;

import java.awt.Color;

import com.shnok.javaserver.commons.random.Rnd;

import com.shnok.javaserver.gameserver.model.location.Location;
import com.shnok.javaserver.gameserver.network.serverpackets.unused.ExServerPrimitive;

public class Sphere extends Circle
{
	// sphere center Z coordinate
	private final int _z;
	
	/**
	 * Sphere constructor.
	 * @param x : Center X coordinate.
	 * @param y : Center Y coordinate.
	 * @param z : Center Z coordinate.
	 * @param r : Sphere radius.
	 */
	public Sphere(int x, int y, int z, int r)
	{
		super(x, y, r);
		
		_z = z;
	}
	
	@Override
	public final double getArea()
	{
		return 4 * Math.PI * _r * _r;
	}
	
	@Override
	public final double getVolume()
	{
		return (4 * Math.PI * _r * _r * _r) / 3;
	}
	
	@Override
	public final boolean isInside(int x, int y, int z)
	{
		final int dx = x - _x;
		final int dy = y - _y;
		final int dz = z - _z;
		
		return (dx * dx + dy * dy + dz * dz) <= _r * _r;
	}
	
	@Override
	public final Location getRandomLocation()
	{
		// get uniform distance and angles
		final double r = Math.cbrt(Rnd.nextDouble()) * _r;
		final double phi = Rnd.nextDouble() * 2 * Math.PI;
		final double theta = Math.acos(2 * Rnd.nextDouble() - 1);
		
		// calculate coordinates
		final int x = (int) (_x + (r * Math.cos(phi) * Math.sin(theta)));
		final int y = (int) (_y + (r * Math.sin(phi) * Math.sin(theta)));
		final int z = (int) (_z + (r * Math.cos(theta)));
		
		// return
		return new Location(x, y, z);
	}
	
	@Override
	public void visualize(String info, ExServerPrimitive debug, int z)
	{
		final int count = (int) (2 * Math.PI * _r / STEP);
		final double angle = 2 * Math.PI / count;
		z = _z - 32;
		
		int dXYZ = _r;
		int dYZX = 0;
		
		for (int i = 1; i <= count; i++)
		{
			int nextXYZ = (int) (Math.cos(angle * i) * _r);
			int nextYZX = (int) (Math.sin(angle * i) * _r);
			
			// draw line in X-plane
			debug.addLine(info, Color.YELLOW, true, _x + dXYZ, _y + dYZX, z, _x + nextXYZ, _y + nextYZX, z);
			
			// draw line in Y-plane
			debug.addLine(info, Color.YELLOW, true, _x + dXYZ, _y, z + dYZX, _x + nextXYZ, _y, z + nextYZX);
			
			// draw line in Z-plane
			debug.addLine(info, Color.YELLOW, true, _x, _y + dXYZ, z + dYZX, _x, _y + nextXYZ, z + nextYZX);
			
			dXYZ = nextXYZ;
			dYZX = nextYZX;
		}
	}
}