package com.shnok.javaserver.gameserver.scripting.script.ai.siegeguards.GludioHold;

import com.shnok.javaserver.commons.random.Rnd;
import com.shnok.javaserver.commons.util.ArraysUtil;

import com.shnok.javaserver.gameserver.enums.ZoneId;
import com.shnok.javaserver.gameserver.model.actor.Creature;
import com.shnok.javaserver.gameserver.model.actor.Npc;
import com.shnok.javaserver.gameserver.model.actor.Playable;
import com.shnok.javaserver.gameserver.model.actor.Player;
import com.shnok.javaserver.gameserver.model.actor.instance.Door;
import com.shnok.javaserver.gameserver.model.actor.instance.SiegeSummon;
import com.shnok.javaserver.gameserver.scripting.script.ai.individual.DefaultNpc;
import com.shnok.javaserver.gameserver.skills.L2Skill;

public class GludioHold extends DefaultNpc
{
	public GludioHold()
	{
		super("ai/siegeguards/GludioHold");
	}
	
	public GludioHold(String descr)
	{
		super(descr);
	}
	
	@Override
	public void onNoDesire(Npc npc)
	{
		npc.getAI().addMoveToDesire(npc.getSpawnLocation(), 30);
	}
	
	@Override
	public void onAttacked(Npc npc, Creature attacker, int damage, L2Skill skill)
	{
		if (attacker.getZ() > (npc.getZ() + 100))
		{
			npc.getAI().addFleeDesire(attacker, 150, 30);
		}
		else if (getPledgeCastleState(npc, attacker) != 2 && attacker instanceof Playable)
		{
			npc.getAI().addAttackDesire(attacker, (((damage * 1.0) / npc.getStatus().getMaxHp()) / 0.05 * 100));
		}
		
		if (npc.isInsideZone(ZoneId.PEACE))
		{
			npc.teleportTo(npc.getSpawnLocation(), 0);
			npc.removeAllAttackDesire();
		}
	}
	
	@Override
	public void onClanAttacked(Npc caller, Npc called, Creature attacker, int damage, L2Skill skill)
	{
		if (attacker.getZ() <= (called.getZ() + 100) && getPledgeCastleState(called, attacker) != 2)
			called.getAI().addAttackDesire(attacker, (((damage * 1.0) / called.getStatus().getMaxHp()) / 0.05 * 50));
	}
	
	@Override
	public void onStaticObjectClanAttacked(Door caller, Npc called, Creature attacker, int damage, L2Skill skill)
	{
		if (attacker instanceof SiegeSummon)
		{
			if (Rnd.get(100) < 10)
				called.getAI().addAttackDesire(attacker.getActingPlayer(), 5000);
			else
				called.getAI().addAttackDesire(attacker, 1000);
		}
		else if (getPledgeCastleState(called, attacker) != 2 && attacker instanceof Playable)
		{
			called.getAI().addAttackDesire(attacker, (((damage * 1.0) / called.getStatus().getMaxHp()) / 0.05 * 50));
		}
	}
	
	@Override
	public void onSeeSpell(Npc npc, Player caster, L2Skill skill, Creature[] targets, boolean isPet)
	{
		if (caster.getZ() <= (npc.getZ() + 100) && getPledgeCastleState(npc, caster) != 2)
		{
			if (skill.isOffensive() && npc.isInCombat() && ArraysUtil.contains(targets, npc.getAI().getTopDesireTarget()))
			{
				npc.getAI().addAttackDesire(caster, (((skill.getAggroPoints() * 1.0) / npc.getStatus().getMaxHp()) / 0.05 * 50));
				
				if (npc.getMove().getGeoPathFailCount() > 10 && npc.getStatus().getHpRatio() < 1.0)
					npc.teleportTo(caster.getPosition(), 0);
			}
		}
	}
	
	@Override
	public void onSeeCreature(Npc npc, Creature creature)
	{
		if (creature.getZ() <= (npc.getZ() + 100) && creature instanceof Playable && getPledgeCastleState(npc, creature) != 2 && npc.getAI().getLifeTime() > 7)
			npc.getAI().addAttackDesire(creature, 200);
		
		if (npc.isInsideZone(ZoneId.PEACE))
		{
			npc.teleportTo(npc.getSpawnLocation(), 0);
			npc.removeAllAttackDesire();
		}
	}
	
	@Override
	public void onMoveToFinished(Npc npc, int x, int y, int z)
	{
		if (!npc.getSpawnLocation().equals(x, y, z))
			npc.getAI().addMoveToDesire(npc.getSpawnLocation(), 30);
		else
			npc.getAI().addDoNothingDesire(40, 30);
	}
}