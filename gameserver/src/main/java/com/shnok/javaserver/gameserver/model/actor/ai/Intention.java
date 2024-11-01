package com.shnok.javaserver.gameserver.model.actor.ai;

import java.util.Objects;

import com.shnok.javaserver.gameserver.enums.IntentionType;
import com.shnok.javaserver.gameserver.model.WorldObject;
import com.shnok.javaserver.gameserver.model.actor.Boat;
import com.shnok.javaserver.gameserver.model.actor.Creature;
import com.shnok.javaserver.gameserver.model.location.Location;
import com.shnok.javaserver.gameserver.skills.L2Skill;

/**
 * A datatype used as a simple "wish" of an actor, consisting of an {@link IntentionType} and all needed parameters.
 */
public class Intention implements Comparable<Intention>
{
	protected IntentionType _type;
	
	private WorldObject _target;
	private Creature _finalTarget;
	
	private L2Skill _skill;
	private Location _loc;
	private Boat _boat;
	
	private boolean _isCtrlPressed;
	private boolean _isShiftPressed;
	
	private int _itemObjectId;
	private String _routeName;
	private int _timer;
	
	private boolean _moveToTarget;
	
	public Intention()
	{
		_type = IntentionType.IDLE;
	}
	
	@Override
	public boolean equals(Object obj)
	{
		if (this == obj)
			return true;
		
		if (!(obj instanceof Intention other))
			return false;
		
		if (_type != other._type)
			return false;
		
		switch (_type)
		{
			case IDLE, NOTHING, WANDER:
				return true;
			
			case ATTACK, FLEE, FOLLOW:
				return Objects.equals(_finalTarget, other._finalTarget);
			
			case PICK_UP:
				return _itemObjectId == other._itemObjectId;
			
			case MOVE_ROUTE:
				return _routeName.equals(other._routeName);
			
			case CAST:
				return Objects.equals(_finalTarget, other._finalTarget) && _skill.equals(other._skill);
			
			case MOVE_TO:
				return _loc.distance2D(other._loc) <= 20 && Math.abs(_loc.getZ() - other._loc.getZ()) <= 30;
			
			case SOCIAL:
				return _itemObjectId == other._itemObjectId;
			
			default:
				return false;
		}
	}
	
	@Override
	public int compareTo(Intention other)
	{
		// Compare by add order
		if (!equals(other))
			return -1;
		
		return 0;
	}
	
	@Override
	public int hashCode()
	{
		return Objects.hash(_boat, _finalTarget, _isCtrlPressed, _isShiftPressed, _itemObjectId, _loc, _moveToTarget, _routeName, _skill, _target, _timer, _type);
	}
	
	@Override
	public String toString()
	{
		return "Intention [type=" + _type.toString() + "]";
	}
	
	public IntentionType getType()
	{
		return _type;
	}
	
	public WorldObject getTarget()
	{
		return _target;
	}
	
	public Creature getFinalTarget()
	{
		return _finalTarget;
	}
	
	public L2Skill getSkill()
	{
		return _skill;
	}
	
	public Location getLoc()
	{
		return _loc;
	}
	
	public Boat getBoat()
	{
		return _boat;
	}
	
	public boolean isCtrlPressed()
	{
		return _isCtrlPressed;
	}
	
	public boolean isShiftPressed()
	{
		return _isShiftPressed;
	}
	
	public int getItemObjectId()
	{
		return _itemObjectId;
	}
	
	public String getRouteName()
	{
		return _routeName;
	}
	
	public int getTimer()
	{
		return _timer;
	}
	
	public boolean getMoveToTarget()
	{
		return _moveToTarget;
	}
	
	/**
	 * Set internally values, used as a shortcut for all "updateAs" methods.
	 * @param type : The new {@link IntentionType} to set.
	 * @param target : A {@link WorldObject} used as target.
	 * @param finalTarget : A {@link Creature} used as target.
	 * @param skill : A {@link L2Skill} used as reference.
	 * @param loc : A {@link Location} used as reference.
	 * @param boat : A {@link Boat} used as reference.
	 * @param isCtrlPressed : A boolean used as reference.
	 * @param isShiftPressed : A boolean used as reference.
	 * @param itemObjectId : An integer used as reference.
	 * @param timer : An integer used as reference.
	 * @param moveToTarget : Move to target(true) or not(false) when executing the desire
	 * @param routeName : A String used as reference.
	 */
	private synchronized void set(IntentionType type, WorldObject target, Creature finalTarget, L2Skill skill, Location loc, Boat boat, boolean isCtrlPressed, boolean isShiftPressed, int itemObjectId, int timer, boolean moveToTarget, String routeName)
	{
		_type = type;
		
		_target = target;
		_finalTarget = finalTarget;
		
		_skill = skill;
		_loc = (loc == null) ? null : loc.clone();
		_boat = boat;
		
		_isCtrlPressed = isCtrlPressed;
		_isShiftPressed = isShiftPressed;
		
		_itemObjectId = itemObjectId;
		_routeName = routeName;
		_timer = timer;
		
		_moveToTarget = moveToTarget;
	}
	
