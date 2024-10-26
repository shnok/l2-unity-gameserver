package com.shnok.javaserver.gameserver.network.serverpackets;

import net.sf.l2j.gameserver.model.actor.Creature;
import net.sf.l2j.gameserver.model.actor.attack.CreatureAttack.HitHolder;

public class Attack extends L2GameServerPacket
{
	public static final int HITFLAG_SS = 0x10;
	public static final int HITFLAG_CRIT = 0x20;
	public static final int HITFLAG_SHLD = 0x40;
	public static final int HITFLAG_MISS = 0x80;
	
	private final int _attackerId;
	
	private final int _x;
	private final int _y;
	private final int _z;
	
	private HitHolder[] _hits;
	
	public Attack(Creature attacker, HitHolder[] hits)
	{
		_attackerId = attacker.getObjectId();
		
		_x = attacker.getX();
		_y = attacker.getY();
		_z = attacker.getZ();
		
		_hits = hits;
	}
	
	@Override
	protected final void writeImpl()
	{
		writeC(0x05);
		
		writeD(_attackerId);
		writeD(_hits[0]._targetId);
		writeD(_hits[0]._damage);
		writeC(_hits[0]._flags);
		writeD(_x);
		writeD(_y);
		writeD(_z);
		writeH(_hits.length - 1);
		
		if (_hits.length > 1)
		{
			for (int i = 1; i < _hits.length; i++)
			{
				writeD(_hits[i]._targetId);
				writeD(_hits[i]._damage);
				writeC(_hits[i]._flags);
			}
		}
	}
}