package com.shnok.javaserver.gameserver.scripting.quest;

import net.sf.l2j.gameserver.enums.QuestStatus;
import net.sf.l2j.gameserver.model.actor.Creature;
import net.sf.l2j.gameserver.model.actor.Npc;
import net.sf.l2j.gameserver.model.actor.Player;
import net.sf.l2j.gameserver.scripting.Quest;
import net.sf.l2j.gameserver.scripting.QuestState;

public class Q618_IntoTheFlame extends Quest
{
	private static final String QUEST_NAME = "Q618_IntoTheFlame";
	
	// NPCs
	private static final int KLEIN = 31540;
	private static final int HILDA = 31271;
	
	// Items
	private static final int VACUALITE_ORE = 7265;
	private static final int VACUALITE = 7266;
	
	// Reward
	private static final int FLOATING_STONE = 7267;
	
	public Q618_IntoTheFlame()
	{
		super(618, "Into The Flame");
		
		setItemsIds(VACUALITE_ORE, VACUALITE);
		
		addQuestStart(KLEIN);
		addTalkId(KLEIN, HILDA);
		
		// Kookaburras, Bandersnatches, Grendels
		addMyDying(21274, 21275, 21276, 21277, 21282, 21283, 21284, 21285, 21290, 21291, 21292, 21293);
	}
	
	@Override
	public String onAdvEvent(String event, Npc npc, Player player)
	{
		String htmltext = event;
		QuestState st = player.getQuestList().getQuestState(QUEST_NAME);
		if (st == null)
			return htmltext;
		
		if (event.equalsIgnoreCase("31540-03.htm"))
		{
			st.setState(QuestStatus.STARTED);
			st.setCond(1);
			playSound(player, SOUND_ACCEPT);
		}
		else if (event.equalsIgnoreCase("31540-05.htm"))
		{
			takeItems(player, VACUALITE, 1);
			giveItems(player, FLOATING_STONE, 1);
			playSound(player, SOUND_FINISH);
			st.exitQuest(true);
		}
		else if (event.equalsIgnoreCase("31271-02.htm"))
		{
			st.setCond(2);
			playSound(player, SOUND_MIDDLE);
		}
		else if (event.equalsIgnoreCase("31271-05.htm"))
		{
			st.setCond(4);
			playSound(player, SOUND_MIDDLE);
			takeItems(player, VACUALITE_ORE, -1);
			giveItems(player, VACUALITE, 1);
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
				htmltext = (player.getStatus().getLevel() < 60) ? "31540-01.htm" : "31540-02.htm";
				break;
			
			case STARTED:
				final int cond = st.getCond();
				switch (npc.getNpcId())
				{
					case KLEIN:
						htmltext = (cond == 4) ? "31540-04.htm" : "31540-03.htm";
						break;
					
					case HILDA:
						if (cond == 1)
							htmltext = "31271-01.htm";
						else if (cond == 2)
							htmltext = "31271-03.htm";
						else if (cond == 3)
							htmltext = "31271-04.htm";
						else if (cond == 4)
							htmltext = "31271-06.htm";
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
		
		final QuestState st = getRandomPartyMember(player, npc, 2);
		if (st == null)
			return;
		
		if (dropItems(st.getPlayer(), VACUALITE_ORE, 1, 50, 500000))
			st.setCond(3);
	}
}