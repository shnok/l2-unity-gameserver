package net.sf.l2j.gameserver.model.records;

import net.sf.l2j.commons.data.StatSet;

/**
 * This record stores Soul Crystal leveling infos related to items.
 * @param level : The current level on the hierarchy tree of items.
 * @param initialItemId : The initial itemId from where we start.
 * @param stagedItemId : The succeeded itemId rewarded if absorb was successful.
 * @param brokenItemId : The broken itemId rewarded if absorb failed.
 */
public record SoulCrystal(int level, int initialItemId, int stagedItemId, int brokenItemId)
{
	public SoulCrystal(StatSet set)
	{
		this(set.getInteger("level"), set.getInteger("initial"), set.getInteger("staged"), set.getInteger("broken"));
	}
}