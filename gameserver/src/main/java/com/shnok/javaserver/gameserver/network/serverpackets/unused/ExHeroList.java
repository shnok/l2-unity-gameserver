package com.shnok.javaserver.gameserver.network.serverpackets.unused;

import java.util.Collection;

import com.shnok.javaserver.commons.data.StatSet;

import com.shnok.javaserver.gameserver.data.manager.HeroManager;
import com.shnok.javaserver.gameserver.network.serverpackets.L2GameServerPacket;

public class ExHeroList extends L2GameServerPacket
{
	private final Collection<StatSet> _sets;
	
	public ExHeroList()
	{
		_sets = HeroManager.getInstance().getHeroes().values();
	}
	
	@Override
	protected void writeImpl()
	{
		writeC(0xfe);
		writeH(0x23);
		writeD(_sets.size());
		
		for (StatSet set : _sets)
		{
			writeS(set.getString(HeroManager.CHAR_NAME));
			writeD(set.getInteger(HeroManager.CLASS_ID));
			writeS(set.getString(HeroManager.CLAN_NAME, ""));
			writeD(set.getInteger(HeroManager.CLAN_CREST, 0));
			writeS(set.getString(HeroManager.ALLY_NAME, ""));
			writeD(set.getInteger(HeroManager.ALLY_CREST, 0));
			writeD(set.getInteger(HeroManager.COUNT));
		}
	}
}