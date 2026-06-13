package com.shnok.javaserver.commons.logging.formatter;

import java.util.logging.LogRecord;

import com.shnok.javaserver.commons.lang.StringUtil;
import com.shnok.javaserver.commons.logging.MasterFormatter;

public class ChatLogFormatter extends MasterFormatter
{
	@Override
	public String format(LogRecord logRecord)
	{
		final StringBuilder sb = new StringBuilder();
		
		StringUtil.append(sb, "[", getFormatedDate(logRecord.getMillis()), "] ");
		
		for (Object p : logRecord.getParameters())
		{
			if (p == null)
				continue;
			
			StringUtil.append(sb, p, " ");
		}
		
		StringUtil.append(sb, logRecord.getMessage(), CRLF);
		
		return sb.toString();
	}
}