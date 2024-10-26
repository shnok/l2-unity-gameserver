package net.sf.l2j.gameserver.model.records;

import net.sf.l2j.commons.data.StatSet;
import net.sf.l2j.commons.util.ArraysUtil;

import net.sf.l2j.gameserver.model.actor.Player;

public record Henna(int symbolId, int dyeId, int drawPrice, int INT, int STR, int CON, int MEN, int DEX, int WIT, int[] classes)
{
	
	public static final int DRAW_AMOUNT = 10;
	public static final int REMOVE_AMOUNT = 5;
	
	public Henna(StatSet set)
	{
		this(set.getInteger("symbolId"), set.getInteger("dyeId"), set.getInteger("price", 0), set.getInteger("INT", 0), set.getInteger("STR", 0), set.getInteger("CON", 0), set.getInteger("MEN", 0), set.getInteger("DEX", 0), set.getInteger("WIT", 0), set.getIntegerArray("classes"));
	}
	
	public int getRemovePrice()
	{
		return drawPrice / REMOVE_AMOUNT;
	}
	
	public boolean canBeUsedBy(Player player)
	{
		return ArraysUtil.contains(classes, player.getClassId().getId());
	}
}