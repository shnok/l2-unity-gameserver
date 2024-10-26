package com.shnok.javaserver.gameserver.scripting.script.ai.individual.Monster.WarriorBase.Warrior.WarriorCastDDMagic;

import net.sf.l2j.commons.random.Rnd;

import net.sf.l2j.gameserver.enums.IntentionType;
import net.sf.l2j.gameserver.enums.actors.NpcSkillType;
import net.sf.l2j.gameserver.model.actor.Creature;
import net.sf.l2j.gameserver.model.actor.Npc;
import net.sf.l2j.gameserver.model.actor.Playable;
import net.sf.l2j.gameserver.model.actor.Player;
import net.sf.l2j.gameserver.skills.L2Skill;

public class Agit02DoomServantAgit extends WarriorCastDDMagicAggressive
{
	public Agit02DoomServantAgit()
	{
		super("ai/individual/Monster/WarriorBase/Warrior/WarriorCastDDMagic");
	}
	
	public Agit02DoomServantAgit(String descr)
	{
		super(descr);
	}
	
	protected final int[] _npcIds =
	{
		35632,
		35411
	};
	
	@Override
	public void onNoDesire(Npc npc)
	{
		// Do nothing
	}
	
	@Override
	public void onCreated(Npc npc)
	{
		startQuestTimerAtFixedRate("3001", npc, null, 1000, 60000);
		npc._flag = 0;
		
		super.onCreated(npc);
	}
	
	@Override
	public String onTimer(String name, Npc npc, Player player)
	{
		if (name.equalsIgnoreCase("3001"))
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
		if ((called.getAI().getLifeTime() > 7 && (attacker instanceof Playable)) && called.getAI().getCurrentIntention().getType() != IntentionType.ATTACK && called._flag == caller._flag)
		{
			final L2Skill ddMagic = getNpcSkillByType(called, NpcSkillType.DD_MAGIC);
			
			if (Rnd.get(100) < 10 && called.distance2D(attacker) > 100)
				called.getAI().addCastDesireHold(attacker, ddMagic, 1000000);
		}
		
		if (called._flag == caller._flag && attacker instanceof Playable)
			called.getAI().addAttackDesire(attacker, (((damage * 1.0) / called.getStatus().getMaxHp()) / 0.05) * 100);
	}
}
