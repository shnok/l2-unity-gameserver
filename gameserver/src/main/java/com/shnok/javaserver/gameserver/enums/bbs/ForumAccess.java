package com.shnok.javaserver.gameserver.enums.bbs;

public enum ForumAccess
{
	NONE("No access"),
	READ("Read access"),
	WRITE("Write access"),
	ALL("All access");
	
	private final String _desc;
	
	private ForumAccess(String desc)
	{
		_desc = desc;
	}
	
	public String getDesc()
	{
		return _desc;
	}
}