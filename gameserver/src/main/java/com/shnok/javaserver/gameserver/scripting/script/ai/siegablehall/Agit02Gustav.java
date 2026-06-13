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

public class Agit02Gustav extends DefaultNpc
{
	public Agit02Gustav()
	{
		super("ai/siegeablehall");
	}
	
	public Agit02Gustav(String descr)
	{
		super(descr);
	}
	
	protected final int[] _npcIds =
	{
		35410
	};
	
	@Override
	public void onCreated(Npc npc)
	{
		npc.broadcastNpcShout(NpcStringId.ID_1000275);
		npc._weightPoint = 100;
		npc._i_ai0 = 0;
		npc._i_ai1 = 0;
		
		// TODO: these npcs should have different AI(_party suffix) when spawned as privates
		// ai_agit02_doom_knight_agit_party
		// ai_agit02_doom_archer_agit_party
		createOnePrivateEx(npc, 35409, 178304, -17712, -2194, 32768, 0, false);
		createOnePrivateEx(npc, 35408, 178306, -17535, -2195, 32768, 0, false);
		createOnePrivateEx(npc, 35416, 178178, -17346, -2226, 32768, 0, false);
		createOnePrivateEx(npc, 35416, 178110, -17768, -2226, 32768, 0, false);
		createOnePrivateEx(npc, 35416, 178132, -17683, -2226, 32768, 0, false);
		createOnePrivateEx(npc, 35416, 178180, -17953, -2226, 32768, 0, false);
		createOnePrivateEx(npc, 35416, 178147, -17172, -2226, 32768, 0, false);
		createOnePrivateEx(npc, 35416, 178056, -17424, -2226, 32768, 0, false);
		createOnePrivateEx(npc, 35416, 178120, -17331, -2226, 32768, 0, false);
		createOnePrivateEx(npc, 35416, 178124, -17577, -2226, 32768, 0, false);
		createOnePrivateEx(npc, 35416, 178159, -18138, -2226, 32768, 0, false);
		createOnePrivateEx(npc, 35416, 178396, -18050, -2226, 32768, 0, false);
		createOnePrivateEx(npc, 35416, 178078, -17917, -2226, 32768, 0, false);
		createOnePrivateEx(npc, 35416, 178261, -17269, -2226, 32768, 0, false);
		createOnePrivateEx(npc, 35416, 178152, -18063, -2226, 32768, 0, false);
		createOnePrivateEx(npc, 35416, 178025, -17541, -2226, 32768, 0, false);
		createOnePrivateEx(npc, 35416, 178285, -18013, -2226, 32768, 0, false);
		createOnePrivateEx(npc, 35413, 178112, -17968, -2221, 32768, 0, false);
		createOnePrivateEx(npc, 35413, 178079, -18092, -2221, 32768, 0, false);
		createOnePrivateEx(npc, 35413, 178039, -17732, -2221, 32768, 0, false);
		createOnePrivateEx(npc, 35413, 178224, -18025, -2221, 32768, 0, false);
		createOnePrivateEx(npc, 35413, 178111, -17510, -2221, 32768, 0, false);
		createOnePrivateEx(npc, 35413, 178088, -17620, -2221, 32768, 0, false);
		createOnePrivateEx(npc, 35413, 178059, -17223, -2221, 32768, 0, false);
		createOnePrivateEx(npc, 35413, 178043, -17325, -2221, 32768, 0, false);
		createOnePrivateEx(npc, 35413, 178311, -17946, -2221, 32768, 0, false);
		createOnePrivateEx(npc, 35413, 178170, -17885, -2221, 32768, 0, false);
		createOnePrivateEx(npc, 35413, 178317, -18097, -2221, 32768, 0, false);
		createOnePrivateEx(npc, 35413, 178099, -17823, -2221, 32768, 0, false);
		createOnePrivateEx(npc, 35413, 178290, -17335, -2221, 32768, 0, false);
		createOnePrivateEx(npc, 35413, 178116, -17386, -2221, 32768, 0, false);
		createOnePrivateEx(npc, 35413, 178169, -17266, -2221, 32768, 0, false);
		
		startQuestTimerAtFixedRate("1001", npc, null, 1000, 60000);
		
		super.onCreated(npc);
	}
	
	@Override
	public String onTimer(String name, Npc npc, Player player)
	{
		if (name.equalsIgnoreCase("1001"))
		{
			if (!npc.isInMyTerritory() && Rnd.get(3) < 1)
			{
				npc.teleportTo(npc.getSpawnLocation(), 0);
				npc.removeAllAttackDesire();
			}
			
			if (Rnd.get(5) < 1)
				npc.getAI().getAggroList().randomizeAttack();
		}
		if (name.equalsIgnoreCase("1002"))
		{
			npc.doDie(npc);
			npc._i_ai1 = 1;
		}
		
		return super.onTimer(name, npc, player);
	}
	
	@Override
	public void onPartyAttacked(Npc caller, Npc called, Creature target, int damage)
	{
		if (Rnd.get(3) < 1)
			called.getAI().addCastDesire(target, 4236, 1, 1000000);
		
		if (target instanceof Playable)
			called.getAI().addAttackDesire(target, (((damage * 1.0 / called.getStatus().getMaxHp()) / 0.05) * damage) * caller._weightPoint / 1000000);
		
		if (called.hasMaster() && called.getMaster().getStatus().getHpRatio() < 0.05 && called._i_ai0 == 0)
		{
			called._i_ai0 = 1;
			called.getAI().addCastDesire(called, 4235, 1, 1000000000);
			called.broadcastNpcSay(NpcStringId.ID_1000278);
		}
	}
	
	@Override
	public void onUseSkillFinished(Npc npc, Creature creature, L2Skill skill, boolean success)
	{
		if (skill.getId() == 4235)
		{
			npc.teleportTo(177134, -18807, -2263, 0);
			npc.removeAllAttackDesire();
			startQuestTimer("1002", npc, null, 3000);
		}
	}
	
	@Override
	public void onSeeSpell(Npc npc, Player caster, L2Skill skill, Creature[] targets, boolean isPet)
	{
		if (skill.getAggroPoints() > 0 && npc.getAI().getCurrentIntention().getType() == IntentionType.ATTACK && npc.getAI().getTopDesireTarget() == caster)
			npc.getAI().addAttackDesire(caster, (((skill.getAggroPoints() * 1.0 / npc.getStatus().getMaxHp()) / 0.05) * 150));
	}
	
	@Override
	public void onPartyDied(Npc caller, Npc called)
	{
		if (caller != called && called._i_ai1 != 1)
			caller.scheduleRespawn(300000);
	}
}
