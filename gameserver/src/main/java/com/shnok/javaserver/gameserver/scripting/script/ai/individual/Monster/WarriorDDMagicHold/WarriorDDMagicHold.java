package com.shnok.javaserver.gameserver.scripting.script.ai.individual.Monster.WarriorDDMagicHold;

import com.shnok.javaserver.gameserver.enums.IntentionType;
import com.shnok.javaserver.gameserver.enums.actors.NpcSkillType;
import com.shnok.javaserver.gameserver.model.actor.Creature;
import com.shnok.javaserver.gameserver.model.actor.Npc;
import com.shnok.javaserver.gameserver.model.actor.Playable;
import com.shnok.javaserver.gameserver.model.actor.Player;
import com.shnok.javaserver.gameserver.scripting.script.ai.individual.Monster.MonsterAI;
import com.shnok.javaserver.gameserver.skills.L2Skill;

public class WarriorDDMagicHold extends MonsterAI
{
	protected static final int SKILL_RANGE = 500;
	
	public WarriorDDMagicHold()
	{
		super("ai/individual/Monster/WarriorDDMagicHold");
	}
	
	public WarriorDDMagicHold(String descr)
	{
		super(descr);
	}
	
	@Override
	public void onAttacked(Npc npc, Creature attacker, int damage, L2Skill skill)
	{
		if (attacker instanceof Playable)
		{
			final IntentionType currentIntentionType = npc.getAI().getCurrentIntention().getType();
			if (currentIntentionType != IntentionType.ATTACK && currentIntentionType != IntentionType.CAST)
				npc.getAI().addCastDesireHold(attacker, getNpcSkillByType(npc, NpcSkillType.DD_MAGIC), 1000000);
			else
			{
				final Creature topDesireTarget = npc.getAI().getTopDesireTarget();
				if (topDesireTarget != null && attacker != topDesireTarget && npc.distance3D(topDesireTarget) > SKILL_RANGE)
				{
					npc.getAI().getAggroList().stopHate(topDesireTarget);
					npc.getAI().addCastDesireHold(attacker, getNpcSkillByType(npc, NpcSkillType.DD_MAGIC), 1000000);
				}
			}
			
			npc.getAI().addAttackDesireHold(attacker, 100);
		}
	}
	
	@Override
	public void onClanAttacked(Npc caller, Npc called, Creature attacker, int damage, L2Skill skill)
	{
		if (attacker instanceof Playable && called.getAI().getLifeTime() > 7)
		{
			final IntentionType currentIntentionType = called.getAI().getCurrentIntention().getType();
			if (currentIntentionType != IntentionType.ATTACK && currentIntentionType != IntentionType.CAST)
				called.getAI().addCastDesireHold(attacker, getNpcSkillByType(called, NpcSkillType.DD_MAGIC), 1000000);
			
			called.getAI().addAttackDesireHold(attacker, 50);
		}
	}
	
	@Override
	public void onUseSkillFinished(Npc npc, Creature creature, L2Skill skill, boolean success)
	{
		final Creature topDesireTarget = npc.getAI().getTopDesireTarget();
		if (topDesireTarget != null)
		{
			if (SKILL_RANGE < npc.distance3D(topDesireTarget))
			{
				npc.getAI().getAggroList().stopHate(topDesireTarget);
				
				startQuestTimer("2001", npc, null, 1000);
				
				return;
			}
			
			if (topDesireTarget instanceof Player)
				npc.getAI().addCastDesireHold(topDesireTarget, getNpcSkillByType(npc, NpcSkillType.DD_MAGIC), 1000000);
		}
	}
	
	@Override
	public String onTimer(String name, Npc npc, Player player)
	{
		if (name.equalsIgnoreCase("2001"))
		{
			final Creature topDesireTarget = npc.getAI().getTopDesireTarget();
			if (topDesireTarget != null)
			{
				if (SKILL_RANGE < npc.distance3D(topDesireTarget))
				{
					npc.getAI().getAggroList().stopHate(topDesireTarget);
					
					startQuestTimer("2001", npc, player, 1000);
					
					return super.onTimer(name, npc, player);
				}
				
				if (topDesireTarget instanceof Player)
					npc.getAI().addCastDesireHold(topDesireTarget, getNpcSkillByType(npc, NpcSkillType.DD_MAGIC), 1000000);
			}
		}
		return super.onTimer(name, npc, player);
	}
}