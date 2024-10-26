package com.shnok.javaserver.gameserver.scripting.quest;

import java.util.HashMap;
import java.util.Map;

import net.sf.l2j.commons.lang.StringUtil;
import net.sf.l2j.commons.math.MathUtil;
import net.sf.l2j.commons.random.Rnd;

import net.sf.l2j.gameserver.enums.QuestStatus;
import net.sf.l2j.gameserver.model.actor.Creature;
import net.sf.l2j.gameserver.model.actor.Npc;
import net.sf.l2j.gameserver.model.actor.Player;
import net.sf.l2j.gameserver.scripting.Quest;
import net.sf.l2j.gameserver.scripting.QuestState;

public class Q386_StolenDignity extends Quest
{
	private static final String QUEST_NAME = "Q386_StolenDignity";
	
	// NPCs
	private static final int ROMP = 30843;
	
	// Items
	private static final int STOLEN_INFERNIUM_ORE = 6363;
	
	private static final Map<Integer, Integer> CHANCES = HashMap.newHashMap(26);
	
	private static final int[] REWARDS =
	{
		5529,
		5532,
		5533,
		5534,
		5535,
		5536,
		5537,
		5538,
		5539,
		5541,
		5542,
		5543,
		5544,
		5545,
		5546,
		5547,
		5548,
		8331,
		8341,
		8342,
		8349,
		8346
	};
	
	public Q386_StolenDignity()
	{
		super(386, "Stolen Dignity");
		
		CHANCES.put(20970, 208000); // Soldier of Ancient Times
		CHANCES.put(20971, 299000); // Warrior of Ancient Times
		CHANCES.put(20958, 170000); // Death Agent
		CHANCES.put(20960, 149000); // Bloody Ghost
		CHANCES.put(20963, 199000); // Bloody Lord
		CHANCES.put(20670, 202000); // Crimson Drake
		CHANCES.put(21114, 352000); // Cursed Guardian
		CHANCES.put(20959, 273000); // Dark Guard
		CHANCES.put(21020, 478000); // Fallen Orc Shaman
		CHANCES.put(21258, 487000); // Fallen Orc Shaman (trans)
		CHANCES.put(21003, 173000); // Grave Lich
		CHANCES.put(20969, 205000); // Giant's Shadow
		CHANCES.put(21108, 245000); // Glow Wisp
		CHANCES.put(21005, 211000); // Grave Predator
		CHANCES.put(21116, 487000); // Hames Orc Overlord
		CHANCES.put(21113, 370000); // Hames Orc Sniper
		CHANCES.put(20954, 184000); // Hungered Corpse
		CHANCES.put(20671, 211000); // Kadios
		CHANCES.put(21110, 260000); // Marsh Predator
		CHANCES.put(20967, 257000); // Past Creature
		CHANCES.put(20956, 216000); // Past Knight
		CHANCES.put(21021, 234000); // Sharp Talon Tiger
		CHANCES.put(21259, 487000); // Fallen Orc Shaman
		CHANCES.put(20974, 440000); // Spiteful Soul Leader
		CHANCES.put(20975, 390000); // Spiteful Soul Wizard
		CHANCES.put(21001, 214000); // Wretched Archer
		
		addQuestStart(ROMP);
		addTalkId(ROMP);
		
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
		
		if (event.equalsIgnoreCase("30843-05.htm"))
		{
			st.setState(QuestStatus.STARTED);
			st.setCond(1);
			playSound(player, SOUND_ACCEPT);
		}
		else if (event.equalsIgnoreCase("30843-08.htm"))
		{
			playSound(player, SOUND_GIVEUP);
			st.exitQuest(true);
		}
		else if (event.equalsIgnoreCase("30843-12.htm"))
		{
			if (player.getInventory().getItemCount(STOLEN_INFERNIUM_ORE) < 100)
				htmltext = "30843-11.htm";
			else
			{
				st.set("board", StringUtil.scrambleString("123456789"));
				takeItems(player, STOLEN_INFERNIUM_ORE, 100);
			}
		}
		else if (event.startsWith("select_1-")) // first pick
		{
			// Register the first char.
			st.set("playerArray", event.substring(9));
			
			// Send back the finalized HTM with dynamic content.
			htmltext = fillBoard(st, getHtmlText("30843-13.htm"));
		}
		else if (event.startsWith("select_2-")) // pick #2-5
		{
			// Stores the current event for future use.
			String number = event.substring(9);
			
			// Restore the player array.
			String playerArray = st.get("playerArray");
			
			// Verify if the given number is already on the player array, if yes, it's invalid, otherwise register it.
			if (playerArray.contains(number))
				htmltext = fillBoard(st, getHtmlText("30843-" + (13 + 2 * playerArray.length()) + ".htm"));
			else
			{
				// Stores the final String.
				st.set("playerArray", playerArray.concat(number));
				
				htmltext = fillBoard(st, getHtmlText("30843-" + (12 + 2 * playerArray.length()) + ".htm"));
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
				htmltext = fillBoard(st, getHtmlText("30843-25.htm"));
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
						htmltext = getHtmlText("30843-22.htm");
						giveItems(player, Rnd.get(REWARDS), 4);
						break;
					
					case 0:
						htmltext = getHtmlText("30843-24.htm");
						giveItems(player, Rnd.get(REWARDS), 10);
						break;
					
					default:
						htmltext = getHtmlText("30843-23.htm");
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
				htmltext = (player.getStatus().getLevel() < 58) ? "30843-04.htm" : "30843-01.htm";
				break;
			
			case STARTED:
				htmltext = (player.getInventory().getItemCount(STOLEN_INFERNIUM_ORE) < 100) ? "30843-06.htm" : "30843-07.htm";
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
		
		dropItems(st.getPlayer(), STOLEN_INFERNIUM_ORE, 1, 0, CHANCES.get(npc.getNpcId()));
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