package com.shnok.javaserver.gameserver.scripting.quest;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.shnok.javaserver.commons.random.Rnd;

import com.shnok.javaserver.Config;
import com.shnok.javaserver.gameserver.data.manager.SevenSignsManager;
import com.shnok.javaserver.gameserver.enums.CabalType;
import com.shnok.javaserver.gameserver.enums.EventHandler;
import com.shnok.javaserver.gameserver.enums.PeriodType;
import com.shnok.javaserver.gameserver.enums.QuestStatus;
import com.shnok.javaserver.gameserver.model.RoomInfo;
import com.shnok.javaserver.gameserver.model.World;
import com.shnok.javaserver.gameserver.model.actor.Npc;
import com.shnok.javaserver.gameserver.model.actor.Player;
import com.shnok.javaserver.gameserver.model.group.Party;
import com.shnok.javaserver.gameserver.model.location.Location;
import com.shnok.javaserver.gameserver.model.spawn.MultiSpawn;
import com.shnok.javaserver.gameserver.model.spawn.NpcMaker;
import com.shnok.javaserver.gameserver.network.serverpackets.Earthquake;
import com.shnok.javaserver.gameserver.scripting.Quest;
import com.shnok.javaserver.gameserver.scripting.QuestState;
import com.shnok.javaserver.gameserver.taskmanager.GameTimeTaskManager;

public class Q635_InTheDimensionalRift extends Quest
{
	private static final int[] FRAGMENT_COSTS =
	{
		Config.RIFT_ENTER_COST_RECRUIT,
		Config.RIFT_ENTER_COST_SOLDIER,
		Config.RIFT_ENTER_COST_OFFICER,
		Config.RIFT_ENTER_COST_CAPTAIN,
		Config.RIFT_ENTER_COST_COMMANDER,
		Config.RIFT_ENTER_COST_HERO
	};
	
	private static final String QUEST_NAME = "Q635_InTheDimensionalRift";
	
	public static final Map<String, List<RoomInfo>> ROOMS = new HashMap<>();
	
	private static final Location TELE_IN_LOC = new Location(-114796, -179334, -6752);
	
	private static final Location[] ORACLE_OF_DUSK_LOCS =
	{
		new Location(-81328, 86536, -5152),
		new Location(-81262, 86468, -5152),
		new Location(-81248, 86582, -5152)
	};
	
	private static final Location[] ORACLE_OF_DAWN_LOCS =
	{
		new Location(-80316, 111356, -4896),
		new Location(-80226, 111290, -4896),
		new Location(-80217, 111435, -4896)
	};
	
	// Items
	private static final int DIMENSIONAL_FRAGMENT = 7079;
	
	public Q635_InTheDimensionalRift()
	{
		super(635, "In the Dimensional Rift");
		
		// Rift Post NPCs.
		for (int i = 31488; i < 31494; i++)
			addEventIds(i, EventHandler.CREATED, EventHandler.FIRST_TALK, EventHandler.TALKED);
		
		// Dimensional Rift Keepers.
		for (int i = 31494; i < 31508; i++)
			addEventIds(i, EventHandler.QUEST_START, EventHandler.TALKED);
		
		// Guardian of Border NPCs.
		for (int i = 31865; i < 31919; i++)
			addEventIds(i, EventHandler.CREATED, EventHandler.FIRST_TALK, EventHandler.SCRIPT_EVENT, EventHandler.TALKED);
	}
	
