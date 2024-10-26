package com.shnok.javaserver.gameserver.scripting.script.ai.boss.frintezza;

import java.util.concurrent.atomic.AtomicInteger;

import com.shnok.javaserver.Config;
import com.shnok.javaserver.gameserver.data.manager.SpawnManager;
import com.shnok.javaserver.gameserver.data.manager.ZoneManager;
import com.shnok.javaserver.gameserver.model.actor.Npc;
import com.shnok.javaserver.gameserver.model.actor.Player;
import com.shnok.javaserver.gameserver.model.group.CommandChannel;
import com.shnok.javaserver.gameserver.model.group.Party;
import com.shnok.javaserver.gameserver.model.location.SpawnLocation;
import com.shnok.javaserver.gameserver.model.memo.GlobalMemo;
import com.shnok.javaserver.gameserver.model.spawn.NpcMaker;
import com.shnok.javaserver.gameserver.model.zone.type.BossZone;
import com.shnok.javaserver.gameserver.network.NpcStringId;
import com.shnok.javaserver.gameserver.network.SystemMessageId;
import com.shnok.javaserver.gameserver.network.serverpackets.ExShowScreenMessage;
import com.shnok.javaserver.gameserver.scripting.script.ai.individual.DefaultNpc;

public class FrintezzaTeleporter extends DefaultNpc
{
	private static final SpawnLocation[] TELE_LOCS =
	{
		new SpawnLocation(173247, -76979, -5104, 0),
		new SpawnLocation(174985, -75161, -5104, 0),
		new SpawnLocation(173917, -76109, -5104, 0),
		new SpawnLocation(174037, -76321, -5104, 0),
		new SpawnLocation(173249, -75154, -5104, 0)
	};
	
	private static final String MAKER_NAME_PATROL = "godard32_2515_05m1";
	private static final String MAKER_NAME_WIZARD_1 = "godard32_2515_01m1";
	private static final String MAKER_NAME_WIZARD_2 = "godard32_2515_02m1";
	private static final String MAKER_NAME_WIZARD_3 = "godard32_2515_03m1";
	private static final String MAKER_NAME_WIZARD_4 = "godard32_2515_04m1";
	private static final String MAKER_NAME_WIZARD_5 = "godard32_2515_08m1";
	private static final String MAKER_NAME_ALARM_1 = "godard32_2515_22m1";
	private static final String MAKER_NAME_ALARM_2 = "godard32_2515_19m1";
	private static final String MAKER_NAME_ALARM_3 = "godard32_2515_20m1";
	private static final String MAKER_NAME_ALARM_4 = "godard32_2515_21m1";
	
	private static final String T_DOOR_NAME_1 = "grave_pathway_1";
	private static final String T_DOOR_NAME_2 = "grave_pathway_2";
	private static final String T_DOOR_NAME_3 = "grave_pathway_3";
	private static final String T_DOOR_NAME_4 = "grave_pathway_4";
	private static final String WALL_DOOR_NAME_1A = "wall_door_a_center_1";
	private static final String WALL_DOOR_NAME_2A = "wall_door_a_center_2";
	private static final String WALL_DOOR_NAME_3A = "wall_door_a_center_3";
	private static final String WALL_DOOR_NAME_4A = "wall_door_a_center_4";
	private static final String WALL_DOOR_NAME_5A = "wall_door_a_center_5";
	private static final String WALL_DOOR_NAME_6A = "wall_door_a_center_6";
	private static final String WALL_DOOR_NAME_7A = "wall_door_a_center_7";
	private static final String WALL_DOOR_NAME_8A = "wall_door_a_center_8";
	private static final String WALL_DOOR_NAME_1B = "wall_door_b_left_1";
	private static final String WALL_DOOR_NAME_2B = "wall_door_b_left_2";
	private static final String WALL_DOOR_NAME_3B = "wall_door_b_left_3";
	private static final String WALL_DOOR_NAME_4B = "wall_door_b_left_4";
	private static final String WALL_DOOR_NAME_5B = "wall_door_b_left_5";
	private static final String WALL_DOOR_NAME_6B = "wall_door_b_right_1";
	private static final String WALL_DOOR_NAME_7B = "wall_door_b_right_2";
	private static final String WALL_DOOR_NAME_8B = "wall_door_b_right_3";
	private static final String WALL_DOOR_NAME_9B = "wall_door_b_right_4";
	private static final String WALL_DOOR_NAME_10B = "wall_door_b_right_5";
	
