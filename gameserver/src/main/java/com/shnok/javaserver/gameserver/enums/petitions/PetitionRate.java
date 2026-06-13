package com.shnok.javaserver.gameserver.enums.petitions;

public enum PetitionRate
{
	VERY_GOOD("Very Good"),
	GOOD("Good"),
	FAIR("Fair"),
	POOR("Poor"),
	VERY_POOR("Very Poor");
	
	public static final PetitionRate[] VALUES = values();
	
	private String _desc;
	
	private PetitionRate(String desc)
	{
		_desc = desc;
	}
	
	public String getDesc()
	{
		return _desc;
	}
}