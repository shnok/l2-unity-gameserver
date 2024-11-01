package com.shnok.javaserver.gameserver.scripting.script.ai.siegeguards.GludioStand;

import com.shnok.javaserver.commons.random.Rnd;
import com.shnok.javaserver.commons.util.ArraysUtil;

import com.shnok.javaserver.gameserver.model.actor.Creature;
import com.shnok.javaserver.gameserver.model.actor.Npc;
import com.shnok.javaserver.gameserver.model.actor.Playable;
import com.shnok.javaserver.gameserver.model.actor.Player;
import com.shnok.javaserver.gameserver.model.actor.instance.Door;
import com.shnok.javaserver.gameserver.model.actor.instance.SiegeSummon;
import com.shnok.javaserver.gameserver.scripting.script.ai.individual.DefaultNpc;
import com.shnok.javaserver.gameserver.skills.L2Skill;

public class GludioStand extends DefaultNpc
{
	public GludioStand()
	{
		super("ai/siegeguards/GludioStand");
	}
	
	public GludioStand(String descr)
	{
		super(descr);
	}
	
	protected final int[] _npcIds =
	{
		35079,
		35121,
		35163,
		35205,
		35248,
		35295,
		35339,
		35484,
		35531,
		35082,
		35124,
		35166,
		35208,
		35251,
		35298,
		35342,
		35487,
		35534,
		35011,
		35021,
		35031,
		35041,
		35051,
		35083,
		35086,
		35089,
		35125,
		35128,
		35131,
		35167,
		35170,
		35173,
		35209,
		35212,
		35215,
		35252,
		35255,
		35258,
		35299,
		35302,
		35305,
		35343,
		35346,
		35349,
		35488,
		35491,
		35494,
		35535,
		35538,
		35541,
		35010,
		35020,
		35030,
		35040,
		35050,
		35084,
		35087,
		35090,
		35126,
		35129,
		35132,
		35168,
		35171,
		35174,
		35210,
		35213,
		35216,
		35253,
		35256,
		35259,
		35300,
		35303,
		35306,
		35344,
		35347,
		35350,
		35489,
		35492,
		35495,
		35536,
		35539,
		35542
	};
	
	@Override
	public void onAttacked(Npc npc, Creature attacker, int damage, L2Skill skill)
	{
		if (attacker.getZ() <= (npc.getZ() + 100) && getPledgeCastleState(npc, attacker) != 2)
			npc.getAI().addAttackDesireHold(attacker, (((damage * 1.0) / npc.getStatus().getMaxHp()) / 0.05 * 100));
	}
	
	@Override
	public void onClanAttacked(Npc caller, Npc called, Creature attacker, int damage, L2Skill skill)
	{
		if (attacker.getZ() <= (caller.getZ() + 100) && getPledgeCastleState(called, attacker) != 2)
			called.getAI().addAttackDesireHold(attacker, (((damage * 1.0) / called.getStatus().getMaxHp()) / 0.05 * 100));
	}
	
	@Override
	public void onStaticObjectClanAttacked(Door caller, Npc called, Creature attacker, int damage, L2Skill skill)
	{
		if (attacker instanceof SiegeSummon)
		{
			if (Rnd.get(100) < 10)
				called.getAI().addAttackDesireHold(attacker.getActingPlayer(), 5000);
			else
				called.getAI().addAttackDesireHold(attacker, 1000);
		}
		else if (getPledgeCastleState(called, attacker) != 2 && attacker instanceof Playable)
		{
			called.getAI().addAttackDesireHold(attacker, (((damage * 1.0) / called.getStatus().getMaxHp()) / 0.05 * 50));
		}
	}
	
	@Override
	public void onSeeCreature(Npc npc, Creature creature)
	{
		if (creature instanceof Playable && getPledgeCastleState(npc, creature) != 2)
			npc.getAI().addAttackDesireHold(creature, 200);
	}
	
	@Override
	public void onSeeSpell(Npc npc, Player caster, L2Skill skill, Creature[] targets, boolean isPet)
	{
		if (getPledgeCastleState(npc, caster) != 2 && skill.isOffensive() && npc.isInCombat() && ArraysUtil.contains(targets, npc.getAI().getTopDesireTarget()))
			npc.getAI().addAttackDesireHold(caster, (((skill.getAggroPoints() * 1.0) / npc.getStatus().getMaxHp()) / 0.05 * 50));
	}
}
