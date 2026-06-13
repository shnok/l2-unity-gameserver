package com.shnok.javaserver.commons.logging.formatter;

import java.util.logging.LogRecord;

import com.shnok.javaserver.commons.lang.StringUtil;
import com.shnok.javaserver.commons.logging.MasterFormatter;

import com.shnok.javaserver.gameserver.model.item.instance.ItemInstance;

public class ItemLogFormatter extends MasterFormatter
{
	@Override
	public String format(LogRecord logRecord)
	{
		final StringBuilder sb = new StringBuilder();
		
		StringUtil.append(sb, "[", getFormatedDate(logRecord.getMillis()), "] ", SPACE, logRecord.getMessage());
		
		for (Object p : logRecord.getParameters())
		{
			if (p == null)
				continue;
			
			if (p instanceof ItemInstance item)
				StringUtil.append(sb, SPACE, item.getLocation(), SPACE, item.getCount(), ((item.getEnchantLevel() > 0) ? " +" + item.getEnchantLevel() + " " : " "), p.toString());
			else
				StringUtil.append(sb, SPACE, p.toString());
		}
		sb.append(CRLF);
		
		return sb.toString();
	}
}