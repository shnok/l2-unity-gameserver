package com.shnok.javaserver.gameserver.data.xml;

import java.nio.file.Path;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.shnok.javaserver.commons.data.xml.IXmlReader;

import com.shnok.javaserver.gameserver.model.location.WalkerLocation;

import org.w3c.dom.Document;
import org.w3c.dom.NamedNodeMap;

/**
 * This class loads and stores routes for walking NPCs, under a List of {@link WalkerLocation} ; the key being the npcId.
 */
public class WalkerRouteData implements IXmlReader
{
	private final Map<String, Map<String, List<WalkerLocation>>> _routes = new HashMap<>();
	
	protected WalkerRouteData()
	{
		load();
	}
	
	@Override
	public void load()
	{
		parseFile("data/xml/walkerRoutes.xml");
		LOGGER.info("Loaded {} walking routes.", _routes.size());
	}
	
	@Override
	public void parseDocument(Document doc, Path path)
	{
		forEach(doc, "list", listNode -> forEach(listNode, "route", routeNode ->
		{
			final NamedNodeMap routeAttrs = routeNode.getAttributes();
			final String routeName = parseString(routeAttrs, "name");
			final Map<String, List<WalkerLocation>> routeList = new HashMap<>();
			
			forEach(routeNode, "npc", npcNode ->
			{
				final NamedNodeMap npcAttrs = npcNode.getAttributes();
				final String npcName = parseString(npcAttrs, "name");
				final List<WalkerLocation> nodeList = new ArrayList<>();
				
				forEach(npcNode, "node", nodeNode -> nodeList.add(new WalkerLocation(parseAttributes(nodeNode))));
				routeList.put(npcName, nodeList);
			});
			_routes.put(routeName, routeList);
		}));
	}
	
	public void reload()
	{
		_routes.clear();
		
		load();
	}
	
	public Map<String, Map<String, List<WalkerLocation>>> getWalkerRoutes()
	{
		return _routes;
	}
	
	public List<WalkerLocation> getWalkerRoute(String routeName, String npcName)
	{
		final Map<String, List<WalkerLocation>> npcRoutes = _routes.get(routeName);
		if (npcRoutes == null)
			return Collections.emptyList();
		
		return npcRoutes.get(npcName);
	}
	
	public static WalkerRouteData getInstance()
	{
		return SingletonHolder.INSTANCE;
	}
	
	private static class SingletonHolder
	{
		protected static final WalkerRouteData INSTANCE = new WalkerRouteData();
	}
}