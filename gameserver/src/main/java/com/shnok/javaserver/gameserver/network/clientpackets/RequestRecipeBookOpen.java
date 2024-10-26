package com.shnok.javaserver.gameserver.network.clientpackets;

import com.shnok.javaserver.gameserver.model.actor.Player;
import com.shnok.javaserver.gameserver.network.SystemMessageId;
import com.shnok.javaserver.gameserver.network.serverpackets.RecipeBookItemList;

public final class RequestRecipeBookOpen extends L2GameClientPacket
{
	private boolean _isDwarven;
	
	@Override
	protected void readImpl()
	{
		_isDwarven = (readD() == 0);
	}
	
	@Override
	protected void runImpl()
	{
		final Player player = getClient().getPlayer();
		if (player == null)
			return;
		
		if (player.getCast().isCastingNow() || player.isAllSkillsDisabled())
		{
			player.sendPacket(SystemMessageId.NO_RECIPE_BOOK_WHILE_CASTING);
			return;
		}
		
		player.sendPacket(new RecipeBookItemList(player, _isDwarven));
	}
}