	private AtomicInteger _av_quest0 = new AtomicInteger();
	
	private Party _p_quest0;
	private Party _p_quest1;
	private Party _p_quest2;
	private Party _p_quest3;
	private Party _p_quest4;
	
	public FrintezzaTeleporter()
	{
		super("ai/boss/frintezza");
		
		addTalkId(32011);
		addFirstTalkId(32011);
	}
	
	public FrintezzaTeleporter(String descr)
	{
		super(descr);
		
		addTalkId(32011);
		addFirstTalkId(32011);
	}
	
	protected final int[] _npcIds =
	{
		32011 // frintessa_teleporter
	};
	
	@Override
	public void onCreated(Npc npc)
	{
		final Npc c0 = (Npc) GlobalMemo.getInstance().getCreature("4");
		
		npc._i_ai0 = (c0 == null) ? 0 : c0.getSpawn().getSpawnData().getDBValue();
		npc._i_ai1 = 0;
		npc._i_quest0 = 0;
		npc._i_quest1 = 0;
		npc._i_quest2 = 0;
		npc._i_quest3 = 0;
		npc._i_quest4 = 0;
		
		startQuestTimer("5000", npc, null, 60000);
		
		super.onCreated(npc);
	}
	
	@Override
	public String onFirstTalk(Npc npc, Player player)
	{
		return "frintessa_teleporter001.htm";
	}
	
