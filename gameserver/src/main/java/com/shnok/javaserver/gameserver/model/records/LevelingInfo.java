package com.shnok.javaserver.gameserver.model.records;

import com.shnok.javaserver.commons.data.StatSet;

import com.shnok.javaserver.gameserver.enums.items.AbsorbCrystalType;

/**
 * This class stores Soul Crystal leveling infos related to NPCs.
 * @param absorbCrystalType : The AbsorbCrystalType which can be LAST_HIT, FULL_PARTY or PARTY_ONE_RANDOM.
 * @param isSkillRequired : If the item cast on monster is required or not.
 * @param chanceStage : The chance of success (base 1000).
 * @param chanceBreak : The chance of break (base 1000).
 * @param levelList : The list of allowed crystals levels.
 */
public record LevelingInfo(AbsorbCrystalType absorbCrystalType, boolean isSkillRequired, int chanceStage, int chanceBreak, int[] levelList)
{
	public LevelingInfo(StatSet set)
	{
		this(set.getEnum("absorbType", AbsorbCrystalType.class), set.getBool("skill"), set.getInteger("chanceStage"), set.getInteger("chanceBreak"), set.getIntegerArray("levelList"));
	}
}