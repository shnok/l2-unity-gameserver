package com.shnok.javaserver.gameserver.scripting.script.teleport;

import java.util.HashMap;
import java.util.Map;

import com.shnok.javaserver.commons.util.ArraysUtil;

import com.shnok.javaserver.gameserver.enums.EventHandler;
import com.shnok.javaserver.gameserver.model.actor.Npc;
import com.shnok.javaserver.gameserver.model.actor.Player;
import com.shnok.javaserver.gameserver.model.location.Location;
import com.shnok.javaserver.gameserver.network.SystemMessageId;
import com.shnok.javaserver.gameserver.scripting.Quest;

public class OracleTeleporter extends Quest
{
	private static final int[] TOWN_DAWN =
	{
		31078,
		31079,
		31080,
		31081,
		31082,
		31083,
		31084,
		31692,
		31694,
		31997,
		31168
	};
	
	private static final int[] TOWN_DUSK =
	{
		31085,
		31086,
		31087,
		31088,
		31089,
		31090,
		31091,
		31693,
		31695,
		31998,
		31169
	};
	
	private static final int[] TEMPLE_PRIEST =
	{
		31127,
		31128,
		31129,
		31130,
		31131,
		31137,
		31138,
		31139,
		31140,
		31141
	};
	
	private static final Map<Integer, Location> RETURN_LOCATIONS = new HashMap<>();
	{
		// Priest of Dusk/Dawn
		RETURN_LOCATIONS.put(31078, new Location(-80555, 150337, -3040));
		RETURN_LOCATIONS.put(31079, new Location(-13953, 121404, -2984));
		RETURN_LOCATIONS.put(31080, new Location(16354, 142820, -2696));
		RETURN_LOCATIONS.put(31081, new Location(83369, 149253, -3400));
		RETURN_LOCATIONS.put(31082, new Location(111386, 220858, -3544));
		RETURN_LOCATIONS.put(31083, new Location(83106, 53965, -1488));
		RETURN_LOCATIONS.put(31084, new Location(146983, 26595, -2200));
		RETURN_LOCATIONS.put(31085, new Location(-82368, 151568, -3120));
		RETURN_LOCATIONS.put(31086, new Location(-14748, 123995, -3112));
		RETURN_LOCATIONS.put(31087, new Location(18482, 144576, -3056));
		RETURN_LOCATIONS.put(31088, new Location(81623, 148556, -3464));
		RETURN_LOCATIONS.put(31089, new Location(112486, 220123, -3592));
		RETURN_LOCATIONS.put(31090, new Location(82819, 54607, -1520));
		RETURN_LOCATIONS.put(31091, new Location(147570, 28877, -2264));
		
		// Gatekeeper Ziggurat
		RETURN_LOCATIONS.put(31095, new Location(-41561, 209225, -5087));
		RETURN_LOCATIONS.put(31096, new Location(45242, 124466, -5413));
		RETURN_LOCATIONS.put(31097, new Location(110711, 174010, -5439));
		RETURN_LOCATIONS.put(31098, new Location(-22341, 77375, -5173));
		RETURN_LOCATIONS.put(31099, new Location(-52889, 79098, -4741));
		RETURN_LOCATIONS.put(31100, new Location(117760, 132794, -4831));
		RETURN_LOCATIONS.put(31101, new Location(171792, -17609, -4901));
		RETURN_LOCATIONS.put(31102, new Location(82564, 209207, -5439));
		RETURN_LOCATIONS.put(31103, new Location(-41565, 210048, -5085));
		RETURN_LOCATIONS.put(31104, new Location(45278, 123608, -5411));
		RETURN_LOCATIONS.put(31105, new Location(111510, 174013, -5437));
		RETURN_LOCATIONS.put(31106, new Location(-21489, 77372, -5171));
		RETURN_LOCATIONS.put(31107, new Location(-52016, 79103, -4739));
		RETURN_LOCATIONS.put(31108, new Location(118557, 132804, -4829));
		RETURN_LOCATIONS.put(31109, new Location(172570, -17605, -4899));
		RETURN_LOCATIONS.put(31110, new Location(83347, 209215, -5437));
		
		// Gatekeeper Ziggurat
		RETURN_LOCATIONS.put(31114, new Location(42495, 143944, -5381));
		RETURN_LOCATIONS.put(31115, new Location(45666, 170300, -4981));
		RETURN_LOCATIONS.put(31116, new Location(77138, 78389, -5125));
		RETURN_LOCATIONS.put(31117, new Location(139903, 79674, -5429));
		RETURN_LOCATIONS.put(31118, new Location(-20021, 13499, -4901));
		RETURN_LOCATIONS.put(31119, new Location(113418, 84535, -6541));
		RETURN_LOCATIONS.put(31120, new Location(-52940, -250272, -7907));
		RETURN_LOCATIONS.put(31121, new Location(46499, 170301, -4979));
		RETURN_LOCATIONS.put(31122, new Location(-20280, -250785, -8163));
		RETURN_LOCATIONS.put(31123, new Location(140673, 79680, -5437));
		RETURN_LOCATIONS.put(31124, new Location(-19182, 13503, -4899));
		RETURN_LOCATIONS.put(31125, new Location(12837, -248483, -9579));
		
		// Priest of Dusk/Dawn
		RETURN_LOCATIONS.put(31168, new Location(115136, 74717, -2608));
		RETURN_LOCATIONS.put(31169, new Location(116642, 77510, -2688));
		
		// Priest of Dusk/Dawn
		RETURN_LOCATIONS.put(31692, new Location(148256, -55454, -2779));
		RETURN_LOCATIONS.put(31693, new Location(149888, -56574, -2979));
		RETURN_LOCATIONS.put(31694, new Location(45664, -50318, -800));
		RETURN_LOCATIONS.put(31695, new Location(44528, -48370, -800));
		
		// Priest of Dusk/Dawn
		RETURN_LOCATIONS.put(31997, new Location(86795, -143078, -1341));
		RETURN_LOCATIONS.put(31998, new Location(85129, -142103, -1542));
	}
	
