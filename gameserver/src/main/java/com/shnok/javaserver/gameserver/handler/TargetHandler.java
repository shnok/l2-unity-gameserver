package com.shnok.javaserver.gameserver.handler;

import net.sf.l2j.gameserver.enums.skills.SkillTargetType;

public class TargetHandler extends AbstractHandler<SkillTargetType, ITargetHandler>
{
	protected TargetHandler()
	{
		super(ITargetHandler.class, "targethandlers");
	}
	
	@Override
	protected void registerHandler(ITargetHandler handler)
	{
		_entries.put(handler.getTargetType(), handler);
	}
	
	public static TargetHandler getInstance()
	{
		return SingletonHolder.INSTANCE;
	}
	
	private static class SingletonHolder
	{
		protected static final TargetHandler INSTANCE = new TargetHandler();
	}
}