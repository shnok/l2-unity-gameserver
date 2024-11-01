package com.shnok.javaserver.gameserver.network.serverpackets.combat;

import com.shnok.javaserver.gameserver.network.serverpackets.L2GameServerPacket;

public class AutoAttackStop extends L2GameServerPacket
{
	private final int _targetObjId;
	
	public AutoAttackStop(int targetObjId)
	{
		_targetObjId = targetObjId;
	}
	
	@Override
	protected final void writeImpl()
	{
		writeC(0x2c);
		writeD(_targetObjId);
	}
}