	@Override
	public String onAdvEvent(String event, Npc npc, Player player)
	{
		String htmltext = "";
		
		if (event.equalsIgnoreCase("story"))
			htmltext = "frintessa_teleporter001a.htm";
		else if (event.equalsIgnoreCase("enter"))
		{
			if (player.isOverweight())
			{
				player.sendPacket(SystemMessageId.INVENTORY_LESS_THAN_80_PERCENT);
				return htmltext;
			}
			
			final Npc c1 = (Npc) GlobalMemo.getInstance().getCreature("4");
			final Npc c2 = (Npc) GlobalMemo.getInstance().getCreature("5");
			final Npc c3 = (Npc) GlobalMemo.getInstance().getCreature("6");
			
			if (npc._i_ai0 != 0)
			{
				if (c1 == null || c3 == null)
					htmltext = "frintessa_teleporter002.htm";
				else
					htmltext = "frintessa_teleporter007.htm";
				
				return htmltext;
			}
			
			if (c1 == null || c2 == null || c3 == null)
				return "frintessa_teleporter002.htm";
			
			final Party playerParty = player.getParty();
			
			final CommandChannel commandChannel = (playerParty == null) ? null : playerParty.getCommandChannel();
			if (commandChannel == null)
				return "frintessa_teleporter003.htm";
			
			final Player c0 = commandChannel.getLeader();
			if (player != c0)
				htmltext = "frintessa_teleporter003.htm";
			else if (!player.getInventory().hasItem(8073))
				htmltext = "frintessa_teleporter004.htm";
			else
			{
				final int ccSize = commandChannel.getParties().size();
				if (ccSize < 4)
					htmltext = "frintessa_teleporter005.htm";
				else if (ccSize > 5)
					htmltext = "frintessa_teleporter006.htm";
				else
				{
					if (commandChannel.getMembers().stream().anyMatch(p -> !p.isIn3DRadius(npc, Config.PARTY_RANGE)))
						return "frintessa_teleporter008.htm";
					
					if (_av_quest0.compareAndExchange(1, 0) == 1)
						htmltext = "frintessa_teleporter007.htm";
					else
					{
						NpcMaker maker0 = SpawnManager.getInstance().getNpcMaker(MAKER_NAME_PATROL);
						if (maker0 != null)
							maker0.getMaker().onMakerScriptEvent("1001", maker0, 0, 0);
						
						maker0 = SpawnManager.getInstance().getNpcMaker(MAKER_NAME_WIZARD_1);
						if (maker0 != null)
							maker0.getMaker().onMakerScriptEvent("1001", maker0, 0, 0);
						
						maker0 = SpawnManager.getInstance().getNpcMaker(MAKER_NAME_WIZARD_2);
						if (maker0 != null)
							maker0.getMaker().onMakerScriptEvent("1001", maker0, 0, 0);
						
						maker0 = SpawnManager.getInstance().getNpcMaker(MAKER_NAME_WIZARD_3);
						if (maker0 != null)
							maker0.getMaker().onMakerScriptEvent("1001", maker0, 0, 0);
						
						maker0 = SpawnManager.getInstance().getNpcMaker(MAKER_NAME_WIZARD_4);
						if (maker0 != null)
							maker0.getMaker().onMakerScriptEvent("1001", maker0, 0, 0);
						
						maker0 = SpawnManager.getInstance().getNpcMaker(MAKER_NAME_ALARM_1);
						if (maker0 != null)
							maker0.getMaker().onMakerScriptEvent("1001", maker0, 0, 0);
						
						maker0 = SpawnManager.getInstance().getNpcMaker(MAKER_NAME_ALARM_2);
						if (maker0 != null)
							maker0.getMaker().onMakerScriptEvent("1001", maker0, 0, 0);
						
						maker0 = SpawnManager.getInstance().getNpcMaker(MAKER_NAME_ALARM_3);
						if (maker0 != null)
							maker0.getMaker().onMakerScriptEvent("1001", maker0, 0, 0);
						
						maker0 = SpawnManager.getInstance().getNpcMaker(MAKER_NAME_ALARM_4);
						if (maker0 != null)
							maker0.getMaker().onMakerScriptEvent("1001", maker0, 0, 0);
						
						takeItems(player, 8073, 1);
						
						// TODO Remove this after zone rework
						final BossZone frintezzaTomb = ZoneManager.getInstance().getZoneById(110012, BossZone.class);
						
						int i = 0;
						
						for (Party party : commandChannel.getParties())
						{
							if (i == 0)
								_p_quest0 = party;
							else if (i == 1)
								_p_quest1 = party;
							else if (i == 2)
								_p_quest2 = party;
							else if (i == 3)
								_p_quest3 = party;
							else if (i == 4)
								_p_quest4 = party;
							
							final SpawnLocation teleLoc = TELE_LOCS[i];
							
							for (Player member : party.getMembers())
							{
								frintezzaTomb.allowPlayerEntry(member, 30);
								
								member.teleportTo(teleLoc, teleLoc.getHeading());
								
								takeItems(member, 8192, -1);
								takeItems(member, 8556, -1);
							}
							
							// Check next Party.
							i++;
						}
						
						npc._i_ai0 = 1;
						
						c1.sendScriptEvent(npc.getObjectId(), 1, 0);
						
						if (c1.getSpawn().getSpawnData().getDBValue() >= 2)
							return "";
						
						commandChannel.broadcastOnScreen(10000, NpcStringId.ID_1010643, 35);
						
						startQuestTimer("4030", npc, null, 300000);
					}
				}
			}
		}
		return htmltext;
	}
	
