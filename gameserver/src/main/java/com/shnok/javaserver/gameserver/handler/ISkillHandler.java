package com.shnok.javaserver.gameserver.handler;

import com.shnok.javaserver.commons.logging.CLogger;

import com.shnok.javaserver.gameserver.enums.skills.SkillType;
import com.shnok.javaserver.gameserver.model.WorldObject;
import com.shnok.javaserver.gameserver.model.actor.Creature;
import com.shnok.javaserver.gameserver.model.item.instance.ItemInstance;
import com.shnok.javaserver.gameserver.skills.L2Skill;

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