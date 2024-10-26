package com.shnok.javaserver.gameserver.scripting.script.ai.individual.Monster.WarriorBase.Warrior.RoyalRushBomb;

import com.shnok.javaserver.commons.random.Rnd;

import com.shnok.javaserver.gameserver.enums.actors.NpcSkillType;
import com.shnok.javaserver.gameserver.model.actor.Creature;
import com.shnok.javaserver.gameserver.model.actor.Npc;
import com.shnok.javaserver.gameserver.scripting.script.ai.individual.Monster.WarriorBase.Warrior.Warrior;
import com.shnok.javaserver.gameserver.skills.L2Skill;

public class RoyalRushBomb extends Warrior
{
	public RoyalRushBomb()
	{
		super("ai/individual/Monster/WarriorBase/Warrior/RoyalRushBomb");
	}
	
	public RoyalRushBomb(String descr)
	{
		super(descr);
	}
	
	protected final int[] _npcIds =
	{
		18149,
		18195,
		18230
	};
	
	@Override
	public void onAttacked(Npc npc, Creature attacker, int damage, L2Skill skill)
	{
		if (Rnd.get(100) < getNpcIntAIParamOrDefault(npc, "SelfRangeDDMagicRate", 33) && (npc.getStatus().getHpRatio() * 100) < getNpcIntAIParamOrDefault(npc, "DDMagicUseHpRate", 30))
			npc.getAI().addCastDesire(npc, getNpcSkillByType(npc, NpcSkillType.SELF_RANGE_DD_MAGIC), 1000000);
		
		super.onAttacked(npc, attacker, damage, skill);
	}
	
	@Override
	public void onUseSkillFinished(Npc npc, Creature creature, L2Skill skill, boolean success)
	{
		npc.deleteMe();
	}
}
