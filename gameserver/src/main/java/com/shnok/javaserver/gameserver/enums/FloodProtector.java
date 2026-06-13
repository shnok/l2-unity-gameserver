package com.shnok.javaserver.gameserver.enums;

import com.shnok.javaserver.Config;

public enum FloodProtector
{
	ROLL_DICE(0, Config.ROLL_DICE_TIME),
	HERO_VOICE(1, Config.HERO_VOICE_TIME),
	SUBCLASS(2, Config.SUBCLASS_TIME),
	DROP_ITEM(3, Config.DROP_ITEM_TIME),
	SERVER_BYPASS(4, Config.SERVER_BYPASS_TIME),
	MULTISELL(5, Config.MULTISELL_TIME),
	MANUFACTURE(6, Config.MANUFACTURE_TIME),
	MANOR(7, Config.MANOR_TIME),
	SENDMAIL(8, Config.SENDMAIL_TIME),
	CHARACTER_SELECT(9, Config.CHARACTER_SELECT_TIME),
	GLOBAL_CHAT(10, Config.GLOBAL_CHAT_TIME),
	TRADE_CHAT(11, Config.TRADE_CHAT_TIME),
	SOCIAL(12, Config.SOCIAL_TIME);
	
	private final int _id;
	private final int _reuseDelay;
	
	private FloodProtector(int id, int reuseDelay)
	{
		_id = id;
		_reuseDelay = reuseDelay;
	}
	
	public int getId()
	{
		return _id;
	}
	
	public int getReuseDelay()
	{
		return _reuseDelay;
	}
	
	public static final int VALUES_LENGTH = FloodProtector.values().length;
}