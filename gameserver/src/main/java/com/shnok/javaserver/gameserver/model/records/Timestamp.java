package net.sf.l2j.gameserver.model.records;

import net.sf.l2j.gameserver.skills.L2Skill;

public record Timestamp(int skillId, int skillLevel, long reuse, long stamp)
{
	public Timestamp(L2Skill skill, long reuse2)
	{
		this(skill.getId(), skill.getLevel(), reuse2, System.currentTimeMillis() + reuse2);
	}
	
	public Timestamp(L2Skill skill, long reuse2, long stamp2)
	{
		this(skill.getId(), skill.getLevel(), reuse2, stamp2);
	}
	
	public long getRemaining()
	{
		return Math.max(stamp - System.currentTimeMillis(), 0);
	}
	
	public boolean hasNotPassed()
	{
		return System.currentTimeMillis() < stamp;
	}
}