package com.shnok.javaserver.commons.geometry.algorithm;

import java.awt.Point;
import java.util.ArrayList;
import java.util.List;

import com.shnok.javaserver.commons.geometry.Triangle;

import com.shnok.javaserver.gameserver.model.location.Point2D;

public class Kong
{
	private Kong()
	{
		throw new IllegalStateException("Utility class");
	}
	
	private static final int TRIANGULATION_MAX_LOOPS = 100;
	
	/**
	 * Creates a triangulated polygon using Kong's algorithm.<br>
	 * The list of points must form a monotone polygon, otherwise the algorithm fails.
	 * @param points : List of {@link Point} points forming a polygon.
	 * @return List of {@link Triangle}s forming a polygon.
	 * @throws IndexOutOfBoundsException : Less than 3 {@link Point2D}s.
	 * @throws IllegalArgumentException : Given {@link Point2D}s do not form monotone polygon.
	 */
	public static final List<Triangle> doTriangulation(List<Point2D> points) throws IndexOutOfBoundsException, IllegalArgumentException
	{
		// not a polygon, throw exception
		if (points.size() < 3)
			throw new IndexOutOfBoundsException("Can't triangulate polygon from less than 3 coordinates.");
		
		// get polygon orientation
		final boolean isCw = getPolygonOrientation(points);
		
		// calculate non convex points
		final List<Point2D> nonConvexPoints = calculateNonConvexPoints(points, isCw);
		
		// polygon triangulation of points based on orientation and non-convex points
		return doTriangulationAlgorithm(points, isCw, nonConvexPoints);
	}
	
	/**
	 * Returns clockwise (cw) or counter-clockwise (ccw) orientation of the polygon.
	 * @param points : List of all points.
	 * @return {@code boolean} : True, when the polygon is clockwise orientated.
	 */
	private static final boolean getPolygonOrientation(List<Point2D> points)
	{
		// first find point with minimum x-coord - if there are several ones take the one with maximal y-coord
		
		// get point
		final int size = points.size();
		int index = 0;
		Point2D point = points.get(0);
		for (int i = 1; i < size; i++)
		{
			Point2D pt = points.get(i);
			
			// x lower, or x same and y higher
			if ((pt.getX() < point.getX()) || pt.getX() == point.getX() && pt.getY() > point.getY())
			{
				point = pt;
				index = i;
			}
		}
		
		// get previous point
		final Point2D pointPrev = points.get(getPrevIndex(size, index));
		
		// get next point
		final Point2D pointNext = points.get(getNextIndex(size, index));
		
		// get orientation
		final int vx = point.getX() - pointPrev.getX();
		final int vy = point.getY() - pointPrev.getY();
		final int res = pointNext.getX() * vy - pointNext.getY() * vx + vx * pointPrev.getY() - vy * pointPrev.getX();
		
		// return
		return res <= 0;
	}
	
	/**
	 * Returns next index to given index of data container.
	 * @param size : Size of the data container.
	 * @param index : Index to be compared.
	 * @return {@code int} : Next index.
	 */
	private static final int getNextIndex(int size, int index)
	{
		// increase index and check for limit
		if (++index >= size)
			return index - size;
		
		return index;
	}
	
	/**
	 * Returns previous index to given index of data container.
	 * @param size : Size of the data container.
	 * @param index : Index to be compared.
	 * @return {@code int} : Previous index.
	 */
	private static final int getPrevIndex(int size, int index)
	{
		// decrease index and check for limit
		if (--index < 0)
			return size + index;
		
		return index;
	}
	
	/**
	 * This determines all concave vertices of the polygon and separate convex ones.
	 * @param points : List of all points.
	 * @param isCw : Polygon orientation (clockwise/counterclockwise).
	 * @return {@code List<Point>} : List of non-convex points.
	 */
	private static final List<Point2D> calculateNonConvexPoints(List<Point2D> points, boolean isCw)
	{
		// list of non convex points
		final List<Point2D> nonConvexPoints = new ArrayList<>();
		
		// result value of test function
		final int size = points.size();
		for (int i = 0; i < size - 1; i++)
		{
			// get 3 points
			final Point2D point = points.get(i);
			final Point2D pointNext = points.get(getNextIndex(size, i));
			final Point2D pointNextNext = points.get(getNextIndex(size, i + 1));
			
			final int vx = pointNext.getX() - point.getX();
			final int vy = pointNext.getY() - point.getY();
			
			// note: cw means res/newres is <= 0
			final boolean res = (pointNextNext.getX() * vy - pointNextNext.getY() * vx + vx * point.getY() - vy * point.getX()) > 0;
			if (res == isCw)
				nonConvexPoints.add(pointNext);
		}
		
		return nonConvexPoints;
	}
	
