package com.shnok.javaserver.gameserver.model.zone.type.subtype;

import java.util.ArrayList;
import java.util.Collections;
import java.util.EnumMap;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;
import java.util.function.Predicate;

import com.shnok.javaserver.commons.logging.CLogger;

import com.shnok.javaserver.gameserver.enums.EventHandler;
import com.shnok.javaserver.gameserver.model.WorldObject;
import com.shnok.javaserver.gameserver.model.actor.Creature;
import com.shnok.javaserver.gameserver.model.actor.Player;
import com.shnok.javaserver.gameserver.model.location.Location;
import com.shnok.javaserver.gameserver.model.zone.ZoneForm;
import com.shnok.javaserver.gameserver.network.serverpackets.unused.ExServerPrimitive;
import com.shnok.javaserver.gameserver.network.serverpackets.L2GameServerPacket;
import com.shnok.javaserver.gameserver.scripting.Quest;

/**
 * An abstract base class for any zone type, which holds {@link Creature}s affected by this zone, linked {@link Quest}s and the associated {@link ZoneForm}.<br>
 * <br>
 * Zones can be retrieved by id, but since most use dynamic IDs, you must set individual zone id yourself if you want the system works correctly (otherwise id can be different if you add or remove zone types or zones).
 */
public abstract class ZoneType
{
	protected static final CLogger LOGGER = new CLogger(ZoneType.class.getName());
	
	protected final Set<Creature> _creatures = ConcurrentHashMap.newKeySet();
	
	private final int _id;
	
	private Map<EventHandler, List<Quest>> _questEvents;
	private ZoneForm _zone;
	
	protected ZoneType(int id)
	{
		_id = id;
	}
	
	protected abstract void onEnter(Creature creature);
	
	protected abstract void onExit(Creature creature);
	
	@Override
	public String toString()
	{
		return getClass().getSimpleName() + "[" + _id + "]";
	}
	
	public int getId()
	{
		return _id;
	}
	
	public ZoneForm getZone()
	{
		return _zone;
	}
	
	public void setZone(ZoneForm zone)
	{
		if (_zone != null)
			throw new IllegalStateException("Zone already set");
		
		_zone = zone;
	}
	
	/**
	 * @param x : The X position to test.
	 * @param y : The Y position to test.
	 * @return true if the given coordinates are within zone's plane. We use getHighZ() as Z reference.
	 */
	public boolean isInsideZone(int x, int y)
	{
		return _zone.isInsideZone(x, y, _zone.getHighZ());
	}
	
	/**
	 * @param x : The X position to test.
	 * @param y : The Y position to test.
	 * @param z : The Z position to test.
	 * @return true if the given coordinates are within the zone.
	 */
	public boolean isInsideZone(int x, int y, int z)
	{
		return _zone.isInsideZone(x, y, z);
	}
	
	/**
	 * @param object : Use object's X/Y positions.
	 * @return true if the {@link WorldObject} is inside the zone.
	 */
	public boolean isInsideZone(WorldObject object)
	{
		return isInsideZone(object.getX(), object.getY(), object.getZ());
	}
	
	public void visualizeZone(ExServerPrimitive debug)
	{
		_zone.visualizeZone(toString(), debug);
	}
	
	/**
	 * Update a {@link Creature} zone state.<br>
	 * <br>
	 * If the {@link Creature} is inside the zone, but not yet part of _creatures {@link Set} :
	 * <ul>
	 * <li>Fire {@link Quest#onZoneEnter}.</li>
	 * <li>Add the {@link Creature} to the {@link Set}.</li>
	 * <li>Fire zone onEnter() event.</li>
	 * </ul>
	 * If the {@link Creature} isn't inside the zone, and was part of _creatures {@link Set}, we run {@link #removeCreature(Creature)}.
	 * @param creature : The affected {@link Creature}.
	 */
	public void revalidateInZone(Creature creature)
	{
		// If the creature can't be affected by this zone, return.
		if (!isAffected(creature))
			return;
		
		// If the creature is inside the zone.
		if (isInsideZone(creature))
		{
			// We test if the creature is already registered.
			if (_creatures.add(creature))
			{
				// Notify to scripts.
				for (Quest quest : getQuestByEvent(EventHandler.ZONE_ENTER))
					quest.onZoneEnter(creature, this);
				
				// Notify Zone implementation.
				onEnter(creature);
			}
		}
		else
			removeCreature(creature);
	}
	
	/**
	 * Remove a {@link Creature} from this zone.
	 * <ul>
	 * <li>Fire {@link Quest#onZoneExit}.</li>
	 * <li>Remove the {@link Creature} from the {@link Map}.</li>
	 * <li>Fire zone onExit() event.</li>
	 * </ul>
	 * @param creature : The {@link Creature} to remove.
	 */
	public void removeCreature(Creature creature)
	{
		// We test and remove the creature if he was part of the zone.
		if (_creatures.remove(creature))
		{
			// Notify to scripts.
			for (Quest quest : getQuestByEvent(EventHandler.ZONE_EXIT))
				quest.onZoneExit(creature, this);
			
			// Notify Zone implementation.
			onExit(creature);
		}
	}
	
