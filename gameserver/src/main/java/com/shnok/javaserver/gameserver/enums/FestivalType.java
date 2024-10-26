package com.shnok.javaserver.gameserver.enums;

public enum FestivalType
{
	MAX_31(60, "Level 31 or lower", 31),
	MAX_42(70, "Level 42 or lower", 42),
	MAX_53(100, "Level 53 or lower", 53),
	MAX_64(120, "Level 64 or lower", 64),
	MAX_NONE(150, "No Level Limit", 80);
	
	private final int _maxScore;
	private final String _name;
	private final int _maxLevel;
	
	private FestivalType(int maxScore, String name, int maxLevel)
	{
		_maxScore = maxScore;
		_name = name;
		_maxLevel = maxLevel;
	}
	
	public int getMaxScore()
	{
		return _maxScore;
	}
	
	public String getName()
	{
		return _name;
	}
	
	public int getMaxLevel()
	{
		return _maxLevel;
	}
	
	public static final FestivalType[] VALUES = values();
}