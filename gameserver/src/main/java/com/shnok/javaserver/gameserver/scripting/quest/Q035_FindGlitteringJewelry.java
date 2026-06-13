package com.shnok.javaserver.gameserver.scripting.quest;

import com.shnok.javaserver.gameserver.enums.QuestStatus;
import com.shnok.javaserver.gameserver.model.actor.Creature;
import com.shnok.javaserver.gameserver.model.actor.Npc;
import com.shnok.javaserver.gameserver.model.actor.Player;
import com.shnok.javaserver.gameserver.scripting.Quest;
import com.shnok.javaserver.gameserver.scripting.QuestState;

public class Q035_FindGlitteringJewelry extends Quest
{
	private static final String QUEST_NAME = "Q035_FindGlitteringJewelry";
	
	// NPCs
	private static final int ELLIE = 30091;
	private static final int FELTON = 30879;
	
	// Items
	private static final int ROUGH_JEWEL = 7162;
	private static final int ORIHARUKON = 1893;
	private static final int SILVER_NUGGET = 1873;
	private static final int THONS = 4044;
	
	// Reward
	private static final int JEWEL_BOX = 7077;
	
	public Q035_FindGlitteringJewelry()
	{
		super(35, "Find Glittering Jewelry");
		
		setItemsIds(ROUGH_JEWEL);
		
		addQuestStart(ELLIE);
		addTalkId(ELLIE, FELTON);
		
		addMyDying(20135); // Alligator
	}
	
	@Override
	public String onAdvEvent(String event, Npc npc, Player player)
	{
		String htmltext = event;
		QuestState st = player.getQuestList().getQuestState(QUEST_NAME);
		if (st == null)
			return htmltext;
		
		if (event.equalsIgnoreCase("30091-1.htm"))
		{
			st.setState(QuestStatus.STARTED);
			st.setCond(1);
			playSound(player, SOUND_ACCEPT);
		}
		else if (event.equalsIgnoreCase("30879-1.htm"))
		{
			st.setCond(2);
			playSound(player, SOUND_MIDDLE);
		}
		else if (event.equalsIgnoreCase("30091-3.htm"))
		{
			st.setCond(4);
			playSound(player, SOUND_MIDDLE);
			takeItems(player, ROUGH_JEWEL, 10);
		}
		else if (event.equalsIgnoreCase("30091-5.htm"))
		{
			if (player.getInventory().getItemCount(ORIHARUKON) >= 5 && player.getInventory().getItemCount(SILVER_NUGGET) >= 500 && player.getInventory().getItemCount(THONS) >= 150)
			{
				takeItems(player, ORIHARUKON, 5);
				takeItems(player, SILVER_NUGGET, 500);
				takeItems(player, THONS, 150);
				giveItems(player, JEWEL_BOX, 1);
				playSound(player, SOUND_FINISH);
				st.exitQuest(false);
			}
			else
				htmltext = "30091-4a.htm";
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
					if (fwear != null && fwear.getCond() == 6)
						htmltext = "30091-0.htm";
					else
						htmltext = "30091-0a.htm";
				}
				else
					htmltext = "30091-0b.htm";
				break;
			
			case STARTED:
				int cond = st.getCond();
				switch (npc.getNpcId())
				{
					case ELLIE:
						if (cond == 1 || cond == 2)
							htmltext = "30091-1a.htm";
						else if (cond == 3)
							htmltext = "30091-2.htm";
						else if (cond == 4)
							htmltext = (player.getInventory().getItemCount(ORIHARUKON) >= 5 && player.getInventory().getItemCount(SILVER_NUGGET) >= 500 && player.getInventory().getItemCount(THONS) >= 150) ? "30091-4.htm" : "30091-4a.htm";
						break;
					
					case FELTON:
						if (cond == 1)
							htmltext = "30879-0.htm";
						else if (cond > 1)
							htmltext = "30879-1a.htm";
						break;
				}
				break;
			
			case COMPLETED:
				htmltext = getAlreadyCompletedMsg();
				break;
		}
		
		return htmltext;
	}
	
	@Override
	public void onMyDying(Npc npc, Creature killer)
	{
		final Player player = killer.getActingPlayer();
		
		final QuestState st = checkPlayerCondition(player, npc, 2);
		if (st == null)
			return;
		
		if (dropItems(player, ROUGH_JEWEL, 1, 10, 500000))
			st.setCond(3);
	}
}