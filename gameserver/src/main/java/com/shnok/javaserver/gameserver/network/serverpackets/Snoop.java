package com.shnok.javaserver.gameserver.network.serverpackets;

public class Snoop extends L2GameServerPacket
{
	private final int _convoId;
	private final String _name;
	private final int _type;
	private final String _speaker;
	private final String _msg;
	
	public Snoop(int id, String name, int type, String speaker, String msg)
	{
		_convoId = id;
		_name = name;
		_type = type;
		_speaker = speaker;
		_msg = msg;
	}
	
	@Override
	protected void writeImpl()
	{
		writeC(0xd5);
		
		writeD(_convoId);
		writeS(_name);
		writeD(0x00); // ??
		writeD(_type);
		writeS(_speaker);
		writeS(_msg);
	}
}