package com.shnok.javaserver.gameserver.network.serverpackets;

import net.sf.l2j.gameserver.enums.SayType;
import net.sf.l2j.gameserver.network.SystemMessageId;

public class BoatSay extends CreatureSay
{
	public BoatSay(SystemMessageId smId)
	{
		super(SayType.BOAT, 801, smId);
	}
}