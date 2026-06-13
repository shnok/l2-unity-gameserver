package com.shnok.javaserver.gameserver.model.records;

import com.shnok.javaserver.commons.data.StatSet;

import com.shnok.javaserver.gameserver.model.records.interfaces.ISkill;
import com.shnok.javaserver.gameserver.skills.L2Skill;

public record NewbieBuff(int skillId, int skillLevel, int lowerLevel, int upperLevel, boolean isMagicClass) implements ISkill
{
	public NewbieBuff(StatSet set)
	{
		this(set.getInteger("skillId"), set.getInteger("skillLevel"), set.getInteger("lowerLevel"), set.getInteger("upperLevel"), set.getBool("isMagicClass"));
	}
	
	@Override
	public L2Skill getSkill()
	{
		return getL2Skill(skillId, skillLevel);
	}
}