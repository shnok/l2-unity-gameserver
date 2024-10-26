package com.shnok.javaserver.gameserver.scripting.script.ai.individual.Monster.WarriorBase.Warrior.WarriorHold;

import net.sf.l2j.commons.random.Rnd;

import net.sf.l2j.gameserver.enums.actors.NpcSkillType;
import net.sf.l2j.gameserver.model.actor.Creature;
import net.sf.l2j.gameserver.model.actor.Npc;
import net.sf.l2j.gameserver.model.actor.Player;
import net.sf.l2j.gameserver.skills.L2Skill;

public class PrimevalPlant extends WarriorHold
{
	public PrimevalPlant()
	{
		super("ai/individual/Monster/WarriorBase/Warrior/WarriorHold");
	}
	
	public PrimevalPlant(String descr)
	{
		super(descr);
	}
	
	protected final int[] _npcIds =
	{
		18345,
		18346
	};
	
	@Override
	public void onCreated(Npc npc)
	{
		startQuestTimerAtFixedRate("5001", npc, null, 15000, 15000);
	}
	
	@Override
	public void onAttacked(Npc npc, Creature attacker, int damage, L2Skill skill)
	{
		// Do nothing
	}
	
	@Override
	public void onScriptEvent(Npc npc, int eventId, int arg1, int arg2)
	{
		// Do nothing
	}
	
	@Override
	public String onTimer(String name, Npc npc, Player player)
	{
		if (name.equalsIgnoreCase("5001"))
		{
			if (Rnd.get(100) <= getNpcIntAIParam(npc, "ProbSelfRangeDeBuff1"))
				npc.getAI().addCastDesire(npc, getNpcSkillByType(npc, NpcSkillType.SELF_RANGE_DEBUFF1), 1000000);
		}
		return super.onTimer(name, npc, player);
	}
}