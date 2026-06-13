package com.shnok.javaserver.gameserver.data.xml;

import java.nio.file.Path;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.shnok.javaserver.commons.data.StatSet;
import com.shnok.javaserver.commons.data.xml.IXmlReader;

import com.shnok.javaserver.gameserver.model.actor.Player;
import com.shnok.javaserver.gameserver.model.records.Henna;

import org.w3c.dom.Document;

/**
 * This class loads and stores {@link Henna}s infos. Hennas are called "dye" ingame.
 */
public class HennaData implements IXmlReader
{
	private final Map<Integer, Henna> _hennas = new HashMap<>();
	
	protected HennaData()
	{
		load();
	}
	
	@Override
	public void load()
	{
		parseFile("data/xml/hennas.xml");
		LOGGER.info("Loaded {} hennas.", _hennas.size());
	}
	
	@Override
	public void parseDocument(Document doc, Path path)
	{
		forEach(doc, "list", listNode -> forEach(listNode, "henna", hennaNode ->
		{
			final StatSet set = parseAttributes(hennaNode);
			_hennas.put(set.getInteger("symbolId"), new Henna(set));
		}));
	}
	
	public Collection<Henna> getHennas()
	{
		return _hennas.values();
	}
	
	public Henna getHenna(int id)
	{
		return _hennas.get(id);
	}
	
	/**
	 * Retrieve all {@link Henna}s available for a {@link Player} class.
	 * @param player : The Player used as class parameter.
	 * @return a List of all available Hennas for this Player.
	 */
	public List<Henna> getAvailableHennasFor(Player player)
	{
		return _hennas.values().stream().filter(h -> h.canBeUsedBy(player)).toList();
	}
	
	public static HennaData getInstance()
	{
		return SingletonHolder.INSTANCE;
	}
	
	private static class SingletonHolder
	{
		protected static final HennaData INSTANCE = new HennaData();
	}
}