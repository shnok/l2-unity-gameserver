package com.shnok.javaserver.gameserver.scripting.script.ai.individual.Monster.LV3Monster;

import net.sf.l2j.commons.random.Rnd;

import net.sf.l2j.gameserver.enums.actors.NpcSkillType;
import net.sf.l2j.gameserver.model.World;
import net.sf.l2j.gameserver.model.actor.Creature;
import net.sf.l2j.gameserver.model.actor.Npc;

public class LV3Wizard extends LV3Monster
{
	public LV3Wizard()
	{
		super("ai/individual/Monster/LV3Monster");
	}
	
	public LV3Wizard(String descr)
	{
		super(descr);
	}
	
	protected final int[] _npcIds =
	{
		27250,
		27251,
		27252,
		27257
	};
	
	@Override
	public void onCreated(Npc npc)
	{
		npc._i_ai0 = 5;
		npc.getAI().addCastDesire(npc, getNpcSkillByType(npc, NpcSkillType.SUMMON_MODE), 1000000);
		
		createOnePrivateEx(npc, 27253, npc.getX(), npc.getY(), npc.getZ(), 0, 0, false, npc._param1, npc._param2, npc._param3);
		
		super.onCreated(npc);
	}
	
	@Override
	public void onPartyDied(Npc caller, Npc called)
	{
		if (called != caller)
		{
			if (called._i_ai0 > 0)
			{
				createOnePrivateEx(called, 27253, called.getX(), called.getY(), called.getZ(), 0, 0, false, called._param1, called._param2, called._param3);
				called.getAI().addCastDesire(called, getNpcSkillByType(called, NpcSkillType.SUMMON_EFFECT), 1000000);
				called._i_ai0 = (called._i_ai0 - 1);
			}
			else
			{
				called.getAI().addCastDesire(called, getNpcSkillByType(called, NpcSkillType.SUMMON_MODE), 1000000);
			}
		}
	}
	
	@Override
	public void onScriptEvent(Npc npc, int eventId, int arg1, int arg2)
	{
		if (eventId == 1000)
		{
			final Creature c0 = (Creature) World.getInstance().getObject(arg1);
			if (c0 != null)
			{
				if (Rnd.get(100) < 50)
					npc.getAI().addCastDesire(c0, getNpcSkillByType(npc, NpcSkillType.SUMMON_HEAL1), 1000000);
				else
					npc.getAI().addCastDesire(c0, getNpcSkillByType(npc, NpcSkillType.SUMMON_HEAL2), 1000000);
			}
		}
	}
}
