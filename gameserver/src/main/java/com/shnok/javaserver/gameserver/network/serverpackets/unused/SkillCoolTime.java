package com.shnok.javaserver.gameserver.network.serverpackets.unused;

import java.util.List;

import com.shnok.javaserver.gameserver.model.actor.Player;
import com.shnok.javaserver.gameserver.model.records.Timestamp;
import com.shnok.javaserver.gameserver.network.serverpackets.L2GameServerPacket;

public class SkillCoolTime extends L2GameServerPacket
{
	public final List<Timestamp> _reuseTimeStamps;
	
	public SkillCoolTime(Player cha)
	{
		_reuseTimeStamps = cha.getReuseTimeStamps().stream().filter(r -> r.hasNotPassed()).toList();
	}
	
	@Override
	protected void writeImpl()
	{
		writeC(0xc1);
		writeD(_reuseTimeStamps.size()); // list size
		for (Timestamp ts : _reuseTimeStamps)
		{
			writeD(ts.skillId());
			writeD(ts.skillLevel());
			writeD((int) ts.reuse() / 1000);
			writeD((int) ts.getRemaining() / 1000);
		}
	}
}