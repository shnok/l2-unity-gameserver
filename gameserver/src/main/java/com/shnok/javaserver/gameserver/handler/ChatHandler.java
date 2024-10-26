package com.shnok.javaserver.gameserver.handler;

import net.sf.l2j.gameserver.enums.SayType;

public class ChatHandler extends AbstractHandler<SayType, IChatHandler>
{
	protected ChatHandler()
	{
		super(IChatHandler.class, "chathandlers");
	}
	
	@Override
	protected void registerHandler(IChatHandler handler)
	{
		for (SayType type : handler.getChatTypeList())
			_entries.put(type, handler);
	}
	
	public static ChatHandler getInstance()
	{
		return SingletonHolder.INSTANCE;
	}
	
	private static class SingletonHolder
	{
		protected static final ChatHandler INSTANCE = new ChatHandler();
	}
}