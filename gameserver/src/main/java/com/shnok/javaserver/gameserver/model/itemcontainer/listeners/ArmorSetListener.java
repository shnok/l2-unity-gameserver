package com.shnok.javaserver.gameserver.model.itemcontainer.listeners;

import net.sf.l2j.gameserver.data.SkillTable;
import net.sf.l2j.gameserver.data.xml.ArmorSetData;
import net.sf.l2j.gameserver.enums.Paperdoll;
import net.sf.l2j.gameserver.model.actor.Playable;
import net.sf.l2j.gameserver.model.actor.Player;
import net.sf.l2j.gameserver.model.item.ArmorSet;
import net.sf.l2j.gameserver.model.item.instance.ItemInstance;
import net.sf.l2j.gameserver.model.item.kind.Item;
import net.sf.l2j.gameserver.network.serverpackets.SkillList;
import net.sf.l2j.gameserver.skills.L2Skill;

public class ArmorSetListener implements OnEquipListener
{
	@Override
	public void onEquip(Paperdoll slot, ItemInstance item, Playable actor)
	{
		if (!item.isEquipable())
			return;
		
		final Player player = (Player) actor;
		
		// Formal Wear skills refresh. Don't bother going farther.
		if (item.getItem().getBodyPart() == Item.SLOT_ALLDRESS)
		{
			player.sendPacket(new SkillList(player));
			return;
		}
		
		// Check if the Player is wearing a chest item.
		final int chestId = player.getInventory().getItemIdFrom(Paperdoll.CHEST);
		if (chestId == 0)
			return;
		
		// Check if this chest is part of an ArmorSet.
		final ArmorSet armorSet = ArmorSetData.getInstance().getSet(chestId);
		if (armorSet == null)
			return;
		
		// Check if equipped item is part of the ArmorSet.
		if (armorSet.containsItem(slot, item.getItemId()))
		{
			if (armorSet.containsAll(player))
			{
				L2Skill skill = SkillTable.getInstance().getInfo(armorSet.getSkillId(), 1);
				if (skill != null)
				{
					player.addSkill(SkillTable.getInstance().getInfo(3006, 1), false);
					player.addSkill(skill, false);
					player.sendPacket(new SkillList(player));
				}
				
				if (armorSet.containsShield(player)) // has shield from set
				{
					L2Skill skills = SkillTable.getInstance().getInfo(armorSet.getShieldSkillId(), 1);
					if (skills != null)
					{
						player.addSkill(skills, false);
						player.sendPacket(new SkillList(player));
					}
				}
				
				if (armorSet.isEnchanted6(player)) // has all parts of set enchanted to 6 or more
				{
					int skillId = armorSet.getEnchant6skillId();
					if (skillId > 0)
					{
						L2Skill skille = SkillTable.getInstance().getInfo(skillId, 1);
						if (skille != null)
						{
							player.addSkill(skille, false);
							player.sendPacket(new SkillList(player));
						}
					}
				}
			}
		}
		else if (armorSet.containsShield(item.getItemId()) && armorSet.containsAll(player))
		{
			L2Skill skills = SkillTable.getInstance().getInfo(armorSet.getShieldSkillId(), 1);
			if (skills != null)
			{
				player.addSkill(skills, false);
				player.sendPacket(new SkillList(player));
			}
		}
	}
	
	@Override
	public void onUnequip(Paperdoll slot, ItemInstance item, Playable actor)
	{
		final Player player = (Player) actor;
		
		// Formal Wear skills refresh. Don't bother going farther.
		if (item.getItem().getBodyPart() == Item.SLOT_ALLDRESS)
		{
			player.sendPacket(new SkillList(player));
			return;
		}
		
		boolean remove = false;
		int removeSkillId1 = 0; // set skill
		int removeSkillId2 = 0; // shield skill
		int removeSkillId3 = 0; // enchant +6 skill
		
		if (slot == Paperdoll.CHEST)
		{
			final ArmorSet armorSet = ArmorSetData.getInstance().getSet(item.getItemId());
			if (armorSet == null)
				return;
			
			remove = true;
			removeSkillId1 = armorSet.getSkillId();
			removeSkillId2 = armorSet.getShieldSkillId();
			removeSkillId3 = armorSet.getEnchant6skillId();
		}
		else
		{
			// Check if the Player is wearing a chest item.
			final int chestId = player.getInventory().getItemIdFrom(Paperdoll.CHEST);
			if (chestId == 0)
				return;
			
			// Check if this chest is part of an ArmorSet.
			final ArmorSet armorSet = ArmorSetData.getInstance().getSet(chestId);
			if (armorSet == null)
				return;
			
			// Check if equipped item is part of the ArmorSet.
			if (armorSet.containsItem(slot, item.getItemId())) // removed part of set
			{
				remove = true;
				removeSkillId1 = armorSet.getSkillId();
				removeSkillId2 = armorSet.getShieldSkillId();
				removeSkillId3 = armorSet.getEnchant6skillId();
			}
			else if (armorSet.containsShield(item.getItemId())) // removed shield
			{
				remove = true;
				removeSkillId2 = armorSet.getShieldSkillId();
			}
		}
		
		if (remove)
		{
			if (removeSkillId1 != 0)
			{
				player.removeSkill(3006, false);
				player.removeSkill(removeSkillId1, false);
			}
			
			if (removeSkillId2 != 0)
				player.removeSkill(removeSkillId2, false);
			
			if (removeSkillId3 != 0)
				player.removeSkill(removeSkillId3, false);
			
			player.sendPacket(new SkillList(player));
		}
	}
	
	public static final ArmorSetListener getInstance()
	{
		return SingletonHolder.INSTANCE;
	}
	
	private static class SingletonHolder
	{
		protected static final ArmorSetListener INSTANCE = new ArmorSetListener();
	}
}