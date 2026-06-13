package com.shnok.javaserver.gameserver.network.serverpackets;

public class ShortCutDelete extends L2GameServerPacket
{
	private final int _slot;
	
	public ShortCutDelete(int slot)
	{
		_slot = slot;
	}
	
	@Override
	protected final void writeImpl()
	{
		writeC(0x46);
		
		writeD(_slot);
		writeD(0x00);
	}
}