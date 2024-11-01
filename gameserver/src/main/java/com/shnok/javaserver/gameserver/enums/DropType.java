package com.shnok.javaserver.gameserver.enums;

import com.shnok.javaserver.Config;

public enum DropType
{
	SPOIL,
	CURRENCY,
	DROP,
	HERB;
	
	public double getDropRate(boolean isRaid)
	{
		switch (this)
		{
			case SPOIL:
				return Config.RATE_DROP_SPOIL;
			
			case CURRENCY:
				return Config.RATE_DROP_CURRENCY;
			
			case DROP:
				return isRaid ? Config.RATE_DROP_ITEMS_BY_RAID : Config.RATE_DROP_ITEMS;
			
			case HERB:
				return Config.RATE_DROP_HERBS;
			
			default:
				return 0;
		}
	}
}
