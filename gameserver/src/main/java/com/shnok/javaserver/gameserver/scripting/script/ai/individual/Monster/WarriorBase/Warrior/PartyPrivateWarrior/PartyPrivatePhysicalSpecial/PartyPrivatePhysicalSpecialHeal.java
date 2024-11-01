package com.shnok.javaserver.gameserver.scripting.script.ai.individual.Monster.WarriorBase.Warrior.PartyPrivateWarrior.PartyPrivatePhysicalSpecial;

import com.shnok.javaserver.commons.random.Rnd;

import com.shnok.javaserver.gameserver.enums.actors.NpcSkillType;
import com.shnok.javaserver.gameserver.model.actor.Creature;
import com.shnok.javaserver.gameserver.model.actor.Npc;
import com.shnok.javaserver.gameserver.skills.L2Skill;

public class PartyPrivatePhysicalSpecialHeal extends PartyPrivatePhysicalSpecial
{
	public PartyPrivatePhysicalSpecialHeal()
	{
		super("ai/individual/Monster/WarriorBase/Warrior/PartyPrivateWarrior/PartyPrivatePhysicalSpecial");
	}
	
	public PartyPrivatePhysicalSpecialHeal(String descr)
	{
		super(descr);
	}
	
	protected final int[] _npcIds =
	{
		22122
	};
	
	@Override
	public void onAttacked(Npc npc, Creature attacker, int damage, L2Skill skill)
	{
		if (npc.getStatus().getHpRatio() <= 0.5 && Rnd.get(100) < 33)
			npc.getAI().addCastDesire(npc, getNpcSkillByType(npc, NpcSkillType.MAGIC_HEAL), 1000000);
		
		super.onAttacked(npc, attacker, damage, skill);
	}
	
	@Override
	public void onPartyAttacked(Npc caller, Npc called, Creature target, int damage)
	{
		if (caller.getStatus().getHpRatio() <= 0.5 && called != caller && Rnd.get(100) < 33)
			called.getAI().addCastDesire(caller, getNpcSkillByType(called, NpcSkillType.MAGIC_HEAL), 1000000);
		
		if (called.hasMaster() && called.getMaster().getStatus().getHpRatio() <= 0.5 && Rnd.get(100) < 33)
			called.getAI().addCastDesire(called.getMaster(), getNpcSkillByType(called, NpcSkillType.MAGIC_HEAL), 1000000);
		
		super.onPartyAttacked(caller, called, target, damage);
	}
}
