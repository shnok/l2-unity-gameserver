package com.shnok.javaserver.gameserver.model.item;

public record LifeStone(int grade, int level)
{
	private static final int[] LEVELS =
	{
		46,
		49,
		52,
		55,
		58,
		61,
		64,
		67,
		70,
		76
	};
	
	public int getPlayerLevel()
	{
		return LEVELS[level];
	}
}