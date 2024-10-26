package com.shnok.javaserver.gameserver.network.serverpackets;

import net.sf.l2j.gameserver.model.actor.Playable;

public class RelationChanged extends L2GameServerPacket
{
	public static final int RELATION_PVP_FLAG = 0x00002; // pvp ???
	public static final int RELATION_HAS_KARMA = 0x00004; // karma ???
	public static final int RELATION_LEADER = 0x00080; // leader
	public static final int RELATION_INSIEGE = 0x00200; // true if in siege
	public static final int RELATION_ATTACKER = 0x00400; // true when attacker
	public static final int RELATION_ALLY = 0x00800; // blue siege icon, cannot have if red
	public static final int RELATION_ENEMY = 0x01000; // true when red icon, doesn't matter with blue
	public static final int RELATION_MUTUAL_WAR = 0x08000; // double fist
	public static final int RELATION_1SIDED_WAR = 0x10000; // single fist
	
	private final int _objectId;
	private final int _relation;
	private final int _autoAttackable;
	private final int _karma;
	private final int _pvpFlag;
	
	public RelationChanged(Playable playable, int relation, boolean isAutoAttackable)
	{
		_objectId = playable.getObjectId();
		_relation = relation;
		_autoAttackable = (isAutoAttackable) ? 1 : 0;
		_karma = playable.getKarma();
		_pvpFlag = playable.getPvpFlag();
	}
	
	@Override
	protected final void writeImpl()
	{
		writeC(0xce);
		writeD(_objectId);
		writeD(_relation);
		writeD(_autoAttackable);
		writeD(_karma);
		writeD(_pvpFlag);
	}
}