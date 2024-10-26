package com.shnok.javaserver.gameserver.model.spawn;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import net.sf.l2j.commons.data.StatSet;

import net.sf.l2j.Config;
import net.sf.l2j.gameserver.data.manager.SpawnManager;
import net.sf.l2j.gameserver.enums.MakerSpawnTime;
import net.sf.l2j.gameserver.model.actor.Npc;
import net.sf.l2j.gameserver.model.memo.MakerMemo;
import net.sf.l2j.gameserver.scripting.Quest;
import net.sf.l2j.gameserver.scripting.SpawnMaker;
import net.sf.l2j.gameserver.scripting.script.maker.AlarmDeviceMaker1;
import net.sf.l2j.gameserver.scripting.script.maker.AlarmDeviceMaker2;
import net.sf.l2j.gameserver.scripting.script.maker.AlarmDeviceMaker3;
import net.sf.l2j.gameserver.scripting.script.maker.AlarmDeviceMaker4;
import net.sf.l2j.gameserver.scripting.script.maker.BenomMaker;
import net.sf.l2j.gameserver.scripting.script.maker.CloseDoorMaker;
import net.sf.l2j.gameserver.scripting.script.maker.DefaultMaker;
import net.sf.l2j.gameserver.scripting.script.maker.DefaultUseDBMaker;
import net.sf.l2j.gameserver.scripting.script.maker.DefaultUseDBMakerForFrintezza;
import net.sf.l2j.gameserver.scripting.script.maker.EventMaker;
import net.sf.l2j.gameserver.scripting.script.maker.ExclusiveDespawnNormalMaker;
import net.sf.l2j.gameserver.scripting.script.maker.ExclusiveSpawnNormalMaker;
import net.sf.l2j.gameserver.scripting.script.maker.FarmMaker;
import net.sf.l2j.gameserver.scripting.script.maker.FreyaDeaconKeeperMaker;
import net.sf.l2j.gameserver.scripting.script.maker.FrintezzaEvilateMaker;
import net.sf.l2j.gameserver.scripting.script.maker.HallKeeperDefaultMaker;
import net.sf.l2j.gameserver.scripting.script.maker.IceFairySirrMaker;
import net.sf.l2j.gameserver.scripting.script.maker.InstantSpawnMaker;
import net.sf.l2j.gameserver.scripting.script.maker.InstantSpawnRandomMaker;
import net.sf.l2j.gameserver.scripting.script.maker.InstantSpawnSerialLastMaker;
import net.sf.l2j.gameserver.scripting.script.maker.InstantSpawnSerialMaker;
import net.sf.l2j.gameserver.scripting.script.maker.ManageTeleportDungeonMaker;
import net.sf.l2j.gameserver.scripting.script.maker.NoOnStartMaker;
import net.sf.l2j.gameserver.scripting.script.maker.OnDayNightSpawnMaker;
import net.sf.l2j.gameserver.scripting.script.maker.OpenDoorMaker;
import net.sf.l2j.gameserver.scripting.script.maker.ParentSpawnAllMaker;
import net.sf.l2j.gameserver.scripting.script.maker.RainbowMaker;
import net.sf.l2j.gameserver.scripting.script.maker.RandomSpawnMaker;
import net.sf.l2j.gameserver.scripting.script.maker.RandomSpawnPawnMaker;
import net.sf.l2j.gameserver.scripting.script.maker.RhamphorhynchusMaker;
import net.sf.l2j.gameserver.scripting.script.maker.RoyalReqNextMaker;
import net.sf.l2j.gameserver.scripting.script.maker.RoyalRushMaker;
import net.sf.l2j.gameserver.scripting.script.maker.RoyalSpawnTreasureBoxMaker;
import net.sf.l2j.gameserver.scripting.script.maker.SailrenDummyMaker;
import net.sf.l2j.gameserver.scripting.script.maker.SailrenMaker;
import net.sf.l2j.gameserver.scripting.script.maker.SculptureGardenMaker;
import net.sf.l2j.gameserver.scripting.script.maker.SculptureIceFairyMaker;
import net.sf.l2j.gameserver.scripting.script.maker.StatueOfShilenMaker;
import net.sf.l2j.gameserver.scripting.script.maker.TyrannosaurusMaker;
import net.sf.l2j.gameserver.scripting.script.maker.UndeadBandmasterMaker;
import net.sf.l2j.gameserver.scripting.script.maker.UniqueNpcKillEventMaker;
import net.sf.l2j.gameserver.scripting.script.maker.VelociraptorMaker;
import net.sf.l2j.gameserver.scripting.script.maker.WarriorPassiveWeaknessMaker;

