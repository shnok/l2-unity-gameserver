package com.shnok.javaserver.gameserver.network.serverpackets.unused;

import com.shnok.javaserver.gameserver.model.actor.Creature;
import com.shnok.javaserver.gameserver.network.serverpackets.L2GameServerPacket;

public class ExFishingHpRegen extends L2GameServerPacket
{
	private final int _objectId;
	private final int _time;
	private final int _fishHp;
	private final int _hpMode;
	private final int _anim;
	private final int _goodUse;
	private final int _penalty;
	private final int _hpBarColor;
	
	public ExFishingHpRegen(Creature creature, int time, int fishHp, int hpMode, int goodUse, int anim, int penalty, int hpBarColor)
	{
		_objectId = creature.getObjectId();
		_time = time;
		_fishHp = fishHp;
		_hpMode = hpMode;
		_goodUse = goodUse;
		_anim = anim;
		_penalty = penalty;
		_hpBarColor = hpBarColor;
	}
	
	@Override
	protected void writeImpl()
	{
		writeC(0xfe);
		writeH(0x16);
		
		writeD(_objectId);
		writeD(_time);
		writeD(_fishHp);
		writeC(_hpMode); // 0 = HP stop, 1 = HP raise
		writeC(_goodUse); // 0 = none, 1 = success, 2 = failed
		writeC(_anim); // Anim: 0 = none, 1 = reeling, 2 = pumping
		writeD(_penalty); // Penalty
		writeC(_hpBarColor); // 0 = normal hp bar, 1 = purple hp bar
	}
}