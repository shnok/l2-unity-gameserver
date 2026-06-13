package com.shnok.javaserver.gameserver.data.xml;

import java.nio.file.Path;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.shnok.javaserver.commons.data.xml.IXmlReader;

import com.shnok.javaserver.gameserver.model.location.TeleportLocation;

import org.w3c.dom.Document;
import org.w3c.dom.NamedNodeMap;

/**
 * This class loads and stores {@link TeleportLocation}s used as regular teleport positions.
 */
public class TeleportData implements IXmlReader
{
	private final Map<Integer, List<TeleportLocation>> _teleports = new HashMap<>();
	
	protected TeleportData()
	{
		load();
	}
	
	@Override
	public void load()
	{
		parseFile("data/xml/teleports.xml");
		LOGGER.info("Loaded {} teleport positions.", _teleports.size());
	}
	
	@Override
	public void parseDocument(Document doc, Path path)
	{
		forEach(doc, "list", listNode -> forEach(listNode, "telPosList", telPosListNode ->
		{
			final NamedNodeMap telPosListAttrs = telPosListNode.getAttributes();
			final int npcId = Integer.parseInt(telPosListAttrs.getNamedItem("npcId").getNodeValue());
			
			final List<TeleportLocation> teleports = new ArrayList<>();
			forEach(telPosListNode, "loc", locNode -> teleports.add(new TeleportLocation(parseAttributes(locNode))));
			
			_teleports.put(npcId, teleports);
		}));
	}
	
	public void reload()
	{
		_teleports.clear();
		
		load();
	}
	
	public List<TeleportLocation> getTeleports(int npcId)
	{
		return _teleports.get(npcId);
	}
	
	public static TeleportData getInstance()
	{
		return SingletonHolder.INSTANCE;
	}
	
	private static class SingletonHolder
	{
		protected static final TeleportData INSTANCE = new TeleportData();
	}
}