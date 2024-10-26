package com.shnok.javaserver.gameserver.scripting.script.ai.individual.Monster.WarriorBase.Warrior.PartyLeaderWarrior;

import net.sf.l2j.gameserver.data.manager.SpawnManager;
import net.sf.l2j.gameserver.model.actor.Creature;
import net.sf.l2j.gameserver.model.actor.Npc;
import net.sf.l2j.gameserver.model.actor.Playable;
import net.sf.l2j.gameserver.model.spawn.NpcMaker;

public class FreyaSecretaryLeader extends PartyLeaderWarriorAggressive
{
	public FreyaSecretaryLeader()
	{
		super("ai/individual/Monster/WarriorBase/Warrior/PartyLeaderWarrior");
	}
	
	public FreyaSecretaryLeader(String descr)
	{
		super(descr);
	}
	
	protected final int[] _npcIds =
	{
		22102 // freya_secretary_leader
	};
	
	@Override
	public void onCreated(Npc npc)
	{
		npc._i_ai1 = 0;
		
		super.onCreated(npc);
	}
	
	@Override
	public void onPartyAttacked(Npc caller, Npc called, Creature target, int damage)
	{
		if (caller != called && !called.isDead() && called._i_ai1 < 15 && caller.getStatus().getHpRatio() < 0.33)
		{
			createOnePrivateEx(called, 18327, caller.getX(), caller.getY(), caller.getZ(), 0, 0, true, 1000, target.getObjectId(), 0);
			createOnePrivateEx(called, 18327, caller.getX(), caller.getY(), caller.getZ(), 0, 0, true, 1000, target.getObjectId(), 0);
			
			caller.sendScriptEvent(10023, 0, 0);
			
			called._i_ai1++;
		}
		
		super.onPartyAttacked(caller, called, target, damage);
	}
	
	@Override
	public void onSeeCreature(Npc npc, Creature creature)
	{
		if (!(creature instanceof Playable))
			return;
		
		if (npc.isInMyTerritory())
			npc.getAI().addAttackDesire(creature, 200);
		
		super.onSeeCreature(npc, creature);
	}
	
	@Override
	public void onPartyDied(Npc caller, Npc called)
	{
		// Do nothing
	}
	
	@Override
	public void onMyDying(Npc npc, Creature killer)
	{
		final NpcMaker maker0 = SpawnManager.getInstance().getNpcMaker("schuttgart13_mb2314_05m1");
		if (maker0 != null)
			maker0.getMaker().onMakerScriptEvent("10005", maker0, 0, 0);
		
		broadcastScriptEvent(npc, 11036, 1, 8000);
	}
}