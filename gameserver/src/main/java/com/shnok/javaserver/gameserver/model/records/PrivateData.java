package com.shnok.javaserver.gameserver.model.records;

import com.shnok.javaserver.commons.data.StatSet;
import com.shnok.javaserver.commons.lang.StringUtil;

public record PrivateData(int id, int weight, int respawnTime)
{
	public PrivateData(StatSet set)
	{
		this(set.getInteger("id"), set.getInteger("weight"), StringUtil.getTimeStamp(set.getString("respawn")));
	}
}