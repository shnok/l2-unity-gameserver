package com.shnok.javaserver.gameserver.scripting.script.ai.individual.Monster.WarriorBase.Warrior.WarriorPhysicalSpecial;

import com.shnok.javaserver.commons.random.Rnd;

import com.shnok.javaserver.gameserver.enums.actors.NpcSkillType;
import com.shnok.javaserver.gameserver.model.actor.Creature;
import com.shnok.javaserver.gameserver.model.actor.Npc;
import com.shnok.javaserver.gameserver.model.actor.Playable;
import com.shnok.javaserver.gameserver.model.actor.Player;
import com.shnok.javaserver.gameserver.network.NpcStringId;
import com.shnok.javaserver.gameserver.skills.L2Skill;

public class WarriorPatrolWeaponEquipped extends WarriorPhysicalSpecial
{
	public WarriorPatrolWeaponEquipped()
	{
		super("ai/individual/Monster/WarriorBase/Warrior/WarriorPhysicalSpecial");
	}
	
	public WarriorPatrolWeaponEquipped(String descr)
	{
		super(descr);
	}
	
	protected final int[] _npcIds =
	{
		22124,
		22126,
		22129
	};
	
	@Override
	public void onCreated(Npc npc)
	{
		npc.getAI().addWanderDesire(5, 5);
		npc._c_ai0 = npc;
		npc._i_ai3 = 0;
		
		super.onCreated(npc);
	}
	
	@Override
	public void onNoDesire(Npc npc)
	{
		npc._i_ai3 = 0;
		
		super.onNoDesire(npc);
	}
	
	@Override
	public void onSeeCreature(Npc npc, Creature creature)
	{
		if (creature instanceof Player)
		{
			final Player player = creature.getActingPlayer();
			if (player.getActiveWeaponInstance() != null)
			{
				npc.getAI().addCastDesire(creature, getNpcSkillByType(npc, NpcSkillType.DEBUFF), 1000000);
				npc._c_ai0 = creature;
			}
		}
		
		super.onSeeCreature(npc, creature);
	}
	
	@Override
	public void onAttacked(Npc npc, Creature attacker, int damage, L2Skill skill)
	{
		if (attacker instanceof Playable)
		{
			final Creature topDesireTarget = npc.getAI().getTopDesireTarget();
			if (topDesireTarget != null)
			{
				if (topDesireTarget == attacker)
				{
					if (Rnd.get(100) < 33)
						npc.getAI().addCastDesire(attacker, getNpcSkillByType(npc, NpcSkillType.SELF_RANGE_PHYSICAL_SPECIAL), 1000000);
					
					if (Rnd.get(100) < 33)
						npc.getAI().addCastDesire(attacker, getNpcSkillByType(npc, NpcSkillType.DEBUFF2), 1000000);
				}
				if (damage == 0)
					damage = 1;
				
				npc.getAI().addAttackDesire(attacker, (((1.0 * damage) / (npc.getStatus().getLevel() + 7)) * 10000));
			}
		}
		
		super.onAttacked(npc, attacker, damage, skill);
	}
	
	@Override
	public void onUseSkillFinished(Npc npc, Creature creature, L2Skill skill, boolean success)
	{
		if (skill == getNpcSkillByType(npc, NpcSkillType.DEBUFF))
		{
			if (npc._i_ai3 == 0)
			{
				npc.broadcastNpcSay(NpcStringId.ID_1121006);
				npc._i_ai3 = 1;
			}
			npc.getAI().addAttackDesire(npc._c_ai0, 10000);
		}
	}
}
