package com.shnok.javaserver.gameserver.network.clientpackets.unused;

import com.shnok.javaserver.gameserver.model.World;
import com.shnok.javaserver.gameserver.model.WorldObject;
import com.shnok.javaserver.gameserver.model.actor.Player;
import com.shnok.javaserver.gameserver.model.actor.instance.Pet;
import com.shnok.javaserver.gameserver.network.clientpackets.L2GameClientPacket;
import com.shnok.javaserver.gameserver.network.serverpackets.combat.ActionFailed;

public final class RequestPetGetItem extends L2GameClientPacket
{
	private int _objectId;
	
	@Override
	protected void readImpl()
	{
		_objectId = readD();
	}
	
	@Override
	protected void runImpl()
	{
		final Player player = getClient().getPlayer();
		if (player == null || !player.hasPet())
			return;
		
		final WorldObject item = World.getInstance().getObject(_objectId);
		if (item == null)
			return;
		
		final Pet pet = (Pet) player.getSummon();
		if (pet.isDead() || pet.isOutOfControl())
		{
			sendPacket(ActionFailed.STATIC_PACKET);
			return;
		}
		
		pet.getAI().tryToPickUp(_objectId, false);
	}
}