	public OracleTeleporter()
	{
		super(-1, "teleport");
		
		addTalkId(TEMPLE_PRIEST);
		
		addEventIds(RETURN_LOCATIONS.keySet(), EventHandler.TALKED);
	}
	
	@Override
	public String onAdvEvent(String event, Npc npc, Player player)
	{
		String htmltext = "";
		
		if (event.equalsIgnoreCase("Return"))
		{
			final int id = player.getMemos().getInteger("OracleTeleport_Npc", -1);
			if (id >= 0)
			{
				player.teleportTo(RETURN_LOCATIONS.get(id), 0);
				
				if (ArraysUtil.contains(TEMPLE_PRIEST, npc.getNpcId()))
					player.setIsIn7sDungeon(false);
			}
		}
		else if (event.equalsIgnoreCase("Festival"))
		{
			final int id = player.getMemos().getInteger("OracleTeleport_Npc", -1);
			if (ArraysUtil.contains(TOWN_DAWN, id))
			{
				player.teleportTo(-80157, 111344, -4901, 0);
				player.setIsIn7sDungeon(true);
			}
			else if (ArraysUtil.contains(TOWN_DUSK, id))
			{
				player.teleportTo(-81261, 86531, -5157, 0);
				player.setIsIn7sDungeon(true);
			}
			else
				htmltext = "oracle1.htm";
		}
		else if (event.equalsIgnoreCase("5.htm"))
		{
			final int id = player.getMemos().getInteger("OracleTeleport_Npc", -1);
			if (id >= 0)
				htmltext = "5a.htm";
			
			player.getMemos().set("OracleTeleport_Npc", npc.getNpcId());
			player.teleportTo(-114755, -179466, -6752, 0);
		}
		else if (event.equalsIgnoreCase("zigurratDimensional"))
		{
			final int level = player.getStatus().getLevel();
			if (level >= 20 && level < 30)
				takeItems(player, 57, 2000);
			else if (level >= 30 && level < 40)
				takeItems(player, 57, 4500);
			else if (level >= 40 && level < 50)
				takeItems(player, 57, 8000);
			else if (level >= 50 && level < 60)
				takeItems(player, 57, 12500);
			else if (level >= 60 && level < 70)
				takeItems(player, 57, 18000);
			else if (level >= 70)
				takeItems(player, 57, 24500);
			
			htmltext = "ziggurat_rift.htm";
			
			player.getMemos().set("OracleTeleport_Npc", npc.getNpcId());
			player.teleportTo(-114755, -179466, -6752, 0);
		}
		else if (event.equalsIgnoreCase("Dimensional"))
		{
			htmltext = "oracle.htm";
			
			player.teleportTo(-114796, -179334, -6752, 0);
		}
		return htmltext;
	}
	
	@Override
	public String onTalk(Npc npc, Player player)
	{
		String htmltext = "";
		
		final int npcId = npc.getNpcId();
		
		if (ArraysUtil.contains(TOWN_DAWN, npcId))
		{
			player.getMemos().set("OracleTeleport_Npc", npcId);
			player.teleportTo(-80157, 111344, -4901, 0);
			player.setIsIn7sDungeon(true);
		}
		else if (ArraysUtil.contains(TOWN_DUSK, npcId))
		{
			player.getMemos().set("OracleTeleport_Npc", npcId);
			player.teleportTo(-81261, 86531, -5157, 0);
			player.setIsIn7sDungeon(true);
		}
		else if ((npcId >= 31095 && npcId <= 31111) || (npcId >= 31114 && npcId <= 31126))
		{
			final int level = player.getStatus().getLevel();
			if (level < 20)
				htmltext = "ziggurat_lowlevel.htm";
			else if (player.getQuestList().getAllQuests(false).size() >= 25)
				player.sendPacket(SystemMessageId.TOO_MANY_QUESTS);
			else if (!player.getInventory().hasItem(7079))
				htmltext = "ziggurat_nofrag.htm";
			else if (level < 30 && player.getAdena() < 2000)
				htmltext = "ziggurat_noadena.htm";
			else if (level < 40 && player.getAdena() < 4500)
				htmltext = "ziggurat_noadena.htm";
			else if (level < 50 && player.getAdena() < 8000)
				htmltext = "ziggurat_noadena.htm";
			else if (level < 60 && player.getAdena() < 12500)
				htmltext = "ziggurat_noadena.htm";
			else if (level < 70 && player.getAdena() < 18000)
				htmltext = "ziggurat_noadena.htm";
			else if (player.getAdena() < 24500)
				htmltext = "ziggurat_noadena.htm";
			else
				htmltext = "ziggurat.htm";
		}
		
		return htmltext;
	}
}