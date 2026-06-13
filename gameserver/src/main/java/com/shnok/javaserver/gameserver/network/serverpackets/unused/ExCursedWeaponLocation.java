package com.shnok.javaserver.gameserver.network.serverpackets.unused;

import java.util.List;

import com.shnok.javaserver.gameserver.model.records.CursedWeaponInfo;
import com.shnok.javaserver.gameserver.network.serverpackets.L2GameServerPacket;

public class ExCursedWeaponLocation extends L2GameServerPacket
{
	private final List<CursedWeaponInfo> _cursedWeaponInfo;
	
	public ExCursedWeaponLocation(List<CursedWeaponInfo> cursedWeaponInfo)
	{
		_cursedWeaponInfo = cursedWeaponInfo;
	}
	
	@Override
	protected void writeImpl()
	{
		writeC(0xfe);
		writeH(0x46);
		
		if (!_cursedWeaponInfo.isEmpty())
		{
			writeD(_cursedWeaponInfo.size());
			for (CursedWeaponInfo cwi : _cursedWeaponInfo)
			{
				writeD(cwi.id());
				writeD(cwi.activated());
				writeLoc(cwi.pos());
			}
		}
		else
		{
			writeD(0);
			writeD(0);
		}
	}
}