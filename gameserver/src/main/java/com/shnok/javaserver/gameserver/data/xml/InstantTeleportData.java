package com.shnok.javaserver.gameserver.data.xml;

import java.nio.file.Path;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.shnok.javaserver.commons.data.xml.IXmlReader;

import com.shnok.javaserver.gameserver.model.location.Location;

import org.w3c.dom.Document;
import org.w3c.dom.NamedNodeMap;

/**
 * This class loads and stores {@link Location}s used as instant teleport positions.
 */
public class InstantTeleportData implements IXmlReader
{
	private final Map<Integer, List<Location>> _teleports = new HashMap<>();
	
	protected InstantTeleportData()
	{
		load();
	}
	
	@Override
	public void load()
	{
		parseFile("data/xml/instantTeleports.xml");
		LOGGER.info("Loaded {} instant teleport positions.", _teleports.size());
	}
	
	@Override
	public void parseDocument(Document doc, Path path)
	{
		forEach(doc, "list", listNode -> forEach(listNode, "telPosList", telPosListNode ->
		{
			final NamedNodeMap telPosListAttrs = telPosListNode.getAttributes();
			final int npcId = Integer.parseInt(telPosListAttrs.getNamedItem("npcId").getNodeValue());
			
			final List<Location> teleports = new ArrayList<>();
			forEach(telPosListNode, "loc", locNode -> teleports.add(new Location(parseAttributes(locNode))));
			
			_teleports.put(npcId, teleports);
		}));
	}
	
	public void reload()
	{
		_teleports.clear();
		
		load();
	}
	
	public List<Location> getTeleports(int npcId)
	{
		return _teleports.get(npcId);
	}
	
	public static InstantTeleportData getInstance()
	{
		return SingletonHolder.INSTANCE;
	}
	
	private static class SingletonHolder
	{
		protected static final InstantTeleportData INSTANCE = new InstantTeleportData();
	}
}