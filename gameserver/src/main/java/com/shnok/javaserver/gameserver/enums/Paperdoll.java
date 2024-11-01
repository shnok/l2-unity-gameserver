package com.shnok.javaserver.gameserver.enums;

public enum Paperdoll
{
	NULL(-1),
	UNDER(0),
	LEAR(1),
	REAR(2),
	NECK(3),
	LFINGER(4),
	RFINGER(5),
	HEAD(6),
	RHAND(7),
	LHAND(8),
	GLOVES(9),
	CHEST(10),
	LEGS(11),
	FEET(12),
	CLOAK(13),
	FACE(14),
	HAIR(15),
	HAIRALL(16);
	
	public static final Paperdoll[] VALUES = values();
	public static final int TOTAL_SLOTS = 17;
	
	private final int _id;
	
	private Paperdoll(int id)
	{
		_id = id;
	}
	
	public int getId()
	{
		return _id;
	}
	
	/**
	 * @param name : The {@link String} to test - <b>IT IS CASE INSENSITIVE</b>.
	 * @return The {@link Paperdoll} associated to the {@link String} name, or Paperdoll.NULL (-1) if not found.
	 */
	public static Paperdoll getEnumByName(String name)
	{
		for (Paperdoll paperdoll : VALUES)
		{
			if (paperdoll.toString().equalsIgnoreCase(name))
				return paperdoll;
		}
		return NULL;
	}
	
	/**
	 * @param id : The id to test.
	 * @return The {@link Paperdoll} associated to the id, or Paperdoll.NULL (-1) if not found.
	 */
	public static Paperdoll getEnumById(int id)
	{
		for (Paperdoll paperdoll : VALUES)
		{
			if (paperdoll.getId() == id)
				return paperdoll;
		}
		return NULL;
	}
}