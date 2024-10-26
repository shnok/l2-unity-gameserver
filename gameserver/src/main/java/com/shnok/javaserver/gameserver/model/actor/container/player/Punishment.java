package com.shnok.javaserver.gameserver.model.actor.container.player;

import java.util.concurrent.ScheduledFuture;
import java.util.concurrent.TimeUnit;

import net.sf.l2j.commons.pool.ThreadPool;

import net.sf.l2j.gameserver.enums.PunishmentType;
import net.sf.l2j.gameserver.enums.ZoneId;
import net.sf.l2j.gameserver.model.actor.Player;
import net.sf.l2j.gameserver.model.olympiad.OlympiadManager;
import net.sf.l2j.gameserver.network.serverpackets.EtcStatusUpdate;
import net.sf.l2j.gameserver.network.serverpackets.NpcHtmlMessage;
import net.sf.l2j.gameserver.network.serverpackets.PlaySound;

public class Punishment
{
	private final Player _owner;
	
	private PunishmentType _type = PunishmentType.NONE;
	private long _timer;
	private ScheduledFuture<?> _task;
	
	public Punishment(Player owner)
	{
		_owner = owner;
	}
	
	public Player getOwner()
	{
		return _owner;
	}
	
	public PunishmentType getType()
	{
		return _type;
	}
	
	/**
	 * Set the {@link PunishmentType} for this {@link Player}. If state is out of range, do nothing.
	 * @param type : The PunishmentType to apply.
	 */
	public void setType(int type)
	{
		if (type < 0 || type > PunishmentType.VALUES.length)
			return;
		
		_type = PunishmentType.VALUES[type];
	}
	
	/**
	 * Sets the {@link PunishmentType} for this {@link Player}, based on a delay.
	 * @param state : The PunishmentType to apply.
	 * @param delayInMinutes : A time in minutes, 0 for infinite.
	 */
	public void setType(PunishmentType state, int delayInMinutes)
	{
		switch (state)
		{
			case NONE: // Remove Punishments
				switch (_type)
				{
					case CHAT:
						_type = state;
						
						stopTask(true);
						
						_owner.sendPacket(new EtcStatusUpdate(_owner));
						_owner.sendMessage("Chatting is now available.");
						_owner.sendPacket(new PlaySound("systemmsg_e.345"));
						break;
					
					case JAIL:
						_type = state;
						
						// Open a Html message to inform the player
						final NpcHtmlMessage html = new NpcHtmlMessage(0);
						html.setFile("data/html/jail_out.htm");
						_owner.sendPacket(html);
						
						stopTask(true);
						_owner.teleportTo(17836, 170178, -3507, 20); // Floran village
						break;
				}
				break;
			
			case CHAT: // Chat ban
				// not allow player to escape jail using chat ban
				if (_type == PunishmentType.JAIL)
					break;
				
				_type = state;
				_timer = 0;
				_owner.sendPacket(new EtcStatusUpdate(_owner));
				
				// Remove the task if any
				stopTask(false);
				
				if (delayInMinutes > 0)
				{
					_timer = delayInMinutes * 60000L;
					
					// start the countdown
					_task = ThreadPool.schedule(() -> setType(PunishmentType.NONE, 0), _timer);
					_owner.sendMessage("Chatting has been suspended for " + delayInMinutes + " minute(s).");
				}
				else
					_owner.sendMessage("Chatting has been suspended.");
				
				// Send same sound packet in both "delay" cases.
				_owner.sendPacket(new PlaySound("systemmsg_e.346"));
				break;
			
			case JAIL: // Jail Player
				_type = state;
				_timer = 0;
				
				// Remove the task if any
				stopTask(false);
				
				if (delayInMinutes > 0)
				{
					_timer = delayInMinutes * 60000L;
					
					// start the countdown
					_task = ThreadPool.schedule(() -> setType(PunishmentType.NONE, 0), _timer);
					_owner.sendMessage("You are jailed for " + delayInMinutes + " minutes.");
				}
				
				if (OlympiadManager.getInstance().isRegisteredInComp(_owner))
					OlympiadManager.getInstance().removeDisconnectedCompetitor(_owner);
				
				// Open a Html message to inform the player
				final NpcHtmlMessage html = new NpcHtmlMessage(0);
				html.setFile("data/html/jail_in.htm");
				_owner.sendPacket(html);
				
				_owner.setIsIn7sDungeon(false);
				_owner.teleportTo(-114356, -249645, -2984, 0); // Jail
				break;
			
			case CHAR: // Ban Character
				_owner.setAccessLevel(-1);
				_owner.logout(false);
				break;
			
			case ACC: // Ban Account
				_owner.setAccountAccesslevel(-100);
				_owner.logout(false);
				break;
			
			default:
				_type = state;
				break;
		}
		
		// store in database
		_owner.storeCharBase();
	}
	
	public long getTimer()
	{
		return _timer;
	}
	
	/**
	 * Set the {@link Punishment} data on {@link Player} load.
	 * @param type : The PunishmentType ordinal to set.
	 * @param timer : The time to set, under ms.
	 */
	public void load(int type, long timer)
	{
		// Set the PunishmentType based on their ordinal.
		setType(type);
		
		// Set the timer, based on PunishmentType.
		_timer = (_type == PunishmentType.NONE) ? 0 : timer;
	}
	
	/**
	 * Handle {@link Punishment} actions.
	 */
	public void handle()
	{
		if (_type != PunishmentType.NONE)
		{
			// If punish timer exists, restart the task.
			if (_timer > 0)
			{
				_task = ThreadPool.schedule(() -> setType(PunishmentType.NONE, 0), _timer);
				_owner.sendMessage("You are still " + _type.getDescription() + " for " + Math.round(_timer / 60000f) + " minutes.");
			}
			
			// If player escaped, put him back in jail.
			if (_type == PunishmentType.JAIL && !_owner.isInsideZone(ZoneId.JAIL))
				_owner.teleportTo(-114356, -249645, -2984, 20);
		}
	}
	
	/**
	 * Stop the {@link Punishment} task.
	 * @param save : If true, we save the task timer.
	 */
	public void stopTask(boolean save)
	{
		if (_task != null)
		{
			if (save)
			{
				long delay = _task.getDelay(TimeUnit.MILLISECONDS);
				if (delay < 0)
					delay = 0;
				
				_timer = delay;
			}
			
			_task.cancel(false);
			_task = null;
		}
	}
}