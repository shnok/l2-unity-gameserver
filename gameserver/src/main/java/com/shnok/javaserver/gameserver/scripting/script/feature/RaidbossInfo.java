package com.shnok.javaserver.gameserver.scripting.script.feature;

import com.shnok.javaserver.commons.lang.StringUtil;

import com.shnok.javaserver.gameserver.data.manager.SpawnManager;
import com.shnok.javaserver.gameserver.model.World;
import com.shnok.javaserver.gameserver.model.actor.Npc;
import com.shnok.javaserver.gameserver.model.actor.Player;
import com.shnok.javaserver.gameserver.model.spawn.ASpawn;
import com.shnok.javaserver.gameserver.scripting.Quest;

public class RaidbossInfo extends Quest
{
	private static final int[] NPCs =
	{
		31729,
		31730,
		31731,
		31732,
		31733,
		31734,
		31735,
		31736,
		31737,
		31738,
		31775,
		31776,
		31777,
		31778,
		31779,
		31780,
		31781,
		31782,
		31783,
		31784,
		31785,
		31786,
		31787,
		31788,
		31789,
		31790,
		31791,
		31792,
		31793,
		31794,
		31795,
		31796,
		31797,
		31798,
		31799,
		31800,
		31801,
		31802,
		31803,
		31804,
		31805,
		31806,
		31807,
		31808,
		31809,
		31810,
		31811,
		31812,
		31813,
		31814,
		31815,
		31816,
		31817,
		31818,
		31819,
		31820,
		31821,
		31822,
		31823,
		31824,
		31825,
		31826,
		31827,
		31828,
		31829,
		31830,
		31831,
		31832,
		31833,
		31834,
		31835,
		31836,
		31837,
		31838,
		31839,
		31840,
		31841,
		31991,
		31992,
		31993,
		31994,
		31995
	};
	
	public RaidbossInfo()
	{
		super(-1, "feature");
		
		addTalkId(NPCs);
	}
	
	@Override
	public String onAdvEvent(String event, Npc npc, Player player)
	{
		if (!StringUtil.isDigit(event))
			return event;
		
		final int raidId = Integer.parseInt(event);
		
		// get spawn information of the raid boss
		final ASpawn spawn = SpawnManager.getInstance().getSpawn(raidId);
		if (spawn != null)
			player.getRadarList().addMarker(spawn.getSpawnLocation());
		else
		{
			// spawn information does not exist, try to find living instance
			final Npc raid = World.getInstance().getNpc(raidId);
			if (raid != null)
				player.getRadarList().addMarker(raid.getPosition());
		}
		
		return null;
	}
	
	@Override
	public String onTalk(Npc npc, Player player)
	{
		return "info.htm";
	}
}