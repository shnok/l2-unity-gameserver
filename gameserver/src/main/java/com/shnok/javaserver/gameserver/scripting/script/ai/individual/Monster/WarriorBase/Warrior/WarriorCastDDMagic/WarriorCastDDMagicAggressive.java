package com.shnok.javaserver.gameserver.scripting.script.ai.individual.Monster.WarriorBase.Warrior.WarriorCastDDMagic;

import net.sf.l2j.commons.random.Rnd;

import net.sf.l2j.gameserver.enums.IntentionType;
import net.sf.l2j.gameserver.enums.actors.NpcSkillType;
import net.sf.l2j.gameserver.model.actor.Creature;
import net.sf.l2j.gameserver.model.actor.Npc;
import net.sf.l2j.gameserver.model.actor.Playable;
import net.sf.l2j.gameserver.skills.L2Skill;

public class WarriorCastDDMagicAggressive extends WarriorCastDDMagic
{
	public WarriorCastDDMagicAggressive()
	{
		super("ai/individual/Monster/WarriorBase/Warrior/WarriorCastDDMagic");
	}
	
	public WarriorCastDDMagicAggressive(String descr)
	{
		super(descr);
	}
	
	protected final int[] _npcIds =
	{
		21421,
		20635,
		20670,
		21569,
		20664,
		20137,
		20668,
		21239,
		21243,
		20146,
		21417,
		20656,
		20596,
		20680,
		20227,
		21223,
		21231,
		21227,
		20235,
		20279,
		21247,
		21255,
		21251,
		20110,
		20113,
		20115,
		21215,
		21219,
		21211,
		20935,
		20605,
		20176,
		20647,
		20194,
		20347,
		20354,
		20431,
		20453,
		20513,
		20546,
		21000,
		21143,
		21146,
		21155,
		21162,
		21235,
		20412,
	};
	
	@Override
	public void onSeeCreature(Npc npc, Creature creature)
	{
		if (!(creature instanceof Playable))
			return;
		
		if (npc.getAI().getLifeTime() > 7 && npc.getAI().getCurrentIntention().getType() != IntentionType.ATTACK && npc.isInMyTerritory())
		{
			if (Rnd.get(100) < 33 && npc.distance2D(creature) > 100)
			{
				final L2Skill DDMagic = getNpcSkillByType(npc, NpcSkillType.DD_MAGIC);
				npc.getAI().addCastDesire(creature, DDMagic, 1000000);
			}
		}
		
		tryToAttack(npc, creature);
		
		super.onSeeCreature(npc, creature);
	}
}