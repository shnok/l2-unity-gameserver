package com.shnok.javaserver.gameserver.enums;

public enum MakerSpawnTime
{
	AGIT_BR_START("agit_battle_royal_start"),
	AGIT_FINAL_START("agit_final_start"),
	AGIT_DEF_START("agit_defend_warfare_start"),
	AGIT_ATK_START("agit_attack_warfare_start"),
	SIEGE_START("siege_warfare_start"),
	PC_SIEGE_START("pc_siege_warfare_start"),
	DOOR_OPEN("door_open");
	
	private final String _name;
	
	private MakerSpawnTime(String name)
	{
		_name = name;
	}
	
	public String getName()
	{
		return _name;
	}
	
	public static final MakerSpawnTime[] VALUES = values();
	
	public static MakerSpawnTime getEnumByName(String name)
	{
		for (MakerSpawnTime mst : VALUES)
		{
			if (mst.getName().equalsIgnoreCase(name))
				return mst;
		}
		return null;
	}
}