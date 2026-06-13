package com.shnok.javaserver.gameserver.scripting.script.ai.individual.Monster.WarriorBase.Warrior.PartyPrivateWarrior.PartyPrivateCoupleFollower;

import com.shnok.javaserver.gameserver.model.actor.Creature;
import com.shnok.javaserver.gameserver.model.actor.Npc;
import com.shnok.javaserver.gameserver.scripting.script.ai.individual.Monster.WarriorBase.Warrior.PartyPrivateWarrior.PartyPrivateWarrior;

public class PartyPrivateCoupleFollower extends PartyPrivateWarrior
{
	public PartyPrivateCoupleFollower()
	{
		super("ai/individual/Monster/WarriorBase/Warrior/PartyPrivateWarrior/PartyPrivateCoupleFollower");
	}
	
	public PartyPrivateCoupleFollower(String descr)
	{
		super(descr);
	}
	
	@Override
	public void onCreated(Npc npc)
	{
		npc._i_ai0 = 0;
		
		super.onCreated(npc);
	}
	
	@Override
	public void onPartyDied(Npc caller, Npc called)
	{
		if ((!called.hasMaster() || called.getMaster().isDead()) && called._i_ai0 == 0 && called.getStatus().getHpRatio() > 0.7)
		{
			final Creature topDesireTarget = called.getAI().getTopDesireTarget();
			if (topDesireTarget != null)
			{
				final int silhouette = getNpcIntAIParam(called, "silhouette");
				final int x = called.getX();
				final int y = called.getY();
				final int z = called.getZ();
				
				createOnePrivateEx(called, silhouette, x + 10, y, z, 0, 0, false, 1000, topDesireTarget.getObjectId(), 0);
				createOnePrivateEx(called, silhouette, x, y + 10, z, 0, 0, false, 1000, topDesireTarget.getObjectId(), 0);
				createOnePrivateEx(called, silhouette, x + 5, y + 5, z, 0, 0, false, 1000, topDesireTarget.getObjectId(), 0);
				
				called._i_ai0 = 1;
			}
		}
	}
}
