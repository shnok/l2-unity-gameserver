package com.shnok.javaserver.gameserver.network.clientpackets;

import net.sf.l2j.gameserver.data.xml.AugmentationData;
import net.sf.l2j.gameserver.enums.ShortcutType;
import net.sf.l2j.gameserver.model.Augmentation;
import net.sf.l2j.gameserver.model.actor.Player;
import net.sf.l2j.gameserver.model.item.LifeStone;
import net.sf.l2j.gameserver.model.item.instance.ItemInstance;
import net.sf.l2j.gameserver.network.SystemMessageId;
import net.sf.l2j.gameserver.network.serverpackets.ExVariationResult;

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