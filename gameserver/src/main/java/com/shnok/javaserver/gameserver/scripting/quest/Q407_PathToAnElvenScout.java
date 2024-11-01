package com.shnok.javaserver.gameserver.scripting.quest;

import com.shnok.javaserver.gameserver.enums.QuestStatus;
import com.shnok.javaserver.gameserver.enums.actors.ClassId;
import com.shnok.javaserver.gameserver.model.actor.Creature;
import com.shnok.javaserver.gameserver.model.actor.Npc;
import com.shnok.javaserver.gameserver.model.actor.Player;
import com.shnok.javaserver.gameserver.network.serverpackets.SocialAction;
import com.shnok.javaserver.gameserver.scripting.Quest;
import com.shnok.javaserver.gameserver.scripting.QuestState;

public class Q407_PathToAnElvenScout extends Quest
{
	private static final String QUEST_NAME = "Q407_PathToAnElvenScout";
	
	// Items
	private static final int REISA_LETTER = 1207;
	private static final int PRIAS_TORN_LETTER_1 = 1208;
	private static final int PRIAS_TORN_LETTER_2 = 1209;
	private static final int PRIAS_TORN_LETTER_3 = 1210;
	private static final int PRIAS_TORN_LETTER_4 = 1211;
	private static final int MORETTI_HERB = 1212;
	private static final int MORETTI_LETTER = 1214;
	private static final int PRIAS_LETTER = 1215;
	private static final int HONORARY_GUARD = 1216;
	private static final int REISA_RECOMMENDATION = 1217;
	private static final int RUSTED_KEY = 1293;
	
	// NPCs
	private static final int REISA = 30328;
	private static final int BABENCO = 30334;
	private static final int MORETTI = 30337;
	private static final int PRIAS = 30426;
	
	public Q407_PathToAnElvenScout()
	{
		super(407, "Path to an Elven Scout");
		
		setItemsIds(REISA_LETTER, PRIAS_TORN_LETTER_1, PRIAS_TORN_LETTER_2, PRIAS_TORN_LETTER_3, PRIAS_TORN_LETTER_4, MORETTI_HERB, MORETTI_LETTER, PRIAS_LETTER, HONORARY_GUARD, RUSTED_KEY);
		
		addQuestStart(REISA);
		addTalkId(REISA, MORETTI, BABENCO, PRIAS);
		
		addMyDying(20053, 27031);
	}
	
	@Override
	public String onAdvEvent(String event, Npc npc, Player player)
	{
		String htmltext = event;
		QuestState st = player.getQuestList().getQuestState(QUEST_NAME);
		if (st == null)
			return htmltext;
		
		if (event.equalsIgnoreCase("30328-05.htm"))
		{
			if (player.getClassId() != ClassId.ELVEN_FIGHTER)
				htmltext = (player.getClassId() == ClassId.ELVEN_SCOUT) ? "30328-02a.htm" : "30328-02.htm";
			else if (player.getStatus().getLevel() < 19)
				htmltext = "30328-03.htm";
			else if (player.getInventory().hasItem(REISA_RECOMMENDATION))
				htmltext = "30328-04.htm";
			else
			{
				st.setState(QuestStatus.STARTED);
				st.setCond(1);
				playSound(player, SOUND_ACCEPT);
				giveItems(player, REISA_LETTER, 1);
			}
		}
		else if (event.equalsIgnoreCase("30337-03.htm"))
		{
			st.setCond(2);
			playSound(player, SOUND_MIDDLE);
			takeItems(player, REISA_LETTER, -1);
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
				htmltext = "30328-01.htm";
				break;
			
			case STARTED:
				final int cond = st.getCond();
				switch (npc.getNpcId())
				{
					case REISA:
						if (cond == 1)
							htmltext = "30328-06.htm";
						else if (cond > 1 && cond < 8)
							htmltext = "30328-08.htm";
						else if (cond == 8)
						{
							htmltext = "30328-07.htm";
							takeItems(player, HONORARY_GUARD, -1);
							giveItems(player, REISA_RECOMMENDATION, 1);
							rewardExpAndSp(player, 3200, 1000);
							player.broadcastPacket(new SocialAction(player, 3));
							playSound(player, SOUND_FINISH);
							st.exitQuest(true);
						}
						break;
					
					case MORETTI:
						if (cond == 1)
							htmltext = "30337-01.htm";
						else if (cond == 2)
							htmltext = (!player.getInventory().hasItem(PRIAS_TORN_LETTER_1)) ? "30337-04.htm" : "30337-05.htm";
						else if (cond == 3)
						{
							htmltext = "30337-06.htm";
							st.setCond(4);
							playSound(player, SOUND_MIDDLE);
							takeItems(player, PRIAS_TORN_LETTER_1, -1);
							takeItems(player, PRIAS_TORN_LETTER_2, -1);
							takeItems(player, PRIAS_TORN_LETTER_3, -1);
							takeItems(player, PRIAS_TORN_LETTER_4, -1);
							giveItems(player, MORETTI_HERB, 1);
							giveItems(player, MORETTI_LETTER, 1);
						}
						else if (cond > 3 && cond < 7)
							htmltext = "30337-09.htm";
						else if (cond == 7 && player.getInventory().hasItem(PRIAS_LETTER))
						{
							htmltext = "30337-07.htm";
							st.setCond(8);
							playSound(player, SOUND_MIDDLE);
							takeItems(player, PRIAS_LETTER, -1);
							giveItems(player, HONORARY_GUARD, 1);
						}
						else if (cond == 8)
							htmltext = "30337-08.htm";
						break;
					
					case BABENCO:
						if (cond == 2)
							htmltext = "30334-01.htm";
						break;
					
					case PRIAS:
						if (cond == 4)
						{
							htmltext = "30426-01.htm";
							st.setCond(5);
							playSound(player, SOUND_MIDDLE);
						}
						else if (cond == 5)
							htmltext = "30426-01.htm";
						else if (cond == 6)
						{
							htmltext = "30426-02.htm";
							st.setCond(7);
							playSound(player, SOUND_MIDDLE);
							takeItems(player, RUSTED_KEY, -1);
							takeItems(player, MORETTI_HERB, -1);
							takeItems(player, MORETTI_LETTER, -1);
							giveItems(player, PRIAS_LETTER, 1);
						}
						else if (cond == 7)
							htmltext = "30426-04.htm";
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
		
		final int cond = st.getCond();
		if (npc.getNpcId() == 20053)
		{
			if (cond == 2)
			{
				if (!player.getInventory().hasItem(PRIAS_TORN_LETTER_1))
				{
					playSound(player, SOUND_ITEMGET);
					giveItems(player, PRIAS_TORN_LETTER_1, 1);
				}
				else if (!player.getInventory().hasItem(PRIAS_TORN_LETTER_2))
				{
					playSound(player, SOUND_ITEMGET);
					giveItems(player, PRIAS_TORN_LETTER_2, 1);
				}
				else if (!player.getInventory().hasItem(PRIAS_TORN_LETTER_3))
				{
					playSound(player, SOUND_ITEMGET);
					giveItems(player, PRIAS_TORN_LETTER_3, 1);
				}
				else if (!player.getInventory().hasItem(PRIAS_TORN_LETTER_4))
				{
					st.setCond(3);
					playSound(player, SOUND_MIDDLE);
					giveItems(player, PRIAS_TORN_LETTER_4, 1);
				}
			}
		}
		else if ((cond == 4 || cond == 5) && dropItems(player, RUSTED_KEY, 1, 1, 600000))
			st.setCond(6);
	}
}