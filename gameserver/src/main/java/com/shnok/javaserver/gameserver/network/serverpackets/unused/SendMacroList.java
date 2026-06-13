package com.shnok.javaserver.gameserver.network.serverpackets.unused;

import com.shnok.javaserver.gameserver.model.Macro;
import com.shnok.javaserver.gameserver.model.records.MacroCmd;
import com.shnok.javaserver.gameserver.network.serverpackets.L2GameServerPacket;

public class SendMacroList extends L2GameServerPacket
{
	private final int _rev;
	private final int _count;
	private final Macro _macro;
	
	public SendMacroList(int rev, int count, Macro macro)
	{
		_rev = rev;
		_count = count;
		_macro = macro;
	}
	
	@Override
	protected final void writeImpl()
	{
		writeC(0xE7);
		
		writeD(_rev); // macro change revision (changes after each macro edition)
		writeC(0); // unknown
		writeC(_count); // count of Macros
		writeC(_macro != null ? 1 : 0); // unknown
		
		if (_macro != null)
		{
			writeD(_macro.id); // Macro ID
			writeS(_macro.name); // Macro Name
			writeS(_macro.descr); // Desc
			writeS(_macro.acronym); // acronym
			writeC(_macro.icon); // icon
			
			writeC(_macro.commands.length); // count
			
			for (int i = 0; i < _macro.commands.length; i++)
			{
				MacroCmd cmd = _macro.commands[i];
				writeC(i + 1); // i of count
				writeC(cmd.type()); // type 1 = skill, 3 = action, 4 = shortcut
				writeD(cmd.d1()); // skill id
				writeC(cmd.d2()); // shortcut id
				writeS(cmd.cmd()); // command name
			}
		}
	}
}