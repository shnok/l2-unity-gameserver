package com.shnok.javaserver.gameserver.model.item;

import java.util.Arrays;

import com.shnok.javaserver.commons.data.StatSet;

import com.shnok.javaserver.gameserver.enums.CabalType;
import com.shnok.javaserver.gameserver.enums.items.TicketType;

public final class MercenaryTicket
{
	private final int _itemId;
	private final TicketType _type;
	private final boolean _isStationary;
	private final int _npcId;
	private final int _maxAmount;
	private final CabalType[] _ssq;
	
	public MercenaryTicket(StatSet set)
	{
		_itemId = set.getInteger("itemId");
		_type = set.getEnum("type", TicketType.class);
		_isStationary = set.getBool("stationary");
		_npcId = set.getInteger("npcId");
		_maxAmount = set.getInteger("maxAmount");
		
		final String[] ssq = set.getStringArray("ssq");
		
		_ssq = new CabalType[ssq.length];
		for (int i = 0; i < ssq.length; i++)
			_ssq[i] = Enum.valueOf(CabalType.class, ssq[i]);
	}
	
	public int getItemId()
	{
		return _itemId;
	}
	
	public TicketType getType()
	{
		return _type;
	}
	
	public boolean isStationary()
	{
		return _isStationary;
	}
	
	public int getNpcId()
	{
		return _npcId;
	}
	
	public int getMaxAmount()
	{
		return _maxAmount;
	}
	
	public boolean isSsqType(CabalType type)
	{
		return Arrays.asList(_ssq).contains(type);
	}
}