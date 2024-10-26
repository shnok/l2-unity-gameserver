package com.shnok.javaserver.gameserver.handler.skillhandlers;

import net.sf.l2j.gameserver.enums.items.ShotType;
import net.sf.l2j.gameserver.enums.skills.SkillType;
import net.sf.l2j.gameserver.handler.ISkillHandler;
import net.sf.l2j.gameserver.model.WorldObject;
import net.sf.l2j.gameserver.model.actor.Creature;
import net.sf.l2j.gameserver.model.actor.Player;
import net.sf.l2j.gameserver.model.actor.instance.Pet;
import net.sf.l2j.gameserver.model.item.instance.ItemInstance;
import net.sf.l2j.gameserver.skills.Formulas;
import net.sf.l2j.gameserver.skills.L2Skill;
import net.sf.l2j.gameserver.taskmanager.DecayTaskManager;

public class Resurrect implements ISkillHandler
{
	private static final SkillType[] SKILL_IDS =
	{
		SkillType.RESURRECT
	};
	
	@Override
	public void useSkill(Creature creature, L2Skill skill, WorldObject[] targets, ItemInstance item)
	{
		if (creature instanceof Player player)
		{
			for (WorldObject target : targets)
			{
				if (target instanceof Player targetPlayer)
					targetPlayer.reviveRequest(player, skill, false);
				else if (target instanceof Pet targetPet)
				{
					if (targetPet.getOwner() == player)
						targetPet.doRevive(Formulas.calcRevivePower(player, skill.getPower()));
					else
						targetPet.getOwner().reviveRequest(player, skill, true);
				}
				else if (target instanceof Creature targetCreature)
					targetCreature.doRevive(Formulas.calcRevivePower(player, skill.getPower()));
			}
		}
		else
		{
			for (WorldObject target : targets)
			{
				if (target instanceof Creature targetCreature)
				{
					DecayTaskManager.getInstance().cancel(targetCreature);
					targetCreature.doRevive(Formulas.calcRevivePower(creature, skill.getPower()));
				}
			}
		}
		creature.setChargedShot(creature.isChargedShot(ShotType.BLESSED_SPIRITSHOT) ? ShotType.BLESSED_SPIRITSHOT : ShotType.SPIRITSHOT, skill.isStaticReuse());
	}
	
	@Override
	public SkillType[] getSkillIds()
	{
		return SKILL_IDS;
	}
}