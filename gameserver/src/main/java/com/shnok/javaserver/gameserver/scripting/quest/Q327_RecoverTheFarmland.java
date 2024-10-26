package com.shnok.javaserver.gameserver.scripting.quest;

import java.util.HashMap;
import java.util.Map;

import net.sf.l2j.commons.random.Rnd;

import net.sf.l2j.gameserver.enums.QuestStatus;
import net.sf.l2j.gameserver.model.actor.Creature;
import net.sf.l2j.gameserver.model.actor.Npc;
import net.sf.l2j.gameserver.model.actor.Player;
import net.sf.l2j.gameserver.scripting.Quest;
import net.sf.l2j.gameserver.scripting.QuestState;

public class Q327_RecoverTheFarmland extends Quest
{
	private static final String QUEST_NAME = "Q327_RecoverTheFarmland";
	
	// Items
	private static final int LEIKAN_LETTER = 5012;
	private static final int TUREK_DOGTAG = 1846;
	private static final int TUREK_MEDALLION = 1847;
	private static final int CLAY_URN_FRAGMENT = 1848;
	private static final int BRASS_TRINKET_PIECE = 1849;
	private static final int BRONZE_MIRROR_PIECE = 1850;
	private static final int JADE_NECKLACE_BEAD = 1851;
	private static final int ANCIENT_CLAY_URN = 1852;
	private static final int ANCIENT_BRASS_TIARA = 1853;
	private static final int ANCIENT_BRONZE_MIRROR = 1854;
	private static final int ANCIENT_JADE_NECKLACE = 1855;
	
	// Rewards
	private static final int ADENA = 57;
	private static final int SOULSHOT_D = 1463;
	private static final int SPIRITSHOT_D = 2510;
	private static final int HEALING_POTION = 1061;
	private static final int HASTE_POTION = 734;
	private static final int POTION_OF_ALACRITY = 735;
	private static final int SCROLL_OF_ESCAPE = 736;
	private static final int SCROLL_OF_RESURRECTION = 737;
	
	// NPCs
	private static final int LEIKAN = 30382;
	private static final int PIOTUR = 30597;
	private static final int IRIS = 30034;
	private static final int ASHA = 30313;
	private static final int NESTLE = 30314;
	
	// Monsters
	private static final int TUREK_ORC_WARLORD = 20495;
	private static final int TUREK_ORC_ARCHER = 20496;
	private static final int TUREK_ORC_SKIRMISHER = 20497;
	private static final int TUREK_ORC_SUPPLIER = 20498;
	private static final int TUREK_ORC_FOOTMAN = 20499;
	private static final int TUREK_ORC_SENTINEL = 20500;
	private static final int TUREK_ORC_SHAMAN = 20501;
	
	// Chances
	private static final int[][] DROPLIST =
	{
		{
			TUREK_ORC_ARCHER,
			140000,
			TUREK_DOGTAG
		},
		{
			TUREK_ORC_SKIRMISHER,
			70000,
			TUREK_DOGTAG
		},
		{
			TUREK_ORC_SUPPLIER,
			120000,
			TUREK_DOGTAG
		},
		{
			TUREK_ORC_FOOTMAN,
			100000,
			TUREK_DOGTAG
		},
		{
			TUREK_ORC_SENTINEL,
			80000,
			TUREK_DOGTAG
		},
		{
			TUREK_ORC_SHAMAN,
			90000,
			TUREK_MEDALLION
		},
		{
			TUREK_ORC_WARLORD,
			180000,
			TUREK_MEDALLION
		}
	};
	
	// Exp
	private static final Map<Integer, Integer> EXP_REWARD = new HashMap<>();
	static
	{
		EXP_REWARD.put(ANCIENT_CLAY_URN, 2766);
		EXP_REWARD.put(ANCIENT_BRASS_TIARA, 3227);
		EXP_REWARD.put(ANCIENT_BRONZE_MIRROR, 3227);
		EXP_REWARD.put(ANCIENT_JADE_NECKLACE, 3919);
	}
	
	public Q327_RecoverTheFarmland()
	{
		super(327, "Recover the Farmland");
		
		setItemsIds(LEIKAN_LETTER);
		
		addQuestStart(LEIKAN, PIOTUR);
		addTalkId(LEIKAN, PIOTUR, IRIS, ASHA, NESTLE);
		
		addMyDying(TUREK_ORC_WARLORD, TUREK_ORC_ARCHER, TUREK_ORC_SKIRMISHER, TUREK_ORC_SUPPLIER, TUREK_ORC_FOOTMAN, TUREK_ORC_SENTINEL, TUREK_ORC_SHAMAN);
	}
	
