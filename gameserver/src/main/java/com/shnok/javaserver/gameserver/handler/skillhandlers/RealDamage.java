package com.shnok.javaserver.gameserver.handler.skillhandlers;

import com.shnok.javaserver.gameserver.enums.skills.SkillType;
import com.shnok.javaserver.gameserver.handler.ISkillHandler;
import com.shnok.javaserver.gameserver.model.WorldObject;
import com.shnok.javaserver.gameserver.model.actor.Creature;
import com.shnok.javaserver.gameserver.model.item.instance.ItemInstance;
import com.shnok.javaserver.gameserver.skills.L2Skill;

public class RealDamage implements ISkillHandler
{
	private static final SkillType[] SKILL_IDS =
	{
		SkillType.REAL_DAMAGE
	};
	
	@Override
	public void useSkill(Creature creature, L2Skill skill, WorldObject[] targets, ItemInstance item)
	{
		for (WorldObject target : targets)
		{
			if (!(target instanceof Creature targetCreature) || targetCreature.isDead())
				continue;
			
			final double hpLeft = targetCreature.getStatus().getHp() - skill.getPower();
			if (hpLeft <= 0d)
				targetCreature.doDie(creature);
			else
				targetCreature.getStatus().setHp(hpLeft, true);
		}
	}
	
	@Override
	public SkillType[] getSkillIds()
	{
		return SKILL_IDS;
	}
}