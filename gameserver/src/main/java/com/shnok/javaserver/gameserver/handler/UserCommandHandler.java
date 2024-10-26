package com.shnok.javaserver.gameserver.handler;

public class UserCommandHandler extends AbstractHandler<Integer, IUserCommandHandler>
{
	protected UserCommandHandler()
	{
		super(IUserCommandHandler.class, "usercommandhandlers");
	}
	
	@Override
	protected void registerHandler(IUserCommandHandler handler)
	{
		for (int id : handler.getUserCommandList())
			_entries.put(id, handler);
	}
	
	public static UserCommandHandler getInstance()
	{
		return SingletonHolder.INSTANCE;
	}
	
	private static class SingletonHolder
	{
		protected static final UserCommandHandler INSTANCE = new UserCommandHandler();
	}
}