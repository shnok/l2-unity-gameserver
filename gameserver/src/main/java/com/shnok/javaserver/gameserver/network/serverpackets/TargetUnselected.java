package com.shnok.javaserver.gameserver.network.serverpackets;

import net.sf.l2j.gameserver.model.actor.Creature;

public class TargetUnselected extends L2GameServerPacket
{
	private final int _targetObjId;
	private final int _x;
	private final int _y;
	private final int _z;
	
	public TargetUnselected(Creature character)
	{
		_targetObjId = character.getObjectId();
		_x = character.getX();
		_y = character.getY();
		_z = character.getZ();
	}
	
	@Override
	protected final void writeImpl()
	{
		writeC(0x2a);
		writeD(_targetObjId);
		writeD(_x);
		writeD(_y);
		writeD(_z);
	}
}