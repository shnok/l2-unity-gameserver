package com.shnok.javaserver.gameserver.handler.itemhandlers;

import net.sf.l2j.commons.util.ArraysUtil;

import net.sf.l2j.gameserver.handler.IItemHandler;
import net.sf.l2j.gameserver.model.actor.Creature;
import net.sf.l2j.gameserver.model.actor.Playable;
import net.sf.l2j.gameserver.model.actor.Player;
import net.sf.l2j.gameserver.model.actor.instance.Pet;
import net.sf.l2j.gameserver.model.actor.instance.Servitor;
import net.sf.l2j.gameserver.model.holder.IntIntHolder;
import net.sf.l2j.gameserver.model.item.instance.ItemInstance;
import net.sf.l2j.gameserver.network.SystemMessageId;
import net.sf.l2j.gameserver.network.serverpackets.SystemMessage;
import net.sf.l2j.gameserver.skills.L2Skill;
import net.sf.l2j.gameserver.skills.effects.EffectTemplate;

public class ItemSkills implements IItemHandler
{
	
	private static final int[] HP_POTION_SKILL_IDS =
	{
		2031, // Lesser Healing Potion
		2032, // Healing potion
		2037 // Greater Healing Potion
	};
	
	@Override
	public void useItem(Playable playable, ItemInstance item, boolean forceUse)
	{
		if (playable instanceof Servitor)
			return;
		
		final boolean isPet = playable instanceof Pet;
		final Player player = playable.getActingPlayer();
		final Creature target = (playable.getTarget() instanceof Creature targetCreature) ? targetCreature : null;
		
		// Pets can only use tradable items.
		if (isPet && !item.isTradable())
		{
			player.sendPacket(SystemMessageId.ITEM_NOT_FOR_PETS);
			return;
		}
		
		final IntIntHolder[] skills = item.getEtcItem().getSkills();
		if (skills == null)
		{
			LOGGER.warn("{} doesn't have any registered skill for handler.", item.getName());
			return;
		}
		
		for (final IntIntHolder skillInfo : skills)
		{
			if (skillInfo == null)
				continue;
			
			final L2Skill itemSkill = skillInfo.getSkill();
			if (itemSkill == null)
				continue;
			
			if (!itemSkill.checkCondition(playable, target, false))
				return;
			
			// The skill is currently under reuse.
			if (playable.isSkillDisabled(itemSkill))
			{
				player.sendPacket(SystemMessage.getSystemMessage(SystemMessageId.S1_PREPARED_FOR_REUSE).addSkillName(itemSkill));
				return;
			}
			
			// Potions and Energy Stones bypass the AI system. The rest does not.
			if (itemSkill.isPotion() || itemSkill.isSimultaneousCast())
			{
				playable.getCast().doInstantCast(itemSkill, item);
				
				if (!isPet && item.isHerb() && player.hasServitor())
					player.getSummon().getCast().doInstantCast(itemSkill, item);
			}
			else
				playable.getAI().tryToCast(target, itemSkill, forceUse, false, (item.isEtcItem() ? item.getObjectId() : 0));
			
			// Send message to owner.
			if (isPet)
				player.sendPacket(SystemMessage.getSystemMessage(SystemMessageId.PET_USES_S1).addSkillName(itemSkill));
			// Buff icon for healing potions.
			else if (ArraysUtil.contains(HP_POTION_SKILL_IDS, skillInfo.getId()) && skillInfo.getId() >= player.getShortBuffTaskSkillId())
			{
				final EffectTemplate template = itemSkill.getEffectTemplates().get(0);
				if (template != null)
					player.shortBuffStatusUpdate(skillInfo.getId(), skillInfo.getValue(), template.getCounter() * template.getPeriod());
			}
		}
	}
}