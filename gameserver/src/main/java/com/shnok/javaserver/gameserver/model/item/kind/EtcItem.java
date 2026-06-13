package com.shnok.javaserver.gameserver.model.item.kind;

import com.shnok.javaserver.commons.data.StatSet;

import com.shnok.javaserver.gameserver.enums.items.EtcItemType;
import com.shnok.javaserver.gameserver.model.itemcontainer.PcInventory;

/**
 * This class is dedicated to the management of EtcItem.
 */
public final class EtcItem extends Item
{
	private final String _handler;
	private final int _sharedReuseGroup;
	private EtcItemType _type;
	private final int _reuseDelay;
	
	public EtcItem(StatSet set)
	{
		super(set);
		
		_type = set.getEnum("etcitem_type", EtcItemType.class, EtcItemType.NONE);
		
		switch (getDefaultAction())
		{
			case soulshot, spiritshot, summon_soulshot, summon_spiritshot:
				_type = EtcItemType.SHOT;
				break;
		}
		
		_type1 = Item.TYPE1_ITEM_QUESTITEM_ADENA;
		_type2 = Item.TYPE2_OTHER; // default is other
		
		if (isQuestItem())
			_type2 = Item.TYPE2_QUEST;
		else if (getItemId() == PcInventory.ADENA_ID || getItemId() == PcInventory.ANCIENT_ADENA_ID)
			_type2 = Item.TYPE2_MONEY;
		
		_handler = set.getString("handler", null);
		_sharedReuseGroup = set.getInteger("shared_reuse_group", -1);
		_reuseDelay = set.getInteger("reuse_delay", 0);
	}
	
	@Override
	public EtcItemType getItemType()
	{
		return _type;
	}
	
	@Override
	public final boolean isConsumable()
	{
		return ((getItemType() == EtcItemType.SHOT) || (getItemType() == EtcItemType.POTION));
	}
	
	@Override
	public int getItemMask()
	{
		return getItemType().mask();
	}
	
	/**
	 * @return the handler name, or null otherwise.
	 */
	public String getHandlerName()
	{
		return _handler;
	}
	
	public int getSharedReuseGroup()
	{
		return _sharedReuseGroup;
	}
	
	public int getReuseDelay()
	{
		return _reuseDelay;
	}
}