package com.shnok.javaserver.commons.logging.formatter;

import java.io.PrintWriter;
import java.io.StringWriter;
import java.util.logging.LogRecord;

import com.shnok.javaserver.commons.logging.MasterFormatter;

public class ConsoleLogFormatter extends MasterFormatter
{
	@Override
	public String format(LogRecord logRecord)
	{
		final StringWriter sw = new StringWriter();
		sw.append(logRecord.getMessage());
		sw.append(CRLF);
		
		final Throwable throwable = logRecord.getThrown();
		if (throwable != null)
			throwable.printStackTrace(new PrintWriter(sw));
		
		return sw.toString();
	}
}