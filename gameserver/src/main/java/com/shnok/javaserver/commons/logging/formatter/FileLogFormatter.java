package com.shnok.javaserver.commons.logging.formatter;

import java.util.logging.LogRecord;

import com.shnok.javaserver.commons.logging.MasterFormatter;

public class FileLogFormatter extends MasterFormatter
{
	@Override
	public String format(LogRecord logRecord)
	{
		return "[" + getFormatedDate(logRecord.getMillis()) + "]" + SPACE + logRecord.getLevel().getName() + SPACE + logRecord.getMessage() + CRLF;
	}
}