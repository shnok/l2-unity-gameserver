package com.shnok.javaserver.gameserver.network.clientpackets;

import net.sf.l2j.gameserver.data.xml.RecipeData;
import net.sf.l2j.gameserver.enums.actors.OperateType;
import net.sf.l2j.gameserver.model.actor.Player;
import net.sf.l2j.gameserver.model.records.Recipe;
import net.sf.l2j.gameserver.network.SystemMessageId;
import net.sf.l2j.gameserver.network.serverpackets.RecipeBookItemList;
import net.sf.l2j.gameserver.network.serverpackets.SystemMessage;

public final class RequestRecipeBookDestroy extends L2GameClientPacket
{
	private int _recipeId;
	
	@Override
	protected void readImpl()
	{
		_recipeId = readD();
	}
	
	@Override
	protected void runImpl()
	{
		final Player player = getClient().getPlayer();
		if (player == null)
			return;
		
		if (player.getOperateType() == OperateType.MANUFACTURE)
		{
			player.sendPacket(SystemMessageId.CANT_ALTER_RECIPEBOOK_WHILE_CRAFTING);
			return;
		}
		
		final Recipe recipe = RecipeData.getInstance().getRecipeList(_recipeId);
		if (recipe == null)
			return;
		
		player.getRecipeBook().removeRecipe(_recipeId);
		player.sendPacket(SystemMessage.getSystemMessage(SystemMessageId.S1_HAS_BEEN_DELETED).addItemName(recipe.recipeId()));
		player.sendPacket(new RecipeBookItemList(player, recipe.isDwarven()));
	}
}