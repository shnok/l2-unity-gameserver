package com.shnok.javaserver.gameserver.enums.actors;

import com.shnok.javaserver.gameserver.enums.skills.Stats;

public enum HennaType
{
	INT(Stats.STAT_INT),
	STR(Stats.STAT_STR),
	CON(Stats.STAT_CON),
	MEN(Stats.STAT_MEN),
	DEX(Stats.STAT_DEX),
	WIT(Stats.STAT_WIT);
	
	private final Stats _stats;
	
	HennaType(Stats stats)
	{
		_stats = stats;
	}
	
	public Stats getStats()
	{
		return _stats;
	}
}