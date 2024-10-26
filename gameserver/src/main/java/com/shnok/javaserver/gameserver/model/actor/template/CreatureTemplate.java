package com.shnok.javaserver.gameserver.model.actor.template;

import net.sf.l2j.commons.data.StatSet;

/**
 * The generic datatype used by any character template. It holds basic informations, such as base stats (STR, CON, DEX,...) and extended stats (power attack, magic attack, hp/mp regen, collision values).
 */
public class CreatureTemplate
{
	private final int _baseSTR;
	private final int _baseCON;
	private final int _baseDEX;
	private final int _baseINT;
	private final int _baseWIT;
	private final int _baseMEN;
	
	private final double _baseHpMax;
	private final double _baseMpMax;
	
	private final double _baseHpRegen;
	private final double _baseMpRegen;
	
	private final double _basePAtk;
	private final double _baseMAtk;
	private final double _basePDef;
	private final double _baseMDef;
	
	private final double _basePAtkSpd;
	
	private final double _baseCritRate;
	
	private final double _baseWalkSpd;
	private final double _baseRunSpd;
	
	protected final double _collisionRadius;
	protected final double _collisionHeight;
	
	public CreatureTemplate(StatSet set)
	{
		_baseSTR = set.getInteger("str", 40);
		_baseCON = set.getInteger("con", 21);
		_baseDEX = set.getInteger("dex", 30);
		_baseINT = set.getInteger("int", 20);
		_baseWIT = set.getInteger("wit", 43);
		_baseMEN = set.getInteger("men", 20);
		
		_baseHpMax = set.getDouble("hp", 0.);
		_baseMpMax = set.getDouble("mp", 0.);
		
		_baseHpRegen = set.getDouble("hpRegen", 1.5);
		_baseMpRegen = set.getDouble("mpRegen", 0.9);
		
		_basePAtk = set.getDouble("pAtk");
		_baseMAtk = set.getDouble("mAtk");
		_basePDef = set.getDouble("pDef");
		_baseMDef = set.getDouble("mDef");
		
		_basePAtkSpd = set.getDouble("atkSpd", 300.);
		
		_baseCritRate = set.getDouble("crit", 4.);
		
		_baseWalkSpd = set.getDouble("walkSpd", 0.);
		_baseRunSpd = set.getDouble("runSpd", 1.);
		
		_collisionRadius = set.getDouble("radius");
		_collisionHeight = set.getDouble("height");
	}
	
	public final int getBaseSTR()
	{
		return _baseSTR;
	}
	
	public final int getBaseCON()
	{
		return _baseCON;
	}
	
	public final int getBaseDEX()
	{
		return _baseDEX;
	}
	
	public final int getBaseINT()
	{
		return _baseINT;
	}
	
	public final int getBaseWIT()
	{
		return _baseWIT;
	}
	
	public final int getBaseMEN()
	{
		return _baseMEN;
	}
	
	public double getBaseHpMax(int level)
	{
		return _baseHpMax;
	}
	
	public double getBaseMpMax(int level)
	{
		return _baseMpMax;
	}
	
	public double getBaseHpRegen(int level)
	{
		return _baseHpRegen;
	}
	
	public double getBaseMpRegen(int level)
	{
		return _baseMpRegen;
	}
	
	public final double getBasePAtk()
	{
		return _basePAtk;
	}
	
	public final double getBaseMAtk()
	{
		return _baseMAtk;
	}
	
	public final double getBasePDef()
	{
		return _basePDef;
	}
	
	public final double getBaseMDef()
	{
		return _baseMDef;
	}
	
	public final double getBasePAtkSpd()
	{
		return _basePAtkSpd;
	}
	
	public final double getBaseCritRate()
	{
		return _baseCritRate;
	}
	
	public final double getBaseWalkSpeed()
	{
		return _baseWalkSpd;
	}
	
	public final double getBaseRunSpeed()
	{
		return _baseRunSpd;
	}
	
	public final double getCollisionRadius()
	{
		return _collisionRadius;
	}
	
	public final double getCollisionHeight()
	{
		return _collisionHeight;
	}
}