package com.shnok.javaserver.gameserver.scripting.quest;

import com.shnok.javaserver.gameserver.enums.QuestStatus;
import com.shnok.javaserver.gameserver.model.actor.Creature;
import com.shnok.javaserver.gameserver.model.actor.Npc;
import com.shnok.javaserver.gameserver.model.actor.Player;
import com.shnok.javaserver.gameserver.scripting.Quest;
import com.shnok.javaserver.gameserver.scripting.QuestState;

public class Q034_InSearchOfCloth extends Quest
{
	private static final String QUEST_NAME = "Q034_InSearchOfCloth";
	
	// NPCs
	private static final int RADIA = 30088;
	private static final int RALFORD = 30165;
	private static final int VARAN = 30294;
	
	// Monsters
	private static final int TRISALIM_SPIDER = 20560;
	private static final int TRISALIM_TARANTULA = 20561;
	
	// Items
	private static final int SPINNERET = 7528;
	private static final int SUEDE = 1866;
	private static final int THREAD = 1868;
	private static final int SPIDERSILK = 7161;
	
	// Rewards
	private static final int MYSTERIOUS_CLOTH = 7076;
	
	public Q034_InSearchOfCloth()
	{
		super(34, "In Search of Cloth");
		
		setItemsIds(SPINNERET, SPIDERSILK);
		
		addQuestStart(RADIA);
		addTalkId(RADIA, RALFORD, VARAN);
		
		addMyDying(TRISALIM_SPIDER, TRISALIM_TARANTULA);
	}
	
	@Override
	public String onAdvEvent(String event, Npc npc, Player player)
	{
		String htmltext = event;
		QuestState st = player.getQuestList().getQuestState(QUEST_NAME);
		if (st == null)
			return htmltext;
		
		if (event.equalsIgnoreCase("30088-1.htm"))
		{
			st.setState(QuestStatus.STARTED);
			st.setCond(1);
			playSound(player, SOUND_ACCEPT);
		}
		else if (event.equalsIgnoreCase("30294-1.htm"))
		{
			st.setCond(2);
			playSound(player, SOUND_MIDDLE);
		}
		else if (event.equalsIgnoreCase("30088-3.htm"))
		{
			st.setCond(3);
			playSound(player, SOUND_MIDDLE);
		}
		else if (event.equalsIgnoreCase("30165-1.htm"))
		{
			st.setCond(4);
			playSound(player, SOUND_MIDDLE);
		}
		else if (event.equalsIgnoreCase("30165-3.htm"))
		{
			st.setCond(6);
			playSound(player, SOUND_MIDDLE);
			takeItems(player, SPINNERET, 10);
			giveItems(player, SPIDERSILK, 1);
		}
		else if (event.equalsIgnoreCase("30088-5.htm"))
		{
			if (player.getInventory().getItemCount(SUEDE) >= 3000 && player.getInventory().getItemCount(THREAD) >= 5000 && player.getInventory().hasItem(SPIDERSILK))
			{
				takeItems(player, SPIDERSILK, 1);
				takeItems(player, SUEDE, 3000);
				takeItems(player, THREAD, 5000);
				giveItems(player, MYSTERIOUS_CLOTH, 1);
				playSound(player, SOUND_FINISH);
				st.exitQuest(false);
			}
			else
				htmltext = "30088-4a.htm";
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
						htmltext = "30088-0.htm";
					else
						htmltext = "30088-0a.htm";
				}
				else
					htmltext = "30088-0b.htm";
				break;
			
			case STARTED:
				int cond = st.getCond();
				switch (npc.getNpcId())
				{
					case RADIA:
						if (cond == 1)
							htmltext = "30088-1a.htm";
						else if (cond == 2)
							htmltext = "30088-2.htm";
						else if (cond == 3)
							htmltext = "30088-3a.htm";
						else if (cond == 6)
						{
							if (player.getInventory().getItemCount(SUEDE) < 3000 || player.getInventory().getItemCount(THREAD) < 5000 || !player.getInventory().hasItem(SPIDERSILK))
								htmltext = "30088-4a.htm";
							else
								htmltext = "30088-4.htm";
						}
						break;
					
					case VARAN:
						if (cond == 1)
							htmltext = "30294-0.htm";
						else if (cond > 1)
							htmltext = "30294-1a.htm";
						break;
					
					case RALFORD:
						if (cond == 3)
							htmltext = "30165-0.htm";
						else if (cond == 4 && player.getInventory().getItemCount(SPINNERET) < 10)
							htmltext = "30165-1a.htm";
						else if (cond == 5)
							htmltext = "30165-2.htm";
						else if (cond > 5)
							htmltext = "30165-3a.htm";
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
		final Player player = killer.getActingPlayer();
		
		final QuestState st = checkPlayerCondition(player, npc, 4);
		if (st == null)
			return;
		
		if (dropItems(player, SPINNERET, 1, 10, 500000))
			st.setCond(5);
	}
}