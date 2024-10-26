package com.shnok.javaserver.gameserver.scripting.script.ai.individual.Monster.WarriorBase.Warrior.PartyPrivateWarrior;

import com.shnok.javaserver.commons.random.Rnd;

import com.shnok.javaserver.gameserver.enums.IntentionType;
import com.shnok.javaserver.gameserver.enums.actors.NpcSkillType;
import com.shnok.javaserver.gameserver.model.actor.Creature;
import com.shnok.javaserver.gameserver.model.actor.Npc;
import com.shnok.javaserver.gameserver.model.actor.Playable;
import com.shnok.javaserver.gameserver.model.actor.Player;
import com.shnok.javaserver.gameserver.skills.L2Skill;

public class PartyPrivateSlowTypeMagic extends PartyPrivateWarrior
{
	public PartyPrivateSlowTypeMagic()
	{
		super("ai/individual/Monster/WarriorBase/Warrior/PartyPrivateWarrior");
	}
	
	public PartyPrivateSlowTypeMagic(String descr)
	{
		super(descr);
	}
	
	protected final int[] _npcIds =
	{
		20964
	};
	
	@Override
	public String onTimer(String name, Npc npc, Player player)
	{
		if (name.equalsIgnoreCase("2001"))
		{
			final IntentionType currentIntention = npc.getAI().getCurrentIntention().getType();
			if (currentIntention != IntentionType.ATTACK && currentIntention != IntentionType.CAST)
			{
				npc._i_ai0 = 0;
				npc._i_ai1 = 0;
				
				return super.onTimer(name, npc, player);
			}
			
			if (npc._i_ai1 == 0)
			{
				final Creature mostHated = npc.getAI().getAggroList().getMostHatedCreature();
				if (mostHated != null && Rnd.get(100) < 50)
				{
					final L2Skill ddMagicSlow = getNpcSkillByType(npc, NpcSkillType.DD_MAGIC_SLOW);
					npc.getAI().addCastDesire(mostHated, ddMagicSlow, 1000000);
				}
			}
			
			startQuestTimer("2001", npc, null, 20000);
			
			npc._i_ai1 = 0;
		}
		return super.onTimer(name, npc, player);
	}
	
	@Override
	public void onCreated(Npc npc)
	{
		npc._i_ai0 = 0;
		npc._i_ai1 = 0;
		
		super.onCreated(npc);
	}
	
	@Override
	public void onAttacked(Npc npc, Creature attacker, int damage, L2Skill skill)
	{
		if (npc.distance2D(attacker) > 200 && Rnd.get(100) < 70)
		{
			final L2Skill ddMagicSlow = getNpcSkillByType(npc, NpcSkillType.DD_MAGIC_SLOW);
			npc.getAI().addCastDesire(attacker, ddMagicSlow, 1000000);
		}
		
		if (npc._i_ai0 == 1)
		{
			final Creature mostHated = npc.getAI().getAggroList().getMostHatedCreature();
			if (mostHated != null && mostHated == attacker)
				npc._i_ai1 = 1;
		}
		else
		{
			startQuestTimer("2001", npc, null, 20000);
			
			npc._i_ai1 = 0;
		}
		super.onAttacked(npc, attacker, damage, skill);
	}
	
	@Override
	public void onClanAttacked(Npc caller, Npc called, Creature attacker, int damage, L2Skill skill)
	{
		if (attacker instanceof Playable && called.getAI().getLifeTime() > 7 && called.getAI().getCurrentIntention().getType() != IntentionType.ATTACK && Rnd.get(100) < 10)
		{
			final L2Skill ddMagicSlow = getNpcSkillByType(called, NpcSkillType.DD_MAGIC_SLOW);
			called.getAI().addCastDesire(attacker, ddMagicSlow, 1000000);
		}
		super.onClanAttacked(caller, called, attacker, damage, skill);
	}
}