package com.shnok.javaserver.gameserver.model.actor.container.npc;

import com.shnok.javaserver.gameserver.model.actor.Playable;

/**
 * This class is used to retain damage infos made on a Attackable. It is used for reward purposes.
 */
public final class RewardInfo
{
	private final Playable _attacker;
	
	private double _damage;
	
	public RewardInfo(Playable attacker)
	{
		_attacker = attacker;
	}
	
	public Playable getAttacker()
	{
		return _attacker;
	}
	
	public void addDamage(double damage)
	{
		_damage += damage;
	}
	
	public double getDamage()
	{
		return _damage;
	}
	
	@Override
	public final boolean equals(Object obj)
	{
		if (this == obj)
			return true;
		
		if (obj instanceof RewardInfo ri)
			return ri._attacker == _attacker;
		
		return false;
	}
	
	@Override
	public final int hashCode()
	{
		return _attacker.getObjectId();
	}
}