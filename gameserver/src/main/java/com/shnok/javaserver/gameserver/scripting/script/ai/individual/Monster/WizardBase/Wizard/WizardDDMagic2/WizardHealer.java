package com.shnok.javaserver.gameserver.scripting.script.ai.individual.Monster.WizardBase.Wizard.WizardDDMagic2;

import com.shnok.javaserver.commons.random.Rnd;

import com.shnok.javaserver.gameserver.enums.actors.NpcSkillType;
import com.shnok.javaserver.gameserver.model.actor.Creature;
import com.shnok.javaserver.gameserver.model.actor.Npc;
import com.shnok.javaserver.gameserver.skills.L2Skill;

public class WizardHealer extends WizardDDMagic2
{
	public WizardHealer()
	{
		super("ai/individual/Monster/WizardBase/Wizard/WizardDDMagic2");
	}
	
	public WizardHealer(String descr)
	{
		super(descr);
	}
	
	protected final int[] _npcIds =
	{
		21300,
		21305
	};
	
	@Override
	public void onCreated(Npc npc)
	{
		final L2Skill buff = getNpcSkillByType(npc, NpcSkillType.BUFF);
		npc.getAI().addCastDesire(npc, buff, 1000000);
		
		npc._i_ai1 = 0;
		npc._i_ai2 = 0;
	}
	
	@Override
	public void onAttacked(Npc npc, Creature attacker, int damage, L2Skill skill)
	{
		final double hpRatio = npc.getStatus().getHpRatio();
		if (Rnd.get(100) < 33 && hpRatio > 0.5)
		{
			if (npc._i_ai2 == 0)
			{
				npc.getAI().addFleeDesire(attacker, 500, 1000000);
				
				final Creature topDesireTarget = npc.getAI().getTopDesireTarget();
				if (npc.getMove().getGeoPathFailCount() > 3 && attacker == topDesireTarget && hpRatio != 1.0)
					npc._i_ai2 = 1;
			}
			else
				super.onAttacked(npc, attacker, damage, skill);
		}
		else
			super.onAttacked(npc, attacker, damage, skill);
	}
	
	@Override
	public void onClanAttacked(Npc caller, Npc called, Creature attacker, int damage, L2Skill skill)
	{
		final int i0 = Rnd.get(100);
		
		if (caller.getStatus().getHpRatio() > 0.5)
		{
			if (i0 < 33)
			{
				final L2Skill buff = getNpcSkillByType(called, NpcSkillType.BUFF);
				called.getAI().addCastDesire(caller, buff, 1000000);
				
				final L2Skill debuff = getNpcSkillByType(called, NpcSkillType.DEBUFF);
				called.getAI().addCastDesire(attacker, debuff, 1000000);
			}
		}
		else if (i0 < 50)
		{
			final L2Skill heal = getNpcSkillByType(called, NpcSkillType.HEAL);
			called.getAI().addCastDesire(caller, heal, 1000000);
		}
		super.onClanAttacked(caller, called, attacker, damage, skill);
	}
	
	@Override
	public void onClanDied(Npc caller, Npc called, Creature killer)
	{
		called.getAI().addFleeDesire(killer, 500, 1000000);
	}
}