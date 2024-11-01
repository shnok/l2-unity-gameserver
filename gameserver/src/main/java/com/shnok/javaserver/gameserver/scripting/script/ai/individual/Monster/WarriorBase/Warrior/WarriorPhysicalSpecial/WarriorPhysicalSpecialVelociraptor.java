package com.shnok.javaserver.gameserver.scripting.script.ai.individual.Monster.WarriorBase.Warrior.WarriorPhysicalSpecial;

import com.shnok.javaserver.gameserver.model.actor.Creature;
import com.shnok.javaserver.gameserver.model.actor.Npc;
import com.shnok.javaserver.gameserver.skills.L2Skill;
import com.shnok.javaserver.gameserver.taskmanager.GameTimeTaskManager;

public class WarriorPhysicalSpecialVelociraptor extends WarriorPhysicalSpecialAggressive
{
	public WarriorPhysicalSpecialVelociraptor()
	{
		super("ai/individual/Monster/WarriorBase/Warrior/WarriorPhysicalSpecial");
	}
	
	public WarriorPhysicalSpecialVelociraptor(String descr)
	{
		super(descr);
	}
	
	protected final int[] _npcIds =
	{
		22218
	};
	
	@Override
	public void onCreated(Npc npc)
	{
		npc._i_ai4 = GameTimeTaskManager.getInstance().getCurrentTick();
		npc._i_quest0 = 0;
		
		super.onCreated(npc);
	}
	
	@Override
	public void onScriptEvent(Npc npc, int eventId, int arg1, int arg2)
	{
		if (eventId == 11049)
		{
			npc._i_quest0 = 1;
		}
		super.onScriptEvent(npc, eventId, arg1, arg2);
	}
	
	@Override
	public void onAttacked(Npc npc, Creature attacker, int damage, L2Skill skill)
	{
		npc._i_ai4 = GameTimeTaskManager.getInstance().getCurrentTick();
		
		super.onAttacked(npc, attacker, damage, skill);
	}
	
	@Override
	public void onSeeCreature(Npc npc, Creature creature)
	{
		npc._i_ai4 = GameTimeTaskManager.getInstance().getCurrentTick();
		
		super.onSeeCreature(npc, creature);
	}
	
	@Override
	public void onNoDesire(Npc npc)
	{
		if (npc._i_quest0 == 1 && getElapsedTicks(npc._i_ai4) > 600)
			broadcastScriptEvent(npc, 11051, 0, 8000);
	}
}