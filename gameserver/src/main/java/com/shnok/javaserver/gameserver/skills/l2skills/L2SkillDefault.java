package com.shnok.javaserver.gameserver.skills.l2skills;

import net.sf.l2j.commons.data.StatSet;

import net.sf.l2j.gameserver.model.WorldObject;
import net.sf.l2j.gameserver.model.actor.Creature;
import net.sf.l2j.gameserver.network.serverpackets.ActionFailed;
import net.sf.l2j.gameserver.skills.L2Skill;

public class L2SkillDefault extends L2Skill
{
	public L2SkillDefault(StatSet set)
	{
		super(set);
	}
	
	@Override
	public void useSkill(Creature creature, WorldObject[] targets)
	{
		creature.sendPacket(ActionFailed.STATIC_PACKET);
		creature.sendMessage("Skill " + getId() + " [" + getSkillType() + "] isn't implemented.");
	}
}