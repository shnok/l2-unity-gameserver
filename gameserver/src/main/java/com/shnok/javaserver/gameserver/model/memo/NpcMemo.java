package com.shnok.javaserver.gameserver.model.memo;

import java.util.Map;

import com.shnok.javaserver.commons.data.MemoSet;

import com.shnok.javaserver.gameserver.model.World;
import com.shnok.javaserver.gameserver.model.WorldObject;
import com.shnok.javaserver.gameserver.model.actor.Creature;
import com.shnok.javaserver.gameserver.model.actor.Npc;

/**
 * An implementation of {@link MemoSet} used for Npc.
 */
public class NpcMemo extends MemoSet
{
	public static final NpcMemo DUMMY_SET = new NpcMemo();
	
	private static final long serialVersionUID = 1L;
	
	public NpcMemo()
	{
		super();
	}
	
	public NpcMemo(final int size)
	{
		super(size);
	}
	
	public NpcMemo(final Map<String, String> m)
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
	
	/**
	 * @param str : The {@link String} used as parameter.
	 * @return The {@link Creature} linked to the objectId passed as a {@link String} parameter, or null if not found.
	 */
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
}