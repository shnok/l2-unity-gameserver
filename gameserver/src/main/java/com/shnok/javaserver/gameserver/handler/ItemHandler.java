package com.shnok.javaserver.gameserver.handler;

import net.sf.l2j.gameserver.model.item.kind.EtcItem;

public class ItemHandler extends AbstractHandler<Integer, IItemHandler>
{
	protected ItemHandler()
	{
		super(IItemHandler.class, "itemhandlers");
	}
	
	@Override
	protected void registerHandler(IItemHandler handler)
	{
		_entries.put(handler.getClass().getSimpleName().intern().hashCode(), handler);
	}
	
	@Override
	public IItemHandler getHandler(Object key)
	{
		if (!(key instanceof EtcItem etcItem) || etcItem.getHandlerName() == null)
			return null;
		
		return super.getHandler(etcItem.getHandlerName().hashCode());
	}
	
	public static ItemHandler getInstance()
	{
		return SingletonHolder.INSTANCE;
	}
	
	private static class SingletonHolder
	{
		protected static final ItemHandler INSTANCE = new ItemHandler();
	}
}