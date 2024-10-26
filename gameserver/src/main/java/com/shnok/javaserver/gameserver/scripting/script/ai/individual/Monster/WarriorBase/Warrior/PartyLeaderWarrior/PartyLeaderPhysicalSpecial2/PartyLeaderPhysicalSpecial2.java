package com.shnok.javaserver.gameserver.scripting.script.ai.individual.Monster.WarriorBase.Warrior.PartyLeaderWarrior.PartyLeaderPhysicalSpecial2;

import net.sf.l2j.commons.random.Rnd;

import net.sf.l2j.gameserver.enums.actors.NpcSkillType;
import net.sf.l2j.gameserver.model.actor.Creature;
import net.sf.l2j.gameserver.model.actor.Npc;
import net.sf.l2j.gameserver.model.actor.Playable;
import net.sf.l2j.gameserver.scripting.script.ai.individual.Monster.WarriorBase.Warrior.PartyLeaderWarrior.PartyLeaderWarrior;
import net.sf.l2j.gameserver.skills.L2Skill;

public class PartyLeaderPhysicalSpecial2 extends PartyLeaderWarrior
{
	public PartyLeaderPhysicalSpecial2()
	{
		super("ai/individual/Monster/WarriorBase/Warrior/PartyLeaderWarrior/PartyLeaderPhysicalSpecial2");
	}
	
	public PartyLeaderPhysicalSpecial2(String descr)
	{
		super(descr);
	}
	
	@Override
	public void onCreated(Npc npc)
	{
		npc._i_ai0 = 0;
		
		super.onCreated(npc);
	}
	
	@Override
	public void onAttacked(Npc npc, Creature attacker, int damage, L2Skill skill)
	{
		if (attacker instanceof Playable)
		{
			final Creature topDesireTarget = npc.getAI().getTopDesireTarget();
			if (topDesireTarget != null)
			{
				if (Rnd.get(100) < 33 && topDesireTarget == attacker && npc.distance2D(attacker) >= 40)
					npc.getAI().addCastDesire(attacker, getNpcSkillByType(npc, NpcSkillType.PHYSICAL_SPECIAL2), 1000000);
				
				if (Rnd.get(100) < 33 && topDesireTarget == attacker && npc.distance2D(attacker) < 40)
					npc.getAI().addCastDesire(attacker, getNpcSkillByType(npc, NpcSkillType.PHYSICAL_SPECIAL), 1000000);
			}
		}
		
		super.onAttacked(npc, attacker, damage, skill);
	}
	
	@Override
	public void onClanAttacked(Npc caller, Npc called, Creature attacker, int damage, L2Skill skill)
	{
		if (called.getAI().getLifeTime() > 7 && (attacker instanceof Playable))
		{
			final Creature topDesireTarget = called.getAI().getTopDesireTarget();
			if (topDesireTarget != null)
			{
				if (Rnd.get(100) < 33 && topDesireTarget == attacker && called.distance2D(attacker) >= 40)
					called.getAI().addCastDesire(attacker, getNpcSkillByType(called, NpcSkillType.PHYSICAL_SPECIAL2), 1000000);
				
				if (Rnd.get(100) < 33 && topDesireTarget == attacker && called.distance2D(attacker) < 40)
					called.getAI().addCastDesire(attacker, getNpcSkillByType(called, NpcSkillType.PHYSICAL_SPECIAL), 1000000);
			}
		}
		
		super.onClanAttacked(caller, called, attacker, damage, skill);
	}
}
