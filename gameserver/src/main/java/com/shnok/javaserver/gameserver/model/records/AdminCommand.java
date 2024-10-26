package com.shnok.javaserver.gameserver.model.records;

import com.shnok.javaserver.commons.data.StatSet;

public record AdminCommand(String name, int accessLevel, String params, String desc)
{
	public AdminCommand(StatSet set)
	{
		this(set.getString("name"), set.getInteger("accessLevel", 8), set.getString("params", ""), set.getString("desc", "The description is missing."));
	}
}