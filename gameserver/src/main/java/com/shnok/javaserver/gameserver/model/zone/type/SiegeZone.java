package com.shnok.javaserver.gameserver.model.zone.type;

import com.shnok.javaserver.Config;
import com.shnok.javaserver.gameserver.enums.RestartType;
import com.shnok.javaserver.gameserver.enums.ZoneId;
import com.shnok.javaserver.gameserver.model.actor.Creature;
import com.shnok.javaserver.gameserver.model.actor.Player;
import com.shnok.javaserver.gameserver.model.actor.instance.SiegeSummon;
import com.shnok.javaserver.gameserver.model.zone.type.subtype.SpawnZoneType;
import com.shnok.javaserver.gameserver.model.zone.type.subtype.ZoneType;
import com.shnok.javaserver.gameserver.network.SystemMessageId;
import com.shnok.javaserver.gameserver.taskmanager.PvpFlagTaskManager;

/**
 * A zone extending {@link SpawnZoneType}, used for castle on siege progress, and which handles following spawns type :
 * <ul>
 * <li>Generic spawn locs : other_restart_village_list (spawns used on siege, to respawn on second closest town.</li>
 * <li>Chaotic spawn locs : chao_restart_point_list (spawns used on siege, to respawn PKs on second closest town.</li>
 * </ul>
 */
public class SiegeZone extends ZoneType
{
	private int _siegableId = -1;
	private boolean _isActiveSiege = false;
	
	public SiegeZone(int id)
	{
		super(id);
	}
	
	@Override
	public void setParameter(String name, String value)
	{
		if (name.equals("castleId") || name.equals("clanHallId"))
			_siegableId = Integer.parseInt(value);
		else
			super.setParameter(name, value);
	}
	
	@Override
	protected void onEnter(Creature creature)
	{
		if (_isActiveSiege)
		{
			creature.setInsideZone(ZoneId.PVP, true);
			creature.setInsideZone(ZoneId.SIEGE, true);
			creature.setInsideZone(ZoneId.NO_SUMMON_FRIEND, true);
			
			if (creature instanceof Player player)
			{
				player.sendPacket(SystemMessageId.ENTERED_COMBAT_ZONE);
				player.enterOnNoLandingZone();
			}
		}
	}
	
	@Override
	protected void onExit(Creature creature)
	{
		creature.setInsideZone(ZoneId.PVP, false);
		creature.setInsideZone(ZoneId.SIEGE, false);
		creature.setInsideZone(ZoneId.NO_SUMMON_FRIEND, false);
		
		if (creature instanceof Player player)
		{
			if (_isActiveSiege)
			{
				player.sendPacket(SystemMessageId.LEFT_COMBAT_ZONE);
				player.exitOnNoLandingZone();
				
				PvpFlagTaskManager.getInstance().add(player, Config.PVP_NORMAL_TIME);
				
				// Set pvp flag
				if (player.getPvpFlag() == 0)
					player.updatePvPFlag(1);
			}
		}
		else if (creature instanceof SiegeSummon siegeSummon)
			siegeSummon.unSummon(siegeSummon.getOwner());
	}
	
	public int getSiegableId()
	{
		return _siegableId;
	}
	
	public boolean isActive()
	{
		return _isActiveSiege;
	}
	
	public void setActive(boolean val)
	{
		_isActiveSiege = val;
		
		if (_isActiveSiege)
		{
			for (Creature creature : _creatures)
				onEnter(creature);
		}
		else
		{
			for (Creature creature : _creatures)
			{
				creature.setInsideZone(ZoneId.PVP, false);
				creature.setInsideZone(ZoneId.SIEGE, false);
				creature.setInsideZone(ZoneId.NO_SUMMON_FRIEND, false);
				
				if (creature instanceof Player player)
				{
					player.sendPacket(SystemMessageId.LEFT_COMBAT_ZONE);
					player.exitOnNoLandingZone();
				}
				else if (creature instanceof SiegeSummon siegeSummon)
					siegeSummon.unSummon(siegeSummon.getOwner());
			}
		}
	}
	
	/**
	 * Kick {@link Player}s who don't belong to the clan set as parameter from this zone. They are ported to chaotic or regular spawn locations depending of their karma.
	 * @param clanId : The castle owner id. Related players aren't teleported out.
	 */
	public void banishForeigners(int clanId)
	{
		for (Player player : getKnownTypeInside(Player.class))
		{
			if (player.getClanId() == clanId)
				continue;
			
			player.teleportTo(RestartType.TOWN);
		}
	}
}