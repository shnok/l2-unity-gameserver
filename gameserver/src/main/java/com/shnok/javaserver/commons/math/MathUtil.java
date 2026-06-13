package com.shnok.javaserver.commons.math;

import com.shnok.javaserver.gameserver.model.WorldObject;
import com.shnok.javaserver.gameserver.model.actor.Creature;
import com.shnok.javaserver.gameserver.model.location.Location;
import com.shnok.javaserver.gameserver.model.location.Point2D;

public class MathUtil
{
	private MathUtil()
	{
		throw new IllegalStateException("Utility class");
	}
	
	public static final int[][] MATRICE_3X3_LINES =
	{
		{
			1,
			2,
			3
		}, // line 1
		{
			4,
			5,
			6
		}, // line 2
		{
			7,
			8,
			9
		}, // line 3
		{
			1,
			4,
			7
		}, // column 1
		{
			2,
			5,
			8
		}, // column 2
		{
			3,
			6,
			9
		}, // column 3
		{
			1,
			5,
			9
		}, // diagonal 1
		{
			3,
			5,
			7
		}, // diagonal 2
	};
	
	/**
	 * @param objectsSize : The overall elements size.
	 * @param pageSize : The number of elements per page.
	 * @return The number of pages, based on the number of elements and the number of elements we want per page.
	 */
	public static int countPagesNumber(int objectsSize, int pageSize)
	{
		return objectsSize / pageSize + (objectsSize % pageSize == 0 ? 0 : 1);
	}
	
	/**
	 * Calculate the angle in degrees between two {@link WorldObject} instances on a 2D plane.<br>
	 * <br>
	 * The angle is calculated based on the positions of the two objects, relative to the positive X-axis.
	 * @param obj1 : The first {@link WorldObject} from which the angle is calculated.
	 * @param obj2 : The second {@link WorldObject} to which the angle is calculated.
	 * @return The angle in degrees, ranging from 0 to 360 degrees.
	 */
	public static double calculateAngleFrom(WorldObject obj1, WorldObject obj2)
	{
		return calculateAngleFrom(obj1.getX(), obj1.getY(), obj2.getX(), obj2.getY());
	}
	
	/**
	 * Calculate the angle in degrees between two points on a 2D plane.<br>
	 * <br>
	 * The angle is calculated based on the coordinates of the two points, relative to the positive X-axis.
	 * @param obj1X : The X coordinate of the first point.
	 * @param obj1Y : The Y coordinate of the first point.
	 * @param obj2X : The X coordinate of the second point.
	 * @param obj2Y : The Y coordinate of the second point.
	 * @return The angle in degrees, ranging from 0 to 360 degrees.
	 */
	public static final double calculateAngleFrom(int obj1X, int obj1Y, int obj2X, int obj2Y)
	{
		double angleTarget = Math.toDegrees(Math.atan2(obj2Y - obj1Y, obj2X - obj1X));
		if (angleTarget < 0)
			angleTarget += 360;
		
		return angleTarget;
	}
	
	/**
	 * Convert a game-specific heading value to degrees.<br>
	 * <br>
	 * The heading is typically represented as an integer value in a range where 0 corresponds to 0 degrees, and 65535 corresponds to just under 360 degrees. This method converts that heading to a standard degree value.
	 * @param clientHeading : The heading value to be converted, typically in the range [0, 65535].
	 * @return The corresponding angle in degrees.
	 */
	public static final double convertHeadingToDegree(int clientHeading)
	{
		return clientHeading / 182.04444444444444;
	}
	
	/**
	 * Calculate a new location based on a starting point, a heading, and a distance.<br>
	 * <br>
	 * The method converts the heading to degrees and calculates the new position after moving a specified distance in the direction of the heading from the given coordinates.
	 * @param x : The X coordinate of the starting point.
	 * @param y : The Y coordinate of the starting point.
	 * @param heading : The heading in game-specific units (not degrees).
	 * @param distance : The distance to move from the starting point in the direction of the heading.
	 * @return A {@link Point2D} representing the new location after the move.
	 */
	public static final Point2D getNewLocationByDistanceAndHeading(int x, int y, int heading, int distance)
	{
		return getNewLocationByDistanceAndDegree(x, y, MathUtil.convertHeadingToDegree(heading), distance);
	}
	
	/**
	 * Calculate a new location based on a starting point, an angle in degrees, and a distance.<br>
	 * <br>
	 * The method calculates the new position after moving a specified distance in the direction of the given angle from the provided coordinates.
	 * @param x : The X coordinate of the starting point.
	 * @param y : The Y coordinate of the starting point.
	 * @param degree : The angle in degrees from the positive X-axis.
	 * @param distance : The distance to move from the starting point in the direction of the angle.
	 * @return A {@link Point2D} representing the new location after the move.
	 */
	public static final Point2D getNewLocationByDistanceAndDegree(int x, int y, double degree, int distance)
	{
		final double radians = Math.toRadians(degree);
		final int deltaX = (int) (distance * Math.cos(radians));
		final int deltaY = (int) (distance * Math.sin(radians));
		
		return new Point2D(x + deltaX, y + deltaY);
	}
	
