package com.shnok.javaserver.gameserver.model.zone.type;

import com.shnok.javaserver.gameserver.enums.RestartType;
import com.shnok.javaserver.gameserver.enums.ZoneId;
import com.shnok.javaserver.gameserver.model.actor.Creature;
import com.shnok.javaserver.gameserver.model.actor.Player;
import com.shnok.javaserver.gameserver.model.actor.Summon;
import com.shnok.javaserver.gameserver.model.olympiad.OlympiadGameTask;
import com.shnok.javaserver.gameserver.model.zone.type.subtype.SpawnZoneType;
import com.shnok.javaserver.gameserver.network.SystemMessageId;
import com.shnok.javaserver.gameserver.network.serverpackets.ExOlympiadMatchEnd;
import com.shnok.javaserver.gameserver.network.serverpackets.ExOlympiadUserInfo;
import com.shnok.javaserver.gameserver.network.serverpackets.L2GameServerPacket;

/**
 * A zone extending {@link SpawnZoneType}, used for olympiad event.<br>
 * <br>
 * Restart and the use of "summoning friend" skill aren't allowed. The zone is considered a pvp zone.
 */
public class OlympiadStadiumZone extends SpawnZoneType
{
	OlympiadGameTask _task = null;
	
	public OlympiadStadiumZone(int id)
	{
		super(id);
	}
	
	@Override
	protected final void onEnter(Creature creature)
	{
		creature.setInsideZone(ZoneId.NO_SUMMON_FRIEND, true);
		creature.setInsideZone(ZoneId.NO_RESTART, true);
		
		if (_task != null && _task.isBattleStarted())
		{
			creature.setInsideZone(ZoneId.PVP, true);
			
			if (creature instanceof Player player)
			{
				player.sendPacket(SystemMessageId.ENTERED_COMBAT_ZONE);
				
				_task.getGame().sendOlympiadInfo(player);
			}
		}
		
		// Only participants, observers and GMs are allowed.
		final Player player = creature.getActingPlayer();
		if (player != null && !player.isGM() && !player.isInOlympiadMode() && !player.isInObserverMode())
		{
			final Summon summon = player.getSummon();
			if (summon != null)
				summon.unSummon(player);
			
			player.teleportTo(RestartType.TOWN);
		}
	}
	
	@Override
	protected final void onExit(Creature creature)
	{
		creature.setInsideZone(ZoneId.NO_SUMMON_FRIEND, false);
		creature.setInsideZone(ZoneId.NO_RESTART, false);
		
		if (_task != null && _task.isBattleStarted())
		{
			creature.setInsideZone(ZoneId.PVP, false);
			
			if (creature instanceof Player player)
			{
				player.sendPacket(SystemMessageId.LEFT_COMBAT_ZONE);
				player.sendPacket(ExOlympiadMatchEnd.STATIC_PACKET);
			}
		}
	}
	
	public final void updateZoneStatus()
	{
		if (_task == null)
			return;
		
		for (Creature creature : _creatures)
		{
			if (_task.isBattleStarted())
			{
				creature.setInsideZone(ZoneId.PVP, true);
				if (creature instanceof Player player)
					player.sendPacket(SystemMessageId.ENTERED_COMBAT_ZONE);
			}
			else
			{
				creature.setInsideZone(ZoneId.PVP, false);
				if (creature instanceof Player player)
				{
					player.sendPacket(SystemMessageId.LEFT_COMBAT_ZONE);
					player.sendPacket(ExOlympiadMatchEnd.STATIC_PACKET);
				}
			}
		}
	}
	
	public final void registerTask(OlympiadGameTask task)
	{
		_task = task;
	}
	
	public final void broadcastStatusUpdate(Player player)
	{
		final ExOlympiadUserInfo packet = new ExOlympiadUserInfo(player);
		for (Player plyr : getKnownTypeInside(Player.class))
		{
			if (plyr.isInObserverMode() || plyr.getOlympiadSide() != player.getOlympiadSide())
				plyr.sendPacket(packet);
		}
	}
	
	public final void broadcastPacketToObservers(L2GameServerPacket packet)
	{
		for (Player player : getKnownTypeInside(Player.class))
		{
			if (player.isInObserverMode())
				player.sendPacket(packet);
		}
	}
}