public class NpcMaker
{
	private final String _name;
	private final Territory _territory;
	private final Territory _bannedTerritory;
	private final SpawnMaker _maker;
	private final MakerMemo _aiParams;
	private final int _maximumNpc;
	private final String _event;
	
	private MakerSpawnTime _spawnTime = null;
	private String[] _spawnTimeParams = null;
	
	private List<MultiSpawn> _spawns;
	private List<Quest> _questEvents = Collections.emptyList();
	
	private int _npcs;
	
	/**
	 * Implicit {@link NpcMaker} constructor.
	 * @param set : Stats of the {@link NpcMaker}.
	 */
	public NpcMaker(StatSet set)
	{
		_name = set.getString("name", null);
		
		_territory = set.getObject("t", Territory.class);
		_bannedTerritory = set.getObject("bt", Territory.class);
		_aiParams = new MakerMemo(set.getMap("aiParams"));
		_maximumNpc = (int) Math.round(set.getInteger("maximumNpcs") * Config.SPAWN_MULTIPLIER);
		_event = set.getString("event", null);
		
		switch (set.getString("maker", ""))
		{
			case "Close_Door_maker":
				_maker = new CloseDoorMaker("Close_Door_maker");
				break;
			
			case "default_use_db_maker":
				_maker = new DefaultUseDBMaker("default_use_db_maker");
				break;
			
			case "exclusive_spawn_normal":
				_maker = new ExclusiveSpawnNormalMaker("exclusive_spawn_normal");
				break;
			
			case "freya_deacon_keeper_maker":
				_maker = new FreyaDeaconKeeperMaker("freya_deacon_keeper_maker");
				break;
			
			case "ice_fairy_sirr_maker":
				_maker = new IceFairySirrMaker("ice_fairy_sirr_maker");
				break;
			
			case "maker_instant_spawn_random":
				_maker = new InstantSpawnRandomMaker("maker_instant_spawn_random");
				break;
			
			case "maker_instant_spawn_serial":
				_maker = new InstantSpawnSerialMaker("maker_instant_spawn_serial");
				break;
			
			case "maker_instant_spawn":
				_maker = new InstantSpawnMaker("maker_instant_spawn");
				break;
			
			case "manage_teleport_dungeon":
				_maker = new ManageTeleportDungeonMaker("manage_teleport_dungeon");
				break;
			
			case "on_day_night_spawn":
				_maker = new OnDayNightSpawnMaker("on_day_night_spawn");
				break;
			
			case "random_spawn":
				_maker = new RandomSpawnMaker("random_spawn");
				break;
			
			case "royal_req_next_maker":
				_maker = new RoyalReqNextMaker("royal_req_next_maker");
				break;
			
			case "royal_rush_maker":
				_maker = new RoyalRushMaker("royal_rush_maker");
				break;
			
			case "royal_spawn_treasurebox":
				_maker = new RoyalSpawnTreasureBoxMaker("royal_spawn_treasurebox");
				break;
			
			case "parent_spawn_all":
				_maker = new ParentSpawnAllMaker("parent_spawn_all");
				break;
			
			case "unique_npc_kill_event":
				_maker = new UniqueNpcKillEventMaker("unique_npc_kill_event");
				break;
			
			case "no_on_start_maker":
				_maker = new NoOnStartMaker("no_on_start_maker");
				break;
			
			case "velociraptor_maker":
				_maker = new VelociraptorMaker("velociraptor_maker");
				break;
			
			case "rhamphorhynchus_maker":
				_maker = new RhamphorhynchusMaker("rhamphorhynchus_maker");
				break;
			
			case "tyrannosaurus_maker":
				_maker = new TyrannosaurusMaker("tyrannosaurus_maker");
				break;
			
			case "sailren_maker":
				_maker = new SailrenMaker("sailren_maker");
				break;
			
			case "sculpture_garden_maker":
				_maker = new SculptureGardenMaker("sculpture_garden_maker");
				break;
			
			case "sculpture_ice_fairy_maker":
				_maker = new SculptureIceFairyMaker("sculpture_ice_fairy_maker");
				break;
			
			case "sailren_dummy_maker":
				_maker = new SailrenDummyMaker("sailren_dummy_maker");
				break;
			
			case "statue_of_shilen_maker":
				_maker = new StatueOfShilenMaker("statue_of_shilen_maker");
				break;
			
			case "benom_maker":
				_maker = new BenomMaker("benom_maker");
				break;
			
			case "default_use_db_maker_for_frintessa":
				_maker = new DefaultUseDBMakerForFrintezza("default_use_db_maker_for_frintessa");
				break;
			
			case "frintessa_evilate_maker":
				_maker = new FrintezzaEvilateMaker("frintessa_evilate_maker");
				break;
			
			case "hall_keeper_wizard_maker":
				_maker = new HallKeeperDefaultMaker("hall_keeper_wizard_maker");
				break;
			
			case "hall_keeper_patrol_maker":
				_maker = new HallKeeperDefaultMaker("hall_keeper_patrol_maker");
				break;
			
			case "alarm_device1_maker":
				_maker = new AlarmDeviceMaker1("alarm_device1_maker");
				break;
			
			case "alarm_device2_maker":
				_maker = new AlarmDeviceMaker2("alarm_device2_maker");
				break;
			
			case "alarm_device3_maker":
				_maker = new AlarmDeviceMaker3("alarm_device3_maker");
				break;
			
			case "alarm_device4_maker":
				_maker = new AlarmDeviceMaker4("alarm_device4_maker");
				break;
			
			case "hall_keeper_guard_maker":
				_maker = new HallKeeperDefaultMaker("hall_keeper_guard_maker");
				break;
			
			case "hall_keeper_captain_maker":
				_maker = new HallKeeperDefaultMaker("hall_keeper_captain_maker");
				break;
			
			case "hall_keeper_self_destruction_maker":
				_maker = new HallKeeperDefaultMaker("hall_keeper_self_destruction_maker");
				break;
			
			case "undead_bandmaster_maker":
				_maker = new UndeadBandmasterMaker("undead_bandmaster_maker");
				break;
			
			case "undead_band_lance_maker":
				_maker = new OpenDoorMaker("undead_band_lance_maker");
				break;
			
			case "undead_band_leader_maker":
				_maker = new OpenDoorMaker("undead_band_leader_maker");
				break;
			
			case "undead_band_archer_wazird_maker":
				_maker = new OpenDoorMaker("undead_band_archer_wazird_maker");
				break;
			
			case "warrior_passive_weakness_maker":
				_maker = new WarriorPassiveWeaknessMaker("warrior_passive_weakness_maker");
				break;
			
			case "hall_timer_bomb_maker":
				_maker = new HallKeeperDefaultMaker("hall_timer_bomb_maker");
				break;
			
			case "farm_maker":
				_maker = new FarmMaker("farm_maker");
				break;
			
			case "rainbow_maker":
				_maker = new RainbowMaker("rainbow_maker");
				break;
			
			case "event_maker":
				_maker = new EventMaker("event_maker");
				break;
			
			case "maker_instant_spawn_serial_last":
				_maker = new InstantSpawnSerialLastMaker("maker_instant_spawn_serial_last");
				break;
			
			case "random_spawn_pawn":
				_maker = new RandomSpawnPawnMaker("random_spawn_pawn");
				break;
			
			case "exclusive_despawn_normal":
				_maker = new ExclusiveDespawnNormalMaker("exclusive_despawn_normal");
				break;
			
			case "exclusive_spawn_use_db":
				_maker = new ExclusiveSpawnNormalMaker("exclusive_spawn_use_db");
				break;
			
			default:
				_maker = new DefaultMaker("default_maker");
				break;
		}
		
		final String makerSpawnTime = set.getString("spawnTime", null);
		if (makerSpawnTime != null)
		{
			final String[] split = makerSpawnTime.split("[()]");
			if (split.length == 2)
			{
				_spawnTime = MakerSpawnTime.getEnumByName(split[0]);
				_spawnTimeParams = split[1].split(";");
			}
		}
		
		_spawns = null;
		_npcs = 0;
	}
	
