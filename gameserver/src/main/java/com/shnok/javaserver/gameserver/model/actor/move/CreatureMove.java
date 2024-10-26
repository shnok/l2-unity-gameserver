package com.shnok.javaserver.gameserver.model.actor.move;

import java.awt.Color;
import java.util.LinkedList;
import java.util.List;
import java.util.Queue;
import java.util.concurrent.ScheduledFuture;

import com.shnok.javaserver.commons.logging.CLogger;
import com.shnok.javaserver.commons.pool.ThreadPool;

import com.shnok.javaserver.Config;
import com.shnok.javaserver.gameserver.data.manager.ZoneManager;
import com.shnok.javaserver.gameserver.enums.AiEventType;
import com.shnok.javaserver.gameserver.enums.actors.MoveType;
import com.shnok.javaserver.gameserver.geoengine.GeoEngine;
import com.shnok.javaserver.gameserver.geoengine.geodata.GeoStructure;
import com.shnok.javaserver.gameserver.model.World;
import com.shnok.javaserver.gameserver.model.WorldObject;
import com.shnok.javaserver.gameserver.model.actor.Creature;
import com.shnok.javaserver.gameserver.model.actor.Npc;
import com.shnok.javaserver.gameserver.model.actor.Playable;
import com.shnok.javaserver.gameserver.model.actor.Player;
import com.shnok.javaserver.gameserver.model.location.Location;
import com.shnok.javaserver.gameserver.model.zone.type.WaterZone;
import com.shnok.javaserver.gameserver.network.serverpackets.ExServerPrimitive;
import com.shnok.javaserver.gameserver.network.serverpackets.L2GameServerPacket;
import com.shnok.javaserver.gameserver.network.serverpackets.MoveToLocation;
import com.shnok.javaserver.gameserver.network.serverpackets.MoveToPawn;
import com.shnok.javaserver.gameserver.network.serverpackets.StopMove;

/**
 * This class groups all movement data related to a {@link Creature}.
 * @param <T> : The {@link Creature} used as actor.
 */
public class CreatureMove<T extends Creature>
{
	private static final CLogger LOGGER = new CLogger(CreatureMove.class.getName());
	
	private static final int FOLLOW_INTERVAL = 1000;
	private static final int ATTACK_FOLLOW_INTERVAL = 500;
	
	protected final T _actor;
	
	protected WorldObject _pawn;
	protected int _offset;
	protected boolean _blocked;
	
	protected byte _moveTypes;
	
	protected final Location _destination = new Location(0, 0, 0);
	
	protected double _xAccurate;
	protected double _yAccurate;
	
	protected final Queue<Location> _geoPath = new LinkedList<>();
	
	protected boolean _isDebugMove;
	protected boolean _isDebugPath;
	
	protected ScheduledFuture<?> _task;
	protected ScheduledFuture<?> _followTask;
	
	protected int _geoPathFailCount;
	
	public CreatureMove(T actor)
	{
		_actor = actor;
	}
	
	public Location getDestination()
	{
		return _destination;
	}
	
	public MoveType getMoveType()
	{
		if ((_moveTypes & MoveType.SWIM.getMask()) != 0)
			return MoveType.SWIM;
		
		if ((_moveTypes & MoveType.FLY.getMask()) != 0)
			return MoveType.FLY;
		
		return MoveType.GROUND;
	}
	
	public void addMoveType(MoveType type)
	{
		_moveTypes |= type.getMask();
	}
	
	public void removeMoveType(MoveType type)
	{
		_moveTypes &= ~type.getMask();
	}
	
	public ScheduledFuture<?> getTask()
	{
		return _task;
	}
	
	public boolean isDebugMove()
	{
		return _isDebugMove;
	}
	
	public void setDebugMove(boolean isDebugMove)
	{
		_isDebugMove = isDebugMove;
	}
	
	public boolean isDebugPath()
	{
		return _isDebugPath;
	}
	
	public void setDebugPath(boolean isDebugPath)
	{
		_isDebugPath = isDebugPath;
	}
	
	/**
	 * Used by players to describe current action of the {@link Creature} associated to this {@link CreatureMove}.
	 * @param player : The Player we send the packet.
	 */
	public void describeMovementTo(Player player)
	{
		player.sendPacket(new MoveToLocation(_actor));
	}
	
