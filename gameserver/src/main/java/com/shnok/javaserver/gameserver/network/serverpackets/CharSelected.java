package com.shnok.javaserver.gameserver.network.serverpackets;

import net.sf.l2j.gameserver.model.actor.Player;
import net.sf.l2j.gameserver.taskmanager.GameTimeTaskManager;

public class CharSelected extends L2GameServerPacket
{
	private final Player _player;
	private final int _sessionId;
	
	public CharSelected(Player player, int sessionId)
	{
		_player = player;
		_sessionId = sessionId;
	}
	
	@Override
	protected final void writeImpl()
	{
		writeC(0x15);
		
		writeS(_player.getName());
		writeD(_player.getObjectId());
		writeS(_player.getTitle());
		writeD(_sessionId);
		writeD(_player.getClanId());
		
		writeD(0x00); // unknown
		
		writeD(_player.getAppearance().getSex().ordinal());
		writeD(_player.getRace().ordinal());
		writeD(_player.getClassId().getId());
		
		writeD(0x01);
		
		writeD(_player.getX());
		writeD(_player.getY());
		writeD(_player.getZ());
		writeF(_player.getStatus().getHp());
		writeF(_player.getStatus().getMp());
		writeD(_player.getStatus().getSp());
		writeQ(_player.getStatus().getExp());
		writeD(_player.getStatus().getLevel());
		writeD(_player.getKarma());
		writeD(_player.getPkKills());
		writeD(_player.getStatus().getINT());
		writeD(_player.getStatus().getSTR());
		writeD(_player.getStatus().getCON());
		writeD(_player.getStatus().getMEN());
		writeD(_player.getStatus().getDEX());
		writeD(_player.getStatus().getWIT());
		
		for (int i = 0; i < 30; i++)
		{
			writeD(0x00);
		}
		
		writeD(0x00); // c3 work
		writeD(0x00); // c3 work
		
		writeD(GameTimeTaskManager.getInstance().getGameTime());
		
		writeD(0x00); // c3
		
		writeD(_player.getClassId().getId());
		
		writeD(0x00); // c3 InspectorBin
		writeD(0x00); // c3
		writeD(0x00); // c3
		writeD(0x00); // c3
	}
}