package com.shnok.javaserver.gameserver.scripting.script.ai.siegablehall;

import com.shnok.javaserver.commons.random.Rnd;

import com.shnok.javaserver.gameserver.enums.actors.NpcSkillType;
import com.shnok.javaserver.gameserver.model.actor.Creature;
import com.shnok.javaserver.gameserver.model.actor.Npc;
import com.shnok.javaserver.gameserver.scripting.script.ai.individual.DefaultNpc;
import com.shnok.javaserver.gameserver.skills.L2Skill;

public class AzitWateringMimicMagical extends DefaultNpc
{
	public AzitWateringMimicMagical()
	{
		super("ai/siegeablehall");
	}
	
	public AzitWateringMimicMagical(String descr)
	{
		super(descr);
	}
	
	protected final int[] _npcIds =
	{
		35594
	};
	
	@Override
	public void onAttacked(Npc npc, Creature attacker, int damage, L2Skill skill)
	{
		if (Rnd.get(9) < 1)
			npc.getAI().addCastDesire(attacker, getNpcSkillByType(npc, NpcSkillType.DD_MAGIC), 1000000);
	}
	
	@Override
	public void onUseSkillFinished(Npc npc, Creature creature, L2Skill skill, boolean success)
	{
		if (getNpcSkillByType(npc, NpcSkillType.DD_MAGIC) == skill && success)
			npc.deleteMe();
	}
}
