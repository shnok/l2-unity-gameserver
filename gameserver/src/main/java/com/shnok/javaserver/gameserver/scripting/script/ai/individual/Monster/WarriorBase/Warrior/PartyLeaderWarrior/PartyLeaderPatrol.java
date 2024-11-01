package com.shnok.javaserver.gameserver.scripting.script.ai.individual.Monster.WarriorBase.Warrior.PartyLeaderWarrior;

import com.shnok.javaserver.gameserver.model.actor.Npc;

public class PartyLeaderPatrol extends PartyLeaderWarrior
{
	public PartyLeaderPatrol()
	{
		super("ai/individual/Monster/WarriorBase/Warrior/PartyLeaderWarrior");
	}
	
	public PartyLeaderPatrol(String descr)
	{
		super(descr);
	}
	
	protected final int[] _npcIds =
	{
		27041
	};
	
	@Override
	public void onNoDesire(Npc npc)
	{
		// Do nothing.
	}
}
