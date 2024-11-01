package com.shnok.javaserver.gameserver.data.xml;

import java.nio.file.Path;
import java.util.HashMap;
import java.util.Map;

import com.shnok.javaserver.commons.data.xml.IXmlReader;

import com.shnok.javaserver.Config;
import com.shnok.javaserver.gameserver.skills.L2Skill;

import org.w3c.dom.Document;
import org.w3c.dom.NamedNodeMap;

/**
 * This class loads and stores spellbook / skillId relation.<br>
 * TODO Could be possibly moved back on skillTrees.
 */
public class SpellbookData implements IXmlReader
{
	private final Map<Integer, Integer> _books = new HashMap<>();
	
	protected SpellbookData()
	{
		load();
	}
	
	@Override
	public void load()
	{
		parseFile("data/xml/spellbooks.xml");
		LOGGER.info("Loaded {} spellbooks.", _books.size());
	}
	
	@Override
	public void parseDocument(Document doc, Path path)
	{
		forEach(doc, "list", listNode -> forEach(listNode, "book", bookNode ->
		{
			final NamedNodeMap attrs = bookNode.getAttributes();
			_books.put(parseInteger(attrs, "skillId"), parseInteger(attrs, "itemId"));
		}));
	}
	
	public int getBookForSkill(int skillId, int level)
	{
		if (skillId == L2Skill.SKILL_DIVINE_INSPIRATION)
		{
			if (!Config.DIVINE_SP_BOOK_NEEDED)
				return 0;
			
			switch (level)
			{
				case 1:
					return 8618; // Ancient Book - Divine Inspiration (Modern Language Version)
				case 2:
					return 8619; // Ancient Book - Divine Inspiration (Original Language Version)
				case 3:
					return 8620; // Ancient Book - Divine Inspiration (Manuscript)
				case 4:
					return 8621; // Ancient Book - Divine Inspiration (Original Version)
				default:
					return 0;
			}
		}
		
		if (level != 1)
			return 0;
		
		if (!Config.SP_BOOK_NEEDED)
			return 0;
		
		if (!_books.containsKey(skillId))
			return 0;
		
		return _books.get(skillId);
	}
	
	public static SpellbookData getInstance()
	{
		return SingletonHolder.INSTANCE;
	}
	
	private static class SingletonHolder
	{
		protected static final SpellbookData INSTANCE = new SpellbookData();
	}
}