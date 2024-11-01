package com.shnok.javaserver.gameserver.network.clientpackets.unused;

import java.util.ArrayList;
import java.util.List;

import com.shnok.javaserver.Config;
import com.shnok.javaserver.gameserver.model.holder.IntIntHolder;
import com.shnok.javaserver.gameserver.network.clientpackets.L2GameClientPacket;

public class RequestBuyProcure extends L2GameClientPacket
{
	private static final int BATCH_LENGTH = 8;
	
	private int _manorId;
	private List<IntIntHolder> _items;
	
	@Override
	protected void readImpl()
	{
		_manorId = readD();
		
		final int count = readD();
		if (count <= 0 || count > Config.MAX_ITEM_IN_PACKET || count * BATCH_LENGTH != _buf.remaining())
			return;
		
		_items = new ArrayList<>(count);
		for (int i = 0; i < count; i++)
		{
			readD(); // service
			final int itemId = readD();
			final int cnt = readD();
			
			if (itemId < 1 || cnt < 1)
			{
				_items = null;
				return;
			}
			
			_items.add(new IntIntHolder(itemId, cnt));
		}
	}
	
	@Override
	protected void runImpl()
	{
		LOGGER.warn("RequestBuyProcure: normally unused, but found {} infos for manorId {}.", _items.size(), _manorId);
	}
}