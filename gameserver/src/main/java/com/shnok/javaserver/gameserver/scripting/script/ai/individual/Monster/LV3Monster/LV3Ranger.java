package com.shnok.javaserver.gameserver.scripting.script.ai.individual.Monster.LV3Monster;

import net.sf.l2j.commons.random.Rnd;

import net.sf.l2j.gameserver.enums.actors.NpcSkillType;
import net.sf.l2j.gameserver.model.actor.Creature;
import net.sf.l2j.gameserver.model.actor.Npc;
import net.sf.l2j.gameserver.model.actor.Playable;
import net.sf.l2j.gameserver.model.actor.Player;
import net.sf.l2j.gameserver.skills.L2Skill;

public class LV3Ranger extends LV3Monster
{
	public LV3Ranger()
	{
		super("ai/individual/Monster/LV3Monster");
	}
	
	public LV3Ranger(String descr)
	{
		super(descr);
	}
	
	protected final int[] _npcIds =
	{
		27289,
		27292,
		27293,
		27296,
		27297,
		27298
	};
	
	@Override
	public void onCreated(Npc npc)
	{
		npc.getAI().addCastDesire(npc, getNpcSkillByType(npc, NpcSkillType.ARROW_DEFENCE_MODE), 1000000);
		
		npc._i_ai0 = 1;
		npc._i_ai1 = 0;
		
		super.onCreated(npc);
	}
	
	@Override
	public void onAttacked(Npc npc, Creature attacker, int damage, L2Skill skill)
	{
		if (attacker instanceof Playable)
		{
			if (damage == 0)
				damage = 1;
			
			npc.getAI().addAttackDesire(attacker, ((1.000000 * damage) / (npc.getStatus().getLevel() + 7)) * 100);
			
			if (npc.getAI().getTopDesireTarget() == attacker && Rnd.get(100) < 33)
			{
				switch (Rnd.get(3))
				{
					case 0:
						npc.getAI().addCastDesire(attacker, getNpcSkillByType(npc, NpcSkillType.PHYSICAL_SPECIAL1), 1000000);
						break;
					
					case 1:
						npc.getAI().addCastDesire(attacker, getNpcSkillByType(npc, NpcSkillType.PHYSICAL_SPECIAL2), 1000000);
						break;
					
					case 2:
						npc.getAI().addCastDesire(attacker, getNpcSkillByType(npc, NpcSkillType.PHYSICAL_SPECIAL3), 1000000);
						break;
				}
			}
		}
		
		if (npc._i_ai0 == 1 && skill != null)
		{
			if (skill == getNpcSkillByType(npc, NpcSkillType.CHECK_SKILL1))
			{
				npc._i_ai0 = 0;
				npc.getAI().addCastDesire(npc, getNpcSkillByType(npc, NpcSkillType.ARROW_DEFENCE_MODE), 1000000);
				npc.getAI().addCastDesire(npc, getNpcSkillByType(npc, NpcSkillType.ARROW_NORMAL_MODE), 1000000);
				
				npc._i_ai1 = 5;
			}
			
			if (skill == getNpcSkillByType(npc, NpcSkillType.CHECK_SKILL2))
			{
				npc._i_ai0 = 0;
				npc.getAI().addCastDesire(npc, getNpcSkillByType(npc, NpcSkillType.ARROW_DEFENCE_MODE), 1000000);
				npc.getAI().addCastDesire(npc, getNpcSkillByType(npc, NpcSkillType.ARROW_NORMAL_MODE), 1000000);
				
				npc._i_ai1 = 3;
			}
		}
		
		super.onAttacked(npc, attacker, damage, skill);
	}
	
	@Override
	public void onUseSkillFinished(Npc npc, Creature creature, L2Skill skill, boolean success)
	{
		if (skill == getNpcSkillByType(npc, NpcSkillType.ARROW_DEFENCE_MODE) && npc._i_ai0 == 0)
		{
			if (npc._i_ai1 != 0)
				startQuestTimer("4004", npc, null, npc._i_ai1 * 1000L);
			else
				startQuestTimer("4004", npc, null, 3000);
		}
		
		super.onUseSkillFinished(npc, creature, skill, success);
	}
	
	@Override
	public String onTimer(String name, Npc npc, Player player)
	{
		if ((name.equalsIgnoreCase("4000") || name.equalsIgnoreCase("4001") || name.equalsIgnoreCase("4002") || name.equalsIgnoreCase("4003") || name.equalsIgnoreCase("4004")) && npc._i_ai0 == 0)
		{
			npc._i_ai0 = 1;
			npc.getAI().addCastDesire(npc, getNpcSkillByType(npc, NpcSkillType.ARROW_DEFENCE_MODE), 1000000);
			npc.getAI().addCastDesire(npc, getNpcSkillByType(npc, NpcSkillType.ARROW_NORMAL_MODE), 1000000);
		}
		
		return super.onTimer(name, npc, null);
	}
}
