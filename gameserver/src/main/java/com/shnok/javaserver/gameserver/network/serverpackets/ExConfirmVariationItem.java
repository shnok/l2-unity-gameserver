package com.shnok.javaserver.gameserver.network.serverpackets;

/**
 * Format: (ch)ddd
 */
public class ExConfirmVariationItem extends L2GameServerPacket
{
	private final int _itemObjId;
	private final int _unk1;
	private final int _unk2;
	
	public ExConfirmVariationItem(int itemObjId)
	{
		_itemObjId = itemObjId;
		_unk1 = 1;
		_unk2 = 1;
	}
	
	@Override
	protected void writeImpl()
	{
		writeC(0xfe);
		writeH(0x52);
		writeD(_itemObjId);
		writeD(_unk1);
		writeD(_unk2);
	}
}