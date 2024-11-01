package com.shnok.javaserver.gameserver.scripting.quest;

import java.util.HashMap;
import java.util.Map;

import com.shnok.javaserver.commons.lang.StringUtil;
import com.shnok.javaserver.commons.math.MathUtil;
import com.shnok.javaserver.commons.random.Rnd;

import com.shnok.javaserver.gameserver.enums.QuestStatus;
import com.shnok.javaserver.gameserver.model.actor.Creature;
import com.shnok.javaserver.gameserver.model.actor.Npc;
import com.shnok.javaserver.gameserver.model.actor.Player;
import com.shnok.javaserver.gameserver.model.holder.IntIntHolder;
import com.shnok.javaserver.gameserver.scripting.Quest;
import com.shnok.javaserver.gameserver.scripting.QuestState;

public class Q384_WarehouseKeepersPastime extends Quest
{
	private static final String QUEST_NAME = "Q384_WarehouseKeepersPastime";
	
	// NPCs
	private static final int CLIFF = 30182;
	private static final int BAXT = 30685;
	
	// Items
	private static final int MEDAL = 5964;
	
	private static final Map<Integer, Integer> CHANCES = HashMap.newHashMap(26);
	
	private static final IntIntHolder[] REWARDS_10_WIN =
	{
		new IntIntHolder(16, 1888), // Synthetic Cokes
		new IntIntHolder(32, 1887), // Varnish of Purity
		new IntIntHolder(50, 1894), // Crafted Leather
		new IntIntHolder(80, 952), // Scroll: Enchant Armor (C)
		new IntIntHolder(89, 1890), // Mithril Alloy
		new IntIntHolder(98, 1893), // Oriharukon
		new IntIntHolder(100, 951)
		// Scroll: Enchant Weapon (C)
	};
	
	private static final IntIntHolder[] REWARDS_10_LOSE =
	{
		new IntIntHolder(50, 4041), // Mold Hardener
		new IntIntHolder(80, 952), // Scroll: Enchant Armor (C)
		new IntIntHolder(98, 1892), // Blacksmith's Frame
		new IntIntHolder(100, 917)
		// Necklace of Mermaid
	};
	
	private static final IntIntHolder[] REWARDS_100_WIN =
	{
		new IntIntHolder(50, 883), // Aquastone Ring
		new IntIntHolder(80, 951), // Scroll: Enchant Weapon (C)
		new IntIntHolder(98, 852), // Moonstone Earring
		new IntIntHolder(100, 401)
		// Drake Leather Armor
	};
	
	private static final IntIntHolder[] REWARDS_100_LOSE =
	{
		new IntIntHolder(50, 951), // Scroll: Enchant Weapon (C)
		new IntIntHolder(80, 500), // Great Helmet
		new IntIntHolder(98, 2437), // Drake Leather Boots
		new IntIntHolder(100, 135)
		// Samurai Longsword
	};
	
	public Q384_WarehouseKeepersPastime()
	{
		super(384, "Warehouse Keeper's Pastime");
		
		CHANCES.put(20947, 160000); // Connabi
		CHANCES.put(20948, 180000); // Bartal
		CHANCES.put(20945, 120000); // Cadeine
		CHANCES.put(20946, 150000); // Sanhidro
		CHANCES.put(20635, 150000); // Carinkain
		CHANCES.put(20773, 610000); // Conjurer Bat Lord
		CHANCES.put(20774, 600000); // Conjurer Bat
		CHANCES.put(20760, 240000); // Dragon Bearer Archer
		CHANCES.put(20758, 240000); // Dragon Bearer Chief
		CHANCES.put(20759, 230000); // Dragon Bearer Warrior
		CHANCES.put(20242, 220000); // Dustwind Gargoyle
		CHANCES.put(20281, 220000); // Dustwind Gargoyle (2)
		CHANCES.put(20556, 140000); // Giant Monstereye
		CHANCES.put(20668, 210000); // Grave Guard
		CHANCES.put(20241, 220000); // Hunter Gargoyle
		CHANCES.put(20286, 220000); // Hunter Gargoyle (2)
		CHANCES.put(20949, 190000); // Luminun
		CHANCES.put(20950, 200000); // Innersen
		CHANCES.put(20942, 90000); // Nightmare Guide
		CHANCES.put(20943, 120000); // Nightmare Keeper
		CHANCES.put(20944, 110000); // Nightmare Lord
		CHANCES.put(20559, 140000); // Rotting Golem
		CHANCES.put(20243, 210000); // Thunder Wyrm
		CHANCES.put(20282, 210000); // Thunder Wyrm (2)
		CHANCES.put(20677, 340000); // Tulben
		CHANCES.put(20605, 150000); // Weird Drake
		
		addQuestStart(CLIFF);
		addTalkId(CLIFF, BAXT);
		
		for (int npcId : CHANCES.keySet())
			addMyDying(npcId);
	}
	