	@Override
	public String onAdvEvent(String event, Npc npc, Player player)
	{
		String htmltext = event;
		QuestState st = player.getQuestList().getQuestState(QUEST_NAME);
		if (st == null)
			return htmltext;
		
		// Piotur
		if (event.equalsIgnoreCase("30597-03.htm") && st.getCond() < 1)
		{
			st.setState(QuestStatus.STARTED);
			st.setCond(1);
			playSound(player, SOUND_ACCEPT);
		}
		else if (event.equalsIgnoreCase("30597-06.htm"))
		{
			playSound(player, SOUND_FINISH);
			st.exitQuest(true);
		}
		// Leikan
		else if (event.equalsIgnoreCase("30382-03.htm"))
		{
			st.setState(QuestStatus.STARTED);
			st.setCond(2);
			playSound(player, SOUND_ACCEPT);
			giveItems(player, LEIKAN_LETTER, 1);
		}
		// Asha
		else if (event.equalsIgnoreCase("30313-02.htm"))
		{
			if (player.getInventory().getItemCount(CLAY_URN_FRAGMENT) >= 5)
			{
				takeItems(player, CLAY_URN_FRAGMENT, 5);
				if (Rnd.get(6) < 5)
				{
					htmltext = "30313-03.htm";
					rewardItems(player, ANCIENT_CLAY_URN, 1);
				}
				else
					htmltext = "30313-10.htm";
			}
		}
		else if (event.equalsIgnoreCase("30313-04.htm"))
		{
			if (player.getInventory().getItemCount(BRASS_TRINKET_PIECE) >= 5)
			{
				takeItems(player, BRASS_TRINKET_PIECE, 5);
				if (Rnd.get(7) < 6)
				{
					htmltext = "30313-05.htm";
					rewardItems(player, ANCIENT_BRASS_TIARA, 1);
				}
				else
					htmltext = "30313-10.htm";
			}
		}
		else if (event.equalsIgnoreCase("30313-06.htm"))
		{
			if (player.getInventory().getItemCount(BRONZE_MIRROR_PIECE) >= 5)
			{
				takeItems(player, BRONZE_MIRROR_PIECE, 5);
				if (Rnd.get(7) < 6)
				{
					htmltext = "30313-07.htm";
					rewardItems(player, ANCIENT_BRONZE_MIRROR, 1);
				}
				else
					htmltext = "30313-10.htm";
			}
		}
		else if (event.equalsIgnoreCase("30313-08.htm"))
		{
			if (player.getInventory().getItemCount(JADE_NECKLACE_BEAD) >= 5)
			{
				takeItems(player, JADE_NECKLACE_BEAD, 5);
				if (Rnd.get(8) < 7)
				{
					htmltext = "30313-09.htm";
					rewardItems(player, ANCIENT_JADE_NECKLACE, 1);
				}
				else
					htmltext = "30313-10.htm";
			}
		}
		// Iris
		else if (event.equalsIgnoreCase("30034-03.htm"))
		{
			final int n = player.getInventory().getItemCount(CLAY_URN_FRAGMENT);
			if (n == 0)
				htmltext = "30034-02.htm";
			else
			{
				playSound(player, SOUND_ITEMGET);
				takeItems(player, CLAY_URN_FRAGMENT, n);
				rewardExpAndSp(player, n * 307L, 0);
			}
		}
		else if (event.equalsIgnoreCase("30034-04.htm"))
		{
			final int n = player.getInventory().getItemCount(BRASS_TRINKET_PIECE);
			if (n == 0)
				htmltext = "30034-02.htm";
			else
			{
				playSound(player, SOUND_ITEMGET);
				takeItems(player, BRASS_TRINKET_PIECE, n);
				rewardExpAndSp(player, n * 368L, 0);
			}
		}
		else if (event.equalsIgnoreCase("30034-05.htm"))
		{
			final int n = player.getInventory().getItemCount(BRONZE_MIRROR_PIECE);
			if (n == 0)
				htmltext = "30034-02.htm";
			else
			{
				playSound(player, SOUND_ITEMGET);
				takeItems(player, BRONZE_MIRROR_PIECE, n);
				rewardExpAndSp(player, n * 368L, 0);
			}
		}
		else if (event.equalsIgnoreCase("30034-06.htm"))
		{
			final int n = player.getInventory().getItemCount(JADE_NECKLACE_BEAD);
			if (n == 0)
				htmltext = "30034-02.htm";
			else
			{
				playSound(player, SOUND_ITEMGET);
				takeItems(player, JADE_NECKLACE_BEAD, n);
				rewardExpAndSp(player, n * 430L, 0);
			}
		}
		else if (event.equalsIgnoreCase("30034-07.htm"))
		{
			boolean isRewarded = false;
			
			for (int i = 1852; i < 1856; i++)
			{
				int n = player.getInventory().getItemCount(i);
				if (n > 0)
				{
					takeItems(player, i, n);
					rewardExpAndSp(player, n * EXP_REWARD.get(i), 0);
					isRewarded = true;
				}
			}
			if (!isRewarded)
				htmltext = "30034-02.htm";
			else
				playSound(player, SOUND_ITEMGET);
		}
		// Nestle
		else if (event.equalsIgnoreCase("30314-03.htm"))
		{
			if (!player.getInventory().hasItem(ANCIENT_CLAY_URN))
				htmltext = "30314-07.htm";
			else
			{
				takeItems(player, ANCIENT_CLAY_URN, 1);
				rewardItems(player, SOULSHOT_D, 70 + Rnd.get(41));
			}
		}
		else if (event.equalsIgnoreCase("30314-04.htm"))
		{
			if (!player.getInventory().hasItem(ANCIENT_BRASS_TIARA))
				htmltext = "30314-07.htm";
			else
			{
				takeItems(player, ANCIENT_BRASS_TIARA, 1);
				final int rnd = Rnd.get(100);
				if (rnd < 40)
					rewardItems(player, HEALING_POTION, 1);
				else if (rnd < 84)
					rewardItems(player, HASTE_POTION, 1);
				else
					rewardItems(player, POTION_OF_ALACRITY, 1);
			}
		}
		else if (event.equalsIgnoreCase("30314-05.htm"))
		{
			if (!player.getInventory().hasItem(ANCIENT_BRONZE_MIRROR))
				htmltext = "30314-07.htm";
			else
			{
				takeItems(player, ANCIENT_BRONZE_MIRROR, 1);
				rewardItems(player, (Rnd.get(100) < 59) ? SCROLL_OF_ESCAPE : SCROLL_OF_RESURRECTION, 1);
			}
		}
		else if (event.equalsIgnoreCase("30314-06.htm"))
		{
			if (!player.getInventory().hasItem(ANCIENT_JADE_NECKLACE))
				htmltext = "30314-07.htm";
			else
			{
				takeItems(player, ANCIENT_JADE_NECKLACE, 1);
				rewardItems(player, SPIRITSHOT_D, 50 + Rnd.get(41));
			}
		}
		
		return htmltext;
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
				htmltext = npc.getNpcId() + ((player.getStatus().getLevel() < 25) ? "-01.htm" : "-02.htm");
				break;
			
			case STARTED:
				final int cond = st.getCond();
				switch (npc.getNpcId())
				{
					case PIOTUR:
						if (!player.getInventory().hasItem(LEIKAN_LETTER))
						{
							if (player.getInventory().hasAtLeastOneItem(TUREK_DOGTAG, TUREK_MEDALLION))
							{
								htmltext = "30597-05.htm";
								
								if (cond < 4)
								{
									st.setCond(4);
									playSound(player, SOUND_MIDDLE);
								}
								
								final int dogtag = player.getInventory().getItemCount(TUREK_DOGTAG);
								final int medallion = player.getInventory().getItemCount(TUREK_MEDALLION);
								
								takeItems(player, TUREK_DOGTAG, -1);
								takeItems(player, TUREK_MEDALLION, -1);
								rewardItems(player, ADENA, dogtag * 40 + medallion * 50 + ((dogtag + medallion >= 10) ? 619 : 0));
							}
							else
								htmltext = "30597-04.htm";
						}
						else
						{
							htmltext = "30597-03a.htm";
							st.setCond(3);
							playSound(player, SOUND_MIDDLE);
							takeItems(player, LEIKAN_LETTER, 1);
						}
						break;
					
					case LEIKAN:
						if (cond == 2)
							htmltext = "30382-04.htm";
						else if (cond == 3 || cond == 4)
						{
							htmltext = "30382-05.htm";
							st.setCond(5);
							playSound(player, SOUND_MIDDLE);
						}
						else if (cond == 5)
							htmltext = "30382-05.htm";
						break;
					
					default:
						htmltext = npc.getNpcId() + "-01.htm";
						break;
				}
				break;
		}
		
		return htmltext;
	}
	
	@Override
	public void onMyDying(Npc npc, Creature killer)
	{
		final Player player = killer.getActingPlayer();
		
		final QuestState st = checkPlayerState(player, npc, QuestStatus.STARTED);
		if (st == null)
			return;
		
		for (int[] npcData : DROPLIST)
		{
			if (npcData[0] == npc.getNpcId())
			{
				dropItemsAlways(player, npcData[2], 1, -1);
				dropItems(player, Rnd.get(1848, 1851), 1, 0, npcData[1]);
				break;
			}
		}
	}
}