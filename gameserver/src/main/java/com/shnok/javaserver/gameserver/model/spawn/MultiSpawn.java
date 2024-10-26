package com.shnok.javaserver.gameserver.model.spawn;

import java.io.InvalidClassException;
import java.util.List;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;

import net.sf.l2j.commons.geometry.AShape;
import net.sf.l2j.commons.random.Rnd;

import net.sf.l2j.Config;
import net.sf.l2j.gameserver.enums.MakerSpawnTime;
import net.sf.l2j.gameserver.geoengine.GeoEngine;
import net.sf.l2j.gameserver.model.WorldObject;
import net.sf.l2j.gameserver.model.actor.Creature;
import net.sf.l2j.gameserver.model.actor.Npc;
import net.sf.l2j.gameserver.model.actor.template.NpcTemplate;
import net.sf.l2j.gameserver.model.location.Location;
import net.sf.l2j.gameserver.model.location.SpawnLocation;
import net.sf.l2j.gameserver.model.memo.SpawnMemo;
import net.sf.l2j.gameserver.model.records.PrivateData;

/**
 * This class manages the spawn and respawn of {@link Npc}s defined by {@link NpcMaker} in a territory based system.<br>
 * The {@link SpawnLocation} can be:
 * <ul>
 * <li>Fixed coordinates.
 * <li>Random one of defined coordinates.
 * <li>Random coordinate from a {@link Territory} of linked {@link NpcMaker}.
 * </ul>
 */
public final class MultiSpawn extends ASpawn
{
	private static final int RANDOM_WALK_LOOP_LIMIT = 3;
	
	private final NpcMaker _npcMaker;
	private final int _total;
	private final int[][] _coords;
	
	private final Set<Npc> _npcs = ConcurrentHashMap.newKeySet();
	
	public MultiSpawn(NpcMaker npcMaker, NpcTemplate template, int total, int respawnDelay, int respawnRandom, List<PrivateData> privateData, SpawnMemo aiParams, int[][] coords, SpawnData spawnData) throws SecurityException, ClassNotFoundException, NoSuchMethodException, InvalidClassException
	{
		super(template);
		
		_respawnDelay = Math.max(0, respawnDelay);
		_respawnRandom = Math.min(respawnDelay, Math.max(0, respawnRandom));
		
		_privateData = privateData;
		_aiParams = aiParams;
		
		_npcMaker = npcMaker;
		_coords = coords;
		_spawnData = spawnData;
		
		// Database name is specified -> single spawn (ignore total value, only 1 instance of NPC may exist).
		if (_spawnData != null)
			_total = 1;
		// Coordinates specified -> fixed spawn.
		else if (_coords != null)
			_total = total;
		// Coordinates not specified -> random spawn.
		else
			_total = (int) Math.round(total * Config.SPAWN_MULTIPLIER);
	}
	
	@Override
	public Npc getNpc()
	{
		if (_npcs.isEmpty())
			return null;
		
		return _npcs.iterator().next();
	}
	
	@Override
	public SpawnLocation getSpawnLocation()
	{
		// "anywhere", spawn is random, generate random coordinates from territory.
		if (_coords == null)
			return _npcMaker.getTerritory().getRandomLocation(_npcMaker.getBannedTerritory());
		
		// "fixed", spawn is defined by one set of coordinates.
		if (_coords.length == 1)
			return new SpawnLocation(_coords[0][0], _coords[0][1], _coords[0][2], _coords[0][3]);
		
		// "fixed_random", spawn is defined by more sets of coordinates, pick one random.
		int chance = Rnd.get(100);
		for (int[] coord : _coords)
		{
			chance -= coord[4];
			if (chance < 0)
				return new SpawnLocation(coord[0], coord[1], coord[2], Rnd.get(65536));
		}
		
		// Should never happen.
		return null;
	}
	
	@Override
	public Location getRandomWalkLocation(Npc npc, int offset)
	{
		// Generate a new Location object based on Npc position.
		final Location loc = npc.getPosition().clone();
		
		// Npc position is out of the territory, return a random location based on NpcMaker's Territory.
		final AShape shape = _npcMaker.getTerritory().getShape(loc);
		if (shape == null)
			return _npcMaker.getTerritory().getRandomLocation();
		
		// Attempt three times to find a random Location matching the offset and banned territory.
		for (int loop = 0; loop < RANDOM_WALK_LOOP_LIMIT; loop++)
		{
			// Generate random location based on offset. Reset each attempt to current Npc position.
			loc.set(npc.getPosition());
			loc.addRandomOffsetBetween(offset / Rnd.get(2, 4), offset);
			
			// Validate location using NpcMaker's territory.
			if (!_npcMaker.getTerritory().isInside(loc))
				continue;
			
			// Validate location using NpcMaker's banned territory.
			if (_npcMaker.getBannedTerritory() != null && _npcMaker.getBannedTerritory().isInside(loc))
				continue;
			
			// Validate location using geodata.
			loc.set(GeoEngine.getInstance().getValidLocation(npc, loc));
			return loc;
		}
		
		// We didn't find a valid Location ; find the current AShape associated to the Npc position, and aim for its center.
		loc.set(GeoEngine.getInstance().getValidLocation(npc, shape.getCenter().getX(), shape.getCenter().getY(), npc.getZ()));
		
		return loc;
	}
	
