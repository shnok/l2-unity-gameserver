package com.shnok.javaserver.gameserver.network.serverpackets;

import com.shnok.javaserver.gameserver.model.group.Party;

public class ExMPCCPartyInfoUpdate extends L2GameServerPacket
{
	private final String _name;
	private final int _leaderObjectId;
	private final int _membersCount;
	private final int _mode;
	
	/**
	 * @param party
	 * @param mode 0 = Remove, 1 = Add
	 */
	public ExMPCCPartyInfoUpdate(Party party, int mode)
	{
		_name = party.getLeader().getName();
		_leaderObjectId = party.getLeaderObjectId();
		_membersCount = party.getMembersCount();
		_mode = mode;
	}
	
	@Override
	protected void writeImpl()
	{
		writeC(0xFE);
		writeH(0x5A);
		writeS(_name);
		writeD(_leaderObjectId);
		writeD(_membersCount);
		writeD(_mode);
	}
}