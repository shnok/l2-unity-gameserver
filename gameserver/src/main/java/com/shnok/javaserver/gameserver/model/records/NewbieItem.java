package com.shnok.javaserver.gameserver.model.records;

import com.shnok.javaserver.commons.data.StatSet;

public record NewbieItem(int id, int count, boolean isEquipped)
{
	public NewbieItem(StatSet set)
	{
		this(set.getInteger("id"), set.getInteger("count"), set.getBool("isEquipped", true));
	}
}