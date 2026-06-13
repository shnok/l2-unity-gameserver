package com.shnok.javaserver.gameserver.scripting.script.ai.boss.drchaos;

import com.shnok.javaserver.gameserver.model.actor.Creature;
import com.shnok.javaserver.gameserver.model.actor.Npc;
import com.shnok.javaserver.gameserver.model.actor.Player;
import com.shnok.javaserver.gameserver.model.group.Party;
import com.shnok.javaserver.gameserver.model.location.Location;
import com.shnok.javaserver.gameserver.network.NpcStringId;
import com.shnok.javaserver.gameserver.network.serverpackets.unused.SpecialCamera;
import com.shnok.javaserver.gameserver.scripting.script.ai.individual.DefaultNpc;

public class DoctorChaosNpc extends DefaultNpc
{
	private static final int SUMMON_RAID = 25512;
	private static final int POS_MACHIN_X = 96320;
	private static final int POS_MACHIN_Y = -110912;
	private static final int POS_MACHIN_Z = -3360;
	private static final int POS_RBOSS_X = 94640;
	private static final int POS_RBOSS_Y = -112496;
	private static final int POS_RBOSS_Z = -3360;
	private static final int POS_DOOR_X = 96016;
	private static final int POS_DOOR_Y = -110736;
	private static final int POS_DOOR_Z = -3376;
	
	public DoctorChaosNpc()
	{
		super("ai/boss/drchaos");
		
		addFirstTalkId(32033);
	}
	
	public DoctorChaosNpc(String descr)
	{
		super(descr);
		
		addFirstTalkId(32033);
	}
	
	protected final int[] _npcIds =
	{
		32033 // dr_chaos_npc
	};
	
	@Override
	public void onCreated(Npc npc)
	{
		npc._i_ai1 = 0;
		npc._c_ai1 = null;
		npc._c_ai2 = null;
		
		if (npc.getZ() == -15536)
			createOnePrivateEx(npc, SUMMON_RAID, POS_RBOSS_X, POS_RBOSS_Y, POS_RBOSS_Z, 0, 0, false);
		
		super.onCreated(npc);
	}
	
	@Override
	public String onFirstTalk(Npc npc, Player player)
	{
		npc._c_ai1 = player;
		npc.broadcastNpcSay(NpcStringId.ID_1010578);
		startQuestTimer("1001", npc, player, 2000);
		
		return null;
	}
	
	@Override
	public String onTimer(String name, Npc npc, Player player)
	{
		if (name.equalsIgnoreCase("1001"))
		{
			npc.lookNeighbor();
			npc.getAI().addMoveToDesire(new Location(POS_MACHIN_X, POS_MACHIN_Y, POS_MACHIN_Z), 1000);
		}
		else if (name.equalsIgnoreCase("1002"))
		{
			npc.broadcastPacket(new SpecialCamera(npc.getObjectId(), 1, -150, 10, 3000, 20000, 0, 0, 0, 0));
			npc.getAI().addMoveToDesire(new Location(POS_DOOR_X, POS_DOOR_Y, POS_DOOR_Z), 1000);
		}
		else if (name.equalsIgnoreCase("2001"))
		{
			if (npc._c_ai1 != null && npc.distance3D(npc._c_ai1) < 600)
			{
				npc._i_ai1 = 1;
				npc.broadcastNpcSay(NpcStringId.ID_1010579);
				startQuestTimer("2002", npc, null, 2000);
				startQuestTimer("1002", npc, null, 11000);
			}
			else
			{
				npc.getAI().addMoveToDesire(npc.getSpawnLocation(), 1000);
				npc._c_ai1 = null;
			}
		}
		else if (name.equalsIgnoreCase("2002"))
		{
			if (npc._c_ai2 == null)
				npc._c_ai2 = npc;
			
			npc._c_ai2.broadcastPacket(new SpecialCamera(npc._c_ai2.getObjectId(), 1, -200, 15, 10000, 20000, 0, 0, 0, 0));
			npc.getAI().addSocialDesire(3, 5000, 1000000);
		}
		else if (name.equalsIgnoreCase("3001"))
		{
			createOnePrivateEx(npc, SUMMON_RAID, POS_RBOSS_X, POS_RBOSS_Y, POS_RBOSS_Z, 0, 0, false, 1000, npc._c_ai1.getObjectId(), 0);
		}
		
		return super.onTimer(name, npc, player);
	}
	
	@Override
	public void onSeeCreature(Npc npc, Creature creature)
	{
		if (creature instanceof Npc npcCreature && npcCreature.getNpcId() == 32032)
			npc._c_ai2 = creature;
	}
	
	@Override
	public void onMoveToFinished(Npc npc, int x, int y, int z)
	{
		if (x == npc.getSpawnLocation().getX() && y == npc.getSpawnLocation().getY())
		{
			npc.removeAllDesire();
		}
		else if (x == POS_MACHIN_X && y == POS_MACHIN_Y)
		{
			npc.getAI().addAttackDesire(npc._c_ai2, 10);
			startQuestTimer("2001", npc, null, 3000);
		}
		else if (x == POS_DOOR_X && y == POS_DOOR_Y && npc._c_ai1 != null)
		{
			npc.broadcastNpcSay(NpcStringId.ID_1010580);
			
			final Party party0 = npc._c_ai1.getParty();
			if (party0 == null)
				npc._c_ai1.teleportTo(94832, -112624, -3328, 0);
			else
				for (Player partyMember : party0.getMembers())
					partyMember.teleportTo(94832, -112624, -3328, 0);
				
			startQuestTimer("3001", npc, null, 3000);
			
			npc.teleportTo(-113091, -243942, -15536, 0);
		}
	}
	
	@Override
	public void onScriptEvent(Npc npc, int eventId, int arg1, int arg2)
	{
		if (eventId == 10029)
			npc.deleteMe();
	}
	
	@Override
	public void onPartyDied(Npc caller, Npc called)
	{
		if (caller != called)
			called.deleteMe();
	}
}
