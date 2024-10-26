package com.shnok.javaserver.gameserver.network.serverpackets;

import net.sf.l2j.gameserver.model.actor.instance.StaticObject;

public class StaticObjectInfo extends L2GameServerPacket
{
	private final StaticObject _staticObject;
	
	public StaticObjectInfo(StaticObject staticObject)
	{
		_staticObject = staticObject;
	}
	
	@Override
	protected final void writeImpl()
	{
		writeC(0x99);
		writeD(_staticObject.getStaticObjectId());
		writeD(_staticObject.getObjectId());
	}
}