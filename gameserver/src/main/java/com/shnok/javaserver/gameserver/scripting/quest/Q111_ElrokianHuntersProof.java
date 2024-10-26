package com.shnok.javaserver.gameserver.scripting.quest;

import net.sf.l2j.gameserver.enums.QuestStatus;
import net.sf.l2j.gameserver.model.actor.Creature;
import net.sf.l2j.gameserver.model.actor.Npc;
import net.sf.l2j.gameserver.model.actor.Player;
import net.sf.l2j.gameserver.scripting.Quest;
import net.sf.l2j.gameserver.scripting.QuestState;

public class Q111_ElrokianHuntersProof extends Quest
{
	private static final String QUEST_NAME = "Q111_ElrokianHuntersProof";
	
	// NPCs
	private static final int MARQUEZ = 32113;
	private static final int MUSHIKA = 32114;
	private static final int ASAMAH = 32115;
	private static final int KIRIKASHIN = 32116;
	
	// Items
	private static final int FRAGMENT = 8768;
	private static final int EXPEDITION_LETTER = 8769;
	private static final int CLAW = 8770;
	private static final int BONE = 8771;
	private static final int SKIN = 8772;
	private static final int PRACTICE_TRAP = 8773;
	
	public Q111_ElrokianHuntersProof()
	{
		super(111, "Elrokian Hunter's Proof");
		
		setItemsIds(FRAGMENT, EXPEDITION_LETTER, CLAW, BONE, SKIN, PRACTICE_TRAP);
		
		addQuestStart(MARQUEZ);
		addTalkId(MARQUEZ, MUSHIKA, ASAMAH, KIRIKASHIN);
		
		addMyDying(22196, 22197, 22198, 22218, 22200, 22201, 22202, 22219, 22208, 22209, 22210, 22221, 22203, 22204, 22205, 22220);
	}
	
	@Override
	public String onAdvEvent(String event, Npc npc, Player player)
	{
		String htmltext = event;
		QuestState st = player.getQuestList().getQuestState(QUEST_NAME);
		if (st == null)
			return htmltext;
		
		if (event.equalsIgnoreCase("32113-002.htm"))
		{
			st.setState(QuestStatus.STARTED);
			st.setCond(1);
			playSound(player, SOUND_ACCEPT);
		}
		else if (event.equalsIgnoreCase("32115-002.htm"))
		{
			st.setCond(3);
			playSound(player, SOUND_MIDDLE);
		}
		else if (event.equalsIgnoreCase("32113-009.htm"))
		{
			st.setCond(4);
			playSound(player, SOUND_MIDDLE);
		}
		else if (event.equalsIgnoreCase("32113-018.htm"))
		{
			st.setCond(6);
			playSound(player, SOUND_MIDDLE);
			takeItems(player, FRAGMENT, -1);
			giveItems(player, EXPEDITION_LETTER, 1);
		}
		else if (event.equalsIgnoreCase("32116-003.htm"))
		{
			st.setCond(7);
			playSound(player, "EtcSound.elcroki_song_full");
		}
		else if (event.equalsIgnoreCase("32116-005.htm"))
		{
			st.setCond(8);
			playSound(player, SOUND_MIDDLE);
		}
		else if (event.equalsIgnoreCase("32115-004.htm"))
		{
			st.setCond(9);
			playSound(player, SOUND_MIDDLE);
		}
		else if (event.equalsIgnoreCase("32115-006.htm"))
		{
			st.setCond(10);
			playSound(player, SOUND_MIDDLE);
		}
		else if (event.equalsIgnoreCase("32116-007.htm"))
		{
			takeItems(player, PRACTICE_TRAP, 1);
			giveItems(player, 8763, 1);
			giveItems(player, 8764, 100);
			rewardItems(player, 57, 1022636);
			playSound(player, SOUND_FINISH);
			st.exitQuest(false);
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
				htmltext = (player.getStatus().getLevel() < 75) ? "32113-000.htm" : "32113-001.htm";
				break;
			
			case STARTED:
				int cond = st.getCond();
				switch (npc.getNpcId())
				{
					case MARQUEZ:
						if (cond == 1 || cond == 2)
							htmltext = "32113-002.htm";
						else if (cond == 3)
							htmltext = "32113-003.htm";
						else if (cond == 4)
							htmltext = "32113-009.htm";
						else if (cond == 5)
							htmltext = "32113-010.htm";
						break;
					
					case MUSHIKA:
						if (cond == 1)
						{
							st.setCond(2);
							playSound(player, SOUND_MIDDLE);
						}
						htmltext = "32114-001.htm";
						break;
					
					case ASAMAH:
						if (cond == 2)
							htmltext = "32115-001.htm";
						else if (cond == 3)
							htmltext = "32115-002.htm";
						else if (cond == 8)
							htmltext = "32115-003.htm";
						else if (cond == 9)
							htmltext = "32115-004.htm";
						else if (cond == 10)
							htmltext = "32115-006.htm";
						else if (cond == 11)
						{
							htmltext = "32115-007.htm";
							st.setCond(12);
							playSound(player, SOUND_MIDDLE);
							takeItems(player, BONE, -1);
							takeItems(player, CLAW, -1);
							takeItems(player, SKIN, -1);
							giveItems(player, PRACTICE_TRAP, 1);
						}
						break;
					
					case KIRIKASHIN:
						if (cond < 6)
							htmltext = "32116-008.htm";
						else if (cond == 6)
						{
							htmltext = "32116-001.htm";
							takeItems(player, EXPEDITION_LETTER, 1);
						}
						else if (cond == 7)
							htmltext = "32116-004.htm";
						else if (cond == 12)
							htmltext = "32116-006.htm";
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
		Player player = killer.getActingPlayer();
		
		final QuestState st = getRandomPartyMemberState(player, npc, QuestStatus.STARTED);
		if (st == null)
			return;
		
		player = st.getPlayer();
		
		switch (npc.getNpcId())
		{
			case 22196, 22197, 22198, 22218:
				if (st.getCond() == 4 && dropItems(player, FRAGMENT, 1, 50, 250000))
					st.setCond(5);
				break;
			
			case 22200, 22201, 22202, 22219:
				if (st.getCond() == 10 && dropItems(player, CLAW, 1, 10, 650000))
					if (player.getInventory().getItemCount(BONE) >= 10 && player.getInventory().getItemCount(SKIN) >= 10)
						st.setCond(11);
				break;
			
			case 22208, 22209, 22210, 22221:
				if (st.getCond() == 10 && dropItems(player, SKIN, 1, 10, 650000))
					if (player.getInventory().getItemCount(CLAW) >= 10 && player.getInventory().getItemCount(BONE) >= 10)
						st.setCond(11);
				break;
			
			case 22203, 22204, 22205, 22220:
				if (st.getCond() == 10 && dropItems(player, BONE, 1, 10, 650000))
					if (player.getInventory().getItemCount(CLAW) >= 10 && player.getInventory().getItemCount(SKIN) >= 10)
						st.setCond(11);
				break;
		}
	}
}