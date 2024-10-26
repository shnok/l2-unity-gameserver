package com.shnok.javaserver.gameserver.scripting.script.ai.individual.Monster.LV3Monster;

import net.sf.l2j.commons.random.Rnd;

import net.sf.l2j.gameserver.enums.actors.NpcSkillType;
import net.sf.l2j.gameserver.model.World;
import net.sf.l2j.gameserver.model.actor.Creature;
import net.sf.l2j.gameserver.model.actor.Npc;
import net.sf.l2j.gameserver.model.actor.Playable;
import net.sf.l2j.gameserver.model.actor.Player;
import net.sf.l2j.gameserver.skills.L2Skill;

public class LV3Dagger extends LV3Monster
{
	public LV3Dagger()
	{
		super("ai/individual/Monster/LV3Monster");
	}
	
	public LV3Dagger(String descr)
	{
		super(descr);
	}
	
	protected final int[] _npcIds =
	{
		27299,
		27300,
		27301
	};
	
	@Override
	public void onCreated(Npc npc)
	{
		startQuestTimerAtFixedRate("4000", npc, null, 15000, 15000);
		
		if (npc._param1 != 0)
		{
			npc._c_ai0 = (Creature) World.getInstance().getObject(npc._param1);
			if (npc._c_ai0 != null)
				npc.getAI().addCastDesire(npc._c_ai0, getNpcSkillByType(npc, NpcSkillType.BUFF), 1000000);
		}
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
			if (Rnd.get(100) < 33)
			{
				if (Rnd.get(100) < 60)
					npc.getAI().addCastDesire(npc, getNpcSkillByType(npc, NpcSkillType.WEAK_MODE_FALSE), 1000000);
				else
					npc.getAI().addCastDesire(npc, getNpcSkillByType(npc, NpcSkillType.WEAK_MODE_TRUE), 1000000);
			}
		}
		return super.onTimer(name, npc, player);
	}
}