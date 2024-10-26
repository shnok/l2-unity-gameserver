package com.shnok.javaserver.gameserver.scripting.script.ai.individual.Monster.WarriorBase.Warrior.RoyalRushRoomBossBasic;

import com.shnok.javaserver.commons.random.Rnd;

import com.shnok.javaserver.gameserver.enums.actors.NpcSkillType;
import com.shnok.javaserver.gameserver.model.actor.Creature;
import com.shnok.javaserver.gameserver.model.actor.Npc;
import com.shnok.javaserver.gameserver.model.actor.Playable;
import com.shnok.javaserver.gameserver.skills.L2Skill;

public class RoyalRushRoomBoss1 extends RoyalRushRoomBossBasic
{
	public RoyalRushRoomBoss1()
	{
		super("ai/individual/Monster/WarriorBase/Warrior/RoyalRushRoomBossBasic");
	}
	
	public RoyalRushRoomBoss1(String descr)
	{
		super(descr);
	}
	
	protected final int[] _npcIds =
	{
		18121,
		18124,
		18127,
		18130,
		18174,
		18177,
		18180,
		18183
	};
	
	@Override
	public void onCreated(Npc npc)
	{
		npc._i_ai0 = 0;
		npc.getAI().addCastDesire(npc, getNpcSkillByType(npc, NpcSkillType.SELF_DEBUFF1), 1000000);
	}
	
	@Override
	public void onAttacked(Npc npc, Creature attacker, int damage, L2Skill skill)
	{
		if (attacker instanceof Playable)
		{
			final Creature topDesireTarget = npc.getAI().getTopDesireTarget();
			if (topDesireTarget != null && topDesireTarget == attacker && Rnd.get(100) < 33)
				npc.getAI().addCastDesire(attacker, getNpcSkillByType(npc, NpcSkillType.RANGE_DD_MAGIC1), 1000000);
		}
		
		super.onAttacked(npc, attacker, damage, skill);
	}
	
	@Override
	public void onClanDied(Npc caller, Npc called, Creature killer)
	{
		if (caller != called)
		{
			switch (called._i_ai0)
			{
				case 2:
				{
					called.getAI().addCastDesire(called, getNpcSkillByType(called, NpcSkillType.SELF_DEBUFF1), 1000000);
					break;
				}
				case 4:
				{
					called.getAI().addCastDesire(called, getNpcSkillByType(called, NpcSkillType.SELF_DEBUFF2), 1000000);
					break;
				}
				case 6:
				{
					called.getAI().addCastDesire(called, getNpcSkillByType(called, NpcSkillType.SELF_DEBUFF3), 1000000);
					break;
				}
			}
			called._i_ai0 = (called._i_ai0 + 1);
		}
		
		super.onClanDied(caller, called, killer);
	}
}
