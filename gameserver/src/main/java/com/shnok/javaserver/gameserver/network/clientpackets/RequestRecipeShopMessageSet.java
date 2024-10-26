package com.shnok.javaserver.gameserver.network.clientpackets;

import net.sf.l2j.gameserver.enums.actors.OperateType;
import net.sf.l2j.gameserver.model.actor.Player;

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