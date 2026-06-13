package com.shnok.javaserver.gameserver.enums.items;

public enum ArmorType implements ItemType
{
	NONE,
	LIGHT,
	HEAVY,
	MAGIC,
	PET,
	SHIELD;
	
	public static final ArmorType[] VALUES = values();
	
	private final int _mask;
	
	private ArmorType()
	{
		_mask = 1 << (ordinal() + WeaponType.values().length);
	}
	
	/**
	 * Returns the ID of the ArmorType after applying a mask.
	 * @return int : ID of the ArmorType after mask
	 */
	@Override
	public int mask()
	{
		return _mask;
	}
}