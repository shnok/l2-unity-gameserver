package com.shnok.javaserver.gameserver.handler;

import net.sf.l2j.commons.logging.CLogger;

import net.sf.l2j.gameserver.enums.skills.SkillType;
import net.sf.l2j.gameserver.model.WorldObject;
import net.sf.l2j.gameserver.model.actor.Creature;
import net.sf.l2j.gameserver.model.item.instance.ItemInstance;
import net.sf.l2j.gameserver.skills.L2Skill;

public interface ISkillHandler
{
	public static final CLogger LOGGER = new CLogger(ISkillHandler.class.getName());
	
	/**
	 * The worker method called by a {@link Creature} when using a {@link L2Skill}.
	 * @param creature : The {@link Creature} who uses that {@link L2Skill}.
	 * @param skill : The casted {@link L2Skill}.
	 * @param targets : The eventual targets, as {@link WorldObject} array.
	 * @param item : The eventual {@link ItemInstance} used for skill cast.
	 */
	public void useSkill(Creature creature, L2Skill skill, WorldObject[] targets, ItemInstance item);
	
	/**
	 * @return Attached {@link SkillType}s to this {@link ISkillHandler}.
	 */
	public SkillType[] getSkillIds();
}