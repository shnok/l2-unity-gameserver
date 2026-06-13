package com.shnok.javaserver.gameserver.scripting.script.ai.individual.Monster.WarriorBase.Warrior;

import com.shnok.javaserver.gameserver.data.SkillTable;
import com.shnok.javaserver.gameserver.enums.actors.NpcSkillType;
import com.shnok.javaserver.gameserver.model.actor.Creature;
import com.shnok.javaserver.gameserver.model.actor.Npc;
import com.shnok.javaserver.gameserver.skills.L2Skill;

public class WarriorBerserker extends Warrior
{
	public WarriorBerserker()
	{
		super("ai/individual/Monster/WarriorBase/Warrior");
	}
	
	public WarriorBerserker(String descr)
	{
		super(descr);
	}
	
	protected final int[] _npcIds =
	{
		20086,
		21236,
		21240,
		20267,
		21244,
		21248,
		21252,
		21208,
		21224,
		21228,
		20601,
		20248,
		21212,
		21216,
		21220,
		21232
	};
	
	@Override
	public void onCreated(Npc npc)
	{
		npc._i_ai1 = 0;
		
		super.onCreated(npc);
	}
	
	@Override
	public void onClanDied(Npc caller, Npc called, Creature killer)
	{
		if (caller != called && called._i_ai1 < getNpcIntAIParam(called, "MaxRoarCount"))
		{
			called._i_ai1++;
			
			final L2Skill furyBase = getNpcSkillByType(called, NpcSkillType.FURY);
			final L2Skill fury = SkillTable.getInstance().getInfo(furyBase.getId(), called._i_ai1);
			
			called.getAI().addCastDesire(called, fury, 1000000);
		}
		super.onClanDied(caller, called, killer);
	}
}