package com.shnok.javaserver.gameserver.enums;

public enum PrivilegeType
{
	NONE(0),
	
	// System Privileges
	SP_INVITE(2),
	SP_MANAGE_TITLES(4),
	SP_WAREHOUSE_SEARCH(8),
	SP_MANAGE_RANKS(16),
	SP_CLAN_WAR(32),
	SP_DISMISS(64),
	SP_EDIT_CREST(128),
	SP_MASTER_RIGHTS(256),
	SP_MANAGE_LEVELS(512),
	
	// Clan Hall Privileges
	CHP_ENTRY_EXIT_RIGHTS(1024),
	CHP_USE_FUNCTIONS(2048),
	CHP_AUCTION(4096),
	CHP_RIGHT_TO_DISMISS(8192),
	CHP_SET_FUNCTIONS(16384),
	
	// Castle Privileges
	CP_ENTRY_EXIT_RIGHTS(32768),
	CP_MANOR_ADMINISTRATION(65536),
	CP_MANAGE_SIEGE_WAR(131072),
	CP_USE_FUNCTIONS(262144),
	CP_RIGHT_TO_DISMISS(524288),
	CP_MANAGE_TAXES(1048576),
	CP_MERCENARIES(2097152),
	CP_SET_FUNCTIONS(4194304),
	
	ALL(8388606);
	
	private int _mask;
	
	private PrivilegeType(int mask)
	{
		_mask = mask;
	}
	
	public int getMask()
	{
		return _mask;
	}
}