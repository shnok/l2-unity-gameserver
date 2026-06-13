package com.shnok.javaserver.gameserver.model.manor;

import java.util.List;

import com.shnok.javaserver.commons.geometry.Polygon;
import com.shnok.javaserver.commons.geometry.Triangle;

/**
 * This class represents the manor area in a world.<br>
 * <br>
 * Manor area consists of a polygon defines by nodes.<br>
 * Manor area has a particular castle assigned to it.
 */
public class ManorArea extends Polygon
{
	private final String _name;
	private final int _castleId;
	
	private final int _minZ;
	private final int _maxZ;
	
	public ManorArea(String name, int castleId, int minZ, int maxZ, List<Triangle> shapes)
	{
		super(shapes);
		
		_name = name;
		_castleId = castleId;
		
		_minZ = minZ;
		_maxZ = maxZ;
	}
	
	/**
	 * @return The manor area name.
	 */
	public final String getName()
	{
		return _name;
	}
	
	/**
	 * @return The manor area castle id.
	 */
	public final int getCastleId()
	{
		return _castleId;
	}
	
	/**
	 * @return The manor area minimum Z coordinate.
	 */
	public final int getMinZ()
	{
		return _minZ;
	}
	
	/**
	 * @return The manor area maximum Z coordinate.
	 */
	public final int getMaxZ()
	{
		return _maxZ;
	}
	
	@Override
	public boolean isInside(int x, int y, int z)
	{
		// Check Z coordinate to exceed limits.
		if (z < _minZ || z > _maxZ)
			return false;
		
		return super.isInside(x, y, z);
	}
}