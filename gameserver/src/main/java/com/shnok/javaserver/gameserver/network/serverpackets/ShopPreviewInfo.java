package com.shnok.javaserver.gameserver.network.serverpackets;

import java.util.Map;

import com.shnok.javaserver.gameserver.enums.Paperdoll;

public class ShopPreviewInfo extends L2GameServerPacket
{
	private final Map<Paperdoll, Integer> _items;
	
	public ShopPreviewInfo(Map<Paperdoll, Integer> items)
	{
		_items = items;
	}
	
	@Override
	protected final void writeImpl()
	{
		writeC(0xf0);
		writeD(Paperdoll.TOTAL_SLOTS);
		writeD(_items.getOrDefault(Paperdoll.REAR, 0)); // unverified
		writeD(_items.getOrDefault(Paperdoll.LEAR, 0)); // unverified
		writeD(_items.getOrDefault(Paperdoll.NECK, 0)); // unverified
		writeD(_items.getOrDefault(Paperdoll.RFINGER, 0)); // unverified
		writeD(_items.getOrDefault(Paperdoll.LFINGER, 0)); // unverified
		writeD(_items.getOrDefault(Paperdoll.HEAD, 0)); // unverified
		writeD(_items.getOrDefault(Paperdoll.RHAND, 0)); // good
		writeD(_items.getOrDefault(Paperdoll.LHAND, 0)); // good
		writeD(_items.getOrDefault(Paperdoll.GLOVES, 0)); // good
		writeD(_items.getOrDefault(Paperdoll.CHEST, 0)); // good
		writeD(_items.getOrDefault(Paperdoll.LEGS, 0)); // good
		writeD(_items.getOrDefault(Paperdoll.FEET, 0)); // good
		writeD(_items.getOrDefault(Paperdoll.CLOAK, 0)); // unverified
		writeD(_items.getOrDefault(Paperdoll.FACE, 0)); // unverified
		writeD(_items.getOrDefault(Paperdoll.HAIR, 0)); // unverified
		writeD(_items.getOrDefault(Paperdoll.HAIRALL, 0)); // unverified
		writeD(_items.getOrDefault(Paperdoll.UNDER, 0)); // unverified
	}
}