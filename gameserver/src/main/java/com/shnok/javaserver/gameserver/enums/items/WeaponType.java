package com.shnok.javaserver.gameserver.enums.items;

import com.shnok.javaserver.gameserver.enums.skills.Stats;

public enum WeaponType implements ItemType
{
	NONE(40, null),
	SWORD(40, Stats.SWORD_WPN_VULN),
	BLUNT(40, Stats.BLUNT_WPN_VULN),
	DAGGER(40, Stats.DAGGER_WPN_VULN),
	BOW(500, Stats.BOW_WPN_VULN),
	POLE(66, Stats.POLE_WPN_VULN),
	ETC(40, null),
	FIST(40, null),
	DUAL(40, Stats.DUAL_WPN_VULN),
	DUALFIST(40, Stats.DUALFIST_WPN_VULN),
	BIGSWORD(40, Stats.BIGSWORD_WPN_VULN),
	FISHINGROD(40, null),
	BIGBLUNT(40, Stats.BIGBLUNT_WPN_VULN),
	PET(40, null);
	
	public static final WeaponType[] VALUES = values();
	
	private final int _mask;
	
	private final int _range;
	private final Stats _vulnStat;
	
	private WeaponType(int range, Stats stat)
	{
		_mask = 1 << ordinal();
		
		_range = range;
		_vulnStat = stat;
	}
	
	@Override
	public int mask()
	{
		return _mask;
	}
	
	public int getRange()
	{
		return _range;
	}
	
	public Stats getVulnStat()
	{
		return _vulnStat;
	}
}