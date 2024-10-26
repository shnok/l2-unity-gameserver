package com.shnok.javaserver.gameserver.network.serverpackets;

import net.sf.l2j.gameserver.model.actor.Player;
import net.sf.l2j.gameserver.model.actor.instance.Door;

public class DoorInfo extends L2GameServerPacket
{
	private final Player _player;
	private final Door _door;
	private final boolean _showHp;
	
	public DoorInfo(Player player, Door door)
	{
		_player = player;
		_door = door;
		_showHp = door.isAttackableBy(_player);
	}
	
	@Override
	protected final void writeImpl()
	{
		writeC(0x4c);
		writeD(_door.getObjectId());
		writeD(_door.getDoorId());
		writeD((_showHp) ? 1 : 0);
		writeD(1); // ??? (can target)
		writeD(_door.isOpened() ? 0 : 1);
		writeD(_door.getStatus().getMaxHp());
		writeD((int) _door.getStatus().getHp());
		writeD(0); // ??? (show HP)
		writeD(0); // ??? (Damage)
	}
}