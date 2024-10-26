package com.shnok.javaserver.gameserver.scripting.quest;

import net.sf.l2j.gameserver.enums.QuestStatus;
import net.sf.l2j.gameserver.model.actor.Creature;
import net.sf.l2j.gameserver.model.actor.Npc;
import net.sf.l2j.gameserver.model.actor.Player;
import net.sf.l2j.gameserver.scripting.Quest;
import net.sf.l2j.gameserver.scripting.QuestState;

public class Q374_WhisperOfDreams_Part1 extends Quest
{
	private static final String QUEST_NAME = "Q374_WhisperOfDreams_Part1";
	
	// NPCs
	private static final int MANAKIA = 30515;
	private static final int TORAI = 30557;
	
	// Monsters
	private static final int CAVE_BEAST = 20620;
	private static final int DEATH_WAVE = 20621;
	
	// Items
	private static final int CAVE_BEAST_TOOTH = 5884;
	private static final int DEATH_WAVE_LIGHT = 5885;
	private static final int SEALED_MYSTERIOUS_STONE = 5886;
	private static final int MYSTERIOUS_STONE = 5887;
	
	// Rewards
	private static final int[][] REWARDS =
	{
		{
			5486,
			3,
			2950
		}, // Dark Crystal, 3x, 2950 adena
		{
			5487,
			2,
			18050
		}, // Nightmare, 2x, 18050 adena
		{
			5488,
			2,
			18050
		}, // Majestic, 2x, 18050 adena
		{
			5485,
			4,
			10450
		}, // Tallum Tunic, 4, 10450 adena
		{
			5489,
			6,
			15550
		}
		// Tallum Stockings, 6, 15550 adena
	};
	
	public Q374_WhisperOfDreams_Part1()
	{
		super(374, "Whisper of Dreams, Part 1");
		
		setItemsIds(DEATH_WAVE_LIGHT, CAVE_BEAST_TOOTH, SEALED_MYSTERIOUS_STONE, MYSTERIOUS_STONE);
		
		addQuestStart(MANAKIA);
		addTalkId(MANAKIA, TORAI);
		
		addMyDying(CAVE_BEAST, DEATH_WAVE);
	}
	
	@Override
	public String onAdvEvent(String event, Npc npc, Player player)
	{
		String htmltext = event;
		QuestState st = player.getQuestList().getQuestState(QUEST_NAME);
		if (st == null)
			return htmltext;
		
		// Manakia
		if (event.equalsIgnoreCase("30515-03.htm"))
		{
			st.setState(QuestStatus.STARTED);
			st.setCond(1);
			st.set("condStone", 1);
			playSound(player, SOUND_ACCEPT);
		}
		else if (event.startsWith("30515-06-"))
		{
			if (player.getInventory().getItemCount(CAVE_BEAST_TOOTH) >= 65 && player.getInventory().getItemCount(DEATH_WAVE_LIGHT) >= 65)
			{
				htmltext = "30515-06.htm";
				playSound(player, SOUND_MIDDLE);
				
				int[] reward = REWARDS[Integer.parseInt(event.substring(9, 10))];
				
				takeItems(player, CAVE_BEAST_TOOTH, -1);
				takeItems(player, DEATH_WAVE_LIGHT, -1);
				
				rewardItems(player, 57, reward[2]);
				giveItems(player, reward[0], reward[1]);
			}
			else
				htmltext = "30515-07.htm";
		}
		else if (event.equalsIgnoreCase("30515-08.htm"))
		{
			playSound(player, SOUND_FINISH);
			st.exitQuest(true);
		}
		// Torai
		else if (event.equalsIgnoreCase("30557-02.htm"))
		{
			if (st.getCond() == 2 && player.getInventory().hasItem(SEALED_MYSTERIOUS_STONE))
			{
				st.setCond(3);
				takeItems(player, SEALED_MYSTERIOUS_STONE, -1);
				giveItems(player, MYSTERIOUS_STONE, 1);
				playSound(player, SOUND_MIDDLE);
			}
			else
				htmltext = "30557-03.htm";
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
				htmltext = (player.getStatus().getLevel() < 56) ? "30515-01.htm" : "30515-02.htm";
				break;
			
			case STARTED:
				final int cond = st.getCond();
				switch (npc.getNpcId())
				{
					case MANAKIA:
						if (!player.getInventory().hasItem(SEALED_MYSTERIOUS_STONE))
						{
							if (player.getInventory().getItemCount(CAVE_BEAST_TOOTH) >= 65 && player.getInventory().getItemCount(DEATH_WAVE_LIGHT) >= 65)
								htmltext = "30515-05.htm";
							else
								htmltext = "30515-04.htm";
						}
						else
						{
							if (cond == 1)
							{
								htmltext = "30515-09.htm";
								st.setCond(2);
								playSound(player, SOUND_MIDDLE);
							}
							else
								htmltext = "30515-10.htm";
						}
						break;
					
					case TORAI:
						if (cond == 2 && player.getInventory().hasItem(SEALED_MYSTERIOUS_STONE))
							htmltext = "30557-01.htm";
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
		
		// Drop tooth or light to anyone.
		QuestState st = getRandomPartyMemberState(player, npc, QuestStatus.STARTED);
		if (st == null)
			return;
		
		dropItems(st.getPlayer(), (npc.getNpcId() == CAVE_BEAST) ? CAVE_BEAST_TOOTH : DEATH_WAVE_LIGHT, 1, 65, 500000);
		
		// Drop Sealed Mysterious Stone to a party member, who still need it.
		st = getRandomPartyMember(player, npc, "condStone", "1");
		if (st != null && dropItems(st.getPlayer(), SEALED_MYSTERIOUS_STONE, 1, 1, 1000))
			st.unset("condStone");
	}
}