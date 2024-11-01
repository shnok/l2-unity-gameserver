package com.shnok.javaserver.gameserver.model.boat;

import java.util.List;

import com.shnok.javaserver.gameserver.network.serverpackets.L2GameServerPacket;

public record ScheduledBoatMessages(int delay, List<L2GameServerPacket> messages)
{
}