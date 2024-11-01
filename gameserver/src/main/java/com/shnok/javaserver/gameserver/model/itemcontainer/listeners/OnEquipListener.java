package com.shnok.javaserver.gameserver.model.itemcontainer.listeners;

import com.shnok.javaserver.gameserver.enums.Paperdoll;
import com.shnok.javaserver.gameserver.model.actor.Playable;
import com.shnok.javaserver.gameserver.model.item.instance.ItemInstance;

public interface OnEquipListener
{
	public void onEquip(Paperdoll slot, ItemInstance item, Playable actor);
	
	public void onUnequip(Paperdoll slot, ItemInstance item, Playable actor);
}