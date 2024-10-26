package com.shnok.javaserver.gameserver.handler;

import com.shnok.javaserver.gameserver.enums.skills.SkillTargetType;
import com.shnok.javaserver.gameserver.model.WorldObject;
import com.shnok.javaserver.gameserver.model.actor.Creature;
import com.shnok.javaserver.gameserver.model.actor.Playable;
import com.shnok.javaserver.gameserver.skills.L2Skill;

public interface ITargetHandler
{
	static final Creature[] EMPTY_TARGET_ARRAY = new Creature[0];
	
	/**
	 * @return The associated {@link SkillTargetType}.
	 */
	public SkillTargetType getTargetType();
	
	/**
	 * The worker method called by a {@link Creature} when using a {@link L2Skill}.
	 * @param caster : The {@link Creature} used as caster.
	 * @param target : The {@link Creature} used as target.
	 * @param skill : The {@link L2Skill} to cast.
	 * @return The array of valid {@link WorldObject} targets, based on the {@link Creature} caster, {@link Creature} target and {@link L2Skill} set as parameters.
	 */
	public Creature[] getTargetList(Creature caster, Creature target, L2Skill skill);
	
	/**
	 * @param caster : The {@link Creature} used as caster.
	 * @param target : The {@link Creature} used as target.
	 * @param skill : The {@link L2Skill} to cast.
	 * @return The real {@link Creature} target.
	 */
	public Creature getFinalTarget(Creature caster, Creature target, L2Skill skill);
	
	/**
	 * @param caster : The {@link Playable} used as caster.
	 * @param target : The {@link Creature} used as target.
	 * @param skill : The {@link L2Skill} to cast.
	 * @param isCtrlPressed : If True, we use specific CTRL rules.
	 * @return True if casting is possible, false otherwise.
	 */
	public boolean meetCastConditions(Playable caster, Creature target, L2Skill skill, boolean isCtrlPressed);
}