package com.shnok.javaserver.gameserver.model.actor.container.npc;

import com.shnok.javaserver.gameserver.model.actor.Creature;

/**
 * This class contains all aggro informations (damage and hate) against a {@link Creature}.<br>
 * <br>
 * Values are limited to 999999999.
 */
public final class AggroInfo
{
	private final Creature _attacker;
	
	private double _damage;
	private double _hate;
	private long _timestamp;
	
	public AggroInfo(Creature attacker)
	{
		_attacker = attacker;
	}
	
	@Override
	public final boolean equals(Object obj)
	{
		if (this == obj)
			return true;
		
		if (obj instanceof AggroInfo ai)
			return ai.getAttacker() == _attacker;
		
		return false;
	}
	
	@Override
	public final int hashCode()
	{
		return _attacker.getObjectId();
	}
	
	@Override
	public String toString()
	{
		return "AggroInfo [attacker=" + _attacker + ", damage=" + _damage + ", hate=" + _hate + "]";
	}
	
	public Creature getAttacker()
	{
		return _attacker;
	}
	
	public double getDamage()
	{
		return _damage;
	}
	
	public void addDamage(double value)
	{
		_damage = Math.min(_damage + value, 999999999);
	}
	
	public double getHate()
	{
		return _hate;
	}
	
	public void addHate(double value)
	{
		_hate = Math.min(_hate + value, 999999999);
	}
	
	public void stopHate()
	{
		_hate = 0;
	}
	
	public long getTimestamp()
	{
		return _timestamp;
	}
	
	public void setTimestamp(long timestamp)
	{
		_timestamp = timestamp;
	}
}