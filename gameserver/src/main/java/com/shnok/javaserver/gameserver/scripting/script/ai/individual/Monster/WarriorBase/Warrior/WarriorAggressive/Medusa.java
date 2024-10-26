package com.shnok.javaserver.gameserver.scripting.script.ai.individual.Monster.WarriorBase.Warrior.WarriorAggressive;

import net.sf.l2j.commons.random.Rnd;

import net.sf.l2j.gameserver.model.actor.Creature;
import net.sf.l2j.gameserver.model.actor.Npc;
import net.sf.l2j.gameserver.network.NpcStringId;
import net.sf.l2j.gameserver.skills.L2Skill;

/**
 * Specific AI for Medusa. She casts a fatal poison on low life.
 */
public class Medusa extends WarriorAggressive
{
	public Medusa()
	{
		super("ai/individual/Monster/WarriorBase/Warrior/WarriorAggressive");
	}
	
	protected final int[] _npcIds =
	{
		20158
	};
	
	@Override
	public void onAttacked(Npc npc, Creature attacker, int damage, L2Skill skill)
	{
		// Chance to cast is 3%, when Medusa's HP is below 20%. Also attacker must be the most hated.
		if (Rnd.get(100) < 3 && npc.getStatus().getHpRatio() < 0.2 && npc.getAI().getAggroList().getMostHatedCreature() == attacker)
		{
			npc.broadcastNpcSay(NpcStringId.ID_1000452);
			npc.getAI().addCastDesire(attacker, 4320, 3, 1000000);
		}
		super.onAttacked(npc, attacker, damage, skill);
	}
}