	public synchronized void updateAsAttack(Creature target, boolean isCtrlPressed, boolean isShiftPressed, boolean moveToTarget)
	{
		set(IntentionType.ATTACK, null, target, null, null, null, isCtrlPressed, isShiftPressed, 0, 0, moveToTarget, null);
	}
	
	public synchronized void updateAsCast(Creature caster, Creature target, L2Skill skill, boolean isCtrlPressed, boolean isShiftPressed, int itemObjectId, boolean moveToTarget)
	{
		set(IntentionType.CAST, null, skill.getFinalTarget(caster, target), skill, null, null, isCtrlPressed, isShiftPressed, itemObjectId, 0, moveToTarget, null);
	}
	
	public synchronized void updateAsFakeDeath(boolean startFakeDeath)
	{
		set(IntentionType.FAKE_DEATH, null, null, null, null, null, startFakeDeath, false, 0, 0, false, null);
	}
	
	public synchronized void updateAsFlee(Creature target, Location startLoc, int distance)
	{
		set(IntentionType.FLEE, null, target, null, startLoc, null, false, false, distance, 0, false, null);
	}
	
	public synchronized void updateAsFollow(Creature target, boolean isShiftPressed)
	{
		set(IntentionType.FOLLOW, null, target, null, null, null, false, isShiftPressed, 0, 0, false, null);
	}
	
	public synchronized void updateAsIdle()
	{
		set(IntentionType.IDLE, null, null, null, null, null, false, false, 0, 0, false, null);
	}
	
	public synchronized void updateAsInteract(WorldObject target, boolean isCtrlPressed, boolean isShiftPressed)
	{
		set(IntentionType.INTERACT, target, null, null, null, null, isCtrlPressed, isShiftPressed, 0, 0, false, null);
	}
	
	public synchronized void updateAsMoveRoute(String routeName)
	{
		set(IntentionType.MOVE_ROUTE, null, null, null, null, null, false, false, 0, 0, false, routeName);
	}
	
	public synchronized void updateAsMoveTo(Location loc, Boat boat)
	{
		set(IntentionType.MOVE_TO, null, null, null, loc, boat, false, false, 0, 0, false, null);
	}
	
	public synchronized void updateAsNothing(int timer)
	{
		set(IntentionType.NOTHING, null, null, null, null, null, false, false, 0, timer, false, null);
	}
	
	public synchronized void updateAsPickUp(int itemObjectId, boolean isShiftPressed)
	{
		set(IntentionType.PICK_UP, null, null, null, null, null, false, isShiftPressed, itemObjectId, 0, false, null);
	}
	
	public synchronized void updateAsSit(WorldObject target)
	{
		set(IntentionType.SIT, target, null, null, null, null, false, false, 0, 0, false, null);
	}
	
	public synchronized void updateAsSocial(int id, int timer)
	{
		set(IntentionType.SOCIAL, null, null, null, null, null, false, false, id, timer, false, null);
	}
	
	public synchronized void updateAsStand()
	{
		set(IntentionType.STAND, null, null, null, null, null, false, false, 0, 0, false, null);
	}
	
	public synchronized void updateAsUseItem(int itemObjectId)
	{
		set(IntentionType.USE_ITEM, null, null, null, null, null, false, false, itemObjectId, 0, false, null);
	}
	
	public synchronized void updateAsWander(int timer)
	{
		set(IntentionType.WANDER, null, null, null, null, null, false, false, 0, timer, false, null);
	}
	
	/**
	 * Update the current {@link Intention} with parameters taken from another {@link Intention}.
	 * @param intention : The {@link Intention} to use as parameters.
	 */
	public synchronized void updateUsing(Intention intention)
	{
		set(intention.getType(), intention.getTarget(), intention.getFinalTarget(), intention.getSkill(), intention.getLoc(), intention.getBoat(), intention.isCtrlPressed(), intention.isShiftPressed(), intention.getItemObjectId(), intention.getTimer(), intention.getMoveToTarget(), intention.getRouteName());
	}
	
	/**
	 * @return True if the current {@link Intention} got blank parameters.
	 */
	public synchronized boolean isBlank()
	{
		return _type == IntentionType.IDLE && _target == null && _finalTarget == null && _skill == null && _loc == null && _boat == null && _itemObjectId == 0 && _timer == 0;
	}
}