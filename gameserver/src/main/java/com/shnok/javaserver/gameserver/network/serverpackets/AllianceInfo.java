package com.shnok.javaserver.gameserver.network.serverpackets;

import java.util.Collection;

import com.shnok.javaserver.gameserver.data.sql.ClanTable;
import com.shnok.javaserver.gameserver.model.pledge.Clan;
import com.shnok.javaserver.gameserver.model.pledge.ClanInfo;

public class AllianceInfo extends L2GameServerPacket
{
	private final String _name;
	private final int _total;
	private final int _online;
	private final String _clanName;
	private final String _leaderName;
	private final ClanInfo[] _allies;
	
	public AllianceInfo(int allianceId)
	{
		final Clan allianceClanLeader = ClanTable.getInstance().getClan(allianceId);
		
		_name = allianceClanLeader.getAllyName();
		_clanName = allianceClanLeader.getName();
		_leaderName = allianceClanLeader.getLeaderName();
		
		final Collection<Clan> allies = ClanTable.getInstance().getClanAllies(allianceId);
		
		_allies = new ClanInfo[allies.size()];
		
		int idx = 0;
		int total = 0;
		int online = 0;
		
		for (final Clan clan : allies)
		{
			final ClanInfo ci = new ClanInfo(clan);
			
			_allies[idx++] = ci;
			
			total += ci.getTotal();
			online += ci.getOnline();
		}
		
		_total = total;
		_online = online;
	}
	
	@Override
	protected void writeImpl()
	{
		writeC(0xb4);
		
		writeS(_name);
		writeD(_total);
		writeD(_online);
		writeS(_clanName);
		writeS(_leaderName);
		
		writeD(_allies.length);
		for (final ClanInfo aci : _allies)
		{
			writeS(aci.getClan().getName());
			writeD(0x00);
			writeD(aci.getClan().getLevel());
			writeS(aci.getClan().getLeaderName());
			writeD(aci.getTotal());
			writeD(aci.getOnline());
		}
	}
	
	public String getName()
	{
		return _name;
	}
	
	public int getTotal()
	{
		return _total;
	}
	
	public int getOnline()
	{
		return _online;
	}
	
	public String getClanName()
	{
		return _clanName;
	}
	
	public String getLeaderName()
	{
		return _leaderName;
	}
	
	public ClanInfo[] getAllies()
	{
		return _allies;
	}
}