package net.sf.l2j.gameserver.model.records;

import net.sf.l2j.gameserver.skills.L2Skill;

public record EffectHolder(int id, int level, int duration)
{
	public EffectHolder(L2Skill skill, int period)
	{
		this(skill.getId(), skill.getLevel(), period);
	}
}
