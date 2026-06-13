package com.shnok.javaserver.gameserver.model;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.function.Consumer;
import java.util.function.Predicate;

import com.shnok.javaserver.gameserver.model.actor.Creature;
import com.shnok.javaserver.gameserver.model.actor.Player;
import com.shnok.javaserver.gameserver.model.location.Location;
import com.shnok.javaserver.gameserver.model.zone.type.DerbyTrackZone;
import com.shnok.javaserver.gameserver.model.zone.type.PeaceZone;
import com.shnok.javaserver.gameserver.model.zone.type.TownZone;
import com.shnok.javaserver.gameserver.model.zone.type.subtype.ZoneType;
import com.shnok.javaserver.gameserver.skills.L2Skill;

public final class WorldRegion
{
	private final Map<Integer, WorldObject> _objects = new ConcurrentHashMap<>();
	
	private final List<ZoneType> _zones = new ArrayList<>();
	
	private final int _tileX;
	private final int _tileY;
	
	private final AtomicBoolean _isActive = new AtomicBoolean();
	private final AtomicInteger _playersCount = new AtomicInteger();
	
	public WorldRegion(int x, int y)
	{
		_tileX = x;
		_tileY = y;
	}
	
	@Override
	public String toString()
	{
		return "WorldRegion " + _tileX + "_" + _tileY + ", _active=" + _isActive.get() + ", _playersCount=" + _playersCount.get() + "]";
	}
	
	public Collection<WorldObject> getObjects()
	{
		return _objects.values();
	}
	
	public <A extends WorldObject> void forEachType(Class<A> type, Consumer<A> action)
	{
		for (WorldObject obj : getObjects())
		{
			// Check instance type.
			if (!type.isAssignableFrom(obj.getClass()))
				continue;
			
			// Consume the action on the casted object.
			action.accept(type.cast(obj));
		}
	}
	
	public <A extends WorldObject> void forEachType(Class<A> type, Predicate<A> filter, Consumer<A> action)
	{
		for (WorldObject obj : getObjects())
		{
			// Check instance type.
			if (!type.isAssignableFrom(obj.getClass()))
				continue;
			
			// Cast the object.
			final A actor = type.cast(obj);
			
			// Check predicate.
			if (!filter.test(actor))
				continue;
			
			// Consume the action on the casted object.
			action.accept(actor);
		}
	}
	
	public List<WorldRegion> getSurroundingRegions()
	{
		final List<WorldRegion> list = new ArrayList<>();
		
		for (int ix = -1; ix <= 1; ix++)
		{
			for (int iy = -1; iy <= 1; iy++)
			{
				final int x = _tileX + ix;
				final int y = _tileY + iy;
				
				if (x >= 0 && x < World.REGIONS_X && y >= 0 && y < World.REGIONS_Y)
					list.add(World.getInstance().getWorldRegions()[x][y]);
			}
		}
		return list;
	}
	
	/**
	 * Run a {@link Consumer} upon regions, and return no result.
	 * @param action : The {@link Consumer} to use.
	 */
	public void forEachSurroundingRegion(Consumer<WorldRegion> action)
	{
		forEachRegion(1, action);
	}
	
	/**
	 * Run a {@link Consumer} upon regions surrounding this specific {@link WorldRegion}, and return no result.
	 * @param depth : The depth of regions to retain.
	 * @param action : The {@link Consumer} to use.
	 */
	public void forEachRegion(int depth, Consumer<WorldRegion> action)
	{
		for (int ix = -depth; ix <= depth; ix++)
		{
			for (int iy = -depth; iy <= depth; iy++)
			{
				final int x = _tileX + ix;
				final int y = _tileY + iy;
				
				if (x >= 0 && x < World.REGIONS_X && y >= 0 && y < World.REGIONS_Y)
					action.accept(World.getInstance().getWorldRegions()[x][y]);
			}
		}
	}
	
