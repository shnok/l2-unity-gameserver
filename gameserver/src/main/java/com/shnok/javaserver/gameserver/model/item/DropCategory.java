package com.shnok.javaserver.gameserver.model.item;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

import com.shnok.javaserver.commons.random.Rnd;

import com.shnok.javaserver.gameserver.enums.DropType;
import com.shnok.javaserver.gameserver.model.holder.IntIntHolder;

public class DropCategory extends ArrayList<DropData>
{
	private static final long serialVersionUID = 1L;
	
	private final DropType _dropType;
	private final double _chance;
	
	public DropCategory(DropType dropType, double chance)
	{
		_dropType = dropType;
		_chance = chance;
	}
	
	/**
	 * @return The {@link DropType} of this {@link DropCategory}.
	 */
	public DropType getDropType()
	{
		return _dropType;
	}
	
	/**
	 * @return The {@link DropCategory} chance.
	 */
	public double getChance()
	{
		return _chance;
	}
	
	/**
	 * Calculates drops of this {@link DropCategory}.
	 * @param levelMultiplier : The input level modifier of the last attacker.
	 * @param raid : The NPC is raid boss.
	 * @return The list of {@link IntIntHolder} holding item ID and item count.
	 */
	public Map<Integer, Integer> calculateDrop(double levelMultiplier, boolean raid)
	{
		// If base chance or multiplier is 0, return directly.
		if (_chance == 0. || levelMultiplier == 0.)
			return Collections.emptyMap();
		
		// Retrieve the Config rate. If rate is 0, return directly.
		final double rate = getDropType().getDropRate(raid);
		if (rate == 0.)
			return Collections.emptyMap();
		
		// The actual drops, stored into a map for easier management. Similar drops stack on each other.
		final Map<Integer, Integer> result = new HashMap<>(1);
		
		// Iterate X times category and drop chances.
		for (int i = 0; i < rate; i++)
		{
			// Test category based on level multiplier.
			double chance = getChance() * levelMultiplier * DropData.PERCENT_CHANCE;
			if (chance >= DropData.MAX_CHANCE || Rnd.get(DropData.MAX_CHANCE) < chance)
			{
				// Evaluate each drop if the drop type is SPOIL.
				if (_dropType == DropType.SPOIL)
				{
					for (DropData dd : this)
					{
						if (dd.chance() * DropData.PERCENT_CHANCE >= DropData.MAX_CHANCE || Rnd.get(DropData.MAX_CHANCE) < dd.chance() * DropData.PERCENT_CHANCE)
							result.merge(dd.itemId(), dd.getRandomDrop(), Integer::sum);
					}
				}
				// For other cases, evaluate all drops, pick only one.
				else
				{
					// Initialize cumulativeChance to 0.
					double cumulativeChance = 0;
					
					// Generate a random value.
					double randomValue = Rnd.get(DropData.MAX_CHANCE);
					
					// Iterate through drops to find the one that matches the random value.
					for (DropData dd : this)
					{
						cumulativeChance += dd.chance() * DropData.PERCENT_CHANCE;
						
						// If the random value is within the chance range, add the drop to the result list.
						if (randomValue < cumulativeChance)
						{
							result.merge(dd.itemId(), dd.getRandomDrop(), Integer::sum);
							break;
						}
					}
				}
			}
		}
		return result;
	}
}