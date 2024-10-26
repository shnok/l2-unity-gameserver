package com.shnok.javaserver.gameserver.data.xml;

import java.nio.file.Path;
import java.util.ArrayList;
import java.util.List;

import com.shnok.javaserver.commons.data.xml.IXmlReader;
import com.shnok.javaserver.commons.geometry.algorithm.Kong;

import com.shnok.javaserver.gameserver.model.actor.instance.Monster;
import com.shnok.javaserver.gameserver.model.location.Location;
import com.shnok.javaserver.gameserver.model.location.Point2D;
import com.shnok.javaserver.gameserver.model.manor.ManorArea;
import com.shnok.javaserver.gameserver.model.manor.Seed;
import com.shnok.javaserver.gameserver.model.residence.castle.Castle;

import org.w3c.dom.Document;
import org.w3c.dom.NamedNodeMap;

/**
 * This class loads and stores {@link ManorArea}s.<br>
 * <br>
 * {@link ManorArea} is a polygon/territory linked to a specific {@link Castle}. This allow {@link Seed} checks while sowing.
 */
public class ManorAreaData implements IXmlReader
{
	private final List<ManorArea> _manorAreas = new ArrayList<>();
	
	protected ManorAreaData()
	{
		load();
	}
	
	@Override
	public void load()
	{
		parseFile("data/xml/manorAreas.xml");
		LOGGER.info("Loaded {} manor areas.", _manorAreas.size());
	}
	
	@Override
	public void parseDocument(Document doc, Path path)
	{
		final List<Point2D> coords = new ArrayList<>();
		
		forEach(doc, "list", listNode -> forEach(listNode, "area", areaNode ->
		{
			final NamedNodeMap terr = areaNode.getAttributes();
			
			// Get manor area parameters.
			final String name = parseString(terr, "name");
			final int castleId = parseInteger(terr, "castleId");
			final int minZ = parseInteger(terr, "minZ");
			final int maxZ = parseInteger(terr, "maxZ");
			
			// Get manor area coordinates (nodes).
			forEach(areaNode, "node", nodeNode ->
			{
				final NamedNodeMap node = nodeNode.getAttributes();
				coords.add(new Point2D(parseInteger(node, "x"), parseInteger(node, "y")));
			});
			
			// Create manor area and store it in the List.
			try
			{
				_manorAreas.add(new ManorArea(name, castleId, minZ, maxZ, Kong.doTriangulation(coords)));
			}
			catch (Exception e)
			{
				LOGGER.warn("Cannot load manor area \"{}\", {}", name, e.getMessage());
			}
			
			// Clear coordinates.
			coords.clear();
		}));
	}
	
	/**
	 * @param monster : The {@link Monster} to evaluate.
	 * @return The {@link ManorArea} of the given {@link Monster}.
	 */
	public final ManorArea getManorArea(Monster monster)
	{
		// The manor area is determined based on monster's spawn location, not its actual location.
		final Location loc = monster.getSpawnLocation();
		
		return _manorAreas.stream().filter(ma -> ma.isInside(loc.getX(), loc.getY(), loc.getZ())).findFirst().orElse(null);
	}
	
	public static ManorAreaData getInstance()
	{
		return SingletonHolder.INSTANCE;
	}
	
	private static class SingletonHolder
	{
		protected static final ManorAreaData INSTANCE = new ManorAreaData();
	}
}