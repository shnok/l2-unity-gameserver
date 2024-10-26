package com.shnok.javaserver.gameserver.scripting.script.ai.individual.Monster.RaidPrivate;

import net.sf.l2j.commons.random.Rnd;

import net.sf.l2j.Config;
import net.sf.l2j.gameserver.enums.ZoneId;
import net.sf.l2j.gameserver.enums.actors.NpcSkillType;
import net.sf.l2j.gameserver.model.actor.Creature;
import net.sf.l2j.gameserver.model.actor.Npc;
import net.sf.l2j.gameserver.model.actor.Playable;
import net.sf.l2j.gameserver.model.actor.Player;
import net.sf.l2j.gameserver.skills.L2Skill;

public class RaidPrivateWizard extends RaidPrivateStandard
{
	public RaidPrivateWizard()
	{
		super("ai/individual/Monster/RaidPrivate/RaidPrivateWizard");
	}
	
	public RaidPrivateWizard(String descr)
	{
		super(descr);
	}
	
	protected final int[] _npcIds =
	{
		25002,
		25033,
		25061,
		25071,
		25083,
		25090,
		25096,
		25110,
		25113,
		25120,
		25129,
		25138,
		25156,
		25180,
		25190,
		25200,
		25209,
		25215,
		25221,
		25224,
		25242,
		25250,
		25253,
		25264,
		25274,
		25284,
		25291,
		25297,
		25303,
		25320,
		25323,
		25329,
		25330,
		25331,
		25345,
		25347,
		25350,
		25399,
		25403,
		25416,
		25421,
		25436,
		25440,
		25451,
		25454,
		25471,
		25479,
		25488,
		29034,
		29038,
		29044,
		25508,
		29063,
		25515
	};
	
	@Override
	public void onCreated(Npc npc)
	{
		npc._c_ai0 = npc;
		
		super.onCreated(npc);
	}
	
	@Override
	public String onTimer(String name, Npc npc, Player player)
	{
		if (name.equalsIgnoreCase("1001"))
		{
			if (npc._c_ai0 != null && npc._c_ai0 != npc && npc.distance2D(npc._c_ai0) < 200)
				npc.getAI().addFleeDesire(npc._c_ai0, Config.MAX_DRIFT_RANGE, 100000000);
		}
		
		return super.onTimer(name, npc, player);
	}
	
	@Override
	public void onAttacked(Npc npc, Creature attacker, int damage, L2Skill skill)
	{
		npc._c_ai0 = attacker;
		
		if (npc.hasMaster() && npc.getMaster().isDead())
		{
			if (attacker instanceof Playable && Rnd.get(30) < 1)
			{
				final L2Skill DDMagic_a = getNpcSkillByType(npc, NpcSkillType.DD_MAGIC_A);
				
				npc.getAI().addCastDesire(attacker, DDMagic_a, 1000000);
			}
			
			final L2Skill debuff_a = getNpcSkillByType(npc, NpcSkillType.DEBUFF_A);
			if (getAbnormalLevel(attacker, debuff_a) == -1 && Rnd.get(75) < 1)
				npc.getAI().addCastDesire(attacker, debuff_a, 1000000);
		}
		
		if (npc.isInsideZone(ZoneId.PEACE))
		{
			npc.abortAll(false);
			npc.instantTeleportTo(npc.getSpawnLocation(), 0);
			npc.removeAllAttackDesire();
		}
	}
	
	@Override
	public void onPartyAttacked(Npc caller, Npc called, Creature target, int damage)
	{
		final L2Skill DDMagic_a = getNpcSkillByType(called, NpcSkillType.DD_MAGIC_A);
		final L2Skill debuff_a = getNpcSkillByType(called, NpcSkillType.DEBUFF_A);
		
		if (target.getStatus().getLevel() >= (called.getStatus().getLevel() + 8))
		{
			if (target instanceof Playable && Rnd.get(300) < 1)
				called.getAI().addCastDesire(target, DDMagic_a, 1000000);
			
			if (getAbnormalLevel(target, debuff_a) == -1 && Rnd.get(750) < 1)
				called.getAI().addCastDesire(target, debuff_a, 1000000);
		}
		else if (target instanceof Playable && Rnd.get(30) < 1)
			called.getAI().addCastDesire(target, DDMagic_a, 1000000);
		
		if (getAbnormalLevel(target, debuff_a) == -1 && Rnd.get(75) < 1)
			called.getAI().addCastDesire(target, debuff_a, 1000000);
		
		super.onPartyAttacked(caller, called, target, damage);
	}
	
	@Override
	public void onSeeSpell(Npc npc, Player caster, L2Skill skill, Creature[] targets, boolean isPet)
	{
		final L2Skill DDMagic_a = getNpcSkillByType(npc, NpcSkillType.DD_MAGIC_A);
		if (Rnd.get(30) < 1)
			npc.getAI().addCastDesire(caster, DDMagic_a, 1000000);
		
		final L2Skill debuff_a = getNpcSkillByType(npc, NpcSkillType.DEBUFF_A);
		if (getAbnormalLevel(caster, debuff_a) == -1 && Rnd.get(75) < 1)
			npc.getAI().addCastDesire(caster, debuff_a, 1000000);
		
		super.onSeeSpell(npc, caster, skill, targets, isPet);
	}
}