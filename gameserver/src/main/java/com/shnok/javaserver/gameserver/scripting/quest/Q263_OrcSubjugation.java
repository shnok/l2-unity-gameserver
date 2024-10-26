package com.shnok.javaserver.gameserver.scripting.quest;

import net.sf.l2j.gameserver.enums.QuestStatus;
import net.sf.l2j.gameserver.enums.actors.ClassRace;
import net.sf.l2j.gameserver.model.actor.Creature;
import net.sf.l2j.gameserver.model.actor.Npc;
import net.sf.l2j.gameserver.model.actor.Player;
import net.sf.l2j.gameserver.scripting.Quest;
import net.sf.l2j.gameserver.scripting.QuestState;

public class Q263_OrcSubjugation extends Quest
{
	private static final String QUEST_NAME = "Q263_OrcSubjugation";
	
	// Items
	private static final int ORC_AMULET = 1116;
	private static final int ORC_NECKLACE = 1117;
	
	public Q263_OrcSubjugation()
	{
		super(263, "Orc Subjugation");
		
		setItemsIds(ORC_AMULET, ORC_NECKLACE);
		
		addQuestStart(30346); // Kayleen
		addTalkId(30346);
		
		addMyDying(20385, 20386, 20387, 20388);
	}
	
	@Override
	public String onAdvEvent(String event, Npc npc, Player player)
	{
		String htmltext = event;
		QuestState st = player.getQuestList().getQuestState(QUEST_NAME);
		if (st == null)
			return htmltext;
		
		if (event.equalsIgnoreCase("30346-03.htm"))
		{
			st.setState(QuestStatus.STARTED);
			st.setCond(1);
			playSound(player, SOUND_ACCEPT);
		}
		else if (event.equalsIgnoreCase("30346-06.htm"))
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
				if (player.getRace() != ClassRace.DARK_ELF)
					htmltext = "30346-00.htm";
				else if (player.getStatus().getLevel() < 8)
					htmltext = "30346-01.htm";
				else
					htmltext = "30346-02.htm";
				break;
			
			case STARTED:
				int amulet = player.getInventory().getItemCount(ORC_AMULET);
				int necklace = player.getInventory().getItemCount(ORC_NECKLACE);
				
				if (amulet == 0 && necklace == 0)
					htmltext = "30346-04.htm";
				else
				{
					htmltext = "30346-05.htm";
					takeItems(player, ORC_AMULET, -1);
					takeItems(player, ORC_NECKLACE, -1);
					rewardItems(player, 57, amulet * 20 + necklace * 30);
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
		
		dropItems(player, (npc.getNpcId() == 20385) ? ORC_AMULET : ORC_NECKLACE, 1, 0, 500000);
	}
}