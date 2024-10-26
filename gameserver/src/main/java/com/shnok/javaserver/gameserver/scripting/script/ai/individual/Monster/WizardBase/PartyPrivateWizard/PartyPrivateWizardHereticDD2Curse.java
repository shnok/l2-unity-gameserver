package com.shnok.javaserver.gameserver.scripting.script.ai.individual.Monster.WizardBase.PartyPrivateWizard;

import net.sf.l2j.gameserver.enums.IntentionType;
import net.sf.l2j.gameserver.model.World;
import net.sf.l2j.gameserver.model.actor.Creature;
import net.sf.l2j.gameserver.model.actor.Npc;
import net.sf.l2j.gameserver.model.actor.Playable;
import net.sf.l2j.gameserver.skills.L2Skill;

public class PartyPrivateWizardHereticDD2Curse extends PartyPrivateWizardDD2Curse
{
	public PartyPrivateWizardHereticDD2Curse()
	{
		super("ai/individual/Monster/WizardBase/PartyPrivateWizard");
	}
	
	public PartyPrivateWizardHereticDD2Curse(String descr)
	{
		super(descr);
	}
	
	protected final int[] _npcIds =
	{
		22165,
		22168,
		22173
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
						npc.getAI().getHateList().addHateInfo(c0, 200);
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
}
