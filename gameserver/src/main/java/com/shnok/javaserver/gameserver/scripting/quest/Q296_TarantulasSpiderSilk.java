package com.shnok.javaserver.gameserver.scripting.quest;

import net.sf.l2j.commons.random.Rnd;

import net.sf.l2j.gameserver.enums.QuestStatus;
import net.sf.l2j.gameserver.model.actor.Creature;
import net.sf.l2j.gameserver.model.actor.Npc;
import net.sf.l2j.gameserver.model.actor.Player;
import net.sf.l2j.gameserver.scripting.Quest;
import net.sf.l2j.gameserver.scripting.QuestState;

public class Q296_TarantulasSpiderSilk extends Quest
{
	private static final String QUEST_NAME = "Q296_TarantulasSpiderSilk";
	
	// NPCs
	private static final int MION = 30519;
	private static final int DEFENDER_NATHAN = 30548;
	
	// Quest Items
	private static final int TARANTULA_SPIDER_SILK = 1493;
	private static final int TARANTULA_SPINNERETTE = 1494;
	
	// Items
	private static final int RING_OF_RACCOON = 1508;
	private static final int RING_OF_FIREFLY = 1509;
	
	public Q296_TarantulasSpiderSilk()
	{
		super(296, "Tarantula's Spider Silk");
		
		setItemsIds(TARANTULA_SPIDER_SILK, TARANTULA_SPINNERETTE);
		
		addQuestStart(MION);
		addTalkId(MION, DEFENDER_NATHAN);
		
		addMyDying(20394, 20403, 20508); // Crimson Tarantula, Hunter Tarantula, Plunder arantula
	}
	
	@Override
	public String onAdvEvent(String event, Npc npc, Player player)
	{
		String htmltext = event;
		QuestState st = player.getQuestList().getQuestState(QUEST_NAME);
		if (st == null)
			return htmltext;
		
		if (event.equalsIgnoreCase("30519-03.htm"))
		{
			if (player.getInventory().hasAtLeastOneItem(RING_OF_RACCOON, RING_OF_FIREFLY))
			{
				st.setState(QuestStatus.STARTED);
				st.setCond(1);
				playSound(player, SOUND_ACCEPT);
			}
			else
				htmltext = "30519-03a.htm";
		}
		else if (event.equalsIgnoreCase("30519-06.htm"))
		{
			takeItems(player, TARANTULA_SPIDER_SILK, -1);
			takeItems(player, TARANTULA_SPINNERETTE, -1);
			playSound(player, SOUND_FINISH);
			st.exitQuest(true);
		}
		else if (event.equalsIgnoreCase("30548-02.htm"))
		{
			final int count = player.getInventory().getItemCount(TARANTULA_SPINNERETTE);
			if (count > 0)
			{
				htmltext = "30548-03.htm";
				takeItems(player, TARANTULA_SPINNERETTE, -1);
				giveItems(player, TARANTULA_SPIDER_SILK, count * (15 + Rnd.get(10)));
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
				htmltext = (player.getStatus().getLevel() < 15) ? "30519-01.htm" : "30519-02.htm";
				break;
			
			case STARTED:
				switch (npc.getNpcId())
				{
					case MION:
						final int count = player.getInventory().getItemCount(TARANTULA_SPIDER_SILK);
						if (count == 0)
							htmltext = "30519-04.htm";
						else
						{
							htmltext = "30519-05.htm";
							takeItems(player, TARANTULA_SPIDER_SILK, -1);
							rewardItems(player, 57, ((count >= 10) ? 2000 : 0) + count * 30);
						}
						break;
					
					case DEFENDER_NATHAN:
						htmltext = "30548-01.htm";
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
		
		final int chance = Rnd.get(100);
		if (chance > 95)
			dropItemsAlways(player, TARANTULA_SPINNERETTE, 1, 0);
		else if (chance > 45)
			dropItemsAlways(player, TARANTULA_SPIDER_SILK, 1, 0);
	}
}