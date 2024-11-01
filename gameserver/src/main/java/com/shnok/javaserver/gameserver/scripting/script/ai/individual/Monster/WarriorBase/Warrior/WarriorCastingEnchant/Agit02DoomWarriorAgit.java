package com.shnok.javaserver.gameserver.scripting.script.ai.individual.Monster.WarriorBase.Warrior.WarriorCastingEnchant;

import com.shnok.javaserver.commons.random.Rnd;

import com.shnok.javaserver.gameserver.model.actor.Creature;
import com.shnok.javaserver.gameserver.model.actor.Npc;
import com.shnok.javaserver.gameserver.model.actor.Playable;
import com.shnok.javaserver.gameserver.model.actor.Player;
import com.shnok.javaserver.gameserver.skills.L2Skill;

public class Agit02DoomWarriorAgit extends WarriorCastingEnchantClanAggressive
{
	public Agit02DoomWarriorAgit()
	{
		super("ai/individual/Monster/WarriorBase/Warrior/WarriorCastingEnchant");
	}
	
	public Agit02DoomWarriorAgit(String descr)
	{
		super(descr);
	}
	
	protected final int[] _npcIds =
	{
		35415,
		35636
	};
	
	@Override
	public void onNoDesire(Npc npc)
	{
		// Do nothing
	}
	
	@Override
	public void onCreated(Npc npc)
	{
		startQuestTimerAtFixedRate("4001", npc, null, 1000, 60000);
		npc._flag = 0;
		
		super.onCreated(npc);
	}
	
	@Override
	public String onTimer(String name, Npc npc, Player player)
	{
		if (name.equalsIgnoreCase("4001"))
		{
			if (!npc.isInMyTerritory() && Rnd.get(3) < 1)
			{
				npc.teleportTo(npc.getSpawnLocation(), 0);
				npc.removeAllAttackDesire();
			}
			if (Rnd.get(5) < 1)
				npc.getAI().getAggroList().randomizeAttack();
		}
		
		return super.onTimer(name, npc, player);
	}
	
	@Override
	public void onClanAttacked(Npc caller, Npc called, Creature attacker, int damage, L2Skill skill)
	{
		if (called._flag == caller._flag && attacker instanceof Playable)
			called.getAI().addAttackDesire(attacker, (((damage * 1.0) / called.getStatus().getMaxHp()) / 0.05) * 100);
	}
}
