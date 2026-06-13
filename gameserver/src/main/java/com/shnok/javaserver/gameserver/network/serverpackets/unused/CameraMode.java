package com.shnok.javaserver.gameserver.network.serverpackets.unused;

import com.shnok.javaserver.gameserver.network.serverpackets.L2GameServerPacket;

public class CameraMode extends L2GameServerPacket
{
	private final int _mode;
	
	/**
	 * Forces client camera mode change
	 * @param mode 0 - third person cam 1 - first person cam
	 */
	public CameraMode(int mode)
	{
		_mode = mode;
	}
	
	@Override
	public void writeImpl()
	{
		writeC(0xf1);
		writeD(_mode);
	}
}