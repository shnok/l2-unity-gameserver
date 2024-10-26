package com.shnok.javaserver.gameserver.scripting.script.ai.individual.Monster.LV3Monster;

import net.sf.l2j.gameserver.model.actor.Creature;
import net.sf.l2j.gameserver.model.actor.Npc;
import net.sf.l2j.gameserver.model.actor.Playable;
import net.sf.l2j.gameserver.model.actor.Player;

public class LV3PartyLeaderMonster extends LV3Monster
{
	public LV3PartyLeaderMonster()
	{
		super("ai/individual/Monster/LV3Monster");
	}
	
	public LV3PartyLeaderMonster(String descr)
	{
		super(descr);
	}
	
	@Override
	public void onCreated(Npc npc)
	{
		createPrivates(npc);
		
		super.onCreated(npc);
	}
	
	@Override
	public void onPartyAttacked(Npc caller, Npc called, Creature target, int damage)
	{
		final Player c0 = target.getActingPlayer();
		if (c0 != null)
		{
			if (c0.getObjectId() != called._param2)
			{
				if (called._c_ai1 != null)
					((Npc) called._c_ai1).sendScriptEvent(1000, 0, 0);
				
				called.deleteMe();
			}
			else if (target instanceof Playable)
			{
				called.getAI().addAttackDesire(target, ((damage / called.getStatus().getMaxHp()) / 0.05) * 50);
			}
		}
	}
}