	/**
	 * @param creature : The {@link Creature} to test.
	 * @return true if the {@link Creature} is in the zone _creatures {@link Set}.
	 */
	public boolean isInZone(Creature creature)
	{
		return _creatures.contains(creature);
	}
	
	public Set<Creature> getCreatures()
	{
		return _creatures;
	}
	
	/**
	 * @param <A> : The object type must be an instance of WorldObject.
	 * @param type : The class specifying object type.
	 * @return a {@link List} of filtered type {@link Creature}s within this zone.
	 */
	public final <A> List<A> getKnownTypeInside(Class<A> type)
	{
		if (_creatures.isEmpty())
			return Collections.emptyList();
		
		return _creatures.stream().filter(type::isInstance).map(type::cast).toList();
	}
	
	/**
	 * @param <A> : The object type must be an instance of WorldObject.
	 * @param type : The class specifying object type.
	 * @param predicate : The {@link Predicate} to match.
	 * @return a {@link List} of filtered type {@link Creature}s based on a {@link Predicate} within this zone.
	 */
	public final <A> List<A> getKnownTypeInside(Class<A> type, Predicate<A> predicate)
	{
		if (_creatures.isEmpty())
			return Collections.emptyList();
		
		return _creatures.stream().filter(o -> type.isInstance(o) && predicate.test(type.cast(o))).map(type::cast).toList();
	}
	
	/**
	 * Add a {@link Quest} on _questEvents {@link Map}. Generate both Map and {@link List} if not existing (lazy initialization).<br>
	 * <br>
	 * If already existing, we remove and add it back.
	 * @param type : The EventType to test.
	 * @param quest : The Quest to add.
	 */
	public void addQuestEvent(EventHandler type, Quest quest)
	{
		if (_questEvents == null)
			_questEvents = new EnumMap<>(EventHandler.class);
		
		_questEvents.computeIfAbsent(type, k -> new ArrayList<>()).remove(quest);
		_questEvents.get(type).add(quest);
	}
	
	/**
	 * @param type : The EventType to test.
	 * @return the {@link List} of available {@link Quest}s associated to this zone for a given {@link EventHandler}.
	 */
	public List<Quest> getQuestByEvent(EventHandler type)
	{
		if (_questEvents == null)
			return Collections.emptyList();
		
		return _questEvents.getOrDefault(type, Collections.emptyList());
	}
	
	/**
	 * Broadcast a {@link L2GameServerPacket} to all {@link Player}s inside the zone.
	 * @param packet : The packet to use.
	 */
	public void broadcastPacket(L2GameServerPacket packet)
	{
		for (Creature creature : _creatures)
		{
			if (creature instanceof Player player)
				player.sendPacket(packet);
		}
	}
	
	/**
	 * Setup new parameters for this zone. By default, we return a warning (which mean this parameter isn't used on child zone).
	 * @param name : The parameter name.
	 * @param value : The parameter value.
	 */
	public void setParameter(String name, String value)
	{
		LOGGER.warn("Unknown name/values couple {}, {} for {}.", name, value, toString());
	}
	
	/**
	 * @param creature : The {@link Creature} to test.
	 * @return true if the given {@link Creature} is affected by this zone. Overriden in children classes.
	 */
	protected boolean isAffected(Creature creature)
	{
		return true;
	}
	
	/**
	 * Teleport all {@link Player}s located in this {@link ZoneType} to specific coords x/y/z.
	 * @param x : The X parameter used as teleport location.
	 * @param y : The Y parameter used as teleport location.
	 * @param z : The Z parameter used as teleport location.
	 */
	public void instantTeleport(int x, int y, int z)
	{
		for (Player player : getKnownTypeInside(Player.class, Player::isOnline))
			player.teleportTo(x, y, z, 0);
	}
	
	/**
	 * Teleport all {@link Player}s located in this {@link ZoneType} to a specific {@link Location}.
	 * @see #instantTeleport(int, int, int)
	 * @param loc : The {@link Location} used as coords.
	 */
	public void instantTeleport(Location loc)
	{
		instantTeleport(loc.getX(), loc.getY(), loc.getZ());
	}
	
	/**
	 * Add a {@link WorldObject} to knownlist.
	 * @param object : An object to be added.
	 */
	public void addKnownObject(WorldObject object)
	{
	}
	
	/**
	 * Remove a {@link WorldObject} from knownlist.
	 * @param object : An object to be removed.
	 */
	public void removeKnownObject(WorldObject object)
	{
	}
}