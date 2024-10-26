package com.shnok.javaserver.gameserver.network.clientpackets;

import net.sf.l2j.gameserver.network.serverpackets.L2GameServerPacket;
import net.sf.l2j.gameserver.network.serverpackets.VersionCheck;

public final class SendProtocolVersion extends L2GameClientPacket
{
	private int _version;
	
	@Override
	protected void readImpl()
	{
		_version = readD();
	}
	
	@Override
	protected void runImpl()
	{
		switch (_version)
		{
			case 737, 740, 744, 746:
				getClient().sendPacket(new VersionCheck(getClient().enableCrypt()));
				break;
			
			default:
				getClient().close((L2GameServerPacket) null);
				break;
		}
	}
}