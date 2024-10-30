package com.shnok.javaserver.gameserver.network.clientpackets.movement.legacy;

import java.nio.BufferUnderflowException;

import com.shnok.javaserver.commons.math.MathUtil;

import com.shnok.javaserver.Config;
import com.shnok.javaserver.gameserver.enums.TeleportMode;
import com.shnok.javaserver.gameserver.enums.boats.BoatDock;
import com.shnok.javaserver.gameserver.model.actor.Boat;
import com.shnok.javaserver.gameserver.model.actor.Player;
import com.shnok.javaserver.gameserver.model.actor.container.player.BoatInfo;
import com.shnok.javaserver.gameserver.model.location.Location;
import com.shnok.javaserver.gameserver.model.location.Point2D;
import com.shnok.javaserver.gameserver.model.location.SpawnLocation;
import com.shnok.javaserver.gameserver.network.SystemMessageId;
import com.shnok.javaserver.gameserver.network.clientpackets.L2GameClientPacket;
import com.shnok.javaserver.gameserver.network.serverpackets.combat.ActionFailed;
import com.shnok.javaserver.gameserver.network.serverpackets.movement.MoveToLocation;
import com.shnok.javaserver.gameserver.network.serverpackets.unused.MoveToLocationInVehicle;

public class MoveBackwardToLocation extends L2GameClientPacket
{
	private static final Point2D CENTER_BOAT = new Point2D(0, -100);
	
	private int _targetX;
	private int _targetY;
	private int _targetZ;
	private int _originX;
	private int _originY;
	private int _originZ;
	private int _moveMovement;
	
	@Override
	protected void readImpl()
	{
		_targetX = readD();
		_targetY = readD();
		_targetZ = readD();
		_originX = readD();
		_originY = readD();
		_originZ = readD();
		
		try
		{
			_moveMovement = readD(); // is 0 if cursor keys are used 1 if mouse is used
		}
		catch (BufferUnderflowException e)
		{
			if (Config.L2WALKER_PROTECTION)
			{
				final Player player = getClient().getPlayer();
				if (player != null)
					player.logout(false);
			}
		}
	}
	
	@Override
	protected void runImpl()
	{
		final Player player = getClient().getPlayer();
		if (player == null)
			return;
		
		final BoatInfo info = player.getBoatInfo();
		
		// Deny movement from arrow keys.
		if (_moveMovement == 0)
		{
			player.sendPacket(ActionFailed.STATIC_PACKET);
			return;
		}
		
		// If Player can't be controlled, forget it.
		if (player.isOutOfControl())
		{
			player.sendPacket(ActionFailed.STATIC_PACKET);
			return;
		}
		
		// If Player can't move, forget it.
		if (player.getStatus().getMoveSpeed() == 0)
		{
			player.sendPacket(ActionFailed.STATIC_PACKET);
			player.sendPacket(SystemMessageId.CANT_MOVE_TOO_ENCUMBERED);
			return;
		}
		
		// Cancel enchant over movement.
		player.cancelActiveEnchant();
		
		// Correct targetZ from floor level to head level.
		_targetZ += player.getCollisionHeight();
		
		// If under teleport mode, teleport instead of tryToMove.
		switch (player.getTeleportMode())
		{
			case ONE_TIME:
				player.setTeleportMode(TeleportMode.NONE);
			case FULL_TIME:
				player.sendPacket(ActionFailed.STATIC_PACKET);
				player.teleportTo(_targetX, _targetY, _targetZ, 0);
				return;
		}
		
		// Generate a Location based on target coords.
		final Location targetLoc = new Location(_targetX, _targetY, _targetZ);
		
		// If we target past 9900 distance, forget it.
		if (!targetLoc.isIn3DRadius(_originX, _originY, _originZ, 9900))
		{
			player.sendPacket(ActionFailed.STATIC_PACKET);
			return;
		}
		
		final boolean isInBoat = info.isInBoat();
		
		// If out of Boat, register a move Intention.
		if (!isInBoat)
		{
			if (player.tryToPassBoatEntrance(targetLoc))
				return;
			
			info.setCanBoard(false);
			player.getAI().tryToMoveTo(targetLoc, null);
		}
		// Player is on the boat, we don't want to schedule a real movement until he gets out of it otherwise GeoEngine will be confused.
		else
		{
			// We want to set the real player heading though so it can be used during actual departure.
			player.getPosition().setHeading(MathUtil.calculateHeadingFrom(_originX, _originY, _targetX, _targetY));
			
			final Boat boat = info.getBoat();
			if (boat == null)
			{
				sendPacket(ActionFailed.STATIC_PACKET);
				return;
			}
			
			final BoatDock dock = boat.getDock();
			if (dock == null)
			{
				sendPacket(ActionFailed.STATIC_PACKET);
				return;
			}
			
			final boolean isMoving = boat.isMoving();
			
			final Point2D targetPoint = new Point2D(_targetX, _targetY);
			final Point2D originPoint = new Point2D(_originX, _originY);
			
			// Check if there is an intersection point with the boat entrance.
			Point2D boardingPoint = dock.getBoardingPoint(originPoint, targetPoint, isInBoat);
			
			// If not, check if there is an intersection point with the boat exit.
			if (boardingPoint == null)
				boardingPoint = BoatDock.getBoardingPoint(dock.getBoatExit(), originPoint, targetPoint, isInBoat);
			
			// No intersection point found, if the boat is docked do nothing.
			if (boardingPoint == null && !isMoving)
			{
				sendPacket(ActionFailed.STATIC_PACKET);
				return;
			}
			
			final Location pos = info.getBoatPosition();
			final int oX = pos.getX();
			final int oY = pos.getY();
			final int z = pos.getZ();
			
			info.setBoatMovement(true);
			
			final double distToBorder = isMoving ? 400 : originPoint.distance2D(boardingPoint);
			
			if (boardingPoint != null && distToBorder < 90)
			{
				// Just sending a client move packet so player will try to move towards exit.
				player.broadcastPacket(new MoveToLocation(player, new Location(boardingPoint.getX(), boardingPoint.getY(), -3624)));
				info.setBoatMovement(false);
				info.setCanBoard(false);
				return;
			}
			
			final SpawnLocation boatPos = info.getBoatPosition();
			final Point2D currentPoint = new Point2D(boatPos.getX(), boatPos.getY());
			
			final double distToCenter = CENTER_BOAT.distance2D(currentPoint);
			
			if (distToCenter > 350)
			{
				sendPacket(ActionFailed.STATIC_PACKET);
				return;
			}
			
			if (!isMoving && distToBorder > 200 && distToCenter > 250)
			{
				sendPacket(ActionFailed.STATIC_PACKET);
				return;
			}
			
			if (isMoving && distToCenter < 250)
			{
				player.broadcastPacket(new MoveToLocation(player, new Location(_targetX, _targetY, _targetZ)));
				info.setBoatMovement(false);
				info.setCanBoard(false);
				return;
			}
			
			if (boardingPoint != null)
			{
				boardingPoint = dock.convertWorldToBoatCoordinates(boardingPoint.getX(), boardingPoint.getY());
				
				final int tX = boardingPoint.getX();
				final int tY = boardingPoint.getY();
				
				player.broadcastPacket(new MoveToLocationInVehicle(player, boat, tX, tY, z, oX, oY, z));
				info.setBoatMovement(false);
				info.setCanBoard(false);
			}
			
			sendPacket(ActionFailed.STATIC_PACKET);
		}
	}
}