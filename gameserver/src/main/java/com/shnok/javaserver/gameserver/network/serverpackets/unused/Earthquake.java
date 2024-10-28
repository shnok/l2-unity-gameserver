package com.shnok.javaserver.gameserver.network.serverpackets.unused;

import com.shnok.javaserver.gameserver.model.WorldObject;
import com.shnok.javaserver.gameserver.model.location.Location;
import com.shnok.javaserver.gameserver.network.serverpackets.L2GameServerPacket;

public class Earthquake extends L2GameServerPacket
{
	private final Location _loc;
	private final int _intensity;
	private final int _duration;
	private final int _isNpc;
	
	public Earthquake(WorldObject object, int intensity, int duration, boolean isNpc)
	{
		_loc = object.getPosition().clone();
		_intensity = intensity;
		_duration = duration;
		_isNpc = (isNpc) ? 1 : 0;
	}
	
	public Earthquake(WorldObject object, int intensity, int duration)
	{
		_loc = object.getPosition().clone();
		_intensity = intensity;
		_duration = duration;
		_isNpc = 0;
	}
	
	@Override
	protected final void writeImpl()
	{
		writeC(0xc4);
		writeLoc(_loc);
		writeD(_intensity);
		writeD(_duration);
		writeD(_isNpc);
	}
}