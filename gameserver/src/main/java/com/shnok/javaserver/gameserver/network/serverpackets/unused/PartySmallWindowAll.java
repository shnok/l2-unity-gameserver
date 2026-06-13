package com.shnok.javaserver.gameserver.network.serverpackets.unused;

import com.shnok.javaserver.gameserver.model.actor.Player;
import com.shnok.javaserver.gameserver.model.group.Party;
import com.shnok.javaserver.gameserver.network.serverpackets.L2GameServerPacket;

public final class PartySmallWindowAll extends L2GameServerPacket
{
	private final Party _party;
	private final Player _player;
	private final int _dist;
	private final int _leaderObjectId;
	
	public PartySmallWindowAll(Player player, Party party)
	{
		_player = player;
		_party = party;
		_leaderObjectId = _party.getLeaderObjectId();
		_dist = _party.getLootRule().ordinal();
	}
	
	@Override
	protected final void writeImpl()
	{
		writeC(0x4e);
		writeD(_leaderObjectId);
		writeD(_dist);
		writeD(_party.getMembersCount() - 1);
		
		for (Player player : _party.getMembers())
		{
			if (player == _player)
				continue;
			
			writeD(player.getObjectId());
			writeS(player.getName());
			writeD((int) player.getStatus().getCp());
			writeD(player.getStatus().getMaxCp());
			writeD((int) player.getStatus().getHp());
			writeD(player.getStatus().getMaxHp());
			writeD((int) player.getStatus().getMp());
			writeD(player.getStatus().getMaxMp());
			writeD(player.getStatus().getLevel());
			writeD(player.getClassId().getId());
			writeD(0);// writeD(0x01); ??
			writeD(player.getRace().ordinal());
		}
	}
}