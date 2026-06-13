package com.shnok.javaserver.gameserver.model.actor.ai.type;

import com.shnok.javaserver.gameserver.model.actor.Creature;
import com.shnok.javaserver.gameserver.model.actor.instance.Door;

public class DoorAI extends CreatureAI<Door>
{
	public DoorAI(Door door)
	{
		super(door);
	}
	
	@Override
	protected void onEvtAttacked(Creature attacker)
	{
		// Do nothing.
	}
	
	@Override
	protected void onEvtFinishedAttack()
	{
		// Do nothing.
	}
	
	@Override
	protected void onEvtArrived()
	{
		// Do nothing.
	}
	
	@Override
	protected void onEvtArrivedBlocked()
	{
		// Do nothing.
	}
	
	@Override
	protected void onEvtDead()
	{
		// Do nothing.
	}
}