	@Override
	public String onTimer(String name, Npc npc, Player player)
	{
		if (name.equalsIgnoreCase("2006"))
		{
			final Npc c0 = (Npc) GlobalMemo.getInstance().getCreature("4");
			final Npc c2 = (Npc) GlobalMemo.getInstance().getCreature("5");
			
			if (c0 != null && c2 != null && c0.getSpawn().getSpawnData().getDBValue() >= 2 && c2.getSpawn().getSpawnData().getDBValue() >= 2)
				return super.onTimer(name, npc, player);
			
			NpcMaker maker0 = SpawnManager.getInstance().getNpcMaker(MAKER_NAME_PATROL);
			if (maker0 != null)
				maker0.getMaker().onMakerScriptEvent("1000", maker0, 0, 0);
			
			maker0 = SpawnManager.getInstance().getNpcMaker(MAKER_NAME_WIZARD_1);
			if (maker0 != null)
				maker0.getMaker().onMakerScriptEvent("1000", maker0, 0, 0);
			
			maker0 = SpawnManager.getInstance().getNpcMaker(MAKER_NAME_WIZARD_2);
			if (maker0 != null)
				maker0.getMaker().onMakerScriptEvent("1000", maker0, 0, 0);
			
			maker0 = SpawnManager.getInstance().getNpcMaker(MAKER_NAME_WIZARD_3);
			if (maker0 != null)
				maker0.getMaker().onMakerScriptEvent("1000", maker0, 0, 0);
			
			maker0 = SpawnManager.getInstance().getNpcMaker(MAKER_NAME_WIZARD_4);
			if (maker0 != null)
				maker0.getMaker().onMakerScriptEvent("1000", maker0, 0, 0);
			
			maker0 = SpawnManager.getInstance().getNpcMaker(MAKER_NAME_WIZARD_5);
			if (maker0 != null)
				maker0.getMaker().onMakerScriptEvent("1000", maker0, 0, 0);
			
			maker0 = SpawnManager.getInstance().getNpcMaker(MAKER_NAME_ALARM_1);
			if (maker0 != null)
				maker0.getMaker().onMakerScriptEvent("1000", maker0, 0, 0);
			
			maker0 = SpawnManager.getInstance().getNpcMaker(MAKER_NAME_ALARM_2);
			if (maker0 != null)
				maker0.getMaker().onMakerScriptEvent("1000", maker0, 0, 0);
			
			maker0 = SpawnManager.getInstance().getNpcMaker(MAKER_NAME_ALARM_3);
			if (maker0 != null)
				maker0.getMaker().onMakerScriptEvent("1000", maker0, 0, 0);
			
			maker0 = SpawnManager.getInstance().getNpcMaker(MAKER_NAME_ALARM_4);
			if (maker0 != null)
				maker0.getMaker().onMakerScriptEvent("1000", maker0, 0, 0);
			
			openCloseDoor(T_DOOR_NAME_1, 1);
			openCloseDoor(T_DOOR_NAME_2, 1);
			openCloseDoor(T_DOOR_NAME_3, 0);
			openCloseDoor(T_DOOR_NAME_4, 0);
			openCloseDoor(WALL_DOOR_NAME_1A, 1);
			openCloseDoor(WALL_DOOR_NAME_2A, 1);
			openCloseDoor(WALL_DOOR_NAME_3A, 1);
			openCloseDoor(WALL_DOOR_NAME_4A, 1);
			openCloseDoor(WALL_DOOR_NAME_5A, 1);
			openCloseDoor(WALL_DOOR_NAME_6A, 1);
			openCloseDoor(WALL_DOOR_NAME_7A, 1);
			openCloseDoor(WALL_DOOR_NAME_8A, 1);
			openCloseDoor(WALL_DOOR_NAME_1B, 1);
			openCloseDoor(WALL_DOOR_NAME_2B, 1);
			openCloseDoor(WALL_DOOR_NAME_3B, 1);
			openCloseDoor(WALL_DOOR_NAME_4B, 1);
			openCloseDoor(WALL_DOOR_NAME_5B, 1);
			openCloseDoor(WALL_DOOR_NAME_6B, 1);
			openCloseDoor(WALL_DOOR_NAME_7B, 1);
			openCloseDoor(WALL_DOOR_NAME_8B, 1);
			openCloseDoor(WALL_DOOR_NAME_9B, 1);
			openCloseDoor(WALL_DOOR_NAME_10B, 1);
			
			npc.getSpawn().instantTeleportInMyTerritory(150037, -57255, -2976, 150);
		}
		else if (name.equalsIgnoreCase("2001"))
		{
			final Npc c0 = (Npc) GlobalMemo.getInstance().getCreature("4");
			
			if (c0 != null && c0.getSpawn().getSpawnData().getDBValue() < 2)
			{
				npc._i_ai0 = 0;
				
				npc.getSpawn().instantTeleportInMyTerritory(150037, -57255, -2976, 150);
				
				c0.sendScriptEvent(npc.getObjectId(), 0, 0);
				
				_av_quest0.set(0);
				
				NpcMaker maker0 = SpawnManager.getInstance().getNpcMaker(MAKER_NAME_PATROL);
				if (maker0 != null)
					maker0.getMaker().onMakerScriptEvent("1000", maker0, 0, 0);
				
				maker0 = SpawnManager.getInstance().getNpcMaker(MAKER_NAME_WIZARD_1);
				if (maker0 != null)
					maker0.getMaker().onMakerScriptEvent("1000", maker0, 0, 0);
				
				maker0 = SpawnManager.getInstance().getNpcMaker(MAKER_NAME_WIZARD_2);
				if (maker0 != null)
					maker0.getMaker().onMakerScriptEvent("1000", maker0, 0, 0);
				
				maker0 = SpawnManager.getInstance().getNpcMaker(MAKER_NAME_WIZARD_3);
				if (maker0 != null)
					maker0.getMaker().onMakerScriptEvent("1000", maker0, 0, 0);
				
				maker0 = SpawnManager.getInstance().getNpcMaker(MAKER_NAME_WIZARD_4);
				if (maker0 != null)
					maker0.getMaker().onMakerScriptEvent("1000", maker0, 0, 0);
				
				maker0 = SpawnManager.getInstance().getNpcMaker(MAKER_NAME_WIZARD_5);
				if (maker0 != null)
					maker0.getMaker().onMakerScriptEvent("1000", maker0, 0, 0);
				
				maker0 = SpawnManager.getInstance().getNpcMaker(MAKER_NAME_ALARM_1);
				if (maker0 != null)
					maker0.getMaker().onMakerScriptEvent("1000", maker0, 0, 0);
				
				maker0 = SpawnManager.getInstance().getNpcMaker(MAKER_NAME_ALARM_2);
				if (maker0 != null)
					maker0.getMaker().onMakerScriptEvent("1000", maker0, 0, 0);
				
				maker0 = SpawnManager.getInstance().getNpcMaker(MAKER_NAME_ALARM_3);
				if (maker0 != null)
					maker0.getMaker().onMakerScriptEvent("1000", maker0, 0, 0);
				
				maker0 = SpawnManager.getInstance().getNpcMaker(MAKER_NAME_ALARM_4);
				if (maker0 != null)
					maker0.getMaker().onMakerScriptEvent("1000", maker0, 0, 0);
				
				openCloseDoor(T_DOOR_NAME_1, 1);
				openCloseDoor(T_DOOR_NAME_2, 1);
				openCloseDoor(T_DOOR_NAME_3, 0);
				openCloseDoor(T_DOOR_NAME_4, 0);
				openCloseDoor(WALL_DOOR_NAME_1A, 1);
				openCloseDoor(WALL_DOOR_NAME_2A, 1);
				openCloseDoor(WALL_DOOR_NAME_3A, 1);
				openCloseDoor(WALL_DOOR_NAME_4A, 1);
				openCloseDoor(WALL_DOOR_NAME_5A, 1);
				openCloseDoor(WALL_DOOR_NAME_6A, 1);
				openCloseDoor(WALL_DOOR_NAME_7A, 1);
				openCloseDoor(WALL_DOOR_NAME_8A, 1);
				openCloseDoor(WALL_DOOR_NAME_1B, 1);
				openCloseDoor(WALL_DOOR_NAME_2B, 1);
				openCloseDoor(WALL_DOOR_NAME_3B, 1);
				openCloseDoor(WALL_DOOR_NAME_4B, 1);
				openCloseDoor(WALL_DOOR_NAME_5B, 1);
				openCloseDoor(WALL_DOOR_NAME_6B, 1);
				openCloseDoor(WALL_DOOR_NAME_7B, 1);
				openCloseDoor(WALL_DOOR_NAME_8B, 1);
				openCloseDoor(WALL_DOOR_NAME_9B, 1);
				openCloseDoor(WALL_DOOR_NAME_10B, 1);
				
				final Npc c1 = (Npc) GlobalMemo.getInstance().getCreature("5");
				if (c1 != null)
					c1.sendScriptEvent(npc.getObjectId(), 0, 0);
				
				startQuestTimer("5000", npc, null, ((30 * 60) * 1000));
			}
		}
		else if (name.equalsIgnoreCase("5000"))
		{
			final Npc c0 = (Npc) GlobalMemo.getInstance().getCreature("4");
			final Npc c2 = (Npc) GlobalMemo.getInstance().getCreature("5");
			
			if (c0 != null && c2 != null)
			{
				if (c0.getSpawn().getSpawnData().getDBValue() == 0 && c2.getSpawn().getSpawnData().getDBValue() == 0)
				{
					npc._i_ai0 = 0;
					_av_quest0.set(0);
					
					npc.getSpawn().instantTeleportInMyTerritory(150037, -57255, -2976, 150);
				}
				else if (c0.getSpawn().getSpawnData().getDBValue() >= 3)
					npc.getSpawn().instantTeleportInMyTerritory(150037, -57255, -2976, 150);
			}
			startQuestTimer("5000", npc, null, (60 * 1000));
		}
		else if (name.equalsIgnoreCase("4030"))
		{
			final Npc c0 = (Npc) GlobalMemo.getInstance().getCreature("4");
			final Npc c2 = (Npc) GlobalMemo.getInstance().getCreature("5");
			
			if (c0 != null && c2 != null && c0.getSpawn().getSpawnData().getDBValue() >= 2)
				return super.onTimer(name, npc, player);
			
			notifyRemainingTime(30);
			
			startQuestTimer("4025", npc, null, ((5 * 60) * 1000));
		}
		else if (name.equalsIgnoreCase("4025"))
		{
			final Npc c0 = (Npc) GlobalMemo.getInstance().getCreature("4");
			final Npc c2 = (Npc) GlobalMemo.getInstance().getCreature("5");
			
			if (c0 != null && c2 != null && c0.getSpawn().getSpawnData().getDBValue() >= 2)
				return super.onTimer(name, npc, player);
			
			notifyRemainingTime(25);
			
			startQuestTimer("4020", npc, null, ((5 * 60) * 1000));
		}
		else if (name.equalsIgnoreCase("4020"))
		{
			final Npc c0 = (Npc) GlobalMemo.getInstance().getCreature("4");
			final Npc c2 = (Npc) GlobalMemo.getInstance().getCreature("5");
			
			if (c0 != null && c2 != null && c0.getSpawn().getSpawnData().getDBValue() >= 2)
				return super.onTimer(name, npc, player);
			
			notifyRemainingTime(20);
			
			startQuestTimer("4015", npc, null, ((5 * 60) * 1000));
		}
		else if (name.equalsIgnoreCase("4015"))
		{
			final Npc c0 = (Npc) GlobalMemo.getInstance().getCreature("4");
			final Npc c2 = (Npc) GlobalMemo.getInstance().getCreature("5");
			
			if (c0 != null && c2 != null && c0.getSpawn().getSpawnData().getDBValue() >= 2)
				return super.onTimer(name, npc, player);
			
			notifyRemainingTime(15);
			
			startQuestTimer("4010", npc, null, ((5 * 60) * 1000));
		}
		else if (name.equalsIgnoreCase("4010"))
		{
			final Npc c0 = (Npc) GlobalMemo.getInstance().getCreature("4");
			final Npc c2 = (Npc) GlobalMemo.getInstance().getCreature("5");
			
			if (c0 != null && c2 != null && c0.getSpawn().getSpawnData().getDBValue() >= 2)
				return super.onTimer(name, npc, player);
			
			notifyRemainingTime(10);
			
			startQuestTimer("4005", npc, null, ((5 * 60) * 1000));
		}
		else if (name.equalsIgnoreCase("4005"))
		{
			final Npc c0 = (Npc) GlobalMemo.getInstance().getCreature("4");
			final Npc c2 = (Npc) GlobalMemo.getInstance().getCreature("5");
			
			if (c0 != null && c2 != null && c0.getSpawn().getSpawnData().getDBValue() >= 2)
				return super.onTimer(name, npc, player);
			
			notifyRemainingTime(5);
			
			startQuestTimer("4004", npc, null, (60 * 1000));
		}
		else if (name.equalsIgnoreCase("4004"))
		{
			final Npc c0 = (Npc) GlobalMemo.getInstance().getCreature("4");
			final Npc c2 = (Npc) GlobalMemo.getInstance().getCreature("5");
			
			if (c0 != null && c2 != null && c0.getSpawn().getSpawnData().getDBValue() >= 2)
				return super.onTimer(name, npc, player);
			
			notifyRemainingTime(4);
			
			startQuestTimer("4003", npc, null, (60 * 1000));
		}
		else if (name.equalsIgnoreCase("4003"))
		{
			final Npc c0 = (Npc) GlobalMemo.getInstance().getCreature("4");
			final Npc c2 = (Npc) GlobalMemo.getInstance().getCreature("5");
			
			if (c0 != null && c2 != null && c0.getSpawn().getSpawnData().getDBValue() >= 2)
				return super.onTimer(name, npc, player);
			
			notifyRemainingTime(3);
			
			startQuestTimer("4002", npc, null, (60 * 1000));
		}
		else if (name.equalsIgnoreCase("4002"))
		{
			final Npc c0 = (Npc) GlobalMemo.getInstance().getCreature("4");
			final Npc c2 = (Npc) GlobalMemo.getInstance().getCreature("5");
			
			if (c0 != null && c2 != null && c0.getSpawn().getSpawnData().getDBValue() >= 2)
				return super.onTimer(name, npc, player);
			
			notifyRemainingTime(2);
			
			startQuestTimer("4001", npc, null, (60 * 1000));
		}
		else if (name.equalsIgnoreCase("4001"))
		{
			final Npc c0 = (Npc) GlobalMemo.getInstance().getCreature("4");
			final Npc c2 = (Npc) GlobalMemo.getInstance().getCreature("5");
			
			if (c0 != null && c2 != null && c0.getSpawn().getSpawnData().getDBValue() >= 2)
				return super.onTimer(name, npc, player);
			
			notifyRemainingTime(1);
			
			startQuestTimer("2001", npc, null, (60 * 1000));
		}
		
		return super.onTimer(name, npc, player);
	}
	
	@Override
	public void onScriptEvent(Npc npc, int eventId, int arg1, int arg2)
	{
		final Npc c0 = (Npc) GlobalMemo.getInstance().getCreature("4");
		if (c0 != null && eventId == c0.getObjectId())
		{
			if (arg1 == 0)
			{
				npc._i_ai0 = 0;
				
				_av_quest0.set(0);
			}
			else if (arg1 == 8)
				npc._i_ai0 = 8;
		}
	}
	
	private void notifyRemainingTime(int leftMinutes)
	{
		final ExShowScreenMessage essm = new ExShowScreenMessage(NpcStringId.ID_1010643.getMessage(leftMinutes), 10000);
		
		if (_p_quest0 != null)
			_p_quest0.broadcastPacket(essm);
		
		if (_p_quest1 != null)
			_p_quest1.broadcastPacket(essm);
		
		if (_p_quest2 != null)
			_p_quest2.broadcastPacket(essm);
		
		if (_p_quest3 != null)
			_p_quest3.broadcastPacket(essm);
		
		if (_p_quest4 != null)
			_p_quest4.broadcastPacket(essm);
	}
}