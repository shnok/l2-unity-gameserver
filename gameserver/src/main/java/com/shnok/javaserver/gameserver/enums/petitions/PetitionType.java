package com.shnok.javaserver.gameserver.enums.petitions;

public enum PetitionType
{
	NONE,
	IMMOBILITY,
	RECOVERY_RELATED,
	BUG_REPORT,
	QUEST_RELATED,
	BAD_USER,
	SUGGESTIONS,
	GAME_TIP,
	OPERATION_RELATED,
	OTHER;
	
	public static final PetitionType[] VALUES = values();
}