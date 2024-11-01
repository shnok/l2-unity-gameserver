package com.shnok.javaserver.gameserver.model.spawn;

import java.util.List;

import com.shnok.javaserver.commons.geometry.Polygon;
import com.shnok.javaserver.commons.geometry.Triangle;
import com.shnok.javaserver.commons.logging.CLogger;
import com.shnok.javaserver.commons.random.Rnd;

import com.shnok.javaserver.gameserver.geoengine.GeoEngine;
import com.shnok.javaserver.gameserver.model.actor.Npc;
import com.shnok.javaserver.gameserver.model.location.Location;
import com.shnok.javaserver.gameserver.model.location.SpawnLocation;
import com.shnok.javaserver.gameserver.network.serverpackets.unused.ExServerPrimitive;

/**
 * Defines an area inside the world where to spawn {@link Npc}s via {@link NpcMaker}.
 * @see Polygon
 */
public class Territory extends Polygon
{
	private static final CLogger LOGGER = new CLogger(Territory.class.getName());
	
	private static final int MAX_ITERATIONS = 10;
	
	// name
	private final String _name;
	
	// min, max and average Z coordinate
	private final int _minZ;
	private final int _maxZ;
	private final int _avgZ;
	
	/**
	 * Implicit constructor.
	 * @param name : The name of {@link Territory}.
	 * @param shapes : A list of {@link Triangle}s forming a {@link Territory}.
	 * @param minZ : Minimum Z.
	 * @param maxZ : Maximum Z.
	 */
	public Territory(String name, List<Triangle> shapes, int minZ, int maxZ)
	{
		super(shapes);
		
		_name = name;
		
		_minZ = minZ;
		_maxZ = maxZ;
		_avgZ = (minZ + maxZ) / 2;
	}
	
	/**
	 * Returns the Territory name.
	 * @return String : The name.
	 */
	public final String getName()
	{
		return _name;
	}
	
	/**
	 * Returns the Territory minimum Z coordinate.
	 * @return int : The minimum Z coordinate.
	 */
	public final int getMinZ()
	{
		return _minZ;
	}
	
	/**
	 * Returns the Territory maximum Z coordinate.
	 * @return int : The maximum Z coordinate.
	 */
	public final int getMaxZ()
	{
		return _maxZ;
	}
	
	/**
	 * Returns the Territory average Z coordinate.
	 * @return int : The average Z coordinate.
	 */
	public final int getAvgZ()
	{
		return _avgZ;
	}
	
	@Override
	public boolean isInside(int x, int y, int z)
	{
		// check Z coordinate to exceed limits
		if (z < _minZ || z > _maxZ)
			return false;
		
		return super.isInside(x, y, z);
	}
	
	public boolean isInside(Location loc)
	{
		return isInside(loc.getX(), loc.getY(), loc.getZ());
	}
	
	@Override
	public SpawnLocation getRandomLocation()
	{
		Location loc = null;
		int z = 0;
		
		// try to find Location within MAX_ITERATIONS iterations
		int failedZ = 0;
		int failedGeo = 0;
		do
		{
			// get random
			long rand = Rnd.get(_size);
			
			// loop Triangles
			for (Triangle shape : _shapes)
			{
				// find random triangle
				rand -= shape.getSize();
				if (rand < 0)
				{
					// get random X, Y coordinates inside Triangle
					loc = shape.getRandomLocation();
					
					// get real Z coordinate based on Geodata
					z = GeoEngine.getInstance().getHeight(loc.getX(), loc.getY(), _avgZ);
					
					// check Z coordinate to exceed limits and eventually get new coordinates
					if (z < _minZ || z > _maxZ)
					{
						failedZ++;
						break;
					}
					
					// check close area for available movement
					if (!GeoEngine.getInstance().canMoveAround(loc.getX(), loc.getY(), z))
					{
						failedGeo++;
						break;
					}
					
					// return Location with XYZ and random heading
					return new SpawnLocation(loc.getX(), loc.getY(), z, Rnd.get(65536));
				}
			}
		}
		while (failedZ + failedGeo < MAX_ITERATIONS);
		
		// correct position not found
		LOGGER.warn("Territory name \"{}\", wrong Z {}, wrong geo {}", _name, failedZ, failedGeo);
		
		// use last estimated, if exists
		return loc == null ? null : new SpawnLocation(loc.getX(), loc.getY(), z, Rnd.get(65536));
	}
	
	public SpawnLocation getRandomLocation(Territory bannedTerritory)
	{
		Location loc = null;
		
		// try to find Location within MAX_ITERATIONS iterations
		int failedZ = 0;
		int failedGeo = 0;
		do
		{
			// get random
			long rand = Rnd.get(_size);
			
			// loop Triangles
			for (Triangle shape : _shapes)
			{
				// find random triangle
				rand -= shape.getSize();
				if (rand < 0)
				{
					// Get random X, Y coordinates inside Triangle ; get real Z coordinate based on Geodata.
					loc = shape.getRandomLocation();
					loc.setZ(GeoEngine.getInstance().getHeight(loc.getX(), loc.getY(), _avgZ));
					
					// If a banned territory is set and generated Location is part of it, break current execution without doing anything.
					if (bannedTerritory != null && bannedTerritory.isInside(loc))
						break;
					
					// check Z coordinate to exceed limits and eventually get new coordinates
					if (loc.getZ() < _minZ || loc.getZ() > _maxZ)
					{
						failedZ++;
						break;
					}
					
					// check close area for available movement
					if (!GeoEngine.getInstance().canMoveAround(loc.getX(), loc.getY(), loc.getZ()))
					{
						failedGeo++;
						break;
					}
					
					// return Location with XYZ and random heading
					return new SpawnLocation(loc.getX(), loc.getY(), loc.getZ(), Rnd.get(65536));
				}
			}
		}
		while (failedZ + failedGeo < MAX_ITERATIONS);
		
		// correct position not found
		LOGGER.warn("Territory name \"{}\", wrong Z {}, wrong geo {}", _name, failedZ, failedGeo);
		
		// Use last iteration, no matter what. It can't be null.
		return (loc == null) ? null : new SpawnLocation(loc.getX(), loc.getY(), loc.getZ(), Rnd.get(65536));
	}
	
	@Override
	public void visualize(String info, ExServerPrimitive debug, int z)
	{
		super.visualize(_name, debug, z);
	}
	
	/**
	 * @param loc : A {@link Location} to test.
	 * @return The {@link Triangle} associated to the {@link Location} set as parameter.
	 */
	public Triangle getShape(Location loc)
	{
		return _shapes.stream().filter(s -> s.isInside(loc.getX(), loc.getY())).findFirst().orElse(null);
	}
}