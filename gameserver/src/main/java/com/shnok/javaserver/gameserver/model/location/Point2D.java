package com.shnok.javaserver.gameserver.model.location;

import java.util.Objects;

import com.shnok.javaserver.commons.random.Rnd;

/**
 * A datatype used to retain a 2D (x/y) point. It got the capability to be set and cleaned.
 */
public class Point2D
{
	protected int _x;
	protected int _y;
	
	public Point2D(int x, int y)
	{
		_x = x;
		_y = y;
	}
	
	@Override
	public Point2D clone()
	{
		return new Point2D(_x, _y);
	}
	
	@Override
	public String toString()
	{
		return _x + ", " + _y;
	}
	
	@Override
	public int hashCode()
	{
		return Objects.hash(_x, _y);
	}
	
	@Override
	public boolean equals(Object obj)
	{
		if (this == obj)
			return true;
		
		if (obj == null)
			return false;
		
		if (getClass() != obj.getClass())
			return false;
		
		final Point2D other = (Point2D) obj;
		return _x == other._x && _y == other._y;
	}
	
	/**
	 * @param x : The X coord to test.
	 * @param y : The Y coord to test.
	 * @return True if all coordinates equals this {@link Point2D} coordinates.
	 */
	public boolean equals(int x, int y)
	{
		return _x == x && _y == y;
	}
	
	public int getX()
	{
		return _x;
	}
	
	public void setX(int x)
	{
		_x = x;
	}
	
	public int getY()
	{
		return _y;
	}
	
	public void setY(int y)
	{
		_y = y;
	}
	
	public void set(int x, int y)
	{
		_x = x;
		_y = y;
	}
	
	/**
	 * Refresh the current {@link Point2D} using a reference {@link Point2D} and a distance. The new destination is calculated to go in opposite side of the {@link Point2D} reference.<br>
	 * <br>
	 * This method is perfect to calculate fleeing characters position.
	 * @param referenceLoc : The Point2D used as position.
	 * @param distance : The distance to be set between current and new position.
	 */
	public void setFleeing(Point2D referenceLoc, int distance)
	{
		final double xDiff = referenceLoc.getX() - _x;
		final double yDiff = referenceLoc.getY() - _y;
		
		final double yxRation = Math.abs(xDiff / yDiff);
		
		final int y = (int) (distance / (yxRation + 1));
		final int x = (int) (y * yxRation);
		
		_x += (xDiff < 0 ? x : -x);
		_y += (yDiff < 0 ? y : -y);
	}
	
	public void clean()
	{
		_x = 0;
		_y = 0;
	}
	
	/**
	 * @param x : The X position to test.
	 * @param y : The Y position to test.
	 * @return The distance between this {@Point2D} and some given coordinates.
	 */
	public double distance2D(int x, int y)
	{
		final double dx = (double) _x - x;
		final double dy = (double) _y - y;
		
		return Math.sqrt((dx * dx) + (dy * dy));
	}
	
	/**
	 * @param point : The {@link Point2D} to test.
	 * @return The distance between this {@Point2D} and the {@link Point2D} set as parameter.
	 */
	public double distance2D(Point2D point)
	{
		return distance2D(point.getX(), point.getY());
	}
	
	/**
	 * @param x : The X position to test.
	 * @param y : The Y position to test.
	 * @param radius : The radius to check.
	 * @return True if this {@link Point2D} is in the radius of some given coordinates.
	 */
	public boolean isIn2DRadius(int x, int y, int radius)
	{
		return distance2D(x, y) < radius;
	}
	
	/**
	 * @param point : The Point2D to test.
	 * @param radius : The radius to check.
	 * @return True if this {@link Point2D} is in the radius of the {@link Point2D} set as parameter.
	 */
	public boolean isIn2DRadius(Point2D point, int radius)
	{
		return distance2D(point) < radius;
	}
	
	/**
	 * Add a strict offset on the current {@link Point2D}, leading to 8 possibilities (center non included).
	 * @param offset : The offset used to impact X and Y.
	 */
	public void addStrictOffset(int offset)
	{
		int x = 0;
		int y = 0;
		while (x == 0 && y == 0)
		{
			x = Rnd.get(-1, 1);
			y = Rnd.get(-1, 1);
		}
		
		x *= offset;
		y *= offset;
		
		_x += x;
		_y += y;
	}
	
	/**
	 * Add a random offset (can be negative as positive) to the current {@link Point2D}.
	 * @param offset : The offset used to impact X and Y.
	 */
	public void addRandomOffset(int offset)
	{
		_x += Rnd.get(-offset, offset);
		_y += Rnd.get(-offset, offset);
	}
	
	/**
	 * Add a random offset between a minimum and a maximum values to the current {@link Point2D}.
	 * @param minOffset : The minimum offset used to impact X and Y.
	 * @param maxOffset : The maximum offset used to impact X and Y.
	 */
	public void addRandomOffsetBetween(int minOffset, int maxOffset)
	{
		if (minOffset < 0 || maxOffset < 0 || maxOffset < minOffset)
			return;
		
		// Get random angle in radians.
		final double angle = Math.toRadians(Rnd.get(360));
		
		// Get random offset.
		final int offset = Rnd.get(minOffset, maxOffset);
		
		// Convert angle and distance to XY offset, then add it to coords.
		_x += (int) (offset * Math.cos(angle));
		_y += (int) (offset * Math.sin(angle));
	}
	
	/**
	 * Add a positive offset to the current {@link Point2D}.
	 * @param offset : The offset used to impact X and Y.
	 */
	public void addPositiveOffset(int offset)
	{
		_x += Rnd.get(offset);
		_y += Rnd.get(offset);
	}
	
	/**
	 * @return The Euclidean length - or magnitude of the vector.
	 */
	public double length()
	{
		return Math.sqrt(_x * _x + _y * _y);
	}
	
	/**
	 * Scale this {@link Point2D} by the given factor set as parameter.
	 * @param factor : The double factor used to scale this {@link Point2D}.
	 */
	public void scale(double factor)
	{
		_x = ((int) Math.round(_x * factor));
		_y = ((int) Math.round(_y * factor));
	}
	
	/**
	 * Rotate the point in the counter-clockwise direction around the origin by the given angle.
	 * @param angle : The angle to rotate.
	 */
	public void rotate(double angle)
	{
		final double cosAngle = Math.cos(angle);
		final double sinAngle = Math.sin(angle);
		
		final double newX = _x * cosAngle - _y * sinAngle;
		final double newY = _x * sinAngle + _y * cosAngle;
		
		_x = (int) Math.round(newX);
		_y = (int) Math.round(newY);
	}
	
	/**
	 * @param point : The {@link Point2D} used to calculate the angle.
	 * @return The relative angle between this {@link Point2D} and another {@link Point2D} passed as a parameter.
	 */
	public double calculateRelativeAngle(Point2D point)
	{
		return Math.atan2(_y, _x) - Math.atan2(point.getY(), point.getX());
	}
}