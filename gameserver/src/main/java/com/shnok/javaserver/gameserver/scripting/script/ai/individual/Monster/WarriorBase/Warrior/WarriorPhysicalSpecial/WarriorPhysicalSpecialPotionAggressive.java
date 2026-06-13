package com.shnok.javaserver.gameserver.scripting.script.ai.individual.Monster.WarriorBase.Warrior.WarriorPhysicalSpecial;

import com.shnok.javaserver.commons.random.Rnd;

import com.shnok.javaserver.gameserver.enums.actors.NpcSkillType;
import com.shnok.javaserver.gameserver.model.actor.Creature;
import com.shnok.javaserver.gameserver.model.actor.Npc;
import com.shnok.javaserver.gameserver.network.NpcStringId;
import com.shnok.javaserver.gameserver.skills.L2Skill;

public class WarriorPhysicalSpecialPotionAggressive extends WarriorPhysicalSpecialAggressive
{
	public WarriorPhysicalSpecialPotionAggressive()
	{
		super("ai/individual/Monster/WarriorBase/Warrior/WarriorPhysicalSpecial");
	}
	
	public WarriorPhysicalSpecialPotionAggressive(String descr)
	{
		super(descr);
	}
	
	protected final int[] _npcIds =
	{
		22022,
		22023,
		22026
	};
	
	@Override
	public void onCreated(Npc npc)
	{
		npc._i_ai1 = 0;
	}
	
	@Override
	public void onAttacked(Npc npc, Creature attacker, int damage, L2Skill skill)
	{
		final L2Skill magicHeal = getNpcSkillByType(npc, NpcSkillType.MAGIC_HEAL);
		if (npc.getStatus().getHpRatio() < 0.8 && npc._i_ai1 != 3 && Rnd.get(100) < 33)
		{
			npc.getAI().addCastDesire(npc, magicHeal, 1000000);
			npc._i_ai1 = 1;
		}
		else if (npc.getStatus().getHpRatio() < 0.5 && npc._i_ai1 != 3 && Rnd.get(100) < 33)
		{
			npc.getAI().addCastDesire(npc, magicHeal, 1000000);
			npc._i_ai1 = 2;
		}
		else if (npc.getStatus().getHpRatio() < 0.33 && npc._i_ai1 != 3 && Rnd.get(100) < 33)
		{
			npc.getAI().addCastDesire(npc, magicHeal, 1000000);
			npc._i_ai1 = 3;
		}
		if (Rnd.get(100) < 33)
		{
			npc.getAI().addCastDesire(attacker, getNpcSkillByType(npc, NpcSkillType.PHYSICAL_SPECIAL), 1000000);
		}
		
		super.onAttacked(npc, attacker, damage, skill);
	}
	
	@Override
	public void onUseSkillFinished(Npc npc, Creature creature, L2Skill skill, boolean success)
	{
		final int i0 = Rnd.get(100);
		if (skill == getNpcSkillByType(npc, NpcSkillType.MAGIC_HEAL))
		{
			if (i0 < 30)
				npc.broadcastNpcSay(NpcStringId.ID_10071);
			else if (i0 < 60)
				npc.broadcastNpcSay(NpcStringId.ID_10072);
			else
				npc.broadcastNpcSay(NpcStringId.ID_10073);
		}
	}
}
