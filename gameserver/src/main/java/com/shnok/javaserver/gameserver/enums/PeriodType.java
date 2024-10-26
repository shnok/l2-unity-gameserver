package com.shnok.javaserver.gameserver.enums;

import com.shnok.javaserver.gameserver.network.SystemMessageId;

public enum PeriodType
{
	RECRUITING("Quest Event Initialization", SystemMessageId.PREPARATIONS_PERIOD_BEGUN),
	COMPETITION("Competition (Quest Event)", SystemMessageId.COMPETITION_PERIOD_BEGUN),
	RESULTS("Quest Event Results", SystemMessageId.RESULTS_PERIOD_BEGUN),
	SEAL_VALIDATION("Seal Validation", SystemMessageId.VALIDATION_PERIOD_BEGUN);
	
	private final String _name;
	private final SystemMessageId _smId;
	
	private PeriodType(String name, SystemMessageId smId)
	{
		_name = name;
		_smId = smId;
	}
	
	public String getName()
	{
		return _name;
	}
	
	public SystemMessageId getMessageId()
	{
		return _smId;
	}
	
	public static final PeriodType[] VALUES = values();
}