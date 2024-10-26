package com.shnok.javaserver.gameserver.enums;

/**
 * This enum contains L2OFF teleport types from AI.obj. See individual comment on each type.
 */
public enum TeleportType
{
	/**
	 * AI.obj equivalent: "" (empty string)<br>
	 * This is the main "standard" teleport used by common GK.
	 */
	STANDARD,
	
	/**
	 * AI.obj equivalent: "NewbieTokenTeleports"<br>
	 * Reference item: 8542
	 */
	NEWBIE_TOKEN,
	
	/**
	 * AI.obj equivalent: "NoblessNeedItemField"<br>
	 * Reference item: 6651
	 */
	NOBLE_HUNTING_ZONE_PASS,
	
	/**
	 * AI.obj equivalent: "NoblessNoItemField"<br>
	 * Reference item: 57
	 */
	NOBLE_HUNTING_ZONE_ADENA,
	
	/**
	 * AI.obj equivalent: "ForFriend"<br>
	 * VARKA/KETRA Alliance related content.
	 */
	ALLY,
	
	/**
	 * AI.obj equivalent: "1"<br>
	 * Used by Clan Hall Functions.
	 */
	CHF_LEVEL_1,
	
	/**
	 * AI.obj equivalent: "2"<br>
	 * Used by Clan Hall Functions.
	 */
	CHF_LEVEL_2;
}