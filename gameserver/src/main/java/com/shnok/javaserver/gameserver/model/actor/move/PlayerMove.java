package com.shnok.javaserver.gameserver.model.actor.move;

import java.time.Duration;
import java.time.Instant;
import java.util.List;

import com.shnok.javaserver.gameserver.enums.actors.MoveType;
import com.shnok.javaserver.gameserver.geoengine.GeoEngine;
import com.shnok.javaserver.gameserver.geoengine.geodata.GeoStructure;
import com.shnok.javaserver.gameserver.handler.usercommandhandlers.Loc;
import com.shnok.javaserver.gameserver.model.World;
import com.shnok.javaserver.gameserver.model.WorldObject;
import com.shnok.javaserver.gameserver.model.actor.Creature;
import com.shnok.javaserver.gameserver.model.actor.Player;
import com.shnok.javaserver.gameserver.model.location.Location;
import com.shnok.javaserver.gameserver.network.serverpackets.combat.ActionAllowed;
import com.shnok.javaserver.gameserver.network.serverpackets.movement.MoveDirection;
import com.shnok.javaserver.gameserver.network.serverpackets.unused.ExServerPrimitive;

/**
 * This class groups all movement data related to a {@link Player}.
 */
public class PlayerMove extends CreatureMove<Player>
{
	private volatile Instant _instant;
	
	private int _moveTimeStamp;
	private double _zAccurate;
	private Location _moveDirection;
	
	public PlayerMove(Player actor)
	{
		super(actor);
	}
	
	@Override
	public void cancelMoveTask()
	{
		super.cancelMoveTask();
		
		_moveTimeStamp = 0;
		_moveDirection = new Location(0,0,0);
	}
	
	private void moveToPawn(WorldObject pawn, int offset)
	{
//		// Get the current position of the pawn.
//		final int tx = pawn.getX();
//		final int ty = pawn.getY();
//		final int tz = pawn.getZ();
//
//		// Set the pawn and offset.
//		_pawn = pawn;
//		_offset = offset;
//
//		if (_task != null)
//			updatePosition(true);
//
//		_instant = Instant.now();
//
//		// Get the current position of the actor.
//		final int ox = _actor.getX();
//		final int oy = _actor.getY();
//		final int oz = _actor.getZ();
//
//		// Set the current x/y/z.
//		_xAccurate = ox;
//		_yAccurate = oy;
//		_zAccurate = oz;
//
//		// Initialize variables.
//		_geoPath.clear();
//
//		// Draw a debug of this movement if activated.
//		if (_isDebugMove)
//		{
//			// Draw debug packet to surrounding GMs.
//			_actor.forEachKnownGM(p ->
//			{
//				// Get debug packet.
//				final ExServerPrimitive debug = p.getDebugPacket("MOVE" + _actor.getObjectId());
//
//				// Reset the packet lines and points.
//				debug.reset();
//
//				// Add a RED point corresponding to initial start location.
//				debug.addPoint(Color.RED, ox, oy, oz);
//
//				// Add a WHITE line corresponding to the initial click release.
//				debug.addLine("MoveToPawn (" + _offset + "): " + tx + " " + ty + " " + tz, Color.WHITE, true, ox, oy, oz, tx, ty, tz);
//
//				p.sendMessage("Moving from " + ox + " " + oy + " " + oz + " to " + tx + " " + ty + " " + tz);
//			});
//		}
//
//		// Set the destination.
//		_destination.set(tx, ty, tz);
//
//		_actor.getPosition().setHeadingTo(tx, ty);
//
//		registerMoveTask();
//
//		_actor.broadcastPacket(new MoveToPawn(_actor, pawn, offset));
	}
	
	@Override
	protected void moveToLocation(Location moveDirection, boolean pathfinding)
	{
		if (_task != null)
			updatePosition(true);

		_moveDirection = moveDirection;
		_instant = Instant.now();

		// Get the current position of the Creature.
		final Location position = _actor.getPosition().clone();

		// Set the current x/y/z.
		_xAccurate = position.getX();
		_yAccurate = position.getY();
		_zAccurate = position.getZ();

		final Location destination = new Location(
				position.getX() + moveDirection.getX() * 50,
				position.getY() + moveDirection.getY() * 50,
				position.getZ());

//		System.out.println("Destination: " + destination);
		System.out.println("Movedirection: " + _moveDirection);

		// Set the destination.
		_destination.set(destination);

		// Calculate the heading.
		_actor.getPosition().setHeadingTo(destination);

		registerMoveTask();

		_actor.sendPacket(ActionAllowed.STATIC_PACKET);

		_actor.broadcastPacket(new MoveDirection(_actor, moveDirection), false);
	}
	
