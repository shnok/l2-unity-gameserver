package com.shnok.javaserver.gameserver.network.clientpackets;

import net.sf.l2j.gameserver.data.xml.HennaData;
import net.sf.l2j.gameserver.model.actor.Player;
import net.sf.l2j.gameserver.model.records.Henna;
import net.sf.l2j.gameserver.network.serverpackets.HennaItemUnequipInfo;

public final class RequestHennaUnequipInfo extends L2GameClientPacket
{
	private int _symbolId;
	
	@Override
	protected void readImpl()
	{
		_symbolId = readD();
	}
	
	@Override
	protected void runImpl()
	{
		final Player player = getClient().getPlayer();
		if (player == null)
			return;
		
		final Henna henna = HennaData.getInstance().getHenna(_symbolId);
		if (henna == null)
			return;
		
		player.sendPacket(new HennaItemUnequipInfo(henna, player));
	}
}