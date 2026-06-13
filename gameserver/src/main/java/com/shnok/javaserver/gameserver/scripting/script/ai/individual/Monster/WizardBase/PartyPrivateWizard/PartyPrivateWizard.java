package com.shnok.javaserver.gameserver.scripting.script.ai.individual.Monster.WizardBase.PartyPrivateWizard;

import com.shnok.javaserver.commons.random.Rnd;

import com.shnok.javaserver.gameserver.enums.IntentionType;
import com.shnok.javaserver.gameserver.model.World;
import com.shnok.javaserver.gameserver.model.actor.Creature;
import com.shnok.javaserver.gameserver.model.actor.Npc;
import com.shnok.javaserver.gameserver.model.actor.Playable;
import com.shnok.javaserver.gameserver.model.actor.Player;
import com.shnok.javaserver.gameserver.model.actor.container.attackable.HateList;
import com.shnok.javaserver.gameserver.network.NpcStringId;
import com.shnok.javaserver.gameserver.scripting.script.ai.individual.Monster.WizardBase.WizardBase;
import com.shnok.javaserver.gameserver.skills.L2Skill;

public class PartyPrivateWizard extends WizardBase
{
	public PartyPrivateWizard()
	{
		super("ai/individual/Monster/WizardBase/PartyPrivateWizard");
	}
	
	public PartyPrivateWizard(String descr)
	{
		super(descr);
	}
	
	@Override
	public void onCreated(Npc npc)
	{
		startQuestTimerAtFixedRate("1002", npc, null, 10000, 10000);
		startQuestTimerAtFixedRate("1006", npc, null, 10000, 10000);
		
		npc._i_ai0 = 0;
		
		super.onCreated(npc);
	}
	
	@Override
	public void onNoDesire(Npc npc)
	{
		final Npc master = npc.getMaster();
		if (master != null && !master.isDead())
			npc.getAI().addFollowDesire(master, 50);
		else
			npc.getAI().addWanderDesire(5, 5);
	}
	
	@Override
	public String onTimer(String name, Npc npc, Player player)
	{
		final HateList hateList = npc.getAI().getHateList();
		
		if (name.equalsIgnoreCase("1002"))
		{
			hateList.refresh();
			hateList.removeIfOutOfRange(1000);
		}
		else if (name.equalsIgnoreCase("1003"))
		{
			if (npc.isMuted())
				startQuestTimer("1003", npc, player, 10000);
			else
			{
				npc.removeAllAttackDesire();
				
				npc._i_ai0 = 0;
				
				final Creature mostHated = hateList.getMostHatedCreature();
				if (mostHated != null)
					onAttacked(npc, mostHated, 100, null);
			}
		}
		else if (name.equalsIgnoreCase("1006"))
		{
			if (npc.hasMaster() && npc.getMaster().isDead())
			{
				if (npc.getAI().getCurrentIntention().getType() != IntentionType.ATTACK && npc.getAI().getCurrentIntention().getType() != IntentionType.CAST)
				{
					npc.decayMe();
					return null;
				}
			}
		}
		return super.onTimer(name, npc, player);
	}
	
	@Override
	public void onAttacked(Npc npc, Creature attacker, int damage, L2Skill skill)
	{
		final HateList hateList = npc.getAI().getHateList();
		
		if (attacker instanceof Playable)
		{
			double f0 = getHateRatio(npc, attacker);
			f0 = (((1.0 * damage) / (npc.getStatus().getLevel() + 7)) + ((f0 / 100) * ((1.0 * damage) / (npc.getStatus().getLevel() + 7))));
			
			if (hateList.isEmpty())
				hateList.addHateInfo(attacker, (f0 * 100) + 300);
			else
				hateList.addHateInfo(attacker, f0 * 100);
		}
		
		if (npc.isMuted())
		{
			npc._i_ai0 = 1;
			
			startQuestTimer("1003", npc, null, 10000);
		}
		super.onAttacked(npc, attacker, damage, skill);
	}
	
