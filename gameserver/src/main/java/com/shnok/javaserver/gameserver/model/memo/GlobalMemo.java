package com.shnok.javaserver.gameserver.model.memo;

import java.util.Map;

import com.shnok.javaserver.commons.data.MemoSet;

import com.shnok.javaserver.gameserver.model.World;
import com.shnok.javaserver.gameserver.model.WorldObject;
import com.shnok.javaserver.gameserver.model.actor.Creature;
import com.shnok.javaserver.gameserver.model.actor.Npc;

/**
 * An implementation of {@link MemoSet} used for storing global data.
 */
public class GlobalMemo extends MemoSet
{
	private static final long serialVersionUID = 1L;
	
	public static final GlobalMemo DUMMY_SET = new GlobalMemo();
	
	public GlobalMemo()
	{
		super();
	}
	
	public GlobalMemo(final int size)
	{
		super(size);
	}
	
	public GlobalMemo(final Map<String, String> m)
	{
		super(m);
	}
	
	@Override
	protected void onSet(String key, String value)
	{
		// Do nothing.
	}
	
	@Override
	protected void onUnset(String key)
	{
		// Do nothing.
	}
	
	public static final GlobalMemo getInstance()
	{
		return SingletonHolder.INSTANCE;
	}
	
	public final Creature getCreature(String str)
	{
		final int id = getInteger(str, 0);
		if (id == 0)
			return null;
		
		final WorldObject object = World.getInstance().getObject(id);
		if (object == null || (object instanceof Npc npc && npc.isDecayed()))
			return null;
		
		return (object instanceof Creature creature) ? creature : null;
	}
	
	private static class SingletonHolder
	{
		protected static final GlobalMemo INSTANCE = new GlobalMemo();
	}
}