package com.shnok.javaserver.gameserver.handler.skillhandlers;

import net.sf.l2j.commons.random.Rnd;

import net.sf.l2j.gameserver.data.manager.ZoneManager;
import net.sf.l2j.gameserver.enums.items.WeaponType;
import net.sf.l2j.gameserver.enums.skills.SkillType;
import net.sf.l2j.gameserver.geoengine.GeoEngine;
import net.sf.l2j.gameserver.handler.ISkillHandler;
import net.sf.l2j.gameserver.model.WorldObject;
import net.sf.l2j.gameserver.model.actor.Creature;
import net.sf.l2j.gameserver.model.actor.Player;
import net.sf.l2j.gameserver.model.item.instance.ItemInstance;
import net.sf.l2j.gameserver.model.location.SpawnLocation;
import net.sf.l2j.gameserver.model.zone.type.FishingZone;
import net.sf.l2j.gameserver.network.SystemMessageId;
import net.sf.l2j.gameserver.skills.L2Skill;

public class Fishing implements ISkillHandler
{
	private static final SkillType[] SKILL_IDS =
	{
		SkillType.FISHING
	};
	
	@Override
	public void useSkill(Creature creature, L2Skill skill, WorldObject[] targets, ItemInstance item)
	{
		if (!(creature instanceof Player player))
			return;
		
		// Cancels fishing
		if (player.isFishing())
		{
			player.getFishingStance().end(false);
			player.sendPacket(SystemMessageId.FISHING_ATTEMPT_CANCELLED);
			return;
		}
		
		// Fishing pole isn't equipped.
		if (player.getAttackType() != WeaponType.FISHINGROD)
		{
			player.sendPacket(SystemMessageId.FISHING_POLE_NOT_EQUIPPED);
			return;
		}
		
		// You can't fish while you are on boat
		if (player.getBoatInfo().isInBoat())
		{
			player.sendPacket(SystemMessageId.CANNOT_FISH_ON_BOAT);
			return;
		}
		
		if (player.isCrafting() || player.isOperating())
		{
			player.sendPacket(SystemMessageId.CANNOT_FISH_WHILE_USING_RECIPE_BOOK);
			return;
		}
		
		// You can't fish in water
		if (player.isInWater())
		{
			player.sendPacket(SystemMessageId.CANNOT_FISH_UNDER_WATER);
			return;
		}
		
		// Check equipped baits.
		final ItemInstance lure = player.getSecondaryWeaponInstance();
		if (lure == null)
		{
			player.sendPacket(SystemMessageId.BAIT_ON_HOOK_BEFORE_FISHING);
			return;
		}
		
		// Calculate a Location for the fishing bait. The Z is edited later.
		final SpawnLocation baitLoc = player.getPosition().clone();
		baitLoc.addOffsetBasedOnHeading(Rnd.get(50) + 250);
		
		boolean canFish = false;
		
		// Pick the fishing zone.
		final FishingZone zone = ZoneManager.getInstance().getZone(baitLoc.getX(), baitLoc.getY(), FishingZone.class);
		if (zone != null)
		{
			// Refresh the Z based on zone, if existing.
			baitLoc.setZ(zone.getWaterZ());
			
			// Check if the height related to the bait location is above water level. If yes, it means the water isn't visible.
			if (GeoEngine.getInstance().canSeeLocation(player, baitLoc) && GeoEngine.getInstance().getHeight(baitLoc) < baitLoc.getZ())
			{
				baitLoc.setZ(baitLoc.getZ() + 10);
				canFish = true;
			}
		}
		
		// You can't fish here.
		if (!canFish)
		{
			player.sendPacket(SystemMessageId.CANNOT_FISH_HERE);
			return;
		}
		
		// Has enough bait, consume 1 and update inventory.
		if (!player.destroyItem(lure, 1, false))
		{
			player.sendPacket(SystemMessageId.NOT_ENOUGH_BAIT);
			return;
		}
		
		// Start fishing.
		player.getFishingStance().start(baitLoc, lure);
	}
	
	@Override
	public SkillType[] getSkillIds()
	{
		return SKILL_IDS;
	}
}