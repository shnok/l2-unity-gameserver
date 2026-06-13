package com.shnok.javaserver.gameserver.network.clientpackets.unused;

import com.shnok.javaserver.gameserver.data.xml.AugmentationData;
import com.shnok.javaserver.gameserver.enums.ShortcutType;
import com.shnok.javaserver.gameserver.model.Augmentation;
import com.shnok.javaserver.gameserver.model.actor.Player;
import com.shnok.javaserver.gameserver.model.item.LifeStone;
import com.shnok.javaserver.gameserver.model.item.instance.ItemInstance;
import com.shnok.javaserver.gameserver.network.SystemMessageId;
import com.shnok.javaserver.gameserver.network.serverpackets.unused.ExVariationResult;

public final class RequestRefine extends AbstractRefinePacket
{
	private int _targetItemObjId;
	private int _refinerItemObjId;
	private int _gemStoneItemObjId;
	private int _gemStoneCount;
	
	@Override
	protected void readImpl()
	{
		_targetItemObjId = readD();
		_refinerItemObjId = readD();
		_gemStoneItemObjId = readD();
		_gemStoneCount = readD();
	}
	
	@Override
	protected void runImpl()
	{
		final Player player = getClient().getPlayer();
		if (player == null)
			return;
		
		final ItemInstance targetItem = player.getInventory().getItemByObjectId(_targetItemObjId);
		if (targetItem == null)
		{
			player.sendPacket(ExVariationResult.RESULT_FAILED);
			player.sendPacket(SystemMessageId.AUGMENTATION_FAILED_DUE_TO_INAPPROPRIATE_CONDITIONS);
			return;
		}
		
		final ItemInstance refinerItem = player.getInventory().getItemByObjectId(_refinerItemObjId);
		if (refinerItem == null)
		{
			player.sendPacket(ExVariationResult.RESULT_FAILED);
			player.sendPacket(SystemMessageId.AUGMENTATION_FAILED_DUE_TO_INAPPROPRIATE_CONDITIONS);
			return;
		}
		
		final ItemInstance gemStoneItem = player.getInventory().getItemByObjectId(_gemStoneItemObjId);
		if (gemStoneItem == null)
		{
			player.sendPacket(ExVariationResult.RESULT_FAILED);
			player.sendPacket(SystemMessageId.AUGMENTATION_FAILED_DUE_TO_INAPPROPRIATE_CONDITIONS);
			return;
		}
		
		if (!isValid(player, targetItem, refinerItem, gemStoneItem))
		{
			player.sendPacket(ExVariationResult.RESULT_FAILED);
			player.sendPacket(SystemMessageId.AUGMENTATION_FAILED_DUE_TO_INAPPROPRIATE_CONDITIONS);
			return;
		}
		
		final LifeStone ls = getLifeStone(refinerItem.getItemId());
		if (ls == null)
		{
			player.sendPacket(ExVariationResult.RESULT_FAILED);
			player.sendPacket(SystemMessageId.AUGMENTATION_FAILED_DUE_TO_INAPPROPRIATE_CONDITIONS);
			return;
		}
		
		if (_gemStoneCount != targetItem.getItem().getCrystalType().getGemstoneCount())
		{
			player.sendPacket(ExVariationResult.RESULT_FAILED);
			player.sendPacket(SystemMessageId.AUGMENTATION_FAILED_DUE_TO_INAPPROPRIATE_CONDITIONS);
			return;
		}
		
		// unequip item
		if (targetItem.isEquipped())
		{
			player.getInventory().unequipItemInSlotAndRecord(targetItem.getLocationSlot());
			player.broadcastUserInfo();
		}
		
		// Consume the life stone
		if (!player.destroyItem(refinerItem, 1, false))
		{
			player.sendPacket(ExVariationResult.RESULT_FAILED);
			player.sendPacket(SystemMessageId.AUGMENTATION_FAILED_DUE_TO_INAPPROPRIATE_CONDITIONS);
			return;
		}
		
		// Consume gemstones
		if (!player.destroyItem(gemStoneItem, _gemStoneCount, false))
		{
			player.sendPacket(ExVariationResult.RESULT_FAILED);
			player.sendPacket(SystemMessageId.AUGMENTATION_FAILED_DUE_TO_INAPPROPRIATE_CONDITIONS);
			return;
		}
		
		final Augmentation aug = AugmentationData.getInstance().generateRandomAugmentation(ls.level(), ls.grade());
		targetItem.setAugmentation(aug, player);
		
		final int stat12 = 0x0000FFFF & aug.getId();
		final int stat34 = aug.getId() >> 16;
		player.sendPacket(new ExVariationResult(stat12, stat34, 1));
		
		// Refresh shortcuts.
		player.getShortcutList().refreshShortcuts(s -> targetItem.getObjectId() == s.getId() && s.getType() == ShortcutType.ITEM);
	}
}