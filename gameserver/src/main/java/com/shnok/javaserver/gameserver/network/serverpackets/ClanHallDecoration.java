package com.shnok.javaserver.gameserver.network.serverpackets;

import net.sf.l2j.gameserver.model.residence.clanhall.ClanHall;

public class ClanHallDecoration extends L2GameServerPacket
{
	private final ClanHall _ch;
	
	public ClanHallDecoration(ClanHall ch)
	{
		_ch = ch;
	}
	
	@Override
	protected final void writeImpl()
	{
		writeC(0xf7);
		writeD(_ch.getId());
		
		// FUNC_RESTORE_HP
		writeC(_ch.getDecoDepth(ClanHall.FUNC_RESTORE_HP));
		
		// FUNC_RESTORE_MP
		writeC(_ch.getDecoDepth(ClanHall.FUNC_RESTORE_MP));
		
		// NOT USED - Statue
		writeC(0);
		
		// FUNC_RESTORE_EXP
		writeC(_ch.getDecoDepth(ClanHall.FUNC_RESTORE_EXP));
		
		// FUNC_TELEPORT
		writeC(_ch.getDecoDepth(ClanHall.FUNC_TELEPORT));
		
		// NOT USED - Crystal
		writeC(0);
		
		// CURTAINS
		writeC(_ch.getDecoDepth(ClanHall.FUNC_DECO_CURTAINS));
		
		// NOT USED - Hangings
		writeC(0);
		
		// FUNC_SUPPORT
		writeC(_ch.getDecoDepth(ClanHall.FUNC_SUPPORT_MAGIC));
		
		// NOT USED - Flag
		writeC(0);
		
		// Front Plateform
		writeC(_ch.getDecoDepth(ClanHall.FUNC_DECO_FIXTURES));
		
		// FUNC_ITEM_CREATE
		writeC(_ch.getDecoDepth(ClanHall.FUNC_CREATE_ITEM));
	}
}