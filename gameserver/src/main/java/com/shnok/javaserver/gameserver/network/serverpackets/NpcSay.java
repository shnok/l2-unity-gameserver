package com.shnok.javaserver.gameserver.network.serverpackets;

import net.sf.l2j.gameserver.enums.SayType;
import net.sf.l2j.gameserver.model.actor.Npc;
import net.sf.l2j.gameserver.model.actor.Summon;

public final class NpcSay extends L2GameServerPacket
{
	private final int _objectId;
	private final SayType _type;
	private final int _npcId;
	private final String _text;
	
	/**
	 * For non-existing {@link Npc} instances, while we want to use {@link Npc} name.<br>
	 * Display a {@link Npc}'s name (from client id).
	 * @param npcId : The template ID of {@link Npc}.
	 * @param type : Type of message.
	 * @param text : The message.
	 */
	public NpcSay(int npcId, SayType type, String text)
	{
		_objectId = 0;
		_type = type;
		_npcId = 1000000 + npcId;
		_text = text;
	}
	
	/**
	 * The {@link Npc} saying a message.<br>
	 * Display a {@link Npc}'s name (from client id). Show message above {@link Npc} instance's head.
	 * @param npc : The {@link Npc}.
	 * @param type : Type of message.
	 * @param text : The message.
	 */
	public NpcSay(Npc npc, SayType type, String text)
	{
		_objectId = npc.getObjectId();
		_type = type;
		_npcId = 1000000 + npc.getNpcId();
		_text = text;
	}
	
	/**
	 * The {@link Summon} saying a message.<br>
	 * Display a {@link Npc}'s name (from client id). Show message above {@link Npc} instance's head.
	 * @param summon : The {@link Summon}.
	 * @param type : Type of message.
	 * @param text : The message.
	 */
	public NpcSay(Summon summon, SayType type, String text)
	{
		_objectId = summon.getObjectId();
		_type = type;
		_npcId = 1000000 + summon.getNpcId();
		_text = text;
	}
	
	@Override
	protected final void writeImpl()
	{
		writeC(0x02);
		writeD(_objectId);
		writeD(_type.ordinal());
		writeD(_npcId);
		writeS(_text);
	}
}