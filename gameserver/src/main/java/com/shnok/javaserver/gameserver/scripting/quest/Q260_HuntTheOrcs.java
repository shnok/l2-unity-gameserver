package com.shnok.javaserver.gameserver.scripting.quest;

import net.sf.l2j.gameserver.enums.QuestStatus;
import net.sf.l2j.gameserver.enums.actors.ClassRace;
import net.sf.l2j.gameserver.model.actor.Creature;
import net.sf.l2j.gameserver.model.actor.Npc;
import net.sf.l2j.gameserver.model.actor.Player;
import net.sf.l2j.gameserver.scripting.Quest;
import net.sf.l2j.gameserver.scripting.QuestState;

public class Q260_HuntTheOrcs extends Quest
{
	private static final String QUEST_NAME = "Q260_HuntTheOrcs";
	
	// NPC
	private static final int RAYEN = 30221;
	
	// Items
	private static final int ORC_AMULET = 1114;
	private static final int ORC_NECKLACE = 1115;
	
	// Monsters
	private static final int KABOO_ORC = 20468;
	private static final int KABOO_ORC_ARCHER = 20469;
	private static final int KABOO_ORC_GRUNT = 20470;
	private static final int KABOO_ORC_FIGHTER = 20471;
	private static final int KABOO_ORC_FIGHTER_LEADER = 20472;
	private static final int KABOO_ORC_FIGHTER_LIEUTENANT = 20473;
	
	public Q260_HuntTheOrcs()
	{
		super(260, "Hunt the Orcs");
		
		setItemsIds(ORC_AMULET, ORC_NECKLACE);
		
		addQuestStart(RAYEN);
		addTalkId(RAYEN);
		
		addMyDying(KABOO_ORC, KABOO_ORC_ARCHER, KABOO_ORC_GRUNT, KABOO_ORC_FIGHTER, KABOO_ORC_FIGHTER_LEADER, KABOO_ORC_FIGHTER_LIEUTENANT);
	}
	
	@Override
	public String onAdvEvent(String event, Npc npc, Player player)
	{
		String htmltext = event;
		QuestState st = player.getQuestList().getQuestState(QUEST_NAME);
		if (st == null)
			return htmltext;
		
		if (event.equalsIgnoreCase("30221-03.htm"))
		{
			st.setState(QuestStatus.STARTED);
			st.setCond(1);
			playSound(player, SOUND_ACCEPT);
		}
		else if (event.equalsIgnoreCase("30221-06.htm"))
		{
			playSound(player, SOUND_FINISH);
			st.exitQuest(true);
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
				if (player.getRace() != ClassRace.ELF)
					htmltext = "30221-00.htm";
				else if (player.getStatus().getLevel() < 6)
					htmltext = "30221-01.htm";
				else
					htmltext = "30221-02.htm";
				break;
			
			case STARTED:
				final int amulets = player.getInventory().getItemCount(ORC_AMULET);
				final int necklaces = player.getInventory().getItemCount(ORC_NECKLACE);
				
				if (amulets == 0 && necklaces == 0)
					htmltext = "30221-04.htm";
				else
				{
					htmltext = "30221-05.htm";
					takeItems(player, ORC_AMULET, -1);
					takeItems(player, ORC_NECKLACE, -1);
					
					int reward = (amulets * 12) + (necklaces * 30);
					if (amulets + necklaces >= 10)
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
			case KABOO_ORC, KABOO_ORC_GRUNT, KABOO_ORC_ARCHER:
				dropItems(player, ORC_AMULET, 1, 0, 500000);
				break;
			
			case KABOO_ORC_FIGHTER, KABOO_ORC_FIGHTER_LEADER, KABOO_ORC_FIGHTER_LIEUTENANT:
				dropItems(player, ORC_NECKLACE, 1, 0, 500000);
				break;
		}
	}
}