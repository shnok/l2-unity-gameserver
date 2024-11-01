package com.shnok.javaserver.gameserver.network.serverpackets.unused;

import com.shnok.javaserver.gameserver.data.xml.RestartPointData;
import com.shnok.javaserver.gameserver.model.actor.Player;
import com.shnok.javaserver.gameserver.model.group.PartyMatchRoom;
import com.shnok.javaserver.gameserver.model.restart.RestartPoint;
import com.shnok.javaserver.gameserver.network.serverpackets.L2GameServerPacket;

public class ExManagePartyRoomMember extends L2GameServerPacket
{
	private final Player _player;
	
	private final int _mode;
	private final int _bbs;
	private final int _status;
	
	public ExManagePartyRoomMember(Player player, PartyMatchRoom room, int mode)
	{
		_player = player;
		_mode = mode;
		
		final RestartPoint rp = RestartPointData.getInstance().getRestartPoint(_player);
		_bbs = (rp == null) ? 100 : rp.getBbs();
		
		if (room.isLeader(_player))
			_status = 1;
		else
		{
			if ((room.getLeader().isInParty() && _player.isInParty()) && (room.getLeader().getParty().getLeaderObjectId() == _player.getParty().getLeaderObjectId()))
				_status = 2;
			else
				_status = 0;
		}
	}
	
	@Override
	protected void writeImpl()
	{
		writeC(0xfe);
		writeH(0x10);
		
		writeD(_mode);
		writeD(_player.getObjectId());
		writeS(_player.getName());
		writeD(_player.getActiveClass());
		writeD(_player.getStatus().getLevel());
		writeD(_bbs);
		writeD(_status);
	}
}