package com.shnok.javaserver.gameserver.scripting.quest;

import net.sf.l2j.gameserver.enums.QuestStatus;
import net.sf.l2j.gameserver.enums.actors.ClassRace;
import net.sf.l2j.gameserver.model.actor.Creature;
import net.sf.l2j.gameserver.model.actor.Npc;
import net.sf.l2j.gameserver.model.actor.Player;
import net.sf.l2j.gameserver.scripting.Quest;
import net.sf.l2j.gameserver.scripting.QuestState;

public class Q003_WillTheSealBeBroken extends Quest
{
	private static final String QUEST_NAME = "Q003_WillTheSealBeBroken";
	
	// Items
	private static final int ONYX_BEAST_EYE = 1081;
	private static final int TAINT_STONE = 1082;
	private static final int SUCCUBUS_BLOOD = 1083;
	
	// Reward
	private static final int SCROLL_ENCHANT_ARMOR_D = 956;
	
	public Q003_WillTheSealBeBroken()
	{
		super(3, "Will the Seal be Broken?");
		
		setItemsIds(ONYX_BEAST_EYE, TAINT_STONE, SUCCUBUS_BLOOD);
		
		addQuestStart(30141); // Talloth
		addTalkId(30141);
		
		addMyDying(20031, 20041, 20046, 20048, 20052, 20057);
	}
	
	@Override
	public String onAdvEvent(String event, Npc npc, Player player)
	{
		String htmltext = event;
		QuestState st = player.getQuestList().getQuestState(QUEST_NAME);
		if (st == null)
			return htmltext;
		
		if (event.equalsIgnoreCase("30141-03.htm"))
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
				if (player.getRace() != ClassRace.DARK_ELF)
					htmltext = "30141-00.htm";
				else if (player.getStatus().getLevel() < 16)
					htmltext = "30141-01.htm";
				else
					htmltext = "30141-02.htm";
				break;
			
			case STARTED:
				int cond = st.getCond();
				if (cond == 1)
					htmltext = "30141-04.htm";
				else if (cond == 2)
				{
					htmltext = "30141-06.htm";
					takeItems(player, ONYX_BEAST_EYE, 1);
					takeItems(player, SUCCUBUS_BLOOD, 1);
					takeItems(player, TAINT_STONE, 1);
					giveItems(player, SCROLL_ENCHANT_ARMOR_D, 1);
					playSound(player, SOUND_FINISH);
					st.exitQuest(false);
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
		
		final QuestState st = checkPlayerCondition(player, npc, 1);
		if (st == null)
			return;
		
		switch (npc.getNpcId())
		{
			case 20031:
				if (dropItemsAlways(player, ONYX_BEAST_EYE, 1, 1) && player.getInventory().hasItems(TAINT_STONE, SUCCUBUS_BLOOD))
					st.setCond(2);
				break;
			
			case 20041, 20046:
				if (dropItemsAlways(player, TAINT_STONE, 1, 1) && player.getInventory().hasItems(ONYX_BEAST_EYE, SUCCUBUS_BLOOD))
					st.setCond(2);
				break;
			
			case 20048, 20052, 20057:
				if (dropItemsAlways(player, SUCCUBUS_BLOOD, 1, 1) && player.getInventory().hasItems(ONYX_BEAST_EYE, TAINT_STONE))
					st.setCond(2);
				break;
		}
	}
}