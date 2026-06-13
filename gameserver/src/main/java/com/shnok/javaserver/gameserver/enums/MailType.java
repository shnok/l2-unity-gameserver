package com.shnok.javaserver.gameserver.enums;

public enum MailType
{
	INBOX("Inbox", "<a action=\"bypass _bbsmail\">Inbox</a>"),
	SENTBOX("Sent Box", "<a action=\"bypass _bbsmail;sentbox\">Sent Box</a>"),
	ARCHIVE("Mail Archive", "<a action=\"bypass _bbsmail;archive\">Mail Archive</a>"),
	TEMPARCHIVE("Temporary Mail Archive", "<a action=\"bypass _bbsmail;temp_archive\">Temporary Mail Archive</a>");
	
	private final String _description;
	private final String _bypass;
	
	private MailType(String description, String bypass)
	{
		_description = description;
		_bypass = bypass;
	}
	
	public String getDescription()
	{
		return _description;
	}
	
	public String getBypass()
	{
		return _bypass;
	}
	
	public static final MailType[] VALUES = values();
}