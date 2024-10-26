package com.shnok.javaserver.gameserver.network.serverpackets;

import com.shnok.javaserver.gameserver.enums.SayType;
import com.shnok.javaserver.gameserver.network.SystemMessageId;

public class BoatSay extends CreatureSay
{
	public BoatSay(SystemMessageId smId)
	{
		super(SayType.BOAT, 801, smId);
	}
}