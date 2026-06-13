package com.shnok.javaserver.gameserver.scripting.script.ai.individual.Monster.WarriorBase.Warrior.WarriorPhysicalSpecialShotRange;

import com.shnok.javaserver.commons.random.Rnd;

import com.shnok.javaserver.gameserver.enums.actors.NpcSkillType;
import com.shnok.javaserver.gameserver.model.actor.Creature;
import com.shnok.javaserver.gameserver.model.actor.Npc;
import com.shnok.javaserver.gameserver.skills.L2Skill;

public class WarriorPhysicalSpecialShotRangeDebuffAggressive extends WarriorPhysicalSpecialShotRangeAggressive
{
	public WarriorPhysicalSpecialShotRangeDebuffAggressive()
	{
		super("ai/individual/Monster/WarriorBase/Warrior/WarriorPhysicalSpecialShotRange");
	}
	
	public WarriorPhysicalSpecialShotRangeDebuffAggressive(String descr)
	{
		super(descr);
	}
	
	protected final int[] _npcIds =
	{
		22054
	};
	
	@Override
	public void onAttacked(Npc npc, Creature attacker, int damage, L2Skill skill)
	{
		if (npc.getStatus().getHpRatio() < 0.33 && Rnd.get(100) < 60)
			npc.getAI().addCastDesire(attacker, getNpcSkillByType(npc, NpcSkillType.DEBUFF1), 1000000);
		else if (npc.getStatus().getHpRatio() < 0.33)
		{
			if (Rnd.get(100) < 50)
			{
				if (Rnd.get(100) < 50)
					npc.getAI().addCastDesire(attacker, getNpcSkillByType(npc, NpcSkillType.DEBUFF2), 1000000);
				else
					npc.getAI().addCastDesire(attacker, getNpcSkillByType(npc, NpcSkillType.DEBUFF3), 1000000);
			}
			else if (Rnd.get(100) < 50)
				npc.getAI().addCastDesire(attacker, getNpcSkillByType(npc, NpcSkillType.DEBUFF4), 1000000);
			else
				npc.getAI().addCastDesire(attacker, getNpcSkillByType(npc, NpcSkillType.DEBUFF5), 1000000);
		}
		
		super.onAttacked(npc, attacker, damage, skill);
	}
}
