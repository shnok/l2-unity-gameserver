package com.shnok.javaserver.gameserver.model.records;

import com.shnok.javaserver.gameserver.model.records.interfaces.ISkill;
import com.shnok.javaserver.gameserver.skills.L2Skill;

public record BuffSkill(int id, int level, int price, String type, String description) implements ISkill
{
	@Override
	public L2Skill getSkill()
	{
		return getL2Skill(id, level);
	}
}