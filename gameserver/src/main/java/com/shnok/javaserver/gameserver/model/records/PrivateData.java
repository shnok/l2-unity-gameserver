package net.sf.l2j.gameserver.model.records;

import net.sf.l2j.commons.data.StatSet;
import net.sf.l2j.commons.lang.StringUtil;

public record PrivateData(int id, int weight, int respawnTime)
{
	public PrivateData(StatSet set)
	{
		this(set.getInteger("id"), set.getInteger("weight"), StringUtil.getTimeStamp(set.getString("respawn")));
	}
}