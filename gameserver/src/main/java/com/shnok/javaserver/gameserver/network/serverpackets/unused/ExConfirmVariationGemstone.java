package com.shnok.javaserver.gameserver.network.serverpackets.unused;

import com.shnok.javaserver.gameserver.network.serverpackets.L2GameServerPacket;

/**
 * Format: (ch)ddddd
 */
public class ExConfirmVariationGemstone extends L2GameServerPacket
{
	private final int _gemstoneObjId;
	private final int _unk1;
	private final int _gemstoneCount;
	private final int _unk2;
	private final int _unk3;
	
	public ExConfirmVariationGemstone(int gemstoneObjId, int count)
	{
		_gemstoneObjId = gemstoneObjId;
		_unk1 = 1;
		_gemstoneCount = count;
		_unk2 = 1;
		_unk3 = 1;
	}
	
	@Override
	protected void writeImpl()
	{
		writeC(0xfe);
		writeH(0x54);
		writeD(_gemstoneObjId);
		writeD(_unk1);
		writeD(_gemstoneCount);
		writeD(_unk2);
		writeD(_unk3);
	}
}