package com.shnok.javaserver.gameserver.enums;

public enum PunishmentType
{
	NONE(""),
	CHAT("chat banned"),
	JAIL("jailed"),
	CHAR("banned"),
	ACC("banned");
	
	private final String _description;
	
	PunishmentType(String description)
	{
		_description = description;
	}
	
	public String getDescription()
	{
		return _description;
	}
	
	public static final PunishmentType[] VALUES = values();
}