package com.shnok.javaserver.gameserver.enums;

public enum SealType
{
	NONE("", ""),
	AVARICE("Avarice", "Seal of Avarice"),
	GNOSIS("Gnosis", "Seal of Gnosis"),
	STRIFE("Strife", "Seal of Strife");
	
	private final String _shortName;
	private final String _fullName;
	
	private SealType(String shortName, String fullName)
	{
		_shortName = shortName;
		_fullName = fullName;
	}
	
	public String getShortName()
	{
		return _shortName;
	}
	
	public String getFullName()
	{
		return _fullName;
	}
	
	public static final SealType[] VALUES = values();
}