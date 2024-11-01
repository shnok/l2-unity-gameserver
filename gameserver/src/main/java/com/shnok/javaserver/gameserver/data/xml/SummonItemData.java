package com.shnok.javaserver.gameserver.data.xml;

import java.nio.file.Path;
import java.util.HashMap;
import java.util.Map;

import com.shnok.javaserver.commons.data.xml.IXmlReader;

import com.shnok.javaserver.gameserver.model.holder.IntIntHolder;

import org.w3c.dom.Document;
import org.w3c.dom.NamedNodeMap;

/**
 * This class loads and stores summon items.<br>
 * TODO Delete it and move it back wherever it belongs.
 */
public class SummonItemData implements IXmlReader
{
	private final Map<Integer, IntIntHolder> _items = new HashMap<>();
	
	protected SummonItemData()
	{
		load();
	}
	
	@Override
	public void load()
	{
		parseFile("data/xml/summonItems.xml");
		LOGGER.info("Loaded {} summon items.", _items.size());
	}
	
	@Override
	public void parseDocument(Document doc, Path path)
	{
		forEach(doc, "list", listNode -> forEach(listNode, "item", itemNode ->
		{
			final NamedNodeMap attrs = itemNode.getAttributes();
			final int itemId = parseInteger(attrs, "id");
			final int npcId = parseInteger(attrs, "npcId");
			final int summonType = parseInteger(attrs, "summonType");
			_items.put(itemId, new IntIntHolder(npcId, summonType));
		}));
	}
	
	public IntIntHolder getSummonItem(int itemId)
	{
		return _items.get(itemId);
	}
	
	public static SummonItemData getInstance()
	{
		return SingletonHolder.INSTANCE;
	}
	
	private static class SingletonHolder
	{
		protected static final SummonItemData INSTANCE = new SummonItemData();
	}
}