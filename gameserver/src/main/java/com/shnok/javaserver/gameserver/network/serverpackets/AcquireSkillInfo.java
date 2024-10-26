package com.shnok.javaserver.gameserver.network.serverpackets;

import java.util.ArrayList;
import java.util.List;

import net.sf.l2j.gameserver.model.records.SkillRequirement;

public class AcquireSkillInfo extends L2GameServerPacket
{
	private final List<SkillRequirement> _reqs;
	private final int _id;
	private final int _level;
	private final int _spCost;
	private final int _mode;
	
	public AcquireSkillInfo(int id, int level, int spCost, int mode)
	{
		_reqs = new ArrayList<>();
		_id = id;
		_level = level;
		_spCost = spCost;
		_mode = mode;
	}
	
	public void addRequirement(int type, int id, int count, int unk)
	{
		_reqs.add(new SkillRequirement(type, id, count, unk));
	}
	
	@Override
	protected final void writeImpl()
	{
		writeC(0x8b);
		writeD(_id);
		writeD(_level);
		writeD(_spCost);
		writeD(_mode); // c4
		
		writeD(_reqs.size());
		
		for (SkillRequirement temp : _reqs)
		{
			writeD(temp.type());
			writeD(temp.itemId());
			writeD(temp.count());
			writeD(temp.unk());
		}
	}
}