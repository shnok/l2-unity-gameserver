package com.shnok.javaserver.gameserver.scripting.quest;

import com.shnok.javaserver.gameserver.enums.QuestStatus;
import com.shnok.javaserver.gameserver.model.actor.Npc;
import com.shnok.javaserver.gameserver.model.actor.Player;
import com.shnok.javaserver.gameserver.scripting.Quest;
import com.shnok.javaserver.gameserver.scripting.QuestState;

public class Q023_LidiasHeart extends Quest
{
	private static final String QUEST_NAME = "Q023_LidiasHeart";
	
	// NPCs
	private static final int INNOCENTIN = 31328;
	private static final int BROKEN_BOOKSHELF = 31526;
	private static final int GHOST_OF_VON_HELLMANN = 31524;
	private static final int TOMBSTONE = 31523;
	private static final int VIOLET = 31386;
	private static final int BOX = 31530;
	
	// Items
	private static final int FOREST_OF_DEADMAN_MAP = 7063;
	private static final int SILVER_KEY = 7149;
	private static final int LIDIA_HAIRPIN = 7148;
	private static final int LIDIA_DIARY = 7064;
	private static final int SILVER_SPEAR = 7150;
	
	private Npc _ghost;
	
	public Q023_LidiasHeart()
	{
		super(23, "Lidia's Heart");
		
		setItemsIds(SILVER_KEY, LIDIA_DIARY, SILVER_SPEAR);
		
		addQuestStart(INNOCENTIN);
		addTalkId(INNOCENTIN, BROKEN_BOOKSHELF, GHOST_OF_VON_HELLMANN, VIOLET, BOX, TOMBSTONE);
		
		addDecayed(GHOST_OF_VON_HELLMANN);
	}
	
	@Override
	public String onAdvEvent(String event, Npc npc, Player player)
	{
		String htmltext = event;
		QuestState st = player.getQuestList().getQuestState(QUEST_NAME);
		if (st == null)
			return htmltext;
		
		if (event.equalsIgnoreCase("31328-02.htm"))
		{
			st.setState(QuestStatus.STARTED);
			st.setCond(1);
			playSound(player, SOUND_ACCEPT);
			giveItems(player, FOREST_OF_DEADMAN_MAP, 1);
			giveItems(player, SILVER_KEY, 1);
		}
		else if (event.equalsIgnoreCase("31328-06.htm"))
		{
			st.setCond(2);
			playSound(player, SOUND_MIDDLE);
		}
		else if (event.equalsIgnoreCase("31526-05.htm"))
		{
			if (!player.getInventory().hasItem(LIDIA_HAIRPIN))
			{
				giveItems(player, LIDIA_HAIRPIN, 1);
				if (player.getInventory().hasItem(LIDIA_DIARY))
				{
					st.setCond(4);
					playSound(player, SOUND_MIDDLE);
				}
				else
					playSound(player, SOUND_ITEMGET);
			}
		}
		else if (event.equalsIgnoreCase("31526-11.htm"))
		{
			if (!player.getInventory().hasItem(LIDIA_DIARY))
			{
				giveItems(player, LIDIA_DIARY, 1);
				if (player.getInventory().hasItem(LIDIA_HAIRPIN))
				{
					st.setCond(4);
					playSound(player, SOUND_MIDDLE);
				}
				else
					playSound(player, SOUND_ITEMGET);
			}
		}
		else if (event.equalsIgnoreCase("31328-11.htm"))
		{
			if (st.getCond() < 5)
			{
				st.setCond(5);
				playSound(player, SOUND_MIDDLE);
			}
		}
		else if (event.equalsIgnoreCase("31328-19.htm"))
		{
			st.setCond(6);
			playSound(player, SOUND_MIDDLE);
		}
		else if (event.equalsIgnoreCase("31524-04.htm"))
		{
			st.setCond(7);
			playSound(player, SOUND_MIDDLE);
			takeItems(player, LIDIA_DIARY, 1);
		}
		else if (event.equalsIgnoreCase("31523-02.htm"))
		{
			if (_ghost == null)
			{
				_ghost = addSpawn(31524, 51432, -54570, -3136, 0, false, 300000, true);
				_ghost.broadcastNpcSay("Who awoke me?");
			}
			else
				htmltext = "31523-03.htm";
		}
		else if (event.equalsIgnoreCase("31523-05.htm"))
		{
			startQuestTimer("tomb_digger", null, player, 10000);
		}
		else if (event.equalsIgnoreCase("31530-02.htm"))
		{
			st.setCond(10);
			playSound(player, SOUND_MIDDLE);
			takeItems(player, SILVER_KEY, 1);
			giveItems(player, SILVER_SPEAR, 1);
		}
		return htmltext;
	}
	
