package com.shnok.javaserver.gameserver.scripting.script.ai.individual.Monster.WarriorBase.Warrior.WarriorCastingCurse.WarriorCastingCurseAggressive;

import com.shnok.javaserver.commons.random.Rnd;

import com.shnok.javaserver.gameserver.enums.IntentionType;
import com.shnok.javaserver.gameserver.enums.actors.NpcSkillType;
import com.shnok.javaserver.gameserver.model.actor.Creature;
import com.shnok.javaserver.gameserver.model.actor.Npc;
import com.shnok.javaserver.gameserver.model.actor.Playable;
import com.shnok.javaserver.gameserver.scripting.script.ai.individual.Monster.WarriorBase.Warrior.WarriorCastingCurse.WarriorCastingCurse;
import com.shnok.javaserver.gameserver.skills.L2Skill;

public class WarriorCastingCurseAggressive extends WarriorCastingCurse
{
	public WarriorCastingCurseAggressive()
	{
		super("ai/individual/Monster/WarriorBase/Warrior/WarriorCastingCurse/WarriorCastingCurseAggressive");
	}
	
	public WarriorCastingCurseAggressive(String descr)
	{
		super(descr);
	}
	
	protected final int[] _npcIds =
	{
		20087,
		20043,
		20796,
		20345,
		20649,
		20145,
		20676,
		20671,
		20652,
		20787,
		20848,
		20849,
		20036,
		20233,
		20050,
		20561,
		20839,
		21611,
		21612,
		20229,
		20338,
		20383,
		20490,
		20928,
		21012,
		21097,
		21145,
		21154,
		21157,
		21161,
		21163,
		21298,
		21613,
		21670,
		21693,
		21716,
		21739,
		21762,
		21785
	};
	
	@Override
	public void onSeeCreature(Npc npc, Creature creature)
	{
		if (!(creature instanceof Playable))
		{
			super.onSeeCreature(npc, creature);
			return;
		}
		
		if (npc.getAI().getLifeTime() > 7 && npc.getAI().getCurrentIntention().getType() == IntentionType.WANDER && npc.isInMyTerritory())
		{
			final L2Skill debuff = getNpcSkillByType(npc, NpcSkillType.DEBUFF);
			if (Rnd.get(100) < 33 && getAbnormalLevel(creature, debuff) <= 0)
				npc.getAI().addCastDesire(creature, debuff, 1000000);
		}
		
		tryToAttack(npc, creature);
		
		super.onSeeCreature(npc, creature);
	}
}