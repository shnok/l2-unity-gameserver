package com.shnok.javaserver.gameserver.scripting.script.ai.individual.Monster.WarriorBase.Warrior.WarriorCastingCurse;

import com.shnok.javaserver.commons.random.Rnd;

import com.shnok.javaserver.gameserver.enums.IntentionType;
import com.shnok.javaserver.gameserver.enums.actors.NpcSkillType;
import com.shnok.javaserver.gameserver.model.actor.Creature;
import com.shnok.javaserver.gameserver.model.actor.Npc;
import com.shnok.javaserver.gameserver.model.actor.Playable;
import com.shnok.javaserver.gameserver.model.actor.Player;
import com.shnok.javaserver.gameserver.scripting.script.ai.individual.Monster.WarriorBase.Warrior.Warrior;
import com.shnok.javaserver.gameserver.skills.L2Skill;

public class WarriorCastingCurse extends Warrior
{
	public WarriorCastingCurse()
	{
		super("ai/individual/Monster/WarriorBase/Warrior/WarriorCastingCurse");
	}
	
	public WarriorCastingCurse(String descr)
	{
		super(descr);
	}
	
	protected final int[] _npcIds =
	{
		20075,
		20261,
		20506,
		20541,
		20850,
		21095,
		21669,
		21692,
		21715,
		21738,
		21761,
		21784,
		20082,
		20795,
		20838,
		20204,
		20228,
		20998,
		20776,
		21007,
		20800,
		20555,
		20646,
		20231,
		20478,
		27120,
		20679,
		20926,
		20038,
		20558,
		20033,
		27119,
		20677,
		22051
	};
	
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
					final L2Skill debuff = getNpcSkillByType(npc, NpcSkillType.DEBUFF);
					if (getAbnormalLevel(mostHated, debuff) <= 0)
						npc.getAI().addCastDesire(mostHated, debuff, 1000000);
				}
			}
			
			startQuestTimer("2001", npc, null, 5000);
			
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
			if (mostHated == attacker)
				npc._i_ai2 = 1;
		}
		else
		{
			startQuestTimer("2001", npc, null, 5000);
			
			npc._i_ai1 = 1;
		}
		
		if (attacker instanceof Playable && mostHated == attacker && Rnd.get(100) < 33)
		{
			final L2Skill debuff = getNpcSkillByType(npc, NpcSkillType.DEBUFF);
			if (getAbnormalLevel(attacker, debuff) <= 0)
				npc.getAI().addCastDesire(attacker, debuff, 1000000);
		}
		super.onAttacked(npc, attacker, damage, skill);
	}
	
	@Override
	public void onClanAttacked(Npc caller, Npc called, Creature attacker, int damage, L2Skill skill)
	{
		if (attacker instanceof Playable && called.getAI().getLifeTime() > 7 && Rnd.get(100) < 33)
		{
			final L2Skill debuff = getNpcSkillByType(called, NpcSkillType.DEBUFF);
			if (getAbnormalLevel(attacker, debuff) <= 0)
				called.getAI().addCastDesire(attacker, debuff, 1000000);
		}
		super.onClanAttacked(caller, called, attacker, damage, skill);
	}
}