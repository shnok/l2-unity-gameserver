package com.shnok.javaserver.gameserver.network.serverpackets;

import java.util.List;

import net.sf.l2j.gameserver.enums.actors.HennaType;
import net.sf.l2j.gameserver.model.actor.Player;
import net.sf.l2j.gameserver.model.actor.container.player.HennaList;
import net.sf.l2j.gameserver.model.records.Henna;

public final class HennaInfo extends L2GameServerPacket
{
	private final HennaList _hennaList;
	
	public HennaInfo(Player player)
	{
		_hennaList = player.getHennaList();
	}
	
	@Override
	protected final void writeImpl()
	{
		writeC(0xe4);
		
		writeC(_hennaList.getStat(HennaType.INT));
		writeC(_hennaList.getStat(HennaType.STR));
		writeC(_hennaList.getStat(HennaType.CON));
		writeC(_hennaList.getStat(HennaType.MEN));
		writeC(_hennaList.getStat(HennaType.DEX));
		writeC(_hennaList.getStat(HennaType.WIT));
		
		writeD(_hennaList.getMaxSize());
		
		final List<Henna> hennas = _hennaList.getHennas();
		writeD(hennas.size());
		for (Henna h : hennas)
		{
			writeD(h.symbolId());
			writeD((_hennaList.canBeUsedBy(h)) ? h.symbolId() : 0);
		}
	}
}