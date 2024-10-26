package com.shnok.javaserver.gameserver.model.itemcontainer.listeners;

import net.sf.l2j.gameserver.enums.Paperdoll;
import net.sf.l2j.gameserver.model.actor.Playable;
import net.sf.l2j.gameserver.model.actor.Player;
import net.sf.l2j.gameserver.model.holder.IntIntHolder;
import net.sf.l2j.gameserver.model.item.instance.ItemInstance;
import net.sf.l2j.gameserver.model.item.kind.Item;
import net.sf.l2j.gameserver.model.item.kind.Weapon;
import net.sf.l2j.gameserver.network.serverpackets.SkillCoolTime;
import net.sf.l2j.gameserver.network.serverpackets.SkillList;
import net.sf.l2j.gameserver.skills.L2Skill;

public class ItemPassiveSkillsListener implements OnEquipListener
{
	@Override
	public void onEquip(Paperdoll slot, ItemInstance item, Playable actor)
	{
		final Player player = (Player) actor;
		final Item it = item.getItem();
		
		boolean update = false;
		boolean updateTimeStamp = false;
		
		if (it instanceof Weapon weapon)
		{
			// Apply augmentation bonuses on equip
			if (item.isAugmented())
				item.getAugmentation().applyBonus(player);
			
			// Verify if the grade penalty is occuring. If yes, then forget +4 dual skills and SA attached to weapon.
			if (player.getSkillLevel(L2Skill.SKILL_EXPERTISE) < weapon.getCrystalType().getId())
				return;
			
			// Add skills bestowed from +4 Duals
			if (item.getEnchantLevel() >= 4)
			{
				final L2Skill enchant4Skill = weapon.getEnchant4Skill();
				if (enchant4Skill != null)
				{
					player.addSkill(enchant4Skill, false);
					update = true;
				}
			}
		}
		
		final IntIntHolder[] skills = it.getSkills();
		if (skills != null)
		{
			for (IntIntHolder skillInfo : skills)
			{
				if (skillInfo == null)
					continue;
				
				final L2Skill itemSkill = skillInfo.getSkill();
				if (itemSkill != null)
				{
					player.addSkill(itemSkill, false);
					
					if (itemSkill.isActive())
					{
						if (!player.getReuseTimeStamp().containsKey(itemSkill.getReuseHashCode()))
						{
							final int equipDelay = itemSkill.getEquipDelay();
							if (equipDelay > 0)
							{
								player.addTimeStamp(itemSkill, equipDelay);
								player.disableSkill(itemSkill, equipDelay);
							}
						}
						updateTimeStamp = true;
					}
					update = true;
				}
			}
		}
		
		if (update)
		{
			player.sendPacket(new SkillList(player));
			
			if (updateTimeStamp)
				player.sendPacket(new SkillCoolTime(player));
		}
	}
	
	@Override
	public void onUnequip(Paperdoll slot, ItemInstance item, Playable actor)
	{
		final Player player = (Player) actor;
		final Item it = item.getItem();
		
		boolean update = false;
		
		if (it instanceof Weapon weapon)
		{
			// Remove augmentation bonuses on unequip
			if (item.isAugmented())
				item.getAugmentation().removeBonus(player);
			
			// Remove skills bestowed from +4 Duals
			if (item.getEnchantLevel() >= 4)
			{
				final L2Skill enchant4Skill = weapon.getEnchant4Skill();
				if (enchant4Skill != null)
				{
					player.removeSkill(enchant4Skill.getId(), false, enchant4Skill.isPassive() || enchant4Skill.isToggle());
					update = true;
				}
			}
		}
		
		final IntIntHolder[] skills = it.getSkills();
		if (skills != null)
		{
			for (IntIntHolder skillInfo : skills)
			{
				if (skillInfo == null)
					continue;
				
				final L2Skill itemSkill = skillInfo.getSkill();
				if (itemSkill != null)
				{
					boolean found = false;
					
					for (ItemInstance pItem : player.getInventory().getPaperdollItems())
					{
						if (it.getItemId() == pItem.getItemId())
						{
							found = true;
							break;
						}
					}
					
					if (!found)
					{
						player.removeSkill(itemSkill.getId(), false, itemSkill.isPassive() || itemSkill.isToggle());
						update = true;
					}
				}
			}
		}
		
		if (update)
			player.sendPacket(new SkillList(player));
	}
	
	public static final ItemPassiveSkillsListener getInstance()
	{
		return SingletonHolder.INSTANCE;
	}
	
	private static class SingletonHolder
	{
		protected static final ItemPassiveSkillsListener INSTANCE = new ItemPassiveSkillsListener();
	}
}