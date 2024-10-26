package com.shnok.javaserver.gameserver.scripting.quest;

import net.sf.l2j.gameserver.enums.QuestStatus;
import net.sf.l2j.gameserver.model.actor.Creature;
import net.sf.l2j.gameserver.model.actor.Npc;
import net.sf.l2j.gameserver.model.actor.Player;
import net.sf.l2j.gameserver.network.serverpackets.MagicSkillUse;
import net.sf.l2j.gameserver.scripting.Quest;
import net.sf.l2j.gameserver.scripting.QuestState;

public class Q641_AttackSailren extends Quest
{
	private static final String QUEST_NAME = "Q641_AttackSailren";
	
	// NPCs
	private static final int STATUE = 32109;
	
	// Quest Item
	private static final int GAZKH_FRAGMENT = 8782;
	private static final int GAZKH = 8784;
	
	public Q641_AttackSailren()
	{
		super(641, "Attack Sailren!");
		
		setItemsIds(GAZKH_FRAGMENT);
		
		addQuestStart(STATUE);
		addTalkId(STATUE);
		
		addMyDying(22196, 22197, 22198, 22199, 22218, 22223);
	}
	
	@Override
	public String onAdvEvent(String event, Npc npc, Player player)
	{
		String htmltext = event;
		QuestState st = player.getQuestList().getQuestState(QUEST_NAME);
		if (st == null)
			return null;
		
		if (event.equalsIgnoreCase("32109-5.htm"))
		{
			st.setState(QuestStatus.STARTED);
			st.setCond(1);
			playSound(player, SOUND_ACCEPT);
		}
		else if (event.equalsIgnoreCase("32109-8.htm"))
		{
			if (player.getInventory().getItemCount(GAZKH_FRAGMENT) >= 30)
			{
				npc.broadcastPacket(new MagicSkillUse(npc, player, 5089, 1, 3000, 0));
				takeItems(player, GAZKH_FRAGMENT, -1);
				giveItems(player, GAZKH, 1);
				playSound(player, SOUND_FINISH);
				st.exitQuest(true);
			}
			else
			{
				htmltext = "32109-6.htm";
				st.setCond(1);
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
				if (player.getStatus().getLevel() < 77)
					htmltext = "32109-3.htm";
				else
				{
					QuestState st2 = player.getQuestList().getQuestState(Q126_TheNameOfEvil_2.QUEST_NAME);
					htmltext = (st2 != null && st2.isCompleted()) ? "32109-1.htm" : "32109-2.htm";
				}
				break;
			
			case STARTED:
				final int cond = st.getCond();
				if (cond == 1)
					htmltext = "32109-5.htm";
				else if (cond == 2)
					htmltext = "32109-7.htm";
				break;
		}
		
		return htmltext;
	}
	
	@Override
	public void onMyDying(Npc npc, Creature killer)
	{
		final Player player = killer.getActingPlayer();
		
		final QuestState st = getRandomPartyMember(player, npc, 1);
		if (st == null)
			return;
		
		if (dropItems(st.getPlayer(), GAZKH_FRAGMENT, 1, 30, 50000))
			st.setCond(2);
	}
}