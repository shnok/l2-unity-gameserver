package com.shnok.javaserver.gameserver.network.serverpackets.unused;

import com.shnok.javaserver.gameserver.network.NpcStringId;
import com.shnok.javaserver.gameserver.network.serverpackets.L2GameServerPacket;

public class ExShowScreenMessage extends L2GameServerPacket
{
	public enum SMPOS
	{
		DUMMY,
		TOP_LEFT,
		TOP_CENTER,
		TOP_RIGHT,
		MIDDLE_LEFT,
		MIDDLE_CENTER,
		MIDDLE_RIGHT,
		BOTTOM_CENTER,
		BOTTOM_RIGHT,
	}
	
	private final int _type;
	private final int _sysMsgId;
	private final boolean _showHide;
	private final int _unk2;
	private final int _unk3;
	private final boolean _showFading;
	private final int _size;
	private final SMPOS _position;
	private final boolean _showEffect;
	private final String _text;
	private final int _time;
	
	public ExShowScreenMessage(NpcStringId message, int time)
	{
		this(1, -1, SMPOS.TOP_CENTER, false, 0, 0, 0, false, time, false, message.getMessage());
	}
	
	public ExShowScreenMessage(NpcStringId message, int time, Object... params)
	{
		this(1, -1, SMPOS.TOP_CENTER, false, 0, 0, 0, false, time, false, message.getMessage(params));
	}
	
	public ExShowScreenMessage(String text, int time)
	{
		this(1, -1, SMPOS.TOP_CENTER, false, 0, 0, 0, false, time, false, text);
	}
	
	public ExShowScreenMessage(String text, int time, SMPOS pos, boolean effect)
	{
		this(1, -1, pos, false, 0, 0, 0, effect, time, false, text);
	}
	
	public ExShowScreenMessage(int type, int sysMsgId, SMPOS position, boolean showHide, int size, int unk2, int unk3, boolean showEffect, int time, boolean showFading, String text)
	{
		_type = type;
		_sysMsgId = sysMsgId;
		_position = position;
		_showHide = showHide;
		_size = size;
		_unk2 = unk2;
		_unk3 = unk3;
		_showEffect = showEffect;
		_time = time;
		_showFading = showFading;
		_text = text;
	}
	
	@Override
	protected void writeImpl()
	{
		writeC(0xfe);
		writeH(0x38);
		writeD(_type); // 0 - system messages, 1 - your defined text
		writeD(_sysMsgId); // system message id (_type must be 0 otherwise no effect)
		writeD(_position.ordinal()); // message position
		writeD(_showHide ? 1 : 0); // hide
		writeD(_size); // font size 0 - normal, 1 - small
		writeD(_unk2); // ?
		writeD(_unk3); // ?
		writeD(_showEffect ? 1 : 0); // upper effect (0 - disabled, 1 enabled) - _position must be 2 (center) otherwise no effect
		writeD(_time); // time
		writeD(_showFading ? 1 : 0); // fade effect (0 - disabled, 1 enabled)
		writeS(_text); // your text (_type must be 1, otherwise no effect)
	}
}