package com.shnok.javaserver.gameserver.scripting.script.ai.individual.Monster.LV3Monster;

import net.sf.l2j.gameserver.model.actor.Creature;
import net.sf.l2j.gameserver.model.actor.Npc;
import net.sf.l2j.gameserver.model.actor.Playable;
import net.sf.l2j.gameserver.model.actor.Player;
import net.sf.l2j.gameserver.skills.L2Skill;

public class LV3PartyPrivateMonster extends LV3Monster
{
	public LV3PartyPrivateMonster()
	{
		super("ai/individual/Monster/LV3Monster");
	}
	
	public LV3PartyPrivateMonster(String descr)
	{
		super(descr);
	}
	
	protected final int[] _npcIds =
	{
		27261,
		27291,
		27268
	};
	
	@Override
	public void onCreated(Npc npc)
	{
		npc._param1 = npc.getMaster()._param1;
		npc._param2 = npc.getMaster()._param2;
		npc._param3 = npc.getMaster()._param3;
		
		startQuestTimer("3007", npc, null, 5000);
		
		super.onCreated(npc);
	}
	
	@Override
	public void onPartyAttacked(Npc caller, Npc called, Creature target, int damage)
	{
		if (target instanceof Playable)
			called.getAI().addAttackDesire(target, (((damage * 1.0) / called.getStatus().getMaxHp()) / 0.050000) * 50);
	}
	
	@Override
	public void onAttacked(Npc npc, Creature attacker, int damage, L2Skill skill)
	{
		if (!npc.hasMaster() || npc.getMaster().isDead())
		{
			if (attacker instanceof Playable)
			{
				if (damage == 0)
				{
					damage = 1;
				}
				npc.getAI().addAttackDesire(attacker, ((1.0 * damage) / (npc.getStatus().getLevel() + 7)) * 100);
			}
		}
		else if (attacker instanceof Playable)
		{
			if (damage == 0)
			{
				damage = 1;
			}
			npc.getAI().addAttackDesire(attacker, ((1.0 * damage) / (npc.getStatus().getLevel() + 7)) * 10);
		}
		
		super.onAttacked(npc, attacker, damage, skill);
	}
	
	@Override
	public void onClanAttacked(Npc caller, Npc called, Creature attacker, int damage, L2Skill skill)
	{
		if (attacker instanceof Playable)
			called.getAI().addAttackDesire(attacker, ((1.0 * damage) / (called.getStatus().getLevel() + 7)) * 30);
	}
	
	@Override
	public void onSeeSpell(Npc npc, Player caster, L2Skill skill, Creature[] targets, boolean isPet)
	{
		if (!npc.hasMaster() || npc.getMaster().isDead())
		{
			if (skill.getAggroPoints() > 0)
			{
				if (npc.getAttack().isAttackingNow() && targets.length > 0 && npc.getAI().getTopDesireTarget() == targets[0])
				{
					final int i0 = skill.getAggroPoints();
					double f0 = getHateRatio(npc, caster);
					f0 = (((1.000000 * i0) / (npc.getStatus().getLevel() + 7)) + ((f0 / 100) * ((1.000000 * i0) / (npc.getStatus().getLevel() + 7))));
					npc.getAI().addAttackDesire(caster, (f0 * 150));
				}
			}
		}
	}
	
	@Override
	public void onPartyDied(Npc caller, Npc called)
	{
		if (caller == called.getMaster())
			called.deleteMe();
	}
	
	@Override
	public String onTimer(String name, Npc npc, Player player)
	{
		if (name.equalsIgnoreCase("1005"))
		{
			if (!npc.isInMyTerritory() && npc.hasMaster() && !npc.isInCombat())
			{
				npc.teleportTo(npc.getSpawnLocation(), 0);
				npc.removeAllAttackDesire();
			}
			startQuestTimer("1005", npc, player, 120000);
		}
		if (name.equalsIgnoreCase("3007"))
		{
			if (!npc.hasMaster() || npc.getMaster().isDead())
				npc.deleteMe();
			else
				startQuestTimer("3007", npc, player, 5000);
		}
		return super.onTimer(name, npc, player);
	}
}
