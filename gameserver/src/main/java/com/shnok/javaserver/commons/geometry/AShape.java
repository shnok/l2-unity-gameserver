package com.shnok.javaserver.commons.geometry;

import com.shnok.javaserver.gameserver.model.location.Location;
import com.shnok.javaserver.gameserver.model.location.Point2D;
import com.shnok.javaserver.gameserver.network.serverpackets.unused.ExServerPrimitive;

public abstract class AShape
{
	/**
	 * Returns size of the AShape floor projection.
	 * @return long : Size.
	 */
	public abstract long getSize();
	
	/**
	 * Returns surface area of the AShape.
	 * @return double : Surface area.
	 */
	public abstract double getArea();
	
	/**
	 * Returns enclosed volume of the AShape.
	 * @return double : Enclosed volume.
	 */
	public abstract double getVolume();
	
	/**
	 * Checks if given X, Y coordinates are laying inside the AShape.
	 * @param x : World X coordinates.
	 * @param y : World Y coordinates.
	 * @return boolean : True, when if coordinates are inside this AShape.
	 */
	public abstract boolean isInside(int x, int y);
	
	/**
	 * Checks if given X, Y, Z coordinates are laying inside the AShape.
	 * @param x : World X coordinates.
	 * @param y : World Y coordinates.
	 * @param z : World Z coordinates.
	 * @return boolean : True, when if coordinates are inside this AShape.
	 */
	public abstract boolean isInside(int x, int y, int z);
	
	/**
	 * Returns {@link Location} of random point inside AShape.<br>
	 * In case AShape is only in 2D space, Z is set as 0.
	 * @return {@link Location} : Random location inside AShape.
	 */
	public abstract Location getRandomLocation();
	
	/**
	 * Add the visualization of itself to given {@link ExServerPrimitive} packet.
	 * @param info : The name to be displayed.
	 * @param debug : The given {@link ExServerPrimitive} packet to be added into.
	 * @param z : The Z coordinate as a view reference.
	 */
	public abstract void visualize(String info, ExServerPrimitive debug, int z);
	
	protected Point2D _center;
	
	public Point2D getCenter()
	{
		return _center;
	}
}