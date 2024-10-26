package com.shnok.javaserver.gameserver.network.serverpackets;

import net.sf.l2j.gameserver.model.actor.Creature;

public class FinishRotation extends L2GameServerPacket
{
	private final int _heading;
	private final int _charObjId;
	
	public FinishRotation(Creature cha)
	{
		_charObjId = cha.getObjectId();
		_heading = cha.getHeading();
	}
	
	@Override
	protected final void writeImpl()
	{
		writeC(0x63);
		writeD(_charObjId);
		writeD(_heading);
	}
}