package com.shnok.javaserver.gameserver.scripting.script.ai.individual.Monster.WarriorBase.Warrior.WarriorCast3SkillsMagical;

import com.shnok.javaserver.gameserver.enums.actors.NpcSkillType;
import com.shnok.javaserver.gameserver.model.actor.Creature;
import com.shnok.javaserver.gameserver.model.actor.Npc;
import com.shnok.javaserver.gameserver.model.actor.Player;
import com.shnok.javaserver.gameserver.skills.L2Skill;

public class WarriorCast3SkillsCurse extends WarriorCast3SkillsMagical
{
	public WarriorCast3SkillsCurse()
	{
		super("ai/individual/Monster/WarriorBase/Warrior/WarriorCast3SkillsMagical");
	}
	
	public WarriorCast3SkillsCurse(String descr)
	{
		super(descr);
	}
	
	protected final int[] _npcIds =
	{
		21301
	};
	
	@Override
	public void onCreated(Npc npc)
	{
		npc._i_ai3 = 0;
		npc._i_ai4 = 0;
		npc._c_ai2 = npc;
	}
	
	@Override
	public void onAttacked(Npc npc, Creature attacker, int damage, L2Skill skill)
	{
		if (npc._i_ai4 == 0)
		{
			if (npc._i_ai3 == 0)
			{
				npc._i_ai3 = 1;
				
				startQuestTimer("5001", npc, null, 5000);
			}
			else if (npc._i_ai3 == 1)
			{
				npc._i_ai4 = 1;
			}
		}
		super.onAttacked(npc, attacker, damage, skill);
	}
	
	@Override
	public void onMyDying(Npc npc, Creature killer)
	{
		if (npc._c_ai2 != null && npc._c_ai2 != npc)
		{
			final L2Skill deBuffCancel = getNpcSkillByType(npc, NpcSkillType.DEBUFF_CANCEL);
			deBuffCancel.getEffects(npc._c_ai2, npc._c_ai2);
		}
	}
	
	@Override
	public String onTimer(String name, Npc npc, Player player)
	{
		if (name.equalsIgnoreCase("5001"))
		{
			if (npc._i_ai3 == 1)
			{
				if (npc._i_ai4 == 1)
				{
					startQuestTimer("5001", npc, null, 5000);
					
					npc._i_ai4 = 0;
				}
				else if (npc._i_ai4 == 0)
				{
					final Creature mostHated = npc.getAI().getAggroList().getMostHatedCreature();
					if (mostHated != null)
					{
						final L2Skill debuff = getNpcSkillByType(npc, NpcSkillType.DEBUFF);
						npc.getAI().addCastDesire(mostHated, debuff, 1000000);
						
						npc._c_ai2 = mostHated;
					}
				}
			}
		}
		return super.onTimer(name, npc, player);
	}
}