package com.shnok.javaserver.gameserver.scripting.script.ai.individual.RoyalRushDefaultNpc;

import com.shnok.javaserver.gameserver.model.actor.Creature;
import com.shnok.javaserver.gameserver.model.actor.Npc;
import com.shnok.javaserver.gameserver.skills.L2Skill;

public class RoyalRushTrasureBox extends RoyalRushAfflict
{
	public RoyalRushTrasureBox()
	{
		super("ai/individual/RoyalRushDefaultNpc");
	}
	
	public RoyalRushTrasureBox(String descr)
	{
		super(descr);
	}
	
	protected final int[] _npcIds =
	{
		18256
	};
	
	@Override
	public void onCreated(Npc npc)
	{
		npc.scheduleDespawn(300000);
		
		super.onCreated(npc);
	}
	
	@Override
	public void onAttacked(Npc npc, Creature attacker, int damage, L2Skill skill)
	{
		npc.getAI().addFleeDesire(attacker, 150, 200);
	}
}
