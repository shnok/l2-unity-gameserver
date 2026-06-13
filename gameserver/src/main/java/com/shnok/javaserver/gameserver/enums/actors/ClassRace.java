package com.shnok.javaserver.gameserver.enums.actors;

import com.shnok.javaserver.gameserver.model.actor.Player;

/**
 * This class defines all races that a player can choose.
 */
public enum ClassRace
{
	HUMAN(1),
	ELF(1.5),
	DARK_ELF(1.5),
	ORC(0.9),
	DWARF(0.8);
	
	public static final ClassRace[] VALUES = values();
	
	private final double _breathMultiplier;
	
	private ClassRace(double breathMultiplier)
	{
		_breathMultiplier = breathMultiplier;
	}
	
	/**
	 * @return the breath multiplier.
	 */
	public double getBreathMultiplier()
	{
		return _breathMultiplier;
	}
	
	public static final boolean isSameRace(Player player, String race)
	{
		if (player == null || race == null)
			return false;
		
		switch (race)
		{
			case "@race_human":
				return player.getRace() == HUMAN;
			
			case "@race_elf":
				return player.getRace() == ELF;
			
			case "@race_dark_elf":
				return player.getRace() == DARK_ELF;
			
			case "@race_orc":
				return player.getRace() == ORC;
			
			case "@race_dwarf":
				return player.getRace() == DWARF;
		}
		return false;
	}
}