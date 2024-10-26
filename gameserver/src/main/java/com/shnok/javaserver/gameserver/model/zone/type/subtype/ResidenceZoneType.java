package com.shnok.javaserver.gameserver.model.zone.type.subtype;

import com.shnok.javaserver.gameserver.model.actor.Player;

/**
 * A zone extending {@link SpawnZoneType} which handles following spawns type :
 * <ul>
 * <li>Generic spawn locs : owner_restart_point_list (spawns used on siege, to respawn on mass gatekeeper room.</li>
 * <li>Chaotic spawn locs : banish_point_list (spawns used to banish players on regular owner maintenance).</li>
 * </ul>
 */
public abstract class ResidenceZoneType extends ZoneType
{
	private int _residenceId;
	
	protected ResidenceZoneType(int id)
	{
		super(id);
	}
	
	/**
	 * Kick {@link Player}s who don't belong to the clan set as parameter from this zone. They are ported to a random "chaotic" location.
	 * @param clanId : The castle owner clanId. Related players aren't teleported out.
	 */
	public abstract void banishForeigners(int clanId);
	
	public int getResidenceId()
	{
		return _residenceId;
	}
	
	protected void setResidenceId(int residenceId)
	{
		_residenceId = residenceId;
	}
}