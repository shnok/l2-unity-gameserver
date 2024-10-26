package com.shnok.javaserver.gameserver.scripting.script.ai.individual.Monster.WarriorBase.Warrior.PartyLeaderWarrior.PartyLeaderPhysicalSpecial2;

import net.sf.l2j.commons.random.Rnd;

import net.sf.l2j.gameserver.enums.IntentionType;
import net.sf.l2j.gameserver.enums.actors.ClassId;
import net.sf.l2j.gameserver.enums.actors.NpcSkillType;
import net.sf.l2j.gameserver.model.actor.Creature;
import net.sf.l2j.gameserver.model.actor.Npc;
import net.sf.l2j.gameserver.model.actor.Playable;
import net.sf.l2j.gameserver.model.actor.Player;
import net.sf.l2j.gameserver.skills.L2Skill;

public class PartyLeaderHeretic2Aggressive extends PartyLeaderPhysicalSpecial2
{
	public PartyLeaderHeretic2Aggressive()
	{
		super("ai/individual/Monster/WarriorBase/Warrior/PartyLeaderWarrior/PartyLeaderPhysicalSpecial2");
	}
	
	public PartyLeaderHeretic2Aggressive(String descr)
	{
		super(descr);
	}
	
	protected final int[] _npcIds =
	{
		22155,
		22159,
		22167
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
	public void onSeeCreature(Npc npc, Creature creature)
	{
		if (npc.getAI().getCurrentIntention().getType() != IntentionType.ATTACK && npc.getAI().getCurrentIntention().getType() != IntentionType.CAST)
			broadcastScriptEventEx(npc, 10033, creature.getObjectId(), npc.getObjectId(), getNpcIntAIParamOrDefault(npc, "HelpCastRange", 500));
		
		if (!(creature instanceof Playable))
			return;
		
		tryToAttack(npc, creature);
		
		if (creature != npc)
		{
			npc.getAI().addCastDesire(creature, getNpcSkillByType(npc, NpcSkillType.SPECIAL_ATTACK), 1000000);
			npc.getAI().addCastDesire(npc, getNpcSkillByType(npc, NpcSkillType.SELF_MAGIC_HEAL), 1000000);
		}
	}
	
	@Override
	public void onAttacked(Npc npc, Creature attacker, int damage, L2Skill skill)
	{
		if (npc.getAI().getCurrentIntention().getType() != IntentionType.ATTACK && npc.getAI().getCurrentIntention().getType() != IntentionType.CAST)
			broadcastScriptEventEx(npc, 10033, attacker.getObjectId(), npc.getObjectId(), getNpcIntAIParamOrDefault(npc, "HelpCastRange", 500));
		
		if (npc.getStatus().getHpRatio() < 0.4 && Rnd.get(100) < 33)
		{
			final int playerClassID = attacker.getActingPlayer().getClassId().getId();
			final boolean isInClericGroup = ClassId.isInGroup(attacker.getActingPlayer(), "@cleric_group");
			
			if (Rnd.get(100) < 33 && (playerClassID == 5 || playerClassID == 90 || isInClericGroup))
				broadcastScriptEventEx(npc, 10002, attacker.getObjectId(), npc.getObjectId(), getNpcIntAIParamOrDefault(npc, "HelpCastRange", 500));
		}
		
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
	public void onUseSkillFinished(Npc npc, Creature creature, L2Skill skill, boolean success)
	{
		if (skill == getNpcSkillByType(npc, NpcSkillType.SELF_MAGIC_HEAL) && success)
			npc._i_ai3 = 1;
	}
	
	@Override
	public void onSeeSpell(Npc npc, Player caster, L2Skill skill, Creature[] targets, boolean isPet)
	{
		if (npc.getStatus().getHpRatio() < 0.4 && Rnd.get(100) < 33 && ClassId.isInGroup(caster.getActingPlayer(), "@cleric_group"))
			broadcastScriptEventEx(npc, 10002, caster.getObjectId(), npc.getObjectId(), getNpcIntAIParamOrDefault(npc, "HelpCastRange", 500));
		
		super.onSeeSpell(npc, caster, skill, targets, isPet);
	}
	
	@Override
	public void onOutOfTerritory(Npc npc)
	{
		npc.removeAllAttackDesire();
		npc.getAI().addMoveToDesire(npc.getSpawnLocation(), 100);
		broadcastScriptEvent(npc, 10035, npc.getObjectId(), getNpcIntAIParamOrDefault(npc, "DistNoDesire", 500));
	}
}
