package com.shnok.javaserver.gameserver.enums.skills;

public enum EffectFlag
{
	NONE,
	CHARM_OF_COURAGE,
	CHARM_OF_LUCK,
	PHOENIX_BLESSING,
	NOBLESS_BLESSING,
	SILENT_MOVE,
	PROTECTION_BLESSING,
	RELAXING,
	FEAR,
	CONFUSED,
	MUTED,
	PHYSICAL_MUTED,
	ROOTED,
	SLEEP,
	STUNNED,
	BETRAYED,
	MEDITATING,
	PARALYZED;
	
	public int getMask()
	{
		return 1 << ordinal();
	}
}