package com.shnok.javaserver.gameserver.scripting.quest;

import com.shnok.javaserver.commons.random.Rnd;

import com.shnok.javaserver.gameserver.enums.QuestStatus;
import com.shnok.javaserver.gameserver.model.actor.Creature;
import com.shnok.javaserver.gameserver.model.actor.Npc;
import com.shnok.javaserver.gameserver.model.actor.Player;
import com.shnok.javaserver.gameserver.scripting.Quest;
import com.shnok.javaserver.gameserver.scripting.QuestState;

public class Q382_KailsMagicCoin extends Quest
{
	private static final String QUEST_NAME = "Q382_KailsMagicCoin";
	
	// Monsters
	private static final int FALLEN_ORC = 21017;
	private static final int FALLEN_ORC_ARCHER = 21019;
	private static final int FALLEN_ORC_SHAMAN = 21020;
	private static final int FALLEN_ORC_CAPTAIN = 21022;
	
	// Items
	private static final int ROYAL_MEMBERSHIP = 5898;
	private static final int SILVER_BASILISK = 5961;
	private static final int GOLD_GOLEM = 5962;
	private static final int BLOOD_DRAGON = 5963;
	
	public Q382_KailsMagicCoin()
	{
		super(382, "Kail's Magic Coin");
		
		setItemsIds(SILVER_BASILISK, GOLD_GOLEM, BLOOD_DRAGON);
		
		addQuestStart(30687); // Vergara
		addTalkId(30687);
		
		addMyDying(FALLEN_ORC, FALLEN_ORC_ARCHER, FALLEN_ORC_SHAMAN, FALLEN_ORC_CAPTAIN);
	}
	
	@Override
	public String onAdvEvent(String event, Npc npc, Player player)
	{
		String htmltext = event;
		QuestState st = player.getQuestList().getQuestState(QUEST_NAME);
		if (st == null)
			return htmltext;
		
		if (event.equalsIgnoreCase("30687-03.htm"))
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
				htmltext = (player.getStatus().getLevel() < 55 || !player.getInventory().hasItem(ROYAL_MEMBERSHIP)) ? "30687-01.htm" : "30687-02.htm";
				break;
			
			case STARTED:
				htmltext = "30687-04.htm";
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
		
		switch (npc.getNpcId())
		{
			case FALLEN_ORC:
				dropItems(player, SILVER_BASILISK, 1, 0, 100000);
				break;
			
			case FALLEN_ORC_ARCHER:
				dropItems(player, GOLD_GOLEM, 1, 0, 100000);
				break;
			
			case FALLEN_ORC_SHAMAN:
				dropItems(player, BLOOD_DRAGON, 1, 0, 100000);
				break;
			
			case FALLEN_ORC_CAPTAIN:
				dropItems(player, 5961 + Rnd.get(3), 1, 0, 100000);
				break;
		}
	}
}