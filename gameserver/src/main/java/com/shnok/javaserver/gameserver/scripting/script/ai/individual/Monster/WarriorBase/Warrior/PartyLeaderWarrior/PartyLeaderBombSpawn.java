package com.shnok.javaserver.gameserver.scripting.script.ai.individual.Monster.WarriorBase.Warrior.PartyLeaderWarrior;

import com.shnok.javaserver.gameserver.enums.IntentionType;
import com.shnok.javaserver.gameserver.model.World;
import com.shnok.javaserver.gameserver.model.actor.Creature;
import com.shnok.javaserver.gameserver.model.actor.Npc;
import com.shnok.javaserver.gameserver.model.actor.Playable;

public class PartyLeaderBombSpawn extends PartyLeaderWarrior
{
	public PartyLeaderBombSpawn()
	{
		super("ai/individual/Monster/WarriorBase/Warrior/PartyLeaderWarrior");
	}
	
	public PartyLeaderBombSpawn(String descr)
	{
		super(descr);
	}
	
	protected final int[] _npcIds =
	{
		21517,
		21512
	};
	
	@Override
	public void onCreated(Npc npc)
	{
		npc._i_ai0 = 0;
		npc._weightPoint = 10;
	}
	
	@Override
	public void onSeeCreature(Npc npc, Creature creature)
	{
		if (creature instanceof Playable && npc._i_ai0 == 0)
		{
			npc._i_ai0 = 1;
			
			if (getNpcIntAIParam(npc, "SummonPrivateRate") == 0)
				createPrivates(npc);
			
			tryToAttack(npc, creature);
		}
		super.onSeeCreature(npc, creature);
	}
	
	@Override
	public void onScriptEvent(Npc npc, int eventId, int arg1, int arg2)
	{
		if (npc.isDead())
			return;
		
		if (eventId == 10000)
		{
			if (npc.getAI().getCurrentIntention().getType() != IntentionType.ATTACK)
			{
				final Creature c0 = (Creature) World.getInstance().getObject(arg1);
				if (c0 != null)
					npc.getAI().addAttackDesire(c0, 1000);
			}
		}
		super.onScriptEvent(npc, eventId, arg1, arg2);
	}
}