	public List<ZoneType> getZones()
	{
		return _zones;
	}
	
	public void addZone(ZoneType zone)
	{
		_zones.add(zone);
	}
	
	public void removeZone(ZoneType zone)
	{
		_zones.remove(zone);
	}
	
	public void revalidateZones(Creature character)
	{
		// Do NOT update the world region while the character is still in the process of teleporting
		if (character.isTeleporting())
			return;
		
		_zones.forEach(z -> z.revalidateInZone(character));
	}
	
	public void removeFromZones(Creature character)
	{
		_zones.forEach(z -> z.removeCreature(character));
	}
	
	public boolean containsZone(int zoneId)
	{
		for (ZoneType z : _zones)
		{
			if (z.getId() == zoneId)
				return true;
		}
		return false;
	}
	
	public boolean checkEffectRangeInsidePeaceZone(L2Skill skill, Location loc)
	{
		final int range = skill.getEffectRange();
		final int up = loc.getY() + range;
		final int down = loc.getY() - range;
		final int left = loc.getX() + range;
		final int right = loc.getX() - range;
		
		for (ZoneType e : _zones)
		{
			if ((e instanceof TownZone tz && tz.isPeaceZone()) || e instanceof DerbyTrackZone || e instanceof PeaceZone)
			{
				if (e.isInsideZone(loc.getX(), up, loc.getZ()))
					return false;
				
				if (e.isInsideZone(loc.getX(), down, loc.getZ()))
					return false;
				
				if (e.isInsideZone(left, loc.getY(), loc.getZ()))
					return false;
				
				if (e.isInsideZone(right, loc.getY(), loc.getZ()))
					return false;
				
				if (e.isInsideZone(loc.getX(), loc.getY(), loc.getZ()))
					return false;
			}
		}
		return true;
	}
	
	public boolean isActive()
	{
		return _isActive.get();
	}
	
	public int getPlayersCount()
	{
		return _playersCount.get();
	}
	
	/**
	 * Check if neighbors (including self) aren't inhabited.
	 * @return true if the above condition is met.
	 */
	public boolean isEmptyNeighborhood()
	{
		for (int ix = -1; ix <= 1; ix++)
		{
			for (int iy = -1; iy <= 1; iy++)
			{
				final int x = _tileX + ix;
				final int y = _tileY + iy;
				
				if (x >= 0 && x < World.REGIONS_X && y >= 0 && y < World.REGIONS_Y)
				{
					final WorldRegion neighbor = World.getInstance().getWorldRegions()[x][y];
					if (neighbor.getPlayersCount() != 0)
						return false;
				}
			}
		}
		return true;
	}
	
	/**
	 * This function turns this region's AI on or off.
	 * @param value : if true, activate hp/mp regen and random animation. If false, clean aggro/attack list, set objects on IDLE and drop their AI tasks.
	 */
	public void setActive(boolean value)
	{
		if (!_isActive.compareAndSet(!value, value))
			return;
		
		for (WorldObject object : _objects.values())
		{
			if (value)
				object.onActiveRegion();
			else
				object.onInactiveRegion();
		}
	}
	
	/**
	 * Put the given object into WorldRegion objects map. If it's a player, increment the counter (used for region activation/desactivation).
	 * @param object : The object to register into this region.
	 */
	public void addVisibleObject(WorldObject object)
	{
		if (object == null)
			return;
		
		_objects.put(object.getObjectId(), object);
		
		if (object instanceof Player)
			_playersCount.incrementAndGet();
	}
	
	/**
	 * Remove the given object from WorldRegion objects map. If it's a player, decrement the counter (used for region activation/desactivation).
	 * @param object : The object to remove from this region.
	 */
	public void removeVisibleObject(WorldObject object)
	{
		if (object == null)
			return;
		
		_objects.remove(object.getObjectId());
		
		if (object instanceof Player)
			_playersCount.decrementAndGet();
	}
}