	/**
	 * Move the {@link Creature} associated to this {@link CreatureMove} to defined {@link Location}.
	 * @param destination : The {@link Location} used as original destination.
	 * @param pathfinding : If true, we try to setup a pathfind. If a pathfind occured, then the destination {@link Location} is the first pathfind segment.
	 */
	protected void moveToLocation(Location destination, boolean pathfinding)
	{
		// Get the current position of the Creature.
		final Location position = _actor.getPosition().clone();
		
		// Set the current x/y.
		_xAccurate = position.getX();
		_yAccurate = position.getY();
		
		// Initialize variables.
		_geoPath.clear();
		
		if (pathfinding)
		{
			// Calculate the path.
			final Location loc = calculatePath(position.getX(), position.getY(), position.getZ(), destination.getX(), destination.getY(), destination.getZ());
			if (loc != null)
				destination.set(loc);
		}
		
		// Draw a debug of this movement if activated.
		if (_isDebugMove)
		{
			// Draw debug packet to surrounding GMs.
			_actor.forEachKnownGM(p ->
			{
				// Get debug packet.
				final ExServerPrimitive debug = p.getDebugPacket("MOVE" + _actor.getObjectId());
				
				// Reset the packet lines and points.
				debug.reset();
				
				// Add a WHITE line corresponding to the initial click release.
				debug.addLine("MoveToLocation: " + destination.toString(), Color.WHITE, true, position, destination);
				
				// Add a RED point corresponding to initial start location.
				debug.addPoint(Color.RED, position);
				
				// Add YELLOW lines corresponding to the geo path, if any. Add a single YELLOW line if no geoPath encountered.
				if (!_geoPath.isEmpty())
				{
					// Add manually a segment, since poll() was executed.
					debug.addLine("Segment #1", Color.YELLOW, true, position, destination);
					
					// Initialize a Location based on target location.
					final Location curPos = new Location(destination);
					int i = 2;
					
					// Iterate geo path.
					for (Location geoPos : _geoPath)
					{
						// Draw a blue line going from initial to geo path.
						debug.addLine("Segment #" + i, Color.YELLOW, true, curPos, geoPos);
						
						// Set current path as geo path ; the draw will start from here.
						curPos.set(geoPos);
						i++;
					}
				}
				else
					debug.addLine("No geopath", Color.YELLOW, true, position, destination);
				
				p.sendMessage("Moving from " + position.toString() + " to " + destination.toString());
			});
		}
		
		// Set the destination.
		_destination.set(destination);
		
		// Calculate the heading.
		_actor.getPosition().setHeadingTo(destination);
		
		registerMoveTask();
		
		// Broadcast MoveToLocation packet to known objects.
		_actor.broadcastPacket(new MoveToLocation(_actor, destination));
	}
	
	public void registerMoveTask()
	{
		if (_task != null)
			return;
		
		_blocked = false;
		
		_task = ThreadPool.scheduleAtFixedRate(() ->
		{
			if (updatePosition(false) && !moveToNextRoutePoint())
				ThreadPool.execute(() ->
				{
					cancelMoveTask();
					
					_actor.revalidateZone(true);
					if (!_blocked)
						_actor.getAI().notifyEvent(AiEventType.ARRIVED, null, null);
					else
						_actor.getAI().notifyEvent(AiEventType.ARRIVED_BLOCKED, null, null);
				});
		}, 100, 100);
	}
	
	public void cancelMoveTask()
	{
		if (_task != null)
		{
			_task.cancel(true);
			_task = null;
		}
	}
	
	public boolean moveToNextRoutePoint()
	{
		// Creature is not on geodata path, return.
		if (_task == null || _geoPath.isEmpty())
			return false;
		
		// Movement is not allowed, return.
		if (_actor.getStatus().getMoveSpeed() <= 0 || _actor.isMovementDisabled())
			return false;
		
		// Geopath is dry, return.
		final Location destination = _geoPath.poll();
		if (destination == null)
			return false;
		
		// Set the current x/y.
		_xAccurate = _actor.getX();
		_yAccurate = _actor.getY();
		
		// Set the destination.
		_destination.set(destination);
		
		// Set the heading.
		_actor.getPosition().setHeadingTo(destination);
		
		// Broadcast MoveToLocation packet to known objects.
		_actor.broadcastPacket(new MoveToLocation(_actor, destination));
		
		return true;
	}
	
