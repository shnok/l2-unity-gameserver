package com.shnok.javaserver.gameserver.scripting.quest;

import net.sf.l2j.gameserver.enums.QuestStatus;
import net.sf.l2j.gameserver.model.actor.Npc;
import net.sf.l2j.gameserver.model.actor.Player;
import net.sf.l2j.gameserver.scripting.Quest;
import net.sf.l2j.gameserver.scripting.QuestState;

public class Q037_MakeFormalWear extends Quest
{
	private static final String QUEST_NAME = "Q037_MakeFormalWear";
	
	// NPCs
	private static final int ALEXIS = 30842;
	private static final int LEIKAR = 31520;
	private static final int JEREMY = 31521;
	private static final int MIST = 31627;
	
	// Items
	private static final int MYSTERIOUS_CLOTH = 7076;
	private static final int JEWEL_BOX = 7077;
	private static final int SEWING_KIT = 7078;
	private static final int DRESS_SHOES_BOX = 7113;
	private static final int SIGNET_RING = 7164;
	private static final int ICE_WINE = 7160;
	private static final int BOX_OF_COOKIES = 7159;
	
	// Reward
	private static final int FORMAL_WEAR = 6408;
	
	public Q037_MakeFormalWear()
	{
		super(37, "Make Formal Wear");
		
		setItemsIds(SIGNET_RING, ICE_WINE, BOX_OF_COOKIES);
		
		addQuestStart(ALEXIS);
		addTalkId(ALEXIS, LEIKAR, JEREMY, MIST);
	}
	
	@Override
	public String onAdvEvent(String event, Npc npc, Player player)
	{
		String htmltext = event;
		QuestState st = player.getQuestList().getQuestState(QUEST_NAME);
		if (st == null)
			return htmltext;
		
		if (event.equalsIgnoreCase("30842-1.htm"))
		{
			st.setState(QuestStatus.STARTED);
			st.setCond(1);
			playSound(player, SOUND_ACCEPT);
		}
		else if (event.equalsIgnoreCase("31520-1.htm"))
		{
			st.setCond(2);
			playSound(player, SOUND_MIDDLE);
			giveItems(player, SIGNET_RING, 1);
		}
		else if (event.equalsIgnoreCase("31521-1.htm"))
		{
			st.setCond(3);
			playSound(player, SOUND_MIDDLE);
			takeItems(player, SIGNET_RING, 1);
			giveItems(player, ICE_WINE, 1);
		}
		else if (event.equalsIgnoreCase("31627-1.htm"))
		{
			st.setCond(4);
			playSound(player, SOUND_MIDDLE);
			takeItems(player, ICE_WINE, 1);
		}
		else if (event.equalsIgnoreCase("31521-3.htm"))
		{
			st.setCond(5);
			playSound(player, SOUND_MIDDLE);
			giveItems(player, BOX_OF_COOKIES, 1);
		}
		else if (event.equalsIgnoreCase("31520-3.htm"))
		{
			st.setCond(6);
			playSound(player, SOUND_MIDDLE);
			takeItems(player, BOX_OF_COOKIES, 1);
		}
		else if (event.equalsIgnoreCase("31520-5.htm"))
		{
			st.setCond(7);
			playSound(player, SOUND_MIDDLE);
			takeItems(player, JEWEL_BOX, 1);
			takeItems(player, MYSTERIOUS_CLOTH, 1);
			takeItems(player, SEWING_KIT, 1);
		}
		else if (event.equalsIgnoreCase("31520-7.htm"))
		{
			takeItems(player, DRESS_SHOES_BOX, 1);
			giveItems(player, FORMAL_WEAR, 1);
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
				htmltext = (player.getStatus().getLevel() < 60) ? "30842-0a.htm" : "30842-0.htm";
				break;
			
			case STARTED:
				int cond = st.getCond();
				switch (npc.getNpcId())
				{
					case ALEXIS:
						if (cond == 1)
							htmltext = "30842-2.htm";
						break;
					
					case LEIKAR:
						if (cond == 1)
							htmltext = "31520-0.htm";
						else if (cond == 2)
							htmltext = "31520-1a.htm";
						else if (cond == 5 || cond == 6)
						{
							if (player.getInventory().hasItems(MYSTERIOUS_CLOTH, JEWEL_BOX, SEWING_KIT))
								htmltext = "31520-4.htm";
							else if (player.getInventory().hasItem(BOX_OF_COOKIES))
								htmltext = "31520-2.htm";
							else
								htmltext = "31520-3a.htm";
						}
						else if (cond == 7)
							htmltext = (player.getInventory().hasItem(DRESS_SHOES_BOX)) ? "31520-6.htm" : "31520-5a.htm";
						break;
					
					case JEREMY:
						if (player.getInventory().hasItem(SIGNET_RING))
							htmltext = "31521-0.htm";
						else if (cond == 3)
							htmltext = "31521-1a.htm";
						else if (cond == 4)
							htmltext = "31521-2.htm";
						else if (cond > 4)
							htmltext = "31521-3a.htm";
						break;
					
					case MIST:
						if (cond == 3)
							htmltext = "31627-0.htm";
						else if (cond > 3)
							htmltext = "31627-2.htm";
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