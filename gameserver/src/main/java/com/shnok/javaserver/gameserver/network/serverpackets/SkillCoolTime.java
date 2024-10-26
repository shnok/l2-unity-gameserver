package com.shnok.javaserver.gameserver.network.serverpackets;

import java.util.List;

import net.sf.l2j.gameserver.model.actor.Player;
import net.sf.l2j.gameserver.model.records.Timestamp;

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