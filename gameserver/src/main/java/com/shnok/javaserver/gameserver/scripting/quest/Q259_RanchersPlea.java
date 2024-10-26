package com.shnok.javaserver.gameserver.scripting.quest;

import net.sf.l2j.gameserver.enums.QuestStatus;
import net.sf.l2j.gameserver.model.actor.Creature;
import net.sf.l2j.gameserver.model.actor.Npc;
import net.sf.l2j.gameserver.model.actor.Player;
import net.sf.l2j.gameserver.scripting.Quest;
import net.sf.l2j.gameserver.scripting.QuestState;

public class Q259_RanchersPlea extends Quest
{
	private static final String QUEST_NAME = "Q259_RanchersPlea";
	
	// NPCs
	private static final int EDMOND = 30497;
	private static final int MARIUS = 30405;
	
	// Monsters
	private static final int GIANT_SPIDER = 20103;
	private static final int TALON_SPIDER = 20106;
	private static final int BLADE_SPIDER = 20108;
	
	// Items
	private static final int GIANT_SPIDER_SKIN = 1495;
	
	// Rewards
	private static final int ADENA = 57;
	private static final int HEALING_POTION = 1061;
	private static final int WOODEN_ARROW = 17;
	
	public Q259_RanchersPlea()
	{
		super(259, "Rancher's Plea");
		
		setItemsIds(GIANT_SPIDER_SKIN);
		
		addQuestStart(EDMOND);
		addTalkId(EDMOND, MARIUS);
		
		addMyDying(GIANT_SPIDER, TALON_SPIDER, BLADE_SPIDER);
	}
	
	@Override
	public String onAdvEvent(String event, Npc npc, Player player)
	{
		String htmltext = event;
		QuestState st = player.getQuestList().getQuestState(QUEST_NAME);
		if (st == null)
			return htmltext;
		
		if (event.equalsIgnoreCase("30497-03.htm"))
		{
			st.setState(QuestStatus.STARTED);
			st.setCond(1);
			playSound(player, SOUND_ACCEPT);
		}
		else if (event.equalsIgnoreCase("30497-06.htm"))
		{
			playSound(player, SOUND_FINISH);
			st.exitQuest(true);
		}
		else if (event.equalsIgnoreCase("30405-04.htm"))
		{
			if (player.getInventory().getItemCount(GIANT_SPIDER_SKIN) >= 10)
			{
				takeItems(player, GIANT_SPIDER_SKIN, 10);
				rewardItems(player, HEALING_POTION, 1);
			}
			else
				htmltext = "<html><body>Incorrect item count</body></html>";
		}
		else if (event.equalsIgnoreCase("30405-05.htm"))
		{
			if (player.getInventory().getItemCount(GIANT_SPIDER_SKIN) >= 10)
			{
				takeItems(player, GIANT_SPIDER_SKIN, 10);
				rewardItems(player, WOODEN_ARROW, 50);
			}
			else
				htmltext = "<html><body>Incorrect item count</body></html>";
		}
		else if (event.equalsIgnoreCase("30405-07.htm"))
		{
			if (player.getInventory().getItemCount(GIANT_SPIDER_SKIN) >= 10)
				htmltext = "30405-06.htm";
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
				htmltext = (player.getStatus().getLevel() < 15) ? "30497-01.htm" : "30497-02.htm";
				break;
			
			case STARTED:
				final int count = player.getInventory().getItemCount(GIANT_SPIDER_SKIN);
				switch (npc.getNpcId())
				{
					case EDMOND:
						if (count == 0)
							htmltext = "30497-04.htm";
						else
						{
							htmltext = "30497-05.htm";
							takeItems(player, GIANT_SPIDER_SKIN, -1);
							rewardItems(player, ADENA, ((count >= 10) ? 250 : 0) + count * 25);
						}
						break;
					
					case MARIUS:
						htmltext = (count < 10) ? "30405-01.htm" : "30405-02.htm";
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
		
		dropItemsAlways(player, GIANT_SPIDER_SKIN, 1, 0);
	}
}