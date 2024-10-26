package com.shnok.javaserver.gameserver.network.serverpackets;

import net.sf.l2j.gameserver.model.actor.Creature;
import net.sf.l2j.gameserver.model.actor.Npc;

public final class ServerObjectInfo extends L2GameServerPacket
{
	private final Npc _npc;
	
	private final int _idTemplate;
	private final String _name;
	
	private final int _x;
	private final int _y;
	private final int _z;
	private final int _heading;
	
	private final double _collisionHeight;
	private final double _collisionRadius;
	
	private final boolean _isAttackable;
	
	private final int _type;
	private final int _effect;
	
	public ServerObjectInfo(Npc npc, Creature actor, int type, int effect)
	{
		_npc = npc;
		
		_idTemplate = _npc.getTemplate().getIdTemplate();
		_name = _npc.getName();
		
		_x = _npc.getX();
		_y = _npc.getY();
		_z = _npc.getZ();
		_heading = _npc.getHeading();
		
		_collisionHeight = _npc.getCollisionHeight();
		_collisionRadius = _npc.getCollisionRadius();
		
		_isAttackable = _npc.isAttackableBy(actor);
		
		_type = type;
		_effect = effect;
	}
	
	public ServerObjectInfo(Npc npc, Creature actor)
	{
		_npc = npc;
		
		_idTemplate = _npc.getTemplate().getIdTemplate();
		_name = _npc.getName();
		
		_x = _npc.getX();
		_y = _npc.getY();
		_z = _npc.getZ();
		_heading = _npc.getHeading();
		
		_collisionHeight = _npc.getCollisionHeight();
		_collisionRadius = _npc.getCollisionRadius();
		
		_isAttackable = _npc.isAttackableBy(actor);
		
		_type = 1;
		_effect = 0;
	}
	
	@Override
	protected final void writeImpl()
	{
		writeC(0x8C);
		writeD(_npc.getObjectId());
		writeD(_idTemplate + 1000000);
		writeS(_name);
		writeD(_isAttackable ? 1 : 0);
		writeD(_x);
		writeD(_y);
		writeD(_z);
		writeD(_heading);
		writeF(1.0); // movement multiplier
		writeF(1.0); // attack speed multiplier
		writeF(_collisionRadius);
		writeF(_collisionHeight);
		writeD((int) (_isAttackable ? _npc.getStatus().getHp() : 0));
		writeD(_isAttackable ? _npc.getStatus().getMaxHp() : 0);
		writeD(_type);
		writeD(_effect);
	}
}