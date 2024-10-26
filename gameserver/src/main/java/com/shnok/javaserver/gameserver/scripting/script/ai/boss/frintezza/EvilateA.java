package com.shnok.javaserver.gameserver.scripting.script.ai.boss.frintezza;

import com.shnok.javaserver.gameserver.model.actor.Creature;
import com.shnok.javaserver.gameserver.model.actor.Npc;
import com.shnok.javaserver.gameserver.model.actor.Player;
import com.shnok.javaserver.gameserver.scripting.script.ai.individual.DefaultNpc;
import com.shnok.javaserver.gameserver.skills.L2Skill;

public class EvilateA extends DefaultNpc
{
	public EvilateA()
	{
		super("ai/boss/frintezza");
	}
	
	public EvilateA(String descr)
	{
		super(descr);
	}
	
	protected final int[] _npcIds =
	{
		29049
	};
	
	@Override
	public void onCreated(Npc npc)
	{
		startQuestTimer("1000", npc, null, 45000);
	}
	
	@Override
	public String onTimer(String name, Npc npc, Player player)
	{
		final int spawnPosX = getNpcIntAIParam(npc, "SpawnPosX");
		final int spawnPosY = getNpcIntAIParam(npc, "SpawnPosY");
		final int spawnPosZ = getNpcIntAIParam(npc, "SpawnPosZ");
		final int spawnAngle = getNpcIntAIParam(npc, "SpawnAngle");
		
		if (name.equalsIgnoreCase("1000"))
		{
			createOnePrivateEx(npc, 29050, spawnPosX, spawnPosY, spawnPosZ, spawnAngle, 0, true);
			startQuestTimer("1001", npc, null, 20000);
		}
		else if (name.equalsIgnoreCase("1001"))
		{
			createOnePrivateEx(npc, 29050, spawnPosX, spawnPosY, spawnPosZ, spawnAngle, 0, true);
			startQuestTimer("1002", npc, null, 20000);
		}
		else if (name.equalsIgnoreCase("1002"))
		{
			createOnePrivateEx(npc, 29050, spawnPosX, spawnPosY, spawnPosZ, spawnAngle, 0, true);
			startQuestTimer("1003", npc, null, 20000);
		}
		else if (name.equalsIgnoreCase("1003"))
		{
			createOnePrivateEx(npc, 29050, spawnPosX, spawnPosY, spawnPosZ, spawnAngle, 0, true);
		}
		
		return super.onTimer(name, npc, player);
	}
	
	@Override
	public void onAttacked(Npc npc, Creature attacker, int damage, L2Skill skill)
	{
		if (skill != null && skill.getId() == 2276)
			npc.doDie(npc);
	}
	
	@Override
	public void onPartyDied(Npc caller, Npc called)
	{
		if (called != caller && called.isMaster() && called.isDead())
			caller.scheduleRespawn(20000);
	}
}
