package com.shnok.javaserver.gameserver.network.clientpackets.movement;

import java.awt.Color;

import com.shnok.javaserver.gameserver.enums.TeleportMode;
import com.shnok.javaserver.gameserver.enums.actors.MoveType;
import com.shnok.javaserver.gameserver.model.World;
import com.shnok.javaserver.gameserver.model.WorldRegion;
import com.shnok.javaserver.gameserver.model.actor.Player;
import com.shnok.javaserver.gameserver.model.actor.container.player.BoatInfo;
import com.shnok.javaserver.gameserver.model.location.SpawnLocation;
import com.shnok.javaserver.gameserver.network.clientpackets.L2GameClientPacket;
import com.shnok.javaserver.gameserver.network.serverpackets.unused.ExServerPrimitive;
import com.shnok.javaserver.gameserver.network.serverpackets.unused.GetOnVehicle;
import com.shnok.javaserver.gameserver.network.serverpackets.movement.ValidateLocation;

public class ValidatePosition extends L2GameClientPacket
{
	private int _x;
	private int _y;
	private int _z;
	@SuppressWarnings("unused")
	private int _heading;
	private int _boatId;
	
	@Override
	protected void readImpl()
	{
		_x = readD();
		_y = readD();
		_z = readD();
		_heading = readD();
		_boatId = readD();
	}
	
	@Override
	protected void runImpl()
	{
		final Player player = getClient().getPlayer();
		if (player == null || player.isTeleporting() || player.isInObserverMode())
			return;
		
		// Disable validation for CameraMode.
		if (player.getTeleportMode() == TeleportMode.CAMERA_MODE)
		{
			// Retrieve the current WorldRegion passed by the client location, and set it. It allows knownlist to be properly refreshed.
			final WorldRegion region = World.getInstance().getRegion(_x, _y);
			if (region != null)
				player.setRegion(region);
			
			player.setXYZ(_x, _y, _z);
			return;
		}
		
		// Disable validation during fall to avoid "jumping".
//		if (player.isFalling(_z))
//			return;
		
		final float actualSpeed;
		final double dist;
		
		final BoatInfo info = player.getBoatInfo();
		// Send back position if client<>server desync is too big. For boats, send back if the desync is bigger than 500.
		if (info.isInBoat())
		{
			final SpawnLocation pos = info.getBoatPosition();
			actualSpeed = 500;
			dist = pos.distance2D(_x, _y);
			
			if (dist > actualSpeed)
				sendPacket(new GetOnVehicle(player.getObjectId(), _boatId, pos));
		}
		// For regular movement, send back if the desync is bigger than actual speed.
		else
		{
			actualSpeed = player.getStatus().getMoveSpeed();
			dist = (player.getMove().getMoveType() == MoveType.GROUND) ? player.getPosition().distance2D(_x, _y) : player.getPosition().distance3D(_x, _y, _z);

			System.out.println("Player position error delta: " + dist);
			if (dist > actualSpeed && !info.isBoatMovement())
				sendPacket(new ValidateLocation(player));
		}
		
		// Draw a debug of this packet if activated.
		if (player.getMove().isDebugMove())
		{
			final String desc = "speed=" + actualSpeed + " desync=" + dist;
			
			// Draw debug packet to all players.
			player.forEachKnownGM(p ->
			{
				// Get debug packet.
				final ExServerPrimitive debug = p.getDebugPacket("MOVE" + player.getObjectId());
				debug.addPoint(desc, Color.GREEN, true, _x, _y, _z);
				debug.sendTo(p);
			});
		}
	}
}