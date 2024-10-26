package com.shnok.javaserver.gameserver.network.serverpackets;

import net.sf.l2j.gameserver.model.records.Sequence;

public class SpecialCamera extends L2GameServerPacket
{
	private final int _objectId;
	private final int _dist;
	private final int _yaw;
	private final int _pitch;
	private final int _time;
	private final int _duration;
	private final int _turn;
	private final int _rise;
	private final int _widescreen;
	private final int _unknown;
	
	public SpecialCamera(Sequence sequence)
	{
		this(sequence.objectId(), sequence.dist(), sequence.yaw(), sequence.pitch(), sequence.time(), sequence.duration(), sequence.turn(), sequence.rise(), sequence.widescreen(), 0);
	}
	
	public SpecialCamera(int objectId, int dist, int yaw, int pitch, int time, int duration, int turn, int rise, int widescreen, int unknown)
	{
		_objectId = objectId;
		_dist = dist;
		_yaw = yaw;
		_pitch = pitch;
		_time = time;
		_duration = duration;
		_turn = turn;
		_rise = rise;
		_widescreen = widescreen;
		_unknown = unknown;
	}
	
	@Override
	public void writeImpl()
	{
		writeC(0xc7);
		writeD(_objectId);
		writeD(_dist);
		writeD(_yaw);
		writeD(_pitch);
		writeD(_time);
		writeD(_duration);
		writeD(_turn);
		writeD(_rise);
		writeD(_widescreen);
		writeD(_unknown);
	}
}