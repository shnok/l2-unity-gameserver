package com.shnok.javaserver.gameserver.scripting.script.ai.individual.Monster.WarriorBase.Warrior.WarriorSlowType;

import net.sf.l2j.commons.random.Rnd;

import net.sf.l2j.gameserver.enums.actors.NpcSkillType;
import net.sf.l2j.gameserver.model.actor.Creature;
import net.sf.l2j.gameserver.model.actor.Npc;
import net.sf.l2j.gameserver.skills.L2Skill;

public class WarriorSlowType2 extends WarriorSlowType
{
	public WarriorSlowType2()
	{
		super("ai/individual/Monster/WarriorBase/Warrior/WarriorSlowType");
	}
	
	public WarriorSlowType2(String descr)
	{
		super(descr);
	}
	
	protected final int[] _npcIds =
	{
		20201,
		20659,
		21800,
		20015,
		20020,
		27099,
		27017,
		20026,
		20029,
		20562,
		20046,
		20041,
		27024,
		20341,
		20616,
		20457,
		22045
	};
	
	@Override
	public void onAttacked(Npc npc, Creature attacker, int damage, L2Skill skill)
	{
		if (npc.distance2D(attacker) > 200 && Rnd.get(100) < 70)
		{
			L2Skill DDMagicSlow = getNpcSkillByType(npc, NpcSkillType.DD_MAGIC_SLOW);
			
			npc.getAI().addCastDesire(attacker, DDMagicSlow, 1000000);
		}
		
		super.onAttacked(npc, attacker, damage, skill);
	}
}
