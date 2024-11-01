package com.shnok.javaserver.gameserver.scripting.script.ai.individual.Monster.WarriorBase.Warrior.PartyLeaderWarrior;

import com.shnok.javaserver.commons.random.Rnd;

import com.shnok.javaserver.gameserver.enums.IntentionType;
import com.shnok.javaserver.gameserver.enums.actors.NpcSkillType;
import com.shnok.javaserver.gameserver.model.actor.Creature;
import com.shnok.javaserver.gameserver.model.actor.Npc;
import com.shnok.javaserver.gameserver.model.actor.Playable;
import com.shnok.javaserver.gameserver.model.actor.Player;
import com.shnok.javaserver.gameserver.skills.L2Skill;

public class PartyLeaderCastCurseAggressive extends PartyLeaderWarriorAggressive
{
	public PartyLeaderCastCurseAggressive()
	{
		super("ai/individual/Monster/WarriorBase/Warrior/PartyLeaderWarrior");
	}
	
	public PartyLeaderCastCurseAggressive(String descr)
	{
		super(descr);
	}
	
	protected final int[] _npcIds =
	{
		20773,
		21081
	};
	
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
				final L2Skill debuff = getNpcSkillByType(npc, NpcSkillType.DEBUFF);
				if (debuff != null && Rnd.get(100) < 50)
				{
					final Creature mostHated = npc.getAI().getAggroList().getMostHatedCreature();
					if (mostHated != null && getAbnormalLevel(mostHated, debuff) <= 0)
						npc.getAI().addCastDesire(mostHated, debuff, 1000000);
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
			final L2Skill debuff = getNpcSkillByType(npc, NpcSkillType.DEBUFF);
			if (debuff != null && Rnd.get(100) < 33 && getAbnormalLevel(attacker, debuff) <= 0)
				npc.getAI().addCastDesire(attacker, debuff, 1000000);
		}
		super.onAttacked(npc, attacker, damage, skill);
	}
	
	@Override
	public void onClanAttacked(Npc caller, Npc called, Creature attacker, int damage, L2Skill skill)
	{
		if (attacker instanceof Playable && called.getAI().getLifeTime() > 7)
		{
			final L2Skill debuff = getNpcSkillByType(called, NpcSkillType.DEBUFF);
			if (debuff != null && Rnd.get(100) < 33 && getAbnormalLevel(attacker, debuff) <= 0)
				called.getAI().addCastDesire(attacker, debuff, 1000000);
		}
		super.onClanAttacked(caller, called, attacker, damage, skill);
	}
}