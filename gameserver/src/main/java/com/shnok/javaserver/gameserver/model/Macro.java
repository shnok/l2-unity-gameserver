package com.shnok.javaserver.gameserver.model;

import java.util.Arrays;

import com.shnok.javaserver.gameserver.model.records.MacroCmd;

/**
 * Macros are used to automate some processes, to perform a series of actions or to use several skills at once.
 */
public class Macro
{
	public static final int CMD_TYPE_SKILL = 1;
	public static final int CMD_TYPE_ACTION = 3;
	public static final int CMD_TYPE_SHORTCUT = 4;
	
	public int id;
	public final int icon;
	public final String name;
	public final String descr;
	public final String acronym;
	public final MacroCmd[] commands;
	
	public Macro(int pId, int pIcon, String pName, String pDescr, String pAcronym, MacroCmd[] pCommands)
	{
		id = pId;
		icon = pIcon;
		name = pName;
		descr = pDescr;
		acronym = pAcronym;
		commands = pCommands;
	}
	
	@Override
	public String toString()
	{
		return "Macro [id=" + id + ", icon=" + icon + ", name=" + name + ", descr=" + descr + ", acronym=" + acronym + ", commands=" + Arrays.toString(commands) + "]";
	}
}