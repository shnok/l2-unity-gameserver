package com.shnok.javaserver.gameserver.scripting.script.ai.boss.frintezza;

import com.shnok.javaserver.gameserver.data.SkillTable;
import com.shnok.javaserver.gameserver.model.actor.Npc;
import com.shnok.javaserver.gameserver.model.actor.Player;
import com.shnok.javaserver.gameserver.model.memo.GlobalMemo;
import com.shnok.javaserver.gameserver.network.serverpackets.unused.PlaySound;
import com.shnok.javaserver.gameserver.network.serverpackets.unused.SpecialCamera;
import com.shnok.javaserver.gameserver.scripting.script.ai.individual.DefaultNpc;

public class OrganDummy extends DefaultNpc
{
	public OrganDummy()
	{
		super("ai/boss/frintezza");
	}
	
	public OrganDummy(String descr)
	{
		super(descr);
	}
	
	protected final int[] _npcIds =
	{
		29052
	};
	
	@Override
	public void onCreated(Npc npc)
	{
		final Npc c0 = (Npc) GlobalMemo.getInstance().getCreature("4");
		if (c0 != null && c0.getSpawn().getSpawnData().getDBValue() >= 4)
		{
			startQuestTimer("1020", npc, null, 1000);
			return;
		}
		
		startQuestTimer("1000", npc, null, 1);
	}
	
