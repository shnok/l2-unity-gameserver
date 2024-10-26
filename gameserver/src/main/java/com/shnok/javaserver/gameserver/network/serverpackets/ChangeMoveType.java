package com.shnok.javaserver.gameserver.network.serverpackets;

import com.shnok.javaserver.gameserver.model.actor.Creature;

public class ChangeMoveType extends L2GameServerPacket
{
	private final int _objectId;
	private final int _running;
	private final int _swimming;
	
	public ChangeMoveType(Creature creature)
	{
		_objectId = creature.getObjectId();
		_running = (creature.isRunning()) ? 1 : 0;
		_swimming = (creature.isInWater()) ? 1 : 0;
	}
	
	@Override
	protected final void writeImpl()
	{
		writeC(0x2e);
		writeD(_objectId);
		writeD(_running);
		writeD(_swimming);
	}
}