	@Override
	public String onTimer(String name, Npc npc, Player player)
	{
		if (name.equalsIgnoreCase("tomb_digger"))
		{
			QuestState st = player.getQuestList().getQuestState(QUEST_NAME);
			if (st != null)
			{
				st.setCond(8);
				playSound(player, SOUND_MIDDLE);
				giveItems(player, SILVER_KEY, 1);
				return "31523-06.htm";
			}
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
				QuestState st2 = player.getQuestList().getQuestState("Q022_TragedyInVonHellmannForest");
				if (st2 != null && st2.isCompleted())
				{
					if (player.getStatus().getLevel() >= 64)
						htmltext = "31328-01.htm";
					else
						htmltext = "31328-00a.htm";
				}
				else
					htmltext = "31328-00.htm";
				break;
			
			case STARTED:
				int cond = st.getCond();
				switch (npc.getNpcId())
				{
					case INNOCENTIN:
						if (cond == 1)
							htmltext = "31328-03.htm";
						else if (cond == 2)
							htmltext = "31328-07.htm";
						else if (cond == 4)
							htmltext = "31328-08.htm";
						else if (cond == 5)
						{
							if (st.getInteger("diary") == 1)
								htmltext = "31328-14.htm";
							else
								htmltext = "31328-11.htm";
						}
						else if (cond > 5)
							htmltext = "31328-21.htm";
						break;
					
					case BROKEN_BOOKSHELF:
						if (cond == 2)
						{
							htmltext = "31526-00.htm";
							st.setCond(3);
							playSound(player, SOUND_MIDDLE);
						}
						else if (cond == 3)
						{
							if (!player.getInventory().hasItem(LIDIA_DIARY))
								htmltext = (!player.getInventory().hasItem(LIDIA_HAIRPIN)) ? "31526-02.htm" : "31526-06.htm";
							else if (!player.getInventory().hasItem(LIDIA_HAIRPIN))
								htmltext = "31526-12.htm";
						}
						else if (cond > 3)
							htmltext = "31526-13.htm";
						break;
					
					case GHOST_OF_VON_HELLMANN:
						if (cond == 6)
							htmltext = "31524-01.htm";
						else if (cond == 7)
							htmltext = "31524-05.htm";
						else if (cond == 8)
							htmltext = "31524-06.htm";
						break;
					
					case TOMBSTONE:
						if (cond == 6)
							htmltext = (_ghost == null) ? "31523-01.htm" : "31523-03.htm";
						else if (cond == 7)
							htmltext = "31523-04.htm";
						else if (cond == 8)
							htmltext = "31523-06.htm";
						break;
					
					case VIOLET:
						if (cond == 8)
						{
							htmltext = "31386-01.htm";
							st.setCond(9);
							playSound(player, SOUND_MIDDLE);
						}
						else if (cond == 9)
							htmltext = "31386-02.htm";
						else if (cond == 10)
						{
							if (player.getInventory().hasItem(SILVER_SPEAR))
							{
								htmltext = "31386-03.htm";
								takeItems(player, SILVER_SPEAR, 1);
								rewardItems(player, 57, 100000);
								playSound(player, SOUND_FINISH);
								st.exitQuest(false);
							}
							else
							{
								htmltext = "31386-02.htm";
								st.setCond(9);
							}
						}
						break;
					
					case BOX:
						if (cond == 9)
							htmltext = "31530-01.htm";
						else if (cond == 10)
							htmltext = "31530-03.htm";
						break;
				}
				break;
			
			case COMPLETED:
				if (npc.getNpcId() == VIOLET)
					htmltext = "31386-04.htm";
				else
					htmltext = getAlreadyCompletedMsg();
				break;
		}
		
		return htmltext;
	}
	
	@Override
	public void onDecayed(Npc npc)
	{
		if (npc == _ghost)
		{
			_ghost = null;
		}
	}
}