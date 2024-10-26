package com.shnok.javaserver.gameserver.geoengine.pathfinding;

import java.awt.Color;
import java.util.Collections;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.Set;

import net.sf.l2j.commons.util.PriorityQueueSet;

import net.sf.l2j.Config;
import net.sf.l2j.gameserver.geoengine.GeoEngine;
import net.sf.l2j.gameserver.geoengine.geodata.ABlock;
import net.sf.l2j.gameserver.geoengine.geodata.GeoStructure;
import net.sf.l2j.gameserver.model.location.Location;
import net.sf.l2j.gameserver.network.serverpackets.ExServerPrimitive;

public class PathFinder
{
	// Container holding Nodes to be explored.
	private PriorityQueueSet<Node> _opened = new PriorityQueueSet<>();
	
	// Container holding Nodes already explored.
	private final Set<Node> _closed = new HashSet<>();
	
	private final long _currentTime;
	
	// Target coordinates.
	private int _gtx;
	private int _gty;
	private int _gtz;
	
	private Node _current;
	
	public PathFinder()
	{
		_currentTime = System.currentTimeMillis();
	}
	
	/**
	 * @param gox : origin point x
	 * @param goy : origin point y
	 * @param goz : origin point z
	 * @param gtx : target point x
	 * @param gty : target point y
	 * @param gtz : target point z
	 * @param debug : The {@link ExServerPrimitive} packet to add debug informations in.
	 * @return A {@link List} of {@link Location}s for the path or an empty {@link List}, if path not found or too complex.
	 */
	public final List<Location> findPath(int gox, int goy, int goz, int gtx, int gty, int gtz, ExServerPrimitive debug)
	{
		// Set target coordinates.
		_gtx = gtx;
		_gty = gty;
		_gtz = gtz;
		
		// Process a new Node.
		_current = new Node(gox, goy, goz, GeoEngine.getInstance().getNsweNearest(gox, goy, goz));
		
		// Set movement cost.
		_current.setCost(null, 0, getCostH(gox, goy, goz));
		
		// Add the start Node to the open list.
		_opened.add(_current);
		
		int count = 0;
		while (!_opened.isEmpty() && count < Config.MAX_ITERATIONS)
		{
			// Get the Node with the lowest F cost from the open list.
			_current = _opened.poll();
			
			// Current Node is the target Node ; we've reached the target. Build the path from subsequent Nodes.
			if (_current.getGeoX() == _gtx && _current.getGeoY() == _gty && _current.getZ() == _gtz)
				return constructPath(debug);
			
			// Move the current Node to the closed list to avoid re-exploration.
			_closed.add(_current);
			
			// Expand the current Node to find all neighbors.
			expand();
			
			count++;
		}
		
		// If the target is not reachable or search iterations exceeded, return an empty path.
		return Collections.emptyList();
	}
	
	/**
	 * Build the path from subsequent nodes. Skip nodes in straight directions, keep only corner nodes.
	 * @param debug : The {@link ExServerPrimitive} packet to add debug informations in.
	 * @return List of {@link Node}s representing the path.
	 */
	private List<Location> constructPath(ExServerPrimitive debug)
	{
		// Create result.
		LinkedList<Location> path = new LinkedList<>();
		
		// Clear X/Y direction.
		int dx = 0;
		int dy = 0;
		
		// Get parent node.
		Node parent = _current.getParent();
		
		// While parent exists.
		while (parent != null)
		{
			// Get parent node to current node X/Y direction.
			final int nx = parent.getGeoX() - _current.getGeoX();
			final int ny = parent.getGeoY() - _current.getGeoY();
			
			// Direction has changed?
			if (dx != nx || dy != ny)
			{
				// Add current node to the beginning of the path.
				path.addFirst(_current);
				
				// Update X/Y direction.
				dx = nx;
				dy = ny;
			}
			
			// Move current node and update its parent.
			_current = parent;
			parent = _current.getParent();
		}
		
		// Process the debug if existing.
		if (debug != null)
			debug.addPoint(System.currentTimeMillis() - _currentTime + "ms", Color.RED, true, _current.getX(), _current.getY(), _current.getZ() + 16);
		
		return path;
	}
	
	/**
	 * Expand the current {@link Node} by exploring its neighbors (axially and diagonally).
	 */
	private void expand()
	{
		// Movement is blocked, skip.
		final byte nswe = _current.getNswe();
		if (nswe == GeoStructure.CELL_FLAG_NONE)
			return;
		
		// Get geo coordinates of the node to be expanded.
		final int x = _current.getGeoX();
		final int y = _current.getGeoY();
		final int z = _current.getZ() + GeoStructure.CELL_IGNORE_HEIGHT;
		
		// Check each expansion direction and add nodes accordingly
		final byte nsweN = addDirectionalNode(x, y, z, nswe, 0, -1, GeoStructure.CELL_FLAG_N);
		final byte nsweS = addDirectionalNode(x, y, z, nswe, 0, 1, GeoStructure.CELL_FLAG_S);
		final byte nsweW = addDirectionalNode(x, y, z, nswe, -1, 0, GeoStructure.CELL_FLAG_W);
		final byte nsweE = addDirectionalNode(x, y, z, nswe, 1, 0, GeoStructure.CELL_FLAG_E);
		
		// Add corner nodes if necessary
		addCornerNode(x, y, z, nswe, -1, -1, GeoStructure.CELL_FLAG_W, GeoStructure.CELL_FLAG_N, nsweW, nsweN);
		addCornerNode(x, y, z, nswe, 1, -1, GeoStructure.CELL_FLAG_E, GeoStructure.CELL_FLAG_N, nsweE, nsweN);
		addCornerNode(x, y, z, nswe, -1, 1, GeoStructure.CELL_FLAG_W, GeoStructure.CELL_FLAG_S, nsweW, nsweS);
		addCornerNode(x, y, z, nswe, 1, 1, GeoStructure.CELL_FLAG_E, GeoStructure.CELL_FLAG_S, nsweE, nsweS);
	}
	
