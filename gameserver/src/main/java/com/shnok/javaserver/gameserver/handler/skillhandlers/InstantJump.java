package com.shnok.javaserver.gameserver.handler.skillhandlers;

import com.shnok.javaserver.commons.math.MathUtil;

import com.shnok.javaserver.gameserver.enums.skills.SkillType;
import com.shnok.javaserver.gameserver.handler.ISkillHandler;
import com.shnok.javaserver.gameserver.model.WorldObject;
import com.shnok.javaserver.gameserver.model.actor.Creature;
import com.shnok.javaserver.gameserver.model.item.instance.ItemInstance;
import com.shnok.javaserver.gameserver.network.serverpackets.ValidateLocation;
import com.shnok.javaserver.gameserver.skills.L2Skill;

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