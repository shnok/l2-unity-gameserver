package com.shnok.javaserver.gameserver.scripting.script.ai.individual.Monster.WizardBase.PartyLeaderWizard;

import com.shnok.javaserver.commons.random.Rnd;

import com.shnok.javaserver.gameserver.enums.IntentionType;
import com.shnok.javaserver.gameserver.enums.actors.NpcSkillType;
import com.shnok.javaserver.gameserver.model.actor.Creature;
import com.shnok.javaserver.gameserver.model.actor.Npc;
import com.shnok.javaserver.gameserver.model.actor.Playable;
import com.shnok.javaserver.gameserver.skills.L2Skill;

public class PartyLeaderWizardHereticAggressive extends PartyLeaderWizardDD2CurseAggressive
{
	public PartyLeaderWizardHereticAggressive()
	{
		super("ai/individual/Monster/WizardBase/PartyLeaderWizard");
	}
	
	public PartyLeaderWizardHereticAggressive(String descr)
	{
		super(descr);
	}
	
	protected final int[] _npcIds =
	{
		22163,
		22171
	};
	
	@Override
	public void onCreated(Npc npc)
	{
		npc._i_ai3 = 0;
		
		super.onCreated(npc);
		
		npc.getAI().addMoveToDesire(npc.getSpawnLocation(), 30);
	}
	
	@Override
	public void onNoDesire(Npc npc)
	{
		npc.getAI().addMoveToDesire(npc.getSpawnLocation(), 30);
	}
	
	@Override
	public void onAttacked(Npc npc, Creature attacker, int damage, L2Skill skill)
	{
		if (npc.getAI().getCurrentIntention().getType() != IntentionType.ATTACK && npc.getAI().getCurrentIntention().getType() != IntentionType.CAST)
			broadcastScriptEventEx(npc, 10033, attacker.getObjectId(), npc.getObjectId(), getNpcIntAIParamOrDefault(npc, "HelpCastRange", 500));
		
		super.onAttacked(npc, attacker, damage, skill);
	}
	
	@Override
	public void onPartyAttacked(Npc caller, Npc called, Creature target, int damage)
	{
		if (called.getStatus().getHpRatio() < 0.4 && Rnd.get(100) < 33 && called._i_ai3 == 0)
			called.lookNeighbor();
		
		super.onPartyAttacked(caller, called, target, damage);
	}
	
	@Override
	public void onSeeCreature(Npc npc, Creature creature)
	{
		if (creature instanceof Playable && creature == npc)
		{
			npc.getAI().addCastDesire(creature, getNpcSkillByType(npc, NpcSkillType.SPECIAL_ATTACK), 1000000);
			npc.getAI().addCastDesire(npc, getNpcSkillByType(npc, NpcSkillType.SELF_MAGIC_HEAL), 1000000);
		}
		
		if (npc.getAI().getLifeTime() > 7 && npc.isInMyTerritory() && npc.getAI().getHateList().size() == 0 && npc.getAI().getCurrentIntention().getType() != IntentionType.ATTACK && npc.getAI().getCurrentIntention().getType() != IntentionType.CAST)
			broadcastScriptEventEx(npc, 10033, creature.getObjectId(), npc.getObjectId(), getNpcIntAIParamOrDefault(npc, "HelpCastRange", 500));
		
		super.onSeeCreature(npc, creature);
	}
	
	@Override
	public void onUseSkillFinished(Npc npc, Creature creature, L2Skill skill, boolean success)
	{
		if (skill == getNpcSkillByType(npc, NpcSkillType.SELF_MAGIC_HEAL) && success)
			npc._i_ai3 = 1;
		
		super.onUseSkillFinished(npc, creature, skill, success);
	}
	
	@Override
	public void onOutOfTerritory(Npc npc)
	{
		npc.removeAllAttackDesire();
		npc.getAI().addMoveToDesire(npc.getSpawnLocation(), 100);
		npc.getAI().getHateList().cleanAllHate();
		broadcastScriptEvent(npc, 10035, npc.getObjectId(), getNpcIntAIParamOrDefault(npc, "DistNoDesire", 500));
	}
}
