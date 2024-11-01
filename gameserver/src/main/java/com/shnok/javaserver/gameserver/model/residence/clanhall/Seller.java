package com.shnok.javaserver.gameserver.model.residence.clanhall;

import com.shnok.javaserver.gameserver.data.sql.ClanTable;
import com.shnok.javaserver.gameserver.model.pledge.Clan;

/**
 * A container used to retain ClanHall seller informations.<br>
 * <br>
 * A seller is a Player who registers a ClanHall on auction system. He is normally the Clan leader who sells his ClanHall.
 */
public class Seller
{
	private final String _name;
	private final String _clanName;
	
	private int _bid;
	
	public Seller(String name, String clanName, int bid)
	{
		_name = name;
		_clanName = clanName;
		_bid = bid;
	}
	
	public String getName()
	{
		return _name;
	}
	
	public String getClanName()
	{
		return _clanName;
	}
	
	public int getBid()
	{
		return _bid;
	}
	
	public void setBid(int bid)
	{
		_bid = bid;
	}
	
	public Clan getClan()
	{
		return ClanTable.getInstance().getClanByName(_clanName);
	}
}