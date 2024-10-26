package com.shnok.javaserver.gameserver.scripting.script.ai.individual.Monster.LV3Monster;

import net.sf.l2j.gameserver.model.actor.Creature;
import net.sf.l2j.gameserver.model.actor.Npc;
import net.sf.l2j.gameserver.model.actor.Playable;

public class LV3Healer extends LV3PartyLeaderMonster
{
	public LV3Healer()
	{
		super("ai/individual/Monster/LV3Monster");
	}
	
	public LV3Healer(String descr)
	{
		super(descr);
	}
	
	protected final int[] _npcIds =
	{
		27266,
		27267
	};
	
	@Override
	public void onPartyAttacked(Npc caller, Npc called, Creature target, int damage)
	{
		if (caller != called && target instanceof Playable)
		{
			double f0 = getHateRatio(called, target);
			f0 = (((1.0 * damage) / (called.getStatus().getLevel() + 7)) + ((f0 / 100) * ((1.0 * damage) / (called.getStatus().getLevel() + 7))));
			called.getAI().addAttackDesire(target, ((f0 * damage) * caller._weightPoint) * 10);
		}
		
		super.onPartyAttacked(caller, called, target, damage);
	}
	
	@Override
	public void onPartyDied(Npc caller, Npc called)
	{
		if (caller != called)
			createOnePrivate(called, caller.getNpcId(), 0, true);
	}
}
