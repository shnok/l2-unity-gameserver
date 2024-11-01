package com.shnok.javaserver.gameserver.network.clientpackets.unused;

import java.util.HashMap;
import java.util.Map;

import com.shnok.javaserver.gameserver.enums.items.CrystalType;
import com.shnok.javaserver.gameserver.enums.items.ItemLocation;
import com.shnok.javaserver.gameserver.enums.items.WeaponType;
import com.shnok.javaserver.gameserver.model.actor.Player;
import com.shnok.javaserver.gameserver.model.item.LifeStone;
import com.shnok.javaserver.gameserver.model.item.instance.ItemInstance;
import com.shnok.javaserver.gameserver.model.item.kind.Weapon;
import com.shnok.javaserver.gameserver.network.SystemMessageId;
import com.shnok.javaserver.gameserver.network.clientpackets.L2GameClientPacket;

public abstract class AbstractRefinePacket extends L2GameClientPacket
{
	public static final int GRADE_NONE = 0;
	public static final int GRADE_MID = 1;
	public static final int GRADE_HIGH = 2;
	public static final int GRADE_TOP = 3;
	
	private static final Map<Integer, LifeStone> _lifeStones = new HashMap<>();
	
	static
	{
		int itemId = 8723;
		
		// Populate GRADE_NONE life stones
		for (int i = 0; i < 10; i++, itemId++)
			_lifeStones.put(itemId, new LifeStone(GRADE_NONE, i));
		
		// Populate GRADE_MID life stones
		for (int i = 0; i < 10; i++, itemId++)
			_lifeStones.put(itemId, new LifeStone(GRADE_MID, i));
		
		// Populate GRADE_HIGH life stones
		for (int i = 0; i < 10; i++, itemId++)
			_lifeStones.put(itemId, new LifeStone(GRADE_HIGH, i));
		
		// Populate GRADE_TOP life stones
		for (int i = 0; i < 10; i++, itemId++)
			_lifeStones.put(itemId, new LifeStone(GRADE_TOP, i));
	}
	
	protected static final LifeStone getLifeStone(int itemId)
	{
		return _lifeStones.get(itemId);
	}
	
	/**
	 * @param player : The {@link Player} to test.
	 * @param item : The {@link ItemInstance} to test.
	 * @param refinerItem : The {@link ItemInstance} used as Lifestone to test.
	 * @param gemstoneItem : The {@link ItemInstance} used as gemstone to test.
	 * @return True if the augmentation process is doable, otherwise return false.
	 */
	protected static final boolean isValid(Player player, ItemInstance item, ItemInstance refinerItem, ItemInstance gemstoneItem)
	{
		// Conditions must be valid.
		if (!isValid(player, item, refinerItem))
			return false;
		
		// Gemstones must belong to owner.
		if (gemstoneItem.getOwnerId() != player.getObjectId())
			return false;
		
		// Gemstones must be in inventory.
		if (gemstoneItem.getLocation() != ItemLocation.INVENTORY)
			return false;
		
		final CrystalType grade = item.getItem().getCrystalType();
		
		// Check for item id.
		if (grade.getGemstoneId() != gemstoneItem.getItemId())
			return false;
		
		// Existing count must be greater or equal required number.
		return gemstoneItem.getCount() >= grade.getGemstoneCount();
	}
	
	/**
	 * @param player : The {@link Player} to test.
	 * @param item : The {@link ItemInstance} to test.
	 * @param refinerItem : The {@link ItemInstance} used as Lifestone to test.
	 * @return True if the conditions for augmentation process are met for the {@link Player}, the {@link ItemInstance} and the {@link ItemInstance} used as Lifestone set as parameters.
	 */
	protected static final boolean isValid(Player player, ItemInstance item, ItemInstance refinerItem)
	{
		if (refinerItem == null)
			return false;
		
		// Both Player and item conditions must be valid.
		if (!isValid(player, item))
			return false;
		
		// The Lifestone must belong to owner.
		if (refinerItem.getOwnerId() != player.getObjectId())
			return false;
		
		// The Lifestone must be in inventory.
		if (refinerItem.getLocation() != ItemLocation.INVENTORY)
			return false;
		
		final LifeStone ls = _lifeStones.get(refinerItem.getItemId());
		if (ls == null)
			return false;
		
		// The level of the Player must be higher than the level of the Lifestone.
		if (player.getStatus().getLevel() < ls.getPlayerLevel())
		{
			player.sendPacket(SystemMessageId.HARDENER_LEVEL_TOO_HIGH);
			return false;
		}
		
		return true;
	}
	
	/**
	 * @param player : The {@link Player} to test.
	 * @param item : The {@link ItemInstance} to test.
	 * @return True if the conditions for augmentation process are met for the {@link Player} and the {@link ItemInstance} set as parameters.
	 */
	protected static final boolean isValid(Player player, ItemInstance item)
	{
		if (item == null)
			return false;
		
		// The Player conditions must be valid.
		if (!isValid(player))
			return false;
		
		// The item must belong to owner.
		if (item.getOwnerId() != player.getObjectId() || item.isAugmented() || item.isHeroItem() || item.isShadowItem() || item.getItem().getCrystalType().isLesser(CrystalType.C))
			return false;
		
		// The item must be in inventory, but not equipped.
		if (item.getLocation() != ItemLocation.INVENTORY && item.getLocation() != ItemLocation.PAPERDOLL)
			return false;
		
		// The item must be a Weapon.
		if (!(item.getItem() instanceof Weapon weapon))
			return false;
		
		// Rods and fists aren't augmentable.
		return weapon.getItemType() != WeaponType.NONE && weapon.getItemType() != WeaponType.FISHINGROD;
	}
	
	/**
	 * @param player : The {@link Player} to test.
	 * @return True if the conditions for augmentation process are met for the {@link Player} set as parameter.
	 */
	protected static final boolean isValid(Player player)
	{
		if (player == null)
			return false;
		
		if (player.isOperating())
		{
			player.sendPacket(SystemMessageId.YOU_CANNOT_AUGMENT_ITEMS_WHILE_A_PRIVATE_STORE_OR_PRIVATE_WORKSHOP_IS_IN_OPERATION);
			return false;
		}
		
		if (player.isProcessingTransaction())
		{
			player.sendPacket(SystemMessageId.YOU_CANNOT_AUGMENT_ITEMS_WHILE_TRADING);
			return false;
		}
		
		if (player.isDead())
		{
			player.sendPacket(SystemMessageId.YOU_CANNOT_AUGMENT_ITEMS_WHILE_DEAD);
			return false;
		}
		
		if (player.isParalyzed())
		{
			player.sendPacket(SystemMessageId.YOU_CANNOT_AUGMENT_ITEMS_WHILE_PARALYZED);
			return false;
		}
		
		if (player.isFishing())
		{
			player.sendPacket(SystemMessageId.YOU_CANNOT_AUGMENT_ITEMS_WHILE_FISHING);
			return false;
		}
		
		if (player.isSitting())
		{
			player.sendPacket(SystemMessageId.YOU_CANNOT_AUGMENT_ITEMS_WHILE_SITTING_DOWN);
			return false;
		}
		
		return !player.isCursedWeaponEquipped();
	}
}