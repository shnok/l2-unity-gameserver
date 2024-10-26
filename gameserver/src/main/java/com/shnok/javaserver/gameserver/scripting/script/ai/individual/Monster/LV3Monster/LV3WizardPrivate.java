package com.shnok.javaserver.gameserver.scripting.script.ai.individual.Monster.LV3Monster;

import net.sf.l2j.commons.random.Rnd;

import net.sf.l2j.gameserver.enums.actors.NpcSkillType;
import net.sf.l2j.gameserver.model.World;
import net.sf.l2j.gameserver.model.actor.Creature;
import net.sf.l2j.gameserver.model.actor.Npc;
import net.sf.l2j.gameserver.model.actor.Player;
import net.sf.l2j.gameserver.skills.L2Skill;

public class LV3WizardPrivate extends LV3PartyPrivateMonster
{
	public LV3WizardPrivate()
	{
		super("ai/individual/Monster/LV3Monster");
	}
	
	public LV3WizardPrivate(String descr)
	{
		super(descr);
	}
	
	protected final int[] _npcIds =
	{
		27253
	};
	
	@Override
	public void onCreated(Npc npc)
	{
		npc._c_ai0 = (Creature) World.getInstance().getObject(npc._param1);
		if (npc._c_ai0 != null)
		{
			if (npc.distance2D(npc._c_ai0) < 100 && npc.getAI().getLifeTime() > 3)
				npc.getAI().addCastDesire(npc, getNpcSkillByType(npc, NpcSkillType.SELF_RANGE_DD_MAGIC1), 1000000);
			else
				npc.getAI().addAttackDesire(npc._c_ai0, 300);
		}
		
		npc.getMaster().sendScriptEvent(1000, npc.getObjectId(), 0);
		
		startQuestTimerAtFixedRate("4000", npc, null, 1000, 1000);
		
		super.onCreated(npc);
	}
	
	@Override
	public String onTimer(String name, Npc npc, Player player)
	{
		if (name.equalsIgnoreCase("4000"))
		{
			if (npc._c_ai0 != null)
			{
				if (npc.distance2D(npc._c_ai0) < 100)
					npc.getAI().addCastDesire(npc, getNpcSkillByType(npc, NpcSkillType.SELF_RANGE_DD_MAGIC1), 1000000);
				
				npc.getAI().addAttackDesire(npc._c_ai0, 50);
			}
		}
		
		return super.onTimer(name, npc, player);
	}
	
	@Override
	public void onUseSkillFinished(Npc npc, Creature creature, L2Skill skill, boolean success)
	{
		if (skill == getNpcSkillByType(npc, NpcSkillType.SELF_RANGE_DD_MAGIC1))
		{
			if (npc._c_ai0 != null)
			{
				if (Rnd.get(100) < 50 && npc.distance2D(npc._c_ai0) < 200)
					npc.getAI().addCastDesire(npc._c_ai0, getNpcSkillByType(npc, NpcSkillType.SELF_RANGE_DD_MAGIC2), 3000);
				else
					npc.getAI().addCastDesire(npc._c_ai0, getNpcSkillByType(npc, NpcSkillType.SELF_RANGE_DD_MAGIC3), 3000);
			}
			npc.removeAllAttackDesire();
		}
		else
		{
			npc.doDie(npc);
		}
	}
}
