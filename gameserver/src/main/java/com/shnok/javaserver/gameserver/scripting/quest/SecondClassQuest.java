package com.shnok.javaserver.gameserver.scripting.quest;

import java.util.HashMap;
import java.util.Map;

import com.shnok.javaserver.gameserver.model.actor.Player;
import com.shnok.javaserver.gameserver.scripting.Quest;

/**
 * Superclass for all second class quests.<br>
 * Contains shared Dimensional Diamond part.
 */
public abstract class SecondClassQuest extends Quest
{
	// Player memo types
	private static final String SECOND_CLASS_CHANGE_35 = "secondClassChange35";
	private static final String SECOND_CLASS_CHANGE_37 = "secondClassChange37";
	private static final String SECOND_CLASS_CHANGE_39 = "secondClassChange39";
	
	// Rewards
	private static final int DIMENSIONAL_DIAMOND = 7562;
	
	// Dimensional Diamond Rewards by Class for 2nd class transfer quest (35)
	private static final Map<Integer, Integer> DF_REWARD_35 = new HashMap<>();
	{
		DF_REWARD_35.put(1, 61);
		DF_REWARD_35.put(4, 45);
		DF_REWARD_35.put(7, 128);
		DF_REWARD_35.put(11, 168);
		DF_REWARD_35.put(15, 49);
		DF_REWARD_35.put(19, 61);
		DF_REWARD_35.put(22, 128);
		DF_REWARD_35.put(26, 168);
		DF_REWARD_35.put(29, 49);
		DF_REWARD_35.put(32, 61);
		DF_REWARD_35.put(35, 128);
		DF_REWARD_35.put(39, 168);
		DF_REWARD_35.put(42, 49);
		DF_REWARD_35.put(45, 61);
		DF_REWARD_35.put(47, 61);
		DF_REWARD_35.put(50, 49);
		DF_REWARD_35.put(54, 85);
		DF_REWARD_35.put(56, 85);
	}
	
	// Dimensional Diamond Rewards by Race for 2nd class transfer quest (37)
	private static final Map<Integer, Integer> DF_REWARD_37 = new HashMap<>();
	{
		DF_REWARD_37.put(0, 96);
		DF_REWARD_37.put(1, 102);
		DF_REWARD_37.put(2, 98);
		DF_REWARD_37.put(3, 109);
		DF_REWARD_37.put(4, 50);
	}
	
	// Dimensional Diamond Rewards by Class for 2nd class transfer quest (39)
	private static final Map<Integer, Integer> DF_REWARD_39 = new HashMap<>();
	{
		DF_REWARD_39.put(1, 72);
		DF_REWARD_39.put(4, 104);
		DF_REWARD_39.put(7, 96);
		DF_REWARD_39.put(11, 122);
		DF_REWARD_39.put(15, 60);
		DF_REWARD_39.put(19, 72);
		DF_REWARD_39.put(22, 96);
		DF_REWARD_39.put(26, 122);
		DF_REWARD_39.put(29, 45);
		DF_REWARD_39.put(32, 104);
		DF_REWARD_39.put(35, 96);
		DF_REWARD_39.put(39, 122);
		DF_REWARD_39.put(42, 60);
		DF_REWARD_39.put(45, 64);
		DF_REWARD_39.put(47, 72);
		DF_REWARD_39.put(50, 92);
		DF_REWARD_39.put(54, 82);
		DF_REWARD_39.put(56, 23);
	}
	
	protected SecondClassQuest(int questId, String descr)
	{
		super(questId, descr);
	}
	
	/**
	 * Rewards {@link Player} with Dimensional Diamonds for the first set of {@link SecondClassQuest} starting at level 35.
	 * @param player : The {@link Player} of particular {@link SecondClassQuest}.
	 * @return True, when Dimensional Diamonds were rewarded.
	 */
	protected static boolean giveDimensionalDiamonds35(Player player)
	{
		if (!player.getMemos().containsKey(SECOND_CLASS_CHANGE_35))
		{
			giveItems(player, DIMENSIONAL_DIAMOND, DF_REWARD_35.get(player.getClassId().getId()));
			player.getMemos().set(SECOND_CLASS_CHANGE_35, true);
			return true;
		}
		
		return false;
	}
	
	/**
	 * Rewards {@link Player} with Dimensional Diamonds for the second set of {@link SecondClassQuest} starting at level 37.
	 * @param player : The {@link Player} of particular {@link SecondClassQuest}.
	 * @return True, when Dimensional Diamonds were rewarded.
	 */
	protected static boolean giveDimensionalDiamonds37(Player player)
	{
		if (!player.getMemos().containsKey(SECOND_CLASS_CHANGE_37))
		{
			giveItems(player, DIMENSIONAL_DIAMOND, DF_REWARD_37.get(player.getRace().ordinal()));
			player.getMemos().set(SECOND_CLASS_CHANGE_37, true);
			return true;
		}
		
		return false;
	}
	
	/**
	 * Rewards {@link Player} with Dimensional Diamonds for the third set of {@link SecondClassQuest} starting at level 39.
	 * @param player : The {@link Player} of particular {@link SecondClassQuest}.
	 * @return True, when Dimensional Diamonds were rewarded.
	 */
	protected static boolean giveDimensionalDiamonds39(Player player)
	{
		if (!player.getMemos().containsKey(SECOND_CLASS_CHANGE_39))
		{
			giveItems(player, DIMENSIONAL_DIAMOND, DF_REWARD_39.get(player.getClassId().getId()));
			player.getMemos().set(SECOND_CLASS_CHANGE_39, true);
			return true;
		}
		
		return false;
	}
}
