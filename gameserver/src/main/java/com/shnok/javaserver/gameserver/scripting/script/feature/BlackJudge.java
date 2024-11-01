package com.shnok.javaserver.gameserver.scripting.script.feature;

import com.shnok.javaserver.commons.lang.StringUtil;

import com.shnok.javaserver.gameserver.enums.EventHandler;
import com.shnok.javaserver.gameserver.model.actor.Npc;
import com.shnok.javaserver.gameserver.model.actor.Player;
import com.shnok.javaserver.gameserver.model.holder.IntIntHolder;
import com.shnok.javaserver.gameserver.scripting.Quest;

public class BlackJudge extends Quest
{
	private static final IntIntHolder[] DEATH_PENALTIES =
	{
		new IntIntHolder(76, 144000),
		new IntIntHolder(61, 86400),
		new IntIntHolder(52, 50400),
		new IntIntHolder(40, 25200),
		new IntIntHolder(20, 8640),
		new IntIntHolder(1, 3600)
	};
	
	public BlackJudge()
	{
		super(-1, "feature");
		
		addEventIds(30981, EventHandler.FIRST_TALK, EventHandler.TALKED);
	}
	
	@Override
	public String onAdvEvent(String event, Npc npc, Player player)
	{
		if (event.equalsIgnoreCase("test_dp"))
		{
			final int playerLevel = player.getStatus().getLevel();
			if (playerLevel >= 76)
				event = "black_judge007.htm";
			else if (playerLevel >= 61)
				event = "black_judge006.htm";
			else if (playerLevel >= 52)
				event = "black_judge005.htm";
			else if (playerLevel >= 40)
				event = "black_judge004.htm";
			else if (playerLevel >= 20)
				event = "black_judge003.htm";
			else if (playerLevel >= 1)
				event = "black_judge002.htm";
		}
		else if (event.startsWith("remove_dp"))
		{
			if (player.getDeathPenaltyBuffLevel() <= 0)
				event = "black_judge009.htm";
			else
			{
				final String[] split = event.split(" ");
				if (split.length < 2 || !StringUtil.isDigit(split[1]))
					return null;
				
				final IntIntHolder deathPenalty = DEATH_PENALTIES[Integer.parseInt(split[1])];
				if (player.getStatus().getLevel() < deathPenalty.getId())
					return null;
				
				if (player.getInventory().getAdena() < deathPenalty.getValue())
					event = "black_judge008.htm";
				else
				{
					takeItems(player, 57, deathPenalty.getValue());
					
					player.reduceDeathPenaltyBuffLevel();
					return null;
				}
			}
		}
		return event;
	}
	
	@Override
	public String onFirstTalk(Npc npc, Player player)
	{
		return "black_judge001.htm";
	}
}