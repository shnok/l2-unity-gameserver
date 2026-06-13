package com.shnok.javaserver.gameserver.scripting.script.ai.individual.Monster.WarriorBase.Warrior.WarriorCast3SkillsMagical3;

import com.shnok.javaserver.commons.random.Rnd;

import com.shnok.javaserver.gameserver.enums.IntentionType;
import com.shnok.javaserver.gameserver.enums.actors.NpcSkillType;
import com.shnok.javaserver.gameserver.enums.items.WeaponType;
import com.shnok.javaserver.gameserver.model.actor.Creature;
import com.shnok.javaserver.gameserver.model.actor.Npc;
import com.shnok.javaserver.gameserver.model.actor.Playable;
import com.shnok.javaserver.gameserver.model.actor.Player;
import com.shnok.javaserver.gameserver.scripting.script.ai.individual.Monster.WarriorBase.Warrior.Warrior;
import com.shnok.javaserver.gameserver.skills.L2Skill;

public class WarriorCast3SkillsMagical3 extends Warrior
{
	public WarriorCast3SkillsMagical3()
	{
		super("ai/individual/Monster/WarriorBase/Warrior/WarriorCast3SkillsMagical3");
	}
	
	public WarriorCast3SkillsMagical3(String descr)
	{
		super(descr);
	}
	
	protected final int[] _npcIds =
	{
		20611,
		21025,
		21115
	};
	
	@Override
	public void onCreated(Npc npc)
	{
		npc._i_ai0 = 0;
		
		super.onCreated(npc);
	}
	
	@Override
	public void onAttacked(Npc npc, Creature attacker, int damage, L2Skill skill)
	{
		if (attacker instanceof Playable)
		{
			final Creature topDesireTarget = npc.getAI().getTopDesireTarget();
			if (topDesireTarget != null && topDesireTarget == attacker)
			{
				final L2Skill debuff = getNpcSkillByType(npc, NpcSkillType.DEBUFF);
				if (Rnd.get(100) < 33 && getAbnormalLevel(attacker, debuff) <= 0)
					npc.getAI().addCastDesire(attacker, debuff, 1000000);
			}
			
			final double dist = npc.distance2D(attacker);
			
			if (dist > 100)
			{
				if (topDesireTarget != null && topDesireTarget == attacker && Rnd.get(100) < 33)
				{
					final L2Skill DDMagic = getNpcSkillByType(npc, NpcSkillType.DD_MAGIC);
					npc.getAI().addCastDesire(attacker, DDMagic, 1000000);
				}
			}
			
			startQuestTimer("2001", npc, null, 12000);
			
			if (npc._i_ai0 == 0)
			{
				final int i6 = Rnd.get(100);
				
				if (dist > 300)
				{
					if (i6 < 50)
					{
						final L2Skill holdMagic = getNpcSkillByType(npc, NpcSkillType.HOLD_MAGIC);
						npc.getAI().addCastDesire(attacker, holdMagic, 1000000);
						
						npc._i_ai0 = 1;
					}
				}
				else if (dist > 100)
				{
					if (topDesireTarget != null && ((topDesireTarget == attacker && i6 < 50) || i6 < 10))
					{
						final L2Skill holdMagic = getNpcSkillByType(npc, NpcSkillType.HOLD_MAGIC);
						npc.getAI().addCastDesire(attacker, holdMagic, 1000000);
						
						npc._i_ai0 = 1;
					}
				}
				
				if (attacker.getAttackType() == WeaponType.BOW && Rnd.get(100) < 50)
				{
					final L2Skill holdMagic = getNpcSkillByType(npc, NpcSkillType.HOLD_MAGIC);
					npc.getAI().addCastDesire(attacker, holdMagic, 1000000);
					
					npc._i_ai0 = 1;
				}
			}
		}
		super.onAttacked(npc, attacker, damage, skill);
	}
	
	@Override
	public void onClanAttacked(Npc caller, Npc called, Creature attacker, int damage, L2Skill skill)
	{
		if (attacker instanceof Playable && called.getAI().getLifeTime() > 7 && called.getAI().getCurrentIntention().getType() != IntentionType.ATTACK)
		{
			final L2Skill debuff = getNpcSkillByType(called, NpcSkillType.DEBUFF);
			if (Rnd.get(100) < 33 && getAbnormalLevel(called, debuff) <= 0)
				called.getAI().addCastDesire(attacker, debuff, 1000000);
			
			if (called.distance2D(attacker) > 100 && Rnd.get(100) < 33)
			{
				final L2Skill DDMagic = getNpcSkillByType(called, NpcSkillType.DD_MAGIC);
				called.getAI().addCastDesire(attacker, DDMagic, 1000000);
			}
		}
		super.onClanAttacked(caller, called, attacker, damage, skill);
	}
	
	@Override
	public String onTimer(String name, Npc npc, Player player)
	{
		if (name.equalsIgnoreCase("2001"))
		{
			final Creature c0 = npc.getLastAttacker();
			if (c0 != null && npc._i_ai0 == 0 && Rnd.get(100) < 80)
			{
				final L2Skill holdMagic = getNpcSkillByType(npc, NpcSkillType.HOLD_MAGIC);
				npc.getAI().addCastDesire(c0, holdMagic, 1000000);
				
				npc._i_ai0 = 1;
			}
		}
		return super.onTimer(name, npc, player);
	}
}