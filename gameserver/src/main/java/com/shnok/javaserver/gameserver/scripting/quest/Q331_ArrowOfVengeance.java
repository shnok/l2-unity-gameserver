package com.shnok.javaserver.gameserver.scripting.quest;

import com.shnok.javaserver.gameserver.enums.QuestStatus;
import com.shnok.javaserver.gameserver.model.actor.Creature;
import com.shnok.javaserver.gameserver.model.actor.Npc;
import com.shnok.javaserver.gameserver.model.actor.Player;
import com.shnok.javaserver.gameserver.scripting.Quest;
import com.shnok.javaserver.gameserver.scripting.QuestState;

public class Q331_ArrowOfVengeance extends Quest
{
	private static final String QUEST_NAME = "Q331_ArrowOfVengeance";
	
	// Items
	private static final int HARPY_FEATHER = 1452;
	private static final int MEDUSA_VENOM = 1453;
	private static final int WYRM_TOOTH = 1454;
	
	public Q331_ArrowOfVengeance()
	{
		super(331, "Arrow of Vengeance");
		
		setItemsIds(HARPY_FEATHER, MEDUSA_VENOM, WYRM_TOOTH);
		
		addQuestStart(30125); // Belton
		addTalkId(30125);
		
		addMyDying(20145, 20158, 20176);
	}
	
	@Override
	public String onAdvEvent(String event, Npc npc, Player player)
	{
		String htmltext = event;
		QuestState st = player.getQuestList().getQuestState(QUEST_NAME);
		if (st == null)
			return htmltext;
		
		if (event.equalsIgnoreCase("30125-03.htm"))
		{
			st.setState(QuestStatus.STARTED);
			st.setCond(1);
			playSound(player, SOUND_ACCEPT);
		}
		else if (event.equalsIgnoreCase("30125-06.htm"))
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
				htmltext = (player.getStatus().getLevel() < 32) ? "30125-01.htm" : "30125-02.htm";
				break;
			
			case STARTED:
				final int harpyFeather = player.getInventory().getItemCount(HARPY_FEATHER);
				final int medusaVenom = player.getInventory().getItemCount(MEDUSA_VENOM);
				final int wyrmTooth = player.getInventory().getItemCount(WYRM_TOOTH);
				
				if (harpyFeather + medusaVenom + wyrmTooth > 0)
				{
					htmltext = "30125-05.htm";
					takeItems(player, HARPY_FEATHER, -1);
					takeItems(player, MEDUSA_VENOM, -1);
					takeItems(player, WYRM_TOOTH, -1);
					
					int reward = harpyFeather * 78 + medusaVenom * 88 + wyrmTooth * 92;
					if (harpyFeather + medusaVenom + wyrmTooth > 10)
						reward += 3100;
					
					rewardItems(player, 57, reward);
				}
				else
					htmltext = "30125-04.htm";
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
		
		switch (npc.getNpcId())
		{
			case 20145:
				dropItems(player, HARPY_FEATHER, 1, 0, 500000);
				break;
			
			case 20158:
				dropItems(player, MEDUSA_VENOM, 1, 0, 500000);
				break;
			
			case 20176:
				dropItems(player, WYRM_TOOTH, 1, 0, 500000);
				break;
		}
	}
}