package com.shnok.javaserver.gameserver.model.residence.clanhall;

/**
 * A container used to retain ClanHall bidder informations.<br>
 * <br>
 * A bidder is a Player who bidded on a ClanHall auction. Biggest bidder is rewarded with the ClanHall in the end of auction time.
 */
public class Bidder extends Seller
{
	private long _time;
	
	public Bidder(String name, String clanName, int bid, long time)
	{
		super(name, clanName, bid);
		
		_time = time;
	}
	
	public long getTime()
	{
		return _time;
	}
	
	public void setTime(long time)
	{
		_time = time;
	}
}