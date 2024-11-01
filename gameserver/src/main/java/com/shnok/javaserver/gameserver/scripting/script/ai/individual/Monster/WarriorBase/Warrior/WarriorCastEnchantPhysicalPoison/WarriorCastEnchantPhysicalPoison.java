package com.shnok.javaserver.gameserver.scripting.script.ai.individual.Monster.WarriorBase.Warrior.WarriorCastEnchantPhysicalPoison;

import com.shnok.javaserver.commons.random.Rnd;

import com.shnok.javaserver.gameserver.enums.actors.NpcSkillType;
import com.shnok.javaserver.gameserver.model.actor.Creature;
import com.shnok.javaserver.gameserver.model.actor.Npc;
import com.shnok.javaserver.gameserver.model.actor.Player;
import com.shnok.javaserver.gameserver.scripting.script.ai.individual.Monster.WarriorBase.Warrior.Warrior;
import com.shnok.javaserver.gameserver.skills.L2Skill;

public class WarriorCastEnchantPhysicalPoison extends Warrior
{
	public WarriorCastEnchantPhysicalPoison()
	{
		super("ai/individual/Monster/WarriorBase/Warrior/WarriorCastEnchantPhysicalPoison");
	}
	
	public WarriorCastEnchantPhysicalPoison(String descr)
	{
		super(descr);
	}
	
	@Override
	public void onCreated(Npc npc)
	{
		final L2Skill buff1 = getNpcSkillByType(npc, NpcSkillType.BUFF1);
		npc.getAI().addCastDesire(npc, buff1, 1000000);
		
		final L2Skill buff2 = getNpcSkillByType(npc, NpcSkillType.BUFF2);
		npc.getAI().addCastDesire(npc, buff2, 1000000);
		
		super.onCreated(npc);
	}
	
	@Override
	public void onAttacked(Npc npc, Creature attacker, int damage, L2Skill skill)
	{
		final L2Skill buff1 = getNpcSkillByType(npc, NpcSkillType.BUFF1);
		if (getAbnormalLevel(npc, buff1) <= 0)
			npc.getAI().addCastDesire(npc, buff1, 1000000);
		
		final L2Skill buff2 = getNpcSkillByType(npc, NpcSkillType.BUFF2);
		if (getAbnormalLevel(npc, buff2) <= 0)
			npc.getAI().addCastDesire(npc, buff2, 1000000);
		
		if (Rnd.get(100) < 33)
		{
			final L2Skill physicalSpecial = getNpcSkillByType(npc, NpcSkillType.PHYSICAL_SPECIAL);
			npc.getAI().addCastDesire(attacker, physicalSpecial, 1000000);
		}
		
		maybeCastDebuffs(attacker, npc);
		
		super.onAttacked(npc, attacker, damage, skill);
	}
	
	@Override
	public void onClanAttacked(Npc caller, Npc called, Creature attacker, int damage, L2Skill skill)
	{
		maybeCastDebuffs(attacker, called);
		
		super.onClanAttacked(caller, called, attacker, damage, skill);
	}
	
	@Override
	public void onSeeSpell(Npc npc, Player caster, L2Skill skill, Creature[] targets, boolean isPet)
	{
		maybeCastDebuffs(caster, npc);
		
		super.onSeeSpell(npc, caster, skill, targets, isPet);
	}
	
	private static void maybeCastDebuffs(Creature attacker, Npc npc)
	{
		if (Rnd.get(100) < 10)
		{
			final L2Skill debuff1 = getNpcSkillByType(npc, NpcSkillType.DEBUFF1);
			
			final int i1 = getAbnormalLevel(attacker, debuff1);
			if (i1 <= 0)
				npc.getAI().addCastDesire(attacker, debuff1, 1000000);
			else if (i1 < 10)
				npc.getAI().addCastDesire(attacker, debuff1.getId(), debuff1.getLevel() + i1, 1000000);
		}
		
		if (Rnd.get(100) < 10)
		{
			final L2Skill debuff2 = getNpcSkillByType(npc, NpcSkillType.DEBUFF2);
			
			final int i1 = getAbnormalLevel(attacker, debuff2);
			if (i1 <= -1)
				npc.getAI().addCastDesire(attacker, debuff2, 1000000);
			else if (i1 < 10)
				npc.getAI().addCastDesire(attacker, debuff2.getId(), debuff2.getLevel() + i1, 1000000);
		}
	}
}