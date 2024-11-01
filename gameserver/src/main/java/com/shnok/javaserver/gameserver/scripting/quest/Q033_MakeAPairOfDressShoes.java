package com.shnok.javaserver.gameserver.scripting.quest;

import com.shnok.javaserver.gameserver.enums.QuestStatus;
import com.shnok.javaserver.gameserver.model.actor.Npc;
import com.shnok.javaserver.gameserver.model.actor.Player;
import com.shnok.javaserver.gameserver.scripting.Quest;
import com.shnok.javaserver.gameserver.scripting.QuestState;

public class Q033_MakeAPairOfDressShoes extends Quest
{
	private static final String QUEST_NAME = "Q033_MakeAPairOfDressShoes";
	
	// NPCs
	private static final int WOODLEY = 30838;
	private static final int IAN = 30164;
	private static final int LEIKAR = 31520;
	
	// Items
	private static final int LEATHER = 1882;
	private static final int THREAD = 1868;
	private static final int ADENA = 57;
	
	// Rewards
	public static int DRESS_SHOES_BOX = 7113;
	
	public Q033_MakeAPairOfDressShoes()
	{
		super(33, "Make a Pair of Dress Shoes");
		
		addQuestStart(WOODLEY);
		addTalkId(WOODLEY, IAN, LEIKAR);
	}
	
	@Override
	public String onAdvEvent(String event, Npc npc, Player player)
	{
		String htmltext = event;
		QuestState st = player.getQuestList().getQuestState(QUEST_NAME);
		if (st == null)
			return htmltext;
		
		if (event.equalsIgnoreCase("30838-1.htm"))
		{
			st.setState(QuestStatus.STARTED);
			st.setCond(1);
			playSound(player, SOUND_ACCEPT);
		}
		else if (event.equalsIgnoreCase("31520-1.htm"))
		{
			st.setCond(2);
			playSound(player, SOUND_MIDDLE);
		}
		else if (event.equalsIgnoreCase("30838-3.htm"))
		{
			st.setCond(3);
			playSound(player, SOUND_MIDDLE);
		}
		else if (event.equalsIgnoreCase("30838-5.htm"))
		{
			if (player.getInventory().getItemCount(LEATHER) >= 200 && player.getInventory().getItemCount(THREAD) >= 600 && player.getInventory().getItemCount(ADENA) >= 200000)
			{
				st.setCond(4);
				playSound(player, SOUND_MIDDLE);
				takeItems(player, ADENA, 200000);
				takeItems(player, LEATHER, 200);
				takeItems(player, THREAD, 600);
			}
			else
				htmltext = "30838-4a.htm";
		}
		else if (event.equalsIgnoreCase("30164-1.htm"))
		{
			if (player.getInventory().getItemCount(ADENA) >= 300000)
			{
				st.setCond(5);
				playSound(player, SOUND_MIDDLE);
				takeItems(player, ADENA, 300000);
			}
			else
				htmltext = "30164-1a.htm";
		}
		else if (event.equalsIgnoreCase("30838-7.htm"))
		{
			giveItems(player, DRESS_SHOES_BOX, 1);
			playSound(player, SOUND_FINISH);
			st.exitQuest(false);
		}
		
		return htmltext;
	}
	
	@Override
	public String onTalk(Npc npc, Player player)
	{
		QuestState st = player.getQuestList().getQuestState(QUEST_NAME);
		String htmltext = getNoQuestMsg();
		if (st == null)
			return htmltext;
		
		switch (st.getState())
		{
			case CREATED:
				if (player.getStatus().getLevel() >= 60)
				{
					QuestState fwear = player.getQuestList().getQuestState("Q037_MakeFormalWear");
					if (fwear != null && fwear.getCond() == 7)
						htmltext = "30838-0.htm";
					else
						htmltext = "30838-0a.htm";
				}
				else
					htmltext = "30838-0b.htm";
				break;
			
			case STARTED:
				int cond = st.getCond();
				switch (npc.getNpcId())
				{
					case WOODLEY:
						if (cond == 1)
							htmltext = "30838-1.htm";
						else if (cond == 2)
							htmltext = "30838-2.htm";
						else if (cond == 3)
						{
							if (player.getInventory().getItemCount(LEATHER) >= 200 && player.getInventory().getItemCount(THREAD) >= 600 && player.getInventory().getItemCount(ADENA) >= 200000)
								htmltext = "30838-4.htm";
							else
								htmltext = "30838-4a.htm";
						}
						else if (cond == 4)
							htmltext = "30838-5a.htm";
						else if (cond == 5)
							htmltext = "30838-6.htm";
						break;
					
					case LEIKAR:
						if (cond == 1)
							htmltext = "31520-0.htm";
						else if (cond > 1)
							htmltext = "31520-1a.htm";
						break;
					
					case IAN:
						if (cond == 4)
							htmltext = "30164-0.htm";
						else if (cond == 5)
							htmltext = "30164-2.htm";
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