	/**
	 * Update the position of the Creature during a movement and return True if the movement is finished.<BR>
	 * <BR>
	 * <B><U> Concept</U> :</B><BR>
	 * <BR>
	 * At the beginning of the move action, all properties are stored. The position of the start point and of the destination permit to estimated in function of the movement speed the time to achieve the destination.<BR>
	 * <BR>
	 * When the movement is started, this method will be called each 0.1 sec to estimate and update the Creature position on the server. Note, that the current server position can differe from the current client position even if each movement is straight foward. That's why, client send regularly a
	 * Client->Server ValidatePosition packet to eventually correct the gap on the server. But, it's always the server position that is used in range calculation.<BR>
	 * <BR>
	 * At the end of the estimated movement time, the Creature position is automatically set to the destination position even if the movement is not finished.
	 * @param firstRun
	 * @return true if the movement is finished.
	 */
	public boolean updatePosition(boolean firstRun)
	{
		if (_task == null || !_actor.isVisible())
			return true;
		
		// We got a pawn target, but it is not known anymore - stop the movement.
		if (_pawn != null && !_actor.knows(_pawn))
			return true;
		
		final MoveType type = getMoveType();
		
		final int curX = _actor.getX();
		final int curY = _actor.getY();
		final int curZ = _actor.getZ();
		
		if (type == MoveType.GROUND)
			_destination.setZ(GeoEngine.getInstance().getHeight(_destination));
		
		final double dx = _destination.getX() - _xAccurate;
		final double dy = _destination.getY() - _yAccurate;
		final double dz = _destination.getZ() - curZ;
		
		// We use Z for delta calculation only if different of GROUND MoveType.
		final double leftDistance = (type == MoveType.GROUND) ? Math.sqrt(dx * dx + dy * dy) : Math.sqrt(dx * dx + dy * dy + dz * dz);
		final double passedDistance = _actor.getStatus().getMoveSpeed() / 10;
		
		// Calculate the maximum Z.
		int maxZ = World.WORLD_Z_MAX;
		if (type == MoveType.SWIM)
		{
			final WaterZone waterZone = ZoneManager.getInstance().getZone(curX, curY, curZ, WaterZone.class);
			if (waterZone != null && GeoEngine.getInstance().getHeight(curX, curY, curZ) - waterZone.getWaterZ() < -20)
				maxZ = waterZone.getWaterZ();
		}
		
		final int nextX;
		final int nextY;
		final int nextZ;
		
		// Set the position only
		if (passedDistance < leftDistance)
		{
			// Calculate the current distance fraction based on the delta.
			final double fraction = passedDistance / leftDistance;
			
			_xAccurate += dx * fraction;
			_yAccurate += dy * fraction;
			
			// Note: Z coord shifted up to avoid dual-layer issues.
			nextX = (int) _xAccurate;
			nextY = (int) _yAccurate;
			nextZ = Math.min((type == MoveType.GROUND) ? GeoEngine.getInstance().getHeight(nextX, nextY, curZ + 2 * GeoStructure.CELL_HEIGHT) : (curZ + (int) (dz * fraction + 0.5)), maxZ);
		}
		// Already there : set the position to the destination.
		else
		{
			nextX = _destination.getX();
			nextY = _destination.getY();
			nextZ = Math.min(_destination.getZ(), maxZ);
		}
		
		// Check if location can be reached (case of dynamic objects, such as opening doors/fences).
		if (type == MoveType.GROUND && !GeoEngine.getInstance().canMoveToTarget(curX, curY, curZ, nextX, nextY, nextZ))
		{
			_blocked = true;
			return true;
		}
		
		// Set the position of the Creature.
		_actor.setXYZ(nextX, nextY, nextZ);
		
		// Draw a debug of this movement if activated.
		if (_isDebugMove)
		{
			final String heading = "" + _actor.getHeading();
			
			// Draw debug packet to surrounding GMs.
			_actor.forEachKnownGM(p ->
			{
				// Get debug packet.
				final ExServerPrimitive debug = p.getDebugPacket("MOVE" + _actor.getObjectId());
				
				// Draw a RED point for current position.
				debug.addPoint(heading, Color.RED, true, _actor.getPosition());
				
				// Send the packet to the Player.
				debug.sendTo(p);
				
				// We are supposed to run, but the difference of Z is way too high.
				if (type == MoveType.GROUND && Math.abs(curZ - _actor.getPosition().getZ()) > 100)
					p.sendMessage("Falling/Climb bug found when moving from " + curX + ", " + curY + ", " + curZ + " to " + _actor.getPosition().toString());
			});
		}
		
		_actor.revalidateZone(false);
		
		if (isOnLastPawnMoveGeoPath() && ((type == MoveType.GROUND) ? _actor.isIn2DRadius(_pawn, _offset) : _actor.isIn3DRadius(_pawn, _offset)))
			return true;
		
		return (passedDistance >= leftDistance);
	}
	
