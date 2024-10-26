package com.shnok.javaserver.gameserver.scripting.script.ai.individual;

import com.shnok.javaserver.commons.random.Rnd;

import com.shnok.javaserver.gameserver.enums.EventHandler;
import com.shnok.javaserver.gameserver.enums.actors.ClassType;
import com.shnok.javaserver.gameserver.model.actor.Npc;
import com.shnok.javaserver.gameserver.model.actor.Player;
import com.shnok.javaserver.gameserver.network.NpcStringId;
import com.shnok.javaserver.gameserver.scripting.Quest;

/**
 * This Npc buffs or debuffs (with an equal 50% chance) the {@link Player} upon interaction, based on {@link Player}'s {@link ClassType}.<br>
 * <br>
 * The 30 seconds timer only affect chat ability.
 */
public class DaimonTheWhiteEyed extends Quest
{
	private static final NpcStringId[] DEBUFF_CHAT =
	{
		NpcStringId.ID_1000458,
		NpcStringId.ID_1000459,
		NpcStringId.ID_1000460
	};
	
	private static final NpcStringId[] BUFF_CHAT =
	{
		NpcStringId.ID_1000461,
		NpcStringId.ID_1000462,
		NpcStringId.ID_1000463
	};
	
	public DaimonTheWhiteEyed()
	{
		super(-1, "ai/individual");
		
		addEventIds(31705, EventHandler.CREATED, EventHandler.FIRST_TALK);
	}
	
	@Override
	public String onTimer(String name, Npc npc, Player player)
	{
		if (name.equalsIgnoreCase("6543"))
		{
			npc._i_ai0 = 0;
		}
		return super.onTimer(name, npc, player);
	}
	
	@Override
	public void onCreated(Npc npc)
	{
		npc.getAI().addMoveRouteDesire("argos_daemon_roaming", 2000);
		
		npc._i_ai0 = 0;
	}
	
	@Override
	public String onFirstTalk(Npc npc, Player player)
	{
		if (Rnd.nextBoolean())
		{
			if (npc._i_ai0 == 0)
			{
				npc.broadcastNpcSay(Rnd.get(DEBUFF_CHAT));
				npc._i_ai0 = 1;
				
				startQuestTimer("6543", npc, null, 30000);
			}
			
			if (player.getClassId().getType() == ClassType.FIGHTER)
				npc.getAI().addCastDesire(player, 1206, 19, 1000000);
			else
				npc.getAI().addCastDesire(player, 1083, 17, 1000000);
		}
		else
		{
			if (npc._i_ai0 == 0)
			{
				npc.broadcastNpcSay(Rnd.get(BUFF_CHAT));
				npc._i_ai0 = 1;
				
				startQuestTimer("6543", npc, null, 30000);
			}
			
			if (player.getClassId().getType() == ClassType.FIGHTER)
				npc.getAI().addCastDesire(player, 1086, 2, 1000000);
			else
				npc.getAI().addCastDesire(player, 1059, 3, 1000000);
		}
		return null;
	}
}