package com.shnok.javaserver.gameserver.network.serverpackets.unused;

import java.util.ArrayList;
import java.util.List;

import com.shnok.javaserver.gameserver.data.manager.CastleManorManager;
import com.shnok.javaserver.gameserver.model.manor.SeedProduction;
import com.shnok.javaserver.gameserver.network.serverpackets.L2GameServerPacket;

public final class BuyListSeed extends L2GameServerPacket
{
	private final int _manorId;
	private final List<SeedProduction> _list;
	private final int _money;
	
	public BuyListSeed(int currentMoney, int castleId)
	{
		_money = currentMoney;
		_manorId = castleId;
		
		_list = new ArrayList<>();
		for (SeedProduction s : CastleManorManager.getInstance().getSeedProduction(castleId, false))
		{
			if (s.getAmount() > 0 && s.getPrice() > 0)
				_list.add(s);
		}
	}
	
	@Override
	protected final void writeImpl()
	{
		writeC(0xE8);
		
		writeD(_money);
		writeD(_manorId);
		
		if (!_list.isEmpty())
		{
			writeH(_list.size());
			for (SeedProduction s : _list)
			{
				writeH(0x04);
				writeD(s.getId());
				writeD(s.getId());
				writeD(s.getAmount());
				writeH(0x04);
				writeH(0x00);
				writeD(s.getPrice());
			}
		}
	}
}