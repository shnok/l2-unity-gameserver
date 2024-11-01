package com.shnok.javaserver.gameserver.scripting.script.ai.individual.Monster.WarriorBase.Warrior.WarriorPhysicalSpecial;

import com.shnok.javaserver.commons.random.Rnd;

import com.shnok.javaserver.gameserver.enums.IntentionType;
import com.shnok.javaserver.gameserver.enums.actors.NpcSkillType;
import com.shnok.javaserver.gameserver.model.actor.Creature;
import com.shnok.javaserver.gameserver.model.actor.Npc;
import com.shnok.javaserver.gameserver.model.actor.Playable;
import com.shnok.javaserver.gameserver.skills.L2Skill;

public class WarriorPhysicalSpecialAggressive extends WarriorPhysicalSpecial
{
	public WarriorPhysicalSpecialAggressive()
	{
		super("ai/individual/Monster/WarriorBase/Warrior/WarriorPhysicalSpecial");
	}
	
	public WarriorPhysicalSpecialAggressive(String descr)
	{
		super(descr);
	}
	
	protected final int[] _npcIds =
	{
		21427,
		21022,
		21018,
		20270,
		20271,
		27140,
		27141,
		27142,
		21270,
		21272,
		20239,
		21638,
		20291,
		27217,
		21238,
		21242,
		20824,
		20818,
		20645,
		21403,
		20927,
		20650,
		20924,
		20626,
		20625,
		20157,
		20090,
		27190,
		20845,
		21629,
		21630,
		20823,
		21069,
		21065,
		21378,
		21377,
		21376,
		21222,
		21226,
		21230,
		21021,
		21259,
		21398,
		20634,
		20669,
		20632,
		20837,
		20801,
		20585,
		21246,
		21250,
		21254,
		27218,
		20683,
		21210,
		21214,
		21218,
		20798,
		20843,
		21623,
		21624,
		20165,
		20257,
		20259,
		20300,
		20304,
		20374,
		20422,
		20615,
		20622,
		20832,
		20857,
		20930,
		20933,
		21014,
		21015,
		21120,
		21123,
		21150,
		21151,
		21158,
		21234,
		21296,
		21326,
		21330,
		21333,
		21337,
		21341,
		21438,
		21439,
		21441,
		21442,
		21450,
		21602,
		21603,
		21604,
		21625,
		21631,
		21660,
		21662,
		21668,
		21672,
		21673,
		21677,
		21678,
		21679,
		21680,
		21683,
		21685,
		21691,
		21695,
		21696,
		21700,
		21701,
		21702,
		21703,
		21706,
		21708,
		21714,
		21718,
		21719,
		21723,
		21724,
		21725,
		21726,
		21729,
		21731,
		21737,
		21741,
		21742,
		21746,
		21747,
		21748,
		21749,
		21752,
		21754,
		21760,
		21764,
		21765,
		21769,
		21770,
		21771,
		21772,
		21775,
		21777,
		21783,
		21787,
		21788,
		21792,
		21793,
		21794,
		21795,
		27191,
		27196,
		27197,
		22006,
		22105,
		22106,
		22107,
		22115,
		22116,
		22117,
		22219,
		22220,
		22221,
		22222
	};
	
	@Override
	public void onSeeCreature(Npc npc, Creature creature)
	{
		if (creature instanceof Playable)
		{
			if (npc.getAI().getCurrentIntention().getType() != IntentionType.ATTACK && npc.getAI().getLifeTime() > 7 && npc.isInMyTerritory())
			{
				final Creature topDesireTarget = npc.getAI().getTopDesireTarget();
				if (topDesireTarget != null && topDesireTarget == creature && npc.distance2D(creature) > 100 && Rnd.get(100) < 33)
				{
					final L2Skill physicalSpecial = getNpcSkillByType(npc, NpcSkillType.PHYSICAL_SPECIAL);
					npc.getAI().addCastDesire(creature, physicalSpecial, 1000000);
				}
			}
			
			tryToAttack(npc, creature);
		}
		
		super.onSeeCreature(npc, creature);
	}
}