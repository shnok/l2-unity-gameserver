package com.shnok.javaserver.commons.geometry;

import java.util.List;

import com.shnok.javaserver.commons.random.Rnd;

import com.shnok.javaserver.gameserver.model.location.Location;
import com.shnok.javaserver.gameserver.network.serverpackets.ExServerPrimitive;

/**
 * A polygon consisting of various {@link Triangle}s.<br>
 */
public class Polygon extends AShape
{
	protected final List<Triangle> _shapes;
	
	protected final long _size;
	
	/**
	 * Constructor of the {@link Polygon}.
	 * @param shapes : List of {@link AShape}.
	 */
	public Polygon(List<Triangle> shapes)
	{
		_shapes = shapes;
		
		int size = 0;
		for (Triangle shape : shapes)
			size += shape.getSize();
		_size = size;
	}
	
	@Override
	public long getSize()
	{
		return _size;
	}
	
	@Override
	public double getArea()
	{
		// not supported yet
		return -1;
	}
	
	@Override
	public double getVolume()
	{
		// not supported yet
		return -1;
	}
	
	@Override
	public boolean isInside(int x, int y)
	{
		for (Triangle shape : _shapes)
			if (shape.isInside(x, y))
				return true;
			
		return false;
	}
	
	@Override
	public boolean isInside(int x, int y, int z)
	{
		for (Triangle shape : _shapes)
			if (shape.isInside(x, y, z))
				return true;
			
		return false;
	}
	
	@Override
	public Location getRandomLocation()
	{
		long size = Rnd.get(_size);
		
		for (Triangle shape : _shapes)
		{
			size -= shape.getSize();
			if (size < 0)
				return shape.getRandomLocation();
		}
		
		// should never happen
		return null;
	}
	
	@Override
	public void visualize(String info, ExServerPrimitive debug, int z)
	{
		for (Triangle shape : _shapes)
			shape.visualize(info, debug, z);
	}
	
	public List<Triangle> getShapes()
	{
		return _shapes;
	}
}