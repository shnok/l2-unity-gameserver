package com.shnok.javaserver.gameserver.enums;

public enum CabalType
{
	NORMAL("No Cabal", "No Cabal"),
	DUSK("dusk", "Revolutionaries of Dusk"),
	DAWN("dawn", "Lords of Dawn");
	
	private final String _shortName;
	private final String _fullName;
	
	private CabalType(String shortName, String fullName)
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
	
	public static final CabalType[] VALUES = values();
}