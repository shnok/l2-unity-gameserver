package com.shnok.javaserver.gameserver.network.serverpackets;

import com.shnok.javaserver.gameserver.model.actor.Player;

public class RecipeShopItemInfo extends L2GameServerPacket
{
	private final int _objectId;
	private final int _recipeId;
	private final int _mp;
	private final int _maxMp;
	
	public RecipeShopItemInfo(Player player, int recipeId)
	{
		_objectId = player.getObjectId();
		_recipeId = recipeId;
		_mp = (int) player.getStatus().getMp();
		_maxMp = player.getStatus().getMaxMp();
	}
	
	@Override
	protected final void writeImpl()
	{
		writeC(0xda);
		writeD(_objectId);
		writeD(_recipeId);
		writeD(_mp);
		writeD(_maxMp);
		writeD(0xffffffff);
	}
}