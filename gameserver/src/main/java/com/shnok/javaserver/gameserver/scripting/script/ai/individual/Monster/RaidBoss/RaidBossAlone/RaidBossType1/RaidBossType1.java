package com.shnok.javaserver.gameserver.scripting.script.ai.individual.Monster.RaidBoss.RaidBossAlone.RaidBossType1;

import net.sf.l2j.commons.random.Rnd;

import net.sf.l2j.gameserver.enums.actors.NpcSkillType;
import net.sf.l2j.gameserver.model.actor.Creature;
import net.sf.l2j.gameserver.model.actor.Npc;
import net.sf.l2j.gameserver.model.actor.Playable;
import net.sf.l2j.gameserver.model.actor.Player;
import net.sf.l2j.gameserver.scripting.script.ai.individual.Monster.RaidBoss.RaidBossStandard;
import net.sf.l2j.gameserver.skills.L2Skill;

public class RaidBossType1 extends RaidBossStandard
{
	public RaidBossType1()
	{
		super("ai/individual/Monster/RaidBoss/RaidBossAlone/RaidBossType1");
	}
	
	public RaidBossType1(String descr)
	{
		super(descr);
	}
	
	protected final int[] _npcIds =
	{
		25019,
		25050,
		25063,
		25088,
		25098,
		25102,
		25118,
		25125,
		25127,
		25158,
		25162,
		25169,
		25188,
		25198,
		25229,
		25233,
		25234,
		25244,
		25248,
		25259,
		25272,
		25276,
		25280,
		25281,
		25282,
		25305,
		25315,
		25365,
		25372,
		25391,
		25394,
		25437,
		29036,
		25512,
		25527,
		25523,
		25255,
		25126,
		25163
	};
	
	@Override
	public String onTimer(String name, Npc npc, Player player)
	{
		if (name.equalsIgnoreCase("1001"))
		{
			if (Rnd.get(5) < 1)
			{
				final L2Skill selfBuff = getNpcSkillByType(npc, NpcSkillType.SELF_BUFF_A);
				npc.getAI().addCastDesire(npc, selfBuff, 1000000);
			}
		}
		return super.onTimer(name, npc, player);
	}
	
	@Override
	public void onAttacked(Npc npc, Creature attacker, int damage, L2Skill skill)
	{
		if (attacker instanceof Playable)
		{
			final Creature topDesireTarget = npc.getAI().getTopDesireTarget();
			final double distance = npc.distance2D(attacker);
			
			if (attacker == topDesireTarget && Rnd.get(15) < 1)
			{
				final L2Skill physicalSpecial_a = getNpcSkillByType(npc, NpcSkillType.PHYSICAL_SPECIAL_A);
				npc.getAI().addCastDesire(attacker, physicalSpecial_a, 1000000);
			}
			
			if (attacker != topDesireTarget && distance < 150 && Rnd.get(375) < 1)
			{
				final L2Skill selfRangePhysicalSpecial_a = getNpcSkillByType(npc, NpcSkillType.SELF_RANGE_PHYSICAL_SPECIAL_A);
				npc.getAI().addCastDesire(npc, selfRangePhysicalSpecial_a, 1000000);
			}
			
			if (distance < 150 && Rnd.get(750) < 1)
			{
				final L2Skill selfRangeCancel_a = getNpcSkillByType(npc, NpcSkillType.SELF_RANGE_CANCEL_A);
				npc.getAI().addCastDesire(npc, selfRangeCancel_a, 1000000);
			}
		}
		super.onAttacked(npc, attacker, damage, skill);
	}
	
	@Override
	public void onSeeSpell(Npc npc, Player caster, L2Skill skill, Creature[] targets, boolean isPet)
	{
		final Creature topDesireTarget = npc.getAI().getTopDesireTarget();
		final double distance = npc.distance2D(caster);
		
		if (caster == topDesireTarget && Rnd.get(15) < 1)
		{
			final L2Skill physicalSpecial_a = getNpcSkillByType(npc, NpcSkillType.PHYSICAL_SPECIAL_A);
			npc.getAI().addCastDesire(caster, physicalSpecial_a, 1000000);
		}
		
		if (caster != topDesireTarget && distance < 150 && Rnd.get(375) < 1)
		{
			final L2Skill selfRangePhysicalSpecial_a = getNpcSkillByType(npc, NpcSkillType.SELF_RANGE_PHYSICAL_SPECIAL_A);
			npc.getAI().addCastDesire(npc, selfRangePhysicalSpecial_a, 1000000);
		}
		
		if (distance < 150 && Rnd.get(750) < 1)
		{
			final L2Skill selfRangeCancel_a = getNpcSkillByType(npc, NpcSkillType.SELF_RANGE_CANCEL_A);
			npc.getAI().addCastDesire(npc, selfRangeCancel_a, 1000000);
		}
		super.onSeeSpell(npc, caster, skill, targets, isPet);
	}
}