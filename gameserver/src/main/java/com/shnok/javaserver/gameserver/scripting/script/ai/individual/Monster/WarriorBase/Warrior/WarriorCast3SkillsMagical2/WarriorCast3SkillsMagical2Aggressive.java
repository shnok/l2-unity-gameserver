package com.shnok.javaserver.gameserver.scripting.script.ai.individual.Monster.WarriorBase.Warrior.WarriorCast3SkillsMagical2;

import com.shnok.javaserver.commons.random.Rnd;

import com.shnok.javaserver.gameserver.enums.IntentionType;
import com.shnok.javaserver.gameserver.enums.actors.NpcSkillType;
import com.shnok.javaserver.gameserver.model.actor.Creature;
import com.shnok.javaserver.gameserver.model.actor.Npc;
import com.shnok.javaserver.gameserver.model.actor.Playable;
import com.shnok.javaserver.gameserver.skills.L2Skill;

public class WarriorCast3SkillsMagical2Aggressive extends WarriorCast3SkillsMagical2
{
	public WarriorCast3SkillsMagical2Aggressive()
	{
		super("ai/individual/Monster/WarriorBase/Warrior/WarriorCast3SkillsMagical2");
	}
	
	public WarriorCast3SkillsMagical2Aggressive(String descr)
	{
		super(descr);
	}
	
	protected final int[] _npcIds =
	{
		18001,
		20621,
		21006,
		20825,
		20160,
		20198,
		21066,
		20612,
		21647,
		21026,
		20056,
		20117,
		20118,
		20352,
		20421,
		18002,
		22119
	};
	
	@Override
	public void onSeeCreature(Npc npc, Creature creature)
	{
		if (!(creature instanceof Playable))
			return;
		
		if (npc.getAI().getLifeTime() > 7 && npc.isInMyTerritory())
		{
			if (npc.distance2D(creature) > 100)
			{
				if (Rnd.get(100) < 33)
				{
					final L2Skill DDMagic1 = getNpcSkillByType(npc, NpcSkillType.DD_MAGIC1);
					npc.getAI().addCastDesire(creature, DDMagic1, 1000000);
				}
				
				if (Rnd.get(100) < 33)
				{
					final L2Skill DDMagic2 = getNpcSkillByType(npc, NpcSkillType.DD_MAGIC2);
					npc.getAI().addCastDesire(creature, DDMagic2, 1000000);
				}
			}
			
			if (npc.getAI().getCurrentIntention().getType() == IntentionType.WANDER)
			{
				final L2Skill debuff = getNpcSkillByType(npc, NpcSkillType.DEBUFF);
				if (Rnd.get(100) < 33 && getAbnormalLevel(creature, debuff) <= 0)
					npc.getAI().addCastDesire(creature, debuff, 1000000);
			}
		}
		
		tryToAttack(npc, creature);
		
		super.onSeeCreature(npc, creature);
	}
}