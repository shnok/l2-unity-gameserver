package com.shnok.javaserver.gameserver.network.clientpackets.unused;

import com.shnok.javaserver.gameserver.enums.actors.OperateType;
import com.shnok.javaserver.gameserver.model.actor.Player;
import com.shnok.javaserver.gameserver.network.clientpackets.L2GameClientPacket;

public class RequestRecipeShopMessageSet extends L2GameClientPacket
{
	private static final int MAX_MSG_LENGTH = 29;
	private String _name;
	
	@Override
	protected void readImpl()
	{
		_name = readS();
	}
	
	@Override
	protected void runImpl()
	{
		final Player player = getClient().getPlayer();
		if (player == null)
			return;
		
		if (_name != null && _name.length() > MAX_MSG_LENGTH)
			return;
		
		if (player.getOperateType() == OperateType.MANUFACTURE || player.getOperateType() == OperateType.MANUFACTURE_MANAGE)
			player.getManufactureList().setStoreName(_name);
	}
}