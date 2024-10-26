package com.shnok.javaserver.gameserver.model.records;

import com.shnok.javaserver.commons.data.StatSet;

public record PlayerLevel(long requiredExpToLevelUp, double karmaModifier, double expLossAtDeath)
{
	public PlayerLevel(StatSet set)
	{
		this(set.getLong("requiredExpToLevelUp"), set.getDouble("karmaModifier", 0.), set.getDouble("expLossAtDeath", 0.));
	}
}