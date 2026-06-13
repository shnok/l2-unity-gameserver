package com.shnok.javaserver.gameserver.enums;

public enum StatusType
{
	LEVEL(1),
	EXP(2),
	STR(3),
	DEX(4),
	CON(5),
	INT(6),
	WIT(7),
	MEN(8),
	
	CUR_HP(9),
	MAX_HP(10),
	CUR_MP(11),
	MAX_MP(12),
	
	SP(13),
	CUR_LOAD(14),
	MAX_LOAD(15),
	
	P_ATK(17),
	ATK_SPD(18),
	P_DEF(19),
	EVASION(20),
	ACCURACY(21),
	CRITICAL(22),
	M_ATK(23),
	CAST_SPD(24),
	M_DEF(25),
	PVP_FLAG(26),
	KARMA(27),
	
	CUR_CP(33),
	MAX_CP(34);
	
	public static final StatusType[] VALUES = values();
	
	private final int _id;
	
	private StatusType(int id)
	{
		_id = id;
	}
	
	public int getId()
	{
		return _id;
	}
}