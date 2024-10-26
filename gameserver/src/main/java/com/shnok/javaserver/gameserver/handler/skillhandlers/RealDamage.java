package com.shnok.javaserver.gameserver.handler.skillhandlers;

import net.sf.l2j.gameserver.enums.skills.SkillType;
import net.sf.l2j.gameserver.handler.ISkillHandler;
import net.sf.l2j.gameserver.model.WorldObject;
import net.sf.l2j.gameserver.model.actor.Creature;
import net.sf.l2j.gameserver.model.item.instance.ItemInstance;
import net.sf.l2j.gameserver.skills.L2Skill;

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