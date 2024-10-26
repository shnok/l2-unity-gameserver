package com.shnok.javaserver.gameserver.scripting.script.ai.group;

import com.shnok.javaserver.commons.random.Rnd;

import com.shnok.javaserver.gameserver.model.actor.Npc;
import com.shnok.javaserver.gameserver.model.actor.Player;
import com.shnok.javaserver.gameserver.model.location.Location;
import com.shnok.javaserver.gameserver.network.NpcStringId;
import com.shnok.javaserver.gameserver.scripting.Quest;

/**
 * Handles teleporting NPCs, such as Toma, Merchant of Mammon, Blacksmith of Mammon and Rooney.<br>
 * When these {@link Npc}s are spawned, they will randomly change their location.
 */
public class RandomTeleport extends Quest
{
	// NPCs
	private static final int MASTER_TOMA = 30556;
	private static final int MERCHANT_OF_MAMMON = 31113;
	private static final int BLACKSMITH_OF_MAMMON = 31126;
	private static final int BLACKSMITH_OF_WIND_ROONEY = 32049;
	
	// NPC messages
	private static final NpcStringId[] BOM_SHOUTS =
	{
		NpcStringId.ID_1000431,
		NpcStringId.ID_1000432,
		NpcStringId.ID_1000433,
	};
	
	// Teleport locations
	private static final Location[] TOMA =
	{
		new Location(151572, -174829, -1781),
		new Location(154132, -220070, -3404),
		new Location(178849, -184342, -342),
	};
	private static final Location[] MOM_LOC = // Pos_1 - Pos_8
	{
		new Location(118336, 132592, -4829), // 1, Necropolis of Martyrdom
		new Location(111296, 173792, -5440), // 2, Necropolis of Worship
		new Location(83136, 208992, -5437), // 3, The Saints Necropolis
		new Location(45020, 123830, -5408), // 4, The Pilgrims Necropolis
		new Location(-21680, 77152, -5171), // 5, The Patriots Necropolis
		new Location(-52208, 78896, -4739), // 6, Necropolis of Devotion
		new Location(-41856, 209904, -5088), // 7, Necropolis of Sacrifice
		new Location(172384, -17823, -4897), // 8, The Disciple Necropolis
	};
	private static final Location[] BOM_LOC = // Pos_1 - Pos_7 (7 is not used)
	{
		new Location(12629, -248700, -9584), // 1, Catacomb of Forbidden Path
		new Location(-20533, -251012, -8161), // 2, Catacomb of the Apostate
		new Location(-53188, -250502, -7905), // 3, Catacomb of the Heretic
		new Location(46288, 170096, -4979), // 4, Catacomb of the Branded
		new Location(-19378, 13264, -4899), // 5, Catacomb of Dark Omens
		new Location(140480, 79472, -5427), // 6, Catacomb of the Witch
	};
	private static final Location[] ROONEY_LOC = // Pos_1 - Pos_39
	{
		new Location(175937, -112167, -5550),
		new Location(178896, -112425, -5860),
		new Location(180628, -115992, -6135),
		new Location(183010, -114753, -6135),
		new Location(184496, -116773, -6135),
		new Location(181857, -109491, -5865),
		new Location(178917, -107633, -5853),
		new Location(178804, -110080, -5853),
		new Location(182221, -106806, -6025),
		new Location(186488, -109715, -5915),
		new Location(183847, -119231, -3113),
		new Location(185193, -120342, -3113),
		new Location(188047, -120867, -3113),
		new Location(189734, -120471, -3113),
		new Location(188754, -118940, -3313),
		new Location(190022, -116803, -3313),
		new Location(188443, -115814, -3313),
		new Location(186421, -114614, -3313),
		new Location(185188, -113307, -3313),
		new Location(187378, -112946, -3313),
		new Location(189815, -113425, -3313),
		new Location(189301, -111327, -3313),
		new Location(190289, -109176, -3313),
		new Location(187783, -110478, -3313),
		new Location(185889, -109990, -3313),
		new Location(181881, -109060, -3695),
		new Location(183570, -111344, -3675),
		new Location(182077, -112567, -3695),
		new Location(180127, -112776, -3698),
		new Location(179155, -108629, -3695),
		new Location(176282, -109510, -3698),
		new Location(176071, -113163, -3515),
		new Location(179376, -117056, -3640),
		new Location(179760, -115385, -3640),
		new Location(177950, -119691, -4140),
		new Location(177037, -120820, -4340),
		new Location(181125, -120148, -3702),
		new Location(182212, -117969, -3352),
		new Location(186074, -118154, -3312),
	};
	
	private Location _mom = MOM_LOC[0];
	private Location _bom = BOM_LOC[0];
	
	public RandomTeleport()
	{
		super(-1, "ai/group");
		
		addCreated(MASTER_TOMA, MERCHANT_OF_MAMMON, BLACKSMITH_OF_MAMMON, BLACKSMITH_OF_WIND_ROONEY);
	}
	
	@Override
	public String onTimer(String name, Npc npc, Player player)
	{
		if (name.equalsIgnoreCase("toma"))
		{
			npc.teleportTo(Rnd.get(TOMA), 0);
		}
		else if (name.equalsIgnoreCase("mom"))
		{
			// generate new random location (that is not occupied)
			Location loc;
			do
			{
				loc = Rnd.get(MOM_LOC);
			}
			while (loc == _mom);
			
			// set MoM location and teleport
			_mom = loc;
			npc.teleportTo(_mom, 0);
		}
		else if (name.equalsIgnoreCase("bom"))
		{
			// generate new random location (that is not occupied)
			Location loc;
			do
			{
				loc = Rnd.get(BOM_LOC);
			}
			while (loc == _bom);
			
			// set BoM location and teleport
			_bom = loc;
			npc.teleportTo(_bom, 0);
			
			// shout
			npc.broadcastNpcShout(Rnd.get(BOM_SHOUTS));
		}
		else if (name.equalsIgnoreCase("rooney"))
		{
			npc.teleportTo(Rnd.get(ROONEY_LOC), 0);
		}
		return null;
	}
	
	@Override
	public void onCreated(Npc npc)
	{
		switch (npc.getNpcId())
		{
			case MASTER_TOMA:
				startQuestTimerAtFixedRate("toma", npc, null, 1800000, 1800000);
				break;
			
			case MERCHANT_OF_MAMMON:
				startQuestTimerAtFixedRate("mom", npc, null, 1800000, 1800000);
				break;
			
			case BLACKSMITH_OF_MAMMON:
				npc.broadcastNpcShout(Rnd.get(BOM_SHOUTS));
				startQuestTimerAtFixedRate("bom", npc, null, 1800000, 1800000);
				break;
			
			case BLACKSMITH_OF_WIND_ROONEY:
				startQuestTimerAtFixedRate("rooney", npc, null, 1800000, 1800000);
				break;
		}
		super.onCreated(npc);
	}
}