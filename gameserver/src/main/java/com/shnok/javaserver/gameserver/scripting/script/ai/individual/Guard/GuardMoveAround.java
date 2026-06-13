package com.shnok.javaserver.gameserver.scripting.script.ai.individual.Guard;

import com.shnok.javaserver.gameserver.model.actor.Npc;

public class GuardMoveAround extends Guard
{
	public GuardMoveAround()
	{
		super("ai/individual/Guard");
	}
	
	public GuardMoveAround(String descr)
	{
		super(descr);
	}
	
	protected final int[] _npcIds =
	{
		31844,
		31845,
		31846,
		31847,
		31848,
		31849,
		31850,
		31851,
		31853
	};
	
	@Override
	public void onNoDesire(Npc npc)
	{
		npc.getAI().addWanderDesire(5, 5);
	}
}
