package com.shnok.javaserver.gameserver.scripting.script.ai.individual.Monster.RaidBoss.RaidBossParty;

import net.sf.l2j.commons.random.Rnd;

import net.sf.l2j.gameserver.enums.actors.NpcSkillType;
import net.sf.l2j.gameserver.model.actor.Creature;
import net.sf.l2j.gameserver.model.actor.Npc;
import net.sf.l2j.gameserver.model.actor.Playable;
import net.sf.l2j.gameserver.model.actor.Player;
import net.sf.l2j.gameserver.skills.L2Skill;

public class RaidBossType4 extends RaidBossParty
{
	public RaidBossType4()
	{
		super("ai/individual/Monster/RaidBoss/RaidBossAlone/RaidBossParty/RaidBossType4");
	}
	
	public RaidBossType4(String descr)
	{
		super(descr);
	}
	
	protected final int[] _npcIds =
	{
		25319,
		25325,
		25290,
		25306,
		25293,
		25349,
		25140,
		25054,
		25245,
		25453,
		25252,
		25309,
		25316,
		25013,
		25023,
		25038,
		25047,
		25064,
		25076,
		25092,
		25103,
		25122,
		25152,
		25176,
		25192,
		25205,
		25230,
		25266,
		25354,
		25369,
		25380,
		25385,
		25398,
		25415,
		25420,
		25423,
		25441,
		25450,
		25467,
		25478,
		25493,
		25498,
		29037,
		25509,
		29062
	};
	
	@Override
	public void onPartyAttacked(Npc caller, Npc called, Creature target, int damage)
	{
		if (target instanceof Playable)
		{
			final Creature topDesireTarget = called.getAI().getTopDesireTarget();
			if (topDesireTarget != null)
			{
				if (target != topDesireTarget && called.distance2D(target) < 150 && called.distance2D(topDesireTarget) < 150)
				{
					final L2Skill selfRangeDebuff_a = getNpcSkillByType(called, NpcSkillType.SELF_RANGE_DEBUFF_A);
					if (getAbnormalLevel(target, selfRangeDebuff_a) == -1 && getAbnormalLevel(topDesireTarget, selfRangeDebuff_a) == -1 && Rnd.get(2) < 1)
						called.getAI().addCastDesire(called, selfRangeDebuff_a, 1000000);
					
					final L2Skill selfRangeDebuffAnother_a = getNpcSkillByType(called, NpcSkillType.SELF_RANGE_DEBUFF_ANOTHER_A);
					if (getAbnormalLevel(target, selfRangeDebuffAnother_a) == -1 && getAbnormalLevel(topDesireTarget, selfRangeDebuffAnother_a) == -1 && Rnd.get(5) < 1)
						called.getAI().addCastDesire(called, selfRangeDebuffAnother_a, 1000000);
				}
				
				if (Rnd.get(75) < 1)
				{
					final L2Skill DDMagic_a = getNpcSkillByType(called, NpcSkillType.DD_MAGIC_A);
					called.getAI().addCastDesire(target, DDMagic_a, 1000000);
				}
			}
		}
		super.onPartyAttacked(caller, called, target, damage);
	}
	
	@Override
	public void onSeeSpell(Npc npc, Player caster, L2Skill skill, Creature[] targets, boolean isPet)
	{
		final Creature topDesireTarget = npc.getAI().getTopDesireTarget();
		if (topDesireTarget != null)
		{
			if (caster != topDesireTarget && npc.distance2D(caster) < 150 && npc.distance2D(topDesireTarget) < 150)
			{
				final L2Skill selfRangeDebuff_a = getNpcSkillByType(npc, NpcSkillType.SELF_RANGE_DEBUFF_A);
				if (getAbnormalLevel(caster, selfRangeDebuff_a) == -1 && getAbnormalLevel(topDesireTarget, selfRangeDebuff_a) == -1 && Rnd.get(2) < 1)
					npc.getAI().addCastDesire(npc, selfRangeDebuff_a, 1000000);
				
				final L2Skill selfRangeDebuffAnother_a = getNpcSkillByType(npc, NpcSkillType.SELF_RANGE_DEBUFF_ANOTHER_A);
				if (getAbnormalLevel(caster, selfRangeDebuffAnother_a) == -1 && getAbnormalLevel(topDesireTarget, selfRangeDebuffAnother_a) == -1 && Rnd.get(5) < 1)
					npc.getAI().addCastDesire(npc, selfRangeDebuffAnother_a, 1000000);
			}
			
			if (Rnd.get(75) < 1)
			{
				final L2Skill DDMagic_a = getNpcSkillByType(npc, NpcSkillType.DD_MAGIC_A);
				npc.getAI().addCastDesire(caster, DDMagic_a, 1000000);
			}
		}
		super.onSeeSpell(npc, caster, skill, targets, isPet);
	}
}