	@Override
	public String onAdvEvent(String event, Npc npc, Player player)
	{
		if (event.equalsIgnoreCase("dimension_keeper_07.htm"))
		{
			final QuestState st = player.getQuestList().getQuestState(QUEST_NAME);
			if (st.isStarted())
				event = "dimension_keeper_07a.htm";
			else
			{
				player.getMemos().set("rift", (npc.getNpcId() - 31493) * 10000);
				
				st.setState(QuestStatus.STARTED);
				st.setCond(1);
				
				playSound(player, SOUND_ACCEPT);
			}
			player.teleportTo(TELE_IN_LOC, 0);
			
			return event;
		}
		else if (event.equalsIgnoreCase("rift_watcher_05.htm"))
		{
			Party party0 = player.getParty();
			if (party0 == null || party0.getMembersCount() < Config.RIFT_MIN_PARTY_SIZE)
				return "rift_watcher_01.htm";
			
			Player c0 = party0.getLeader();
			if (player != c0)
			{
				String html = getHtmlText("rift_watcher_02.htm");
				html = html.replace("<?name?>", c0.getName());
				return html;
			}
			
			int i3 = -1;
			int i6 = GameTimeTaskManager.getInstance().getCurrentTick();
			
			final int stonesToRemove = FRAGMENT_COSTS[npc.getNpcId() - 31488];
			
			for (Player c1 : party0.getMembers())
			{
				final QuestState st = c1.getQuestList().getQuestState("Q635_InTheDimensionalRift");
				
				if (st == null || !st.isStarted())
				{
					String html = getHtmlText("rift_watcher_03a.htm");
					html = html.replace("<?name?>", c1.getName());
					return html;
				}
				
				if (c1.getInventory().getItemCount(DIMENSIONAL_FRAGMENT) < stonesToRemove)
				{
					final int i5 = getMemoStateEx(c1, 635, 1);
					if (getMemoState(c1, 635) != 2 || (i6 - i5) > 3600)
					{
						String html = getHtmlText("rift_watcher_03.htm");
						html = html.replace("<?name?>", c1.getName());
						return html;
					}
				}
			}
			
			final String levelName = npc.getSpawn().getMemo().get("LevelName");
			if (levelName == null)
				return null;
			
			final List<RoomInfo> rlist0 = ROOMS.get(levelName);
			if (rlist0 != null)
			{
				i3 = -1;
				int i5 = 0;
				RoomInfo room0 = null;
				
				for (int i4 = 0; i4 < 8; i4++)
				{
					room0 = rlist0.get(i4);
					
					if (room0.isLocked())
					{
						if (room0.getParty() == party0)
							i3 = -2;
					}
					else
						i5 = (i5 + 1);
				}
				
				if (i3 != -2 && i5 >= 3)
				{
					i3 = -1;
					
					for (int i4 = 0; i4 < 8; i4++)
					{
						if (i3 == -1)
						{
							i5 = Rnd.get(8);
							room0 = rlist0.get(i5);
							
							if (!room0.isLocked())
							{
								room0.setLock(true);
								i3 = i5;
							}
						}
					}
					
					if (i3 == -1)
					{
						for (int i4 = 0; i4 < 8; i4++)
						{
							if (i3 == -1)
							{
								room0 = rlist0.get(i4);
								
								if (!room0.isLocked())
								{
									room0.setLock(true);
									i3 = i4;
								}
							}
						}
					}
					
					if (room0 != null)
						room0.setParty(party0);
				}
			}
			
			if (i3 == -1)
				return "rift_watcher_04.htm";
			
			if (i3 == -2)
				return "rift_watcher_08.htm";
			
			final int posX = npc.getSpawn().getMemo().getInteger("PosX" + i3, 0);
			final int posY = npc.getSpawn().getMemo().getInteger("PosY" + i3, 0);
			final int currentTick = GameTimeTaskManager.getInstance().getCurrentTick();
			
			for (Player member : party0.getMembers())
			{
				if (npc.distance2D(member) > 1500)
					continue;
				
				member.destroyItemByItemId(DIMENSIONAL_FRAGMENT, stonesToRemove, true);
				
				setMemoState(member, 635, currentTick);
				setMemoStateEx(member, 635, 1, (member == party0.getLeader()) ? 1 : 0);
				
				member.teleportTo(posX, posY, npc.getZ(), 0);
			}
			
			return event;
		}
		else if (event.equalsIgnoreCase("rift_watcher_06.htm"))
		{
			int i0 = player.getMemos().getInteger("rift", 0);
			int i1 = (i0 % 10000);
			int i2 = (((i0 - i1) + 5) / 10000);
			CabalType i6 = SevenSignsManager.getInstance().getPlayerCabal(player.getObjectId());
			
			if (i1 >= 95 && i1 < 195)
			{
				switch (i6)
				{
					case DAWN:
						player.teleportTo(-80542, 150315, -3040, 0);
						break;
					
					case DUSK:
						player.teleportTo(-82340, 151575, -3120, 0);
						break;
				}
			}
			else if (i1 >= 195 && i1 < 295)
			{
				switch (i6)
				{
					case DAWN:
						player.teleportTo(-13996, 121413, -2984, 0);
						break;
					
					case DUSK:
						player.teleportTo(-14727, 124002, -3112, 0);
						break;
				}
			}
			else if (i1 >= 295 && i1 < 395)
			{
				switch (i6)
				{
					case DAWN:
						player.teleportTo(16320, 142915, -2696, 0);
						break;
					
					case DUSK:
						player.teleportTo(18501, 144673, -3056, 0);
						break;
				}
			}
			else if (i1 >= 395 && i1 < 495)
			{
				switch (i6)
				{
					case DAWN:
						player.teleportTo(83312, 149236, -3400, 0);
						break;
					
					case DUSK:
						player.teleportTo(81572, 148580, -3464, 0);
						break;
				}
			}
			else if (i1 >= 495 && i1 < 595)
			{
				switch (i6)
				{
					case DAWN:
						player.teleportTo(111359, 220959, -3544, 0);
						break;
					
					case DUSK:
						player.teleportTo(112441, 220149, -3544, 0);
						break;
				}
			}
			else if (i1 >= 595 && i1 < 695)
			{
				switch (i6)
				{
					case DAWN:
						player.teleportTo(83057, 53983, -1488, 0);
						break;
					
					case DUSK:
						player.teleportTo(82842, 54613, -1520, 0);
						break;
				}
			}
			else if (i1 >= 695 && i1 < 795)
			{
				switch (i6)
				{
					case DAWN:
						player.teleportTo(146955, 26690, -2200, 0);
						break;
					
					case DUSK:
						player.teleportTo(147528, 28899, -2264, 0);
						break;
				}
			}
			else if (i1 >= 795 && i1 < 895)
			{
				switch (i6)
				{
					case DAWN:
						player.teleportTo(115206, 74775, -2600, 0);
						break;
					
					case DUSK:
						player.teleportTo(116651, 77512, -2688, 0);
						break;
				}
			}
			else if (i1 >= 995 && i1 < 1095)
			{
				switch (i6)
				{
					case DAWN:
						player.teleportTo(148326, -55533, -2776, 0);
						break;
					
					case DUSK:
						player.teleportTo(149968, -56645, -2976, 0);
						break;
				}
			}
			else if (i1 >= 1095 && i0 < 1195)
			{
				switch (i6)
				{
					case DAWN:
						player.teleportTo(45605, -50360, -792, 0);
						break;
					
					case DUSK:
						player.teleportTo(44505, -48331, -792, 0);
						break;
				}
			}
			else if (i1 >= 1195 && i0 < 1295)
			{
				switch (i6)
				{
					case DAWN:
						player.teleportTo(86730, -143148, -1336, 0);
						break;
					
					case DUSK:
						player.teleportTo(85048, -142046, -1536, 0);
						break;
				}
			}
			else if (i2 == 1)
				player.teleportTo(-41443, 210030, -5080, 0);
			else if (i2 == 2)
				player.teleportTo(-53034, -250421, -7935, 0);
			else if (i2 == 3)
				player.teleportTo(45160, 123605, -5408, 0);
			else if (i2 == 4)
				player.teleportTo(46488, 170184, -4976, 0);
			else if (i2 == 5)
				player.teleportTo(111521, 173905, -5432, 0);
			else if (i2 == 6)
				player.teleportTo(-20395, -250930, -8191, 0);
			else if (i2 == 7)
				player.teleportTo(-21482, 77253, -5168, 0);
			else if (i2 == 8)
				player.teleportTo(140688, 79565, -5424, 0);
			else if (i2 == 9)
				player.teleportTo(-52007, 78986, -4736, 0);
			else if (i2 == 10)
				player.teleportTo(118547, 132669, -4824, 0);
			else if (i2 == 11)
				player.teleportTo(172562, -17730, -4896, 0);
			else if (i2 == 12)
				player.teleportTo(83344, 209110, -5432, 0);
			else if (i2 == 13)
				player.teleportTo(-19154, 13415, -4896, 0);
			else if (i2 == 14)
				player.teleportTo(12747, -248614, -9607, 0);
			else if (i2 == 21)
				player.teleportTo(-41559, 209140, -5080, 0);
			else if (i2 == 22)
				player.teleportTo(42448, 143943, -5376, 0);
			else if (i2 == 23)
				player.teleportTo(45239, 124522, -5408, 0);
			else if (i2 == 24)
				player.teleportTo(45680, 170299, -4976, 0);
			else if (i2 == 25)
				player.teleportTo(110659, 174008, -5432, 0);
			else if (i2 == 26)
				player.teleportTo(77132, 78399, -5120, 0);
			else if (i2 == 27)
				player.teleportTo(-22408, 77375, -5168, 0);
			else if (i2 == 28)
				player.teleportTo(139807, 79675, -5424, 0);
			else if (i2 == 29)
				player.teleportTo(-53177, 79100, -4736, 0);
			else if (i2 == 30)
				player.teleportTo(117647, 132801, -4824, 0);
			else if (i2 == 31)
				player.teleportTo(171684, -17602, -4896, 0);
			else if (i2 == 32)
				player.teleportTo(82456, 209218, -5432, 0);
			else if (i2 == 33)
				player.teleportTo(-20105, 13505, -4896, 0);
			else if (i2 == 34)
				player.teleportTo(113299, 84547, -6536, 0);
		}
		else if (event.equalsIgnoreCase("rift_watcher_10.htm"))
		{
			if (SevenSignsManager.getInstance().getCurrentPeriod() != PeriodType.COMPETITION)
				return "rift_watcher_12.htm";
			
			switch (SevenSignsManager.getInstance().getPlayerCabal(player.getObjectId()))
			{
				case DUSK:
					player.teleportTo(Rnd.get(ORACLE_OF_DUSK_LOCS), 0);
					break;
				
				case DAWN:
					player.teleportTo(Rnd.get(ORACLE_OF_DAWN_LOCS), 0);
					break;
				
				default:
					return "rift_watcher_11.htm";
			}
		}
		else if (event.equalsIgnoreCase("GiveUpRift"))
		{
			final String levelName = npc.getSpawn().getMemo().get("LevelName");
			final int roomIndex = npc.getSpawn().getMemo().getInteger("RoomIndex");
			
			final List<RoomInfo> rlist0 = ROOMS.get(levelName);
			final RoomInfo room0 = rlist0.get(roomIndex);
			
			final Party party0 = room0.getParty();
			if (party0 == null || !party0.isLeader(player))
				return "tel_dungeon_npc_notleader.htm";
			
			oustPlayers(npc);
			
			final NpcMaker maker0 = ((MultiSpawn) npc.getSpawn()).getNpcMaker();
			maker0.getMaker().onMakerScriptEvent("0", maker0, 0, 0);
			
			for (int i0 : room0.getMemberIds())
			{
				final Player c0 = World.getInstance().getPlayer(i0);
				if (c0 != null)
					setMemoStateEx(c0, 635, 1, -1);
			}
			
			room0.clear();
			return null;
		}
		else if (event.equalsIgnoreCase("ChangeRiftRoom"))
		{
			if (!player.getParty().isLeader(player))
				return "tel_dungeon_npc_notleader.htm";
			
			final int i0 = getMemoStateEx(player, 635, 1);
			if (i0 != 1)
				return "tel_dungeon_npc_nomorechance.htm";
			
			setMemoStateEx(player, 635, 1, 0);
			
			final String levelName = npc.getSpawn().getMemo().get("LevelName");
			final List<RoomInfo> rlist0 = ROOMS.get(levelName);
			
			final List<RoomInfo> rlist1 = rlist0.stream().filter(r -> !r.isLocked() && r.getIndex() != 8).toList();
			if (rlist1.isEmpty())
				npc._i_ai4 = -1;
			else
			{
				final int roomIndex = npc.getSpawn().getMemo().getInteger("RoomIndex");
				final RoomInfo room0 = rlist0.get(roomIndex);
				
				final RoomInfo room1 = Rnd.get(rlist1);
				room1.setLock(true);
				
				final NpcMaker maker0 = ((MultiSpawn) npc.getSpawn()).getNpcMaker();
				maker0.getMaker().onMakerScriptEvent("0", maker0, 0, 0);
				
				room1.setParty(room0.getParty());
				
				final int posX = npc.getSpawn().getMemo().getInteger("PosX" + room1.getIndex(), 0);
				final int posY = npc.getSpawn().getMemo().getInteger("PosY" + room1.getIndex(), 0);
				
				for (Player member : room0.getParty().getMembers())
					if (member.distance2D(npc) <= 2000)
						member.teleportTo(posX, posY, npc.getZ(), 0);
					
				room0.clear();
			}
			return null;
		}
		return event;
	}
	
