package com.shnok.javaserver.gameserver.network.serverpackets;

import net.sf.l2j.gameserver.model.actor.Playable;

public class InventoryUpdate extends AbstractInventoryUpdate
{
	public InventoryUpdate(Playable playable)
	{
		super(playable);
	}
}