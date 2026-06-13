package com.shnok.javaserver.gameserver.enums.actors;

public enum OperateType
{
	NONE(0),
	SELL(1),
	SELL_MANAGE(2),
	BUY(3),
	BUY_MANAGE(4),
	MANUFACTURE(5),
	MANUFACTURE_MANAGE(6),
	OBSERVE(7),
	PACKAGE_SELL(8);
	
	private int _id;
	
	private OperateType(int id)
	{
		_id = id;
	}
	
	public int getId()
	{
		return _id;
	}
}