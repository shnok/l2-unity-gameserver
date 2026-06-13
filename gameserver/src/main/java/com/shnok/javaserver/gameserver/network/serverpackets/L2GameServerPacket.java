package com.shnok.javaserver.gameserver.network.serverpackets;

import com.shnok.javaserver.commons.logging.CLogger;
import com.shnok.javaserver.commons.mmocore.SendablePacket;

import com.shnok.javaserver.Config;
import com.shnok.javaserver.gameserver.network.GameClient;

public abstract class L2GameServerPacket extends SendablePacket<GameClient>
{
	protected static final CLogger LOGGER = new CLogger(L2GameServerPacket.class.getName());
	
	protected abstract void writeImpl();
	
	@Override
	protected void write()
	{
		if (Config.PACKET_HANDLER_DEBUG)
			LOGGER.info(getType());
		
		try
		{
			writeImpl();
		}
		catch (Exception e)
		{
			LOGGER.error("Failed writing {} for {}. ", e, getType(), getClient().toString());
		}
	}
	
	public void runImpl()
	{
	}
	
	public String getType()
	{
		return "[S] " + getClass().getSimpleName();
	}
}