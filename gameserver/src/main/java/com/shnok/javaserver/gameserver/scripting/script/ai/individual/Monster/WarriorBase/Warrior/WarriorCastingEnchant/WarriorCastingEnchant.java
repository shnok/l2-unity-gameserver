package com.shnok.javaserver.gameserver.scripting.script.ai.individual.Monster.WarriorBase.Warrior.WarriorCastingEnchant;

import com.shnok.javaserver.commons.random.Rnd;

import com.shnok.javaserver.gameserver.enums.actors.NpcSkillType;
import com.shnok.javaserver.gameserver.model.actor.Creature;
import com.shnok.javaserver.gameserver.model.actor.Npc;
import com.shnok.javaserver.gameserver.model.actor.Playable;
import com.shnok.javaserver.gameserver.scripting.script.ai.individual.Monster.WarriorBase.Warrior.Warrior;
import com.shnok.javaserver.gameserver.skills.L2Skill;

public class WarriorCastingEnchant extends Warrior
{
	public WarriorCastingEnchant()
	{
		super("ai/individual/Monster/WarriorBase/Warrior/WarriorCastingEnchant");
	}
	
	public WarriorCastingEnchant(String descr)
	{
		super(descr);
	}
	
	protected final int[] _npcIds =
	{
		20681,
		21138,
		21168,
		21171,
		21174,
		21189,
		21192,
		21195,
		20569,
		20552,
		20550,
		20675,
		20586,
		22004,
		22085
	};
	
	@Override
	public void onCreated(Npc npc)
	{
		npc._i_ai0 = 0;
		npc._i_ai1 = 0;
		
		super.onCreated(npc);
	}
	
	@Override
	public void onAttacked(Npc npc, Creature attacker, int damage, L2Skill skill)
	{
		if (attacker instanceof Playable && npc._i_ai1 == 0 && Rnd.get(100) < 33 && npc.getStatus().getHpRatio() > 0.5)
		{
			npc.getAI().addCastDesire(npc, getNpcSkillByType(npc, NpcSkillType.BUFF), 1000000);
			npc._i_ai1 = 1;
		}
		super.onAttacked(npc, attacker, damage, skill);
	}
}
