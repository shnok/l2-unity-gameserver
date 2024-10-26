package com.shnok.javaserver.gameserver.handler.skillhandlers;

import com.shnok.javaserver.gameserver.enums.items.ShotType;
import com.shnok.javaserver.gameserver.enums.skills.SkillType;
import com.shnok.javaserver.gameserver.handler.ISkillHandler;
import com.shnok.javaserver.gameserver.model.WorldObject;
import com.shnok.javaserver.gameserver.model.actor.Creature;
import com.shnok.javaserver.gameserver.model.actor.Player;
import com.shnok.javaserver.gameserver.model.actor.instance.Pet;
import com.shnok.javaserver.gameserver.model.item.instance.ItemInstance;
import com.shnok.javaserver.gameserver.skills.Formulas;
import com.shnok.javaserver.gameserver.skills.L2Skill;
import com.shnok.javaserver.gameserver.taskmanager.DecayTaskManager;

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