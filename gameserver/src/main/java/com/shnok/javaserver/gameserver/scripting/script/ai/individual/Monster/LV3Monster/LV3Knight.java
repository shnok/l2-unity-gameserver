package com.shnok.javaserver.gameserver.scripting.script.ai.individual.Monster.LV3Monster;

import com.shnok.javaserver.commons.random.Rnd;

import com.shnok.javaserver.gameserver.enums.actors.NpcSkillType;
import com.shnok.javaserver.gameserver.model.actor.Creature;
import com.shnok.javaserver.gameserver.model.actor.Npc;
import com.shnok.javaserver.gameserver.model.actor.Playable;
import com.shnok.javaserver.gameserver.model.actor.Player;
import com.shnok.javaserver.gameserver.skills.L2Skill;

public class LV3Knight extends LV3Monster
{
	public LV3Knight()
	{
		super("ai/individual/Monster/LV3Monster");
	}
	
	public LV3Knight(String descr)
	{
		super(descr);
	}
	
	protected final int[] _npcIds =
	{
		27258,
		27271,
		27286,
		27287
	};
	
	@Override
	public void onCreated(Npc npc)
	{
		npc._i_ai0 = 0;
		npc._i_ai1 = 0;
		npc.getAI().addCastDesire(npc, getNpcSkillByType(npc, NpcSkillType.DEFENCE_MODE), 1000000);
		
		super.onCreated(npc);
	}
	
	@Override
	public void onAttacked(Npc npc, Creature attacker, int damage, L2Skill skill)
	{
		if (skill != null && (skill.getId() == 28 || skill.getId() == 18) && npc._i_ai0 == 0)
		{
			npc._i_ai0 = 1;
			npc.getAI().addCastDesire(npc, getNpcSkillByType(npc, NpcSkillType.DEFENCE_MODE), 1000000);
			startQuestTimer("4000", npc, null, (5000 + (1000 * Rnd.get(6))));
		}
		
		if (attacker instanceof Playable)
		{
			if (damage == 0)
				damage = 1;
			
			npc.getAI().addAttackDesire(attacker, ((1.0 * damage) / (npc.getStatus().getLevel() + 7)) * 100);
			
			if (npc.getAI().getTopDesireTarget() == attacker && Rnd.get(100) < 33)
			{
				switch (Rnd.get(3))
				{
					case 0:
						npc.getAI().addCastDesire(attacker, getNpcSkillByType(npc, NpcSkillType.PHYSICAL_SPECIAL1), 1000);
						break;
					
					case 1:
						npc.getAI().addCastDesire(attacker, getNpcSkillByType(npc, NpcSkillType.PHYSICAL_SPECIAL2), 1000);
						break;
					
					case 2:
						npc.getAI().addCastDesire(attacker, getNpcSkillByType(npc, NpcSkillType.PHYSICAL_SPECIAL3), 1000);
						break;
				}
			}
		}
		
		if (npc._i_ai0 == 1 && npc._i_ai1 == 0 && npc.getStatus().getHpRatio() < 0.5 && Rnd.get(100) < 80)
		{
			npc._i_ai0 = 2;
			npc._i_ai1 = 1;
			npc.getAI().addCastDesire(npc, getNpcSkillByType(npc, NpcSkillType.CRAZY_MODE), 1000000);
			startQuestTimer("4001", npc, null, (5000 + (1000 * Rnd.get(6))));
		}
		
		super.onAttacked(npc, attacker, damage, skill);
	}
	
	@Override
	public String onTimer(String name, Npc npc, Player player)
	{
		if (name.equalsIgnoreCase("4000"))
		{
			if (npc._i_ai0 == 1)
			{
				npc._i_ai0 = 0;
				npc.getAI().addCastDesire(npc, getNpcSkillByType(npc, NpcSkillType.DEFENCE_MODE), 1000000);
			}
		}
		else if (name.equalsIgnoreCase("4001"))
		{
			if (npc._i_ai0 == 2)
			{
				npc._i_ai0 = 0;
				npc.getAI().addCastDesire(npc, getNpcSkillByType(npc, NpcSkillType.DEFENCE_MODE), 1000000);
				npc.getAI().addCastDesire(npc, getNpcSkillByType(npc, NpcSkillType.CRAZY_MODE), 1000000);
			}
		}
		return super.onTimer(name, npc, player);
	}
	
	// TODO: Desire Manipulation
	// EventHandler DESIRE_MANIPULATION(speller,desire,skill_name_id) {
	// return;
	// if( Rnd.get(100) < 90 && npc._i_ai0 == 0 ) {
	// npc._i_ai0 = 1;
	// if( myself::Skill_GetConsumeMP(DefenceMode) < myself.sm.mp && myself::Skill_GetConsumeHP(DefenceMode) < myself.sm.hp && myself::Skill_InReuseDelay(DefenceMode) == 0 ) {
	// myself::AddUseSkillDesire(myself.sm,DefenceMode,1,1,1000000);
	// }
	// myself::AddTimerEx(4000,( 5000 + ( 1000 * Rnd.get(6) ) ));
	// }
	// }
}