package com.shnok.javaserver.gameserver.scripting.script.ai.individual.Monster.WarriorBase.Warrior.WarriorCastingEnchant;

import com.shnok.javaserver.commons.random.Rnd;

import com.shnok.javaserver.gameserver.enums.IntentionType;
import com.shnok.javaserver.gameserver.enums.actors.NpcSkillType;
import com.shnok.javaserver.gameserver.model.actor.Creature;
import com.shnok.javaserver.gameserver.model.actor.Npc;
import com.shnok.javaserver.gameserver.model.actor.Playable;
import com.shnok.javaserver.gameserver.model.actor.Player;
import com.shnok.javaserver.gameserver.model.actor.instance.Door;
import com.shnok.javaserver.gameserver.model.actor.instance.SiegeSummon;
import com.shnok.javaserver.gameserver.skills.L2Skill;

public class Agit02DoomArcherAgit extends WarriorCastingEnchantClan
{
	public Agit02DoomArcherAgit()
	{
		super("ai/individual/Monster/WarriorBase/Warrior/WarriorCastingEnchant");
	}
	
	public Agit02DoomArcherAgit(String descr)
	{
		super(descr);
	}
	
	protected final int[] _npcIds =
	{
		35413
	};
	
	@Override
	public void onNoDesire(Npc npc)
	{
		// Do nothing
	}
	
	@Override
	public void onCreated(Npc npc)
	{
		npc._flag = 0;
		
		super.onCreated(npc);
	}
	
	@Override
	public void onAttacked(Npc npc, Creature attacker, int damage, L2Skill skill)
	{
		if (attacker instanceof Playable)
		{
			final L2Skill buff = getNpcSkillByType(npc, NpcSkillType.BUFF);
			if (npc._i_ai1 == 0 && Rnd.get(100) < 33 && npc.getStatus().getHpRatio() > 0.5)
				npc.getAI().addCastDesire(npc, buff, 1000000);
			
			npc._i_ai1 = 1;
		}
		
		if (attacker instanceof Playable)
			npc.getAI().addAttackDesireHold(attacker, (((damage * 1.0) / npc.getStatus().getMaxHp()) / 0.05) * 100);
	}
	
	@Override
	public void onClanAttacked(Npc caller, Npc called, Creature attacker, int damage, L2Skill skill)
	{
		if (called.getAI().getCurrentIntention().getType() != IntentionType.ATTACK && called._i_ai1 == 0 && called._flag == caller._flag)
		{
			final L2Skill buff = getNpcSkillByType(called, NpcSkillType.BUFF);
			if (Rnd.get(100) < 50 && getAbnormalLevel(caller, buff) <= 0 && caller.getStatus().getHpRatio() > 0.5)
				called.getAI().addCastDesire(caller, buff, 1000000);
		}
		called._i_ai1 = 1;
		
		if (called._flag == caller._flag && attacker instanceof Playable)
			called.getAI().addAttackDesireHold(attacker, (((damage * 1.0) / called.getStatus().getMaxHp()) / 0.05) * 100);
	}
	
	@Override
	public void onSeeSpell(Npc npc, Player caster, L2Skill skill, Creature[] targets, boolean isPet)
	{
		if (skill.getAggroPoints() > 0)
			npc.getAI().addAttackDesireHold(caster, (((skill.getAggroPoints() * 1.0) / npc.getStatus().getMaxHp()) / 0.05) * 150);
	}
	
	@Override
	public void onStaticObjectClanAttacked(Door caller, Npc called, Creature attacker, int damage, L2Skill skill)
	{
		if (attacker instanceof SiegeSummon)
		{
			called.getAI().addAttackDesire(attacker.getActingPlayer(), 5000);
			called.getAI().addAttackDesire(attacker, 1000);
		}
		else if (attacker instanceof Playable)
		{
			called.getAI().addAttackDesire(attacker, (((damage * 1.0) / called.getStatus().getMaxHp()) / 0.05 * 50));
		}
	}
	
	@Override
	public void onSeeCreature(Npc npc, Creature creature)
	{
		if (!(creature instanceof Playable))
			return;
		
		npc.getAI().addAttackDesireHold(creature, 200);
	}
}
