package com.shnok.javaserver.gameserver.network.serverpackets;

import com.shnok.javaserver.gameserver.model.pledge.Clan;

public class PledgeShowInfoUpdate extends L2GameServerPacket
{
	private final int _clanId;
	private final int _crestId;
	private final int _level;
	private final int _castleId;
	private final int _chId;
	private final int _rank;
	private final int _reputation;
	private final int _dissolution;
	private final int _allyId;
	private final String _allyName;
	private final int _allyCrestId;
	private final int _atWar;
	
	public PledgeShowInfoUpdate(Clan clan)
	{
		_clanId = clan.getClanId();
		_crestId = clan.getCrestId();
		_level = clan.getLevel();
		_castleId = clan.getCastleId();
		_chId = clan.getClanHallId();
		_rank = clan.getRank();
		_reputation = clan.getReputationScore();
		_dissolution = clan.getDissolvingExpiryTime() > 0 ? 3 : 0;
		_allyId = clan.getAllyId();
		_allyName = clan.getAllyName();
		_allyCrestId = clan.getAllyCrestId();
		_atWar = (clan.isAtWar()) ? 1 : 0;
	}
	
	@Override
	protected final void writeImpl()
	{
		writeC(0x88);
		writeD(_clanId);
		writeD(_crestId);
		writeD(_level);
		writeD(_castleId);
		writeD(_chId);
		writeD(_rank);
		writeD(_reputation);
		writeD(_dissolution);
		writeD(0);
		writeD(_allyId);
		writeS(_allyName);
		writeD(_allyCrestId);
		writeD(_atWar);
	}
}