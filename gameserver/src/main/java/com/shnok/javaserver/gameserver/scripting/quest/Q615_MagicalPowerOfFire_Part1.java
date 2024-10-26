package com.shnok.javaserver.gameserver.scripting.quest;

import com.shnok.javaserver.gameserver.enums.QuestStatus;
import com.shnok.javaserver.gameserver.model.actor.Creature;
import com.shnok.javaserver.gameserver.model.actor.Npc;
import com.shnok.javaserver.gameserver.model.actor.Player;
import com.shnok.javaserver.gameserver.network.NpcStringId;
import com.shnok.javaserver.gameserver.scripting.Quest;
import com.shnok.javaserver.gameserver.scripting.QuestState;

public class Q615_MagicalPowerOfFire_Part1 extends Quest
{
	private static final String QUEST_NAME = "Q615_MagicalPowerOfFire_Part1";
	
	// NPCs
	private static final int NARAN = 31378;
	private static final int UDAN = 31379;
	private static final int ASEFA_BOX = 31559;
	private static final int ASEFA_EYE = 31685;
	
	// Items
	private static final int THIEF_KEY = 1661;
	private static final int STOLEN_RED_TOTEM = 7242;
	private static final int RED_TOTEM = 7243;
	private static final int DIVINE_STONE = 7081;
	
	public Q615_MagicalPowerOfFire_Part1()
	{
		super(615, "Magical Power of Fire - Part 1");
		
		setItemsIds(STOLEN_RED_TOTEM);
		
		addQuestStart(NARAN);
		addTalkId(NARAN, UDAN, ASEFA_BOX);
		
		addMyDying(ASEFA_EYE);
		addSeeCreature(21350, 21351, 21353, 21354, 21355, 21357, 21358, 21360, 21361, 21362, 21369, 21370, 21364, 21365, 21366, 21368, 21371, 21372, 21373, 21374, 21375);
	}
	
	@Override
	public String onAdvEvent(String event, Npc npc, Player player)
	{
		String htmltext = event;
		QuestState st = player.getQuestList().getQuestState(QUEST_NAME);
		if (st == null)
			return htmltext;
		
		if (event.equalsIgnoreCase("31378-03.htm"))
		{
			st.setState(QuestStatus.STARTED);
			st.setCond(1);
			st.set("spawned", 0);
			playSound(player, SOUND_ACCEPT);
		}
		else if (event.equalsIgnoreCase("31559-03.htm"))
		{
			// You have been discovered ; quest is failed.
			if (st.getInteger("spawned") == 1)
				htmltext = "31559-04.htm";
			// No Thief's Key in inventory.
			else if (!player.getInventory().hasItem(THIEF_KEY))
				htmltext = "31559-02.htm";
			else
			{
				st.setCond(3);
				playSound(player, SOUND_ITEMGET);
				takeItems(player, THIEF_KEY, 1);
				giveItems(player, STOLEN_RED_TOTEM, 1);
			}
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
				htmltext = (player.getStatus().getLevel() >= 74 && player.getAllianceWithVarkaKetra() <= -2) ? "31378-01.htm" : "31378-02.htm";
				break;
			
			case STARTED:
				final int cond = st.getCond();
				switch (npc.getNpcId())
				{
					case NARAN:
						htmltext = "31378-04.htm";
						break;
					
					case UDAN:
						if (cond == 1)
						{
							htmltext = "31379-01.htm";
							st.setCond(2);
							playSound(player, SOUND_MIDDLE);
						}
						else if (cond == 2)
						{
							if (st.getInteger("spawned") == 0)
								htmltext = "31379-02.htm";
							else
							{
								htmltext = "31379-03.htm";
								st.set("spawned", 0);
								playSound(player, SOUND_MIDDLE);
							}
						}
						else if (cond == 3 && player.getInventory().hasItem(STOLEN_RED_TOTEM))
						{
							htmltext = "31379-04.htm";
							
							takeItems(player, STOLEN_RED_TOTEM, 1);
							giveItems(player, RED_TOTEM, 1);
							giveItems(player, DIVINE_STONE, 1);
							
							st.unset("spawned");
							playSound(player, SOUND_FINISH);
							st.exitQuest(true);
						}
						break;
					
					case ASEFA_BOX:
						if (cond == 2)
							htmltext = "31559-01.htm";
						else if (cond == 3)
							htmltext = "31559-05.htm";
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
		
		final QuestState st = getRandomPartyMemberState(player, npc, QuestStatus.STARTED);
		if (st == null)
			return;
		
		npc.broadcastNpcSay(NpcStringId.ID_61504);
	}
	
	@Override
	public void onSeeCreature(Npc npc, Creature creature)
	{
		if (creature instanceof Player player)
		{
			final QuestState st = player.getQuestList().getQuestState(QUEST_NAME);
			if (st == null)
				return;
			
			if (st.getInteger("spawned") == 0 && st.getCond() == 2)
			{
				// Put "spawned" flag to 1 to avoid to spawn another.
				st.set("spawned", 1);
				
				// Spawn Asefa's Eye.
				final Npc asefaEye = addSpawn(ASEFA_EYE, player, true, 10000, true);
				if (asefaEye != null)
				{
					asefaEye.broadcastNpcSay(NpcStringId.ID_61503);
					playSound(player, SOUND_GIVEUP);
				}
			}
		}
	}
}