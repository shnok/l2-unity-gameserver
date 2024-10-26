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

public class RaidPrivateHealer extends RaidPrivateStandard
{
	public RaidPrivateHealer()
	{
		super("ai/individual/Monster/RaidPrivate/RaidPrivateHealer");
	}
	
	public RaidPrivateHealer(String descr)
	{
		super(descr);
	}
	
	protected final int[] _npcIds =
	{
		25008,
		25017,
		25021,
		25030,
		25045,
		25052,
		25074,
		25080,
		25100,
		25135,
		25144,
		25160,
		25167,
		25174,
		25183,
		25203,
		25227,
		25236,
		25239,
		25278,
		25287,
		25292,
		25313,
		25321,
		25340,
		25343,
		25351,
		25359,
		25368,
		25377,
		25396,
		25400,
		25417,
		25422,
		25432,
		25449,
		25452,
		25455,
		25458,
		25465,
		25480,
		25483,
		29031,
		29039,
		29043,
		25525,
		29057,
		29064
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
			if (Rnd.get(3) < 1)
			{
				final L2Skill selfRangeBuff_a = getNpcSkillByType(npc, NpcSkillType.SELF_RANGE_BUFF_A);
				
				npc.getAI().addCastDesire(npc, selfRangeBuff_a, 1000000);
			}
		}
		
		return super.onTimer(name, npc, player);
	}
	
	@Override
	public void onAttacked(Npc npc, Creature attacker, int damage, L2Skill skill)
	{
		if (attacker instanceof Playable)
			npc._c_ai0 = attacker;
		
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
		if (target.getStatus().getLevel() > (called.getStatus().getLevel() + 8))
			called.getAI().addCastDesire(target, 4515, 1, 1000000);
		
		final L2Skill healMagic_a = getNpcSkillByType(called, NpcSkillType.HEAL_MAGIC_A);
		
		if (caller.getStatus().getHpRatio() < 0.5 && Rnd.get(3) < 1)
			called.getAI().addCastDesire(caller, healMagic_a, 1000000);
		
		called.getAI().addCastDesire(caller, healMagic_a, 100);
		
		if (called.isInsideZone(ZoneId.PEACE))
		{
			called.abortAll(false);
			called.instantTeleportTo(called.getSpawnLocation(), 0);
			called.removeAllAttackDesire();
		}
	}
	
	@Override
	public void onSeeSpell(Npc npc, Player caster, L2Skill skill, Creature[] targets, boolean isPet)
	{
		// Do nothing.
	}
	
	@Override
	public void onUseSkillFinished(Npc npc, Creature creature, L2Skill skill, boolean success)
	{
		final L2Skill healMagic_a = getNpcSkillByType(npc, NpcSkillType.HEAL_MAGIC_A);
		if (skill.getId() == healMagic_a.getId())
		{
			if (npc._c_ai0 != null && npc._c_ai0 != npc && npc.distance2D(npc._c_ai0) < 200)
				npc.getAI().addFleeDesire(npc._c_ai0, Config.MAX_DRIFT_RANGE, 100000000);
		}
	}
}