package com.shnok.javaserver.gameserver.enums.items;

public enum CrystalType
{
	NONE(0, 0, 0, 0, 0, 0),
	D(1, 1458, 11, 90, 0, 0),
	C(2, 1459, 6, 45, 2130, 20),
	B(3, 1460, 11, 67, 2130, 30),
	A(4, 1461, 19, 144, 2131, 20),
	S(5, 1462, 25, 250, 2131, 25);
	
	private final int _id;
	private final int _crystalId;
	private final int _crystalEnchantBonusArmor;
	private final int _crystalEnchantBonusWeapon;
	private final int _gemstoneId;
	private final int _gemstoneCount;
	
	private CrystalType(int id, int crystalId, int crystalEnchantBonusArmor, int crystalEnchantBonusWeapon, int gemstoneId, int gemstoneCount)
	{
		_id = id;
		_crystalId = crystalId;
		_crystalEnchantBonusArmor = crystalEnchantBonusArmor;
		_crystalEnchantBonusWeapon = crystalEnchantBonusWeapon;
		_gemstoneId = gemstoneId;
		_gemstoneCount = gemstoneCount;
	}
	
	public int getId()
	{
		return _id;
	}
	
	public int getCrystalId()
	{
		return _crystalId;
	}
	
	public int getCrystalEnchantBonusArmor()
	{
		return _crystalEnchantBonusArmor;
	}
	
	public int getCrystalEnchantBonusWeapon()
	{
		return _crystalEnchantBonusWeapon;
	}
	
	public int getGemstoneId()
	{
		return _gemstoneId;
	}
	
	public int getGemstoneCount()
	{
		return _gemstoneCount;
	}
	
	public boolean isGreater(CrystalType crystalType)
	{
		return getId() > crystalType.getId();
	}
	
	public boolean isLesser(CrystalType crystalType)
	{
		return getId() < crystalType.getId();
	}
}