	@Override
	public boolean updatePosition(boolean firstRun)
	{
		if (_task == null || !_actor.isVisible())
			return true;

		// Save current Instant.
		final Instant instant = Instant.now();

		// Compare tested and saved Instants.
		long timePassed = Duration.between(_instant, instant).toMillis();
		if (timePassed == 0)
			timePassed = 1;

		_instant = instant;

		final MoveType type = getMoveType();

//		final boolean canBypassZCheck = _actor.getBoatInfo().getBoat() != null || type == MoveType.FLY;

		// Increment the timestamp.
		_moveTimeStamp++;

		final int curX = _actor.getX();
		final int curY = _actor.getY();
		final int curZ = _actor.getZ();

		if (_pawn != null && !firstRun)
			_destination.set(_pawn.getPosition());

		if (type == MoveType.GROUND)
			_destination.setZ(GeoEngine.getInstance().getHeight(_destination));

		final double dx = _destination.getX() - curX;
		final double dy = _destination.getY() - curY;
		final double dz = _destination.getZ() - curZ;

		// We use Z for delta calculation only if different of GROUND MoveType.
		final double leftDistance = (type == MoveType.GROUND) ? Math.sqrt(dx * dx + dy * dy) : Math.sqrt(dx * dx + dy * dy + dz * dz);
		float speed = _actor.getStatus().getRealMoveSpeed(!_actor.isRunning());

		final double passedDistance = speed / (1000d / timePassed);

//		// Calculate the maximum Z. Only FLY is allowed to bypass Z check.
		int maxZ = World.WORLD_Z_MAX;

		final int nextX;
		final int nextY;
		final int nextZ;
//		System.out.println(Thread.currentThread().threadId() + " - TIME PASSED: " + timePassed + " dx:" + dx + " dy:" + dy + " dz:" + dz + "Speed: " + speed + " UnitySpd: " + speed / 52.5f);

		// Set the position only
		if (passedDistance < leftDistance)
		{
			// Calculate the current distance fraction based on the delta.
			final double fraction = passedDistance / leftDistance;

			_xAccurate += dx * fraction;
			_yAccurate += dy * fraction;
			_zAccurate += dz * fraction;

			// Note: Z coord shifted up to avoid dual-layer issues.
			nextX = (int) Math.round(_xAccurate);
			nextY = (int) Math.round(_yAccurate);
			nextZ = Math.min((type == MoveType.GROUND) ? GeoEngine.getInstance().getHeight(nextX, nextY, curZ + 2 * GeoStructure.CELL_HEIGHT) : (int) Math.round(_zAccurate), maxZ);
		}
		// Already there : set the position to the destination.
		else
		{
			nextX = _destination.getX();
			nextY = _destination.getY();
			nextZ = Math.min(_destination.getZ(), maxZ);
		}

//		System.out.println("Movedirection: " + _moveDirection);

		// Check if location can be reached (case of dynamic objects, such as opening doors/fences).
		if (type == MoveType.GROUND && !GeoEngine.getInstance().canMoveToTarget(
				curX, curY, curZ,
				nextX - _moveDirection.getX() / 10, nextY - _moveDirection.getY() / 10, nextZ))
		{
			System.out.println("Player running through a wall!");
			_blocked = true;
			return true;
		}

		// Calculate the heading. Must be computed BEFORE setting setXYZ, otherwise ends to 0.
		if (_pawn != null)
			_actor.getPosition().setHeadingTo(nextX, nextY);

		// Set the position of the Creature.
		_actor.setXYZ(nextX, nextY, nextZ);

//		System.out.println("Updated position to: (" + _actor.getPosition().getY()/52.5f + "," + + _actor.getPosition().getZ()/52.5f + "," + + _actor.getPosition().getX()/52.5f + ")");

		_actor.revalidateZone(false);
//
//		if (isOnLastPawnMoveGeoPath() && ((type == MoveType.GROUND) ? _actor.isIn2DRadius(_pawn, _offset) : _actor.isIn3DRadius(_pawn, _offset)))
//			return true;
//
		return (passedDistance >= leftDistance);
	}
	
