package com.shnok.javaserver.gameserver.scripting.script.ai.siegeguards.GludioHold;

import com.shnok.javaserver.commons.random.Rnd;

import com.shnok.javaserver.gameserver.enums.actors.NpcSkillType;
import com.shnok.javaserver.gameserver.model.actor.Creature;
import com.shnok.javaserver.gameserver.model.actor.Npc;
import com.shnok.javaserver.gameserver.network.NpcStringId;
import com.shnok.javaserver.gameserver.skills.L2Skill;

public class GludioSwordGuardUseSkill extends GludioSwordGuard
{
	public GludioSwordGuardUseSkill()
	{
		super("ai/siegeguards/GludioHold");
	}
	
	public GludioSwordGuardUseSkill(String descr)
	{
		super(descr);
	}
	
	protected final int[] _npcIds =
	{
		35060
	};
	
	@Override
	public void onAttacked(Npc npc, Creature attacker, int damage, L2Skill skill)
	{
		final Creature topDesireTarget = npc.getAI().getTopDesireTarget();
		if (topDesireTarget != null && getPledgeCastleState(npc, attacker) != 2)
		{
			if (attacker == topDesireTarget && Rnd.get(25) < 1)
				npc.getAI().addCastDesire(attacker, getNpcSkillByType(npc, NpcSkillType.PHYSICAL_SPECIAL), 1000000);
			
			if (npc.distance2D(attacker) < 150 && Rnd.get((50 * 15)) < 1)
				npc.getAI().addCastDesire(npc, getNpcSkillByType(npc, NpcSkillType.DISPELL), 1000000);
		}
		
		super.onAttacked(npc, attacker, damage, skill);
	}
	
	@Override
	public void onClanAttacked(Npc caller, Npc called, Creature attacker, int damage, L2Skill skill)
	{
		final Creature topDesireTarget = called.getAI().getTopDesireTarget();
		if (topDesireTarget != null && getPledgeCastleState(called, attacker) != 2)
		{
			if (attacker == topDesireTarget && Rnd.get(25) < 1)
				called.getAI().addCastDesire(attacker, getNpcSkillByType(called, NpcSkillType.PHYSICAL_SPECIAL), 1000000);
			
			if (called.distance2D(attacker) < 150 && Rnd.get((50 * 15)) < 1)
				called.getAI().addCastDesire(called, getNpcSkillByType(called, NpcSkillType.DISPELL), 1000000);
		}
		
		super.onClanAttacked(caller, called, attacker, damage, skill);
	}
	
	@Override
	public void onSeeCreature(Npc npc, Creature creature)
	{
		if (getPledgeCastleState(npc, creature) != 2)
		{
			
			final int i0 = Rnd.get(10000);
			if (i0 < 1)
			{
				npc.broadcastNpcShout(NpcStringId.ID_1800012);
			}
			else if (i0 < 2)
			{
				npc.broadcastNpcShout(NpcStringId.ID_1800013);
			}
			
			npc.getAI().addAttackDesire(creature, 200);
		}
	}
}
