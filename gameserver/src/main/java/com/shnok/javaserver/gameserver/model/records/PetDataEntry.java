package net.sf.l2j.gameserver.model.records;

import net.sf.l2j.commons.data.StatSet;

public record PetDataEntry(long maxExp, int maxMeal, int expType, int mealInBattle, int mealInNormal, double pAtk, double pDef, double mAtk, double mDef, double maxHp, double maxMp, float hpRegen, float mpRegen, int ssCount, int spsCount, int mountMealInBattle, int mountMealInNormal, double mountAtkSpd, double mountPatk, double mountMatk, int mountBaseSpeed, int mountWaterSpeed, int mountFlySpeed)
{
	public PetDataEntry(StatSet set)
	{
		this(set.getLong("exp"), set.getInteger("maxMeal"), set.getInteger("expType"), set.getInteger("mealInBattle"), set.getInteger("mealInNormal"), set.getDouble("pAtk"), set.getDouble("pDef"), set.getDouble("mAtk"), set.getDouble("mDef"), set.getDouble("hp"), set.getDouble("mp"), set.getFloat("hpRegen"), set.getFloat("mpRegen"), set.getInteger("ssCount"), set.getInteger("spsCount"), set.getInteger("mealInBattleOnRide", 0), set.getInteger("mealInNormalOnRide", 0), set.getDouble("atkSpdOnRide", 0.), set.getDouble("pAtkOnRide", 0.), set.getDouble("mAtkOnRide", 0.), set.getInteger("mountBaseSpeed"), set.getInteger("mountWaterSpeed"), set.getInteger("mountFlySpeed"));
	}
}