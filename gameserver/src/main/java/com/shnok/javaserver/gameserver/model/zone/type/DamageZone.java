package com.shnok.javaserver.gameserver.model.zone.type;

import java.util.concurrent.Future;

import com.shnok.javaserver.commons.pool.ThreadPool;

import com.shnok.javaserver.gameserver.enums.SiegeSide;
import com.shnok.javaserver.gameserver.enums.ZoneId;
import com.shnok.javaserver.gameserver.enums.skills.Stats;
import com.shnok.javaserver.gameserver.model.actor.Creature;
import com.shnok.javaserver.gameserver.model.actor.Playable;
import com.shnok.javaserver.gameserver.model.actor.Player;
import com.shnok.javaserver.gameserver.model.zone.type.subtype.CastleZoneType;
import com.shnok.javaserver.gameserver.network.SystemMessageId;
import com.shnok.javaserver.gameserver.network.serverpackets.unused.EtcStatusUpdate;

/**
 * A zone extending {@link CastleZoneType}, which fires a task on the first {@link Creature} entrance, notably used by castle damage traps.<br>
 * <br>
 * This task decreases HPs using a reuse delay and can affect specific class types. The zone is considered a danger zone.
 */
public class DamageZone extends CastleZoneType
{
	private volatile Future<?> _task;
	
	private int _hpDamage = 200;
	private int _initialDelay = 1000;
	private int _reuseDelay = 5000;
	
	public DamageZone(int id)
	{
		super(id);
	}
	
	@Override
	public void setParameter(String name, String value)
	{
		if (name.equals("hpDamage"))
			_hpDamage = Integer.parseInt(value);
		else if (name.equalsIgnoreCase("initialDelay"))
			_initialDelay = Integer.parseInt(value);
		else if (name.equalsIgnoreCase("reuseDelay"))
			_reuseDelay = Integer.parseInt(value);
		else
			super.setParameter(name, value);
	}
	
	@Override
	protected boolean isAffected(Creature creature)
	{
		return creature instanceof Playable;
	}
	
	@Override
	protected void onEnter(Creature creature)
	{
		if (_hpDamage > 0)
		{
			// Castle traps are active only during siege, or if they're activated.
			if (getCastle() != null && (!isEnabled() || !getCastle().getSiege().isInProgress()))
				return;
			
			Future<?> task = _task;
			if (task == null)
			{
				synchronized (this)
				{
					task = _task;
					if (task == null)
						_task = task = ThreadPool.scheduleAtFixedRate(() ->
						{
							if (_creatures.isEmpty() || _hpDamage <= 0 || (getCastle() != null && (!isEnabled() || !getCastle().getSiege().isInProgress())))
							{
								stopTask();
								return;
							}
							
							// Effect all people inside the zone.
							for (Creature temp : _creatures)
							{
								if (!temp.isDead())
									temp.reduceCurrentHp(_hpDamage * (1 + (temp.getStatus().calcStat(Stats.DAMAGE_ZONE_VULN, 0, null, null) / 100)), null, null);
							}
						}, _initialDelay, _reuseDelay);
					
					// Message for castle traps.
					if (getCastle() != null)
						getCastle().getSiege().announce(SystemMessageId.A_TRAP_DEVICE_HAS_BEEN_TRIPPED, SiegeSide.DEFENDER);
				}
			}
		}
		
		if (creature instanceof Player player)
		{
			player.setInsideZone(ZoneId.DANGER_AREA, true);
			player.sendPacket(new EtcStatusUpdate(player));
		}
	}
	
	@Override
	protected void onExit(Creature creature)
	{
		if (creature instanceof Player player)
		{
			player.setInsideZone(ZoneId.DANGER_AREA, false);
			
			if (!player.isInsideZone(ZoneId.DANGER_AREA))
				player.sendPacket(new EtcStatusUpdate(player));
		}
	}
	
	private void stopTask()
	{
		if (_task != null)
		{
			_task.cancel(false);
			_task = null;
		}
	}
}