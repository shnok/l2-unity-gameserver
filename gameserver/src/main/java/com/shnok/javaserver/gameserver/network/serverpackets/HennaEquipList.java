package com.shnok.javaserver.gameserver.network.serverpackets;

import java.util.List;

import net.sf.l2j.gameserver.data.xml.HennaData;
import net.sf.l2j.gameserver.model.actor.Player;
import net.sf.l2j.gameserver.model.records.Henna;

public class HennaEquipList extends L2GameServerPacket
{
	private final int _adena;
	private final int _maxHennas;
	private final List<Henna> _availableHennas;
	
	public HennaEquipList(Player player)
	{
		_adena = player.getAdena();
		_maxHennas = player.getHennaList().getMaxSize();
		_availableHennas = HennaData.getInstance().getHennas().stream().filter(h -> h.canBeUsedBy(player) && player.getInventory().getItemByItemId(h.dyeId()) != null).toList();
	}
	
	@Override
	protected final void writeImpl()
	{
		writeC(0xe2);
		writeD(_adena);
		writeD(_maxHennas);
		writeD(_availableHennas.size());
		
		for (Henna temp : _availableHennas)
		{
			writeD(temp.symbolId());
			writeD(temp.dyeId());
			writeD(Henna.DRAW_AMOUNT);
			writeD(temp.drawPrice());
			writeD(1);
		}
	}
}