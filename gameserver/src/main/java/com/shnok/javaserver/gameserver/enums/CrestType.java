package com.shnok.javaserver.gameserver.enums;

public enum CrestType
{
	PLEDGE("Crest_", 256),
	PLEDGE_LARGE("LargeCrest_", 2176),
	ALLY("AllyCrest_", 192);
	
	private final String _prefix;
	private final int _size;
	
	private CrestType(String prefix, int size)
	{
		_prefix = prefix;
		_size = size;
	}
	
	public final String getPrefix()
	{
		return _prefix;
	}
	
	public final int getSize()
	{
		return _size;
	}
}