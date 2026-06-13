package com.shnok.javaserver.gameserver.scripting.script.feature;

import java.util.HashMap;
import java.util.Map;

import com.shnok.javaserver.commons.lang.StringUtil;

import com.shnok.javaserver.gameserver.model.actor.Npc;
import com.shnok.javaserver.gameserver.model.actor.Player;
import com.shnok.javaserver.gameserver.model.records.ScoreData;
import com.shnok.javaserver.gameserver.scripting.Quest;

public class EchoCrystal extends Quest
{
	private static final int ADENA = 57;
	private static final int COST = 200;
	
	private static final Map<Integer, ScoreData> SCORES = new HashMap<>();
	{
		SCORES.put(4410, new ScoreData(4411, "01", "02", "03"));
		SCORES.put(4409, new ScoreData(4412, "04", "05", "06"));
		SCORES.put(4408, new ScoreData(4413, "07", "08", "09"));
		SCORES.put(4420, new ScoreData(4414, "10", "11", "12"));
		SCORES.put(4421, new ScoreData(4415, "13", "14", "15"));
		SCORES.put(4419, new ScoreData(4417, "16", "05", "06"));
		SCORES.put(4418, new ScoreData(4416, "17", "05", "06"));
	}
	
	public EchoCrystal()
	{
		super(-1, "feature");
		
		addTalkId(31042, 31043);
	}
	
	@Override
	public String onAdvEvent(String event, Npc npc, Player player)
	{
		String htmltext = "";
		
		if (StringUtil.isDigit(event))
		{
			final int score = Integer.parseInt(event);
			
			final ScoreData sd = SCORES.get(score);
			if (sd != null)
			{
				if (!player.getInventory().hasItem(score))
					htmltext = npc.getNpcId() + "-" + sd.noScoreMsg() + ".htm";
				else if (player.getInventory().getItemCount(ADENA) < COST)
					htmltext = npc.getNpcId() + "-" + sd.noAdenaMsg() + ".htm";
				else
				{
					htmltext = npc.getNpcId() + "-" + sd.okMsg() + ".htm";
					
					takeItems(player, ADENA, COST);
					giveItems(player, sd.crystalId(), 1);
				}
			}
		}
		
		return htmltext;
	}
	
	@Override
	public String onTalk(Npc npc, Player player)
	{
		return "1.htm";
	}
}