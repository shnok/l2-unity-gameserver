package com.shnok.javaserver.gameserver.network.clientpackets;

import com.shnok.javaserver.gameserver.model.actor.Player;
import com.shnok.javaserver.gameserver.model.actor.container.player.BoatInfo;
import com.shnok.javaserver.gameserver.network.serverpackets.ActionFailed;

public final class CannotMoveAnymoreInVehicle extends L2GameClientPacket
{
	private int _boatId;
	private int _x;
	private int _y;
	private int _z;
	private int _heading;
	
	@Override
	protected void readImpl()
	{
		_boatId = readD();
		_x = readD();
		_y = readD();
		_z = readD();
		_heading = readD();
	}
	
	@Override
	protected void runImpl()
	{
		final Player player = getClient().getPlayer();
		if (player == null)
			return;
		
		final BoatInfo info = player.getBoatInfo();
		
		if (info.isInBoat() && info.getBoat().getObjectId() == _boatId)
		{
			info.getBoatPosition().set(_x, _y, _z, _heading);
			info.stopMoveInVehicle(_boatId);
			player.sendPacket(ActionFailed.STATIC_PACKET);
		}
	}
}