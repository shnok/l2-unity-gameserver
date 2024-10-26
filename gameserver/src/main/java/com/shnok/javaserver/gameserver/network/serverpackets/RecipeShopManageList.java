package com.shnok.javaserver.gameserver.network.serverpackets;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import com.shnok.javaserver.gameserver.model.actor.Player;
import com.shnok.javaserver.gameserver.model.craft.ManufactureList;
import com.shnok.javaserver.gameserver.model.records.ManufactureItem;
import com.shnok.javaserver.gameserver.model.records.Recipe;

public class RecipeShopManageList extends L2GameServerPacket
{
	private final Player _player;
	private final Collection<Recipe> _recipes;
	private final List<ManufactureItem> _items = new ArrayList<>();
	
	public RecipeShopManageList(Player player, boolean isDwarven)
	{
		_player = player;
		_recipes = player.getRecipeBook().get(isDwarven && player.hasDwarvenCraft());
		
		final ManufactureList manufactureList = player.getManufactureList();
		manufactureList.setState(isDwarven);
		
		_items.addAll(manufactureList);
		_items.removeIf(i -> i.isDwarven() != isDwarven || !player.getRecipeBook().hasRecipe(i.recipeId()));
	}
	
	@Override
	protected final void writeImpl()
	{
		writeC(0xd8);
		writeD(_player.getObjectId());
		writeD(_player.getAdena());
		writeD(_player.getManufactureList().isDwarven() ? 0x00 : 0x01);
		
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
		
		writeD(_items.size());
		
		for (ManufactureItem item : _items)
		{
			writeD(item.recipeId());
			writeD(0x00);
			writeD(item.cost());
		}
	}
}