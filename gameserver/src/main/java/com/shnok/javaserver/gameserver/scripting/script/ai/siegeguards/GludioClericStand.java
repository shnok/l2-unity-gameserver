package com.shnok.javaserver.gameserver.scripting.script.ai.siegeguards;

import com.shnok.javaserver.commons.random.Rnd;

import com.shnok.javaserver.gameserver.enums.actors.NpcSkillType;
import com.shnok.javaserver.gameserver.model.actor.Creature;
import com.shnok.javaserver.gameserver.model.actor.Npc;
import com.shnok.javaserver.gameserver.scripting.script.ai.individual.DefaultNpc;
import com.shnok.javaserver.gameserver.skills.L2Skill;

public class GludioClericStand extends DefaultNpc
{
	public GludioClericStand()
	{
		super("ai/siegeguards");
	}
	
	public GludioClericStand(String descr)
	{
		super(descr);
	}
	
	protected final int[] _npcIds =
	{
		35013,
		35023,
		35033,
		35043,
		35053,
		35081,
		35123,
		35165,
		35207,
		35250,
		35297,
		35341,
		35486,
		35533
	};
	
	@Override
	public void onClanAttacked(Npc caller, Npc called, Creature attacker, int damage, L2Skill skill)
	{
		if (caller.getStatus().getHpRatio() < 0.6 && Rnd.get(100) < 20)
			called.getAI().addCastDesireHold(caller, getNpcSkillByType(called, NpcSkillType.MAGIC_HEAL), 1000000);
	}
}
