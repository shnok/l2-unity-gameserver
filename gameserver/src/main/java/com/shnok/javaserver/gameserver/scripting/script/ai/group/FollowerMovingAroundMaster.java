package com.shnok.javaserver.gameserver.scripting.script.ai.group;

import java.util.HashMap;
import java.util.Map;

import com.shnok.javaserver.gameserver.enums.EventHandler;
import com.shnok.javaserver.gameserver.model.actor.Npc;
import com.shnok.javaserver.gameserver.model.actor.Player;
import com.shnok.javaserver.gameserver.model.location.Location;
import com.shnok.javaserver.gameserver.scripting.Quest;

/**
 * This script is used by the 18 followers moving around their master.
 */
public class FollowerMovingAroundMaster extends Quest
{
	private static final Map<Integer, Integer> MASTERS = new HashMap<>();
	
	static
	{
		MASTERS.put(30731, 31202); // Martin, Maximus
		MASTERS.put(30827, 31203); // Lundy, Moon Dancer
		MASTERS.put(30828, 31204); // Waters, Georgio
		MASTERS.put(30829, 31205); // Cooper, Katz
		MASTERS.put(30830, 31206); // Joey, Ten Ten
		MASTERS.put(30831, 31207); // Nelson, Sardinia
		MASTERS.put(30869, 31208); // Lemper, La Grange
		MASTERS.put(31067, 31209); // Rood, Misty Rain
		MASTERS.put(31265, 31758); // Annette, Rafi
		MASTERS.put(31309, 31266); // Woods, Kaiser
		MASTERS.put(31592, 31593); // Telson, Dorothy
		MASTERS.put(31605, 31606); // Kinsley, Alice de Catrina
		MASTERS.put(31608, 31609); // Belinda, Aurora
		MASTERS.put(31614, 31629); // Radyss, Kaleidos
		MASTERS.put(31624, 31625); // Donath, Yeti
		MASTERS.put(31701, 31703); // Follower 2, Follower a
		MASTERS.put(31702, 31704); // Follower 3, Follower b
		MASTERS.put(31954, 31955); // Saroyan, Ruby
		MASTERS.put(32070, 32071); // Adolph, Linda
	}
	
	private static final Map<Integer, Npc> FOLLOWERS = new HashMap<>();
	
	public FollowerMovingAroundMaster()
	{
		super(-1, "ai/group");
		
		addEventIds(MASTERS.keySet(), EventHandler.CREATED);
	}
	
	@Override
	public String onTimer(String name, Npc npc, Player player)
	{
		if (name.equalsIgnoreCase("5001"))
		{
			// Get the master.
			final Npc master = FOLLOWERS.get(npc.getNpcId());
			
			// Clone its Location, then apply random offset.
			final Location loc = master.getPosition().clone();
			loc.addRandomOffset(50);
			
			if (!npc.isAISleeping())
				npc.getAI().addMoveToDesire(loc, 30);
		}
		return null;
	}
	
	@Override
	public void onCreated(Npc npc)
	{
		final int followerId = MASTERS.get(npc.getNpcId());
		
		// Spawn the pet related to this Npc.
		final Npc follower = addSpawn(followerId, npc, false, 0, false);
		follower.setRunning(true);
		
		// Keep the reference between follower and its master.
		FOLLOWERS.put(followerId, npc);
		
		// Start a 5s timer to make it move.
		startQuestTimerAtFixedRate("5001", follower, null, 5000, 5000);
		
		super.onCreated(npc);
	}
}