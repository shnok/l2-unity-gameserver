package com.shnok.javaserver.gameserver.data.xml;

import java.nio.file.Path;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.shnok.javaserver.commons.data.StatSet;
import com.shnok.javaserver.commons.data.xml.IXmlReader;

import com.shnok.javaserver.gameserver.enums.boats.BoatDock;
import com.shnok.javaserver.gameserver.idfactory.IdFactory;
import com.shnok.javaserver.gameserver.model.actor.Boat;
import com.shnok.javaserver.gameserver.model.actor.template.CreatureTemplate;
import com.shnok.javaserver.gameserver.model.boat.BoatEngine;
import com.shnok.javaserver.gameserver.model.boat.BoatItinerary;
import com.shnok.javaserver.gameserver.model.location.BoatLocation;
import com.shnok.javaserver.gameserver.taskmanager.BoatTaskManager;

import org.w3c.dom.Document;
import org.w3c.dom.NamedNodeMap;

public class BoatData implements IXmlReader
{
	private final List<BoatItinerary> _itineraries = new ArrayList<>();
	private final Map<Integer, Boat> _boats = new HashMap<>();
	
	private final CreatureTemplate _template;
	
	protected BoatData()
	{
		final StatSet set = new StatSet();
		
		set.set("str", 0);
		set.set("con", 0);
		set.set("dex", 0);
		set.set("int", 0);
		set.set("wit", 0);
		set.set("men", 0);
		
		set.set("hp", 50000);
		
		set.set("hpRegen", 3.e-3f);
		set.set("mpRegen", 3.e-3f);
		
		set.set("radius", 0);
		set.set("height", 0);
		
		set.set("pAtk", 0);
		set.set("mAtk", 0);
		set.set("pDef", 100);
		set.set("mDef", 100);
		
		set.set("runSpd", 0);
		
		_template = new CreatureTemplate(set);
	}
	
	@Override
	public void parseDocument(Document doc, Path path)
	{
		forEach(doc, "list", listNode -> forEach(listNode, "itinerary", itineraryNode ->
		{
			final NamedNodeMap attrs = itineraryNode.getAttributes();
			
			final BoatDock dock1 = parseEnum(attrs, BoatDock.class, "dock1");
			final BoatDock dock2 = parseEnum(attrs, BoatDock.class, "dock2", null);
			
			final int item1 = parseInteger(attrs, "item1", 0);
			final int item2 = parseInteger(attrs, "item2", 0);
			
			final int heading = parseInteger(attrs, "heading");
			
			final List<BoatLocation[]> routes = new ArrayList<>();
			
			forEach(itineraryNode, "route", routeNode ->
			{
				final List<BoatLocation> nodes = new ArrayList<>();
				
				forEach(routeNode, "node", nodeNode -> nodes.add(new BoatLocation(parseAttributes(nodeNode))));
				
				routes.add(nodes.toArray(new BoatLocation[0]));
			});
			
			_itineraries.add(new BoatItinerary(dock1, dock2, item1, item2, heading, routes.toArray(new BoatLocation[0][])));
		}));
	}
	
	public void reload()
	{
		_itineraries.clear();
		
		_boats.values().forEach(Boat::deleteMe);
		_boats.clear();
		
		for (BoatDock dock : BoatDock.VALUES)
			dock.setBusy(false);
		
		BoatTaskManager.getInstance().clear();
		
		load();
	}
	
	@Override
	public void load()
	{
		parseFile("data/xml/boatRoutes.xml");
		LOGGER.info("Loaded {} boat itineraries.", _itineraries.size());
		
		_itineraries.forEach(itinerary -> BoatTaskManager.getInstance().add(new BoatEngine(itinerary)));
	}
	
	public List<BoatItinerary> getItineraries()
	{
		return _itineraries;
	}
	
	public Boat getBoat(int boatId)
	{
		return _boats.get(boatId);
	}
	
	public Boat getNewBoat(BoatItinerary itinerary)
	{
		final Boat boat = new Boat(IdFactory.getInstance().getNextId(), _template);
		boat.spawnMe(itinerary);
		
		_boats.put(boat.getObjectId(), boat);
		
		return boat;
	}
	
	public static final BoatData getInstance()
	{
		return SingletonHolder.INSTANCE;
	}
	
	private static class SingletonHolder
	{
		protected static final BoatData INSTANCE = new BoatData();
	}
}