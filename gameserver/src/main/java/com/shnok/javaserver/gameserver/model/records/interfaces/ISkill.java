package com.shnok.javaserver.gameserver.model.records.interfaces;

import net.sf.l2j.gameserver.data.SkillTable;
import net.sf.l2j.gameserver.skills.L2Skill;

public interface ISkill
{
	default L2Skill getL2Skill(int id, int level)
	{
		return SkillTable.getInstance().getInfo(id, level);
	}
	
	public L2Skill getSkill();
}