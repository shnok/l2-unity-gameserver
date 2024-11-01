package com.shnok.javaserver.gameserver.scripting.quest;

import com.shnok.javaserver.gameserver.enums.QuestStatus;
import com.shnok.javaserver.gameserver.enums.actors.ClassRace;
import com.shnok.javaserver.gameserver.model.actor.Creature;
import com.shnok.javaserver.gameserver.model.actor.Npc;
import com.shnok.javaserver.gameserver.model.actor.Player;
import com.shnok.javaserver.gameserver.network.serverpackets.SocialAction;
import com.shnok.javaserver.gameserver.scripting.Quest;
import com.shnok.javaserver.gameserver.scripting.QuestState;

public class Q107_MercilessPunishment extends Quest
{
	private static final String QUEST_NAME = "Q107_MercilessPunishment";
	
	// NPCs
	private static final int HATOS = 30568;
	private static final int PARUGON = 30580;
	
	// Items
	private static final int HATOS_ORDER_1 = 1553;
	private static final int HATOS_ORDER_2 = 1554;
	private static final int HATOS_ORDER_3 = 1555;
	private static final int LETTER_TO_HUMAN = 1557;
	private static final int LETTER_TO_DARKELF = 1556;
	private static final int LETTER_TO_ELF = 1558;
	
	// Rewards
	private static final int LESSER_HEALING_POTION = 1060;
	private static final int BUTCHER_SWORD = 1510;
	private static final int ECHO_BATTLE = 4412;
	private static final int ECHO_LOVE = 4413;
	private static final int ECHO_SOLITUDE = 4414;
	private static final int ECHO_FEAST = 4415;
	private static final int ECHO_CELEBRATION = 4416;
	
	public Q107_MercilessPunishment()
	{
		super(107, "Merciless Punishment");
		
		setItemsIds(HATOS_ORDER_1, HATOS_ORDER_2, HATOS_ORDER_3, LETTER_TO_HUMAN, LETTER_TO_DARKELF, LETTER_TO_ELF);
		
		addQuestStart(HATOS);
		addTalkId(HATOS, PARUGON);
		
		addMyDying(27041); // Baranka's Messenger
	}
	
	@Override
	public String onAdvEvent(String event, Npc npc, Player player)
	{
		QuestState st = player.getQuestList().getQuestState(QUEST_NAME);
		String htmltext = event;
		if (st == null)
			return htmltext;
		
		if (event.equalsIgnoreCase("30568-03.htm"))
		{
			st.setState(QuestStatus.STARTED);
			st.setCond(1);
			playSound(player, SOUND_ACCEPT);
			giveItems(player, HATOS_ORDER_1, 1);
		}
		else if (event.equalsIgnoreCase("30568-06.htm"))
		{
			playSound(player, SOUND_GIVEUP);
			st.exitQuest(true);
		}
		else if (event.equalsIgnoreCase("30568-07.htm"))
		{
			st.setCond(4);
			playSound(player, SOUND_MIDDLE);
			takeItems(player, HATOS_ORDER_1, 1);
			giveItems(player, HATOS_ORDER_2, 1);
		}
		else if (event.equalsIgnoreCase("30568-09.htm"))
		{
			st.setCond(6);
			playSound(player, SOUND_MIDDLE);
			takeItems(player, HATOS_ORDER_2, 1);
			giveItems(player, HATOS_ORDER_3, 1);
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
				if (player.getRace() != ClassRace.ORC)
					htmltext = "30568-00.htm";
				else if (player.getStatus().getLevel() < 12)
					htmltext = "30568-01.htm";
				else
					htmltext = "30568-02.htm";
				break;
			
			case STARTED:
				int cond = st.getCond();
				switch (npc.getNpcId())
				{
					case HATOS:
						if (cond == 1 || cond == 2)
							htmltext = "30568-04.htm";
						else if (cond == 3)
							htmltext = "30568-05.htm";
						else if (cond == 4 || cond == 6)
							htmltext = "30568-09.htm";
						else if (cond == 5)
							htmltext = "30568-08.htm";
						else if (cond == 7)
						{
							htmltext = "30568-10.htm";
							takeItems(player, HATOS_ORDER_3, -1);
							takeItems(player, LETTER_TO_DARKELF, -1);
							takeItems(player, LETTER_TO_HUMAN, -1);
							takeItems(player, LETTER_TO_ELF, -1);
							giveItems(player, BUTCHER_SWORD, 1);
							
							rewardNewbieShots(player, 7000, 0);
							rewardItems(player, LESSER_HEALING_POTION, 100);
							rewardItems(player, ECHO_BATTLE, 10);
							rewardItems(player, ECHO_LOVE, 10);
							rewardItems(player, ECHO_SOLITUDE, 10);
							rewardItems(player, ECHO_FEAST, 10);
							rewardItems(player, ECHO_CELEBRATION, 10);
							
							player.broadcastPacket(new SocialAction(player, 3));
							playSound(player, SOUND_FINISH);
							st.exitQuest(false);
						}
						break;
					
					case PARUGON:
						htmltext = "30580-01.htm";
						if (cond == 1)
						{
							st.setCond(2);
							playSound(player, SOUND_MIDDLE);
						}
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
		
		final QuestState st = checkPlayerState(player, npc, QuestStatus.STARTED);
		if (st == null)
			return;
		
		final int cond = st.getCond();
		
		if (cond == 2)
		{
			st.setCond(3);
			playSound(player, SOUND_MIDDLE);
			giveItems(player, LETTER_TO_HUMAN, 1);
		}
		else if (cond == 4)
		{
			st.setCond(5);
			playSound(player, SOUND_MIDDLE);
			giveItems(player, LETTER_TO_DARKELF, 1);
		}
		else if (cond == 6)
		{
			st.setCond(7);
			playSound(player, SOUND_MIDDLE);
			giveItems(player, LETTER_TO_ELF, 1);
		}
	}
}