package com.shnok.javaserver.commons.logging.formatter;

import java.util.logging.LogRecord;

import com.shnok.javaserver.commons.logging.MasterFormatter;

public class GMAuditFormatter extends MasterFormatter
{
	@Override
	public String format(LogRecord logRecord)
	{
		return "[" + getFormatedDate(logRecord.getMillis()) + "]" + SPACE + logRecord.getMessage() + CRLF;
	}
}