package com.shnok.javaserver.gameserver.scripting.quest;

import net.sf.l2j.commons.random.Rnd;

import net.sf.l2j.gameserver.enums.QuestStatus;
import net.sf.l2j.gameserver.model.actor.Creature;
import net.sf.l2j.gameserver.model.actor.Npc;
import net.sf.l2j.gameserver.model.actor.Player;
import net.sf.l2j.gameserver.scripting.Quest;
import net.sf.l2j.gameserver.scripting.QuestState;
import net.sf.l2j.gameserver.skills.L2Skill;

public class Q367_ElectrifyingRecharge extends Quest
{
	private static final String QUEST_NAME = "Q367_ElectrifyingRecharge";
	
	// NPCs
	private static final int LORAIN = 30673;
	
	// Item
	private static final int INITIAL_TITAN_LAMP = 5875;
	private static final int TITAN_LAMP_1 = 5876;
	private static final int TITAN_LAMP_2 = 5877;
	private static final int TITAN_LAMP_3 = 5878;
	private static final int FINAL_TITAN_LAMP = 5879;
	private static final int BROKEN_TITAN_LAMP = 5880;
	
	// Mobs
	private static final int CATHEROK = 21035;
	
	public Q367_ElectrifyingRecharge()
	{
		super(367, "Electrifying Recharge!");
		
		setItemsIds(INITIAL_TITAN_LAMP, TITAN_LAMP_1, TITAN_LAMP_2, TITAN_LAMP_3, FINAL_TITAN_LAMP, BROKEN_TITAN_LAMP);
		
		addQuestStart(LORAIN);
		addTalkId(LORAIN);
		
		addAttacked(CATHEROK);
	}
	
	@Override
	public String onAdvEvent(String event, Npc npc, Player player)
	{
		String htmltext = event;
		QuestState st = player.getQuestList().getQuestState(QUEST_NAME);
		if (st == null)
			return htmltext;
		
		if (event.equalsIgnoreCase("30673-03.htm"))
		{
			st.setState(QuestStatus.STARTED);
			st.setCond(1);
			playSound(player, SOUND_ACCEPT);
			giveItems(player, INITIAL_TITAN_LAMP, 1);
		}
		else if (event.equalsIgnoreCase("30673-08.htm"))
		{
			playSound(player, SOUND_GIVEUP);
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
				htmltext = (player.getStatus().getLevel() < 37) ? "30673-02.htm" : "30673-01.htm";
				break;
			
			case STARTED:
				final int cond = st.getCond();
				if (cond == 1)
				{
					if (player.getInventory().hasItem(BROKEN_TITAN_LAMP))
					{
						htmltext = "30673-05.htm";
						takeItems(player, BROKEN_TITAN_LAMP, -1);
						giveItems(player, INITIAL_TITAN_LAMP, 1);
						playSound(player, SOUND_ACCEPT);
					}
					else if (player.getInventory().hasAtLeastOneItem(TITAN_LAMP_1, TITAN_LAMP_2, TITAN_LAMP_3))
						htmltext = "30673-04.htm";
					else
						htmltext = "30673-03.htm";
				}
				else if (cond == 2)
				{
					htmltext = "30673-06.htm";
					st.setCond(1);
					takeItems(player, FINAL_TITAN_LAMP, -1);
					giveItems(player, INITIAL_TITAN_LAMP, 1);
					
					// Dye reward.
					final int i0 = Rnd.get(14);
					if (i0 == 0)
						rewardItems(player, 4553, 1);
					else if (i0 == 1)
						rewardItems(player, 4554, 1);
					else if (i0 == 2)
						rewardItems(player, 4555, 1);
					else if (i0 == 3)
						rewardItems(player, 4556, 1);
					else if (i0 == 4)
						rewardItems(player, 4557, 1);
					else if (i0 == 5)
						rewardItems(player, 4558, 1);
					else if (i0 == 6)
						rewardItems(player, 4559, 1);
					else if (i0 == 7)
						rewardItems(player, 4560, 1);
					else if (i0 == 8)
						rewardItems(player, 4561, 1);
					else if (i0 == 9)
						rewardItems(player, 4562, 1);
					else if (i0 == 10)
						rewardItems(player, 4563, 1);
					else if (i0 == 11)
						rewardItems(player, 4564, 1);
					else
						rewardItems(player, 4445, 1);
				}
				break;
		}
		return htmltext;
	}
	
	@Override
	public void onAttacked(Npc npc, Creature attacker, int damage, L2Skill skill)
	{
		Player player = attacker.getActingPlayer();
		
		final QuestState st = getRandomPartyMember(player, npc, 1);
		if (st == null)
			return;
		
		// For every occurred attack, the NPC tries to cast skillId 4072. The only restrictions are inherent skill restriction (mp cost, skill reuse, etc).
		npc.getAI().addCastDesire(player, 4072, 1, 1000000);
		
		player = st.getPlayer();
		if (!player.getInventory().hasItem(FINAL_TITAN_LAMP))
		{
			final int i0 = Rnd.get(37);
			if (i0 == 0)
			{
				if (player.getInventory().hasItem(INITIAL_TITAN_LAMP))
				{
					takeItems(player, INITIAL_TITAN_LAMP, -1);
					giveItems(player, TITAN_LAMP_1, 1);
					playSound(player, SOUND_ITEMGET);
				}
				else if (player.getInventory().hasItem(TITAN_LAMP_1))
				{
					takeItems(player, TITAN_LAMP_1, -1);
					giveItems(player, TITAN_LAMP_2, 1);
					playSound(player, SOUND_ITEMGET);
				}
				else if (player.getInventory().hasItem(TITAN_LAMP_2))
				{
					takeItems(player, TITAN_LAMP_2, -1);
					giveItems(player, TITAN_LAMP_3, 1);
					playSound(player, SOUND_ITEMGET);
				}
				else if (player.getInventory().hasItem(TITAN_LAMP_3))
				{
					st.setCond(2);
					playSound(player, SOUND_MIDDLE);
					takeItems(player, TITAN_LAMP_3, -1);
					giveItems(player, FINAL_TITAN_LAMP, 1);
				}
			}
			else if (i0 == 1 && !player.getInventory().hasItem(BROKEN_TITAN_LAMP))
			{
				takeItems(player, INITIAL_TITAN_LAMP, -1);
				takeItems(player, TITAN_LAMP_1, -1);
				takeItems(player, TITAN_LAMP_2, -1);
				takeItems(player, TITAN_LAMP_3, -1);
				giveItems(player, BROKEN_TITAN_LAMP, 1);
				playSound(player, SOUND_ITEMGET);
			}
		}
	}
}