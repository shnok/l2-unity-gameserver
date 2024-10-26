package com.shnok.javaserver.commons.geometry.basic;

import com.shnok.javaserver.gameserver.model.location.Point2D;

public class Line2D
{
	private final int _p1x;
	private final int _p1y;
	
	private final int _p2x;
	private final int _p2y;
	
	public Line2D(int p1x, int p1y, int p2x, int p2y)
	{
		_p1x = p1x;
		_p1y = p1y;
		
		_p2x = p2x;
		_p2y = p2y;
	}
	
	@Override
	public Line2D clone()
	{
		return new Line2D(_p1x, _p1y, _p2x, _p2y);
	}
	
	public int getP1x()
	{
		return _p1x;
	}
	
	public int getP1y()
	{
		return _p1y;
	}
	
	public int getP2x()
	{
		return _p2x;
	}
	
	public int getP2y()
	{
		return _p2y;
	}
	
	public boolean intersectsLine(Line2D other)
	{
		final double dx1 = _p2x - _p1x;
		final double dy1 = _p2y - _p1y;
		final double dx2 = other._p2x - other._p1x;
		final double dy2 = other._p2y - other._p1y;
		
		final double delta = dx1 * dy2 - dy1 * dx2;
		
		// Lines are parallel or colinear.
		if (delta == 0)
			return false;
		
		final double t = ((_p1y - other._p1y) * dx2 - (_p1x - other._p1x) * dy2) / delta;
		final double u = ((_p1y - other._p1y) * dx1 - (_p1x - other._p1x) * dy1) / delta;
		
		// Check if point is within both line segments.
		return t >= 0 && t <= 1 && u >= 0 && u <= 1;
	}
	
	public Point2D getIntersectionPoint(Line2D line)
	{
		return getIntersectionPoint(line._p1x, line._p1y, line._p2x, line._p2y);
	}
	
	public Point2D getIntersectionPoint(Point2D start, Point2D end)
	{
		return getIntersectionPoint(start.getX(), start.getY(), end.getX(), end.getY());
	}
	
	public Point2D getAdjustedIntersectionPoint(Point2D start, Point2D end, double ratio)
	{
		final Point2D intersection = getIntersectionPoint(start, end);
		
		if (intersection == null)
			return null;
		
		final int x = (int) Math.round(start.getX() + ratio * (intersection.getX() - start.getX()));
		final int y = (int) Math.round(start.getY() + ratio * (intersection.getY() - start.getY()));
		
		return new Point2D(x, y);
	}
	
	public Point2D getIntersectionPoint(int x3, int y3, int x4, int y4)
	{
		final double dx1 = _p2x - _p1x;
		final double dy1 = _p2y - _p1y;
		final double dx2 = x4 - x3;
		final double dy2 = y4 - y3;
		
		final double delta = dx1 * dy2 - dy1 * dx2;
		
		// Lines are parallel or colinear.
		if (delta == 0)
			return null;
		
		final double t = ((_p1y - y3) * dx2 - (_p1x - x3) * dy2) / delta;
		final double u = ((_p1y - y3) * dx1 - (_p1x - x3) * dy1) / delta;
		
		// Lines don't cross anywhere.
		if (t < 0 || t > 1 || u < 0 || u > 1)
			return null;
		
		final int x = (int) (_p1x + t * dx1);
		final int y = (int) (_p1y + t * dy1);
		
		return new Point2D(x, y);
	}
	
	public double distance(int x2, int y2)
	{
		return Math.sqrt(Math.pow(x2 - _p1x, 2) + Math.pow(y2 - _p1y, 2));
	}
	
	public double getY()
	{
		return _p2y - _p1y;
	}
	
	public double getX()
	{
		return _p2x - _p1x;
	}
	
	public int getCenterX()
	{
		return (_p1x + _p2x) / 2;
	}
	
	public int getCenterY()
	{
		return (_p1y + _p2y) / 2;
	}
	
	public Point2D getPoint()
	{
		return new Point2D((int) getX(), (int) getY());
	}
}