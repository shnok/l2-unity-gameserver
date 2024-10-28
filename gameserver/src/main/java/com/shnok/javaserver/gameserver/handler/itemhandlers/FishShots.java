package com.shnok.javaserver.gameserver.handler.itemhandlers;

import com.shnok.javaserver.gameserver.enums.items.ShotType;
import com.shnok.javaserver.gameserver.enums.items.WeaponType;
import com.shnok.javaserver.gameserver.handler.IItemHandler;
import com.shnok.javaserver.gameserver.model.actor.Playable;
import com.shnok.javaserver.gameserver.model.actor.Player;
import com.shnok.javaserver.gameserver.model.holder.IntIntHolder;
import com.shnok.javaserver.gameserver.model.item.instance.ItemInstance;
import com.shnok.javaserver.gameserver.model.item.kind.Weapon;
import com.shnok.javaserver.gameserver.network.SystemMessageId;
import com.shnok.javaserver.gameserver.network.serverpackets.unused.MagicSkillUse;

public class FishShots implements IItemHandler
{
	@Override
	public void useItem(Playable playable, ItemInstance item, boolean forceUse)
	{
		if (!(playable instanceof Player player))
			return;
		
		final ItemInstance weaponInst = player.getActiveWeaponInstance();
		final Weapon weaponItem = player.getActiveWeaponItem();
		
		if (weaponInst == null || weaponItem.getItemType() != WeaponType.FISHINGROD)
			return;
		
		// Fishshot is already active
		if (player.isChargedShot(ShotType.FISH_SOULSHOT))
			return;
		
		// Wrong grade of soulshot for that fishing pole.
		if (weaponItem.getCrystalType() != item.getItem().getCrystalType())
		{
			player.sendPacket(SystemMessageId.WRONG_FISHINGSHOT_GRADE);
			return;
		}
		
		if (!player.destroyItem(item.getObjectId(), 1, false))
		{
			player.sendPacket(SystemMessageId.NOT_ENOUGH_SOULSHOTS);
			return;
		}
		
		final IntIntHolder[] skills = item.getItem().getSkills();
		
		player.setChargedShot(ShotType.FISH_SOULSHOT, true);
		player.broadcastPacket(new MagicSkillUse(player, skills[0].getId(), 1, 0, 0));
	}
}