package com.shnok.javaserver.gameserver.scripting.script.ai.individual.Monster.LV3Monster;

import com.shnok.javaserver.commons.random.Rnd;

import com.shnok.javaserver.gameserver.enums.actors.NpcSkillType;
import com.shnok.javaserver.gameserver.model.actor.Creature;
import com.shnok.javaserver.gameserver.model.actor.Npc;
import com.shnok.javaserver.gameserver.model.actor.Playable;
import com.shnok.javaserver.gameserver.model.actor.Player;
import com.shnok.javaserver.gameserver.skills.L2Skill;

public class LV3Orc extends LV3Monster
{
	public LV3Orc()
	{
		super("ai/individual/Monster/LV3Monster");
	}
	
	public LV3Orc(String descr)
	{
		super(descr);
	}
	
	protected final int[] _npcIds =
	{
		27294,
		27295
	};
	
	@Override
	public void onCreated(Npc npc)
	{
		npc._i_ai0 = 0;
		npc.setEnchantEffect(10);
		npc.getAI().addCastDesire(npc, getNpcSkillByType(npc, NpcSkillType.BIG_BODY_SKILL), 1000000);
		
		startQuestTimerAtFixedRate("4000", npc, null, 1000, 1000);
		
		super.onCreated(npc);
	}
	
	@Override
	public String onTimer(String name, Npc npc, Player player)
	{
		if (name.equalsIgnoreCase("4000"))
		{
			if (!npc.isStunned())
			{
				if (npc._i_ai0 == 1)
				{
					npc.removeAllDesire();
					npc.getAI().addCastDesire(npc, getNpcSkillByType(npc, NpcSkillType.DEBUFF1), 1000000);
					npc._i_ai0++;
					npc.getAI().addAttackDesire(npc._c_ai0, 200);
				}
				else if (npc._i_ai0 == 3)
				{
					npc.removeAllDesire();
					npc.getAI().addCastDesire(npc, getNpcSkillByType(npc, NpcSkillType.DEBUFF2), 1000000);
					npc.getAI().addCastDesire(npc, getNpcSkillByType(npc, NpcSkillType.NORMAL_BODY_SKILL), 1000000);
					npc._i_ai0++;
					npc.getAI().addAttackDesire(npc._c_ai0, 200);
				}
				else if (npc._i_ai0 == 5)
				{
					npc.removeAllDesire();
					npc.getAI().addCastDesire(npc, getNpcSkillByType(npc, NpcSkillType.DEBUFF3), 1000000);
					npc._i_ai0++;
					npc.getAI().addAttackDesire(npc._c_ai0, 1, 200);
				}
			}
		}
		
		return super.onTimer(name, npc, player);
	}
	
	@Override
	public void onAttacked(Npc npc, Creature attacker, int damage, L2Skill skill)
	{
		if (npc.isStunned() && skill != null && skill.getId() == 1245)
		{
			switch (npc._i_ai0)
			{
				case 0:
					npc._i_ai0++;
					npc.setEnchantEffect(0);
					break;
				
				case 2, 4:
					npc._i_ai0++;
					break;
			}
		}
		
		if (attacker instanceof Playable)
		{
			if (damage == 0)
				damage = 1;
			
			npc.getAI().addAttackDesire(attacker, (((1.0 * damage) / (npc.getStatus().getLevel() + 7)) * 100));
			
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
		
		super.onAttacked(npc, attacker, damage, skill);
	}
}