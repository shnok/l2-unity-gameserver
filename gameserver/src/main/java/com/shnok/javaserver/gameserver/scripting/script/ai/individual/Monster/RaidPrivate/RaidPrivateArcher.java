package com.shnok.javaserver.gameserver.scripting.script.ai.individual.Monster.RaidPrivate;

import net.sf.l2j.commons.random.Rnd;

import net.sf.l2j.Config;
import net.sf.l2j.gameserver.enums.actors.NpcSkillType;
import net.sf.l2j.gameserver.model.actor.Creature;
import net.sf.l2j.gameserver.model.actor.Npc;
import net.sf.l2j.gameserver.model.actor.Playable;
import net.sf.l2j.gameserver.model.actor.Player;
import net.sf.l2j.gameserver.skills.L2Skill;

public class RaidPrivateArcher extends RaidPrivateStandard
{
	public RaidPrivateArcher()
	{
		super("ai/individual/Monster/RaidPrivate/RaidPrivateArcher");
	}
	
	public RaidPrivateArcher(String descr)
	{
		super(descr);
	}
	
	protected final int[] _npcIds =
	{
		25005,
		25011,
		25027,
		25036,
		25042,
		25058,
		25068,
		25086,
		25107,
		25116,
		25132,
		25147,
		25150,
		25171,
		25186,
		25212,
		25218,
		25257,
		25261,
		25270,
		25300,
		25344,
		25364,
		25389,
		25413,
		25446,
		25477,
		25491,
		25502,
		29098
	};
	
	@Override
	public void onCreated(Npc npc)
	{
		npc._i_ai2 = 0;
		
		super.onCreated(npc);
	}
	
	@Override
	public void onAttacked(Npc npc, Creature attacker, int damage, L2Skill skill)
	{
		if (npc._i_ai2 == 0 && npc.distance2D(attacker) < 100)
		{
			startQuestTimer("100002", npc, null, 2000);
			
			npc._i_ai2 = 1;
			npc._c_ai1 = attacker;
		}
		super.onAttacked(npc, attacker, damage, skill);
	}
	
	@Override
	public String onTimer(String name, Npc npc, Player player)
	{
		if (name.equalsIgnoreCase("100002"))
		{
			npc.getAI().addFleeDesire(npc._c_ai1, Config.MAX_DRIFT_RANGE, 10000);
			npc._i_ai2 = 0;
		}
		return super.onTimer(name, npc, player);
	}
	
	@Override
	public void onPartyAttacked(Npc caller, Npc called, Creature target, int damage)
	{
		if (target instanceof Playable)
		{
			final Creature topDesireTarget = called.getAI().getTopDesireTarget();
			if (topDesireTarget != null)
			{
				if (target == topDesireTarget && Rnd.get(20) < 1)
				{
					final L2Skill longRangePhysicalSpecial_a = getNpcSkillByType(called, NpcSkillType.LONG_RANGE_PHYSICAL_SPECIAL_A);
					called.getAI().addCastDesire(target, longRangePhysicalSpecial_a, 1000000);
				}
			}
		}
		super.onPartyAttacked(caller, called, target, damage);
	}
	
	@Override
	public void onSeeSpell(Npc npc, Player caster, L2Skill skill, Creature[] targets, boolean isPet)
	{
		final Creature topDesireTarget = npc.getAI().getTopDesireTarget();
		if (topDesireTarget != null && caster == topDesireTarget && Rnd.get(20) < 1)
		{
			final L2Skill longRangePhysicalSpecial_a = getNpcSkillByType(npc, NpcSkillType.LONG_RANGE_PHYSICAL_SPECIAL_A);
			npc.getAI().addCastDesire(caster, longRangePhysicalSpecial_a, 1000000);
		}
		super.onSeeSpell(npc, caster, skill, targets, isPet);
	}
}