package com.shnok.javaserver.gameserver.model.holder.skillnode;

import net.sf.l2j.commons.data.StatSet;

import net.sf.l2j.gameserver.model.holder.IntIntHolder;

/**
 * A generic datatype used to store skills informations for player templates.<br>
 * <br>
 * It extends {@link IntIntHolder} and isn't directly used.
 */
public class SkillNode extends IntIntHolder
{
	private final int _minLvl;
	
	public SkillNode(StatSet set)
	{
		super(set.getInteger("id"), set.getInteger("lvl"));
		
		_minLvl = set.getInteger("minLvl");
	}
	
	public int getMinLvl()
	{
		return _minLvl;
	}
}