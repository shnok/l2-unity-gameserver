package com.shnok.javaserver.gameserver.network.serverpackets;

import java.util.Collection;

import net.sf.l2j.gameserver.model.actor.Player;
import net.sf.l2j.gameserver.model.records.Recipe;

public class RecipeBookItemList extends L2GameServerPacket
{
	private Collection<Recipe> _recipes;
	private final boolean _isDwarven;
	private final int _maxMp;
	
	public RecipeBookItemList(Player player, boolean isDwarven)
	{
		_recipes = player.getRecipeBook().get(isDwarven);
		_isDwarven = isDwarven;
		_maxMp = player.getStatus().getMaxMp();
	}
	
	@Override
	protected final void writeImpl()
	{
		writeC(0xD6);
		
		writeD((_isDwarven) ? 0x00 : 0x01);
		writeD(_maxMp);
		
		if (_recipes == null)
			writeD(0);
		else
		{
			writeD(_recipes.size());
			
			int i = 0;
			for (Recipe recipe : _recipes)
			{
				writeD(recipe.id());
				writeD(++i);
			}
		}
	}
}