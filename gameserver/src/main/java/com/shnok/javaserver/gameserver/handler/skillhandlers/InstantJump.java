package com.shnok.javaserver.gameserver.handler.skillhandlers;

import net.sf.l2j.commons.math.MathUtil;

import net.sf.l2j.gameserver.enums.skills.SkillType;
import net.sf.l2j.gameserver.handler.ISkillHandler;
import net.sf.l2j.gameserver.model.WorldObject;
import net.sf.l2j.gameserver.model.actor.Creature;
import net.sf.l2j.gameserver.model.item.instance.ItemInstance;
import net.sf.l2j.gameserver.network.serverpackets.ValidateLocation;
import net.sf.l2j.gameserver.skills.L2Skill;

public class InstantJump implements ISkillHandler
{
	private static final SkillType[] SKILL_IDS =
	{
		SkillType.INSTANT_JUMP
	};
	
	@Override
	public void useSkill(Creature creature, L2Skill skill, WorldObject[] targets, ItemInstance item)
	{
		if (!(targets[0] instanceof Creature targetCreature))
			return;
		
		double ph = MathUtil.convertHeadingToDegree(targetCreature.getHeading());
		ph += 180;
		
		if (ph > 360)
			ph -= 360;
		
		ph = (Math.PI * ph) / 180;
		
		final int x = (int) (targetCreature.getX() + (25 * Math.cos(ph)));
		final int y = (int) (targetCreature.getY() + (25 * Math.sin(ph)));
		
		// Abort attack, cast and move.
		creature.abortAll(false);
		
		// Teleport the actor.
		creature.setXYZ(x, y, targetCreature.getZ());
		creature.broadcastPacket(new ValidateLocation(creature));
	}
	
	@Override
	public SkillType[] getSkillIds()
	{
		return SKILL_IDS;
	}
}