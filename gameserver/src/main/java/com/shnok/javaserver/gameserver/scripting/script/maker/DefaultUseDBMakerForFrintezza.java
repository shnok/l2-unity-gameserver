package com.shnok.javaserver.gameserver.scripting.script.maker;

import com.shnok.javaserver.gameserver.model.spawn.MultiSpawn;
import com.shnok.javaserver.gameserver.model.spawn.NpcMaker;
import com.shnok.javaserver.gameserver.model.spawn.SpawnData;

public class DefaultUseDBMakerForFrintezza extends DefaultUseDBMaker
{
	public DefaultUseDBMakerForFrintezza(String name)
	{
		super(name);
	}
	
	@Override
	public void onNpcDBInfo(MultiSpawn ms, SpawnData spawnData, NpcMaker maker)
	{
		ms.doSpawn(true);
	}
}
