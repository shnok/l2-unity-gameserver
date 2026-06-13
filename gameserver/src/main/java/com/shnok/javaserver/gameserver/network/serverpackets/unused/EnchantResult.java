package com.shnok.javaserver.gameserver.network.serverpackets.unused;

import com.shnok.javaserver.gameserver.network.serverpackets.L2GameServerPacket;

public class EnchantResult extends L2GameServerPacket
{
	public static final EnchantResult SUCCESS = new EnchantResult(0);
	public static final EnchantResult UNK_RESULT_1 = new EnchantResult(1);
	public static final EnchantResult CANCELLED = new EnchantResult(2);
	public static final EnchantResult UNSUCCESS = new EnchantResult(3);
	public static final EnchantResult UNK_RESULT_4 = new EnchantResult(4);
	
	private final int _result;
	
	private EnchantResult(int result)
	{
		_result = result;
	}
	
	@Override
	protected final void writeImpl()
	{
		writeC(0x81);
		writeD(_result);
	}
}