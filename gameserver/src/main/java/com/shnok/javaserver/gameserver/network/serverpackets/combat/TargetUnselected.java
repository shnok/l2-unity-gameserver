package com.shnok.javaserver.gameserver.network.serverpackets.combat;

import com.shnok.javaserver.gameserver.model.actor.Creature;
import com.shnok.javaserver.gameserver.network.serverpackets.L2GameServerPacket;

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