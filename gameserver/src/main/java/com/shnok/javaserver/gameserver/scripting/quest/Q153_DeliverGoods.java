package com.shnok.javaserver.gameserver.scripting.quest;

import net.sf.l2j.gameserver.enums.QuestStatus;
import net.sf.l2j.gameserver.model.actor.Npc;
import net.sf.l2j.gameserver.model.actor.Player;
import net.sf.l2j.gameserver.scripting.Quest;
import net.sf.l2j.gameserver.scripting.QuestState;

public class Q153_DeliverGoods extends Quest
{
	private static final String QUEST_NAME = "Q153_DeliverGoods";
	
	// NPCs
	private static final int JACKSON = 30002;
	private static final int SILVIA = 30003;
	private static final int ARNOLD = 30041;
	private static final int RANT = 30054;
	
	// Items
	private static final int DELIVERY_LIST = 1012;
	private static final int HEAVY_WOOD_BOX = 1013;
	private static final int CLOTH_BUNDLE = 1014;
	private static final int CLAY_POT = 1015;
	private static final int JACKSON_RECEIPT = 1016;
	private static final int SILVIA_RECEIPT = 1017;
	private static final int RANT_RECEIPT = 1018;
	
	// Rewards
	private static final int SOULSHOT_NO_GRADE = 1835;
	private static final int RING_OF_KNOWLEDGE = 875;
	
	public Q153_DeliverGoods()
	{
		super(153, "Deliver Goods");
		
		setItemsIds(DELIVERY_LIST, HEAVY_WOOD_BOX, CLOTH_BUNDLE, CLAY_POT, JACKSON_RECEIPT, SILVIA_RECEIPT, RANT_RECEIPT);
		
		addQuestStart(ARNOLD);
		addTalkId(JACKSON, SILVIA, ARNOLD, RANT);
	}
	
	@Override
	public String onAdvEvent(String event, Npc npc, Player player)
	{
		String htmltext = event;
		QuestState st = player.getQuestList().getQuestState(QUEST_NAME);
		if (st == null)
			return htmltext;
		
		if (event.equalsIgnoreCase("30041-02.htm"))
		{
			st.setState(QuestStatus.STARTED);
			st.setCond(1);
			playSound(player, SOUND_ACCEPT);
			giveItems(player, DELIVERY_LIST, 1);
			giveItems(player, CLAY_POT, 1);
			giveItems(player, CLOTH_BUNDLE, 1);
			giveItems(player, HEAVY_WOOD_BOX, 1);
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
				htmltext = (player.getStatus().getLevel() < 2) ? "30041-00.htm" : "30041-01.htm";
				break;
			
			case STARTED:
				switch (npc.getNpcId())
				{
					case ARNOLD:
						if (st.getCond() == 1)
							htmltext = "30041-03.htm";
						else if (st.getCond() == 2)
						{
							htmltext = "30041-04.htm";
							takeItems(player, DELIVERY_LIST, 1);
							takeItems(player, JACKSON_RECEIPT, 1);
							takeItems(player, SILVIA_RECEIPT, 1);
							takeItems(player, RANT_RECEIPT, 1);
							giveItems(player, RING_OF_KNOWLEDGE, 1);
							giveItems(player, RING_OF_KNOWLEDGE, 1);
							rewardExpAndSp(player, 600, 0);
							playSound(player, SOUND_FINISH);
							st.exitQuest(false);
						}
						break;
					
					case JACKSON:
						if (player.getInventory().hasItem(HEAVY_WOOD_BOX))
						{
							htmltext = "30002-01.htm";
							takeItems(player, HEAVY_WOOD_BOX, 1);
							giveItems(player, JACKSON_RECEIPT, 1);
							
							if (player.getInventory().hasItems(SILVIA_RECEIPT, RANT_RECEIPT))
							{
								st.setCond(2);
								playSound(player, SOUND_MIDDLE);
							}
							else
								playSound(player, SOUND_ITEMGET);
						}
						else
							htmltext = "30002-02.htm";
						break;
					
					case SILVIA:
						if (player.getInventory().hasItem(CLOTH_BUNDLE))
						{
							htmltext = "30003-01.htm";
							takeItems(player, CLOTH_BUNDLE, 1);
							giveItems(player, SILVIA_RECEIPT, 1);
							giveItems(player, SOULSHOT_NO_GRADE, 3);
							
							if (player.getInventory().hasItems(JACKSON_RECEIPT, RANT_RECEIPT))
							{
								st.setCond(2);
								playSound(player, SOUND_MIDDLE);
							}
							else
								playSound(player, SOUND_ITEMGET);
						}
						else
							htmltext = "30003-02.htm";
						break;
					
					case RANT:
						if (player.getInventory().hasItem(CLAY_POT))
						{
							htmltext = "30054-01.htm";
							takeItems(player, CLAY_POT, 1);
							giveItems(player, RANT_RECEIPT, 1);
							
							if (player.getInventory().hasItems(JACKSON_RECEIPT, SILVIA_RECEIPT))
							{
								st.setCond(2);
								playSound(player, SOUND_MIDDLE);
							}
							else
								playSound(player, SOUND_ITEMGET);
						}
						else
							htmltext = "30054-02.htm";
						break;
				}
				break;
			
			case COMPLETED:
				htmltext = getAlreadyCompletedMsg();
				break;
		}
		
		return htmltext;
	}
}