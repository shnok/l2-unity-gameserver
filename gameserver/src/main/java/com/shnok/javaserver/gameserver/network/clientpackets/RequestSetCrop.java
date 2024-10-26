package com.shnok.javaserver.gameserver.network.clientpackets;

import java.util.ArrayList;
import java.util.List;

import com.shnok.javaserver.Config;
import com.shnok.javaserver.gameserver.data.manager.CastleManorManager;
import com.shnok.javaserver.gameserver.enums.PrivilegeType;
import com.shnok.javaserver.gameserver.model.actor.Player;
import com.shnok.javaserver.gameserver.model.manor.CropProcure;
import com.shnok.javaserver.gameserver.model.manor.Seed;
import com.shnok.javaserver.gameserver.network.serverpackets.ActionFailed;

public class RequestSetCrop extends L2GameClientPacket
{
	private static final int BATCH_LENGTH = 13;
	
	private int _manorId;
	private List<CropProcure> _items;
	
	@Override
	protected void readImpl()
	{
		_manorId = readD();
		final int count = readD();
		if (count <= 0 || count > Config.MAX_ITEM_IN_PACKET || (count * BATCH_LENGTH) != _buf.remaining())
			return;
		
		_items = new ArrayList<>(count);
		for (int i = 0; i < count; i++)
		{
			final int itemId = readD();
			final int sales = readD();
			final int price = readD();
			final int type = readC();
			
			if (itemId < 1 || sales < 0 || price < 0)
			{
				_items.clear();
				return;
			}
			
			if (sales > 0)
				_items.add(new CropProcure(itemId, sales, type, sales, price));
		}
	}
	
	@Override
	protected void runImpl()
	{
		if (_items.isEmpty())
			return;
		
		final CastleManorManager manor = CastleManorManager.getInstance();
		if (!manor.isModifiablePeriod())
		{
			sendPacket(ActionFailed.STATIC_PACKET);
			return;
		}
		
		// Check player privileges
		final Player player = getClient().getPlayer();
		if (player == null || player.getClan() == null || player.getClan().getCastleId() != _manorId || !player.hasClanPrivileges(PrivilegeType.CP_MANOR_ADMINISTRATION) || !player.getAI().canDoInteract(player.getCurrentFolk()))
		{
			sendPacket(ActionFailed.STATIC_PACKET);
			return;
		}
		
		// Filter crops with start amount lower than 0 and incorrect price
		final List<CropProcure> list = new ArrayList<>(_items.size());
		for (CropProcure cp : _items)
		{
			final Seed s = manor.getSeedByCrop(cp.getId(), _manorId);
			if (s != null && cp.getStartAmount() <= s.getCropsLimit() && cp.getPrice() >= s.getCropMinPrice() && cp.getPrice() <= s.getCropMaxPrice())
				list.add(cp);
		}
		
		// Save crop list
		manor.setNextCropProcure(list, _manorId);
	}
}