package com.shnok.javaserver.gameserver.model.location;

import com.shnok.javaserver.gameserver.model.actor.Npc;
import com.shnok.javaserver.gameserver.model.residence.castle.Castle;
import com.shnok.javaserver.gameserver.model.spawn.Spawn;

/**
 * A datatype extending {@link SpawnLocation}, which handles a single HolyThing spawn point and its parameters.
 */
public class ArtifactSpawnLocation extends SpawnLocation
{
	private final int _npcId;
	private final Castle _castle;
	
	private Npc _npc;
	
	public ArtifactSpawnLocation(int npcId, Castle castle)
	{
		super(SpawnLocation.DUMMY_SPAWNLOC);
		
		_npcId = npcId;
		_castle = castle;
	}
	
	public int getNpcId()
	{
		return _npcId;
	}
	
	public Npc getNpc()
	{
		return _npc;
	}
	
	public void spawnMe()
	{
		try
		{
			final Spawn spawn = new Spawn(_npcId);
			spawn.setLoc(this);
			
			_npc = spawn.doSpawn(false);
			_npc.setResidence(_castle);
		}
		catch (Exception e)
		{
			// Do nothing.
		}
	}
}