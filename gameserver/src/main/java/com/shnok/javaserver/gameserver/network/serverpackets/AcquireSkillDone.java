package com.shnok.javaserver.gameserver.network.serverpackets;

public class AcquireSkillDone extends L2GameServerPacket
{
	public static final AcquireSkillDone STATIC_PACKET = new AcquireSkillDone();
	
	@Override
	protected final void writeImpl()
	{
		writeC(0x8e);
	}
}