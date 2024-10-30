package com.shnok.javaserver.gameserver.network.serverpackets.combat;

import com.shnok.javaserver.gameserver.network.serverpackets.L2GameServerPacket;

public class AutoAttackStart extends L2GameServerPacket
{
	private final int _targetObjId;
	
	public AutoAttackStart(int targetId)
	{
		_targetObjId = targetId;
	}
	
	@Override
	protected final void writeImpl()
	{
		writeC(0x2b);
		writeD(_targetObjId);
	}
}