	private byte addDirectionalNode(int x, int y, int z, byte nswe, int dx, int dy, byte directionFlag)
	{
		return ((nswe & directionFlag) != 0) ? addNode(x + dx, y + dy, z, false) : GeoStructure.CELL_FLAG_NONE;
	}
	
	private void addCornerNode(int x, int y, int z, byte nswe, int dx, int dy, byte directionFlagX, byte directionFlagY, byte nsweX, byte nsweY)
	{
		if ((nsweX & directionFlagY) != 0 && (nsweY & directionFlagX) != 0)
		{
			if ((getNodeNswe(x + dx, y, z) & directionFlagY) != 0)
				addNode(x + dx, y + dy, z, true);
		}
	}
	
	private static byte getNodeNswe(int gx, int gy, int gz)
	{
		// Check new node is out of geodata grid (world coordinates).
		if (gx < 0 || gx >= GeoStructure.GEO_CELLS_X || gy < 0 || gy >= GeoStructure.GEO_CELLS_Y)
			return GeoStructure.CELL_FLAG_NONE;
		
		// Get geodata block and check if there is a layer at given coordinates.
		final ABlock block = GeoEngine.getInstance().getBlock(gx, gy);
		final int index = block.getIndexBelow(gx, gy, gz, null);
		if (index < 0)
			return GeoStructure.CELL_FLAG_NONE;
		
		// Get node geodata nswe.
		return block.getNswe(index, null);
	}
	
	/**
	 * Generate a {@link Node}, validate it and add to opened list.
	 * @param gx : The new node X geodata coordinate.
	 * @param gy : The new node Y geodata coordinate.
	 * @param gz : The new node Z geodata coordinate.
	 * @param diagonal : The new node is being explored in diagonal direction.
	 * @return The nswe of the added node. Blank, if not added.
	 */
	private byte addNode(int gx, int gy, int gz, boolean diagonal)
	{
		// Check new node is out of geodata grid (world coordinates).
		if (gx < 0 || gx >= GeoStructure.GEO_CELLS_X || gy < 0 || gy >= GeoStructure.GEO_CELLS_Y)
			return GeoStructure.CELL_FLAG_NONE;
		
		// Get geodata block and check if there is a layer at given coordinates.
		final ABlock block = GeoEngine.getInstance().getBlock(gx, gy);
		final int index = block.getIndexBelow(gx, gy, gz, null);
		if (index < 0)
			return GeoStructure.CELL_FLAG_NONE;
		
		// Get node geodata Z and nswe.
		gz = block.getHeight(index, null);
		final byte nswe = block.getNswe(index, null);
		
		// Generate a new Node.
		final Node node = new Node(gx, gy, gz, nswe);
		
		// If the node is already in the opened or closed containers, return early.
		if (_opened.contains(node) || _closed.contains(node))
			return nswe;
		
		// Calculate node weight based on the nswe data.
		final int weight = (nswe == GeoStructure.CELL_FLAG_ALL) ? (diagonal ? Config.MOVE_WEIGHT_DIAG : Config.MOVE_WEIGHT) : (diagonal ? Config.OBSTACLE_WEIGHT_DIAG : Config.OBSTACLE_WEIGHT);
		
		// Set node movement cost.
		node.setCost(_current, weight, getCostH(gx, gy, gz));
		
		// Add the node to opened list.
		_opened.add(node);
		
		return nswe;
	}
	
	/**
	 * Calculate cost H value, calculated using diagonal distance method.<br>
	 * Note: Manhattan distance is too simple, causing to explore more unwanted cells.
	 * @param gx : The node geodata X coordinate.
	 * @param gy : The node geodata Y coordinate.
	 * @param gz : The node geodata Z coordinate.
	 * @return The cost H value (estimated cost to reach the target).
	 */
	private int getCostH(int gx, int gy, int gz)
	{
		final int dx = Math.abs(gx - _gtx);
		final int dy = Math.abs(gy - _gty);
		final int dz = Math.abs(gz - _gtz) / GeoStructure.CELL_HEIGHT;
		
		return (int) (Math.sqrt(dx * dx + dy * dy + dz * dz) * Config.HEURISTIC_WEIGHT);
	}
}