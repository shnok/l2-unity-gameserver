package com.shnok.javaserver.gameserver.scripting.script.ai.individual.Monster.WarriorBase.Warrior.WarriorPhysicalSpecial;

import com.shnok.javaserver.commons.random.Rnd;

import com.shnok.javaserver.gameserver.enums.actors.NpcSkillType;
import com.shnok.javaserver.gameserver.model.actor.Creature;
import com.shnok.javaserver.gameserver.model.actor.Npc;
import com.shnok.javaserver.gameserver.model.actor.Playable;
import com.shnok.javaserver.gameserver.model.group.Party;
import com.shnok.javaserver.gameserver.skills.L2Skill;

public class WarriorSaintTransform extends WarriorPhysicalSpecial
{
	public WarriorSaintTransform()
	{
		super("ai/individual/Monster/WarriorBase/Warrior/WarriorPhysicalSpecial");
	}
	
	public WarriorSaintTransform(String descr)
	{
		super(descr);
	}
	
	protected final int[] _npcIds =
	{
		21534,
		21528,
		21522,
		21538
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
		final Party party = attacker.getParty();
		if (party != null && Rnd.get(100) < 33)
		{
			final L2Skill selfRangeDDMagic = getNpcSkillByType(npc, NpcSkillType.SELF_RANGE_DD_MAGIC);
			if (npc.getCast().meetsHpMpConditions(npc, selfRangeDDMagic))
				npc.getAI().addCastDesire(npc, selfRangeDDMagic, 1000000);
			else
				npc._i_ai0 = 1;
		}
		
		if (attacker instanceof Playable)
		{
			final Creature mostHated = npc.getAI().getAggroList().getMostHatedCreature();
			final Creature finalTarget = mostHated != null ? mostHated : attacker;
			final L2Skill dispell = getNpcSkillByType(npc, NpcSkillType.DISPELL);
			
			final double hpRatio = npc.getStatus().getHpRatio();
			if (npc._i_ai0 == 0 && hpRatio > 0.9 && Rnd.get(100) < 90)
			{
				if (npc.getCast().meetsHpMpConditions(finalTarget, dispell))
					npc.getAI().addCastDesire(finalTarget, dispell, 1000000);
				else
					npc.getAI().addAttackDesire(finalTarget, 1000);
				
				npc._i_ai0 = 1;
			}
			else if (npc._i_ai0 < 1 && hpRatio > 0.4 && hpRatio < 0.5 && Rnd.get(100) < 80)
			{
				if (npc.getCast().meetsHpMpConditions(finalTarget, dispell))
					npc.getAI().addCastDesire(finalTarget, dispell, 1000000);
				else
					npc.getAI().addAttackDesire(finalTarget, 1000);
				
				npc._i_ai0 = 2;
			}
		}
		
		super.onAttacked(npc, attacker, damage, skill);
	}
}