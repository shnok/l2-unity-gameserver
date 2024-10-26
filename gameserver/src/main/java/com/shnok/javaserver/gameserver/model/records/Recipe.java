package net.sf.l2j.gameserver.model.records;

import java.util.List;

import net.sf.l2j.commons.data.StatSet;

import net.sf.l2j.gameserver.model.holder.IntIntHolder;

public record Recipe(List<IntIntHolder> materials, IntIntHolder product, int id, int level, int recipeId, String alias, int successRate, int mpCost, boolean isDwarven)
{
	public Recipe(StatSet set)
	{
		this(set.getIntIntHolderList("material"), set.getIntIntHolder("product"), set.getInteger("id"), set.getInteger("level"), set.getInteger("itemId"), set.getString("alias"), set.getInteger("successRate"), set.getInteger("mpConsume"), set.getBool("isDwarven"));
	}
}