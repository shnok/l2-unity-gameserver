package com.shnok.javaserver.gameserver.scripting.quest;

import java.util.HashMap;
import java.util.Map;

import com.shnok.javaserver.gameserver.enums.QuestStatus;
import com.shnok.javaserver.gameserver.model.actor.Npc;
import com.shnok.javaserver.gameserver.model.actor.Player;
import com.shnok.javaserver.gameserver.network.NpcStringId;
import com.shnok.javaserver.gameserver.scripting.Quest;
import com.shnok.javaserver.gameserver.scripting.QuestState;

public class Q115_TheOtherSideOfTruth extends Quest
{
	private static final String QUEST_NAME = "Q115_TheOtherSideOfTruth";
	
	// Items
	private static final int MISA_LETTER = 8079;
	private static final int RAFFORTY_LETTER = 8080;
	private static final int PIECE_OF_TABLET = 8081;
	private static final int REPORT_PIECE = 8082;
	
	// NPCs
	private static final int RAFFORTY = 32020;
	private static final int MISA = 32018;
	private static final int KIERRE = 32022;
	private static final int SCULPTURE_1 = 32021;
	private static final int SCULPTURE_2 = 32077;
	private static final int SCULPTURE_3 = 32078;
	private static final int SCULPTURE_4 = 32079;
	private static final int SUSPICIOUS_MAN = 32019;
	
	// Used to test progression through sculptures. The array consists of value to add, used modulo, tested modulo value, tested values 1/2/3/4.
	private static final Map<Integer, int[]> NPC_VALUES = new HashMap<>();
	static
	{
		NPC_VALUES.put(32021, new int[]
		{
			1,
			2,
			1,
			6,
			10,
			12,
			14
		});
		NPC_VALUES.put(32077, new int[]
		{
			2,
			4,
			1,
			5,
			9,
			12,
			13
		});
		NPC_VALUES.put(32078, new int[]
		{
			4,
			8,
			3,
			3,
			9,
			10,
			11
		});
		NPC_VALUES.put(32079, new int[]
		{
			8,
			0,
			7,
			3,
			5,
			6,
			7
		});
	}
	
	public Q115_TheOtherSideOfTruth()
	{
		super(115, "The Other Side of Truth");
		
		setItemsIds(MISA_LETTER, RAFFORTY_LETTER, PIECE_OF_TABLET, REPORT_PIECE);
		
		addQuestStart(RAFFORTY);
		addTalkId(RAFFORTY, MISA, KIERRE, SCULPTURE_1, SCULPTURE_2, SCULPTURE_3, SCULPTURE_4);
	}
	
