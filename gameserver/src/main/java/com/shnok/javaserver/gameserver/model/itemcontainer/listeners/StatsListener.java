package com.shnok.javaserver.gameserver.model.itemcontainer.listeners;

import net.sf.l2j.gameserver.enums.Paperdoll;
import net.sf.l2j.gameserver.model.actor.Playable;
import net.sf.l2j.gameserver.model.item.instance.ItemInstance;

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