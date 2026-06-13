package com.shnok.javaserver.gameserver.handler.skillhandlers;

import com.shnok.javaserver.gameserver.enums.skills.SkillType;
import com.shnok.javaserver.gameserver.handler.ISkillHandler;
import com.shnok.javaserver.gameserver.model.WorldObject;
import com.shnok.javaserver.gameserver.model.actor.Creature;
import com.shnok.javaserver.gameserver.model.actor.Player;
import com.shnok.javaserver.gameserver.model.actor.instance.Monster;
import com.shnok.javaserver.gameserver.model.item.instance.ItemInstance;
import com.shnok.javaserver.gameserver.scripting.QuestState;
import com.shnok.javaserver.gameserver.skills.L2Skill;

public class DrainSoul implements ISkillHandler
{
	private static final String qn = "Q350_EnhanceYourWeapon";
	
	private static final SkillType[] SKILL_IDS =
	{
		SkillType.DRAIN_SOUL
	};
	
	@Override
	public void useSkill(Creature creature, L2Skill skill, WorldObject[] targets, ItemInstance item)
	{
		// Check player.
		if (!(creature instanceof Player player) || creature.isDead())
			return;
		
		// Check quest condition.
		final QuestState st = player.getQuestList().getQuestState(qn);
		if (st == null || !st.isStarted())
			return;
		
		// Get target.
		if (!(targets[0] instanceof Monster targetMonster))
			return;
		
		// Check monster.
		if (targetMonster.isDead())
			return;
		
		// Range condition, cannot be higher than skill's effectRange.
		if (!player.isIn3DRadius(targetMonster, skill.getEffectRange()))
			return;
		
		// Register.
		targetMonster.registerAbsorber(player);
	}
	
	@Override
	public SkillType[] getSkillIds()
	{
		return SKILL_IDS;
	}
}