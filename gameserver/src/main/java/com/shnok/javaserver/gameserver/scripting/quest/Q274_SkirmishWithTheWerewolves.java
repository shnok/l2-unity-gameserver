package com.shnok.javaserver.gameserver.scripting.quest;

import com.shnok.javaserver.commons.random.Rnd;

import com.shnok.javaserver.gameserver.enums.QuestStatus;
import com.shnok.javaserver.gameserver.enums.actors.ClassRace;
import com.shnok.javaserver.gameserver.model.actor.Creature;
import com.shnok.javaserver.gameserver.model.actor.Npc;
import com.shnok.javaserver.gameserver.model.actor.Player;
import com.shnok.javaserver.gameserver.scripting.Quest;
import com.shnok.javaserver.gameserver.scripting.QuestState;

public class Q274_SkirmishWithTheWerewolves extends Quest
{
	private static final String QUEST_NAME = "Q274_SkirmishWithTheWerewolves";
	
	// Needed items
	private static final int NECKLACE_OF_VALOR = 1507;
	private static final int NECKLACE_OF_COURAGE = 1506;
	
	// Items
	private static final int MARAKU_WEREWOLF_HEAD = 1477;
	private static final int MARAKU_WOLFMEN_TOTEM = 1501;
	
	public Q274_SkirmishWithTheWerewolves()
	{
		super(274, "Skirmish with the Werewolves");
		
		setItemsIds(MARAKU_WEREWOLF_HEAD, MARAKU_WOLFMEN_TOTEM);
		
		addQuestStart(30569);
		addTalkId(30569);
		
		addMyDying(20363, 20364);
	}
	
	@Override
	public String onAdvEvent(String event, Npc npc, Player player)
	{
		QuestState st = player.getQuestList().getQuestState(QUEST_NAME);
		String htmltext = event;
		if (st == null)
			return htmltext;
		
		if (event.equalsIgnoreCase("30569-03.htm"))
		{
			st.setState(QuestStatus.STARTED);
			st.setCond(1);
			playSound(player, SOUND_ACCEPT);
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
				if (player.getRace() != ClassRace.ORC)
					htmltext = "30569-00.htm";
				else if (player.getStatus().getLevel() < 9)
					htmltext = "30569-01.htm";
				else if (player.getInventory().hasAtLeastOneItem(NECKLACE_OF_COURAGE, NECKLACE_OF_VALOR))
					htmltext = "30569-02.htm";
				else
					htmltext = "30569-07.htm";
				break;
			
			case STARTED:
				if (st.getCond() == 1)
					htmltext = "30569-04.htm";
				else
				{
					htmltext = "30569-05.htm";
					
					int amount = 3500 + player.getInventory().getItemCount(MARAKU_WOLFMEN_TOTEM) * 600;
					
					takeItems(player, MARAKU_WEREWOLF_HEAD, -1);
					takeItems(player, MARAKU_WOLFMEN_TOTEM, -1);
					rewardItems(player, 57, amount);
					
					playSound(player, SOUND_FINISH);
					st.exitQuest(true);
				}
				break;
		}
		
		return htmltext;
	}
	
	@Override
	public void onMyDying(Npc npc, Creature killer)
	{
		final Player player = killer.getActingPlayer();
		
		final QuestState st = checkPlayerCondition(player, npc, 1);
		if (st == null)
			return;
		
		if (dropItemsAlways(player, MARAKU_WEREWOLF_HEAD, 1, 40))
			st.setCond(2);
		
		if (Rnd.get(100) < 6)
			giveItems(player, MARAKU_WOLFMEN_TOTEM, 1);
	}
}