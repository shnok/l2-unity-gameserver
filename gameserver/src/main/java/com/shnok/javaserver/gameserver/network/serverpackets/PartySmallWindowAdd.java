package com.shnok.javaserver.gameserver.network.serverpackets;

import com.shnok.javaserver.gameserver.model.actor.Player;
import com.shnok.javaserver.gameserver.model.group.Party;

public final class PartySmallWindowAdd extends L2GameServerPacket
{
	private final Player _player;
	private final int _leaderId;
	private final int _distribution;
	
	public PartySmallWindowAdd(Player player, Party party)
	{
		_player = player;
		_leaderId = party.getLeaderObjectId();
		_distribution = party.getLootRule().ordinal();
	}
	
	@Override
	protected final void writeImpl()
	{
		writeC(0x4f);
		writeD(_leaderId);
		writeD(_distribution);
		writeD(_player.getObjectId());
		writeS(_player.getName());
		writeD((int) _player.getStatus().getCp());
		writeD(_player.getStatus().getMaxCp());
		writeD((int) _player.getStatus().getHp());
		writeD(_player.getStatus().getMaxHp());
		writeD((int) _player.getStatus().getMp());
		writeD(_player.getStatus().getMaxMp());
		writeD(_player.getStatus().getLevel());
		writeD(_player.getClassId().getId());
		writeD(0);// writeD(0x01); ??
		writeD(0);
	}
}