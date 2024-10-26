package com.shnok.javaserver.gameserver.data.xml;

import java.nio.file.Path;
import java.util.ArrayList;
import java.util.List;

import com.shnok.javaserver.commons.data.xml.IXmlReader;

import com.shnok.javaserver.gameserver.model.records.ClanHallDeco;

import org.w3c.dom.Document;

/**
 * This class loads and stores {@link ClanHallDeco}s infos.
 */
public class ClanHallDecoData implements IXmlReader
{
	private final List<ClanHallDeco> _decos = new ArrayList<>();
	
	protected ClanHallDecoData()
	{
		load();
	}
	
	@Override
	public void load()
	{
		parseFile("data/xml/clanHallDeco.xml");
		LOGGER.info("Loaded {} clan halls decos.", _decos.size());
	}
	
	@Override
	public void parseDocument(Document doc, Path path)
	{
		forEach(doc, "list", listNode -> forEach(listNode, "deco", chdNode -> _decos.add(new ClanHallDeco(parseAttributes(chdNode)))));
	}
	
	public final int getDecoFee(int type, int level)
	{
		final ClanHallDeco deco = _decos.stream().filter(d -> d.type() == type && d.level() == level).findFirst().orElse(null);
		return (deco != null) ? deco.price() : 0;
	}
	
	public final int getDecoDays(int type, int level)
	{
		final ClanHallDeco deco = _decos.stream().filter(d -> d.type() == type && d.level() == level).findFirst().orElse(null);
		return (deco != null) ? deco.days() : 0;
	}
	
	public final int getDecoDepth(int type, int level)
	{
		final ClanHallDeco deco = _decos.stream().filter(d -> d.type() == type && d.level() == level).findFirst().orElse(null);
		return (deco != null) ? deco.depth() : 0;
	}
	
	public static ClanHallDecoData getInstance()
	{
		return SingletonHolder.INSTANCE;
	}
	
	private static class SingletonHolder
	{
		protected static final ClanHallDecoData INSTANCE = new ClanHallDecoData();
	}
}