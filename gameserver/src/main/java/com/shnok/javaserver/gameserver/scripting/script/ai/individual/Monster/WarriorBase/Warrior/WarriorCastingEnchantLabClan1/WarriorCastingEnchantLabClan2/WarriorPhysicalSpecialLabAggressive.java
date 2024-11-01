package com.shnok.javaserver.gameserver.scripting.script.ai.individual.Monster.WarriorBase.Warrior.WarriorCastingEnchantLabClan1.WarriorCastingEnchantLabClan2;

import com.shnok.javaserver.commons.random.Rnd;

import com.shnok.javaserver.gameserver.enums.actors.NpcSkillType;
import com.shnok.javaserver.gameserver.model.actor.Creature;
import com.shnok.javaserver.gameserver.model.actor.Npc;
import com.shnok.javaserver.gameserver.model.actor.Playable;
import com.shnok.javaserver.gameserver.skills.L2Skill;

public class WarriorPhysicalSpecialLabAggressive extends WarriorCastingEnchantLabClan2
{
	public WarriorPhysicalSpecialLabAggressive()
	{
		super("ai/individual/Monster/WarriorBase/Warrior/WarriorCastingEnchantLabClan1/WarriorCastingEnchantLabClan2");
	}
	
	public WarriorPhysicalSpecialLabAggressive(String descr)
	{
		super(descr);
	}
	
	protected final int[] _npcIds =
	{
		22059,
		22064,
		22065,
		22076,
		22077,
		22078
	};
	
	@Override
	public void onSeeCreature(Npc npc, Creature creature)
	{
		if (!(creature instanceof Playable))
			return;
		
		tryToAttack(npc, creature);
		
		super.onSeeCreature(npc, creature);
	}
	
	@Override
	public void onAttacked(Npc npc, Creature attacker, int damage, L2Skill skill)
	{
		if (Rnd.get(100) < 33)
			npc.getAI().addCastDesire(attacker, getNpcSkillByType(npc, NpcSkillType.SELF_RANGE_PHYSICAL_SPECIAL), 1000000);
		
		super.onAttacked(npc, attacker, damage, skill);
	}
}
