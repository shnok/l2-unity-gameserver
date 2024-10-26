package com.shnok.javaserver.gameserver.model.actor.container.monster;

import net.sf.l2j.gameserver.model.actor.Creature;
import net.sf.l2j.gameserver.model.actor.Player;
import net.sf.l2j.gameserver.model.actor.instance.Monster;

/**
 * A container holding all related informations of a {@link Monster} over-hit.<br>
 * <br>
 * An over-hit occurs when a {@link Player} procs a over-hit-friendly skill over a Monster. If that Monster dies due to that skill, the damage amount difference is calculated as exp points bonus.
 */
public class OverhitState
{
	private final Monster _owner;
	
	private boolean _isOverhit;
	private double _overhitDamage;
	private Creature _overhitAttacker;
	
	public OverhitState(Monster owner)
	{
		_owner = owner;
	}
	
	/**
	 * Set the over-hit flag on this {@link Monster}.
	 * @param status : The status of the over-hit flag.
	 */
	public void set(boolean status)
	{
		_isOverhit = status;
	}
	
	/**
	 * Test and eventually set the over-hit values based on the {@link Creature} who did the strike and the damage amount.
	 * @param attacker : The Creature who attacked this {@link Monster}.
	 * @param damage : The damage amount.
	 */
	public void test(Creature attacker, double damage)
	{
		// Over-hit wasn't procced, simply return.
		if (!_isOverhit)
			return;
		
		// No damage.
		if (damage <= 0)
		{
			_isOverhit = false;
			return;
		}
		
		// Calculate the over-hit damage.
		final double overhitDamage = ((_owner.getStatus().getHp() - damage) * (-1));
		
		// Not enough damage to kill the Monster.
		if (overhitDamage < 0)
		{
			_isOverhit = false;
			return;
		}
		
		// Over-hit is a success, set variables.
		_overhitDamage = overhitDamage;
		_overhitAttacker = attacker;
	}
	
	/**
	 * Clear all over-hit related variables.
	 */
	public void clear()
	{
		_isOverhit = false;
		_overhitDamage = 0;
		_overhitAttacker = null;
	}
	
	/**
	 * Calculate the bonus involved by over-hit over a given amount of exp points.
	 * @param normalExp : The base exp points.
	 * @return the calculated value (base exp points + bonus).
	 */
	public long calculateOverhitExp(long normalExp)
	{
		// Get the percentage based on the total of extra (over-hit) damage done relative to the total (maximum) amount of HP.
		double overhitPercentage = ((_overhitDamage * 100) / _owner.getStatus().getMaxHp());
		
		// Over-hit damage percentages are limited to 25% max.
		if (overhitPercentage > 25)
			overhitPercentage = 25;
		
		// Get the over-hit exp bonus according to the above over-hit damage percentage.
		double overhitExp = ((overhitPercentage / 100) * normalExp);
		
		// Return the rounded amount of exp points to be added to the player's normal exp reward.
		return Math.round(overhitExp);
	}
	
	/**
	 * @param player : The Player to test.
	 * @return true if the hit is a valid over-hit (over-hit and attacker is the tested Player).
	 */
	public boolean isValidOverhit(Player player)
	{
		return _isOverhit && _overhitAttacker != null && _overhitAttacker.getActingPlayer() != null && player == _overhitAttacker.getActingPlayer();
	}
}