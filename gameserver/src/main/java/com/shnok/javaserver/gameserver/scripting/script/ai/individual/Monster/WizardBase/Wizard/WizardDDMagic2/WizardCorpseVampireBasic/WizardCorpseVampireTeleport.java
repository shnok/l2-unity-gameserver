package com.shnok.javaserver.gameserver.scripting.script.ai.individual.Monster.WizardBase.Wizard.WizardDDMagic2.WizardCorpseVampireBasic;

import com.shnok.javaserver.gameserver.enums.IntentionType;
import com.shnok.javaserver.gameserver.enums.actors.NpcSkillType;
import com.shnok.javaserver.gameserver.model.actor.Creature;
import com.shnok.javaserver.gameserver.model.actor.Npc;
import com.shnok.javaserver.gameserver.model.actor.Playable;
import com.shnok.javaserver.gameserver.model.actor.Player;
import com.shnok.javaserver.gameserver.skills.L2Skill;

public class WizardCorpseVampireTeleport extends WizardCorpseVampireBasic
{
	public WizardCorpseVampireTeleport()
	{
		super("ai/individual/Monster/WizardBase/Wizard/WizardDDMagic2/WizardCorpseVampireBasic");
	}
	
	public WizardCorpseVampireTeleport(String descr)
	{
		super(descr);
	}
	
	protected final int[] _npcIds =
	{
		21588,
		21589
	};
	
	@Override
	public void onCreated(Npc npc)
	{
		startQuestTimerAtFixedRate("3000", npc, null, 10000, 10000);
		
		super.onCreated(npc);
	}
	
	@Override
	public void onSeeCreature(Npc npc, Creature creature)
	{
		if (!(creature instanceof Playable))
		{
			super.onSeeCreature(npc, creature);
			return;
		}
		
		if (npc.getAI().getCurrentIntention().getType() != IntentionType.ATTACK && npc.getAI().getLifeTime() > 7)
		{
			if (npc.distance2D(creature) > 200)
			{
				npc.abortAll(false);
				npc.teleportTo(creature.getPosition().clone(), 0);
				
				final L2Skill teleportEffect = getNpcSkillByType(npc, NpcSkillType.TELEPORT_EFFECT);
				npc.getAI().addCastDesire(npc, teleportEffect, 1000000);
			}
			
			if (npc.isInMyTerritory())
				npc.getAI().addAttackDesire(creature, 200);
		}
	}
	
	@Override
	public String onTimer(String name, Npc npc, Player player)
	{
		if (name.equalsIgnoreCase("3000"))
		{
			npc.lookNeighbor();
		}
		return super.onTimer(name, npc, player);
	}
}