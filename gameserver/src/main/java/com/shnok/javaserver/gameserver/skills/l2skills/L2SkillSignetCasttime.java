package com.shnok.javaserver.gameserver.skills.l2skills;

import net.sf.l2j.commons.data.StatSet;

import net.sf.l2j.gameserver.model.WorldObject;
import net.sf.l2j.gameserver.model.actor.Creature;
import net.sf.l2j.gameserver.skills.L2Skill;

public final class L2SkillSignetCasttime extends L2Skill
{
	public final int effectNpcId;
	public final int effectId;
	
	public L2SkillSignetCasttime(StatSet set)
	{
		super(set);
		
		effectNpcId = set.getInteger("effectNpcId", -1);
		effectId = set.getInteger("effectId", -1);
	}
	
	@Override
	public void useSkill(Creature creature, WorldObject[] targets)
	{
		if (creature.isAlikeDead())
			return;
		
		getEffectsSelf(creature);
	}
}