	/**
	 * Calculate the heading angle from one point to another in a 2D plane.<br>
	 * <br>
	 * The heading is computed as the angle in degrees between the line formed by the two points and the positive X-axis, adjusted to fall within the range [0, 360) degrees. This angle is then converted into a game-specific heading, where 360 degrees corresponds to 65536 units.
	 * @param obj1X : The X coordinate of the first point.
	 * @param obj1Y : The Y coordinate of the first point.
	 * @param obj2X : The X coordinate of the second point.
	 * @param obj2Y : The Y coordinate of the second point.
	 * @return The calculated heading as an integer, where 0 represents 0 degrees, and 65535 represents just under 360 degrees
	 */
	public static final int calculateHeadingFrom(int obj1X, int obj1Y, int obj2X, int obj2Y)
	{
		// Calculate the angle in degrees between the two points.
		double angleTarget = Math.toDegrees(Math.atan2(obj2Y - obj1Y, obj2X - obj1X));
		
		// Ensure the angle is within the range [0, 360).
		if (angleTarget < 0)
			angleTarget += 360;
		
		// Convert the angle in degrees to the game's heading (in a 0 to 65535 range).
		return (int) Math.round(angleTarget * 182.04444444444444);
	}
	
	/**
	 * Check whether a {@link WorldObject} is within a specified range of another {@link WorldObject}.<br>
	 * <br>
	 * This method calculates the distance between {@link WorldObject}s in a 2D or 3D plane, depending on includeZAxis parameter.<br>
	 * <br>
	 * The distance is then compared to the specified range, which may also include the object's collision radius if applicable.
	 * @param range : The maximum range to test. A value of -1 indicates that the range is unlimited.
	 * @param obj1 : The {@link WorldObject} whose distance to the location is being checked. If null, return false.
	 * @param obj2 : The {@link WorldObject} to check the distance against. If null, return false.
	 * @param includeZAxis : If true, the method includes the Z-axis in the distance calculation.
	 * @return True if the object is within the specified range of the location, or false otherwise.
	 */
	public static boolean checkIfInRange(int range, WorldObject obj1, WorldObject obj2, boolean includeZAxis)
	{
		// Early null check for both objects.
		if (obj1 == null || obj2 == null)
			return false;
		
		// If range is -1, return true (indicating no range limitation).
		if (range == -1)
			return true;
		
		// Calculate total collision radius of both objects.
		double totalRadius = 0;
		if (obj1 instanceof Creature creature)
			totalRadius += creature.getCollisionRadius();
		
		if (obj2 instanceof Creature creature)
			totalRadius += creature.getCollisionRadius();
		
		// Calculate squared distance in the X and Y planes.
		final long dx = (long) obj1.getX() - obj2.getX();
		final long dy = (long) obj1.getY() - obj2.getY();
		
		long distanceSquared = dx * dx + dy * dy;
		
		// If Z-axis is included, consider the Z-axis distance.
		if (includeZAxis)
		{
			final long dz = (long) obj1.getZ() - obj2.getZ();
			distanceSquared += dz * dz;
		}
		
		// Calculate the squared maximum allowed distance.
		final double maxDistanceSquared = (range + totalRadius) * (range + totalRadius);
		
		// Return whether the actual distance is within the maximum allowed distance.
		return distanceSquared <= maxDistanceSquared;
	}
	
	/**
	 * Check whether a {@link WorldObject} is within a specified range of a given {@link Location}.<br>
	 * <br>
	 * This method calculates the distance between the {@link WorldObject} and the {@link Location} in a 2D or 3D plane, depending on includeZAxis parameter.<br>
	 * <br>
	 * The distance is then compared to the specified range, which may also include the object's collision radius if applicable.
	 * @param range : The maximum range to test. A value of -1 indicates that the range is unlimited.
	 * @param obj : The {@link WorldObject} whose distance to the location is being checked. If null, return false.
	 * @param loc : The {@link Location} to check the distance against. If this is {@link Location#DUMMY_LOC}, return false.
	 * @param includeZAxis : If true, the method includes the Z-axis in the distance calculation.
	 * @return True if the object is within the specified range of the location, or false otherwise.
	 */
	public static boolean checkIfInRange(int range, WorldObject obj, Location loc, boolean includeZAxis)
	{
		// Early null check and dummy location check.
		if (obj == null || loc.equals(Location.DUMMY_LOC))
			return false;
		
		// If range is -1, return true (indicating no range limitation).
		if (range == -1)
			return true;
		
		// Calculate collision radius if the object is a Creature.
		double collisionRadius = 0;
		if (obj instanceof Creature creature)
			collisionRadius = creature.getCollisionRadius();
		
		// Calculate squared distance in the X and Y planes using long to avoid overflow.
		final long dx = (long) obj.getX() - loc.getX();
		final long dy = (long) obj.getY() - loc.getY();
		
		long distanceSquared = dx * dx + dy * dy;
		
		// If Z-axis is included, consider the Z-axis distance.
		if (includeZAxis)
		{
			final long dz = (long) obj.getZ() - loc.getZ();
			distanceSquared += dz * dz;
		}
		
		// Calculate the squared maximum allowed distance.
		double maxDistanceSquared = (range + collisionRadius) * (range + collisionRadius);
		
		// Return whether the actual distance is within the maximum allowed distance.
		return distanceSquared <= maxDistanceSquared;
	}
	
	/**
	 * @param val : The initial value to edit.
	 * @param numPlaces : The number of decimals to keep.
	 * @return The rounded value to specified number of digits after the decimal point. Based on PHP round().
	 */
	public static float roundTo(float val, int numPlaces)
	{
		if (numPlaces <= 1)
			return Math.round(val);
		
		float exponent = (float) Math.pow(10, numPlaces);
		
		return (Math.round(val * exponent) / exponent);
	}
}