package com.shnok.javaserver.gameserver.network.serverpackets;

import java.util.Set;

public class ExCursedWeaponList extends L2GameServerPacket
{
	private final Set<Integer> _cursedWeaponIds;
	
	public ExCursedWeaponList(Set<Integer> cursedWeaponIds)
	{
		_cursedWeaponIds = cursedWeaponIds;
	}
	
	@Override
	protected void writeImpl()
	{
		writeC(0xfe);
		writeH(0x45);
		
		writeD(_cursedWeaponIds.size());
		for (int id : _cursedWeaponIds)
			writeD(id);
	}
}