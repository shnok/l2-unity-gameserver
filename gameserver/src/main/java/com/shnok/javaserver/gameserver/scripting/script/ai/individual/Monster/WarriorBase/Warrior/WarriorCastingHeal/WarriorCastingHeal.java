package com.shnok.javaserver.gameserver.scripting.script.ai.individual.Monster.WarriorBase.Warrior.WarriorCastingHeal;

import com.shnok.javaserver.commons.random.Rnd;

import com.shnok.javaserver.gameserver.enums.IntentionType;
import com.shnok.javaserver.gameserver.enums.actors.NpcSkillType;
import com.shnok.javaserver.gameserver.model.actor.Creature;
import com.shnok.javaserver.gameserver.model.actor.Npc;
import com.shnok.javaserver.gameserver.scripting.script.ai.individual.Monster.WarriorBase.Warrior.Warrior;
import com.shnok.javaserver.gameserver.skills.L2Skill;

public class WarriorCastingHeal extends Warrior
{
	public WarriorCastingHeal()
	{
		super("ai/individual/Monster/WarriorBase/Warrior/WarriorCastingHeal");
	}
	
	public WarriorCastingHeal(String descr)
	{
		super(descr);
	}
	
	protected final int[] _npcIds =
	{
		20686
	};
	
	@Override
	public void onAttacked(Npc npc, Creature attacker, int damage, L2Skill skill)
	{
		if (Rnd.get(100) < 33 && npc.getStatus().getHpRatio() < 0.7)
			npc.getAI().addCastDesire(npc, getNpcSkillByType(npc, NpcSkillType.MAGIC_HEAL), 1000000);
		
		super.onAttacked(npc, attacker, damage, skill);
	}
	
	@Override
	public void onClanAttacked(Npc caller, Npc called, Creature attacker, int damage, L2Skill skill)
	{
		if (Rnd.get(100) < 33 && called.getAI().getCurrentIntention().getType() != IntentionType.ATTACK)
			called.getAI().addCastDesire(caller, getNpcSkillByType(called, NpcSkillType.MAGIC_HEAL), 1000000);
		
		super.onClanAttacked(caller, called, attacker, damage, skill);
	}
}