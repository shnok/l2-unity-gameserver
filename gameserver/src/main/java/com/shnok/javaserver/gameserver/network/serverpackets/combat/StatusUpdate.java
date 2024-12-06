package com.shnok.javaserver.gameserver.network.serverpackets.combat;

import java.util.ArrayList;
import java.util.List;

import com.shnok.javaserver.gameserver.enums.StatusType;
import com.shnok.javaserver.gameserver.model.WorldObject;
import com.shnok.javaserver.gameserver.model.holder.IntIntHolder;
import com.shnok.javaserver.gameserver.network.serverpackets.L2GameServerPacket;

public class StatusUpdate extends L2GameServerPacket
{
	private final int _objectId;
	private final List<IntIntHolder> _attributes;
	
	public StatusUpdate(WorldObject object)
	{
		_attributes = new ArrayList<>();
		_objectId = object.getObjectId();
	}
	
	public void addAttribute(StatusType type, int level)
	{
//		System.out.println("NEW STATUS UPDATE: " + type);
		_attributes.add(new IntIntHolder(type.getId(), level));
	}
	
	@Override
	protected final void writeImpl()
	{
		writeC(0x0e);
		writeD(_objectId);
		writeD(_attributes.size());
		
		for (IntIntHolder temp : _attributes)
		{
			writeD(temp.getId());
			writeD(temp.getValue());
		}
	}
}
