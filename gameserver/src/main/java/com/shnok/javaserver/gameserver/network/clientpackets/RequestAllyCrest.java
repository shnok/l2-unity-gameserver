package com.shnok.javaserver.gameserver.network.clientpackets;

import net.sf.l2j.gameserver.data.cache.CrestCache;
import net.sf.l2j.gameserver.enums.CrestType;
import net.sf.l2j.gameserver.model.actor.Player;
import net.sf.l2j.gameserver.network.serverpackets.AllyCrest;

public final class RequestAllyCrest extends L2GameClientPacket
{
	private int _crestId;
	
	@Override
	protected void readImpl()
	{
		_crestId = readD();
	}
	
	@Override
	protected void runImpl()
	{
		final Player player = getClient().getPlayer();
		if (player == null)
			return;
		
		final byte[] data = CrestCache.getInstance().getCrest(CrestType.ALLY, _crestId);
		if (data == null)
			return;
		
		player.sendPacket(new AllyCrest(_crestId, data));
	}
}