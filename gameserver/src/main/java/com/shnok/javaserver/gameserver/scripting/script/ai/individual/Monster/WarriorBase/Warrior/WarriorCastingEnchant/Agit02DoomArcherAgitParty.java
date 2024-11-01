package com.shnok.javaserver.gameserver.scripting.script.ai.individual.Monster.WarriorBase.Warrior.WarriorCastingEnchant;

import com.shnok.javaserver.commons.random.Rnd;

import com.shnok.javaserver.gameserver.enums.IntentionType;
import com.shnok.javaserver.gameserver.enums.actors.NpcSkillType;
import com.shnok.javaserver.gameserver.model.actor.Creature;
import com.shnok.javaserver.gameserver.model.actor.Npc;
import com.shnok.javaserver.gameserver.model.actor.Playable;
import com.shnok.javaserver.gameserver.model.actor.Player;
import com.shnok.javaserver.gameserver.skills.L2Skill;

public class Agit02DoomArcherAgitParty extends WarriorCastingEnchantClan
{
	public Agit02DoomArcherAgitParty()
	{
		super("ai/individual/Monster/WarriorBase/Warrior/WarriorCastingEnchant");
	}
	
	public Agit02DoomArcherAgitParty(String descr)
	{
		super(descr);
	}
	
	protected final int[] _npcIds =
	{
		35646
	};
	
	@Override
	public void onNoDesire(Npc npc)
	{
		// Do nothing
	}
	
	@Override
	public void onCreated(Npc npc)
	{
		startQuestTimerAtFixedRate("4001", npc, null, 1000, 60000);
		
		super.onCreated(npc);
	}
	
	@Override
	public String onTimer(String name, Npc npc, Player player)
	{
		if (name.equalsIgnoreCase("4001"))
		{
			if (!npc.isInMyTerritory() && Rnd.get(3) < 1 && npc.hasMaster())
			{
				npc.teleportTo(npc.getMaster().getPosition(), 0);
				npc.removeAllAttackDesire();
			}
			if (Rnd.get(5) < 1)
				npc.getAI().getAggroList().randomizeAttack();
		}
		
		return super.onTimer(name, npc, player);
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
	}
	
	@Override
	public void onPartyAttacked(Npc caller, Npc called, Creature target, int damage)
	{
		if (called.getAI().getCurrentIntention().getType() != IntentionType.ATTACK && called._i_ai1 == 0 && called._flag == caller._flag)
		{
			final L2Skill buff = getNpcSkillByType(called, NpcSkillType.BUFF);
			if (Rnd.get(100) < 50 && getAbnormalLevel(caller, buff) <= 0 && caller.getStatus().getHpRatio() > 0.5)
				called.getAI().addCastDesire(caller, buff, 1000000);
		}
		called._i_ai1 = 1;
		
		if (called._flag == caller._flag && target instanceof Playable)
			called.getAI().addAttackDesireHold(target, (((damage * 1.0) / called.getStatus().getMaxHp()) / 0.05) * 100);
	}
	
	@Override
	public void onSeeSpell(Npc npc, Player caster, L2Skill skill, Creature[] targets, boolean isPet)
	{
		if (skill.getAggroPoints() > 0)
			npc.getAI().addAttackDesireHold(caster, (((skill.getAggroPoints() * 1.0) / npc.getStatus().getMaxHp()) / 0.05) * 150);
	}
	
	@Override
	public void onClanAttacked(Npc caller, Npc called, Creature attacker, int damage, L2Skill skill)
	{
		// Do nothing
	}
}
