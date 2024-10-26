package com.shnok.javaserver.gameserver.scripting.quest;

import net.sf.l2j.gameserver.enums.QuestStatus;
import net.sf.l2j.gameserver.enums.actors.ClassRace;
import net.sf.l2j.gameserver.model.actor.Npc;
import net.sf.l2j.gameserver.model.actor.Player;
import net.sf.l2j.gameserver.scripting.Quest;
import net.sf.l2j.gameserver.scripting.QuestState;

public class Q160_NerupasRequest extends Quest
{
	private static final String QUEST_NAME = "Q160_NerupasRequest";
	
	// Items
	private static final int SILVERY_SPIDERSILK = 1026;
	private static final int UNOREN_RECEIPT = 1027;
	private static final int CREAMEES_TICKET = 1028;
	private static final int NIGHTSHADE_LEAF = 1029;
	
	// Reward
	private static final int LESSER_HEALING_POTION = 1060;
	
	// NPCs
	private static final int NERUPA = 30370;
	private static final int UNOREN = 30147;
	private static final int CREAMEES = 30149;
	private static final int JULIA = 30152;
	
	public Q160_NerupasRequest()
	{
		super(160, "Nerupa's Request");
		
		setItemsIds(SILVERY_SPIDERSILK, UNOREN_RECEIPT, CREAMEES_TICKET, NIGHTSHADE_LEAF);
		
		addQuestStart(NERUPA);
		addTalkId(NERUPA, UNOREN, CREAMEES, JULIA);
	}
	
	@Override
	public String onAdvEvent(String event, Npc npc, Player player)
	{
		String htmltext = event;
		QuestState st = player.getQuestList().getQuestState(QUEST_NAME);
		if (st == null)
			return htmltext;
		
		if (event.equalsIgnoreCase("30370-04.htm"))
		{
			st.setState(QuestStatus.STARTED);
			st.setCond(1);
			playSound(player, SOUND_ACCEPT);
			giveItems(player, SILVERY_SPIDERSILK, 1);
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
				if (player.getRace() != ClassRace.ELF)
					htmltext = "30370-00.htm";
				else if (player.getStatus().getLevel() < 3)
					htmltext = "30370-02.htm";
				else
					htmltext = "30370-03.htm";
				break;
			
			case STARTED:
				int cond = st.getCond();
				switch (npc.getNpcId())
				{
					case NERUPA:
						if (cond < 4)
							htmltext = "30370-05.htm";
						else if (cond == 4)
						{
							htmltext = "30370-06.htm";
							takeItems(player, NIGHTSHADE_LEAF, 1);
							rewardItems(player, LESSER_HEALING_POTION, 5);
							rewardExpAndSp(player, 1000, 0);
							playSound(player, SOUND_FINISH);
							st.exitQuest(false);
						}
						break;
					
					case UNOREN:
						if (cond == 1)
						{
							htmltext = "30147-01.htm";
							st.setCond(2);
							playSound(player, SOUND_MIDDLE);
							takeItems(player, SILVERY_SPIDERSILK, 1);
							giveItems(player, UNOREN_RECEIPT, 1);
						}
						else if (cond == 2)
							htmltext = "30147-02.htm";
						else if (cond == 4)
							htmltext = "30147-03.htm";
						break;
					
					case CREAMEES:
						if (cond == 2)
						{
							htmltext = "30149-01.htm";
							st.setCond(3);
							playSound(player, SOUND_MIDDLE);
							takeItems(player, UNOREN_RECEIPT, 1);
							giveItems(player, CREAMEES_TICKET, 1);
						}
						else if (cond == 3)
							htmltext = "30149-02.htm";
						else if (cond == 4)
							htmltext = "30149-03.htm";
						break;
					
					case JULIA:
						if (cond == 3)
						{
							htmltext = "30152-01.htm";
							st.setCond(4);
							playSound(player, SOUND_MIDDLE);
							takeItems(player, CREAMEES_TICKET, 1);
							giveItems(player, NIGHTSHADE_LEAF, 1);
						}
						else if (cond == 4)
							htmltext = "30152-02.htm";
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