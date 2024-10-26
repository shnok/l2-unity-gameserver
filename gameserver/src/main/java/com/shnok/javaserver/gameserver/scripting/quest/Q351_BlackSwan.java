package com.shnok.javaserver.gameserver.scripting.quest;

import net.sf.l2j.commons.random.Rnd;

import net.sf.l2j.gameserver.enums.QuestStatus;
import net.sf.l2j.gameserver.model.actor.Creature;
import net.sf.l2j.gameserver.model.actor.Npc;
import net.sf.l2j.gameserver.model.actor.Player;
import net.sf.l2j.gameserver.scripting.Quest;
import net.sf.l2j.gameserver.scripting.QuestState;

public class Q351_BlackSwan extends Quest
{
	private static final String QUEST_NAME = "Q351_BlackSwan";
	
	// NPCs
	private static final int GOSTA = 30916;
	private static final int IASON_HEINE = 30969;
	private static final int ROMAN = 30897;
	
	// Items
	private static final int ORDER_OF_GOSTA = 4296;
	private static final int LIZARD_FANG = 4297;
	private static final int BARREL_OF_LEAGUE = 4298;
	private static final int BILL_OF_IASON_HEINE = 4310;
	
	public Q351_BlackSwan()
	{
		super(351, "Black Swan");
		
		setItemsIds(ORDER_OF_GOSTA, BARREL_OF_LEAGUE, LIZARD_FANG);
		
		addQuestStart(GOSTA);
		addTalkId(GOSTA, IASON_HEINE, ROMAN);
		
		addMyDying(20784, 20785, 21639, 21640);
	}
	
	@Override
	public String onAdvEvent(String event, Npc npc, Player player)
	{
		String htmltext = event;
		QuestState st = player.getQuestList().getQuestState(QUEST_NAME);
		if (st == null)
			return htmltext;
		
		if (event.equalsIgnoreCase("30916-03.htm"))
		{
			st.setState(QuestStatus.STARTED);
			st.setCond(1);
			playSound(player, SOUND_ACCEPT);
			giveItems(player, ORDER_OF_GOSTA, 1);
		}
		else if (event.equalsIgnoreCase("30969-02a.htm"))
		{
			final int lizardFangs = player.getInventory().getItemCount(LIZARD_FANG);
			if (lizardFangs > 0)
			{
				htmltext = "30969-02.htm";
				
				takeItems(player, LIZARD_FANG, -1);
				rewardItems(player, 57, lizardFangs * 20 + ((lizardFangs >= 10) ? 3880 : 0));
			}
		}
		else if (event.equalsIgnoreCase("30969-03a.htm"))
		{
			final int barrels = player.getInventory().getItemCount(BARREL_OF_LEAGUE);
			if (barrels > 0)
			{
				htmltext = "30969-03.htm";
				
				takeItems(player, BARREL_OF_LEAGUE, -1);
				rewardItems(player, BILL_OF_IASON_HEINE, barrels);
				rewardItems(player, 57, barrels * 20 + ((barrels >= 10) ? 3880 : 0));
				
				// Heine explains than player can speak with Roman in order to exchange bills for rewards.
				if (st.getCond() == 1)
				{
					st.setCond(2);
					playSound(player, SOUND_MIDDLE);
				}
			}
		}
		else if (event.equalsIgnoreCase("30969-06.htm"))
		{
			// If no more quest items finish the quest for real, else send a "Return" type HTM.
			if (!player.getInventory().hasItems(BARREL_OF_LEAGUE, LIZARD_FANG))
			{
				htmltext = "30969-07.htm";
				playSound(player, SOUND_FINISH);
				st.exitQuest(true);
			}
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
				htmltext = (player.getStatus().getLevel() < 32) ? "30916-00.htm" : "30916-01.htm";
				break;
			
			case STARTED:
				switch (npc.getNpcId())
				{
					case GOSTA:
						htmltext = "30916-04.htm";
						break;
					
					case IASON_HEINE:
						htmltext = "30969-01.htm";
						break;
					
					case ROMAN:
						htmltext = (player.getInventory().hasItem(BILL_OF_IASON_HEINE)) ? "30897-01.htm" : "30897-02.htm";
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
		
		final int random = Rnd.get(4);
		if (random < 3)
		{
			dropItemsAlways(player, LIZARD_FANG, (random < 2) ? 1 : 2, 0);
			dropItems(player, BARREL_OF_LEAGUE, 1, 0, 50000);
		}
		else
			dropItems(player, BARREL_OF_LEAGUE, 1, 0, (npc.getNpcId() > 20785) ? 30000 : 40000);
	}
}