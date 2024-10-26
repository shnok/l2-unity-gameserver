package com.shnok.javaserver.gameserver.scripting.quest;

import net.sf.l2j.commons.random.Rnd;

import net.sf.l2j.gameserver.enums.QuestStatus;
import net.sf.l2j.gameserver.model.actor.Creature;
import net.sf.l2j.gameserver.model.actor.Npc;
import net.sf.l2j.gameserver.model.actor.Player;
import net.sf.l2j.gameserver.scripting.Quest;
import net.sf.l2j.gameserver.scripting.QuestState;

public class Q258_BringWolfPelts extends Quest
{
	private static final String QUEST_NAME = "Q258_BringWolfPelts";
	
	// Item
	private static final int WOLF_PELT = 702;
	
	// Rewards
	private static final int COTTON_SHIRT = 390;
	private static final int LEATHER_PANTS = 29;
	private static final int LEATHER_SHIRT = 22;
	private static final int SHORT_LEATHER_GLOVES = 1119;
	private static final int TUNIC = 426;
	
	public Q258_BringWolfPelts()
	{
		super(258, "Bring Wolf Pelts");
		
		setItemsIds(WOLF_PELT);
		
		addQuestStart(30001); // Lector
		addTalkId(30001);
		
		addMyDying(20120, 20442); // Wolf, Elder Wolf
	}
	
	@Override
	public String onAdvEvent(String event, Npc npc, Player player)
	{
		String htmltext = event;
		QuestState st = player.getQuestList().getQuestState(QUEST_NAME);
		if (st == null)
			return htmltext;
		
		if (event.equalsIgnoreCase("30001-03.htm"))
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
		QuestState st = player.getQuestList().getQuestState(QUEST_NAME);
		String htmltext = getNoQuestMsg();
		if (st == null)
			return htmltext;
		
		switch (st.getState())
		{
			case CREATED:
				htmltext = (player.getStatus().getLevel() < 3) ? "30001-01.htm" : "30001-02.htm";
				break;
			
			case STARTED:
				if (player.getInventory().getItemCount(WOLF_PELT) < 40)
					htmltext = "30001-05.htm";
				else
				{
					takeItems(player, WOLF_PELT, -1);
					int randomNumber = Rnd.get(16);
					
					// Reward is based on a random number (1D16).
					if (randomNumber == 0)
						giveItems(player, COTTON_SHIRT, 1);
					else if (randomNumber < 6)
						giveItems(player, LEATHER_PANTS, 1);
					else if (randomNumber < 9)
						giveItems(player, LEATHER_SHIRT, 1);
					else if (randomNumber < 13)
						giveItems(player, SHORT_LEATHER_GLOVES, 1);
					else
						giveItems(player, TUNIC, 1);
					
					htmltext = "30001-06.htm";
					
					if (randomNumber == 0)
						playSound(player, SOUND_JACKPOT);
					else
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
		
		if (dropItemsAlways(player, WOLF_PELT, 1, 40))
			st.setCond(2);
	}
}