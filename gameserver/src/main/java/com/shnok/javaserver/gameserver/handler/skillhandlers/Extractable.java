package com.shnok.javaserver.gameserver.handler.skillhandlers;

import net.sf.l2j.commons.random.Rnd;

import net.sf.l2j.gameserver.enums.skills.SkillType;
import net.sf.l2j.gameserver.handler.ISkillHandler;
import net.sf.l2j.gameserver.model.WorldObject;
import net.sf.l2j.gameserver.model.actor.Creature;
import net.sf.l2j.gameserver.model.actor.Player;
import net.sf.l2j.gameserver.model.holder.IntIntHolder;
import net.sf.l2j.gameserver.model.item.instance.ItemInstance;
import net.sf.l2j.gameserver.network.SystemMessageId;
import net.sf.l2j.gameserver.skills.L2Skill;
import net.sf.l2j.gameserver.skills.extractable.ExtractableProductItem;
import net.sf.l2j.gameserver.skills.extractable.ExtractableSkill;

public class Extractable implements ISkillHandler
{
	private static final SkillType[] SKILL_IDS =
	{
		SkillType.EXTRACTABLE,
		SkillType.EXTRACTABLE_FISH
	};
	
	@Override
	public void useSkill(Creature creature, L2Skill skill, WorldObject[] targets, ItemInstance item)
	{
		if (!(creature instanceof Player player))
			return;
		
		final ExtractableSkill exItem = skill.getExtractableSkill();
		if (exItem == null || exItem.productItems().isEmpty())
		{
			LOGGER.warn("Missing informations for extractable skill id: {}.", skill.getId());
			return;
		}
		
		int chance = Rnd.get(100000);
		boolean created = false;
		
		for (ExtractableProductItem expi : exItem.productItems())
		{
			chance -= (int) (expi.chance() * 1000);
			if (chance >= 0)
				continue;
			
			// The inventory is full, terminate.
			if (!player.getInventory().validateCapacityByItemIds(expi.items()))
			{
				player.sendPacket(SystemMessageId.SLOTS_FULL);
				return;
			}
			
			// Inventory has space, create all items.
			for (IntIntHolder iih : expi.items())
			{
				player.addItem(iih.getId(), iih.getValue(), true);
				created = true;
			}
			
			break;
		}
		
		if (!created)
			player.sendPacket(SystemMessageId.NOTHING_INSIDE_THAT);
	}
	
	@Override
	public SkillType[] getSkillIds()
	{
		return SKILL_IDS;
	}
}