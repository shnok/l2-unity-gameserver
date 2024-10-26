package com.shnok.javaserver.gameserver.network.clientpackets;

import net.sf.l2j.gameserver.data.xml.BoatData;
import net.sf.l2j.gameserver.model.actor.Boat;
import net.sf.l2j.gameserver.model.actor.Player;
import net.sf.l2j.gameserver.model.actor.container.player.BoatInfo;
import net.sf.l2j.gameserver.network.serverpackets.ActionFailed;
import net.sf.l2j.gameserver.network.serverpackets.GetOnVehicle;

public final class RequestGetOnVehicle extends L2GameClientPacket
{
	private int _boatId;
	private int _x;
	private int _y;
	private int _z;
	
	@Override
	protected void readImpl()
	{
		_boatId = readD();
		_x = readD();
		_y = readD();
		_z = readD();
	}
	
	@Override
	protected void runImpl()
	{
		final Player player = getClient().getPlayer();
		if (player == null)
			return;
		
		final BoatInfo info = player.getBoatInfo();
		
		if (!info.canBoard())
		{
			sendPacket(ActionFailed.STATIC_PACKET);
			return;
		}
		final boolean isInBoat = info.isInBoat();
		final Boat boat = isInBoat ? info.getBoat() : BoatData.getInstance().getBoat(_boatId);
		
		if (boat == null || (isInBoat && boat.getObjectId() != _boatId))
		{
			sendPacket(ActionFailed.STATIC_PACKET);
			return;
		}
		
		// Summon is not allowed to board. Player can actually onboard freely.
		if (player.getSummon() != null)
			player.getSummon().unSummon(player);
		
		// Assigning boat and its coordinates to the player.
		info.setBoat(boat);
		player.setXYZ(boat.getX(), boat.getY(), boat.getZ());
		player.revalidateZone(true);
		
		boat.addPassenger(player);
		
		player.broadcastPacket(new GetOnVehicle(player.getObjectId(), boat.getObjectId(), _x, _y, _z));
	}
}