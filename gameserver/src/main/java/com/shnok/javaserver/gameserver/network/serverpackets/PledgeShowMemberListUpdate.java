package com.shnok.javaserver.gameserver.network.serverpackets;

import net.sf.l2j.gameserver.model.actor.Player;
import net.sf.l2j.gameserver.model.pledge.ClanMember;

public final class PledgeShowMemberListUpdate extends L2GameServerPacket
{
	private final int _pledgeType;
	private final int _hasSponsor;
	private final String _name;
	private final int _level;
	private final int _classId;
	private final int _isOnline;
	private final int _race;
	private final int _sex;
	
	public PledgeShowMemberListUpdate(Player player)
	{
		_pledgeType = player.getPledgeType();
		_hasSponsor = (player.getSponsor() != 0 || player.getApprentice() != 0) ? 1 : 0;
		_name = player.getName();
		_level = player.getStatus().getLevel();
		_classId = player.getClassId().getId();
		_race = player.getRace().ordinal();
		_sex = player.getAppearance().getSex().ordinal();
		_isOnline = (player.isOnline()) ? player.getObjectId() : 0;
	}
	
	public PledgeShowMemberListUpdate(ClanMember member)
	{
		_name = member.getName();
		_level = member.getLevel();
		_classId = member.getClassId();
		_isOnline = (member.isOnline()) ? member.getObjectId() : 0;
		_pledgeType = member.getPledgeType();
		_hasSponsor = (member.getSponsor() != 0 || member.getApprentice() != 0) ? 1 : 0;
		
		if (_isOnline != 0)
		{
			_race = member.getPlayerInstance().getRace().ordinal();
			_sex = member.getPlayerInstance().getAppearance().getSex().ordinal();
		}
		else
		{
			_sex = 0;
			_race = 0;
		}
	}
	
	@Override
	protected final void writeImpl()
	{
		writeC(0x54);
		writeS(_name);
		writeD(_level);
		writeD(_classId);
		writeD(_sex);
		writeD(_race);
		writeD(_isOnline);
		writeD(_pledgeType);
		writeD(_hasSponsor);
	}
}