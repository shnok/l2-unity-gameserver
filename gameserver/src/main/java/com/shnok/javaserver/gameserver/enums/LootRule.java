package com.shnok.javaserver.gameserver.enums;

import com.shnok.javaserver.gameserver.network.SystemMessageId;

public enum LootRule
{
	ITEM_LOOTER(SystemMessageId.LOOTING_FINDERS_KEEPERS),
	ITEM_RANDOM(SystemMessageId.LOOTING_RANDOM),
	ITEM_RANDOM_SPOIL(SystemMessageId.LOOTING_RANDOM_INCLUDE_SPOIL),
	ITEM_ORDER(SystemMessageId.LOOTING_BY_TURN),
	ITEM_ORDER_SPOIL(SystemMessageId.LOOTING_BY_TURN_INCLUDE_SPOIL);
	
	private final SystemMessageId _smId;
	
	private LootRule(SystemMessageId smId)
	{
		_smId = smId;
	}
	
	public SystemMessageId getMessageId()
	{
		return _smId;
	}
	
	public static final LootRule[] VALUES = values();
}