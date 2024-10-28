package com.shnok.javaserver.gameserver.model.zone.type;

import com.shnok.javaserver.gameserver.enums.ZoneId;
import com.shnok.javaserver.gameserver.enums.actors.MoveType;
import com.shnok.javaserver.gameserver.model.actor.Creature;
import com.shnok.javaserver.gameserver.model.actor.Npc;
import com.shnok.javaserver.gameserver.model.actor.Player;
import com.shnok.javaserver.gameserver.model.zone.type.subtype.ZoneType;
import com.shnok.javaserver.gameserver.network.serverpackets.AbstractNpcInfo.NpcInfo;
import com.shnok.javaserver.gameserver.network.serverpackets.unused.ServerObjectInfo;

/**
 * A zone extending {@link ZoneType}, used for the water behavior. {@link Player}s can drown if they stay too long below water line.
 */
public class WaterZone extends ZoneType
{
	public WaterZone(int id)
	{
		super(id);
	}
	
	@Override
	protected void onEnter(Creature creature)
	{
		creature.setInsideZone(ZoneId.WATER, true);
		creature.getMove().addMoveType(MoveType.SWIM);
		
		if (creature instanceof Player player)
			player.broadcastUserInfo();
		else if (creature instanceof Npc npc)
		{
			npc.forEachKnownType(Player.class, player ->
			{
				if (npc.getStatus().getMoveSpeed() == 0)
					player.sendPacket(new ServerObjectInfo(npc, player));
				else
					player.sendPacket(new NpcInfo(npc, player));
			});
		}
	}
	
	@Override
	protected void onExit(Creature creature)
	{
		creature.setInsideZone(ZoneId.WATER, false);
		creature.getMove().removeMoveType(MoveType.SWIM);
		
		if (creature instanceof Player player)
			player.broadcastUserInfo();
		else if (creature instanceof Npc npc)
		{
			npc.forEachKnownType(Player.class, player ->
			{
				if (npc.getStatus().getMoveSpeed() == 0)
					player.sendPacket(new ServerObjectInfo(npc, player));
				else
					player.sendPacket(new NpcInfo(npc, player));
			});
		}
	}
	
	public int getWaterZ()
	{
		return getZone().getHighZ();
	}
}