	@Override
	public void onCreated(Npc npc)
	{
		if (npc.getNpcId() >= 31488 && npc.getNpcId() <= 31493)
		{
			final String levelName = npc.getSpawn().getMemo().get("LevelName");
			if (levelName != null)
			{
				final List<RoomInfo> rooms = new ArrayList<>();
				for (int i = 0; i < 9; i++)
					rooms.add(new RoomInfo(i));
				
				ROOMS.put(levelName, rooms);
			}
		}
		else
		{
			npc._i_ai1 = 3;
			npc._i_ai2 = 0;
			
			oustPlayers(npc);
			
			startQuestTimerAtFixedRate("2001", npc, null, 1000, 10000);
		}
		
		super.onCreated(npc);
	}
	
	@Override
	public String onFirstTalk(Npc npc, Player player)
	{
		if (npc.getNpcId() >= 31488 && npc.getNpcId() <= 31493)
			return "rift_watcher_1001.htm";
		
		if (npc.getNpcId() >= 31865 && npc.getNpcId() <= 31918)
			return "tel_dungeon_npc_hi" + npc.getSpawn().getMemo().get("RoomIndex") + ".htm";
		
		return null;
	}
	
	@Override
	public String onTalk(Npc npc, Player player)
	{
		if (npc.getNpcId() >= 31494 && npc.getNpcId() <= 31507)
		{
			if (player.getStatus().getLevel() < 20)
				return "dimension_keeper_01.htm";
			
			if (player.getQuestList().getAllQuests(false).size() >= 25)
				return "dimension_keeper_02.htm";
			
			if (!player.getInventory().hasItem(DIMENSIONAL_FRAGMENT))
				return "dimension_keeper_03.htm";
			
			return "dimension_keeper_04.htm";
		}
		return null;
	}
	
