package com.shnok.javaserver.gameserver.scripting.script.ai.individual.Monster.WarriorBase.Warrior.WarriorCastingCurse;

import com.shnok.javaserver.commons.random.Rnd;

import com.shnok.javaserver.gameserver.enums.actors.NpcSkillType;
import com.shnok.javaserver.gameserver.model.actor.Creature;
import com.shnok.javaserver.gameserver.model.actor.Npc;
import com.shnok.javaserver.gameserver.model.actor.Playable;
import com.shnok.javaserver.gameserver.model.actor.Player;
import com.shnok.javaserver.gameserver.skills.L2Skill;

public class Agit02DoomKnightAgitParty extends WarriorCastingCurse
{
	public Agit02DoomKnightAgitParty()
	{
		super("ai/individual/Monster/WarriorBase/Warrior/WarriorCastingCurse");
	}
	
	public Agit02DoomKnightAgitParty(String descr)
	{
		super(descr);
	}
	
	@Override
	public void onNoDesire(Npc npc)
	{
		// Do nothing
	}
	
	@Override
	public void onCreated(Npc npc)
	{
		startQuestTimerAtFixedRate("3001", npc, null, 1000, 60000);
		
		super.onCreated(npc);
	}
	
	@Override
	public String onTimer(String name, Npc npc, Player player)
	{
		if (name.equalsIgnoreCase("3001"))
		{
			if (!npc.isInMyTerritory() && Rnd.get(3) < 1 && npc.hasMaster())
			{
				npc.teleportTo(npc.getMaster().getPosition(), 0);
				npc.removeAllAttackDesire();
			}
			if (Rnd.get(5) < 1)
				npc.getAI().getAggroList().randomizeAttack();
		}
		
		return super.onTimer(name, npc, player);
	}
	
	@Override
	public void onPartyAttacked(Npc caller, Npc called, Creature target, int damage)
	{
		if (called.getAI().getLifeTime() > 8 && target instanceof Playable)
		{
			final L2Skill debuff = getNpcSkillByType(called, NpcSkillType.DEBUFF);
			if (Rnd.get(100) < 10 && getAbnormalLevel(caller, debuff) <= 0)
				called.getAI().addCastDesire(target, debuff, 1000000);
		}
		
		if (called._flag == caller._flag && target instanceof Playable)
			called.getAI().addAttackDesire(target, (((damage * 1.0) / called.getStatus().getMaxHp()) / 0.05) * 100);
	}
	
	@Override
	public void onSeeSpell(Npc npc, Player caster, L2Skill skill, Creature[] targets, boolean isPet)
	{
		if (skill.getAggroPoints() > 0)
			npc.getAI().addAttackDesire(caster, (((skill.getAggroPoints() * 1.0) / npc.getStatus().getMaxHp()) / 0.05) * 150);
	}
	
	@Override
	public void onClanAttacked(Npc caller, Npc called, Creature attacker, int damage, L2Skill skill)
	{
		// Do nothing
	}
}