	/**
	 * @return the name of the {@link NpcMaker}.
	 */
	public final String getName()
	{
		return _name;
	}
	
	/**
	 * @return the {@link Territory} of the {@link NpcMaker}.
	 */
	public final Territory getTerritory()
	{
		return _territory;
	}
	
	/**
	 * @return the banned {@link Territory} of the {@link NpcMaker}.
	 */
	public final Territory getBannedTerritory()
	{
		return _bannedTerritory;
	}
	
	/**
	 * @return the {@link SpawnMaker} of the {@link NpcMaker}.
	 */
	public final SpawnMaker getMaker()
	{
		return _maker;
	}
	
	/**
	 * @return the {@link MakerSpawnTime} of the {@link NpcMaker}.
	 */
	public final MakerSpawnTime getMakerSpawnTime()
	{
		return _spawnTime;
	}
	
	/**
	 * @return the {@link MakerSpawnTime} parameters of the {@link NpcMaker}.
	 */
	public final String[] getMakerSpawnTimeParams()
	{
		return _spawnTimeParams;
	}
	
	/**
	 * @return the {@link MakerMemo} of the {@link NpcMaker}.
	 */
	public final MakerMemo getMakerMemo()
	{
		return _aiParams;
	}
	
	/**
	 * @return the maximum amount of NPCs allowed for the {@link NpcMaker}.
	 */
	public final int getMaximumNpc()
	{
		return _maximumNpc;
	}
	
