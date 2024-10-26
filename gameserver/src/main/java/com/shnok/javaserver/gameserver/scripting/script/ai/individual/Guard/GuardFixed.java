package com.shnok.javaserver.gameserver.scripting.script.ai.individual.Guard;

import net.sf.l2j.gameserver.model.actor.Creature;
import net.sf.l2j.gameserver.model.actor.Npc;
import net.sf.l2j.gameserver.model.actor.Player;
import net.sf.l2j.gameserver.skills.L2Skill;

public class GuardFixed extends GuardStand
{
	public GuardFixed()
	{
		super("ai/individual/Guard");
	}
	
	public GuardFixed(String descr)
	{
		super(descr);
	}
	
	protected final int[] _npcIds =
	{
		30040,
		30041,
		30044,
		30045,
		30072,
		30073,
		30075,
		30122,
		30123,
		30197,
		30199,
		30200,
		30201,
		30217,
		30218,
		30220,
		30331,
		30334,
		30335,
		30336,
		30338,
		30346,
		30349,
		30355,
		30356,
		30379,
		30380,
		30381,
		30383,
		30385,
		30465,
		30478,
		30541,
		30542,
		30546,
		30547,
		30577,
		30578,
		30579,
		30581,
		30709,
		30713,
		30733,
		30871,
		30873,
		30875,
		30877,
		30883,
		30885,
		30887,
		30889,
		30917,
		30919,
		30921,
		30923,
		31671,
		31672,
		31673,
		31674
	};
	
	@Override
	public void onAttacked(Npc npc, Creature attacker, int damage, L2Skill skill)
	{
		npc.getAI().addAttackDesireHold(attacker, 2000);
	}
	
	@Override
	public void onSeeCreature(Npc npc, Creature creature)
	{
		if (creature instanceof Player && creature.getActingPlayer().getKarma() > 0)
			npc.getAI().addAttackDesireHold(creature, 1500);
	}
}