	@Override
	public void onPartyAttacked(Npc caller, Npc called, Creature target, int damage)
	{
		if (target instanceof Playable)
		{
			final HateList hateList = called.getAI().getHateList();
			
			double f0 = getHateRatio(called, target);
			f0 = (((1.0 * damage) / (called.getStatus().getLevel() + 7)) + ((f0 / 100) * ((1.0 * damage) / (called.getStatus().getLevel() + 7))));
			
			if (hateList.isEmpty())
				hateList.addHateInfo(target, (f0 * 10) + 300);
			else
				hateList.addHateInfo(target, f0 * 10);
		}
	}
	
	@Override
	public void onClanAttacked(Npc caller, Npc called, Creature attacker, int damage, L2Skill skill)
	{
		if (attacker instanceof Playable && called.hasMaster() && called.getMaster().isDead() && called.getAI().getLifeTime() > 7)
		{
			final HateList hateList = called.getAI().getHateList();
			
			double f0 = getHateRatio(called, attacker);
			f0 = (((1.0 * damage) / (called.getStatus().getLevel() + 7)) + ((f0 / 100) * ((1.0 * damage) / (called.getStatus().getLevel() + 7))));
			
			if (hateList.isEmpty())
				hateList.addHateInfo(attacker, (f0 * 30) + 300);
			else
				hateList.addHateInfo(attacker, f0 * 30);
		}
	}
	
	@Override
	public void onSeeSpell(Npc npc, Player caster, L2Skill skill, Creature[] targets, boolean isPet)
	{
		if (npc.hasMaster() && npc.getMaster().isDead())
		{
			final int effectPoints = skill.getAggroPoints();
			if (effectPoints > 0 && !skill.isOffensive())
			{
				final HateList hateList = npc.getAI().getHateList();
				
				double f0 = getHateRatio(npc, caster);
				f0 = (((1. * effectPoints) / (npc.getStatus().getLevel() + 7)) + ((f0 / 100) * ((1. * effectPoints) / (npc.getStatus().getLevel() + 7))));
				
				final Creature mostHated = npc.getAI().getAggroList().getMostHatedCreature();
				if (npc.getAI().getCurrentIntention().getType() == IntentionType.ATTACK && mostHated != null && mostHated == caster)
					hateList.addHateInfo(caster, f0 * 150);
				else
					hateList.addHateInfo(caster, f0 * 75);
			}
		}
	}
	
	// EventHandler DESIRE_MANIPULATION(speller,desire)
	// {
	// myself::MakeAttackEvent(speller,desire,0);
	// }
	
	@Override
	public void onMyDying(Npc npc, Creature killer)
	{
		final int shoutMsg4 = getNpcIntAIParam(npc, "ShoutMsg4");
		if (shoutMsg4 > 0 && Rnd.get(100) < 30)
			npc.broadcastNpcShout(NpcStringId.get(shoutMsg4));
	}
	
	@Override
	public void onScriptEvent(Npc npc, int eventId, int arg1, int arg2)
	{
		if (npc.isDead())
			return;
		
		if (eventId == 10002)
		{
			final Npc master = npc.getMaster();
			if (master != null && arg1 == master.getObjectId())
			{
				final Creature topDesireTarget = npc.getAI().getTopDesireTarget();
				final Creature creatureFromFlag = (Creature) World.getInstance().getObject(master._flag);
				
				if (topDesireTarget != null && topDesireTarget == creatureFromFlag)
					return;
				
				switch (Rnd.get(4))
				{
					case 0:
						npc.broadcastNpcSay(NpcStringId.ID_1000292);
						break;
					
					case 1:
						npc.broadcastNpcSay(NpcStringId.ID_1000400);
						break;
					
					case 2:
						npc.broadcastNpcSay(NpcStringId.ID_1000401);
						break;
					
					case 3:
						npc.broadcastNpcSay(NpcStringId.ID_1000402);
						break;
				}
				
				final HateList hateList = npc.getAI().getHateList();
				hateList.refresh();
				hateList.addHateInfo(creatureFromFlag, 2000);
			}
		}
	}
}