package com.shnok.javaserver.gameserver.network.serverpackets;

import com.shnok.javaserver.gameserver.model.actor.instance.Door;
import com.shnok.javaserver.gameserver.model.residence.castle.Castle;

public class DoorStatusUpdate extends L2GameServerPacket
{
	private final Door _door;
	private final boolean _showHp;
	
	public DoorStatusUpdate(Door door)
	{
		_door = door;
		_showHp = door.getResidence() instanceof Castle castle && castle.getSiege().isInProgress();
	}
	
	@Override
	protected final void writeImpl()
	{
		writeC(0x4d);
		writeD(_door.getObjectId());
		writeD(_door.isOpened() ? 0 : 1);
		writeD(_door.getDamage());
		writeD((_showHp) ? 1 : 0);
		writeD(_door.getDoorId());
		writeD(_door.getStatus().getMaxHp());
		writeD((int) _door.getStatus().getHp());
	}
}