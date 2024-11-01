package com.shnok.javaserver.gameserver.network.clientpackets.unused;

import com.shnok.javaserver.gameserver.data.xml.BoatData;
import com.shnok.javaserver.gameserver.enums.boats.BoatDock;
import com.shnok.javaserver.gameserver.model.actor.Boat;
import com.shnok.javaserver.gameserver.model.actor.Player;
import com.shnok.javaserver.gameserver.model.actor.container.player.BoatInfo;
import com.shnok.javaserver.gameserver.model.location.Point2D;
import com.shnok.javaserver.gameserver.network.clientpackets.L2GameClientPacket;
import com.shnok.javaserver.gameserver.network.serverpackets.combat.ActionFailed;
import com.shnok.javaserver.gameserver.network.serverpackets.unused.MoveToLocationInVehicle;

public final class RequestMoveToLocationInVehicle extends L2GameClientPacket
{
	private int _boatId;
	private int _tX;
	private int _tY;
	private int _tZ;
	private int _oX;
	private int _oY;
	private int _oZ;
	
	@Override
	protected void readImpl()
	{
		_boatId = readD();
		_tX = readD();
		_tY = readD();
		_tZ = readD();
		_oX = readD();
		_oY = readD();
		_oZ = readD();
	}
	
	@Override
	protected void runImpl()
	{
		final Player player = getClient().getPlayer();
		if (player == null)
			return;
		
		if (player.isSittingNow() || player.isSitting() || player.isStandingNow())
		{
			sendPacket(ActionFailed.STATIC_PACKET);
			return;
		}
		
		final BoatInfo info = player.getBoatInfo();
		
		info.setBoatMovement(true);
		info.setCanBoard(true);
		
		if (_tX == _oX && _tY == _oY && _tZ == _oZ)
		{
			info.stopMoveInVehicle(_boatId);
			return;
		}
		
		final boolean isInBoat = info.isInBoat();
		
		Boat boat = info.getBoat();
		if (boat == null)
		{
			boat = BoatData.getInstance().getBoat(_boatId);
			if (boat == null)
			{
				player.sendPacket(ActionFailed.STATIC_PACKET);
				return;
			}
			
			if (boat.isMoving())
			{
				player.sendPacket(ActionFailed.STATIC_PACKET);
				return;
			}
			
			final BoatDock dock = boat.getDock();
			if (dock == null)
			{
				player.sendPacket(ActionFailed.STATIC_PACKET);
				return;
			}
			
			final Point2D point = dock.getAdjustedBoardingPoint(player.getPosition(), dock.convertBoatToWorldCoordinates(_tX, _tY), isInBoat);
			
			if (player.getPosition().distance2D(point) < 50)
				moveToLocationInVehicle(player, boat);
			else
				player.moveToBoatEntrance(point, boat);
		}
		else
		{
			if (boat.getObjectId() != _boatId)
			{
				player.sendPacket(ActionFailed.STATIC_PACKET);
				return;
			}
			
			if (_tZ > -48)
				moveToLocationInVehicle(player, boat);
		}
		
		player.sendPacket(ActionFailed.STATIC_PACKET);
	}
	
	private void moveToLocationInVehicle(final Player player, Boat boat)
	{
		if (_tY > 470)
			return;
		
		player.getBoatInfo().getBoatPosition().set(_tX, _tY, _tZ);
		player.broadcastPacket(new MoveToLocationInVehicle(player, boat, _tX, _tY, _tZ, _oX, _oY, _oZ));
	}
}