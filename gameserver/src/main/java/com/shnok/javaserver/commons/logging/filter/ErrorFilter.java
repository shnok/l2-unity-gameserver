package com.shnok.javaserver.commons.logging.filter;

import java.util.logging.Filter;
import java.util.logging.LogRecord;

public class ErrorFilter implements Filter
{
	@Override
	public boolean isLoggable(LogRecord logRecord)
	{
		return logRecord.getThrown() != null;
	}
}