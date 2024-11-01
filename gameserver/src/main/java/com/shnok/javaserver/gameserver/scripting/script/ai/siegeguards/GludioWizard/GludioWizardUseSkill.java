package com.shnok.javaserver.gameserver.scripting.script.ai.siegeguards.GludioWizard;

import com.shnok.javaserver.commons.random.Rnd;

import com.shnok.javaserver.gameserver.enums.ZoneId;
import com.shnok.javaserver.gameserver.enums.actors.NpcSkillType;
import com.shnok.javaserver.gameserver.model.actor.Creature;
import com.shnok.javaserver.gameserver.model.actor.Npc;
import com.shnok.javaserver.gameserver.model.actor.Playable;
import com.shnok.javaserver.gameserver.network.NpcStringId;
import com.shnok.javaserver.gameserver.skills.L2Skill;

public class GludioWizardUseSkill extends GludioWizard
{
	public GludioWizardUseSkill()
	{
		super("ai/siegeguards/GludioWizard");
	}
	
	public GludioWizardUseSkill(String descr)
	{
		super(descr);
	}
	
	protected final int[] _npcIds =
	{
		35061
	};
	
	@Override
	public void onSeeCreature(Npc npc, Creature creature)
	{
		if (getPledgeCastleState(npc, creature) != 2 && creature instanceof Playable)
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
			npc.getAI().addCastDesire(creature, getNpcSkillByType(npc, NpcSkillType.DD_MAGIC), 1000000);
		}
		
		if (npc.isInsideZone(ZoneId.PEACE))
		{
			npc.teleportTo(npc.getSpawnLocation(), 0);
			npc.removeAllAttackDesire();
		}
	}
	
	@Override
	public void onClanAttacked(Npc caller, Npc called, Creature attacker, int damage, L2Skill skill)
	{
		if (getPledgeCastleState(called, attacker) != 2 && attacker instanceof Playable)
		{
			if (Rnd.get(100) < 50)
				called.getAI().addCastDesire(attacker, getNpcSkillByType(called, NpcSkillType.DD_MAGIC), 1000000);
			
			if (Rnd.get(100) < 25)
				called.getAI().addCastDesire(attacker, getNpcSkillByType(called, NpcSkillType.HOLD_MAGIC), 1000000);
		}
	}
	
	@Override
	public void onAttacked(Npc npc, Creature attacker, int damage, L2Skill skill)
	{
		if (getPledgeCastleState(npc, attacker) != 2 && attacker instanceof Playable)
		{
			if (Rnd.get(100) < 50)
				npc.getAI().addCastDesire(attacker, getNpcSkillByType(npc, NpcSkillType.DD_MAGIC), 1000000);
			
			if (Rnd.get(100) < 25)
				npc.getAI().addCastDesire(attacker, getNpcSkillByType(npc, NpcSkillType.HOLD_MAGIC), 1000000);
		}
	}
}
