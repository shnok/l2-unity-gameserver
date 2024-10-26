package com.shnok.javaserver.gameserver.model;

import net.sf.l2j.gameserver.enums.ShortcutType;

public class Shortcut
{
	private final int _slot;
	private final int _page;
	private final int _id;
	private final ShortcutType _type;
	private final int _characterType;
	
	private int _level;
	private int _sharedReuseGroup = -1;
	
	public Shortcut(int slotId, int pageId, ShortcutType type, int id, int level, int characterType)
	{
		_slot = slotId;
		_page = pageId;
		_type = type;
		_id = id;
		_level = level;
		_characterType = characterType;
	}
	
	public int getId()
	{
		return _id;
	}
	
	public int getLevel()
	{
		return _level;
	}
	
	public void setLevel(int level)
	{
		_level = level;
	}
	
	public int getPage()
	{
		return _page;
	}
	
	public int getSlot()
	{
		return _slot;
	}
	
	public ShortcutType getType()
	{
		return _type;
	}
	
	public int getCharacterType()
	{
		return _characterType;
	}
	
	public int getSharedReuseGroup()
	{
		return _sharedReuseGroup;
	}
	
	public void setSharedReuseGroup(int group)
	{
		_sharedReuseGroup = group;
	}
}