package com.shnok.javaserver.gameserver.scripting.script.ai.individual.Monster.WarriorBase.Warrior.RoyalRushRoomBossBasic;

import com.shnok.javaserver.commons.random.Rnd;

import com.shnok.javaserver.gameserver.enums.actors.NpcSkillType;
import com.shnok.javaserver.gameserver.model.actor.Creature;
import com.shnok.javaserver.gameserver.model.actor.Npc;
import com.shnok.javaserver.gameserver.model.actor.Playable;
import com.shnok.javaserver.gameserver.skills.L2Skill;

public class RoyalRushRoomBoss4 extends RoyalRushRoomBossBasic
{
	public RoyalRushRoomBoss4()
	{
		super("ai/individual/Monster/WarriorBase/Warrior/RoyalRushRoomBossBasic");
	}
	
	public RoyalRushRoomBoss4(String descr)
	{
		super(descr);
	}
	
	protected final int[] _npcIds =
	{
		18141,
		18142,
		18143,
		18144,
		18173,
		18176,
		18179,
		18182,
		18212,
		18213,
		18214,
		18215,
		18216,
		18217,
		18218,
		18219
	};
	
	@Override
	public void onCreated(Npc npc)
	{
		npc.getAI().addCastDesire(npc, getNpcSkillByType(npc, NpcSkillType.CLAN_BUFF1), 1000000);
		
		super.onCreated(npc);
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
	public void onClanAttacked(Npc caller, Npc called, Creature attacker, int damage, L2Skill skill)
	{
		if (Rnd.get(100) < 33 && called.getAI().getTopDesireTarget() != attacker)
			called.getAI().addCastDesire(attacker, getNpcSkillByType(called, NpcSkillType.RANGE_DD_MAGIC1), 1000000);
		
		if (Rnd.get(100) < 10)
			called.getAI().addCastDesire(caller, getNpcSkillByType(called, NpcSkillType.CLAN_BUFF1), 1000000);
		
		super.onClanAttacked(caller, called, attacker, damage, skill);
	}
}
