package com.shnok.javaserver.gameserver.data.xml;

import java.nio.file.Path;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

import com.shnok.javaserver.commons.data.StatSet;
import com.shnok.javaserver.commons.data.xml.IXmlReader;

import com.shnok.javaserver.gameserver.data.manager.SpawnManager;
import com.shnok.javaserver.gameserver.model.actor.Npc;
import com.shnok.javaserver.gameserver.model.actor.template.NpcTemplate;
import com.shnok.javaserver.gameserver.model.location.ObserverLocation;
import com.shnok.javaserver.gameserver.model.spawn.Spawn;

import org.w3c.dom.Document;
import org.w3c.dom.NamedNodeMap;

/**
 * This class loads and stores {@link ObserverLocation}s, which are infos related to Broadcasting Towers.
 */
public class ObserverGroupData implements IXmlReader
{
	private final Map<Integer, List<ObserverLocation>> _groups = new HashMap<>();
	private final List<Spawn> _spawns = new ArrayList<>();
	
	protected ObserverGroupData()
	{
		load();
	}
	
	@Override
	public void load()
	{
		parseFile("data/xml/observerGroups.xml");
		LOGGER.info("Loaded {} observer groups and {} spawns.", _groups.size(), _spawns.size());
	}
	
	@Override
	public void parseDocument(Document doc, Path path)
	{
		forEach(doc, "list", listNode ->
		{
			forEach(listNode, "groups", groupsNode -> forEach(groupsNode, "group", groupNode ->
			{
				final NamedNodeMap groupAttrs = groupNode.getAttributes();
				final int id = Integer.parseInt(groupAttrs.getNamedItem("id").getNodeValue());
				
				// Generate new LinkedList if that id doesn't exist yet.
				final List<ObserverLocation> group = _groups.computeIfAbsent(id, k -> new LinkedList<>());
				
				forEach(groupNode, "entry", entryNode ->
				{
					final StatSet set = parseAttributes(entryNode);
					
					group.add(new ObserverLocation(set));
				});
			}));
			
			forEach(listNode, "spawns", spawnsNode -> forEach(spawnsNode, "spawn", spawnNode ->
			{
				final NamedNodeMap spawnAttrs = spawnNode.getAttributes();
				
				final int npcId = parseInteger(spawnAttrs, "id");
				
				final NpcTemplate template = NpcData.getInstance().getTemplate(npcId);
				if (template == null)
				{
					LOGGER.warn("Template {} couldn't be found.", npcId);
					return;
				}
				
				final int x = parseInteger(spawnAttrs, "x");
				final int y = parseInteger(spawnAttrs, "y");
				final int z = parseInteger(spawnAttrs, "z");
				
				final List<Integer> groups = new LinkedList<>();
				
				for (String splittedString : parseString(spawnAttrs, "groups").split(";"))
					groups.add(Integer.parseInt(splittedString));
				
				try
				{
					final Spawn spawn = new Spawn(template);
					spawn.setLoc(x, y, z, -1);
					spawn.setRespawnDelay(10);
					
					SpawnManager.getInstance().addSpawn(spawn);
					
					_spawns.add(spawn);
					
					final Npc npc = spawn.doSpawn(false);
					npc.setObserverGroups(groups);
				}
				catch (Exception e)
				{
					LOGGER.error("Failed to initialize a spawn.", e);
				}
			}));
		});
	}
	
	public void reload()
	{
		_groups.clear();
		
		// Clear the Npc and Spawns references.
		for (Spawn spawn : _spawns)
		{
			spawn.doDelete();
			
			SpawnManager.getInstance().deleteSpawn(spawn);
		}
		_spawns.clear();
		
		load();
	}
	
	public final List<ObserverLocation> getObserverLocations(int groupId)
	{
		return _groups.get(groupId);
	}
	
	public final ObserverLocation getObserverLocation(int id)
	{
		return _groups.values().stream().flatMap(List::stream).filter(ol -> ol.getLocId() == id).findFirst().orElse(null);
	}
	
	public static ObserverGroupData getInstance()
	{
		return SingletonHolder.INSTANCE;
	}
	
	private static class SingletonHolder
	{
		protected static final ObserverGroupData INSTANCE = new ObserverGroupData();
	}
}