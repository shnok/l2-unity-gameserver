package net.sf.l2j.gameserver.model.records;

import net.sf.l2j.commons.data.StatSet;

import net.sf.l2j.gameserver.taskmanager.GameTimeTaskManager;

public record Fish(int id, int level, int hp, int hpRegen, int type, int group, int guts, int gutsCheckTime, int waitTime, int combatTime)
{
	public Fish(StatSet set)
	{
		this(set.getInteger("id"), set.getInteger("level"), set.getInteger("hp"), set.getInteger("hpRegen"), set.getInteger("type"), set.getInteger("group"), set.getInteger("guts"), set.getInteger("gutsCheckTime"), set.getInteger("waitTime"), set.getInteger("combatTime"));
	}
	
	public int getType(boolean isLureNight)
	{
		if (!GameTimeTaskManager.getInstance().isNight() && isLureNight)
			return -1;
		
		return type();
	}
}