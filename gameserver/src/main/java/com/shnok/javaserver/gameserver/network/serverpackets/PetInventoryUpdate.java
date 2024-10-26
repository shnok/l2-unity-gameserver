package com.shnok.javaserver.gameserver.network.serverpackets;

import com.shnok.javaserver.gameserver.model.actor.Playable;

public class PetInventoryUpdate extends AbstractInventoryUpdate
{
	public PetInventoryUpdate(Playable playable)
	{
		super(playable);
	}
}