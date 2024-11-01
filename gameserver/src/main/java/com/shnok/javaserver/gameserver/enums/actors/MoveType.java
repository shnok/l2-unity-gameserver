package com.shnok.javaserver.gameserver.enums.actors;

public enum MoveType
{
	GROUND(0),
	SWIM(1),
	FLY(2);
	
	private final int _id;
	private final int _mask;
	
	private MoveType(int id)
	{
		_id = id;
		_mask = (1 << ordinal());
	}
	
	public int getId()
	{
		return _id;
	}
	
	public int getMask()
	{
		return _mask;
	}
}