package com.shnok.javaserver.gameserver.scripting.script.ai.siegablehall;

import com.shnok.javaserver.commons.random.Rnd;

import com.shnok.javaserver.gameserver.enums.IntentionType;
import com.shnok.javaserver.gameserver.model.actor.Creature;
import com.shnok.javaserver.gameserver.model.actor.Npc;
import com.shnok.javaserver.gameserver.model.actor.Playable;
import com.shnok.javaserver.gameserver.model.actor.Player;
import com.shnok.javaserver.gameserver.network.NpcStringId;
import com.shnok.javaserver.gameserver.scripting.script.ai.individual.DefaultNpc;
import com.shnok.javaserver.gameserver.skills.L2Skill;

public class Agit02Mikhail extends DefaultNpc
{
	public Agit02Mikhail()
	{
		super("ai/siegeablehall");
	}
	
	public Agit02Mikhail(String descr)
	{
		super(descr);
	}
	
	protected final int[] _npcIds =
	{
		35409
	};
	
	@Override
	public void onCreated(Npc npc)
	{
		npc.broadcastNpcShout(NpcStringId.ID_1000276);
		npc._i_ai0 = 0;
		startQuestTimerAtFixedRate("1001", npc, null, 1000, 60000);
		
		super.onCreated(npc);
	}
	
	@Override
	public String onTimer(String name, Npc npc, Player player)
	{
		if (name.equalsIgnoreCase("1001"))
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
		if (Rnd.get(3) < 1)
			called.getAI().addCastDesire(target, 4237, 1, 1000000);
		
		if (target instanceof Playable)
			called.getAI().addAttackDesire(target, (((damage * 1.0 / called.getStatus().getMaxHp()) / 0.05) * damage) * caller._weightPoint / 1000000);
		
		if (called.hasMaster() && called.getMaster().getStatus().getHpRatio() < 0.05 && called._i_ai0 == 0)
		{
			called._i_ai0 = 1;
			called.getAI().addCastDesire(called, 4235, 1, 1000000000);
			called.broadcastNpcSay(NpcStringId.ID_1000279);
		}
	}
	
	@Override
	public void onUseSkillFinished(Npc npc, Creature creature, L2Skill skill, boolean success)
	{
		if (skill.getId() == 4235)
		{
			npc.teleportTo(177134, -18807, -2263, 0);
			npc.removeAllAttackDesire();
		}
	}
	
	@Override
	public void onSeeSpell(Npc npc, Player caster, L2Skill skill, Creature[] targets, boolean isPet)
	{
		if (skill.getAggroPoints() > 0 && npc.getAI().getCurrentIntention().getType() == IntentionType.ATTACK && npc.getAI().getTopDesireTarget() == caster)
			npc.getAI().addAttackDesire(caster, (((skill.getAggroPoints() * 1.0 / npc.getStatus().getMaxHp()) / 0.05) * 150));
	}
}
