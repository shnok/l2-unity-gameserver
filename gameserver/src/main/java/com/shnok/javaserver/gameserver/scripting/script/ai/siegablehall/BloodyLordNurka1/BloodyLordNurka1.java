package com.shnok.javaserver.gameserver.scripting.script.ai.siegablehall.BloodyLordNurka1;

import com.shnok.javaserver.commons.random.Rnd;

import com.shnok.javaserver.gameserver.data.SkillTable;
import com.shnok.javaserver.gameserver.model.actor.Creature;
import com.shnok.javaserver.gameserver.model.actor.Npc;
import com.shnok.javaserver.gameserver.model.actor.Playable;
import com.shnok.javaserver.gameserver.model.actor.Player;
import com.shnok.javaserver.gameserver.scripting.script.ai.individual.DefaultNpc;
import com.shnok.javaserver.gameserver.skills.L2Skill;

public class BloodyLordNurka1 extends DefaultNpc
{
	private static final L2Skill DEBUFF = SkillTable.getInstance().getInfo(5456, 1);
	private static final L2Skill DD_MAGIC = SkillTable.getInstance().getInfo(4042, 1);
	
	public BloodyLordNurka1()
	{
		super("ai/siegeablehall/BloodyLordNurka1");
	}
	
	public BloodyLordNurka1(String descr)
	{
		super(descr);
	}
	
	protected final int[] _npcIds =
	{
		35368
	};
	
	@Override
	public void onCreated(Npc npc)
	{
		npc.getSpawn().instantTeleportInMyTerritory(51952, 111060, -1970, 200);
	}
	
	@Override
	public void onAttacked(Npc npc, Creature attacker, int damage, L2Skill skill)
	{
		if (attacker.getStatus().getLevel() > npc.getStatus().getLevel() + 8 && getAbnormalLevel(attacker, DEBUFF) == -1)
		{
			npc.getAI().addCastDesireHold(attacker, DEBUFF, 1000000);
			npc.getAI().getAggroList().stopHate(attacker);
		}
		if (attacker instanceof Playable)
		{
			if (Rnd.get(100) < 10)
				npc.getAI().addCastDesireHold(attacker, DD_MAGIC, 1000000);
			
			npc.getAI().addAttackDesireHold(attacker, (((damage * 1.0) / npc.getStatus().getMaxHp()) / 0.050000) * 10000);
		}
	}
	
	@Override
	public void onClanAttacked(Npc caller, Npc called, Creature attacker, int damage, L2Skill skill)
	{
		if (attacker instanceof Playable)
		{
			if (Rnd.get(100) < 2)
				called.getAI().addCastDesireHold(attacker, DD_MAGIC, 1000000);
			
			called.getAI().addAttackDesireHold(attacker, (((damage * 1.0) / called.getStatus().getMaxHp()) / 0.050000) * 5000);
		}
	}
	
	@Override
	public void onSeeSpell(Npc npc, Player caster, L2Skill skill, Creature[] targets, boolean isPet)
	{
		if (skill.getAggroPoints() > 0 && Rnd.get(100) < 15)
			npc.getAI().addCastDesireHold(caster, DD_MAGIC, 1000000);
	}
}
