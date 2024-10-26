package com.shnok.javaserver.gameserver.model.records;

import com.shnok.javaserver.gameserver.skills.L2Skill;

public record EffectHolder(int id, int level, int duration)
{
	public EffectHolder(L2Skill skill, int period)
	{
		this(skill.getId(), skill.getLevel(), period);
	}
}
