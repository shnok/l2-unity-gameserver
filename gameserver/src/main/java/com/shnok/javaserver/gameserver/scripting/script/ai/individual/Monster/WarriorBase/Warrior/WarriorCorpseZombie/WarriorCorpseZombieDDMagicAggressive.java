package com.shnok.javaserver.gameserver.scripting.script.ai.individual.Monster.WarriorBase.Warrior.WarriorCorpseZombie;

import com.shnok.javaserver.commons.random.Rnd;

import com.shnok.javaserver.gameserver.data.SkillTable;
import com.shnok.javaserver.gameserver.enums.IntentionType;
import com.shnok.javaserver.gameserver.enums.actors.NpcSkillType;
import com.shnok.javaserver.gameserver.model.actor.Creature;
import com.shnok.javaserver.gameserver.model.actor.Npc;
import com.shnok.javaserver.gameserver.model.actor.Playable;
import com.shnok.javaserver.gameserver.skills.L2Skill;

public class WarriorCorpseZombieDDMagicAggressive extends WarriorCorpseZombieDDMagic
{
	public WarriorCorpseZombieDDMagicAggressive()
	{
		super("ai/individual/Monster/WarriorBase/Warrior/WarriorCorpseZombie");
	}
	
	public WarriorCorpseZombieDDMagicAggressive(String descr)
	{
		super(descr);
	}
	
	protected final int[] _npcIds =
	{
		21409,
		21570
	};
	
	@Override
	public void onSeeCreature(Npc npc, Creature creature)
	{
		if (!(creature instanceof Playable))
			return;
		
		if (npc.getAI().getLifeTime() > 7 && npc.getAI().getCurrentIntention().getType() != IntentionType.ATTACK)
		{
			if (getNpcIntAIParam(npc, "IsTeleport") != 0 && npc.distance2D(creature) > 100 && Rnd.get(100) < 10)
			{
				npc.abortAll(false);
				npc.instantTeleportTo(creature.getPosition(), 0);
				
				final L2Skill avTeleport = SkillTable.getInstance().getInfo(4671, 1);
				npc.getAI().addCastDesire(creature, avTeleport, 1000000);
			}
			
			if (npc.isInMyTerritory())
			{
				if (npc.distance2D(creature) > 100 && Rnd.get(100) < 33)
				{
					final L2Skill DDMagic = getNpcSkillByType(npc, NpcSkillType.DD_MAGIC);
					npc.getAI().addCastDesire(creature, DDMagic, 1000000);
				}
			}
		}
		
		tryToAttack(npc, creature);
		
		super.onSeeCreature(npc, creature);
	}
}