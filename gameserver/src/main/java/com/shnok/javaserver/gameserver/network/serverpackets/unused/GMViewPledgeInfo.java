package com.shnok.javaserver.gameserver.network.serverpackets.unused;

import com.shnok.javaserver.gameserver.model.actor.Player;
import com.shnok.javaserver.gameserver.model.pledge.Clan;
import com.shnok.javaserver.gameserver.model.pledge.ClanMember;
import com.shnok.javaserver.gameserver.network.serverpackets.L2GameServerPacket;

public class GMViewPledgeInfo extends L2GameServerPacket
{
	private final Clan _clan;
	private final Player _player;
	
	public GMViewPledgeInfo(Clan clan, Player player)
	{
		_clan = clan;
		_player = player;
	}
	
	@Override
	protected final void writeImpl()
	{
		writeC(0x90);
		writeS(_player.getName());
		writeD(_clan.getClanId());
		writeD(0x00);
		writeS(_clan.getName());
		writeS(_clan.getLeaderName());
		writeD(_clan.getCrestId());
		writeD(_clan.getLevel());
		writeD(_clan.getCastleId());
		writeD(_clan.getClanHallId());
		writeD(_clan.getRank());
		writeD(_clan.getReputationScore());
		writeD(_clan.getDissolvingExpiryTime() > 0 ? 3 : 0);
		writeD(0);
		writeD(_clan.getAllyId());
		writeS(_clan.getAllyName());
		writeD(_clan.getAllyCrestId());
		writeD(_clan.isAtWar() ? 1 : 0);
		writeD(_clan.getMembersCount());
		
		for (ClanMember member : _clan.getMembers())
		{
			writeS(member.getName());
			writeD(member.getLevel());
			writeD(member.getClassId());
			writeD(member.getSex().ordinal());
			writeD(member.getRace().ordinal());
			writeD(member.isOnline() ? member.getObjectId() : 0);
			writeD(member.getSponsor() != 0 ? 1 : 0);
		}
	}
}