	@Override
	public String onTimer(String name, Npc npc, Player player)
	{
		final String levelName = npc.getSpawn().getMemo().get("LevelName");
		
		final List<RoomInfo> rlist0 = ROOMS.get(levelName);
		if (rlist0 == null)
		{
			startQuestTimer(name, npc, player, 10000);
			return super.onTimer(name, npc, player);
		}
		
		final NpcMaker maker0 = ((MultiSpawn) npc.getSpawn()).getNpcMaker();
		final int gtTicks = GameTimeTaskManager.getInstance().getCurrentTick();
		
		final int roomIndex = npc.getSpawn().getMemo().getInteger("RoomIndex");
		final RoomInfo room0 = rlist0.get(roomIndex);
		
		if (name.equalsIgnoreCase("2001"))
		{
			if (room0 != null)
			{
				if (room0.isLocked())
				{
					if (gtTicks - room0.getTime() < 15)
					{
						maker0.getMaker().onMakerScriptEvent("1", maker0, 0, 0);
					}
					else if (room0.isPartyChanged() || !npc.getSpawn().isInMyTerritory(room0.getParty().getLeader()))
					{
						if (gtTicks - room0.getTime() > 3)
						{
							for (int i0 : room0.getMemberIds())
							{
								final Player c0 = World.getInstance().getPlayer(i0);
								if (c0 != null)
									setMemoState(c0, 635, -1);
							}
							
							oustPlayers(npc);
							
							maker0.getMaker().onMakerScriptEvent("0", maker0, 0, 0);
							room0.clear();
						}
					}
					else
					{
						if (gtTicks - room0.getTime() > (60 * Config.RIFT_AUTO_JUMPS_TIME_MIN + Rnd.get(Config.RIFT_AUTO_JUMPS_TIME_RND) * 30))
						{
							if (npc._i_ai2 == 1)
							{
								if (roomIndex == 8 && npc._i_ai1 == 2)
								{
									// Do nothing.
								}
								else
								{
									room0.setLock(false);
									maker0.getMaker().onMakerScriptEvent("0", maker0, 0, 0);
									npc._i_ai2 = 0;
								}
							}
							else
							{
								int i0 = -1;
								for (int i1 : room0.getMemberIds())
								{
									if (i0 == -1)
									{
										final Player c0 = World.getInstance().getPlayer(i1);
										if (c0 != null)
										{
											i0 = getMemoState(c0, 635);
											break;
										}
									}
								}
								
								if (roomIndex == 8 && npc._i_ai1 == 2)
								{
									// Do nothing.
								}
								else
								{
									if (i0 + 60 * 40 < gtTicks)
									{
										for (int i1 : room0.getMemberIds())
										{
											final Player c0 = World.getInstance().getPlayer(i1);
											if (c0 != null)
												setMemoStateEx(c0, 635, 1, -1);
										}
										
										oustPlayers(npc);
										
										maker0.getMaker().onMakerScriptEvent("0", maker0, 0, 0);
										room0.clear();
									}
									else
									{
										startQuestTimer("2002", npc, player, 5000);
										
										for (Player pm : room0.getParty().getMembers())
											pm.sendPacket(new Earthquake(pm, 20, 10));
									}
								}
							}
						}
						else if (roomIndex == 8 && npc._i_ai1 == 3)
						{
							startQuestTimer("2002", npc, player, 5000);
							
							for (Player pm : room0.getParty().getMembers())
								pm.sendPacket(new Earthquake(pm, 10, 10));
						}
					}
				}
				else
					oustPlayers(npc);
			}
		}
		else if (name.equalsIgnoreCase("2002"))
		{
			if (room0.isPartyChanged())
			{
				for (int i1 : room0.getMemberIds())
				{
					final Player c0 = World.getInstance().getPlayer(i1);
					setMemoStateEx(c0, 635, 1, -1);
				}
				
				oustPlayers(npc);
				
				maker0.getMaker().onMakerScriptEvent("0", maker0, 0, 0);
				room0.clear();
			}
			else
			{
				Player c0 = room0.getParty().getLeader();
				if (c0.isDead() || !npc.getSpawn().isInMyTerritory(c0))
				{
					for (int i1 : room0.getMemberIds())
					{
						c0 = World.getInstance().getPlayer(i1);
						setMemoStateEx(c0, 635, 1, -1);
					}
					
					oustPlayers(npc);
					
					maker0.getMaker().onMakerScriptEvent("0", maker0, 0, 0);
					room0.clear();
				}
				else if (roomIndex == 8 && npc._i_ai1 == 2)
				{
					// Do nothing.
				}
				else
				{
					RoomInfo room1 = rlist0.get(8);
					if (roomIndex != 8 && Rnd.get(100) < Config.RIFT_ANAKAZEL_PORT_CHANCE && !room1.isLocked())
					{
						final int posX = npc.getSpawn().getMemo().getInteger("PosX8", 0);
						final int posY = npc.getSpawn().getMemo().getInteger("PosY8", 0);
						
						for (Player member : room0.getParty().getMembers())
							if (member.distance2D(npc) <= 2000)
								member.teleportTo(posX, posY, npc.getZ(), 0);
							
						room1.setLock(true);
						maker0.getMaker().onMakerScriptEvent("0", maker0, 0, 0);
						room1.setParty(room0.getParty());
					}
					else
					{
						int i0 = -1;
						for (int i1 = 0; i1 < 8; i1++)
						{
							if (i0 == -1)
							{
								int i2 = Rnd.get(8);
								room1 = rlist0.get(i2);
								if (!room1.isLocked())
								{
									room1.setLock(true);
									i0 = i2;
								}
							}
						}
						
						if (i0 == -1)
						{
							for (int i1 = 0; i1 < 8; i1++)
							{
								if (i0 == -1)
								{
									room1 = rlist0.get(i1);
									if (!room1.isLocked())
									{
										room1.setLock(true);
										i0 = i1;
									}
								}
							}
						}
						
						if (i0 != -1)
						{
							maker0.getMaker().onMakerScriptEvent("0", maker0, 0, 0);
							room1.setParty(room0.getParty());
							
							final int posX = npc.getSpawn().getMemo().getInteger("PosX" + i0, 0);
							final int posY = npc.getSpawn().getMemo().getInteger("PosY" + i0, 0);
							
							for (Player member : room0.getParty().getMembers())
								if (member.distance2D(npc) <= 2000)
									member.teleportTo(posX, posY, npc.getZ(), 0);
						}
					}
					room0.clear();
				}
			}
		}
		
		return super.onTimer(name, npc, player);
	}
	
	@Override
	public void onScriptEvent(Npc npc, int eventId, int arg1, int arg2)
	{
		if (eventId == 2 || eventId == 3)
			npc._i_ai1 = eventId;
	}
	
	private static void oustPlayers(Npc npc)
	{
		final int escapeX = npc.getSpawn().getMemo().getInteger("escape_x");
		final int escapeY = npc.getSpawn().getMemo().getInteger("escape_y");
		final int escapeZ = npc.getSpawn().getMemo().getInteger("escape_z");
		
		npc.getSpawn().instantTeleportInMyTerritory(escapeX, escapeY, escapeZ, 100);
	}
}