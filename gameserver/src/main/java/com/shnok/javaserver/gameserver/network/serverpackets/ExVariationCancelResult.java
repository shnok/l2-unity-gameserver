package com.shnok.javaserver.gameserver.network.serverpackets;

/**
 * Format: (ch)ddd
 */
public class ExVariationCancelResult extends L2GameServerPacket
{
	private final int _closeWindow;
	private final int _unk1;
	
	public ExVariationCancelResult(int result)
	{
		_closeWindow = 1;
		_unk1 = result;
	}
	
	@Override
	protected void writeImpl()
	{
		writeC(0xfe);
		writeH(0x57);
		writeD(_closeWindow);
		writeD(_unk1);
	}
}