	public boolean maybeStartOffensiveFollow(Creature target, int weaponAttackRange)
	{
		if (weaponAttackRange < 0)
			return false;
		
		boolean shouldFollow = true;
		int totalRadius = (int) (weaponAttackRange + _actor.getCollisionRadius() + target.getCollisionRadius());
		if (_actor instanceof Npc)
			totalRadius += (target.isMoving() ? 50 : 0);
		
		if (_actor.isIn2DRadius(target, totalRadius))
			shouldFollow = false;
		
		if (!shouldFollow)
		{
			if (_actor instanceof Npc && !GeoEngine.getInstance().canSeeTarget(_actor, target) && !_actor.isMovementDisabled() && _actor.getAI().getCurrentIntention().getMoveToTarget())
				startOffensiveFollow(target, (int) target.getCollisionRadius());
			else
				return false;
		}
		else if (!_actor.isMovementDisabled() && _actor.getAI().getCurrentIntention().getMoveToTarget())
		{
			startOffensiveFollow(target, weaponAttackRange);
		}
		
		return true;
	}
	
	public boolean maybeStartFriendlyFollow(Creature target, int range)
	{
		if (_actor.isMovementDisabled())
			return false;
		
		startFriendlyFollow(target, range);
		return true;
	}
	
	// Used for:
	// Players: Pickup (pathfinding = true), casting signets (pathfinding = false), regular movement
	// Monsters: regular movement, NOT for combat
	public boolean maybeMoveToLocation(Location destination, int offset, boolean pathfinding, boolean isShiftPressed)
	{
		if (_actor.isIn3DRadius(destination, offset))
			return false;
		
		if (!_actor.isMovementDisabled() && !isShiftPressed)
		{
			_pawn = null;
			_offset = 0;
			
			moveToLocation(destination, pathfinding);
		}
		
		return true;
	}
	
	/**
	 * Stop the movement of the {@link Creature}.
	 */
	public void stop()
	{
		// Actor isn't actually moving.
		if (_task == null && _followTask == null)
			return;
		
		cancelFollowTask();
		cancelMoveTask();
		
		_actor.revalidateZone(true);
		_actor.broadcastPacket(new StopMove(_actor));
	}
	
	/**
	 * @return true if this {@link Creature} is under LAST geopath entry AND follow a pawn.
	 */
	protected boolean isOnLastPawnMoveGeoPath()
	{
		return _geoPath.isEmpty() && _pawn instanceof Creature;
	}
	
	/**
	 * If this {@link Creature} is under LAST geopath entry AND follow a pawn, send the {@link MoveToPawn} packet.<br>
	 * <br>
	 * Otherwise, it means it still follows a determined path, which means he uses {@link MoveToLocation} packet.
	 * @return the {@link L2GameServerPacket} packet to send.
	 */
	protected L2GameServerPacket findPacketToSend()
	{
		return new MoveToLocation(_actor);
	}
	
