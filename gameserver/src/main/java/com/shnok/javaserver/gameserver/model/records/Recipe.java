package com.shnok.javaserver.gameserver.model.records;

import java.util.List;

import com.shnok.javaserver.commons.data.StatSet;

import com.shnok.javaserver.gameserver.model.holder.IntIntHolder;

public record Recipe(List<IntIntHolder> materials, IntIntHolder product, int id, int level, int recipeId, String alias, int successRate, int mpCost, boolean isDwarven)
{
	public Recipe(StatSet set)
	{
		this(set.getIntIntHolderList("material"), set.getIntIntHolder("product"), set.getInteger("id"), set.getInteger("level"), set.getInteger("itemId"), set.getString("alias"), set.getInteger("successRate"), set.getInteger("mpConsume"), set.getBool("isDwarven"));
	}
}