	@Override
	public boolean isInMyTerritory(WorldObject worldObject)
	{
		final Location loc = worldObject.getPosition().clone();
		
		// Check location using NpcMaker's banned territory.
		if (_npcMaker.getBannedTerritory() != null && _npcMaker.getBannedTerritory().isInside(loc))
			return false;
		
		// Check location using NpcMaker's territory.
		return _npcMaker.getTerritory().isInside(loc);
	}
	
	@Override
	public Npc doSpawn(boolean isSummonSpawn, Creature summoner)
	{
		final Npc npc = super.doSpawn(isSummonSpawn, summoner);
		if (npc == null)
		{
			LOGGER.warn("Can not spawn id {} from maker {}.", getNpcId(), _npcMaker.getName());
			return null;
		}
		
		// Process dynamic Residence setting.
		final MakerSpawnTime mst = _npcMaker.getMakerSpawnTime();
		if (mst != null && mst != MakerSpawnTime.DOOR_OPEN)
		{
			final String[] params = _npcMaker.getMakerSpawnTimeParams();
			if (params != null)
				npc.setResidence(params[0]);
		}
		
		_npcs.add(npc);
		return npc;
	}
	
	@Override
	public void onSpawn(Npc npc)
	{
		synchronized (_npcMaker)
		{
			// Notify NpcMaker.
			_npcMaker.onSpawn(npc);
		}
	}
	
	@Override
	public void doDelete()
	{
		// Copying set prevents deleteAll to trigger onNpcDeleted and double spawns.
		Set<Npc> tmpNpcs = Set.copyOf(_npcs);
		
		_npcs.clear();
		
		tmpNpcs.forEach(npc ->
		{
			// Cancel respawn task.
			npc.cancelRespawn();
			
			// Delete privates which were manually spawned via createOnePrivate / createOnePrivateEx.
			if (npc.isMaster())
				npc.getMinions().forEach(Npc::deleteMe);
			
			// Delete the NPC.
			npc.deleteMe();
		});
		
		// Reset spawn data.
		if (_spawnData != null)
			_spawnData.setStatus((byte) -1);
	}
	
	@Override
	public void onDecay(Npc npc)
	{
		synchronized (_npcMaker)
		{
			// Notify NpcMaker.
			_npcMaker.onDecay(npc);
			
			if (getRespawnDelay() > 0)
			{
				// Calculate the random delay.
				final long respawnDelay = calculateRespawnDelay() * 1000;
				
				// Check spawn data and set respawn.
				if (_spawnData != null)
					_spawnData.setRespawn(respawnDelay);
			}
			else
			{
				// Respawn is disabled, delete NPC.
				_npcs.remove(npc);
			}
		}
	}
	
	@Override
	public String toString()
	{
		return "MultiSpawn [id=" + getNpcId() + "]";
	}
	
	@Override
	public String getDescription()
	{
		return "NpcMaker: " + _npcMaker.getName();
	}
	
	@Override
	public void updateSpawnData()
	{
		if (_spawnData == null)
			return;
		
		_npcs.forEach(npc -> _spawnData.setStats(npc));
	}
	
	@Override
	public void sendScriptEvent(int eventId, int arg1, int arg2)
	{
		_npcs.forEach(npc -> npc.sendScriptEvent(eventId, arg1, arg2));
	}
	
	public NpcMaker getNpcMaker()
	{
		return _npcMaker;
	}
	
	public int[][] getCoords()
	{
		return _coords;
	}
	
	public int getTotal()
	{
		return _total;
	}
	
	public Set<Npc> getNpcs()
	{
		return _npcs;
	}
	
	public int getNpcsAmount()
	{
		return _npcs.size();
	}
	
	public long getSpawned()
	{
		return _npcs.stream().filter(n -> !n.isDecayed()).count();
	}
	
	public long getDecayed()
	{
		return _npcs.stream().filter(Npc::isDecayed).count();
	}
	
	/**
	 * Respawns all {@link Npc}s of this {@link MultiSpawn}.
	 */
	public void doRespawn()
	{
		final Npc toRespawn = _npcs.stream().filter(Npc::isDecayed).findFirst().orElse(null);
		
		if (toRespawn != null)
			doRespawn(toRespawn);
	}
	
	public void loadDBNpcInfo()
	{
		_npcMaker.getMaker().onNpcDBInfo(this, _spawnData, _npcMaker);
	}
}