package net.sf.l2j.gameserver.model.records;

import net.sf.l2j.commons.data.StatSet;

import net.sf.l2j.gameserver.model.records.interfaces.ISkill;
import net.sf.l2j.gameserver.skills.L2Skill;

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