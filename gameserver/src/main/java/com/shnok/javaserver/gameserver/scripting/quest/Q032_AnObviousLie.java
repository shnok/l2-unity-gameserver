package com.shnok.javaserver.gameserver.scripting.quest;

import net.sf.l2j.gameserver.enums.QuestStatus;
import net.sf.l2j.gameserver.model.actor.Creature;
import net.sf.l2j.gameserver.model.actor.Npc;
import net.sf.l2j.gameserver.model.actor.Player;
import net.sf.l2j.gameserver.scripting.Quest;
import net.sf.l2j.gameserver.scripting.QuestState;

public class Q032_AnObviousLie extends Quest
{
	private static final String QUEST_NAME = "Q032_AnObviousLie";
	
	// Items
	private static final int SUEDE = 1866;
	private static final int THREAD = 1868;
	private static final int SPIRIT_ORE = 3031;
	private static final int MAP = 7165;
	private static final int MEDICINAL_HERB = 7166;
	
	// Rewards
	private static final int CAT_EARS = 6843;
	private static final int RACOON_EARS = 7680;
	private static final int RABBIT_EARS = 7683;
	
	// NPCs
	private static final int GENTLER = 30094;
	private static final int MAXIMILIAN = 30120;
	private static final int MIKI_THE_CAT = 31706;
	
	public Q032_AnObviousLie()
	{
		super(32, "An Obvious Lie");
		
		setItemsIds(MAP, MEDICINAL_HERB);
		
		addQuestStart(MAXIMILIAN);
		addTalkId(MAXIMILIAN, GENTLER, MIKI_THE_CAT);
		
		addMyDying(20135); // Alligator
	}
	
	@Override
	public String onAdvEvent(String event, Npc npc, Player player)
	{
		String htmltext = event;
		QuestState st = player.getQuestList().getQuestState(QUEST_NAME);
		if (st == null)
			return htmltext;
		
		if (event.equalsIgnoreCase("30120-1.htm"))
		{
			st.setState(QuestStatus.STARTED);
			st.setCond(1);
			playSound(player, SOUND_ACCEPT);
		}
		else if (event.equalsIgnoreCase("30094-1.htm"))
		{
			st.setCond(2);
			playSound(player, SOUND_MIDDLE);
			giveItems(player, MAP, 1);
		}
		else if (event.equalsIgnoreCase("31706-1.htm"))
		{
			st.setCond(3);
			playSound(player, SOUND_MIDDLE);
			takeItems(player, MAP, 1);
		}
		else if (event.equalsIgnoreCase("30094-4.htm"))
		{
			st.setCond(5);
			playSound(player, SOUND_MIDDLE);
			takeItems(player, MEDICINAL_HERB, 20);
		}
		else if (event.equalsIgnoreCase("30094-7.htm"))
		{
			if (player.getInventory().getItemCount(SPIRIT_ORE) < 500)
				htmltext = "30094-5.htm";
			else
			{
				st.setCond(6);
				playSound(player, SOUND_MIDDLE);
				takeItems(player, SPIRIT_ORE, 500);
			}
		}
		else if (event.equalsIgnoreCase("31706-4.htm"))
		{
			st.setCond(7);
			playSound(player, SOUND_MIDDLE);
		}
		else if (event.equalsIgnoreCase("30094-10.htm"))
		{
			st.setCond(8);
			playSound(player, SOUND_MIDDLE);
		}
		else if (event.equalsIgnoreCase("30094-13.htm"))
			playSound(player, SOUND_MIDDLE);
		else if (event.equalsIgnoreCase("cat"))
		{
			if (player.getInventory().getItemCount(THREAD) < 1000 || player.getInventory().getItemCount(SUEDE) < 500)
				htmltext = "30094-11.htm";
			else
			{
				htmltext = "30094-14.htm";
				takeItems(player, SUEDE, 500);
				takeItems(player, THREAD, 1000);
				giveItems(player, CAT_EARS, 1);
				playSound(player, SOUND_FINISH);
				st.exitQuest(false);
			}
		}
		else if (event.equalsIgnoreCase("racoon"))
		{
			if (player.getInventory().getItemCount(THREAD) < 1000 || player.getInventory().getItemCount(SUEDE) < 500)
				htmltext = "30094-11.htm";
			else
			{
				htmltext = "30094-14.htm";
				takeItems(player, SUEDE, 500);
				takeItems(player, THREAD, 1000);
				giveItems(player, RACOON_EARS, 1);
				playSound(player, SOUND_FINISH);
				st.exitQuest(false);
			}
		}
		else if (event.equalsIgnoreCase("rabbit"))
		{
			if (player.getInventory().getItemCount(THREAD) < 1000 || player.getInventory().getItemCount(SUEDE) < 500)
				htmltext = "30094-11.htm";
			else
			{
				htmltext = "30094-14.htm";
				takeItems(player, SUEDE, 500);
				takeItems(player, THREAD, 1000);
				giveItems(player, RABBIT_EARS, 1);
				playSound(player, SOUND_FINISH);
				st.exitQuest(false);
			}
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
				htmltext = (player.getStatus().getLevel() < 45) ? "30120-0a.htm" : "30120-0.htm";
				break;
			
			case STARTED:
				int cond = st.getCond();
				switch (npc.getNpcId())
				{
					case MAXIMILIAN:
						htmltext = "30120-2.htm";
						break;
					
					case GENTLER:
						if (cond == 1)
							htmltext = "30094-0.htm";
						else if (cond == 2 || cond == 3)
							htmltext = "30094-2.htm";
						else if (cond == 4)
							htmltext = "30094-3.htm";
						else if (cond == 5)
							htmltext = (player.getInventory().getItemCount(SPIRIT_ORE) < 500) ? "30094-5.htm" : "30094-6.htm";
						else if (cond == 6)
							htmltext = "30094-8.htm";
						else if (cond == 7)
							htmltext = "30094-9.htm";
						else if (cond == 8)
							htmltext = (player.getInventory().getItemCount(THREAD) < 1000 || player.getInventory().getItemCount(SUEDE) < 500) ? "30094-11.htm" : "30094-12.htm";
						break;
					
					case MIKI_THE_CAT:
						if (cond == 2)
							htmltext = "31706-0.htm";
						else if (cond > 2 && cond < 6)
							htmltext = "31706-2.htm";
						else if (cond == 6)
							htmltext = "31706-3.htm";
						else if (cond > 6)
							htmltext = "31706-5.htm";
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
		
		final QuestState st = checkPlayerCondition(player, npc, 3);
		if (st == null)
			return;
		
		if (dropItemsAlways(player, MEDICINAL_HERB, 1, 20))
			st.setCond(4);
	}
}