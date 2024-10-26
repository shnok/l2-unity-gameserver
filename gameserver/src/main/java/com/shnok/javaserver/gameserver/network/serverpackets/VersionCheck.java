package com.shnok.javaserver.gameserver.network.serverpackets;

import java.util.Arrays;

import net.sf.l2j.Config;

public final class VersionCheck extends L2GameServerPacket
{
	private final byte[] _key;
	
	public VersionCheck(byte[] key)
	{
		_key = Arrays.copyOfRange(key, 0, 8);
	}
	
	@Override
	public void writeImpl()
	{
		writeC(0x00);
		writeC(0x01);
		writeB(_key);
		writeD(Config.USE_BLOWFISH_CIPHER ? 0x01 : 0x00);
		writeD(0x01);
	}
}