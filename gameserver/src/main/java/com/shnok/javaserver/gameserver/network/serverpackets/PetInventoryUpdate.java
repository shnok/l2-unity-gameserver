package com.shnok.javaserver.gameserver.network.serverpackets;

import net.sf.l2j.gameserver.model.actor.Playable;

public class PetInventoryUpdate extends AbstractInventoryUpdate
{
	public PetInventoryUpdate(Playable playable)
	{
		super(playable);
	}
}