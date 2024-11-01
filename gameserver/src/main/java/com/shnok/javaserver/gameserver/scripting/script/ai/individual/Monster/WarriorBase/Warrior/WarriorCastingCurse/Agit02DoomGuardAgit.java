package com.shnok.javaserver.gameserver.scripting.script.ai.individual.Monster.WarriorBase.Warrior.WarriorCastingCurse;

import com.shnok.javaserver.commons.random.Rnd;

import com.shnok.javaserver.gameserver.enums.actors.NpcSkillType;
import com.shnok.javaserver.gameserver.model.actor.Creature;
import com.shnok.javaserver.gameserver.model.actor.Npc;
import com.shnok.javaserver.gameserver.model.actor.Playable;
import com.shnok.javaserver.gameserver.model.actor.Player;
import com.shnok.javaserver.gameserver.skills.L2Skill;

public class Agit02DoomGuardAgit extends WarriorCastingCurse
{
	public Agit02DoomGuardAgit()
	{
		super("ai/individual/Monster/WarriorBase/Warrior/WarriorCastingCurse");
	}
	
	public Agit02DoomGuardAgit(String descr)
	{
		super(descr);
	}
	
	protected final int[] _npcIds =
	{
		35412,
		35633
	};
	
	@Override
	public void onNoDesire(Npc npc)
	{
		// Do nothing
	}
	
	@Override
	public void onCreated(Npc npc)
	{
		npc._flag = 0;
		
		super.onCreated(npc);
	}
	
	@Override
	public void onAttacked(Npc npc, Creature attacker, int damage, L2Skill skill)
	{
		if (attacker instanceof Playable)
		{
			final Creature topDesireTarget = npc.getAI().getTopDesireTarget();
			final L2Skill debuff = getNpcSkillByType(npc, NpcSkillType.DEBUFF);
			if (topDesireTarget != null && Rnd.get(100) < 10 && getAbnormalLevel(attacker, debuff) <= 0 && topDesireTarget == attacker)
				npc.getAI().addCastDesireHold(topDesireTarget, debuff, 1000000);
		}
		
		if (attacker instanceof Playable)
			npc.getAI().addAttackDesireHold(attacker, (((damage * 1.0) / npc.getStatus().getMaxHp()) / 0.05) * 100);
	}
	
	@Override
	public void onClanAttacked(Npc caller, Npc called, Creature attacker, int damage, L2Skill skill)
	{
		if ((called.getAI().getLifeTime() > 7 && (attacker instanceof Playable)) && called._flag == caller._flag)
		{
			final L2Skill debuff = getNpcSkillByType(called, NpcSkillType.DEBUFF);
			
			if (Rnd.get(100) < 10 && getAbnormalLevel(attacker, debuff) <= 0)
				called.getAI().addCastDesireHold(attacker, debuff, 1000000);
		}
		
		if (called._flag == caller._flag && attacker instanceof Playable)
			called.getAI().addAttackDesireHold(attacker, (((damage * 1.0) / called.getStatus().getMaxHp()) / 0.05) * 100);
	}
	
	@Override
	public void onSeeSpell(Npc npc, Player caster, L2Skill skill, Creature[] targets, boolean isPet)
	{
		if (skill.getAggroPoints() > 0)
			npc.getAI().addAttackDesireHold(caster, (((skill.getAggroPoints() * 1.0) / npc.getStatus().getMaxHp()) / 0.05) * 150);
	}
	
	@Override
	public void onSeeCreature(Npc npc, Creature creature)
	{
		if (!(creature instanceof Playable))
			return;
		
		npc.getAI().addAttackDesireHold(creature, 200);
	}
	
}
