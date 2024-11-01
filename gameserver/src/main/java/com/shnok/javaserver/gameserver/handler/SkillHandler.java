package com.shnok.javaserver.gameserver.handler;

import com.shnok.javaserver.gameserver.enums.skills.SkillType;

public class SkillHandler extends AbstractHandler<SkillType, ISkillHandler>
{
	protected SkillHandler()
	{
		super(ISkillHandler.class, "skillhandlers");
	}
	
	@Override
	protected void registerHandler(ISkillHandler handler)
	{
		for (SkillType st : handler.getSkillIds())
			_entries.put(st, handler);
	}
	
	@Override
	public ISkillHandler getHandler(Object key)
	{
		if (!(key instanceof SkillType st))
			return null;
		
		return super.getHandler(st);
	}
	
	public static SkillHandler getInstance()
	{
		return SingletonHolder.INSTANCE;
	}
	
	private static class SingletonHolder
	{
		protected static final SkillHandler INSTANCE = new SkillHandler();
	}
}