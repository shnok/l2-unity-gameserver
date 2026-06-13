package com.shnok.javaserver.gameserver.network.serverpackets.unused;

import java.util.List;

import com.shnok.javaserver.gameserver.enums.actors.HennaType;
import com.shnok.javaserver.gameserver.model.actor.Player;
import com.shnok.javaserver.gameserver.model.actor.container.player.HennaList;
import com.shnok.javaserver.gameserver.model.records.Henna;
import com.shnok.javaserver.gameserver.network.serverpackets.L2GameServerPacket;

public class GMHennaInfo extends L2GameServerPacket
{
	private final HennaList _hennaList;
	
	public GMHennaInfo(Player player)
	{
		_hennaList = player.getHennaList();
	}
	
	@Override
	protected void writeImpl()
	{
		writeC(0xea);
		
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