package com.shnok.javaserver.gameserver.model.records;

import com.shnok.javaserver.commons.data.StatSet;

public record HealSps(int skillId, int skillLevel, int magicLevel, int correction, int neededMatk)
{
	public HealSps(StatSet set)
	{
		this(set.getInteger("skillId", 0), set.getInteger("skillLevel", 0), set.getInteger("magicLevel", 0), set.getInteger("correction"), set.getInteger("neededMatk"));
	}
}