package com.shnok.javaserver.gameserver.scripting.script.ai.individual.Monster.WarriorBase.Warrior.PartyLeaderWarrior;

import net.sf.l2j.commons.random.Rnd;

import net.sf.l2j.gameserver.model.actor.Creature;
import net.sf.l2j.gameserver.model.actor.Npc;
import net.sf.l2j.gameserver.model.actor.Player;
import net.sf.l2j.gameserver.skills.L2Skill;

public class PartyLeaderPhysicalSpecialSummon extends PartyLeaderPhysicalSpecial
{
	public PartyLeaderPhysicalSpecialSummon()
	{
		super("ai/individual/Monster/WarriorBase/Warrior/PartyLeaderWarrior");
	}
	
	public PartyLeaderPhysicalSpecialSummon(String descr)
	{
		super(descr);
	}
	
	protected final int[] _npcIds =
	{
		22029
	};
	
	@Override
	public void onCreated(Npc npc)
	{
		npc._i_ai0 = 0;
		npc._i_ai1 = 0;
	}
	
	@Override
	public void onAttacked(Npc npc, Creature attacker, int damage, L2Skill skill)
	{
		if (npc.getStatus().getHpRatio() < 0.5 && npc._i_ai0 < 2 && npc._i_ai1 == 0)
		{
			final int i1 = (Rnd.get(50) - 25);
			final int i2 = (Rnd.get(50) - 25);
			
			createOnePrivateEx(npc, getNpcIntAIParam(npc, "silhouette"), npc.getX() + i1, npc.getY() + i2, npc.getZ(), 0, 0, true, 1000, attacker.getObjectId(), 0);
			npc._i_ai0 = (npc._i_ai0 + 1);
			npc._i_ai1 = 1;
			startQuestTimer("6006", npc, null, 5000);
		}
		
		super.onAttacked(npc, attacker, damage, skill);
	}
	
	@Override
	public String onTimer(String name, Npc npc, Player player)
	{
		if (name.equalsIgnoreCase("6006"))
			npc._i_ai1 = 0;
		
		return super.onTimer(name, npc, null);
	}
}