	/**
	 * @param ox : The original X coordinate we start from.
	 * @param oy : The original Y coordinate we start from.
	 * @param oz : The original Z coordinate we start from.
	 * @param tx : The target X coordinate we search to join.
	 * @param ty : The target Y coordinate we search to join.
	 * @param tz : The target Z coordinate we search to join.
	 * @return the new {@link Location} destination to set, or null if not needed. Feed the geopath if needed.
	 */
	protected Location calculatePath(int ox, int oy, int oz, int tx, int ty, int tz)
	{
		// We can process to next point without extra help ; return directly.
		if (GeoEngine.getInstance().canMoveToTarget(ox, oy, oz, tx, ty, tz))
			return null;
		
		// Create dummy packet.
		final ExServerPrimitive dummy = _isDebugPath ? new ExServerPrimitive() : null;
		
		// Calculate the path. If no path or too short, calculate the first valid location.
		final List<Location> path = GeoEngine.getInstance().findPath(ox, oy, oz, tx, ty, tz, _actor instanceof Playable, dummy);
		
		if (path.size() < 2)
		{
			addGeoPathFailCount();
			return GeoEngine.getInstance().getValidLocation(ox, oy, oz, tx, ty, tz, null);
		}
		
		resetGeoPathFailCount();
		
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
	
	/**
	 * Create and launch a follow task upon a {@link WorldObject} pawn, executed every 1s. It is used by onIntentionFollow.
	 * @param pawn : The WorldObject to follow.
	 * @param offset : The specific range to follow at.
	 */
	public void startFriendlyFollow(Creature pawn, int offset)
	{
		cancelFollowTask();
		
		// Create and Launch an AI Follow Task to execute every 1s
		_followTask = ThreadPool.scheduleAtFixedRate(() -> friendlyFollowTask(pawn, offset), 5, FOLLOW_INTERVAL);
	}
	
	/**
	 * Create and launch a follow task upon a {@link WorldObject} pawn, every 0.5s, following at specified range.
	 * @param pawn : The WorldObject to follow.
	 * @param offset : The specific range to follow at.
	 */
	public void startOffensiveFollow(Creature pawn, int offset)
	{
		cancelFollowTask();
		
		_followTask = ThreadPool.scheduleAtFixedRate(() -> offensiveFollowTask(pawn, offset), 5, ATTACK_FOLLOW_INTERVAL);
	}
	
	protected void offensiveFollowTask(Creature target, int offset)
	{
		// No follow task, return.
		if (_followTask == null)
			return;
		
		// Pawn isn't registered on knownlist.
		if (!_actor.knows(target))
			return;
		
		final Location destination = target.getPosition().clone();
		final int realOffset = (int) (offset + _actor.getCollisionRadius() + target.getCollisionRadius());
		
		// Don't bother moving if already in radius.
		if ((getMoveType() == MoveType.GROUND) ? _actor.isIn2DRadius(destination, realOffset) : _actor.isIn3DRadius(destination, realOffset))
			return;
		
		_pawn = target;
		_offset = offset;
		
		moveToLocation(destination, true);
	}
	
	protected void friendlyFollowTask(Creature target, int offset)
	{
		// No follow task, return.
		if (_followTask == null)
			return;
		
		// Invalid pawn to follow, or the pawn isn't registered on knownlist.
		if (!_actor.knows(target))
			return;
		
		if (target.isInBoat())
			return;
		
		final Location destination = target.getPosition().clone();
		final int realOffset = (int) (offset + _actor.getCollisionRadius() + target.getCollisionRadius());
		
		// Don't bother moving if already in radius.
		if ((getMoveType() == MoveType.GROUND) ? _actor.isIn2DRadius(destination, realOffset) : _actor.isIn3DRadius(destination, realOffset))
			return;
		
		_pawn = null;
		_offset = 0;
		
		moveToLocation(destination, true);
	}
	
	/**
	 * Stop the follow task.
	 */
	public void cancelFollowTask()
	{
		if (_followTask != null)
		{
			_followTask.cancel(false);
			_followTask = null;
		}
	}
	
	public void avoidAttack(Creature attacker)
	{
		// Summon behavior
	}
	
	public int getGeoPathFailCount()
	{
		return _geoPathFailCount;
	}
	
	public void resetGeoPathFailCount()
	{
		_geoPathFailCount = 0;
	}
	
	public void addGeoPathFailCount()
	{
		if (_geoPathFailCount > Config.MAX_GEOPATH_FAIL_COUNT)
		{
			LOGGER.warn("{} fails as geopathing at {}.", _actor.getName(), _actor.getPosition().toString());
			
			// Reset the count to 0.
			_geoPathFailCount = 0;
			return;
		}
		
		_geoPathFailCount++;
	}
}