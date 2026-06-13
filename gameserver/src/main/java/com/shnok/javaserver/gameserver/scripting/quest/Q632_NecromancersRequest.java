package com.shnok.javaserver.gameserver.scripting.quest;

import com.shnok.javaserver.commons.util.ArraysUtil;

import com.shnok.javaserver.gameserver.enums.QuestStatus;
import com.shnok.javaserver.gameserver.model.actor.Creature;
import com.shnok.javaserver.gameserver.model.actor.Npc;
import com.shnok.javaserver.gameserver.model.actor.Player;
import com.shnok.javaserver.gameserver.scripting.Quest;
import com.shnok.javaserver.gameserver.scripting.QuestState;

public class Q632_NecromancersRequest extends Quest
{
	private static final String QUEST_NAME = "Q632_NecromancersRequest";
	
	// Monsters
	private static final int[] VAMPIRES =
	{
		21568,
		21573,
		21582,
		21585,
		21586,
		21587,
		21588,
		21589,
		21590,
		21591,
		21592,
		21593,
		21594,
		21595
	};
	
	private static final int[] UNDEADS =
	{
		21547,
		21548,
		21549,
		21551,
		21552,
		21555,
		21556,
		21562,
		21571,
		21576,
		21577,
		21579
	};
	
	// Items
	private static final int VAMPIRE_HEART = 7542;
	private static final int ZOMBIE_BRAIN = 7543;
	
	public Q632_NecromancersRequest()
	{
		super(632, "Necromancer's Request");
		
		setItemsIds(VAMPIRE_HEART, ZOMBIE_BRAIN);
		
		addQuestStart(31522); // Mysterious Wizard
		addTalkId(31522);
		
		addMyDying(VAMPIRES);
		addMyDying(UNDEADS);
	}
	
	@Override
	public String onAdvEvent(String event, Npc npc, Player player)
	{
		String htmltext = event;
		QuestState st = player.getQuestList().getQuestState(QUEST_NAME);
		if (st == null)
			return htmltext;
		
		if (event.equalsIgnoreCase("31522-03.htm"))
		{
			st.setState(QuestStatus.STARTED);
			st.setCond(1);
			playSound(player, SOUND_ACCEPT);
		}
		else if (event.equalsIgnoreCase("31522-06.htm"))
		{
			if (player.getInventory().getItemCount(VAMPIRE_HEART) >= 200)
			{
				st.setCond(1);
				playSound(player, SOUND_MIDDLE);
				takeItems(player, VAMPIRE_HEART, -1);
				rewardItems(player, 57, 120000);
			}
			else
				htmltext = "31522-09.htm";
		}
		else if (event.equalsIgnoreCase("31522-08.htm"))
		{
			playSound(player, SOUND_FINISH);
			st.exitQuest(true);
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
				htmltext = (player.getStatus().getLevel() < 63) ? "31522-01.htm" : "31522-02.htm";
				break;
			
			case STARTED:
				htmltext = (player.getInventory().getItemCount(VAMPIRE_HEART) >= 200) ? "31522-05.htm" : "31522-04.htm";
				break;
		}
		
		return htmltext;
	}
	
	@Override
	public void onMyDying(Npc npc, Creature killer)
	{
		final Player player = killer.getActingPlayer();
		
		final QuestState st = getRandomPartyMemberState(player, npc, QuestStatus.STARTED);
		if (st == null)
			return;
		
		if (ArraysUtil.contains(UNDEADS, npc.getNpcId()))
		{
			dropItems(st.getPlayer(), ZOMBIE_BRAIN, 1, 0, 330000);
			return;
		}
		
		if (st.getCond() == 1 && dropItems(st.getPlayer(), VAMPIRE_HEART, 1, 200, 500000))
			st.setCond(2);
	}
}