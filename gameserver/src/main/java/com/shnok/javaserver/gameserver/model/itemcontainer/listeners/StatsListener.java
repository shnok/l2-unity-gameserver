package com.shnok.javaserver.gameserver.model.itemcontainer.listeners;

import com.shnok.javaserver.gameserver.enums.Paperdoll;
import com.shnok.javaserver.gameserver.model.actor.Playable;
import com.shnok.javaserver.gameserver.model.item.instance.ItemInstance;

public class StatsListener implements OnEquipListener
{
	@Override
	public void onEquip(Paperdoll slot, ItemInstance item, Playable playable)
	{
		playable.addStatFuncs(item.getStatFuncs(playable));
	}
	
	@Override
	public void onUnequip(Paperdoll slot, ItemInstance item, Playable playable)
	{
		playable.removeStatsByOwner(item);
	}
	
	public static final StatsListener getInstance()
	{
		return SingletonHolder.INSTANCE;
	}
	
	private static class SingletonHolder
	{
		protected static final StatsListener INSTANCE = new StatsListener();
	}
}