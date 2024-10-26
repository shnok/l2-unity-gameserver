package com.shnok.javaserver.gameserver.scripting.script.ai.individual;

import com.shnok.javaserver.gameserver.enums.ZoneId;
import com.shnok.javaserver.gameserver.model.actor.Creature;
import com.shnok.javaserver.gameserver.model.actor.Npc;
import com.shnok.javaserver.gameserver.model.actor.Playable;
import com.shnok.javaserver.gameserver.skills.L2Skill;

public class ImperialGravekeeper extends DefaultNpc
{
	public ImperialGravekeeper()
	{
		super("ai/individual");
	}
	
	public ImperialGravekeeper(String descr)
	{
		super(descr);
	}
	
	protected final int[] _npcIds =
	{
		27181
	};
	
	@Override
	public void onNoDesire(Npc npc)
	{
		npc.getAI().addWanderDesire(5, 5);
	}
	
	@Override
	public void onCreated(Npc npc)
	{
		npc._i_ai0 = 1;
		npc._i_ai1 = 50;
		npc._i_ai2 = 80;
		npc._weightPoint = 10;
	}
	
	@Override
	public void onAttacked(Npc npc, Creature attacker, int damage, L2Skill skill)
	{
		final double hpRatio = npc.getStatus().getHpRatio();
		
		if (!npc.isInsideZone(ZoneId.PEACE))
		{
			if (hpRatio * 100 <= npc._i_ai1)
			{
				if (npc._i_ai0 == 1 || npc._i_ai0 == 3)
					npc.getSpawn().instantTeleportInMyTerritory(179520, 6464, -2706, 200);
				else
					npc.getSpawn().instantTeleportInMyTerritory(171104, 6496, -2706, 200);
			}
			
			npc.getAI().addCastDesire(npc, 4080, 1, 1000000);
			
			if (npc._i_ai1 == 50)
				npc._i_ai1 = 30;
			else if (npc._i_ai1 == 30)
				npc._i_ai1 = -1;
		}
		
		if (hpRatio * 100 <= npc._i_ai2)
		{
			if (npc._i_ai2 == 80)
				npc._i_ai2 = 40;
			else if (npc._i_ai2 == 40)
				npc._i_ai2 = 20;
			else
				npc._i_ai2 = -1;
			
			createOnePrivate(npc, 27180, 0, false);
			createOnePrivate(npc, 27180, 0, false);
			createOnePrivate(npc, 27180, 0, false);
			createOnePrivate(npc, 27180, 0, false);
		}
		
		if (hpRatio > 0.5)
			npc._i_ai1 = 50;
		else if (hpRatio > 0.3)
			npc._i_ai1 = 30;
		
		if (hpRatio > 0.8)
			npc._i_ai2 = 80;
		else if (hpRatio > 0.4)
			npc._i_ai2 = 40;
		else if (hpRatio > 0.2)
			npc._i_ai2 = 20;
		
		if (attacker instanceof Playable)
			npc.getAI().addAttackDesire(attacker, (((damage * 1.0) / npc.getStatus().getMaxHp()) / 0.05) * 100);
	}
	
	@Override
	public void onPartyAttacked(Npc caller, Npc called, Creature target, int damage)
	{
		if (target instanceof Playable)
			called.getAI().addAttackDesire(target, (((damage * 1.0) / called.getStatus().getMaxHp()) / 0.05) * 50);
	}
	
	@Override
	public void onClanAttacked(Npc caller, Npc called, Creature attacker, int damage, L2Skill skill)
	{
		if (attacker instanceof Playable)
			called.getAI().addAttackDesire(attacker, (((damage * 1.0) / called.getStatus().getMaxHp()) / 0.05) * 50);
	}
	
	@Override
	public void onOutOfTerritory(Npc npc)
	{
		npc.removeAllAttackDesire();
	}
}