	@Override
	public String onTimer(String name, Npc npc, Player player)
	{
		if (name.equalsIgnoreCase("1000"))
		{
			npc.broadcastPacket(new PlaySound(1, "BS04_A", npc));
			npc.broadcastPacket(new SpecialCamera(npc.getObjectId(), 2500, 180, 20, 0, 10000, 20, 90, 1, 1));
			
			startQuestTimer("1001", npc, null, 100);
		}
		else if (name.equalsIgnoreCase("1001"))
		{
			npc.broadcastPacket(new SpecialCamera(npc.getObjectId(), 2500, 180, 20, 100, 10000, 20, 90, 1, 1));
			
			startQuestTimer("1002", npc, null, 400);
		}
		else if (name.equalsIgnoreCase("1002"))
		{
			npc.broadcastPacket(new SpecialCamera(npc.getObjectId(), 2000, 190, 0, 4000, 10000, 0, 30, 1, 1));
			
			startQuestTimer("1003", npc, null, 4000);
		}
		else if (name.equalsIgnoreCase("1003"))
		{
			npc.broadcastPacket(new SpecialCamera(npc.getObjectId(), 100, 180, 0, 5000, 10000, 0, 0, 1, 1));
			
			startQuestTimer("1004", npc, null, 7300);
		}
		else if (name.equalsIgnoreCase("1004"))
		{
			npc.broadcastPacket(new SpecialCamera(npc.getObjectId(), 5, 165, -15, 5000, 10000, 6, 15, 1, 1));
			
			startQuestTimer("1005", npc, null, 7300);
		}
		else if (name.equalsIgnoreCase("1005"))
		{
			npc.broadcastPacket(new SpecialCamera(npc.getObjectId(), 1100, 240, 0, 0, 10000, 210, 0, 1, 1));
			
			startQuestTimer("1006", npc, null, 100);
		}
		else if (name.equalsIgnoreCase("1006"))
		{
			npc.broadcastPacket(new SpecialCamera(npc.getObjectId(), 1100, 240, 0, 0, 10000, 210, 0, 1, 1));
			
			startQuestTimer("1007", npc, null, 3400);
		}
		else if (name.equalsIgnoreCase("1007"))
		{
			npc.broadcastPacket(new SpecialCamera(npc.getObjectId(), 150, 180, 0, 0, 10000, 0, 0, 1, 1));
			
			startQuestTimer("1008", npc, null, 100);
		}
		else if (name.equalsIgnoreCase("1008"))
		{
			npc.broadcastPacket(new SpecialCamera(npc.getObjectId(), 150, 180, 0, 0, 10000, 0, 0, 1, 1));
			
			startQuestTimer("1009", npc, null, 400);
		}
		else if (name.equalsIgnoreCase("1009"))
		{
			npc.broadcastPacket(new SpecialCamera(npc.getObjectId(), 50, 180, 20, 4000, 10000, 0, -5, 1, 1));
			
			final Npc c0 = (Npc) GlobalMemo.getInstance().getCreature("4");
			if (c0 != null)
				npc.getAI().addAttackDesire(c0, 200);
			
			startQuestTimer("1010", npc, null, 5000);
		}
		else if (name.equalsIgnoreCase("1010"))
		{
			npc.broadcastPacket(new SpecialCamera(npc.getObjectId(), 0, 280, 40, 0, 10000, 0, 0, 1, 1));
			
			startQuestTimer("1011", npc, null, 2500);
		}
		else if (name.equalsIgnoreCase("1011"))
		{
			npc.broadcastPacket(new SpecialCamera(npc.getObjectId(), 500, 230, 45, 7500, 10000, 15, 3, 1, 1));
			
			startQuestTimer("1012", npc, null, 7500);
		}
		else if (name.equalsIgnoreCase("1012"))
		{
			npc.broadcastPacket(new SpecialCamera(npc.getObjectId(), 2100, 200, 13, 9000, 10000, 4, 10, 1, 1));
			
			startQuestTimer("1013", npc, null, 9500);
		}
		else if (name.equalsIgnoreCase("1013"))
		{
			npc.broadcastPacket(new SpecialCamera(npc.getObjectId(), 2000, 190, 0, 0, 10000, -50, 45, 1, 1));
			
			startQuestTimer("1014", npc, null, 100);
		}
		else if (name.equalsIgnoreCase("1014"))
		{
			npc.broadcastPacket(new SpecialCamera(npc.getObjectId(), 2000, 190, 0, 0, 10000, -50, 45, 1, 1));
			
			startQuestTimer("1015", npc, null, 1000);
		}
		else if (name.equalsIgnoreCase("1015"))
		{
			npc.broadcastPacket(new SpecialCamera(npc.getObjectId(), 2000, 195, 0, 1800, 10000, -47, 42, 1, 1));
			
			startQuestTimer("1016", npc, null, 1800);
		}
		else if (name.equalsIgnoreCase("1016"))
		{
			npc.broadcastPacket(new SpecialCamera(npc.getObjectId(), 2000, 195, 0, 600, 10000, -47, 0, 1, 1));
			
			startQuestTimer("1017", npc, null, 2800);
		}
		else if (name.equalsIgnoreCase("1017"))
		{
			npc.broadcastPacket(new SpecialCamera(npc.getObjectId(), 1800, 175, 0, 0, 10000, 40, -3, 1, 1));
			
			startQuestTimer("1018", npc, null, 100);
		}
		else if (name.equalsIgnoreCase("1018"))
		{
			npc.broadcastPacket(new SpecialCamera(npc.getObjectId(), 1800, 175, 0, 0, 10000, 40, -3, 1, 1));
			
			startQuestTimer("1019", npc, null, 900);
		}
		else if (name.equalsIgnoreCase("1019"))
		{
			npc.broadcastPacket(new SpecialCamera(npc.getObjectId(), 2000, 180, 1, 2000, 3000, 0, -5, 1, 1));
			
			startQuestTimer("1020", npc, null, 3000);
		}
		else if (name.equalsIgnoreCase("1020"))
		{
			final Npc c0 = (Npc) GlobalMemo.getInstance().getCreature("4");
			if (c0 != null && npc.distance2D(c0) < 20)
				npc.getAI().addAttackDesire(c0, 200);
			else
				npc.deleteMe();
			
			startQuestTimer("1020", npc, null, 10000);
		}
		else if (name.equalsIgnoreCase("2000"))
		{
			npc.broadcastPacket(new SpecialCamera(npc.getObjectId(), 500, 210, 10, 0, 10000, 10, 0, 1, 1));
			
			startQuestTimer("2001", npc, null, 100);
		}
		else if (name.equalsIgnoreCase("2001"))
		{
			npc.broadcastPacket(new SpecialCamera(npc.getObjectId(), 500, 210, 10, 0, 10000, 10, 0, 1, 1));
			
			startQuestTimer("2002", npc, null, 400);
		}
		else if (name.equalsIgnoreCase("2002"))
		{
			npc.broadcastPacket(new SpecialCamera(npc.getObjectId(), 5, 210, 10, 5500, 10000, 0, 0, 1, 1));
			
			startQuestTimer("2003", npc, null, 5500);
		}
		else if (name.equalsIgnoreCase("2003"))
		{
			npc.broadcastPacket(new SpecialCamera(npc.getObjectId(), 5, 150, 10, 0, 10000, 0, 0, 1, 1));
			
			startQuestTimer("2004", npc, null, 100);
		}
		else if (name.equalsIgnoreCase("2004"))
		{
			npc.broadcastPacket(new SpecialCamera(npc.getObjectId(), 5, 150, 10, 0, 10000, 0, 0, 1, 1));
			
			startQuestTimer("2005", npc, null, 1000);
		}
		else if (name.equalsIgnoreCase("2005"))
		{
			npc.broadcastPacket(new SpecialCamera(npc.getObjectId(), 1500, 180, 10, 6000, 6000, 0, 13, 1, 1));
			
			startQuestTimer("2006", npc, null, 6000);
		}
		else if (name.equalsIgnoreCase("3000"))
		{
			npc.broadcastPacket(new SpecialCamera(npc.getObjectId(), 0, 230, 10, 0, 10000, 0, -5, 1, 1));
			
			startQuestTimer("3001", npc, null, 100);
		}
		else if (name.equalsIgnoreCase("3001"))
		{
			npc.removeAllDesire();
			
			final Npc c0 = (Npc) GlobalMemo.getInstance().getCreature("4");
			if (c0 != null)
				npc.getAI().addCastDesire(c0, SkillTable.getInstance().getInfo(5005, 1), 1000000);
			
			npc.broadcastPacket(new SpecialCamera(npc.getObjectId(), 0, 230, 10, 0, 10000, 0, -5, 1, 1));
			
			startQuestTimer("3002", npc, null, 400);
		}
		else if (name.equalsIgnoreCase("3002"))
		{
			npc.broadcastPacket(new SpecialCamera(npc.getObjectId(), 0, 180, 10, 5000, 10000, 0, -5, 1, 1));
			
			startQuestTimer("3006", npc, null, 7000);
		}
		else if (name.equalsIgnoreCase("3006"))
		{
			npc.broadcastPacket(new SpecialCamera(npc.getObjectId(), 1000, 180, 15, 8000, 8000, 0, 10, 1, 1));
			
			startQuestTimer("3007", npc, null, 8000);
		}
		return super.onTimer(name, npc, player);
	}
	
	@Override
	public void onPartyDied(Npc caller, Npc called)
	{
		if (caller.getNpcId() == 29045)
			called.doDie(called);
	}
	
	@Override
	public void onScriptEvent(Npc npc, int eventId, int arg1, int arg2)
	{
		if (arg1 == 50000)
			startQuestTimer("2000", npc, null, 1000);
		else if (arg1 == 8)
			startQuestTimer("3000", npc, null, 5000);
	}
}