package com.shnok.javaserver.gameserver.skills.l2skills;

import com.shnok.javaserver.commons.data.StatSet;

import com.shnok.javaserver.gameserver.model.WorldObject;
import com.shnok.javaserver.gameserver.model.actor.Creature;
import com.shnok.javaserver.gameserver.model.actor.Npc;
import com.shnok.javaserver.gameserver.model.spawn.Spawn;
import com.shnok.javaserver.gameserver.skills.L2Skill;

public class L2SkillSpawn extends L2Skill
{
	private final int _npcId;
	private final int _despawnDelay;
	
	public L2SkillSpawn(StatSet set)
	{
		super(set);
		
		_npcId = set.getInteger("npcId", 0);
		_despawnDelay = set.getInteger("despawnDelay", 0);
	}
	
	@Override
	public void useSkill(Creature creature, WorldObject[] targets)
	{
		if (creature.isAlikeDead())
			return;
		
		try
		{
			// Create spawn.
			final Spawn spawn = new Spawn(_npcId);
			spawn.setLoc(creature.getPosition());
			
			// Spawn NPC.
			final Npc npc = spawn.doSpawn(false);
			if (_despawnDelay > 0)
				npc.scheduleDespawn(_despawnDelay);
		}
		catch (Exception e)
		{
			LOGGER.error("Failed to initialize a spawn.", e);
		}
	}
}