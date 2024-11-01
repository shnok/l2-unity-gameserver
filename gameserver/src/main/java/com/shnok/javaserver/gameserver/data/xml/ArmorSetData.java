package com.shnok.javaserver.gameserver.data.xml;

import java.nio.file.Path;
import java.util.Collection;
import java.util.HashMap;
import java.util.Map;

import com.shnok.javaserver.commons.data.StatSet;
import com.shnok.javaserver.commons.data.xml.IXmlReader;

import com.shnok.javaserver.gameserver.model.item.ArmorSet;

import org.w3c.dom.Document;

/**
 * This class loads and stores {@link ArmorSet}s, the key being the chest item id.
 */
public class ArmorSetData implements IXmlReader
{
	private final Map<Integer, ArmorSet> _armorSets = new HashMap<>();
	
	protected ArmorSetData()
	{
		load();
	}
	
	@Override
	public void load()
	{
		parseFile("data/xml/armorSets.xml");
		LOGGER.info("Loaded {} armor sets.", _armorSets.size());
	}
	
	@Override
	public void parseDocument(Document doc, Path path)
	{
		forEach(doc, "list", listNode -> forEach(listNode, "armorset", armorsetNode ->
		{
			final StatSet set = parseAttributes(armorsetNode);
			_armorSets.put(set.getInteger("chest"), new ArmorSet(set));
		}));
	}
	
	public ArmorSet getSet(int chestId)
	{
		return _armorSets.get(chestId);
	}
	
	public Collection<ArmorSet> getSets()
	{
		return _armorSets.values();
	}
	
	public static ArmorSetData getInstance()
	{
		return SingletonHolder.INSTANCE;
	}
	
	private static class SingletonHolder
	{
		protected static final ArmorSetData INSTANCE = new ArmorSetData();
	}
}