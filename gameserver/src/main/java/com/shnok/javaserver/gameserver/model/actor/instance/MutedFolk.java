package com.shnok.javaserver.gameserver.model.actor.instance;

import net.sf.l2j.gameserver.model.actor.Player;
import net.sf.l2j.gameserver.model.actor.template.NpcTemplate;

/**
 * An instance similar to {@link Folk}, except they can't be interacted by {@link Player}s.
 */
public final class MutedFolk extends Folk
{
	public MutedFolk(int objectId, NpcTemplate template)
	{
		super(objectId, template);
	}
	
	@Override
	public void onInteract(Player player)
	{
		// Do nothing.
	}
}