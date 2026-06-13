package com.shnok.javaserver.commons.network;

public enum ServerType
{
	AUTO(0, "Auto"),
	GOOD(1, "Good"),
	NORMAL(2, "Normal"),
	FULL(3, "Full"),
	DOWN(4, "Down"),
	GM_ONLY(5, "Gm Only");
	
	private final int _id;
	private final String _name;
	
	private ServerType(int id, String name)
	{
		_id = id;
		_name = name;
	}
	
	public int getId()
	{
		return _id;
	}
	
	public String getName()
	{
		return _name;
	}
	
	public static final ServerType[] VALUES = values();
}
