package com.shnok.javaserver.commons.geometry;

import java.awt.Color;

import com.shnok.javaserver.commons.random.Rnd;

import com.shnok.javaserver.gameserver.model.location.Location;
import com.shnok.javaserver.gameserver.model.location.Point2D;
import com.shnok.javaserver.gameserver.network.serverpackets.ExServerPrimitive;

public class Triangle extends AShape
{
	// A point
	protected final int _Ax;
	protected final int _Ay;
	
	// BA vector coordinates
	protected final int _BAx;
	protected final int _BAy;
	
	// CA vector coordinates
	protected final int _CAx;
	protected final int _CAy;
	
	// size
	protected final long _size;
	
	/**
	 * Triangle constructor.
	 * @param A : Point A of the triangle.
	 * @param B : Point B of the triangle.
	 * @param C : Point C of the triangle.
	 */
	public Triangle(Point2D A, Point2D B, Point2D C)
	{
		_Ax = A.getX();
		_Ay = A.getY();
		
		_BAx = B.getX() - A.getX();
		_BAy = B.getY() - A.getY();
		
		_CAx = C.getX() - A.getX();
		_CAy = C.getY() - A.getY();
		
		_size = Math.abs((long) (_BAx * _CAy - _CAx * _BAy)) / 2;
		
		_center = new Point2D((A.getX() + B.getX() + C.getX()) / 3, (A.getY() + B.getY() + C.getY()) / 3);
	}
	
	@Override
	public final long getSize()
	{
		return _size;
	}
	
	@Override
	public double getArea()
	{
		return _size;
	}
	
	@Override
	public double getVolume()
	{
		return 0;
	}
	
	@Override
	public final boolean isInside(int x, int y)
	{
		// method parameters must be LONG, since whole calculations must be done in LONG...we are doing really big numbers
		final long dx = x - _Ax;
		final long dy = y - _Ay;
		
		final boolean a = (0 - dx) * (_BAy - 0) - (_BAx - 0) * (0 - dy) >= 0;
		final boolean b = (_BAx - dx) * (_CAy - _BAy) - (_CAx - _BAx) * (_BAy - dy) >= 0;
		final boolean c = (_CAx - dx) * (0 - _CAy) - (0 - _CAx) * (_CAy - dy) >= 0;
		
		return a == b && b == c;
	}
	
	@Override
	public boolean isInside(int x, int y, int z)
	{
		// method parameters must be LONG, since whole calculations must be done in LONG...we are doing really big numbers
		final long dx = x - _Ax;
		final long dy = y - _Ay;
		
		final boolean a = (0 - dx) * (_BAy - 0) - (_BAx - 0) * (0 - dy) >= 0;
		final boolean b = (_BAx - dx) * (_CAy - _BAy) - (_CAx - _BAx) * (_BAy - dy) >= 0;
		final boolean c = (_CAx - dx) * (0 - _CAy) - (0 - _CAx) * (_CAy - dy) >= 0;
		
		return a == b && b == c;
	}
	
	@Override
	public Location getRandomLocation()
	{
		// get relative length of AB and AC vectors
		double ba = Rnd.nextDouble();
		double ca = Rnd.nextDouble();
		
		// adjust length if too long
		if (ba + ca > 1)
		{
			ba = 1 - ba;
			ca = 1 - ca;
		}
		
		// calculate coordinates (take A, add AB and AC)
		final int x = _Ax + (int) (ba * _BAx + ca * _CAx);
		final int y = _Ay + (int) (ba * _BAy + ca * _CAy);
		
		// return
		return new Location(x, y, 0);
	}
	
	@Override
	public void visualize(String info, ExServerPrimitive debug, int z)
	{
		final int x2 = _Ax + _BAx;
		final int y2 = _Ay + _BAy;
		final int x3 = _Ax + _CAx;
		final int y3 = _Ay + _CAy;
		
		debug.addLine(info, Color.YELLOW, true, _Ax, _Ay, z, x2, y2, z);
		debug.addLine(info, Color.YELLOW, true, _Ax, _Ay, z, x3, y3, z);
		debug.addLine(info, Color.YELLOW, true, x2, y2, z, x3, y3, z);
	}
}