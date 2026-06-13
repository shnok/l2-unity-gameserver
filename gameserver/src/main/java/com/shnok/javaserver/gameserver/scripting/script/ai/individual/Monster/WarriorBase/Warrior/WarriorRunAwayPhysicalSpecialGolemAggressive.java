package com.shnok.javaserver.gameserver.scripting.script.ai.individual.Monster.WarriorBase.Warrior;

import com.shnok.javaserver.commons.random.Rnd;

import com.shnok.javaserver.Config;
import com.shnok.javaserver.gameserver.enums.actors.NpcSkillType;
import com.shnok.javaserver.gameserver.model.actor.Creature;
import com.shnok.javaserver.gameserver.model.actor.Npc;
import com.shnok.javaserver.gameserver.model.actor.Playable;
import com.shnok.javaserver.gameserver.model.actor.Player;
import com.shnok.javaserver.gameserver.skills.L2Skill;

public class WarriorRunAwayPhysicalSpecialGolemAggressive extends Warrior
{
	public WarriorRunAwayPhysicalSpecialGolemAggressive()
	{
		super("ai/individual/Monster/WarriorBase/Warrior");
	}
	
	public WarriorRunAwayPhysicalSpecialGolemAggressive(String descr)
	{
		super(descr);
	}
	
	protected final int[] _npcIds =
	{
		22050,
		22056
	};
	
	@Override
	public void onCreated(Npc npc)
	{
		npc._i_ai1 = 0;
		
		super.onCreated(npc);
	}
	
	@Override
	public void onSeeCreature(Npc npc, Creature creature)
	{
		if (!(creature instanceof Playable))
			return;
		
		tryToAttack(npc, creature);
		
		if (npc.distance2D(creature) > 200)
			npc.getAI().addCastDesire(creature, getNpcSkillByType(npc, NpcSkillType.LONG_RANGE_PHYSICAL_SPECIAL), 1000000);
		
		startQuestTimer("1002", npc, null, 5000);
	}
	
	@Override
	public void onAttacked(Npc npc, Creature attacker, int damage, L2Skill skill)
	{
		npc._c_ai1 = attacker;
		
		if (npc._i_ai1 == 0 && npc.distance2D(attacker) < 100)
		{
			npc._i_ai1 = 1;
			startQuestTimer("1003", npc, null, 2000);
		}
		
		if (Rnd.get(100) < 33)
			npc.getAI().addCastDesire(attacker, getNpcSkillByType(npc, NpcSkillType.PHYSICAL_SPECIAL), 1000000);
		
		if (npc.distance2D(attacker) > 200)
			npc.getAI().addCastDesire(attacker, getNpcSkillByType(npc, NpcSkillType.LONG_RANGE_PHYSICAL_SPECIAL), 1000000);
		
		super.onAttacked(npc, attacker, damage, skill);
	}
	
	@Override
	public String onTimer(String name, Npc npc, Player player)
	{
		if (name.equalsIgnoreCase("1002"))
		{
			if (npc._c_ai1 != null && npc.distance2D(npc._c_ai1) > 200)
				npc.getAI().addCastDesire(npc._c_ai1, getNpcSkillByType(npc, NpcSkillType.LONG_RANGE_PHYSICAL_SPECIAL), 1000000);
			
			startQuestTimer("1002", npc, null, 5000);
		}
		else if (name.equalsIgnoreCase("1003"))
		{
			npc.getAI().addFleeDesire(npc._c_ai1, Config.MAX_DRIFT_RANGE, 1000);
			npc._i_ai1 = 0;
		}
		return null;
	}
}