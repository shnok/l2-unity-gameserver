package com.shnok.javaserver.gameserver.scripting.quest;

import net.sf.l2j.gameserver.enums.QuestStatus;
import net.sf.l2j.gameserver.model.actor.Creature;
import net.sf.l2j.gameserver.model.actor.Npc;
import net.sf.l2j.gameserver.model.actor.Player;
import net.sf.l2j.gameserver.scripting.Quest;
import net.sf.l2j.gameserver.scripting.QuestState;

public class Q036_MakeASewingKit extends Quest
{
	private static final String QUEST_NAME = "Q036_MakeASewingKit";
	
	// Items
	private static final int REINFORCED_STEEL = 7163;
	private static final int ARTISANS_FRAME = 1891;
	private static final int ORIHARUKON = 1893;
	
	// Reward
	private static final int SEWING_KIT = 7078;
	
	public Q036_MakeASewingKit()
	{
		super(36, "Make a Sewing Kit");
		
		setItemsIds(REINFORCED_STEEL);
		
		addQuestStart(30847); // Ferris
		addTalkId(30847);
		
		addMyDying(20566); // Iron Golem
	}
	
	@Override
	public String onAdvEvent(String event, Npc npc, Player player)
	{
		String htmltext = event;
		QuestState st = player.getQuestList().getQuestState(QUEST_NAME);
		if (st == null)
			return htmltext;
		
		if (event.equalsIgnoreCase("30847-1.htm"))
		{
			st.setState(QuestStatus.STARTED);
			st.setCond(1);
			playSound(player, SOUND_ACCEPT);
		}
		else if (event.equalsIgnoreCase("30847-3.htm"))
		{
			st.setCond(3);
			playSound(player, SOUND_MIDDLE);
			takeItems(player, REINFORCED_STEEL, 5);
		}
		else if (event.equalsIgnoreCase("30847-5.htm"))
		{
			if (player.getInventory().getItemCount(ORIHARUKON) >= 10 && player.getInventory().getItemCount(ARTISANS_FRAME) >= 10)
			{
				takeItems(player, ARTISANS_FRAME, 10);
				takeItems(player, ORIHARUKON, 10);
				giveItems(player, SEWING_KIT, 1);
				playSound(player, SOUND_FINISH);
				st.exitQuest(false);
			}
			else
				htmltext = "30847-4a.htm";
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
				if (player.getStatus().getLevel() >= 60)
				{
					QuestState fwear = player.getQuestList().getQuestState("Q037_MakeFormalWear");
					if (fwear != null && fwear.getCond() == 6)
						htmltext = "30847-0.htm";
					else
						htmltext = "30847-0a.htm";
				}
				else
					htmltext = "30847-0b.htm";
				break;
			
			case STARTED:
				int cond = st.getCond();
				if (cond == 1)
					htmltext = "30847-1a.htm";
				else if (cond == 2)
					htmltext = "30847-2.htm";
				else if (cond == 3)
					htmltext = (player.getInventory().getItemCount(ORIHARUKON) < 10 || player.getInventory().getItemCount(ARTISANS_FRAME) < 10) ? "30847-4a.htm" : "30847-4.htm";
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
		
		final QuestState st = checkPlayerCondition(player, npc, 1);
		if (st == null)
			return;
		
		if (dropItems(player, REINFORCED_STEEL, 1, 5, 500000))
			st.setCond(2);
	}
}