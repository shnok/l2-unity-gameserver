package com.shnok.javaserver.gameserver.handler.skillhandlers;

import net.sf.l2j.gameserver.data.xml.NpcData;
import net.sf.l2j.gameserver.data.xml.SummonItemData;
import net.sf.l2j.gameserver.enums.items.ItemLocation;
import net.sf.l2j.gameserver.enums.skills.SkillType;
import net.sf.l2j.gameserver.geoengine.GeoEngine;
import net.sf.l2j.gameserver.handler.ISkillHandler;
import net.sf.l2j.gameserver.model.World;
import net.sf.l2j.gameserver.model.WorldObject;
import net.sf.l2j.gameserver.model.actor.Creature;
import net.sf.l2j.gameserver.model.actor.Player;
import net.sf.l2j.gameserver.model.actor.instance.Pet;
import net.sf.l2j.gameserver.model.actor.template.NpcTemplate;
import net.sf.l2j.gameserver.model.holder.IntIntHolder;
import net.sf.l2j.gameserver.model.item.instance.ItemInstance;
import net.sf.l2j.gameserver.model.location.SpawnLocation;
import net.sf.l2j.gameserver.skills.L2Skill;

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