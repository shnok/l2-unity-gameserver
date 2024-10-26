package com.shnok.javaserver.gameserver.model.actor.ai.type;

import com.shnok.javaserver.gameserver.model.actor.Boat;
import com.shnok.javaserver.gameserver.model.actor.Creature;
import com.shnok.javaserver.gameserver.model.actor.Player;
import com.shnok.javaserver.gameserver.network.serverpackets.VehicleDeparture;

public class BoatAI extends CreatureAI<Boat>
{
	public BoatAI(Boat boat)
	{
		super(boat);
	}
	
	@Override
	public void describeStateToPlayer(Player player)
	{
		if (_actor.isMoving())
			player.sendPacket(new VehicleDeparture(_actor));
	}
	
	@Override
	public void onEvtAttacked(Creature attacker)
	{
		// Do nothing.
	}
	
	@Override
	protected void onEvtArrived()
	{
		_actor.getMove().onArrival();
	}
	
	@Override
	protected void onEvtDead()
	{
		// Do nothing.
	}
	
	@Override
	protected void onEvtFinishedCasting()
	{
		// Do nothing.
	}
}