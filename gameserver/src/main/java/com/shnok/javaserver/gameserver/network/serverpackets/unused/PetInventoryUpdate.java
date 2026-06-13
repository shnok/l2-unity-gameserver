package com.shnok.javaserver.gameserver.network.serverpackets.unused;

import com.shnok.javaserver.gameserver.model.actor.Playable;
import com.shnok.javaserver.gameserver.network.serverpackets.item.AbstractInventoryUpdate;

public class PetInventoryUpdate extends AbstractInventoryUpdate
{
	public PetInventoryUpdate(Playable playable)
	{
		super(playable);
	}
}