	/**
	 * Perform Kong's triangulation algorithm.
	 * @param points : List of all points.
	 * @param isCw : Polygon orientation (clockwise/counterclockwise).
	 * @param nonConvexPoints : List of all non-convex points.
	 * @return {@code List<Triangle>} : List of {@link Triangle}.
	 * @throws IllegalArgumentException : When coordinates are not aligned to form monotone polygon.
	 */
	private static final List<Triangle> doTriangulationAlgorithm(List<Point2D> points, boolean isCw, List<Point2D> nonConvexPoints) throws IllegalArgumentException
	{
		// create the list
		final List<Triangle> triangles = new ArrayList<>();
		
		int size = points.size();
		int loops = 0;
		int index = 1;
		while (size > 3)
		{
			// get next and previous indexes
			final int indexPrev = getPrevIndex(size, index);
			final int indexNext = getNextIndex(size, index);
			
			// get points
			final Point2D pointPrev = points.get(indexPrev);
			final Point2D point = points.get(index);
			final Point2D pointNext = points.get(indexNext);
			
			// check point to create polygon ear
			if (isEar(isCw, nonConvexPoints, pointPrev, point, pointNext))
			{
				// create triangle from polygon ear
				triangles.add(new Triangle(pointPrev, point, pointNext));
				
				// remove middle point from list, update size
				points.remove(index);
				size--;
				
				// move index
				index = getPrevIndex(size, index);
			}
			else
			{
				// move index
				index = indexNext;
			}
			
			if (++loops == TRIANGULATION_MAX_LOOPS)
				throw new IllegalArgumentException("Coordinates are not aligned to form monotone polygon.");
		}
		
		// add last triangle
		triangles.add(new Triangle(points.get(0), points.get(1), points.get(2)));
		
		// return triangles
		return triangles;
	}
	
	/**
	 * Returns true if the triangle formed by A, B, C points is an ear considering the polygon - thus if no other point is inside and it is convex.
	 * @param isCw : Polygon orientation (clockwise/counterclockwise).
	 * @param nonConvexPoints : List of all non-convex points.
	 * @param A : ABC triangle
	 * @param B : ABC triangle
	 * @param C : ABC triangle
	 * @return {@code boolean} : True, when ABC is ear of the polygon.
	 */
	private static final boolean isEar(boolean isCw, List<Point2D> nonConvexPoints, Point2D A, Point2D B, Point2D C)
	{
		// ABC triangle
		if (!isConvex(isCw, A, B, C))
			return false;
		
		// iterate over all concave points and check if one of them lies inside the given triangle
		for (int i = 0; i < nonConvexPoints.size(); i++)
		{
			if (isInside(A, B, C, nonConvexPoints.get(i)))
				return false;
		}
		
		return true;
	}
	
	/**
	 * Returns true when the {@link Point2D} B is convex considered the actual polygon. A, B and C are three consecutive {@link Point2D} of the polygon.
	 * @param isCw : Polygon orientation (clockwise/counterclockwise).
	 * @param A : {@link Point2D}, previous to B.
	 * @param B : {@link Point2D}, which convex information is being checked.
	 * @param C : {@link Point2D}, next to B.
	 * @return {@code boolean} : True, when B is convex point.
	 */
	private static final boolean isConvex(boolean isCw, Point2D A, Point2D B, Point2D C)
	{
		// get vector coordinates
		final int BAx = B.getX() - A.getX();
		final int BAy = B.getY() - A.getY();
		
		// get virtual triangle orientation
		final boolean cw = (C.getX() * BAy - C.getY() * BAx + BAx * A.getY() - BAy * A.getX()) > 0;
		
		// compare with orientation of polygon
		return cw != isCw;
	}
	
	/**
	 * Returns true, when {@link Point2D} P is inside triangle ABC.
	 * @param A : ABC triangle
	 * @param B : ABC triangle
	 * @param C : ABC triangle
	 * @param P : {@link Point2D} to be checked in ABC.
	 * @return {@code boolean} : True, when P is inside ABC.
	 */
	private static final boolean isInside(Point2D A, Point2D B, Point2D C, Point2D P)
	{
		// get vector coordinates
		final int BAx = B.getX() - A.getX();
		final int BAy = B.getY() - A.getY();
		final int CAx = C.getX() - A.getX();
		final int CAy = C.getY() - A.getY();
		final int PAx = P.getX() - A.getX();
		final int PAy = P.getY() - A.getY();
		
		// get determinant
		final double detXYZ = BAx * CAy - CAx * BAy;
		
		// calculate BA and CA coefficient to each P from A
		final double ba = (BAx * PAy - PAx * BAy) / detXYZ;
		final double ca = (PAx * CAy - CAx * PAy) / detXYZ;
		
		// check coefficients
		return (ba > 0 && ca > 0 && (ba + ca) < 1);
	}
}