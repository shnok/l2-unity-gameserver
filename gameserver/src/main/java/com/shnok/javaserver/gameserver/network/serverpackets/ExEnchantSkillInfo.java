package com.shnok.javaserver.gameserver.network.serverpackets;

import java.util.ArrayList;

import net.sf.l2j.gameserver.model.records.SkillRequirement;

public class ExEnchantSkillInfo extends L2GameServerPacket
{
	private final ArrayList<SkillRequirement> _reqs;
	private final int _id;
	private final int _level;
	private final int _spCost;
	private final int _xpCost;
	private final int _rate;
	
	public ExEnchantSkillInfo(int id, int level, int spCost, int xpCost, int rate)
	{
		_reqs = new ArrayList<>();
		_id = id;
		_level = level;
		_spCost = spCost;
		_xpCost = xpCost;
		_rate = rate;
	}
	
	public void addRequirement(int type, int id, int count, int unk)
	{
		_reqs.add(new SkillRequirement(type, id, count, unk));
	}
	
	@Override
	protected void writeImpl()
	{
		writeC(0xfe);
		writeH(0x18);
		
		writeD(_id);
		writeD(_level);
		writeD(_spCost);
		writeQ(_xpCost);
		writeD(_rate);
		
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