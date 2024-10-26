package com.shnok.javaserver.gameserver.scripting.script.ai.individual.Monster.WarriorBase.Warrior.PartyLeaderWarrior.PartyLeaderPhysicalSpecial2;

import net.sf.l2j.gameserver.enums.IntentionType;
import net.sf.l2j.gameserver.model.actor.Creature;
import net.sf.l2j.gameserver.model.actor.Npc;
import net.sf.l2j.gameserver.model.actor.Playable;
import net.sf.l2j.gameserver.skills.L2Skill;

public class PartyLeaderHereticAggressive extends PartyLeaderPhysicalSpecial2
{
	public PartyLeaderHereticAggressive()
	{
		super("ai/individual/Monster/WarriorBase/Warrior/PartyLeaderWarrior/PartyLeaderPhysicalSpecial2");
	}
	
	public PartyLeaderHereticAggressive(String descr)
	{
		super(descr);
	}
	
	protected final int[] _npcIds =
	{
		22188
	};
	
	@Override
	public void onCreated(Npc npc)
	{
		npc._i_ai3 = 0;
		super.onCreated(npc);
	}
	
	@Override
	public void onAttacked(Npc npc, Creature attacker, int damage, L2Skill skill)
	{
		if (npc.getAI().getCurrentIntention().getType() != IntentionType.ATTACK && npc.getAI().getCurrentIntention().getType() != IntentionType.CAST)
			broadcastScriptEventEx(npc, 10033, attacker.getObjectId(), npc.getObjectId(), getNpcIntAIParamOrDefault(npc, "HelpCastRange", 500));
		
		super.onAttacked(npc, attacker, damage, skill);
	}
	
	@Override
	public void onSeeCreature(Npc npc, Creature creature)
	{
		if (npc.getAI().getCurrentIntention().getType() != IntentionType.ATTACK && npc.getAI().getCurrentIntention().getType() != IntentionType.CAST)
			broadcastScriptEventEx(npc, 10033, creature.getObjectId(), npc.getObjectId(), getNpcIntAIParamOrDefault(npc, "HelpCastRange", 500));
		
		if (!(creature instanceof Playable))
			return;
		
		tryToAttack(npc, creature);
	}
	
	@Override
	public void onUseSkillFinished(Npc npc, Creature creature, L2Skill skill, boolean success)
	{
		// Do nothing
	}
	
	@Override
	public void onOutOfTerritory(Npc npc)
	{
		npc.removeAllAttackDesire();
		npc.getAI().addMoveToDesire(npc.getSpawnLocation(), 100);
		broadcastScriptEvent(npc, 10035, npc.getObjectId(), getNpcIntAIParamOrDefault(npc, "DistNoDesire", 500));
	}
	
	@Override
	public void onMyDying(Npc npc, Creature killer)
	{
		broadcastScriptEventEx(npc, 2, 0, npc.getObjectId(), 4000);
	}
}
