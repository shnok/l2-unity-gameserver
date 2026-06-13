package com.shnok.javaserver.gameserver.scripting.script.ai.individual.Monster.LV3Monster;

import com.shnok.javaserver.gameserver.model.World;
import com.shnok.javaserver.gameserver.model.actor.Creature;
import com.shnok.javaserver.gameserver.model.actor.Npc;
import com.shnok.javaserver.gameserver.model.actor.Playable;
import com.shnok.javaserver.gameserver.model.actor.Player;
import com.shnok.javaserver.gameserver.scripting.script.ai.individual.Monster.MonsterAI;
import com.shnok.javaserver.gameserver.skills.L2Skill;

public class LV3Monster extends MonsterAI
{
	public LV3Monster()
	{
		super("ai/individual/Monster/LV3Monster");
	}
	
	public LV3Monster(String descr)
	{
		super(descr);
	}
	
	@Override
	public void onCreated(Npc npc)
	{
		if (npc._param1 != 0)
		{
			npc._c_ai0 = (Creature) World.getInstance().getObject(npc._param1);
			if (npc._c_ai0 != null)
				npc.getAI().addAttackDesire(npc._c_ai0, 200);
		}
		
		if (npc._param3 != 0 && npc.hasMaster())
			npc._c_ai1 = npc.getMaster();
		
		startQuestTimer("3000", npc, null, 12 * 60 * 1000L);
	}
	
	@Override
	public void onAttacked(Npc npc, Creature attacker, int damage, L2Skill skill)
	{
		final Player c0 = attacker.getActingPlayer();
		if (c0 != null)
		{
			if (c0.getObjectId() != npc._param2)
			{
				if (npc._c_ai1 != null)
					((Npc) npc._c_ai1).sendScriptEvent(1000, 0, 0);
				
				final Npc npc0 = (Npc) World.getInstance().getObject(npc._param3);
				if (npc0 != null)
					npc0._i_quest0 = 0;
				
				npc.deleteMe();
			}
			
			if (attacker instanceof Playable)
			{
				double f0 = getHateRatio(npc, attacker);
				f0 = (((1.0 * damage) / (npc.getStatus().getLevel() + 7)) + ((f0 / 100) * ((1.0 * damage) / (npc.getStatus().getLevel() + 7))));
				npc.getAI().addAttackDesire(attacker, f0 * 100);
			}
		}
	}
	
	@Override
	public String onTimer(String name, Npc npc, Player player)
	{
		if (name.equalsIgnoreCase("3000"))
		{
			final Npc npc0 = (Npc) World.getInstance().getObject(npc._param3);
			if (npc0 != null)
				npc0._i_quest0 = 0;
			
			npc.deleteMe();
		}
		
		return null;
	}
	
	@Override
	public void onSeeSpell(Npc npc, Player caster, L2Skill skill, Creature[] targets, boolean isPet)
	{
		if (caster != null && caster.getObjectId() != npc._param2)
		{
			if (npc._c_ai1 != null)
				((Npc) npc._c_ai1).sendScriptEvent(1000, 0, 0);
			
			final Npc npc0 = (Npc) World.getInstance().getObject(npc._param3);
			if (npc0 != null)
				npc0._i_quest0 = 0;
			
			npc.deleteMe();
		}
	}
}
