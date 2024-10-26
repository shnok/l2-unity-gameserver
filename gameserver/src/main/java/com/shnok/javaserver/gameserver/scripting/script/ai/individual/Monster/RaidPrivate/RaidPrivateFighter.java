package com.shnok.javaserver.gameserver.scripting.script.ai.individual.Monster.RaidPrivate;

import net.sf.l2j.commons.random.Rnd;

import net.sf.l2j.gameserver.enums.actors.NpcSkillType;
import net.sf.l2j.gameserver.model.actor.Creature;
import net.sf.l2j.gameserver.model.actor.Npc;
import net.sf.l2j.gameserver.model.actor.Playable;
import net.sf.l2j.gameserver.model.actor.Player;
import net.sf.l2j.gameserver.skills.L2Skill;

public class RaidPrivateFighter extends RaidPrivateStandard
{
	public RaidPrivateFighter()
	{
		super("ai/individual/Monster/RaidPrivate/RaidPrivateFighter");
	}
	
	public RaidPrivateFighter(String descr)
	{
		super(descr);
	}
	
	protected final int[] _npcIds =
	{
		25003,
		25014,
		25024,
		25034,
		25039,
		25048,
		25055,
		25062,
		25065,
		25072,
		25077,
		25084,
		25091,
		25093,
		25097,
		25104,
		25111,
		25114,
		25121,
		25123,
		25130,
		25139,
		25141,
		25153,
		25157,
		25177,
		25181,
		25191,
		25193,
		25201,
		25206,
		25210,
		25216,
		25222,
		25225,
		25231,
		25243,
		25246,
		25251,
		25254,
		25265,
		25268,
		25275,
		25285,
		25294,
		25298,
		25304,
		25307,
		25310,
		25317,
		25324,
		25326,
		25348,
		25353,
		25356,
		25358,
		25361,
		25367,
		25370,
		25374,
		25376,
		25379,
		25381,
		25384,
		25386,
		25393,
		25397,
		25402,
		25405,
		25406,
		25408,
		25409,
		25411,
		25419,
		25425,
		25427,
		25428,
		25430,
		25435,
		25439,
		25443,
		25461,
		25462,
		25464,
		25466,
		25469,
		25472,
		25474,
		25482,
		25485,
		25486,
		25489,
		25494,
		25497,
		25499,
		29041,
		25507,
		25505,
		25510,
		25516,
		25332
	};
	
	@Override
	public void onPartyAttacked(Npc caller, Npc called, Creature target, int damage)
	{
		if (target instanceof Playable)
		{
			final Creature topDesireTarget = called.getAI().getTopDesireTarget();
			if (topDesireTarget != null)
			{
				if (target == topDesireTarget && Rnd.get(15) < 1)
				{
					final L2Skill physicalSpecial_a = getNpcSkillByType(called, NpcSkillType.PHYSICAL_SPECIAL_A);
					called.getAI().addCastDesire(target, physicalSpecial_a, 1000000);
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
			if (caster == topDesireTarget && Rnd.get(15) < 1)
			{
				final L2Skill physicalSpecial_a = getNpcSkillByType(npc, NpcSkillType.PHYSICAL_SPECIAL_A);
				npc.getAI().addCastDesire(caster, physicalSpecial_a, 1000000);
			}
		}
		super.onSeeSpell(npc, caster, skill, targets, isPet);
	}
}