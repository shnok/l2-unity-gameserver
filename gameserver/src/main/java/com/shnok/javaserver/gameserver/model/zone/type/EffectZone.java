package com.shnok.javaserver.gameserver.model.zone.type;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.Future;

import com.shnok.javaserver.commons.pool.ThreadPool;
import com.shnok.javaserver.commons.random.Rnd;

import com.shnok.javaserver.gameserver.enums.ZoneId;
import com.shnok.javaserver.gameserver.model.actor.Creature;
import com.shnok.javaserver.gameserver.model.actor.Player;
import com.shnok.javaserver.gameserver.model.holder.IntIntHolder;
import com.shnok.javaserver.gameserver.model.zone.type.subtype.ZoneType;
import com.shnok.javaserver.gameserver.network.serverpackets.EtcStatusUpdate;
import com.shnok.javaserver.gameserver.skills.L2Skill;

/**
 * A zone extending {@link ZoneType}, which fires a task on the first {@link Creature} entrance.<br>
 * <br>
 * This task launches skill effects on all {@link Creature}s within this zone, and can affect specific class types. It can also be activated or desactivated. The zone is considered a danger zone.
 */
public class EffectZone extends ZoneType
{
	private final List<IntIntHolder> _skills = new ArrayList<>(5);
	
	private int _chance = 100;
	private int _initialDelay = 0;
	private int _reuseDelay = 30000;
	private Class<?> _target = Player.class;
	
	private boolean _isEnabled = true;
	
	private volatile Future<?> _task;
	
	public EffectZone(int id)
	{
		super(id);
	}
	
	@Override
	public void setParameter(String name, String value)
	{
		if (name.equals("chance"))
			_chance = Integer.parseInt(value);
		else if (name.equals("initialDelay"))
			_initialDelay = Integer.parseInt(value);
		else if (name.equals("reuseDelay"))
			_reuseDelay = Integer.parseInt(value);
		else if (name.equals("defaultStatus"))
			_isEnabled = Boolean.parseBoolean(value);
		else if (name.equals("skill"))
		{
			final String[] skills = value.split(";");
			for (String skill : skills)
			{
				final String[] skillSplit = skill.split("-");
				if (skillSplit.length != 2)
					LOGGER.warn("Invalid skill format {} for {}.", skill, toString());
				else
				{
					try
					{
						_skills.add(new IntIntHolder(Integer.parseInt(skillSplit[0]), Integer.parseInt(skillSplit[1])));
					}
					catch (NumberFormatException nfe)
					{
						LOGGER.warn("Invalid skill format {} for {}.", skill, toString());
					}
				}
			}
		}
		else if (name.equals("targetType"))
		{
			try
			{
				_target = Class.forName("com.shnok.javaserver.gameserver.model.actor." + value);
			}
			catch (ClassNotFoundException e)
			{
				LOGGER.error("Invalid target type {} for {}.", value, toString());
			}
		}
		else
			super.setParameter(name, value);
	}
	
	@Override
	protected boolean isAffected(Creature creature)
	{
		return _target.isInstance(creature);
	}
	
	@Override
	protected void onEnter(Creature creature)
	{
		Future<?> task = _task;
		if (task == null)
		{
			synchronized (this)
			{
				task = _task;
				if (task == null)
					_task = ThreadPool.scheduleAtFixedRate(this::applyEffect, _initialDelay, _reuseDelay);
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
	
	/**
	 * Edit this zone activation state.
	 * @param state : The new state to set.
	 */
	public void editStatus(boolean state)
	{
		_isEnabled = state;
	}
	
	/**
	 * Apply this {@link EffectZone} effect to all {@link Creature}s of defined target type.
	 */
	private final void applyEffect()
	{
		if (!_isEnabled)
			return;
		
		if (_creatures.isEmpty())
		{
			_task.cancel(true);
			_task = null;
			
			return;
		}
		
		for (Creature temp : _creatures)
		{
			if (temp.isDead() || Rnd.get(100) >= _chance)
				continue;
			
			for (IntIntHolder entry : _skills)
			{
				final L2Skill skill = entry.getSkill();
				if (skill != null && skill.checkCondition(temp, temp, false) && temp.getFirstEffect(entry.getId()) == null)
					skill.getEffects(temp, temp);
			}
		}
	}
}