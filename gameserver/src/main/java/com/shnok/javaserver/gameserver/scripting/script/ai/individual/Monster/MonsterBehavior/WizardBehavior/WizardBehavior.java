package com.shnok.javaserver.gameserver.scripting.script.ai.individual.Monster.MonsterBehavior.WizardBehavior;

import com.shnok.javaserver.gameserver.enums.IntentionType;
import com.shnok.javaserver.gameserver.model.actor.Creature;
import com.shnok.javaserver.gameserver.model.actor.Npc;
import com.shnok.javaserver.gameserver.model.actor.Playable;
import com.shnok.javaserver.gameserver.model.actor.Player;
import com.shnok.javaserver.gameserver.model.actor.container.attackable.HateList;
import com.shnok.javaserver.gameserver.scripting.script.ai.individual.Monster.MonsterBehavior.MonsterBehavior;
import com.shnok.javaserver.gameserver.skills.L2Skill;

public class WizardBehavior extends MonsterBehavior
{
	protected static final double ATTACK_BOOST_VALUE = 300.0;
	protected static final double USE_SKILL_BOOST_VALUE = 100000.0;
	protected static final double ATTACKED_WEIGHT_POINT = 10.0;
	protected static final double CLAN_ATTACKED_WEIGHT_POINT = 1.0;
	protected static final double PARTY_ATTACKED_WEIGHT_POINT = 1.0;
	protected static final double SEE_SPELL_WEIGHT_POINT = 10.0;
	protected static final double HATE_SKILL_WEIGHT_POINT = 10.0;
	
	public WizardBehavior()
	{
		super("ai/WizardBehavior");
	}
	
	public WizardBehavior(String descr)
	{
		super(descr);
	}
	
	@Override
	public void onCreated(Npc npc)
	{
		if (getNpcIntAIParam(npc, "AttackRange") == 2)
			npc._i_ai4 = 0;
		
		startQuestTimerAtFixedRate("5002", npc, null, 10000, 10000);
		
		super.onCreated(npc);
	}
	
	@Override
	public void onAttacked(Npc npc, Creature attacker, int damage, L2Skill skill)
	{
		if (attacker instanceof Playable)
		{
			final HateList hateList = npc.getAI().getHateList();
			if (hateList.size() == 0)
				hateList.addHateInfo(attacker, (damage * ATTACKED_WEIGHT_POINT) + ATTACK_BOOST_VALUE);
			else
				hateList.addHateInfo(attacker, (damage * ATTACKED_WEIGHT_POINT));
		}
		
		if (npc.isMuted())
		{
			npc._i_ai4 = 1;
			startQuestTimer("5001", npc, null, 10000);
		}
		
		super.onAttacked(npc, attacker, damage, skill);
	}
	
	@Override
	public String onTimer(String name, Npc npc, Player player)
	{
		if (name.equalsIgnoreCase("5001"))
		{
			if (npc.isMuted())
				startQuestTimer("5001", npc, null, 10000);
			else
			{
				npc.removeAllAttackDesire();
				npc._i_ai4 = 0;
				
				final Creature mostHatedHI = npc.getAI().getHateList().getMostHatedCreature();
				if (mostHatedHI != null)
					onAttacked(npc, mostHatedHI, 100, null);
			}
		}
		else if (name.equalsIgnoreCase("5002"))
		{
			npc.getAI().getHateList().refresh();
			npc.getAI().getHateList().removeIfOutOfRange(2000);
		}
		
		return super.onTimer(name, npc, player);
	}
	
	@Override
	public void onClanAttacked(Npc caller, Npc called, Creature attacker, int damage, L2Skill skill)
	{
		if (called.getAI().getLifeTime() > getNpcIntAIParam(called, "Aggressive_Time"))
		{
			final int partyType = getNpcIntAIParam(called, "Party_Type");
			final int partyLoyalty = getNpcIntAIParam(called, "Party_Loyalty");
			
			if (((partyType == 0 || (partyType == 1 && partyLoyalty == 0)) || partyType == 2) && attacker instanceof Playable)
			{
				final HateList hateList = called.getAI().getHateList();
				if (hateList.size() == 0)
					hateList.addHateInfo(attacker, (damage * CLAN_ATTACKED_WEIGHT_POINT) + ATTACK_BOOST_VALUE);
				else
					hateList.addHateInfo(attacker, (damage * CLAN_ATTACKED_WEIGHT_POINT));
			}
		}
		
		super.onClanAttacked(caller, called, attacker, damage, skill);
	}
	
	@Override
	public void onPartyAttacked(Npc caller, Npc called, Creature target, int damage)
	{
		if (called.getAI().getLifeTime() > getNpcIntAIParam(called, "Aggressive_Time"))
		{
			final int partyType = getNpcIntAIParam(called, "Party_Type");
			final int partyLoyalty = getNpcIntAIParam(called, "Party_Loyalty");
			
			if ((((partyType == 1 && (partyLoyalty == 0 || partyLoyalty == 1)) || (partyType == 1 && partyLoyalty == 2 && caller == called.getMaster())) || (partyType == 2 && caller != called.getMaster())) && target instanceof Playable)
			{
				final HateList hateList = called.getAI().getHateList();
				if (hateList.size() == 0)
					hateList.addHateInfo(target, (damage * PARTY_ATTACKED_WEIGHT_POINT) + ATTACK_BOOST_VALUE);
				else
					hateList.addHateInfo(target, (damage * PARTY_ATTACKED_WEIGHT_POINT));
			}
		}
		
		super.onPartyAttacked(caller, called, target, damage);
	}
	
	@Override
	public void onSeeCreature(Npc npc, Creature creature)
	{
		if (creature instanceof Playable && getNpcIntAIParam(npc, "IsAggressive") != 0 && npc.getAI().getLifeTime() >= getNpcIntAIParam(npc, "Aggressive_Time"))
		{
			final HateList hateList = npc.getAI().getHateList();
			if (hateList.size() == 0)
				hateList.addHateInfo(creature, (100 * PARTY_ATTACKED_WEIGHT_POINT) + 300);
			else
				hateList.addHateInfo(creature, (100 * PARTY_ATTACKED_WEIGHT_POINT));
		}
		
		super.onSeeCreature(npc, creature);
	}
	
	@Override
	public void onSeeSpell(Npc npc, Player caster, L2Skill skill, Creature[] targets, boolean isPet)
	{
		if (targets.length > 0 && (skill.getAggroPoints() > 0 || skill.getPower(npc) > 0 || skill.isOffensive()))
		{
			final HateList hateList = npc.getAI().getHateList();
			if (npc.getAI().getCurrentIntention().getType() == IntentionType.ATTACK && caster == npc.getAI().getTopDesireTarget())
				hateList.addHateInfo(caster, ATTACKED_WEIGHT_POINT);
			else
				hateList.addHateInfo(caster, SEE_SPELL_WEIGHT_POINT);
		}
		
		super.onSeeSpell(npc, caster, skill, targets, isPet);
	}
	
	// EventHandler DESIRE_MANIPULATION(speller,desire) {
	// myself::MakeAttackEvent(speller,( desire * HATE_SKILL_WEIGHT_POINT ),0);
	// super;
	// }
}
