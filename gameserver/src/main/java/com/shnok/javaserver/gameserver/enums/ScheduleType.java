package com.shnok.javaserver.gameserver.enums;

import java.util.Calendar;

/**
 * Period of the script.
 */
public enum ScheduleType
{
	HOURLY(Calendar.HOUR),
	DAILY(Calendar.DAY_OF_YEAR),
	WEEKLY(Calendar.WEEK_OF_YEAR),
	MONTHLY_DAY(Calendar.MONTH),
	MONTHLY_WEEK(Calendar.MONTH),
	YEARLY_DAY(Calendar.YEAR),
	YEARLY_WEEK(Calendar.YEAR);
	
	private final int _period;
	
	private ScheduleType(int period)
	{
		_period = period;
	}
	
	public final int getPeriod()
	{
		return _period;
	}
}