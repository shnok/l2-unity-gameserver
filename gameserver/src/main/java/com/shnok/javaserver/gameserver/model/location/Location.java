package com.shnok.javaserver.gameserver.model.location;

import java.util.Arrays;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import java.util.Objects;

import net.sf.l2j.commons.data.StatSet;

/**
 * A datatype used to retain a 3D (x/y/z) point. It got the capability to be set and cleaned.
 */
public class Location extends Point2D
{
	public static final Location DUMMY_LOC = new Location(0, 0, 0);
	
	protected int _z;
	
	public Location(int x, int y, int z)
	{
		super(x, y);
		
		_z = z;
	}
	
	public Location(Location loc)
	{
		this(loc.getX(), loc.getY(), loc.getZ());
	}
	
	public Location(StatSet set)
	{
		this(set.getInteger("x"), set.getInteger("y"), set.getInteger("z"));
	}
	
	@Override
	public Location clone()
	{
		return new Location(_x, _y, _z);
	}
	
	@Override
	public String toString()
	{
		return super.toString() + ", " + _z;
	}
	
	@Override
	public int hashCode()
	{
		return 31 * super.hashCode() + Objects.hash(_z);
	}
	
	@Override
	public boolean equals(Object obj)
	{
		if (this == obj)
			return true;
		
		if (!super.equals(obj))
			return false;
		
		if (getClass() != obj.getClass())
			return false;
		
		final Location other = (Location) obj;
		return _z == other._z;
	}
	
	@Override
	public void clean()
	{
		super.clean();
		
		_z = 0;
	}
	
	/**
	 * @param x : The X coord to test.
	 * @param y : The Y coord to test.
	 * @param z : The Z coord to test.
	 * @return True if all coordinates equals this {@link Location} coordinates.
	 */
	public boolean equals(int x, int y, int z)
	{
		return super.equals(x, y) && _z == z;
	}
	
	public int getZ()
	{
		return _z;
	}
	
	public void setZ(int z)
	{
		_z = z;
	}
	
	public void set(int x, int y, int z)
	{
		super.set(x, y);
		
		_z = z;
	}
	
	public void set(Location loc)
	{
		set(loc.getX(), loc.getY(), loc.getZ());
	}
	
	/**
	 * Set the current {@link Location} as {@link Location} set as parameter, minus the offset.
	 * @param loc : The {@link Location} used as destination.
	 * @param offset : The offset used to impact the {@link Location}.
	 */
	public void setLocationMinusOffset(Location loc, double offset)
	{
		final int dx = loc.getX() - _x;
		final int dy = loc.getY() - _y;
		final int dz = loc.getZ() - _z;
		
		double fraction = Math.sqrt(dx * dx + dy * dy + dz * dz);
		fraction = 1 - (offset / fraction);
		
		_x += (int) (dx * fraction);
		_y += (int) (dy * fraction);
		_z += (int) (dz * fraction);
	}
	
	/**
	 * @param x : The X position to test.
	 * @param y : The Y position to test.
	 * @param z : The Z position to test.
	 * @return The distance between this {@link Location} and some given coordinates.
	 */
	public double distance3D(int x, int y, int z)
	{
		final double dx = (double) _x - x;
		final double dy = (double) _y - y;
		final double dz = (double) _z - z;
		
		return Math.sqrt((dx * dx) + (dy * dy) + (dz * dz));
	}
	
	/**
	 * @param loc : The {@link Location} to test.
	 * @return The distance between this {@Location} and the {@link Location} set as parameter.
	 */
	public double distance3D(Location loc)
	{
		return distance3D(loc.getX(), loc.getY(), loc.getZ());
	}
	
	/**
	 * @param x : The X position to test.
	 * @param y : The Y position to test.
	 * @param z : The Z position to test.
	 * @param radius : The radius to check.
	 * @return True if this {@link Location} is in the radius of some given coordinates.
	 */
	public boolean isIn3DRadius(int x, int y, int z, int radius)
	{
		return distance3D(x, y, z) < radius;
	}
	
	/**
	 * @param point : The {@link Location} to test.
	 * @param radius : The radius to check.
	 * @return True if this {@link Location} is in the radius of the {@link Location} set as parameter.
	 */
	public boolean isIn3DRadius(Location point, int radius)
	{
		return distance3D(point) < radius;
	}
	
	/**
	 * @param positions : The array of {@link Location}s used as positions.
	 * @return The nearest {@link Location} from this {@link Location}.
	 */
	public Location getClosestPosition(Location[] positions)
	{
		return Arrays.stream(positions).min(Comparator.comparingDouble(this::distance3D)).orElse(null);
	}
	
	/**
	 * @param positions : The array of {@link Location}s used as positions.
	 * @param count : The number of returned {@link Location}s.
	 * @return A {@link List} of the {@link Location}s set as parameter, sorted by distance and limited by the count number set as parameter.
	 */
	public List<Location> getClosestPositionList(Location[] positions, int count)
	{
		return Arrays.stream(positions).sorted(Collections.reverseOrder(Comparator.comparingDouble(this::distance3D))).limit(count).toList();
	}
}