package net.sf.l2j.gameserver.model.records;

import net.sf.l2j.gameserver.model.records.interfaces.ISkill;
import net.sf.l2j.gameserver.skills.L2Skill;

public record BuffSkill(int id, int level, int price, String type, String description) implements ISkill
{
	@Override
	public L2Skill getSkill()
	{
		return getL2Skill(id, level);
	}
}