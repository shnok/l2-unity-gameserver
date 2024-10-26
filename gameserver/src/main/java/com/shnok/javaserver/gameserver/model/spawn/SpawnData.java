package com.shnok.javaserver.gameserver.model.spawn;

import java.sql.ResultSet;
import java.sql.SQLException;

import com.shnok.javaserver.gameserver.model.actor.Npc;
import com.shnok.javaserver.gameserver.model.location.SpawnLocation;

/**
 * A class used to save {@link Npc}.<br>
 * Note: The {@code name} is limited to 80 characters in database.<br>
 * Note: The {@code status} uses following values:<br>
 * <ul>
 * <li>< 0: Not initialized yet (missing HP, MP and position data).</li>
 * <li>0: NPC is dead.</li>
 * <li>1: NPC is alive (eventually in some basic state).</li>
 * <li>> 1: NPC is alive and in some advanced state.</li>
 * </ul>
 */
public class SpawnData extends SpawnLocation
{
	private final String _name;
	
	private byte _status;
	
	private int _currentHp;
	private int _currentMp;
	
	private int _dbValue;
	
	private long _respawnTime;
	
	/**
	 * New instance of {@link SpawnData}, which is not in database yet.
	 * @param name : The name of the NPC data entry.
	 */
	public SpawnData(String name)
	{
		super(0, 0, 0, 0);
		
		_name = name;
		
		_status = -1;
		
		_currentHp = 0;
		_currentMp = 0;
		
		_dbValue = 0;
		
		_respawnTime = 0;
	}
	
	/**
	 * New instance of {@link SpawnData} loaded from database.
	 * @param name : The name of the NPC data entry.
	 * @param rset : The data of the entry.
	 * @throws SQLException Thrown, when failed to read data entry.
	 */
	public SpawnData(String name, ResultSet rset) throws SQLException
	{
		super(rset.getInt("loc_x"), rset.getInt("loc_y"), rset.getInt("loc_z"), rset.getInt("heading"));
		
		_name = name;
		
		_status = rset.getByte("status");
		
		_currentHp = rset.getInt("current_hp");
		_currentMp = rset.getInt("current_mp");
		
		_dbValue = rset.getInt("db_value");
		
		_respawnTime = rset.getLong("respawn_time");
	}
	
	/**
	 * @return The name of the NPC's {@link SpawnData}.
	 */
	public final String getName()
	{
		return _name;
	}
	
	/**
	 * @return the status of the NPC.
	 */
	public final byte getStatus()
	{
		return _status;
	}
	
	/**
	 * @return The current HP of the NPC.
	 */
	public final int getCurrentHp()
	{
		return _currentHp;
	}
	
	/**
	 * @return The current MP of the NPC.
	 */
	public final int getCurrentMp()
	{
		return _currentMp;
	}
	
	/**
	 * @return The db_value of the NPC.
	 */
	public final int getDBValue()
	{
		return _dbValue;
	}
	
	/**
	 * @return The respawn time of the NPC.
	 */
	public final long getRespawnTime()
	{
		return _respawnTime;
	}
	
	/**
	 * @return True, if NPC is dead and respawn timer has not passed yet.
	 */
	public final boolean checkDead()
	{
		return _status == 0 && _respawnTime > 0 && _respawnTime > System.currentTimeMillis();
	}
	
	/**
	 * Check if NPC is still alive, becomes alive or {@link SpawnData} is not initialized yet.<br>
	 * Note: This check can be called only after {@code checkDead()} had been handled already.
	 * @param loc : The new {@link SpawnLocation}.
	 * @param maxHp : The max HP of the NPC.
	 * @param maxMp : The max MP of the NPC.
	 * @return True, if NPC is alive (-> update spawn location, HP and MP). False, when NPC was dead and become alive, or {@link SpawnData} is not initialized yet.
	 */
	public final boolean checkAlive(SpawnLocation loc, double maxHp, double maxMp)
	{
		// NPC was dead, is alive now or not initialized.
		if ((_status == 0 && _respawnTime > 0 && _respawnTime <= System.currentTimeMillis()) || _status < 0)
		{
			_status = 1;
			
			_currentHp = (int) maxHp;
			_currentMp = (int) maxMp;
			
			set(loc);
			
			_respawnTime = 0;
			return false;
		}
		
		// Is still alive.
		return true;
	}
	
	/**
	 * Set the NPC's status accordingly.
	 * @param status : The new status.
	 */
	public final void setStatus(byte status)
	{
		_status = status;
	}
	
	/**
	 * Set the NPC's db value accordingly.
	 * @param value : The new value.
	 */
	public final void setDBValue(int value)
	{
		_dbValue = value;
	}
	
	/**
	 * NPC is alive, update NPC's live data accordingly.
	 * @param npc : The {@link Npc} being checked.
	 */
	public final void setStats(Npc npc)
	{
		// The NPC is dead, skip update.
		if (_status == 0)
			return;
		
		// Update HP, MP and location.
		_currentHp = (int) npc.getStatus().getHp();
		_currentMp = (int) npc.getStatus().getMp();
		set(npc.getPosition());
		
		// Reset respawn time.
		_respawnTime = 0;
	}
	
	/**
	 * NPC has died, update NPC's respawn data accordingly.<br>
	 * @param respawnDelay : The respawn delay in ms.
	 */
	public final void setRespawn(long respawnDelay)
	{
		// Reset status, HP, MP and location.
		_status = 0;
		_currentHp = 0;
		_currentMp = 0;
		set(0, 0, 0, 0);
		
		// Set respawn time.
		_respawnTime = System.currentTimeMillis() + respawnDelay;
	}
	
	/**
	 * NPC is dead, cancel NPC's respawn data.
	 */
	public final void cancelRespawn()
	{
		// Reset respawn time (to non-zero).
		_respawnTime = 1;
	}
}