package com.shnok.javaserver.gameserver.scripting.script.ai.siegablehall.BloodyLordNurka1;

import com.shnok.javaserver.gameserver.model.actor.Npc;
import com.shnok.javaserver.gameserver.model.actor.Player;
import com.shnok.javaserver.gameserver.model.location.Location;

public class BloodyLordNurka2 extends BloodyLordNurka1
{
	public BloodyLordNurka2()
	{
		super("ai/siegeablehall/BloodyLordNurka1");
	}
	
	public BloodyLordNurka2(String descr)
	{
		super(descr);
	}
	
	protected final int[] _npcIds =
	{
		35375
	};
	
	@Override
	public void onCreated(Npc npc)
	{
		npc.getSpawn().instantTeleportInMyTerritory(51952, 111060, -1970, 200);
		startQuestTimerAtFixedRate("2001", npc, null, 10000, 10000);
		npc._flag = 0;
		createPrivates(npc);
	}
	
	@Override
	public String onTimer(String name, Npc npc, Player player)
	{
		if (name.equalsIgnoreCase("2001"))
		{
			final long timeAfterSiegeStart = (npc.getSiegableHall().getSiegeDate().getTimeInMillis() - System.currentTimeMillis()) / 1000;
			if (timeAfterSiegeStart >= 1800)
			{
				if (timeAfterSiegeStart < 3600)
				{
					if (npc._flag < 1)
					{
						npc.getAI().addMoveToDesire(new Location(45802, 109981, -1970), 100000000);
						npc._flag = 1;
					}
				}
				else if (npc._flag < 2)
				{
					npc.getAI().addMoveToDesire(new Location(44525, 108867, -2020), 100000000);
					npc._flag = 2;
				}
			}
		}
		return super.onTimer(name, npc, player);
	}
	
	@Override
	public void onPartyDied(Npc caller, Npc called)
	{
		if (caller != called)
			caller.scheduleRespawn((caller.getSpawn().getRespawnDelay() * 1000));
	}
}
