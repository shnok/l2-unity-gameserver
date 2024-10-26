package com.shnok.javaserver.gameserver.scripting.script.ai.individual.Monster.WarriorBase.Warrior.PartyLeaderWarrior;

import com.shnok.javaserver.commons.random.Rnd;

import com.shnok.javaserver.gameserver.enums.IntentionType;
import com.shnok.javaserver.gameserver.enums.actors.NpcSkillType;
import com.shnok.javaserver.gameserver.model.actor.Creature;
import com.shnok.javaserver.gameserver.model.actor.Npc;
import com.shnok.javaserver.gameserver.model.actor.Playable;
import com.shnok.javaserver.gameserver.model.actor.Player;
import com.shnok.javaserver.gameserver.skills.L2Skill;

public class PartyLeaderCastDD extends PartyLeaderWarrior
{
	public PartyLeaderCastDD()
	{
		super("ai/individual/Monster/WarriorBase/Warrior/PartyLeaderWarrior");
	}
	
	public PartyLeaderCastDD(String descr)
	{
		super(descr);
	}
	
	@Override
	public String onTimer(String name, Npc npc, Player player)
	{
		if (name.equalsIgnoreCase("2001"))
		{
			final IntentionType currentIntention = npc.getAI().getCurrentIntention().getType();
			if (currentIntention != IntentionType.ATTACK && currentIntention != IntentionType.CAST)
			{
				npc._i_ai1 = 0;
				npc._i_ai2 = 0;
				
				return super.onTimer(name, npc, player);
			}
			
			if (npc._i_ai2 == 0)
			{
				final L2Skill ddMagic = getNpcSkillByType(npc, NpcSkillType.DD_MAGIC);
				if (ddMagic != null && Rnd.get(100) < 50)
				{
					final Creature mostHated = npc.getAI().getAggroList().getMostHatedCreature();
					if (mostHated != null)
						npc.getAI().addCastDesire(mostHated, ddMagic, 1000000);
				}
			}
			
			startQuestTimer("2001", npc, player, 5000);
			
			npc._i_ai2 = 0;
		}
		return super.onTimer(name, npc, player);
	}
	
	@Override
	public void onCreated(Npc npc)
	{
		npc._i_ai1 = 0;
		npc._i_ai2 = 0;
		
		super.onCreated(npc);
	}
	
	@Override
	public void onAttacked(Npc npc, Creature attacker, int damage, L2Skill skill)
	{
		final Creature mostHated = npc.getAI().getAggroList().getMostHatedCreature();
		
		if (npc._i_ai1 == 1)
		{
			if (mostHated != null && mostHated == attacker && npc._i_ai2 != 1)
				npc._i_ai2 = 1;
		}
		else
		{
			startQuestTimer("2001", npc, null, 5000);
			
			npc._i_ai1 = 1;
		}
		
		if (attacker instanceof Playable && mostHated == attacker)
		{
			final L2Skill ddMagic = getNpcSkillByType(npc, NpcSkillType.DD_MAGIC);
			if (ddMagic != null && Rnd.get(100) < 33 && npc.distance2D(attacker) < 100)
				npc.getAI().addCastDesire(attacker, ddMagic, 1000000);
		}
		super.onAttacked(npc, attacker, damage, skill);
	}
	
	@Override
	public void onClanAttacked(Npc caller, Npc called, Creature attacker, int damage, L2Skill skill)
	{
		if (attacker instanceof Playable && called.getAI().getLifeTime() > 7)
		{
			final L2Skill ddMagic = getNpcSkillByType(called, NpcSkillType.DD_MAGIC);
			if (ddMagic != null && Rnd.get(100) < 33 && called.distance2D(attacker) > 100)
				called.getAI().addCastDesire(attacker, ddMagic, 1000000);
		}
		super.onClanAttacked(caller, called, attacker, damage, skill);
	}
}