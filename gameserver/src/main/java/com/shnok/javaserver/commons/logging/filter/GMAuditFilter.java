package com.shnok.javaserver.commons.logging.filter;

import java.util.logging.Filter;
import java.util.logging.LogRecord;

public class GMAuditFilter implements Filter
{
	@Override
	public boolean isLoggable(LogRecord logRecord)
	{
		return logRecord.getLoggerName().equalsIgnoreCase("gmaudit");
	}
}