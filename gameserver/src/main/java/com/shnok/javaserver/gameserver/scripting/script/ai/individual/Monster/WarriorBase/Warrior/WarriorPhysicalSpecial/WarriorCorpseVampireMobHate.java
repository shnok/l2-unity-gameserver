package com.shnok.javaserver.gameserver.scripting.script.ai.individual.Monster.WarriorBase.Warrior.WarriorPhysicalSpecial;

import com.shnok.javaserver.commons.random.Rnd;

import com.shnok.javaserver.gameserver.enums.IntentionType;
import com.shnok.javaserver.gameserver.enums.actors.NpcSkillType;
import com.shnok.javaserver.gameserver.model.actor.Creature;
import com.shnok.javaserver.gameserver.model.actor.Npc;
import com.shnok.javaserver.gameserver.model.actor.Playable;
import com.shnok.javaserver.gameserver.skills.L2Skill;

public class WarriorCorpseVampireMobHate extends WarriorCorpseVampire
{
	public WarriorCorpseVampireMobHate()
	{
		super("ai/individual/Monster/WarriorBase/Warrior/WarriorPhysicalSpecial");
	}
	
	public WarriorCorpseVampireMobHate(String descr)
	{
		super(descr);
	}
	
	protected final int[] _npcIds =
	{
		21582,
		21594,
		21587
	};
	
	@Override
	public void onClanAttacked(Npc caller, Npc called, Creature attacker, int damage, L2Skill skill)
	{
		if (called.getAI().getLifeTime() > 7 && attacker instanceof Playable && called.getAI().getCurrentIntention().getType() == IntentionType.ATTACK)
		{
			if (called.distance2D(attacker) < 100 && Rnd.get(100) < 33)
			{
				L2Skill DDMagic2 = getNpcSkillByType(called, NpcSkillType.DD_MAGIC2);
				
				called.getAI().addCastDesire(attacker, DDMagic2, 1000000);
			}
		}
		super.onClanAttacked(caller, called, attacker, damage, skill);
	}
}
