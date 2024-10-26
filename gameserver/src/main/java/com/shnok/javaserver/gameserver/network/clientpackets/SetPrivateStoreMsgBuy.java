package com.shnok.javaserver.gameserver.network.clientpackets;

import net.sf.l2j.gameserver.model.actor.Player;
import net.sf.l2j.gameserver.network.serverpackets.PrivateStoreMsgBuy;

public final class SetPrivateStoreMsgBuy extends L2GameClientPacket
{
	private static final int MAX_MSG_LENGTH = 29;
	
	private String _storeMsg;
	
	@Override
	protected void readImpl()
	{
		_storeMsg = readS();
	}
	
	@Override
	protected void runImpl()
	{
		final Player player = getClient().getPlayer();
		if (player == null || player.getBuyList() == null)
			return;
		
		// store message is limited to 29 characters.
		if (_storeMsg != null && _storeMsg.length() > MAX_MSG_LENGTH)
			return;
		
		player.getBuyList().setTitle(_storeMsg);
		player.sendPacket(new PrivateStoreMsgBuy(player));
	}
}