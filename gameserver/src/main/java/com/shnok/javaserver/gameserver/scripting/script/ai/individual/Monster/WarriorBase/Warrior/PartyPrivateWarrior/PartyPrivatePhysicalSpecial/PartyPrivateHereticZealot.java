package com.shnok.javaserver.gameserver.scripting.script.ai.individual.Monster.WarriorBase.Warrior.PartyPrivateWarrior.PartyPrivatePhysicalSpecial;

import com.shnok.javaserver.gameserver.enums.IntentionType;
import com.shnok.javaserver.gameserver.enums.actors.NpcSkillType;
import com.shnok.javaserver.gameserver.model.World;
import com.shnok.javaserver.gameserver.model.actor.Creature;
import com.shnok.javaserver.gameserver.model.actor.Npc;
import com.shnok.javaserver.gameserver.model.actor.Playable;
import com.shnok.javaserver.gameserver.skills.L2Skill;

public class PartyPrivateHereticZealot extends PartyPrivatePhysicalSpecial
{
	public PartyPrivateHereticZealot()
	{
		super("ai/individual/Monster/WarriorBase/Warrior/PartyPrivateWarrior/PartyPrivatePhysicalSpecial");
	}
	
	public PartyPrivateHereticZealot(String descr)
	{
		super(descr);
	}
	
	protected final int[] _npcIds =
	{
		22161,
		22169,
		22172,
		22190
	};
	
	@Override
	public void onCreated(Npc npc)
	{
		npc._i_ai4 = 0;
		
		super.onCreated(npc);
	}
	
	@Override
	public void onAttacked(Npc npc, Creature attacker, int damage, L2Skill skill)
	{
		if ((npc.getAI().getCurrentIntention().getType() != IntentionType.ATTACK && npc.getAI().getCurrentIntention().getType() != IntentionType.CAST) && npc._i_ai4 == 0)
			npc._i_ai4 = 1;
		
		super.onAttacked(npc, attacker, damage, skill);
	}
	
	@Override
	public void onPartyAttacked(Npc caller, Npc called, Creature target, int damage)
	{
		if ((called.getAI().getCurrentIntention().getType() != IntentionType.ATTACK && called.getAI().getCurrentIntention().getType() != IntentionType.CAST) && called._i_ai4 == 0)
			called._i_ai4 = 1;
		
		super.onPartyAttacked(caller, called, target, damage);
	}
	
	@Override
	public void onOutOfTerritory(Npc npc)
	{
		if (npc._i_ai4 == 0)
		{
			npc.removeAllAttackDesire();
			npc.getAI().addMoveToDesire(npc.getSpawnLocation(), 100);
		}
	}
	
	@Override
	public void onScriptEvent(Npc npc, int eventId, int arg1, int arg2)
	{
		if (npc.hasMaster() && arg2 == npc.getMaster().getObjectId())
		{
			if (eventId == 10033 || eventId == 10002)
			{
				final Creature c0 = (Creature) World.getInstance().getObject(arg1);
				if (c0 != null)
				{
					if (eventId == 10033)
						npc._i_ai4 = 1;
					
					npc.removeAllAttackDesire();
					
					if (c0 instanceof Playable)
						npc.getAI().addAttackDesire(c0, 200);
				}
			}
			else if (eventId == 10035)
			{
				npc._i_ai4 = 0;
				npc.removeAllAttackDesire();
				npc.getAI().addMoveToDesire(npc.getSpawnLocation(), 100);
			}
		}
	}
	
	@Override
	public void onPartyDied(Npc caller, Npc called)
	{
		if (caller == called.getMaster())
		{
			called._i_ai0 = 1;
			final Creature topDesireTarget = called.getAI().getTopDesireTarget();
			if (topDesireTarget != null)
				called.getAI().addCastDesire(topDesireTarget, getNpcSkillByType(called, NpcSkillType.PHYSICAL_SPECIAL2), 1000000);
		}
		
		super.onPartyDied(caller, called);
	}
	
	@Override
	public void onUseSkillFinished(Npc npc, Creature creature, L2Skill skill, boolean success)
	{
		if (skill == getNpcSkillByType(npc, NpcSkillType.PHYSICAL_SPECIAL2) && success)
			npc.doDie(npc);
	}
}
