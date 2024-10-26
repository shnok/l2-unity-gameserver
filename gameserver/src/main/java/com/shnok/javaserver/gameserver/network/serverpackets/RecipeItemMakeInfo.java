package com.shnok.javaserver.gameserver.network.serverpackets;

import com.shnok.javaserver.gameserver.data.xml.RecipeData;
import com.shnok.javaserver.gameserver.model.actor.Player;
import com.shnok.javaserver.gameserver.model.records.Recipe;

public class RecipeItemMakeInfo extends L2GameServerPacket
{
	private final int _id;
	private final int _mp;
	private final int _maxMp;
	private final int _status;
	
	public RecipeItemMakeInfo(int id, Player player, int status)
	{
		_id = id;
		_mp = (int) player.getStatus().getMp();
		_maxMp = player.getStatus().getMaxMp();
		_status = status;
	}
	
	public RecipeItemMakeInfo(int id, Player player)
	{
		this(id, player, -1);
	}
	
	@Override
	protected final void writeImpl()
	{
		final Recipe recipe = RecipeData.getInstance().getRecipeList(_id);
		if (recipe != null)
		{
			writeC(0xD7);
			
			writeD(_id);
			writeD((recipe.isDwarven()) ? 0 : 1);
			writeD(_mp);
			writeD(_maxMp);
			writeD(_status);
		}
	}
}