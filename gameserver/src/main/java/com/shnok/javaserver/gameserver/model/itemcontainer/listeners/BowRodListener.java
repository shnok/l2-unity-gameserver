package com.shnok.javaserver.gameserver.model.itemcontainer.listeners;

import com.shnok.javaserver.gameserver.enums.Paperdoll;
import com.shnok.javaserver.gameserver.enums.items.WeaponType;
import com.shnok.javaserver.gameserver.model.actor.Playable;
import com.shnok.javaserver.gameserver.model.item.instance.ItemInstance;

public class BowRodListener implements OnEquipListener
{
	@Override
	public void onEquip(Paperdoll slot, ItemInstance item, Playable actor)
	{
		if (slot != Paperdoll.RHAND)
			return;
		
		if (item.getItemType() == WeaponType.BOW)
		{
			final ItemInstance arrow = actor.getInventory().findArrowForBow(item.getItem());
			if (arrow != null)
				actor.getInventory().setPaperdollItem(Paperdoll.LHAND, arrow);
		}
	}
	
	@Override
	public void onUnequip(Paperdoll slot, ItemInstance item, Playable actor)
	{
		if (slot != Paperdoll.RHAND)
			return;
		
		if (item.getItemType() == WeaponType.BOW || item.getItemType() == WeaponType.FISHINGROD)
		{
			final ItemInstance lHandItem = actor.getSecondaryWeaponInstance();
			if (lHandItem != null)
				actor.getInventory().setPaperdollItem(Paperdoll.LHAND, null);
		}
	}
	
	public static final BowRodListener getInstance()
	{
		return SingletonHolder.INSTANCE;
	}
	
	private static class SingletonHolder
	{
		protected static final BowRodListener INSTANCE = new BowRodListener();
	}
}