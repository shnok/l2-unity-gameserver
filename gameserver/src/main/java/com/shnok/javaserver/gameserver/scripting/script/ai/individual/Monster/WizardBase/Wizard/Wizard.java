package com.shnok.javaserver.gameserver.scripting.script.ai.individual.Monster.WizardBase.Wizard;

import com.shnok.javaserver.commons.random.Rnd;

import com.shnok.javaserver.gameserver.enums.IntentionType;
import com.shnok.javaserver.gameserver.model.actor.Creature;
import com.shnok.javaserver.gameserver.model.actor.Npc;
import com.shnok.javaserver.gameserver.model.actor.Playable;
import com.shnok.javaserver.gameserver.model.actor.Player;
import com.shnok.javaserver.gameserver.model.actor.container.attackable.HateList;
import com.shnok.javaserver.gameserver.network.NpcStringId;
import com.shnok.javaserver.gameserver.scripting.script.ai.individual.Monster.WizardBase.WizardBase;
import com.shnok.javaserver.gameserver.skills.L2Skill;

public class Wizard extends WizardBase
{
	public Wizard()
	{
		super("ai/individual/Monster/WizardBase/Wizard");
	}
	
	public Wizard(String descr)
	{
		super(descr);
	}
	
	@Override
	public void onNoDesire(Npc npc)
	{
		npc.getAI().addWanderDesire(5, 5);
	}
	
	@Override
	public void onCreated(Npc npc)
	{
		final int shoutMsg1 = getNpcIntAIParam(npc, "ShoutMsg1");
		if (shoutMsg1 > 0)
			npc.broadcastNpcShout(NpcStringId.get(shoutMsg1));
		
		if (getNpcIntAIParam(npc, "MoveAroundSocial") > 0 || getNpcIntAIParam(npc, "ShoutMsg2") > 0 || getNpcIntAIParam(npc, "ShoutMsg3") > 0)
			startQuestTimerAtFixedRate("1001", npc, null, 10000, 10000);
		
		startQuestTimerAtFixedRate("1002", npc, null, 10000, 10000);
		
		npc._i_ai0 = 0;
		
		super.onCreated(npc);
	}
	
	@Override
	public String onTimer(String name, Npc npc, Player player)
	{
		final HateList hateList = npc.getAI().getHateList();
		
		if (name.equalsIgnoreCase("1001"))
		{
			switch (npc.getAI().getCurrentIntention().getType())
			{
				case IDLE, WANDER:
					final int moveAroundSocial2 = getNpcIntAIParam(npc, "MoveAroundSocial2");
					final int moveAroundSocial1 = getNpcIntAIParam(npc, "MoveAroundSocial1");
					final int moveAroundSocial = getNpcIntAIParam(npc, "MoveAroundSocial");
					
					if (moveAroundSocial2 > 0 && Rnd.get(100) < 20)
						npc.getAI().addSocialDesire(3, moveAroundSocial2 * 1000 / 30, 50);
					else if (moveAroundSocial1 > 0 && Rnd.get(100) < 20)
						npc.getAI().addSocialDesire(2, moveAroundSocial1 * 1000 / 30, 50);
					else if (moveAroundSocial > 0 && Rnd.get(100) < 20)
						npc.getAI().addSocialDesire(1, moveAroundSocial * 1000 / 30, 50);
					break;
				
				case ATTACK:
					final int shoutMsg3 = getNpcIntAIParam(npc, "ShoutMsg3");
					if (shoutMsg3 > 0 && Rnd.get(100) < 10)
						npc.broadcastNpcShout(NpcStringId.get(shoutMsg3));
					break;
			}
		}
		else if (name.equalsIgnoreCase("1002"))
		{
			hateList.refresh();
			hateList.removeIfOutOfRange(1000);
		}
		else if (name.equalsIgnoreCase("1003"))
		{
			if (npc.isMuted())
				startQuestTimer("1003", npc, null, 10000);
			else
			{
				npc.removeAllAttackDesire();
				
				npc._i_ai0 = 0;
				
				final Creature mostHated = hateList.getMostHatedCreature();
				if (mostHated != null)
					onAttacked(npc, mostHated, 100, null);
			}
		}
		return super.onTimer(name, npc, player);
	}
	
	@Override
	public void onAttacked(Npc npc, Creature attacker, int damage, L2Skill skill)
	{
		if (attacker instanceof Playable)
		{
			final HateList hateList = npc.getAI().getHateList();
			
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
	public void onClanAttacked(Npc caller, Npc called, Creature attacker, int damage, L2Skill skill)
	{
		if (attacker instanceof Playable && called.getAI().getLifeTime() > 7)
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
		if (skill.getAggroPoints() > 0 && !skill.isOffensive())
		{
			final HateList hateList = npc.getAI().getHateList();
			
			double f0 = getHateRatio(npc, caster);
			f0 = (((1. * skill.getAggroPoints()) / (npc.getStatus().getLevel() + 7)) + ((f0 / 100) * ((1. * skill.getAggroPoints()) / (npc.getStatus().getLevel() + 7))));
			
			final Creature mostHated = npc.getAI().getAggroList().getMostHatedCreature();
			if (npc.getAI().getCurrentIntention().getType() == IntentionType.ATTACK && mostHated != null && mostHated == caster)
				hateList.addHateInfo(caster, f0 * 150);
			else
				hateList.addHateInfo(caster, f0 * 75);
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
}