	/**
	 * @return the event name of the {@link NpcMaker}, used to spawn/despawn special groups of NPCs.
	 */
	public final String getEvent()
	{
		if (_event != null)
			return _event;
		else if (_aiParams != null && _aiParams.get("EventName") != null)
			return _aiParams.get("EventName");
		
		return null;
	}
	
	/**
	 * @return true, if the {@link NpcMaker} is to be spawned on server start.
	 */
	public final boolean isOnStart()
	{
		boolean isOnStart = true;
		if (_aiParams != null && _aiParams.get("on_start_spawn") != null && _aiParams.getInteger("on_start_spawn") == 0)
			isOnStart = false;
		
		return _event == null && _spawnTime == null && isOnStart;
	}
	
	/**
	 * Sets the {@link MultiSpawn} to the {@link NpcMaker}. Used only for creation of {@link NpcMaker} by {@link SpawnManager}.
	 * @param spawns : {@link List} of all {@link MultiSpawn}.
	 */
	public final void setSpawns(List<MultiSpawn> spawns)
	{
		_spawns = spawns;
	}
	
	/**
	 * @return the {@link List} of all {@link MultiSpawn} of the {@link NpcMaker}.
	 */
	public final List<MultiSpawn> getSpawns()
	{
		return _spawns;
	}
	
	/**
	 * @return the amount of currently spawned {@link Npc}s by the {@link NpcMaker}.
	 */
	public final int getNpcsAlive()
	{
		return _npcs;
	}
	
	/**
	 * @return the amount of currently decayed {@link Npc}s by the {@link NpcMaker}.
	 */
	public final long getNpcsDead()
	{
		return _spawns.stream().mapToLong(sd -> sd.getDecayed()).sum();
	}
	
	/**
	 * @return the list of registered {@link Quest}s.
	 */
	public final List<Quest> getQuestEvents()
	{
		return _questEvents;
	}
	
	/**
	 * Add a {@link Quest} on _questEvents {@link List}. Generate {@link List} if not existing (lazy initialization).<br>
	 * If already existing, we remove and add it back.
	 * @param quest : The {@link Quest} to add.
	 */
	public final void addQuestEvent(Quest quest)
	{
		if (_questEvents.isEmpty())
			_questEvents = new ArrayList<>(3);
		
		_questEvents.remove(quest);
		_questEvents.add(quest);
	}
	
	/**
	 * Spawns {@link Npc}s of this {@link NpcMaker} up maximum defined count.
	 * @return the amount of spawned {@link Npc}s.
	 */
	public final synchronized int spawnAll()
	{
		if (_maker != null)
			_maker.onStart(this);
		
		return _npcs;
	}
	
	/**
	 * Handles {@link Npc} spawn event in the {@link NpcMaker}.
	 * @param npc : The spawned {@link Npc}.
	 */
	public final void onSpawn(Npc npc)
	{
		_npcs++;
		if (_maker != null)
			_maker.onNpcCreated(npc, (MultiSpawn) npc.getSpawn(), this);
	}
	
	/**
	 * Handles {@link Npc} decay event in the {@link NpcMaker}.
	 * @param npc : The despawned {@link Npc}.
	 */
	public final void onDecay(Npc npc)
	{
		if (--_npcs == 0)
		{
			for (Quest quest : _questEvents)
				quest.onMakerNpcsKilled(this, npc);
		}
		
		if (_maker != null)
		{
			boolean shouldCallEvent = false;
			
			for (MultiSpawn ms : _spawns)
				for (Npc msNpc : ms.getNpcs())
					if (msNpc.getObjectId() == npc.getObjectId())
						shouldCallEvent = true;
					
			if (shouldCallEvent)
				_maker.onNpcDeleted(npc, (MultiSpawn) npc.getSpawn(), this);
		}
	}
	
	/**
	 * Immediately respawns all {@link Npc}s of this {@link NpcMaker}.
	 * @return the amount of respawned {@link Npc}s.
	 */
	public final synchronized int respawnAll()
	{
		int npcs = _npcs;
		
		// Respawn all NPCs.
		_spawns.forEach(MultiSpawn::doRespawn);
		
		return _npcs - npcs;
	}
	
	/**
	 * Deletes all {@link Npc}s of this {@link NpcMaker}.
	 * @return the amount of despawned {@link Npc}s.
	 */
	public final synchronized int deleteAll()
	{
		int npcs = _npcs;
		
		// Delete spawned NPCs.
		// Note: must set npcs to 0 (prevents onMakerNpcsKilled event to be called)
		_npcs = 0;
		_spawns.forEach(MultiSpawn::doDelete);
		
		_npcs = 0;
		return npcs;
	}
}