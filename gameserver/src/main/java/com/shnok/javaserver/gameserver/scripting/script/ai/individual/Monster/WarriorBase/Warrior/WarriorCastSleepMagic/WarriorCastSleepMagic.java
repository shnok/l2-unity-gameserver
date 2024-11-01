package com.shnok.javaserver.gameserver.scripting.script.ai.individual.Monster.WarriorBase.Warrior.WarriorCastSleepMagic;

import com.shnok.javaserver.commons.random.Rnd;

import com.shnok.javaserver.gameserver.enums.IntentionType;
import com.shnok.javaserver.gameserver.enums.actors.NpcSkillType;
import com.shnok.javaserver.gameserver.model.actor.Creature;
import com.shnok.javaserver.gameserver.model.actor.Npc;
import com.shnok.javaserver.gameserver.model.actor.Playable;
import com.shnok.javaserver.gameserver.model.actor.Player;
import com.shnok.javaserver.gameserver.scripting.script.ai.individual.Monster.WarriorBase.Warrior.Warrior;
import com.shnok.javaserver.gameserver.skills.L2Skill;

public class WarriorCastSleepMagic extends Warrior
{
	public WarriorCastSleepMagic()
	{
		super("ai/individual/Monster/WarriorBase/Warrior/WarriorCastSleepMagic");
	}
	
	public WarriorCastSleepMagic(String descr)
	{
		super(descr);
	}
	
	protected final int[] _npcIds =
	{
		20284,
		20283,
		20276,
		21023,
		20078
	};
	
	@Override
	public void onCreated(Npc npc)
	{
		npc._i_ai0 = 0;
		npc._i_ai1 = 0;
		npc._i_ai2 = 0;
		
		super.onCreated(npc);
	}
	
	@Override
	public String onTimer(String name, Npc npc, Player player)
	{
		if (name.equalsIgnoreCase("2001"))
		{
			if (npc.getAI().getCurrentIntention().getType() != IntentionType.ATTACK && npc.getAI().getCurrentIntention().getType() != IntentionType.CAST)
			{
				npc._i_ai1 = 0;
				npc._i_ai2 = 0;
				
				return super.onTimer(name, npc, player);
			}
			
			if (npc._i_ai2 == 0)
			{
				final Creature mostHated = npc.getAI().getAggroList().getMostHatedCreature();
				if (mostHated != null && Rnd.get(100) < 50)
				{
					final L2Skill sleepMagic = getNpcSkillByType(npc, NpcSkillType.SLEEP_MAGIC);
					if (getAbnormalLevel(mostHated, sleepMagic) <= 0)
						npc.getAI().addCastDesire(mostHated, sleepMagic, 1000000);
				}
			}
			
			startQuestTimer("2001", npc, null, 5000);
			
			npc._i_ai2 = 0;
		}
		return super.onTimer(name, npc, player);
	}
	
	@Override
	public void onAttacked(Npc npc, Creature attacker, int damage, L2Skill skill)
	{
		final Creature mostHated = npc.getAI().getAggroList().getMostHatedCreature();
		
		if (npc._i_ai1 == 1)
		{
			if (mostHated == attacker)
				npc._i_ai2 = 1;
		}
		else
		{
			startQuestTimer("2001", npc, null, 5000);
			
			npc._i_ai1 = 1;
		}
		
		if (attacker instanceof Playable)
		{
			if (npc._i_ai0 == 0)
				npc._i_ai0 = 1;
			else if (npc._i_ai0 == 1 && mostHated != attacker && Rnd.get(100) < 30 && npc.getStatus().getHpRatio() > 0.1)
				npc.getAI().addCastDesire(attacker, getNpcSkillByType(npc, NpcSkillType.SLEEP_MAGIC), 1000000);
		}
		super.onAttacked(npc, attacker, damage, skill);
	}
}