package com.shnok.javaserver.gameserver.scripting.script.ai.individual.Monster.WizardBase.PartyPrivateWizard;

import com.shnok.javaserver.commons.random.Rnd;

import com.shnok.javaserver.gameserver.enums.actors.NpcSkillType;
import com.shnok.javaserver.gameserver.model.actor.Creature;
import com.shnok.javaserver.gameserver.model.actor.Npc;
import com.shnok.javaserver.gameserver.model.actor.Playable;
import com.shnok.javaserver.gameserver.skills.L2Skill;

public class PartyPrivateWizardDD2Heal extends PartyPrivateWizardDD2
{
	public PartyPrivateWizardDD2Heal()
	{
		super("ai/individual/Monster/WizardBase/PartyPrivateWizard");
	}
	
	public PartyPrivateWizardDD2Heal(String descr)
	{
		super(descr);
	}
	
	protected final int[] _npcIds =
	{
		21543,
		21546,
		21823
	};
	
	@Override
	public void onAttacked(Npc npc, Creature attacker, int damage, L2Skill skill)
	{
		super.onAttacked(npc, attacker, damage, skill);
		
		if (attacker instanceof Playable && Rnd.get(100) < 33 && npc.getStatus().getHpRatio() < 0.7)
			npc.getAI().addCastDesire(npc, getNpcSkillByType(npc, NpcSkillType.MAGIC_HEAL), 1000000);
	}
	
	@Override
	public void onPartyAttacked(Npc caller, Npc called, Creature target, int damage)
	{
		super.onPartyAttacked(caller, called, target, damage);
		
		if (target instanceof Playable && Rnd.get(100) < 33 && caller.getStatus().getHpRatio() < 0.7)
			called.getAI().addCastDesire(caller, getNpcSkillByType(called, NpcSkillType.MAGIC_HEAL), 1000000);
	}
	
	@Override
	public void onClanAttacked(Npc caller, Npc called, Creature attacker, int damage, L2Skill skill)
	{
		if (attacker instanceof Playable && called.getAI().getLifeTime() > 7 && called.getAI().getHateList().isEmpty() && Rnd.get(100) < 33)
			called.getAI().addCastDesire(caller, getNpcSkillByType(called, NpcSkillType.MAGIC_HEAL), 1000000);
		
		super.onClanAttacked(caller, called, attacker, damage, skill);
	}
}