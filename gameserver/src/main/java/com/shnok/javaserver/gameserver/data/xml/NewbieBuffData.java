package com.shnok.javaserver.gameserver.data.xml;

import java.nio.file.Path;
import java.util.ArrayList;
import java.util.List;

import com.shnok.javaserver.commons.data.StatSet;
import com.shnok.javaserver.commons.data.xml.IXmlReader;

import com.shnok.javaserver.gameserver.model.records.NewbieBuff;

import org.w3c.dom.Document;

/**
 * This class loads and store {@link NewbieBuff} into a {@link List}.
 */
public class NewbieBuffData implements IXmlReader
{
	private final List<NewbieBuff> _buffs = new ArrayList<>();
	
	private int _magicLowestLevel = 100;
	private int _physicLowestLevel = 100;
	
	protected NewbieBuffData()
	{
		load();
	}
	
	@Override
	public void load()
	{
		parseFile("data/xml/newbieBuffs.xml");
		LOGGER.info("Loaded {} newbie buffs.", _buffs.size());
	}
	
	@Override
	public void parseDocument(Document doc, Path path)
	{
		forEach(doc, "list", listNode -> forEach(listNode, "buff", buffNode ->
		{
			final StatSet set = parseAttributes(buffNode);
			final int lowerLevel = set.getInteger("lowerLevel");
			if (set.getBool("isMagicClass"))
			{
				if (lowerLevel < _magicLowestLevel)
					_magicLowestLevel = lowerLevel;
			}
			else
			{
				if (lowerLevel < _physicLowestLevel)
					_physicLowestLevel = lowerLevel;
			}
			_buffs.add(new NewbieBuff(set));
		}));
	}
	
	/**
	 * @param isMage : If true, return buffs list associated to mage classes.
	 * @param level : Filter the list by the given level.
	 * @return The {@link List} of valid {@link NewbieBuff}s for the given class type and level.
	 */
	public List<NewbieBuff> getValidBuffs(boolean isMage, int level)
	{
		return _buffs.stream().filter(b -> b.isMagicClass() == isMage && level >= b.lowerLevel() && level <= b.upperLevel()).toList();
	}
	
	public int getLowestBuffLevel(boolean isMage)
	{
		return (isMage) ? _magicLowestLevel : _physicLowestLevel;
	}
	
	public static NewbieBuffData getInstance()
	{
		return SingletonHolder.INSTANCE;
	}
	
	private static class SingletonHolder
	{
		protected static final NewbieBuffData INSTANCE = new NewbieBuffData();
	}
}