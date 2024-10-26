package com.shnok.javaserver.gameserver.scripting.script.ai.individual.Monster.WarriorBase.Warrior.PartyLeaderWarrior;

import net.sf.l2j.commons.random.Rnd;

import net.sf.l2j.gameserver.enums.actors.NpcSkillType;
import net.sf.l2j.gameserver.model.actor.Creature;
import net.sf.l2j.gameserver.model.actor.Npc;
import net.sf.l2j.gameserver.model.actor.Playable;
import net.sf.l2j.gameserver.skills.L2Skill;

public class PartyLeaderCastSplashAggressive extends PartyLeaderWarriorAggressive
{
	public PartyLeaderCastSplashAggressive()
	{
		super("ai/individual/Monster/WarriorBase/Warrior/PartyLeaderWarrior");
	}
	
	public PartyLeaderCastSplashAggressive(String descr)
	{
		super(descr);
	}
	
	protected final int[] _npcIds =
	{
		21058,
		20966,
		20973,
		21078,
		20986,
	};
	
	@Override
	public void onAttacked(Npc npc, Creature attacker, int damage, L2Skill skill)
	{
		if (attacker instanceof Playable)
		{
			final Creature mostHated = npc.getAI().getAggroList().getMostHatedCreature();
			if (mostHated == attacker && npc.distance2D(attacker) < 200 && Rnd.get(100) < 33)
				npc.getAI().addCastDesire(npc, getNpcSkillByType(npc, NpcSkillType.SELF_RANGE_DD_MAGIC), 1000000);
		}
		super.onAttacked(npc, attacker, damage, skill);
	}
	
	@Override
	public void onClanAttacked(Npc caller, Npc called, Creature attacker, int damage, L2Skill skill)
	{
		if (attacker instanceof Playable && called.getAI().getLifeTime() > 7)
		{
			final Creature mostHated = called.getAI().getAggroList().getMostHatedCreature();
			if (mostHated == attacker && called.distance2D(attacker) < 200 && Rnd.get(100) < 33)
				called.getAI().addCastDesire(called, getNpcSkillByType(called, NpcSkillType.SELF_RANGE_DD_MAGIC), 1000000);
		}
		super.onClanAttacked(caller, called, attacker, damage, skill);
	}
}