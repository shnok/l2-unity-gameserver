package com.shnok.javaserver.gameserver.model.actor.container.player;

import com.shnok.javaserver.gameserver.data.xml.PlayerLevelData;
import com.shnok.javaserver.gameserver.enums.actors.ClassId;

/**
 * Used to store information about a Player SubClass.
 */
public final class SubClass
{
	private ClassId _class;
	private final int _classIndex;
	private long _exp;
	private int _sp;
	private int _level;
	
	/**
	 * Implicit constructor with all parameters to be set.
	 * @param classId : Class ID of the subclass.
	 * @param classIndex : Class index of the subclass.
	 * @param exp : Exp of the subclass.
	 * @param sp : Sp of the subclass.
	 * @param level : Level of the subclass.
	 */
	public SubClass(int classId, int classIndex, long exp, int sp, byte level)
	{
		_class = ClassId.VALUES[classId];
		_classIndex = classIndex;
		_exp = exp;
		_sp = sp;
		_level = level;
	}
	
	/**
	 * Implicit constructor with default EXP, SP and level parameters.
	 * @param classId : Class ID of the subclass.
	 * @param classIndex : Class index of the subclass.
	 */
	public SubClass(int classId, int classIndex)
	{
		_class = ClassId.VALUES[classId];
		_classIndex = classIndex;
		_exp = PlayerLevelData.getInstance().getPlayerLevel(40).requiredExpToLevelUp();
		_sp = 0;
		_level = 40;
	}
	
	public ClassId getClassDefinition()
	{
		return _class;
	}
	
	public int getClassId()
	{
		return _class.getId();
	}
	
	public void setClassId(int classId)
	{
		_class = ClassId.VALUES[classId];
	}
	
	public int getClassIndex()
	{
		return _classIndex;
	}
	
	public long getExp()
	{
		return _exp;
	}
	
	public void setExp(long exp)
	{
		_exp = Math.min(exp, PlayerLevelData.getInstance().getRequiredExpForHighestLevel());
	}
	
	public int getSp()
	{
		return _sp;
	}
	
	public void setSp(int sp)
	{
		_sp = sp;
	}
	
	public int getLevel()
	{
		return _level;
	}
	
	public void setLevel(int level)
	{
		_level = Math.clamp(level, 40, PlayerLevelData.getInstance().getRealMaxLevel());
	}
}