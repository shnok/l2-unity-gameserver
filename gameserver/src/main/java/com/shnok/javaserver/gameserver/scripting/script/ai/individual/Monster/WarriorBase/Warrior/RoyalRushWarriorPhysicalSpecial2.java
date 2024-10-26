package com.shnok.javaserver.gameserver.scripting.script.ai.individual.Monster.WarriorBase.Warrior;

import com.shnok.javaserver.commons.random.Rnd;

import com.shnok.javaserver.gameserver.enums.IntentionType;
import com.shnok.javaserver.gameserver.enums.actors.NpcSkillType;
import com.shnok.javaserver.gameserver.model.World;
import com.shnok.javaserver.gameserver.model.actor.Creature;
import com.shnok.javaserver.gameserver.model.actor.Npc;
import com.shnok.javaserver.gameserver.model.actor.Playable;
import com.shnok.javaserver.gameserver.skills.L2Skill;

public class RoyalRushWarriorPhysicalSpecial2 extends Warrior
{
	public RoyalRushWarriorPhysicalSpecial2()
	{
		super("ai/individual/Monster/WarriorBase/Warrior");
	}
	
	public RoyalRushWarriorPhysicalSpecial2(String descr)
	{
		super(descr);
	}
	
	protected final int[] _npcIds =
	{
		18135,
		18146,
		18168,
		18189,
		18224,
		21419
	};
	
	@Override
	public void onCreated(Npc npc)
	{
		npc._i_ai0 = 0;
	}
	
	@Override
	public void onAttacked(Npc npc, Creature attacker, int damage, L2Skill skill)
	{
		final int weaponID = getNpcIntAIParam(npc, "WeaponID");
		if (npc.getStatus().getHpRatio() < 0.5 && weaponID != 0)
		{
			npc.equipItem(weaponID, 0);
			npc.setEnchantEffect(15);
			npc._i_ai0 = 1;
		}
		if (attacker instanceof Playable)
		{
			final Creature topDesireTarget = npc.getAI().getTopDesireTarget();
			if (topDesireTarget != null)
			{
				if (Rnd.get(100) < 33 && topDesireTarget == attacker && npc.distance2D(attacker) < 100 && npc._i_ai0 == 0)
					npc.getAI().addCastDesire(attacker, getNpcSkillByType(npc, NpcSkillType.PHYSICAL_SPECIAL2), 1000000);
				
				if (Rnd.get(100) < 33 && npc._i_ai0 == 1)
					npc.rechargeShots(true, false);
				
				if (Rnd.get(100) < 33 && topDesireTarget == attacker && npc.distance2D(attacker) > 100)
					npc.getAI().addCastDesire(attacker, getNpcSkillByType(npc, NpcSkillType.PHYSICAL_SPECIAL2), 1000000);
			}
		}
		
		super.onAttacked(npc, attacker, damage, skill);
	}
	
	@Override
	public void onClanAttacked(Npc caller, Npc called, Creature attacker, int damage, L2Skill skill)
	{
		if ((called.getAI().getLifeTime() > 7 && (attacker instanceof Playable)) && called.getAI().getCurrentIntention().getType() != IntentionType.ATTACK)
		{
			if (Rnd.get(100) < 33 && called.distance2D(attacker) > 100)
				called.getAI().addCastDesire(attacker, getNpcSkillByType(called, NpcSkillType.PHYSICAL_SPECIAL1), 1000000);
			
			if (Rnd.get(100) < 33 && called.distance2D(attacker) < 100)
			{
				if (called._i_ai0 == 0)
					called.getAI().addCastDesire(attacker, getNpcSkillByType(called, NpcSkillType.PHYSICAL_SPECIAL2), 1000000);
				else
					called.rechargeShots(true, false);
			}
		}
		
		super.onClanAttacked(caller, called, attacker, damage, skill);
	}
	
	@Override
	public void onScriptEvent(Npc npc, int eventId, int arg1, int arg2)
	{
		if (eventId == 1234)
		{
			final Creature c0 = (Creature) World.getInstance().getObject(arg1);
			if (c0 != null)
			{
				if (Rnd.get(100) < 80)
				{
					npc.getAI().addAttackDesire(c0, 300);
				}
				else
				{
					npc.removeAllAttackDesire();
					npc.getAI().addAttackDesire(c0, 1000);
				}
			}
		}
	}
}
