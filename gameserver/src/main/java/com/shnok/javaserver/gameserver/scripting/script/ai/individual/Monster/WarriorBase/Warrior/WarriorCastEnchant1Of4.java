package com.shnok.javaserver.gameserver.scripting.script.ai.individual.Monster.WarriorBase.Warrior;

import com.shnok.javaserver.commons.random.Rnd;

import com.shnok.javaserver.gameserver.enums.actors.NpcSkillType;
import com.shnok.javaserver.gameserver.model.actor.Creature;
import com.shnok.javaserver.gameserver.model.actor.Npc;
import com.shnok.javaserver.gameserver.model.actor.Playable;
import com.shnok.javaserver.gameserver.skills.L2Skill;

public class WarriorCastEnchant1Of4 extends Warrior
{
	protected static final NpcSkillType[] BUFFS =
	{
		NpcSkillType.SELF_BUFF1,
		NpcSkillType.SELF_BUFF2,
		NpcSkillType.SELF_BUFF3,
		NpcSkillType.SELF_BUFF4
	};
	
	public WarriorCastEnchant1Of4()
	{
		super("ai/individual/Monster/WarriorBase/Warrior");
	}
	
	public WarriorCastEnchant1Of4(String descr)
	{
		super(descr);
	}
	
	protected final int[] _npcIds =
	{
		22009
	};
	
	@Override
	public void onCreated(Npc npc)
	{
		npc._i_ai0 = Rnd.get(BUFFS.length);
		npc._i_ai1 = 0;
		
		super.onCreated(npc);
	}
	
	@Override
	public void onAttacked(Npc npc, Creature attacker, int damage, L2Skill skill)
	{
		if (attacker instanceof Playable)
		{
			if (npc._i_ai1 == 0 && Rnd.get(100) < 33 && npc.getStatus().getHpRatio() > 0.5)
			{
				final L2Skill selfBuff = getNpcSkillByType(npc, BUFFS[npc._i_ai0]);
				
				npc.getAI().addCastDesire(npc, selfBuff, 1000000);
			}
			npc._i_ai1 = 1;
		}
		super.onAttacked(npc, attacker, damage, skill);
	}
}