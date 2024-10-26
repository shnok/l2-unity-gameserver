package com.shnok.javaserver.commons.logging.filter;

import java.util.logging.Filter;
import java.util.logging.LogRecord;

public class ChatFilter implements Filter
{
	@Override
	public boolean isLoggable(LogRecord logRecord)
	{
		return logRecord.getLoggerName().equals("chat");
	}
}