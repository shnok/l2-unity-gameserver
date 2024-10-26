package com.shnok.javaserver.gameserver.network.serverpackets;

public class ExOlympiadMode extends L2GameServerPacket
{
	private final int _mode;
	
	/**
	 * @param mode (0 = return, 1 = side 1, 2 = side 2, 3 = spectate)
	 */
	public ExOlympiadMode(int mode)
	{
		_mode = mode;
	}
	
	@Override
	protected final void writeImpl()
	{
		writeC(0xfe);
		writeH(0x2b);
		writeC(_mode);
	}
}