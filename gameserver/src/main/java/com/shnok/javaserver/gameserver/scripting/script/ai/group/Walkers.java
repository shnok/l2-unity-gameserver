package com.shnok.javaserver.gameserver.scripting.script.ai.group;

import com.shnok.javaserver.commons.util.ArraysUtil;

import com.shnok.javaserver.gameserver.model.actor.Npc;
import com.shnok.javaserver.gameserver.scripting.Quest;

public class Walkers extends Quest
{
	private static final int[] WALKING_NPCS =
	{
		31357,
		31358,
		31359,
		31360,
		31362,
		31364,
		31365,
		31525,
		32072,
		32128
	};
	
	public Walkers()
	{
		super(-1, "ai/group/Walker");
		
		addCreated(31356, 31357, 31358, 31359, 31360, 31361, 31362, 31363, 31364, 31365, 31525, 31705, 32070, 32072, 32128);
	}
	
	@Override
	public void onCreated(Npc npc)
	{
		if (ArraysUtil.contains(WALKING_NPCS, npc.getNpcId()))
			npc.setWalkOrRun(false);
		
		npc.getAI().addMoveRouteDesire(npc.getTemplate().getAlias(), 1000000);
	}
}