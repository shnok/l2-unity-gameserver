package com.shnok.javaserver.gameserver.skills.l2skills;

import com.shnok.javaserver.commons.data.StatSet;

import com.shnok.javaserver.gameserver.model.WorldObject;
import com.shnok.javaserver.gameserver.model.actor.Creature;
import com.shnok.javaserver.gameserver.skills.L2Skill;

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