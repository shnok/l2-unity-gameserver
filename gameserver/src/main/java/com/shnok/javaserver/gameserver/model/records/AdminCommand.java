package net.sf.l2j.gameserver.model.records;

import net.sf.l2j.commons.data.StatSet;

public record AdminCommand(String name, int accessLevel, String params, String desc)
{
	public AdminCommand(StatSet set)
	{
		this(set.getString("name"), set.getInteger("accessLevel", 8), set.getString("params", ""), set.getString("desc", "The description is missing."));
	}
}