package com.shnok.javaserver.gameserver.scripting.task;

import java.util.HashMap;
import java.util.Map;

import com.shnok.javaserver.gameserver.data.manager.RaidPointManager;
import com.shnok.javaserver.gameserver.data.sql.ClanTable;
import com.shnok.javaserver.gameserver.model.pledge.Clan;
import com.shnok.javaserver.gameserver.scripting.ScheduledQuest;

/**
 * Each month players hunt Raid Bosses and get raid points. At the end of the month top 100 players from the list get clan reputation points.<br>
 * <br>
 * The points are added after the first weekly game maintenance of the month.<br>
 * <br>
 * Only the players belonging to clans level 5 or higher can get such points.
 */
public final class RaidPointReset extends ScheduledQuest
{
	public RaidPointReset()
	{
		super(-1, "task");
	}
	
	@Override
	public final void onStart()
	{
		// Calculate ranks for the first 100 players.
		final Map<Integer, Integer> ranks = RaidPointManager.getInstance().getWinners();
		
		// A temporary Map used to gather points for each clan. In the end we reward all clans.
		final Map<Clan, Integer> rewards = new HashMap<>();
		
		// Iterate clans.
		for (Clan clan : ClanTable.getInstance().getClans())
		{
			// Don't bother with low level clans.
			if (clan.getLevel() < 5)
				continue;
			
			// Iterate ranks.
			for (Map.Entry<Integer, Integer> entry : ranks.entrySet())
			{
				// Don't bother if checked player isn't member of the clan.
				if (!clan.isMember(entry.getKey()))
					continue;
				
				int points = 0;
				switch (entry.getValue())
				{
					case 1:
						points = 1250;
						break;
					
					case 2:
						points = 900;
						break;
					
					case 3:
						points = 700;
						break;
					
					case 4:
						points = 600;
						break;
					
					case 5:
						points = 450;
						break;
					
					case 6:
						points = 350;
						break;
					
					case 7:
						points = 300;
						break;
					
					case 8:
						points = 200;
						break;
					
					case 9:
						points = 150;
						break;
					
					case 10:
						points = 100;
						break;
					
					default:
						points = (entry.getValue() <= 50) ? 25 : 12;
						break;
				}
				rewards.merge(clan, points, Integer::sum);
			}
		}
		
		// Iterate and reward CRPs to clans.
		for (Map.Entry<Clan, Integer> entry : rewards.entrySet())
			entry.getKey().addReputationScore(entry.getValue());
		
		// Cleanup the data.
		RaidPointManager.getInstance().cleanUp();
	}
	
	@Override
	public final void onEnd()
	{
		// Do nothing.
	}
}