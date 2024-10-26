package com.shnok.javaserver.gameserver.scripting.script.ai.individual.Monster.WarriorBase.Warrior.WarriorCastingHeal;

import net.sf.l2j.commons.random.Rnd;

import net.sf.l2j.gameserver.enums.IntentionType;
import net.sf.l2j.gameserver.enums.actors.NpcSkillType;
import net.sf.l2j.gameserver.model.actor.Creature;
import net.sf.l2j.gameserver.model.actor.Npc;
import net.sf.l2j.gameserver.scripting.script.ai.individual.Monster.WarriorBase.Warrior.Warrior;
import net.sf.l2j.gameserver.skills.L2Skill;

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