	/**
	 * @param target : The WorldObject we try to reach.
	 * @param offset : The interact area radius.
	 * @param isShiftPressed : If movement is necessary, it disallows it.
	 * @return true if a movement must be done to reach the {@link WorldObject}, based on an offset.
	 */
	public boolean maybeMoveToPawn(WorldObject target, int offset, boolean isShiftPressed)
	{
		if (offset < 0 || _actor == target)
			return false;
//
		if (_actor.isIn3DRadius(target, (int) (offset + _actor.getCollisionRadius() + ((target instanceof Creature targetCreature) ? targetCreature.getCollisionRadius() : 0))))
			return false;

		System.out.println("maybeMoveToPawn");

		return true;
//
//		if (!_actor.isMovementDisabled() && !isShiftPressed)
//		{
//			_pawn = target;
//			_offset = offset;
//
//			moveToPawn(target, offset);
//		}
//
//		return true;
//		return isShiftPressed;
	}
	
	@Override
	protected void offensiveFollowTask(Creature target, int offset)
	{
		System.out.println("Player offensive follow task");
//		// No follow task, return.
//		if (_followTask == null)
//			return;
//
//		// Pawn isn't registered on knownlist.
//		if (!_actor.knows(target))
//		{
//			_actor.getAI().tryToIdle();
//			return;
//		}
//
//		final int realOffset = (int) (offset + _actor.getCollisionRadius() + target.getCollisionRadius());
//		if ((getMoveType() == MoveType.GROUND) ? _actor.isIn2DRadius(target, realOffset) : _actor.isIn3DRadius(target, realOffset))
//			return;
//
//		// If an obstacle is/appears while the _followTask is running (ex: door closing) between the Player and the pawn, move to latest good location.
//		final Location moveOk = GeoEngine.getInstance().getValidLocation(_actor, target);
//		final boolean isPathClear = MathUtil.checkIfInRange(offset, target, moveOk, true);
//		if (isPathClear)
//		{
//			_pawn = target;
//			_offset = offset;
//
//			moveToPawn(target, offset);
//		}
//		else
//		{
//			_pawn = null;
//			_offset = 0;
//
//			moveToLocation(moveOk, false);
//		}
	}
	
	@Override
	protected void friendlyFollowTask(Creature target, int offset)
	{
		System.out.println("Player friendlyFollowTask");

//		// No follow task, return.
//		if (_followTask == null)
//			return;
//
//		// Invalid pawn to follow, or the pawn isn't registered on knownlist.
//		if (!_actor.knows(target))
//		{
//			_actor.getAI().tryToIdle();
//			return;
//		}
//
//		// Don't bother moving if already in radius.
//		if ((getMoveType() == MoveType.GROUND) ? _actor.isIn2DRadius(target, offset) : _actor.isIn3DRadius(target, offset))
//			return;
//
//		if (_task == null)
//		{
//			_pawn = target;
//			_offset = offset;
//
//			moveToPawn(target, offset);
//		}
	}
	
	@Override
	protected Location calculatePath(int ox, int oy, int oz, int tx, int ty, int tz)
	{
		// Retain some informations fur future use.
		final MoveType moveType = getMoveType();
		
		// We can process to next point without extra help ; return directly.
		if (moveType == MoveType.FLY)
		{
			if (GeoEngine.getInstance().canFlyToTarget(ox, oy, oz, 32, tx, ty, tz))
				return null;
		}
		else if (GeoEngine.getInstance().canMoveToTarget(ox, oy, oz, tx, ty, tz))
			return null;
		
		// Create dummy packet.
		final ExServerPrimitive dummy = _isDebugPath ? new ExServerPrimitive() : null;
		
		if (moveType != MoveType.GROUND)
			return GeoEngine.getInstance().getValidFlyLocation(ox, oy, oz, 32, tx, ty, tz, dummy);
		
		// Calculate the path. If no path or too short, calculate the first valid location.
		final List<Location> path = GeoEngine.getInstance().findPath(ox, oy, oz, tx, ty, tz, true, dummy);
		if (path.size() < 2)
			return GeoEngine.getInstance().getValidLocation(ox, oy, oz, tx, ty, tz, null);
		
		// Draw a debug of this movement if activated.
		if (_isDebugPath)
		{
			// Draw debug packet to all players.
			_actor.forEachKnownGM(p ->
			{
				// Get debug packet.
				final ExServerPrimitive debug = p.getDebugPacket("PATH" + _actor.getObjectId());
				
				// Reset the packet and add all lines and points.
				debug.reset();
				debug.addAll(dummy);
				
				// Send.
				debug.sendTo(p);
			});
		}
		
		// Feed the geopath with whole path.
		_geoPath.addAll(path);
		
		// Retrieve first Location.
		return _geoPath.poll();
	}
}
