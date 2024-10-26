package com.shnok.javaserver.gameserver.scripting.quest;

import net.sf.l2j.gameserver.enums.QuestStatus;
import net.sf.l2j.gameserver.enums.actors.ClassRace;
import net.sf.l2j.gameserver.model.actor.Creature;
import net.sf.l2j.gameserver.model.actor.Npc;
import net.sf.l2j.gameserver.model.actor.Player;
import net.sf.l2j.gameserver.scripting.Quest;
import net.sf.l2j.gameserver.scripting.QuestState;

public class Q257_TheGuardIsBusy extends Quest
{
	private static final String QUEST_NAME = "Q257_TheGuardIsBusy";
	
	// Items
	private static final int GLUDIO_LORD_MARK = 1084;
	private static final int ORC_AMULET = 752;
	private static final int ORC_NECKLACE = 1085;
	private static final int WEREWOLF_FANG = 1086;
	
	public Q257_TheGuardIsBusy()
	{
		super(257, "The Guard Is Busy");
		
		setItemsIds(ORC_AMULET, ORC_NECKLACE, WEREWOLF_FANG, GLUDIO_LORD_MARK);
		
		addQuestStart(30039); // Gilbert
		addTalkId(30039);
		
		addMyDying(20006, 20093, 20096, 20098, 20130, 20131, 20132, 20342, 20343);
	}
	
	@Override
	public String onAdvEvent(String event, Npc npc, Player player)
	{
		String htmltext = event;
		QuestState st = player.getQuestList().getQuestState(QUEST_NAME);
		if (st == null)
			return htmltext;
		
		if (event.equalsIgnoreCase("30039-03.htm"))
		{
			st.setState(QuestStatus.STARTED);
			st.setCond(1);
			playSound(player, SOUND_ACCEPT);
			giveItems(player, GLUDIO_LORD_MARK, 1);
		}
		else if (event.equalsIgnoreCase("30039-05.htm"))
		{
			takeItems(player, GLUDIO_LORD_MARK, 1);
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
				if (player.getRace() != ClassRace.HUMAN)
					htmltext = "30039-00.htm";
				else if (player.getStatus().getLevel() < 6)
					htmltext = "30039-01.htm";
				else
					htmltext = "30039-02.htm";
				break;
			
			case STARTED:
				final int amulets = player.getInventory().getItemCount(ORC_AMULET);
				final int necklaces = player.getInventory().getItemCount(ORC_NECKLACE);
				final int fangs = player.getInventory().getItemCount(WEREWOLF_FANG);
				
				if (amulets + necklaces + fangs == 0)
					htmltext = "30039-04.htm";
				else
				{
					htmltext = "30039-07.htm";
					
					takeItems(player, ORC_AMULET, -1);
					takeItems(player, ORC_NECKLACE, -1);
					takeItems(player, WEREWOLF_FANG, -1);
					
					int reward = (10 * amulets) + 20 * (necklaces + fangs);
					if (amulets + necklaces + fangs >= 10)
						reward += 1000;
					
					rewardItems(player, 57, reward);
					rewardNewbieShots(player, 6000, 3000);
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
		
		switch (npc.getNpcId())
		{
			case 20006, 20130, 20131:
				dropItems(player, ORC_AMULET, 1, 0, 500000);
				break;
			
			case 20093, 20096, 20098:
				dropItems(player, ORC_NECKLACE, 1, 0, 500000);
				break;
			
			case 20342:
				dropItems(player, WEREWOLF_FANG, 1, 0, 200000);
				break;
			
			case 20343:
				dropItems(player, WEREWOLF_FANG, 1, 0, 400000);
				break;
			
			case 20132:
				dropItems(player, WEREWOLF_FANG, 1, 0, 500000);
				break;
		}
	}
}