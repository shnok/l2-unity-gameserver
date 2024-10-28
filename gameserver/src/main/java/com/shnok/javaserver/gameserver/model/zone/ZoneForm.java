package com.shnok.javaserver.gameserver.model.zone;

import com.shnok.javaserver.gameserver.network.serverpackets.unused.ExServerPrimitive;

/**
 * Abstract base class for any zone form.
 */
public abstract class ZoneForm
{
	public abstract boolean isInsideZone(int x, int y, int z);
	
	public abstract boolean intersectsRectangle(int x1, int x2, int y1, int y2);
	
	public abstract int getLowZ(); // Support for the ability to extract the z coordinates of zones.
	
	public abstract int getHighZ(); // New fishing patch makes use of that to get the Z for the hook
	
	public abstract void visualizeZone(String info, ExServerPrimitive debug);
	
	protected boolean lineSegmentsIntersect(int ax1, int ay1, int ax2, int ay2, int bx1, int by1, int bx2, int by2)
	{
		return java.awt.geom.Line2D.linesIntersect(ax1, ay1, ax2, ay2, bx1, by1, bx2, by2);
	}
}