	@Override
	public String onAdvEvent(String event, Npc npc, Player player)
	{
		String htmltext = event;
		QuestState st = player.getQuestList().getQuestState(QUEST_NAME);
		if (st == null)
			return htmltext;
		
		if (event.equalsIgnoreCase("32020-03.htm"))
		{
			st.setState(QuestStatus.STARTED);
			st.setCond(1);
			playSound(player, SOUND_ACCEPT);
		}
		else if (event.equalsIgnoreCase("32020-05.htm") || event.equalsIgnoreCase("32020-08.htm") || event.equalsIgnoreCase("32020-13.htm"))
		{
			playSound(player, SOUND_GIVEUP);
			st.exitQuest(true);
		}
		else if (event.equalsIgnoreCase("32020-07.htm"))
		{
			st.setCond(3);
			playSound(player, SOUND_MIDDLE);
			takeItems(player, MISA_LETTER, 1);
		}
		else if (event.equalsIgnoreCase("32020-11.htm") || event.equalsIgnoreCase("32020-12.htm"))
		{
			if (st.getCond() == 3)
			{
				st.setCond(4);
				playSound(player, SOUND_MIDDLE);
			}
		}
		else if (event.equalsIgnoreCase("32020-17.htm"))
		{
			st.setCond(5);
			playSound(player, SOUND_MIDDLE);
		}
		else if (event.equalsIgnoreCase("32020-23.htm"))
		{
			st.setCond(10);
			playSound(player, SOUND_MIDDLE);
			takeItems(player, REPORT_PIECE, 1);
		}
		else if (event.equalsIgnoreCase("32020-27.htm"))
		{
			if (!player.getInventory().hasItem(PIECE_OF_TABLET))
			{
				st.setCond(11);
				playSound(player, SOUND_MIDDLE);
			}
			else
			{
				htmltext = "32020-25.htm";
				takeItems(player, PIECE_OF_TABLET, 1);
				rewardItems(player, 57, 60040);
				playSound(player, SOUND_FINISH);
				st.exitQuest(false);
			}
		}
		else if (event.equalsIgnoreCase("32020-28.htm"))
		{
			if (!player.getInventory().hasItem(PIECE_OF_TABLET))
			{
				st.setCond(11);
				playSound(player, SOUND_MIDDLE);
			}
			else
			{
				htmltext = "32020-26.htm";
				takeItems(player, PIECE_OF_TABLET, 1);
				rewardItems(player, 57, 60040);
				playSound(player, SOUND_FINISH);
				st.exitQuest(false);
			}
		}
		else if (event.equalsIgnoreCase("32018-05.htm"))
		{
			st.setCond(7);
			playSound(player, SOUND_MIDDLE);
			takeItems(player, RAFFORTY_LETTER, 1);
		}
		else if (event.equalsIgnoreCase("sculpture-03.htm"))
		{
			final int[] infos = NPC_VALUES.get(npc.getNpcId());
			final int ex = st.getInteger("ex");
			final int numberToModulo = (infos[1] == 0) ? ex : ex % infos[1];
			
			if (numberToModulo <= infos[2] && (ex == infos[3] || ex == infos[4] || ex == infos[5]))
			{
				st.set("ex", ex + infos[0]);
				giveItems(player, PIECE_OF_TABLET, 1);
				playSound(player, SOUND_ITEMGET);
			}
		}
		else if (event.equalsIgnoreCase("sculpture-04.htm"))
		{
			final int[] infos = NPC_VALUES.get(npc.getNpcId());
			final int ex = st.getInteger("ex");
			final int numberToModulo = (infos[1] == 0) ? ex : ex % infos[1];
			
			if (numberToModulo <= infos[2] && (ex == infos[3] || ex == infos[4] || ex == infos[5]))
				st.set("ex", ex + infos[0]);
		}
		else if (event.equalsIgnoreCase("sculpture-06.htm"))
		{
			st.setCond(8);
			playSound(player, SOUND_MIDDLE);
			
			// Spawn a suspicious man broadcasting a message, which dissapear few seconds later broadcasting a second message.
			final Npc stranger = addSpawn(SUSPICIOUS_MAN, player.getX() + 50, player.getY() + 50, player.getZ(), 0, false, 3100, false);
			stranger.broadcastNpcSay(NpcStringId.ID_11550);
			
			startQuestTimer("despawn_1", stranger, null, 3000);
		}
		else if (event.equalsIgnoreCase("32022-02.htm"))
		{
			st.setCond(9);
			playSound(player, SOUND_MIDDLE);
			giveItems(player, REPORT_PIECE, 1);
			
			// Spawn a suspicious man broadcasting a message, which dissapear few seconds later broadcasting a second message.
			final Npc stranger = addSpawn(SUSPICIOUS_MAN, player.getX() + 50, player.getY() + 50, player.getZ(), 0, false, 5100, false);
			stranger.broadcastNpcSay(NpcStringId.ID_11552);
			
			startQuestTimer("despawn_2", stranger, null, 5000);
		}
		return htmltext;
	}
	
	@Override
	public String onTimer(String name, Npc npc, Player player)
	{
		if (name.equalsIgnoreCase("despawn_1"))
		{
			npc.broadcastNpcSay(NpcStringId.ID_11551);
		}
		else if (name.equalsIgnoreCase("despawn_2"))
		{
			npc.broadcastNpcSay(NpcStringId.ID_11553);
		}
		
		return null;
	}
	
