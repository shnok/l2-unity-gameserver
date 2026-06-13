package com.shnok.javaserver.gameserver.scripting.script.ai.individual.Monster.WarriorBase.Warrior.WarriorCastHoldMagic;

import com.shnok.javaserver.commons.random.Rnd;

import com.shnok.javaserver.gameserver.data.SkillTable;
import com.shnok.javaserver.gameserver.model.actor.Creature;
import com.shnok.javaserver.gameserver.model.actor.Npc;
import com.shnok.javaserver.gameserver.model.actor.Player;
import com.shnok.javaserver.gameserver.skills.L2Skill;

public class OrfenPrivateRiba extends WarriorCastHoldMagic
{
	public OrfenPrivateRiba()
	{
		super("ai/individual/Monster/WarriorBase/Warrior/WarriorCastHoldMagicAggressive");
	}
	
	public OrfenPrivateRiba(String descr)
	{
		super(descr);
	}
	
	protected final int[] _npcIds =
	{
		29017 // riba
	};
	
	@Override
	public void onCreated(Npc npc)
	{
		startQuestTimer("5001", npc, null, (90000 + Rnd.get(240000)));
		
		super.onCreated(npc);
	}
	
	@Override
	public String onTimer(String name, Npc npc, Player player)
	{
		if (name.equalsIgnoreCase("5001"))
		{
			if (Rnd.get(100) < 66)
			{
				npc.getAI().getAggroList().randomizeAttack();
			}
			
			startQuestTimer("5001", npc, null, (90000 + Rnd.get(240000)));
		}
		
		return super.onTimer(name, npc, null);
	}
	
	@Override
	public void onAttacked(Npc npc, Creature attacker, int damage, L2Skill skill)
	{
		if (attacker.getStatus().getLevel() > (npc.getStatus().getLevel() + 8))
		{
			final L2Skill raidCurse = SkillTable.getInstance().getInfo(4515, 1);
			npc.getAI().addCastDesire(attacker, raidCurse, 1000000);
		}
	}
	
	@Override
	public void onSeeSpell(Npc npc, Player caster, L2Skill skill, Creature[] targets, boolean isPet)
	{
		if (caster.getStatus().getLevel() > (npc.getStatus().getLevel() + 8))
		{
			final L2Skill raidMute = SkillTable.getInstance().getInfo(4215, 1);
			
			npc.getAI().addCastDesire(caster, raidMute, 1000000);
			
			return;
		}
		super.onSeeSpell(npc, caster, skill, targets, isPet);
	}
}
