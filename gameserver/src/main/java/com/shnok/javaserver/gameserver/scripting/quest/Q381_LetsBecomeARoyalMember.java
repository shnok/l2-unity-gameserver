package com.shnok.javaserver.gameserver.scripting.quest;

import net.sf.l2j.gameserver.enums.QuestStatus;
import net.sf.l2j.gameserver.model.actor.Creature;
import net.sf.l2j.gameserver.model.actor.Npc;
import net.sf.l2j.gameserver.model.actor.Player;
import net.sf.l2j.gameserver.scripting.Quest;
import net.sf.l2j.gameserver.scripting.QuestState;

public class Q381_LetsBecomeARoyalMember extends Quest
{
	private static final String QUEST_NAME = "Q381_LetsBecomeARoyalMember";
	
	// NPCs
	private static final int SORINT = 30232;
	private static final int SANDRA = 30090;
	
	// Items
	private static final int KAIL_COIN = 5899;
	private static final int COIN_ALBUM = 5900;
	private static final int GOLDEN_CLOVER_COIN = 7569;
	private static final int COIN_COLLECTOR_MEMBERSHIP = 3813;
	
	// Reward
	private static final int ROYAL_MEMBERSHIP = 5898;
	
	public Q381_LetsBecomeARoyalMember()
	{
		super(381, "Lets Become a Royal Member!");
		
		setItemsIds(KAIL_COIN, GOLDEN_CLOVER_COIN);
		
		addQuestStart(SORINT);
		addTalkId(SORINT, SANDRA);
		
		addMyDying(21018, 27316);
	}
	
	@Override
	public String onAdvEvent(String event, Npc npc, Player player)
	{
		String htmltext = event;
		QuestState st = player.getQuestList().getQuestState(QUEST_NAME);
		if (st == null)
			return htmltext;
		
		if (event.equalsIgnoreCase("30090-02.htm"))
			st.set("aCond", 1); // Alternative cond used for Sandra.
		else if (event.equalsIgnoreCase("30232-03.htm"))
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
				htmltext = (player.getStatus().getLevel() < 55 || !player.getInventory().hasItem(COIN_COLLECTOR_MEMBERSHIP)) ? "30232-02.htm" : "30232-01.htm";
				break;
			
			case STARTED:
				switch (npc.getNpcId())
				{
					case SORINT:
						if (!player.getInventory().hasItem(KAIL_COIN))
							htmltext = "30232-04.htm";
						else if (!player.getInventory().hasItem(COIN_ALBUM))
							htmltext = "30232-05.htm";
						else
						{
							htmltext = "30232-06.htm";
							takeItems(player, KAIL_COIN, -1);
							takeItems(player, COIN_ALBUM, -1);
							giveItems(player, ROYAL_MEMBERSHIP, 1);
							playSound(player, SOUND_FINISH);
							st.exitQuest(true);
						}
						break;
					
					case SANDRA:
						if (!player.getInventory().hasItem(COIN_ALBUM))
						{
							if (st.getInteger("aCond") == 0)
								htmltext = "30090-01.htm";
							else
							{
								if (!player.getInventory().hasItem(GOLDEN_CLOVER_COIN))
									htmltext = "30090-03.htm";
								else
								{
									htmltext = "30090-04.htm";
									takeItems(player, GOLDEN_CLOVER_COIN, -1);
									giveItems(player, COIN_ALBUM, 1);
								}
							}
						}
						else
							htmltext = "30090-05.htm";
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
		
		final QuestState st = checkPlayerState(player, npc, QuestStatus.STARTED);
		if (st == null)
			return;
		
		if (npc.getNpcId() == 21018)
			dropItems(player, KAIL_COIN, 1, 1, 50000);
		else if (st.getInteger("aCond") == 1)
			dropItemsAlways(player, GOLDEN_CLOVER_COIN, 1, 1);
	}
}