	@Override
	public String onTalk(Npc npc, Player player)
	{
		String htmltext = getNoQuestMsg();
		QuestState st = player.getQuestList().getQuestState(QUEST_NAME);
		if (st == null)
			return htmltext;
		
		switch (st.getState())
		{
			case CREATED:
				htmltext = (player.getStatus().getLevel() < 53) ? "32020-02.htm" : "32020-01.htm";
				break;
			
			case STARTED:
				final int cond = st.getCond();
				
				switch (npc.getNpcId())
				{
					case RAFFORTY:
						if (cond == 1)
							htmltext = "32020-04.htm";
						else if (cond == 2)
							htmltext = "32020-06.htm";
						else if (cond == 3)
							htmltext = "32020-09.htm";
						else if (cond == 4)
							htmltext = "32020-16.htm";
						else if (cond == 5)
						{
							htmltext = "32020-18.htm";
							st.setCond(6);
							playSound(player, SOUND_MIDDLE);
							giveItems(player, RAFFORTY_LETTER, 1);
						}
						else if (cond == 6)
						{
							if (!player.getInventory().hasItem(RAFFORTY_LETTER))
							{
								htmltext = "32020-20.htm";
								giveItems(player, RAFFORTY_LETTER, 1);
								playSound(player, SOUND_ITEMGET);
							}
							else
								htmltext = "32020-19.htm";
						}
						else if (cond == 7)
							htmltext = "32020-19.htm";
						else if (cond == 8)
							htmltext = "32020-21.htm";
						else if (cond == 9)
							htmltext = "32020-22.htm";
						else if (cond == 10)
							htmltext = "32020-24.htm";
						else if (cond == 11)
							htmltext = "32020-29.htm";
						else if (cond == 12)
						{
							htmltext = "32020-30.htm";
							takeItems(player, PIECE_OF_TABLET, 1);
							rewardItems(player, 57, 60040);
							playSound(player, SOUND_FINISH);
							st.exitQuest(false);
						}
						break;
					
					case MISA:
						if (cond == 1)
						{
							htmltext = "32018-02.htm";
							st.setCond(2);
							playSound(player, SOUND_MIDDLE);
							giveItems(player, MISA_LETTER, 1);
						}
						else if (cond == 2)
							htmltext = "32018-03.htm";
						else if (cond == 6)
							htmltext = "32018-04.htm";
						else if (cond > 6)
							htmltext = "32018-06.htm";
						else
							htmltext = "32018-01.htm";
						break;
					
					case KIERRE:
						if (cond == 8)
							htmltext = "32022-01.htm";
						else if (cond == 9)
						{
							if (!player.getInventory().hasItem(REPORT_PIECE))
							{
								htmltext = "32022-04.htm";
								giveItems(player, REPORT_PIECE, 1);
								playSound(player, SOUND_ITEMGET);
							}
							else
								htmltext = "32022-03.htm";
						}
						else if (cond == 11)
							htmltext = "32022-05.htm";
						break;
					
					case SCULPTURE_1, SCULPTURE_2, SCULPTURE_3, SCULPTURE_4:
						if (cond == 7)
						{
							final int[] infos = NPC_VALUES.get(npc.getNpcId());
							final int ex = st.getInteger("ex");
							final int numberToModulo = (infos[1] == 0) ? ex : ex % infos[1];
							
							if (numberToModulo <= infos[2])
							{
								if (ex == infos[3] || ex == infos[4] || ex == infos[5])
									htmltext = "sculpture-02.htm";
								else if (ex == infos[6])
									htmltext = "sculpture-05.htm";
								else
								{
									st.set("ex", ex + infos[0]);
									htmltext = "sculpture-01.htm";
								}
							}
							else
								htmltext = "sculpture-01a.htm";
						}
						else if (cond > 7 && cond < 11)
							htmltext = "sculpture-07.htm";
						else if (cond == 11)
						{
							if (!player.getInventory().hasItem(PIECE_OF_TABLET))
							{
								htmltext = "sculpture-08.htm";
								st.setCond(12);
								playSound(player, SOUND_MIDDLE);
								giveItems(player, PIECE_OF_TABLET, 1);
							}
							else
								htmltext = "sculpture-09.htm";
						}
						else if (cond == 12)
							htmltext = "sculpture-09.htm";
						break;
				}
				break;
			
			case COMPLETED:
				htmltext = getAlreadyCompletedMsg();
				break;
		}
		
		return htmltext;
	}
}