	@Override
	public String onAdvEvent(String event, Npc npc, Player player)
	{
		String htmltext = event;
		QuestState st = player.getQuestList().getQuestState(QUEST_NAME);
		if (st == null)
			return htmltext;
		
		final int npcId = npc.getNpcId();
		if (event.equalsIgnoreCase("30182-05.htm"))
		{
			st.setState(QuestStatus.STARTED);
			st.setCond(1);
			playSound(player, SOUND_ACCEPT);
		}
		else if (event.equalsIgnoreCase(npcId + "-08.htm"))
		{
			playSound(player, SOUND_GIVEUP);
			st.exitQuest(true);
		}
		else if (event.equalsIgnoreCase(npcId + "-11.htm"))
		{
			if (player.getInventory().getItemCount(MEDAL) < 10)
				htmltext = npcId + "-12.htm";
			else
			{
				st.set("bet", 10);
				st.set("board", StringUtil.scrambleString("123456789"));
				takeItems(player, MEDAL, 10);
			}
		}
		else if (event.equalsIgnoreCase(npcId + "-13.htm"))
		{
			if (player.getInventory().getItemCount(MEDAL) < 100)
				htmltext = npcId + "-12.htm";
			else
			{
				st.set("bet", 100);
				st.set("board", StringUtil.scrambleString("123456789"));
				takeItems(player, MEDAL, 100);
			}
		}
		else if (event.startsWith("select_1-")) // first pick
		{
			// Register the first char.
			st.set("playerArray", event.substring(9));
			
			// Send back the finalized HTM with dynamic content.
			htmltext = fillBoard(st, getHtmlText(npcId + "-14.htm"));
		}
		else if (event.startsWith("select_2-")) // pick #2-5
		{
			// Stores the current event for future use.
			String number = event.substring(9);
			
			// Restore the player array.
			String playerArray = st.get("playerArray");
			
			// Verify if the given number is already on the player array, if yes, it's invalid, otherwise register it.
			if (playerArray.contains(number))
				htmltext = fillBoard(st, getHtmlText(npcId + "-" + (14 + 2 * playerArray.length()) + ".htm"));
			else
			{
				// Stores the final String.
				st.set("playerArray", playerArray.concat(number));
				
				htmltext = fillBoard(st, getHtmlText(npcId + "-" + (13 + 2 * playerArray.length()) + ".htm"));
			}
		}
		else if (event.startsWith("select_3-")) // pick #6
		{
			// Stores the current event for future use.
			String number = event.substring(9);
			
			// Restore the player array.
			String playerArray = st.get("playerArray");
			
			// Verify if the given number is already on the player array, if yes, it's invalid, otherwise calculate reward.
			if (playerArray.contains(number))
				htmltext = fillBoard(st, getHtmlText(npcId + "-26.htm"));
			else
			{
				// No need to store the String on player db, but still need to update it.
				final String playerChoice = playerArray.concat(number);
				
				// Transform the generated board (9 string length) into a 2d matrice (3x3 int).
				final String[] board = st.get("board").split("");
				
				// test for all line combination
				int winningLines = 0;
				
				for (int[] map : MathUtil.MATRICE_3X3_LINES)
				{
					// test line combination
					boolean won = true;
					for (int index : map)
						won &= playerChoice.contains(board[index - 1]);
					
					// cut the loop, when you won
					if (won)
						winningLines++;
				}
				
				switch (winningLines)
				{
					case 3:
						htmltext = getHtmlText(npcId + "-23.htm");
						
						int chance = Rnd.get(100);
						for (IntIntHolder reward : ((st.getInteger("bet") == 10) ? REWARDS_10_WIN : REWARDS_100_WIN))
						{
							if (chance < reward.getId())
							{
								giveItems(player, reward.getValue(), 1);
								if (reward.getValue() == 2437)
									giveItems(player, 2463, 1);
								
								break;
							}
						}
						break;
					
					case 0:
						htmltext = getHtmlText(npcId + "-25.htm");
						
						chance = Rnd.get(100);
						for (IntIntHolder reward : ((st.getInteger("bet") == 10) ? REWARDS_10_LOSE : REWARDS_100_LOSE))
						{
							if (chance < reward.getId())
							{
								giveItems(player, reward.getValue(), 1);
								break;
							}
						}
						break;
					
					default:
						htmltext = getHtmlText(npcId + "-24.htm");
						break;
				}
				
				for (int i = 1; i < 10; i++)
				{
					htmltext = htmltext.replace("<?Cell" + i + "?>", board[i - 1]);
					htmltext = htmltext.replace("<?FontColor" + i + "?>", (playerChoice.contains(board[i - 1])) ? "ff0000" : "ffffff");
				}
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
				htmltext = (player.getStatus().getLevel() < 40) ? "30182-04.htm" : "30182-01.htm";
				break;
			
			case STARTED:
				switch (npc.getNpcId())
				{
					case CLIFF:
						htmltext = (player.getInventory().getItemCount(MEDAL) < 10) ? "30182-06.htm" : "30182-07.htm";
						break;
					
					case BAXT:
						htmltext = (player.getInventory().getItemCount(MEDAL) < 10) ? "30685-01.htm" : "30685-02.htm";
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
		
		dropItems(st.getPlayer(), MEDAL, 1, 0, CHANCES.get(npc.getNpcId()));
	}
	
	private static final String fillBoard(QuestState st, String htmltext)
	{
		final String playerArray = st.get("playerArray");
		final String[] board = st.get("board").split("");
		
		for (int i = 1; i < 10; i++)
			htmltext = htmltext.replace("<?Cell" + i + "?>", (playerArray.contains(board[i - 1])) ? board[i - 1] : "?");
		
		return htmltext;
	}
}