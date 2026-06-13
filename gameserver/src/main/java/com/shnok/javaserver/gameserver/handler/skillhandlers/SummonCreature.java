package com.shnok.javaserver.gameserver.handler.skillhandlers;

import com.shnok.javaserver.gameserver.data.xml.NpcData;
import com.shnok.javaserver.gameserver.data.xml.SummonItemData;
import com.shnok.javaserver.gameserver.enums.items.ItemLocation;
import com.shnok.javaserver.gameserver.enums.skills.SkillType;
import com.shnok.javaserver.gameserver.geoengine.GeoEngine;
import com.shnok.javaserver.gameserver.handler.ISkillHandler;
import com.shnok.javaserver.gameserver.model.World;
import com.shnok.javaserver.gameserver.model.WorldObject;
import com.shnok.javaserver.gameserver.model.actor.Creature;
import com.shnok.javaserver.gameserver.model.actor.Player;
import com.shnok.javaserver.gameserver.model.actor.instance.Pet;
import com.shnok.javaserver.gameserver.model.actor.template.NpcTemplate;
import com.shnok.javaserver.gameserver.model.holder.IntIntHolder;
import com.shnok.javaserver.gameserver.model.item.instance.ItemInstance;
import com.shnok.javaserver.gameserver.model.location.SpawnLocation;
import com.shnok.javaserver.gameserver.skills.L2Skill;

public class SummonCreature implements ISkillHandler
{
	private static final SkillType[] SKILL_IDS =
	{
		SkillType.SUMMON_CREATURE
	};
	
	@Override
	public void useSkill(Creature creature, L2Skill skill, WorldObject[] targets, ItemInstance item)
	{
		// Must be called by a Player.
		if (!(creature instanceof Player player))
			return;
		
		// Sanity check - skill cast may have been interrupted or cancelled.
		final ItemInstance checkedItem = player.getInventory().getItemByObjectId(player.getAI().getCurrentIntention().getItemObjectId());
		if (checkedItem == null)
			return;
		
		// Check for summon item validity.
		if (checkedItem.getOwnerId() != player.getObjectId() || checkedItem.getLocation() != ItemLocation.INVENTORY)
			return;
		
		// Owner has a pet listed in world.
		if (World.getInstance().getPet(player.getObjectId()) != null)
			return;
		
		// Check summon item validity.
		final IntIntHolder summonItem = SummonItemData.getInstance().getSummonItem(checkedItem.getItemId());
		if (summonItem == null)
			return;
		
		// Check NpcTemplate validity.
		final NpcTemplate npcTemplate = NpcData.getInstance().getTemplate(summonItem.getId());
		if (npcTemplate == null)
			return;
		
		// Add the pet instance to world.
		final Pet pet = Pet.restore(checkedItem, npcTemplate, player);
		if (pet == null)
			return;
		
		World.getInstance().addPet(player.getObjectId(), pet);
		
		player.setSummon(pet);
		
		pet.forceRunStance();
		pet.setTitle(player.getName());
		pet.startFeed();
		
		final SpawnLocation spawnLoc = creature.getPosition().clone();
		spawnLoc.addStrictOffset(40);
		spawnLoc.setHeadingTo(creature.getPosition());
		spawnLoc.set(GeoEngine.getInstance().getValidLocation(creature, spawnLoc));
		
		pet.spawnMe(spawnLoc);
		pet.getAI().setFollowStatus(true);
	}
	
	@Override
	public SkillType[] getSkillIds()
	{
		return SKILL_IDS;
	}
}