package com.shnok.javaserver.gameserver.handler.itemhandlers;

import net.sf.l2j.gameserver.handler.IItemHandler;
import net.sf.l2j.gameserver.model.WorldObject;
import net.sf.l2j.gameserver.model.actor.Playable;
import net.sf.l2j.gameserver.model.actor.Player;
import net.sf.l2j.gameserver.model.actor.instance.Chest;
import net.sf.l2j.gameserver.model.holder.IntIntHolder;
import net.sf.l2j.gameserver.model.item.instance.ItemInstance;
import net.sf.l2j.gameserver.network.SystemMessageId;
import net.sf.l2j.gameserver.skills.L2Skill;

/**
 * That handler is used for the different types of keys. Such items aren't consumed until the skill is definitively launched.
 */
public class Keys implements IItemHandler
{
	@Override
	public void useItem(Playable playable, ItemInstance item, boolean forceUse)
	{
		if (!(playable instanceof Player player))
			return;
		
		if (player.isSitting())
		{
			player.sendPacket(SystemMessageId.CANT_MOVE_SITTING);
			return;
		}
		
		if (player.isMovementDisabled())
			return;
		
		final WorldObject target = playable.getTarget();
		if (!(target instanceof Chest targetChest))
		{
			player.sendPacket(SystemMessageId.INVALID_TARGET);
			return;
		}
		
		// Target must be a valid chest (not dead or already interacted).
		if (targetChest.isDead() || targetChest.isInteracted())
		{
			player.sendPacket(SystemMessageId.INVALID_TARGET);
			return;
		}
		
		final IntIntHolder[] skills = item.getEtcItem().getSkills();
		if (skills == null)
		{
			LOGGER.warn("{} doesn't have any registered skill for handler.", item.getName());
			return;
		}
		
		// TODO Necessary for loop?
		for (IntIntHolder skillInfo : skills)
		{
			if (skillInfo == null)
				continue;
			
			final L2Skill itemSkill = skillInfo.getSkill();
			if (itemSkill == null)
				continue;
			
			// Key consumption is made on skill call, not on item call.
			playable.getAI().tryToCast(targetChest, itemSkill);
		}
	}
}