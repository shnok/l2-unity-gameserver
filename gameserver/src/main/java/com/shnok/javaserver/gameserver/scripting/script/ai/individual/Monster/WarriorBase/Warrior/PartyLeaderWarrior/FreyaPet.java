package com.shnok.javaserver.gameserver.scripting.script.ai.individual.Monster.WarriorBase.Warrior.PartyLeaderWarrior;

import com.shnok.javaserver.commons.random.Rnd;

import com.shnok.javaserver.gameserver.data.manager.SpawnManager;
import com.shnok.javaserver.gameserver.enums.actors.ClassId;
import com.shnok.javaserver.gameserver.enums.actors.NpcSkillType;
import com.shnok.javaserver.gameserver.model.actor.Creature;
import com.shnok.javaserver.gameserver.model.actor.Npc;
import com.shnok.javaserver.gameserver.model.actor.Player;
import com.shnok.javaserver.gameserver.model.spawn.NpcMaker;
import com.shnok.javaserver.gameserver.network.NpcStringId;
import com.shnok.javaserver.gameserver.skills.L2Skill;

public class FreyaPet extends PartyLeaderWarriorAggressive
{
	public FreyaPet()
	{
		super("ai/individual/Monster/WarriorBase/Warrior/PartyLeaderWarrior");
	}
	
	public FreyaPet(String descr)
	{
		super(descr);
	}
	
	protected final int[] _npcIds =
	{
		22104 // freya_pet
	};
	
	@Override
	public void onCreated(Npc npc)
	{
		npc._c_ai0 = null;
		npc._c_ai1 = null;
		npc._c_ai2 = null;
		
		super.onCreated(npc);
	}
	
	@Override
	public void onSeeCreature(Npc npc, Creature creature)
	{
		if (creature instanceof Player playerCreature && ClassId.isInGroup(playerCreature, "@cleric_group"))
		{
			if (npc._c_ai0 == null)
				npc._c_ai0 = creature;
			else if (npc._c_ai1 == null)
				npc._c_ai1 = creature;
			else if (npc._c_ai2 == null)
				npc._c_ai2 = creature;
		}
		super.onSeeCreature(npc, creature);
	}
	
	@Override
	public void onAttacked(Npc npc, Creature attacker, int damage, L2Skill skill)
	{
		if (Rnd.get(100) < 5)
			npc.getAI().addCastDesire(npc, getNpcSkillByType(npc, NpcSkillType.SELF_BUFF), 1000000);
		
		if (Rnd.get(100) < 3)
		{
			if (npc.getAI().getTopDesireTarget() == attacker)
				npc.broadcastNpcSay(NpcStringId.ID_1000399, attacker.getName());
			
			npc.getAI().getAggroList().cleanAllHate();
			npc.getAI().addAttackDesire(attacker, 1000000);
			
			npc._flag = attacker.getObjectId();
			
			broadcastScriptEvent(npc, 10002, npc.getObjectId(), 2000);
		}
		
		if (npc.getStatus().getHpRatio() < 0.8)
			broadcastScriptEvent(npc, 10034, 0, 2000);
		
		super.onAttacked(npc, attacker, damage, skill);
	}
	
	@Override
	public void onPartyDied(Npc caller, Npc called)
	{
		if (caller != called)
		{
			called.lookNeighbor();
			
			if (called._c_ai0 != null && !called._c_ai0.isDead())
			{
				called.broadcastNpcSay(NpcStringId.ID_1000399, called._c_ai0.getName());
				
				called.getAI().getAggroList().cleanAllHate();
				called.getAI().addAttackDesire(called._c_ai0, 1000000);
				
				called._flag = called._c_ai0.getObjectId();
				
				broadcastScriptEvent(called, 10002, called.getObjectId(), 500);
			}
			
			if (called._c_ai1 != null && !called._c_ai1.isDead())
			{
				called.broadcastNpcSay(NpcStringId.ID_1000399, called._c_ai1.getName());
				
				called.getAI().getAggroList().cleanAllHate();
				called.getAI().addAttackDesire(called._c_ai1, 1000000);
				
				called._flag = called._c_ai1.getObjectId();
				
				broadcastScriptEvent(called, 10002, called.getObjectId(), 500);
			}
			
			if (called._c_ai2 != null && !called._c_ai2.isDead())
			{
				called.broadcastNpcSay(NpcStringId.ID_1000399, called._c_ai2.getName());
				
				called.getAI().getAggroList().cleanAllHate();
				called.getAI().addAttackDesire(called._c_ai2, 1000000);
				
				called._flag = called._c_ai2.getObjectId();
				
				broadcastScriptEvent(called, 10002, called.getObjectId(), 500);
			}
		}
	}
	
	@Override
	public void onMyDying(Npc npc, Creature killer)
	{
		final NpcMaker maker0 = SpawnManager.getInstance().getNpcMaker("schuttgart13_mb2314_05m1");
		if (maker0 != null)
			maker0.getMaker().onMakerScriptEvent("10005", maker0, 0, 0);
		
		broadcastScriptEvent(npc, 11036, 2, 7000);
	}
}