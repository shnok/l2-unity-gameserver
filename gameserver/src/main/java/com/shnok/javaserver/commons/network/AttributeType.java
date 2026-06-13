package com.shnok.javaserver.commons.network;

public enum AttributeType
{
	NONE(0),
	STATUS(1),
	CLOCK(2),
	BRACKETS(3),
	AGE_LIMIT(4),
	TEST_SERVER(5),
	PVP_SERVER(6),
	MAX_PLAYERS(7);
	
	private final int _id;
	
	private AttributeType(int id)
	{
		_id = id;
	}
	
	public int getId()
	{
		return _id;
	}
	
	public static final AttributeType[] VALUES = values();
}
