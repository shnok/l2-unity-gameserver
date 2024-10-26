package com.shnok.javaserver.gameserver.network.serverpackets;

import net.sf.l2j.gameserver.model.actor.Player;

public class PledgeShowMemberListAdd extends L2GameServerPacket
{
	private final String _name;
	private final int _lvl;
	private final int _classId;
	private final int _isOnline;
	private final int _pledgeType;
	private final int _race;
	private final int _sex;
	
	public PledgeShowMemberListAdd(Player player)
	{
		_name = player.getName();
		_lvl = player.getStatus().getLevel();
		_classId = player.getClassId().getId();
		_isOnline = (player.isOnline()) ? player.getObjectId() : 0;
		_pledgeType = player.getPledgeType();
		_race = player.getRace().ordinal();
		_sex = player.getAppearance().getSex().ordinal();
	}
	
	@Override
	protected final void writeImpl()
	{
		writeC(0x55);
		writeS(_name);
		writeD(_lvl);
		writeD(_classId);
		writeD(_sex);
		writeD(_race);
		writeD(_isOnline);
		writeD(_pledgeType);
	}
}