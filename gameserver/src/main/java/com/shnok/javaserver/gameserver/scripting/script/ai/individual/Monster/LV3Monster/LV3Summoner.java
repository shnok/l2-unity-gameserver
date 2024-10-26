package com.shnok.javaserver.gameserver.scripting.script.ai.individual.Monster.LV3Monster;

import com.shnok.javaserver.commons.random.Rnd;

import com.shnok.javaserver.gameserver.enums.actors.NpcSkillType;
import com.shnok.javaserver.gameserver.model.actor.Creature;
import com.shnok.javaserver.gameserver.model.actor.Npc;
import com.shnok.javaserver.gameserver.model.actor.Playable;
import com.shnok.javaserver.gameserver.model.actor.Player;
import com.shnok.javaserver.gameserver.skills.L2Skill;

public class LV3Summoner extends LV3Monster
{
	public LV3Summoner()
	{
		super("ai/individual/Monster/LV3Monster");
	}
	
	public LV3Summoner(String descr)
	{
		super(descr);
	}
	
	protected final int[] _npcIds =
	{
		27313,
		27314,
		27315
	};
	
	@Override
	public void onCreated(Npc npc)
	{
		startQuestTimerAtFixedRate("4000", npc, null, 1000, 1000);
		startQuestTimerAtFixedRate("4001", npc, null, 3000, 3000);
		npc._i_ai0 = 0;
		
		super.onCreated(npc);
	}
	
	@Override
	public void onAttacked(Npc npc, Creature attacker, int damage, L2Skill skill)
	{
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
		
		super.onAttacked(npc, attacker, damage, skill);
	}
	
	@Override
	public String onTimer(String name, Npc npc, Player player)
	{
		if (name.equalsIgnoreCase("4000"))
		{
			if (getAbnormalLevel(npc, getNpcSkillByType(npc, NpcSkillType.BUFF1)) > 0)
				npc._i_ai0 = 1;
			else
				npc._i_ai0 = 0;
		}
		else if (name.equalsIgnoreCase("4001"))
		{
			if (npc._c_ai0 != null && npc._i_ai0 == 0 && Rnd.get(100) < 50)
			{
				npc.removeAllDesire();
				npc.getAI().addCastDesire(npc, getNpcSkillByType(npc, NpcSkillType.BUFF1), 1000000);
				npc.getAI().addAttackDesire(npc._c_ai0, 200);
				
				npc._i_ai0 = 1;
			}
		}
		
		return super.onTimer(name, npc, player);
	}
}