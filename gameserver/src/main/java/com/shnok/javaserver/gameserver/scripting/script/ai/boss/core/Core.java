package com.shnok.javaserver.gameserver.scripting.script.ai.boss.core;

import net.sf.l2j.commons.random.Rnd;

import net.sf.l2j.gameserver.data.manager.SpawnManager;
import net.sf.l2j.gameserver.model.actor.Creature;
import net.sf.l2j.gameserver.model.actor.Npc;
import net.sf.l2j.gameserver.model.actor.Player;
import net.sf.l2j.gameserver.model.actor.instance.Door;
import net.sf.l2j.gameserver.network.NpcStringId;
import net.sf.l2j.gameserver.network.serverpackets.PlaySound;
import net.sf.l2j.gameserver.scripting.script.ai.individual.DefaultNpc;
import net.sf.l2j.gameserver.skills.L2Skill;

public class Core extends DefaultNpc
{
	private static final int TELEPORTATION_CUBE = 31842;
	private static final int CORE_DOOR = 20210001;
	
	public Core()
	{
		super("ai/boss/core");
	}
	
	public Core(String descr)
	{
		super(descr);
		addDoorChange(CORE_DOOR);
	}
	
	protected final int[] _npcIds =
	{
		29006 // core
	};
	
	@Override
	public void onDoorChange(Door door)
	{
		if (door.isOpened())
			SpawnManager.getInstance().startSpawnTime("door_open", "[kuruma_parent]", null, null, false);
		else
			SpawnManager.getInstance().stopSpawnTime("door_open", "[kuruma_parent]", null, null, false);
	}
	
	@Override
	public void onCreated(Npc npc)
	{
		npc.broadcastPacket(new PlaySound(1, "BS01_A", npc));
		npc._i_ai0 = 0;
		
		createPrivates(npc);
	}
	
	@Override
	public void onPartyDied(Npc caller, Npc called)
	{
		if (caller != called && called.isMaster() && !called.isDead())
			caller.scheduleRespawn((280 + Rnd.get(40)) * 1000L);
	}
	
	@Override
	public void onAttacked(Npc npc, Creature attacker, int damage, L2Skill skill)
	{
		if (attacker.getStatus().getLevel() > npc.getStatus().getLevel() + 8)
			npc.getAI().addCastDesire(attacker, 4515, 1, 1000000);
		
		if (npc._i_ai0 == 0)
		{
			npc.broadcastNpcSay(NpcStringId.ID_1000001);
			npc.broadcastNpcSay(NpcStringId.ID_1000002);
			
			npc._i_ai0 = 1;
		}
		else if (Rnd.get(100) < 1)
			npc.broadcastNpcSay(NpcStringId.ID_1000003);
	}
	
	@Override
	public void onClanAttacked(Npc caller, Npc called, Creature attacker, int damage, L2Skill skill)
	{
		if (!called.isDead())
		{
			if (called._i_ai0 == 0)
			{
				called.broadcastNpcSay(NpcStringId.ID_1000001);
				called.broadcastNpcSay(NpcStringId.ID_1000002);
				called._i_ai0 = 1;
			}
			else if (Rnd.get(100) < 1)
				called.broadcastNpcSay(NpcStringId.ID_1000003);
		}
	}
	
	@Override
	public void onMyDying(Npc npc, Creature killer)
	{
		npc.broadcastPacket(new PlaySound(1, "BS02_D", npc));
		
		npc.broadcastNpcSay(NpcStringId.ID_1000004);
		npc.broadcastNpcSay(NpcStringId.ID_1000005);
		npc.broadcastNpcSay(NpcStringId.ID_1000006);
		
		addSpawn(TELEPORTATION_CUBE, 16502, 110165, -6394, 0, false, 900000, false);
		addSpawn(TELEPORTATION_CUBE, 18948, 110166, -6397, 0, false, 900000, false);
	}
	
	@Override
	public void onSeeSpell(Npc npc, Player caster, L2Skill skill, Creature[] targets, boolean isPet)
	{
		if (caster.getStatus().getLevel() > npc.getStatus().getLevel() + 8)
		{
			npc.getAI().addCastDesire(caster, 4215, 1, 1000000);
			return;
		}
		
		super.onSeeSpell(npc, caster, skill, targets, isPet);
	}
}