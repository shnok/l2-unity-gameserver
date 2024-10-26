package com.shnok.javaserver.gameserver.scripting.script.ai.boss.sailren;

import com.shnok.javaserver.commons.random.Rnd;

import com.shnok.javaserver.gameserver.data.manager.SpawnManager;
import com.shnok.javaserver.gameserver.model.actor.Creature;
import com.shnok.javaserver.gameserver.model.actor.Npc;
import com.shnok.javaserver.gameserver.model.actor.Player;
import com.shnok.javaserver.gameserver.model.group.Party;
import com.shnok.javaserver.gameserver.model.spawn.NpcMaker;
import com.shnok.javaserver.gameserver.network.serverpackets.SpecialCamera;
import com.shnok.javaserver.gameserver.scripting.script.ai.individual.DefaultNpc;

public class SailrenDummy extends DefaultNpc
{
	public SailrenDummy()
	{
		super("ai/boss/sailren");
	}
	
	public SailrenDummy(String descr)
	{
		super(descr);
	}
	
	protected final int[] _npcIds =
	{
		32110 // sailren_dummy
	};
	
	@Override
	public void onCreated(Npc npc)
	{
		npc._i_ai0 = 0;
		
		super.onCreated(npc);
	}
	
	@Override
	public String onTimer(String name, Npc npc, Player player)
	{
		if (name.matches("(?i)1001|1002|1003|1004"))
		{
			npc.getAI().getDesireQueue().clear();
			
			npc.getAI().addCastDesire(npc, 5090, 1, 1000000);
		}
		else if (name.matches("(?i)1005|1006"))
		{
			npc.getAI().getDesireQueue().clear();
			
			npc.getAI().addCastDesire(npc, 5091, 1, 1000000);
		}
		else if (name.equalsIgnoreCase("2000"))
		{
			npc.broadcastPacket(new SpecialCamera(npc.getObjectId(), 100, 180, 30, 3000, 20000, 0, 50, 1, 0));
			
			startQuestTimer("2001", npc, null, 3000);
		}
		else if (name.equalsIgnoreCase("2001"))
		{
			npc.broadcastPacket(new SpecialCamera(npc.getObjectId(), 150, 270, 25, 3000, 20000, 0, 30, 1, 0));
			
			startQuestTimer("2002", npc, null, 3000);
		}
		else if (name.equalsIgnoreCase("2002"))
		{
			npc.broadcastPacket(new SpecialCamera(npc.getObjectId(), 160, 360, 20, 3000, 20000, 10, 15, 1, 0));
			
			startQuestTimer("2003", npc, null, 3000);
		}
		else if (name.equalsIgnoreCase("2003"))
		{
			npc.broadcastPacket(new SpecialCamera(npc.getObjectId(), 160, 450, 10, 3000, 20000, 0, 10, 1, 0));
			
			startQuestTimer("2004", npc, null, 3000);
		}
		else if (name.equalsIgnoreCase("2004"))
		{
			npc.broadcastPacket(new SpecialCamera(npc.getObjectId(), 160, 560, 0, 3000, 20000, 0, 10, 1, 0));
			
			startQuestTimer("2005", npc, null, 7000);
		}
		else if (name.equalsIgnoreCase("2005"))
		{
			npc.broadcastPacket(new SpecialCamera(npc.getObjectId(), 70, 560, 0, 500, 7000, -15, 20, 1, 0));
			
			startQuestTimer("2006", npc, null, 15000);
		}
		else if (name.equalsIgnoreCase("2006"))
		{
			broadcastScriptEventEx(npc, 11055, npc._i_ai0, 0, 1000);
		}
		else if (name.equalsIgnoreCase("3001"))
		{
			npc.broadcastPacket(new SpecialCamera(npc.getObjectId(), 60, 110, 30, 4000, 20000, 0, 65, 1, 0));
			npc.getAI().addCastDesire(npc, 5090, 1, 1000000);
			startQuestTimer("1001", npc, null, 3400);
			npc.getAI().addCastDesire(npc, 5090, 1, 1000000);
			startQuestTimer("1002", npc, null, 6800);
			npc.getAI().addCastDesire(npc, 5090, 1, 1000000);
			startQuestTimer("1003", npc, null, 10000);
			npc.getAI().addCastDesire(npc, 5090, 1, 1000000);
			startQuestTimer("1004", npc, null, 13200);
			npc.getAI().addCastDesire(npc, 5091, 1, 1000000);
			startQuestTimer("1005", npc, null, 16500);
			npc.getAI().addCastDesire(npc, 5091, 1, 1000000);
			startQuestTimer("1006", npc, null, 23500);
			startQuestTimer("2000", npc, null, 4100);
		}
		return super.onTimer(name, npc, player);
	}
	
	@Override
	public void onSeeCreature(Npc npc, Creature creature)
	{
		if (creature instanceof Player)
		{
			final Party party0 = creature.getParty();
			if (party0 != null)
				npc._i_ai0 = party0.getLeader().getObjectId();
		}
	}
	
	@Override
	public void onScriptEvent(Npc npc, int eventId, int arg1, int arg2)
	{
		if (eventId == 11044)
		{
			startQuestTimer("3001", npc, null, 180006);
		}
		else if (eventId == 11046 || eventId == 11048)
		{
			oustPlayers(npc);
		}
		else if (eventId == 11051)
		{
			oustPlayers(npc);
			
			NpcMaker maker0 = SpawnManager.getInstance().getNpcMaker("rune20_mb2017_01m1");
			if (maker0 != null)
			{
				maker0.getMaker().onMakerScriptEvent("11052", maker0, 0, 0);
				maker0.getMaker().onMakerScriptEvent("1000", maker0, 0, 0);
			}
			
			maker0 = SpawnManager.getInstance().getNpcMaker("rune20_mb2017_02m1");
			if (maker0 != null)
			{
				maker0.getMaker().onMakerScriptEvent("11052", maker0, 0, 0);
				maker0.getMaker().onMakerScriptEvent("1000", maker0, 0, 0);
			}
			
			maker0 = SpawnManager.getInstance().getNpcMaker("rune20_mb2017_03m1");
			if (maker0 != null)
			{
				maker0.getMaker().onMakerScriptEvent("11052", maker0, 0, 0);
				maker0.getMaker().onMakerScriptEvent("1000", maker0, 0, 0);
			}
			
			maker0 = SpawnManager.getInstance().getNpcMaker("rune16_npc2017_01m1");
			if (maker0 != null)
				maker0.getMaker().onMakerScriptEvent("11050", maker0, 0, 0);
		}
		else if (eventId == 11054)
		{
			createOnePrivateEx(npc, 32107, 27644, -6638, -2008, 0, 0, true);
		}
		else if (eventId == 11047)
		{
			oustPlayers(npc);
		}
	}
	
	private static void oustPlayers(Npc npc)
	{
		if (Rnd.get(100) < 50)
			npc.getSpawn().instantTeleportInMyTerritory(23575, -7727, -1272, 100);
		else
			npc.getSpawn().instantTeleportInMyTerritory(23421, -8167, -1326, 100);
	}
}