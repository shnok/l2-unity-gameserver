package com.shnok.javaserver.gameserver.scripting.script.ai.individual.Monster.WarriorBase.Warrior;

import com.shnok.javaserver.gameserver.model.actor.Creature;
import com.shnok.javaserver.gameserver.model.actor.Npc;
import com.shnok.javaserver.gameserver.skills.L2Skill;

public class WarriorDisguise extends Warrior
{
	public WarriorDisguise()
	{
		super("ai/individual/Monster/WarriorBase/Warrior");
	}
	
	public WarriorDisguise(String descr)
	{
		super(descr);
	}
	
	protected final int[] _npcIds =
	{
		21266,
		21267,
		21258
	};
	
	@Override
	public void onCreated(Npc npc)
	{
		npc._i_ai2 = 0;
	}
	
	@Override
	public void onAttacked(Npc npc, Creature attacker, int damage, L2Skill skill)
	{
		if (npc._i_ai2 == 0)
		{
			createOnePrivateEx(npc, getNpcIntAIParam(npc, "silhouette"), npc.getX(), npc.getY(), npc.getZ(), npc.getHeading(), 0, false, 1000, attacker.getObjectId(), 0);
			
			npc._i_ai2 = 1;
			npc.deleteMe();
		}
	}
}