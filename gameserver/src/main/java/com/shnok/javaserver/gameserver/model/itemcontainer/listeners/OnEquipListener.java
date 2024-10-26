package com.shnok.javaserver.gameserver.model.itemcontainer.listeners;

import net.sf.l2j.gameserver.enums.Paperdoll;
import net.sf.l2j.gameserver.model.actor.Playable;
import net.sf.l2j.gameserver.model.item.instance.ItemInstance;

public interface OnEquipListener
{
	public void onEquip(Paperdoll slot, ItemInstance item, Playable actor);
	
	public void onUnequip(Paperdoll slot, ItemInstance item, Playable actor);
}