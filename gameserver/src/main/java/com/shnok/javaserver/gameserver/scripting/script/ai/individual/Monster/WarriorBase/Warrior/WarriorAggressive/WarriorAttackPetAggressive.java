package com.shnok.javaserver.gameserver.scripting.script.ai.individual.Monster.WarriorBase.Warrior.WarriorAggressive;

import net.sf.l2j.gameserver.model.World;
import net.sf.l2j.gameserver.model.actor.Creature;
import net.sf.l2j.gameserver.model.actor.Npc;

public class WarriorAttackPetAggressive extends WarriorAggressive
{
	public WarriorAttackPetAggressive()
	{
		super("ai/individual/Monster/WarriorBase/Warrior/WarriorAggressive");
	}
	
	public WarriorAttackPetAggressive(String descr)
	{
		super(descr);
	}
	
	protected final int[] _npcIds =
	{
		21444,
		21445,
		21448
	};
	
	@Override
	public void onScriptEvent(Npc npc, int eventId, int arg1, int arg2)
	{
		if (eventId == 10017)
		{
			final Creature c0 = (Creature) World.getInstance().getObject(arg1);
			if (c0 != null)
				npc.getAI().addAttackDesire(c0, 100);
		}
		super.onScriptEvent(npc, eventId, arg1, arg2);
	}
}