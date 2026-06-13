package com.shnok.javaserver.gameserver.scripting.script.ai.siegablehall.AzitWateringGameManager;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;

import com.shnok.javaserver.commons.random.Rnd;

import com.shnok.javaserver.gameserver.data.cache.HtmCache;
import com.shnok.javaserver.gameserver.data.manager.ClanHallManager;
import com.shnok.javaserver.gameserver.model.World;
import com.shnok.javaserver.gameserver.model.actor.Creature;
import com.shnok.javaserver.gameserver.model.actor.Npc;
import com.shnok.javaserver.gameserver.model.actor.Player;
import com.shnok.javaserver.gameserver.model.group.Party;
import com.shnok.javaserver.gameserver.network.NpcStringId;
import com.shnok.javaserver.gameserver.scripting.script.ai.individual.DefaultNpc;
import com.shnok.javaserver.gameserver.scripting.script.siegablehall.RainbowSpringsChateau;
import com.shnok.javaserver.gameserver.skills.L2Skill;

public class AzitWateringGameManager extends DefaultNpc
{
	private static final Map<String, Integer> CHAR_ITEM_MAP = new HashMap<>();
	
	private static final String[] QUIZ_STRINGS =
	{
		"BABYDUCK",
		"ALBATROS",
		"PELICAN",
		"KINGFISHER",
		"CYGNUS",
		"TRITON",
		"RAINBOW",
		"SPRING"
	};
	
	protected static int NUMBER_OF_CIRCLE = 0;
	protected static int GAME_MASTER = 0;
	
	protected static String AREA_DATA_DEBUFF = "azit_watering_game_manager_default";
	
	private final List<Player> _playerList = new ArrayList<>();
	
	public AzitWateringGameManager()
	{
		super("ai/siegeablehall/AzitWateringGameManager");
		
		CHAR_ITEM_MAP.put("A", 8035);
		CHAR_ITEM_MAP.put("B", 8036);
		CHAR_ITEM_MAP.put("C", 8037);
		CHAR_ITEM_MAP.put("D", 8038);
		CHAR_ITEM_MAP.put("E", 8039);
		CHAR_ITEM_MAP.put("F", 8040);
		CHAR_ITEM_MAP.put("G", 8041);
		CHAR_ITEM_MAP.put("H", 8042);
		CHAR_ITEM_MAP.put("I", 8043);
		CHAR_ITEM_MAP.put("K", 8045);
		CHAR_ITEM_MAP.put("L", 8046);
		CHAR_ITEM_MAP.put("N", 8047);
		CHAR_ITEM_MAP.put("O", 8048);
		CHAR_ITEM_MAP.put("P", 8049);
		CHAR_ITEM_MAP.put("R", 8050);
		CHAR_ITEM_MAP.put("S", 8051);
		CHAR_ITEM_MAP.put("T", 8052);
		CHAR_ITEM_MAP.put("U", 8053);
		CHAR_ITEM_MAP.put("W", 8054);
		CHAR_ITEM_MAP.put("Y", 8055);
	}
	
	public AzitWateringGameManager(String descr)
	{
		super(descr);
	}
	
	@Override
	public void onCreated(Npc npc)
	{
		_playerList.clear();
		
		npc._i_ai0 = 0;
		npc._i_ai1 = 0;
		npc._i_ai2 = 0;
		npc._i_ai4 = 0;
		npc._c_ai0 = null;
		npc._c_ai1 = null;
		npc._c_ai2 = null;
		npc._c_ai3 = null;
		npc._c_ai4 = null;
		npc._i_quest0 = -1;
		npc._i_quest1 = 0;
		npc._i_quest2 = 0;
		npc._i_quest3 = 0;
		npc._i_quest4 = 0;
		npc._c_quest0 = null;
		npc._c_quest4 = null;
		
		startQuestTimer("1004", npc, null, (6 * 10000));
		startQuestTimer("1005", npc, null, 45000);
		startQuestTimer("1001", npc, null, 10000);
	}
	
	@Override
	public String onFirstTalk(Npc npc, Player player)
	{
		final Party party0 = player.getParty();
		if (party0 != null && npc._i_ai2 == 0)
		{
			_playerList.addAll(party0.getMembers());
			
			npc._i_ai2 = 1;
		}
		
		return HtmCache.getInstance().getHtm("data/html/script/siegablehall/RainbowSpringsChateau/" + npc.getNpcId() + "/watering_manager001.htm");
	}
	
	@Override
	public String onAdvEvent(String event, Npc npc, Player player)
	{
		String html = "";
		
		if (event.equalsIgnoreCase("giveWord"))
		{
			if (npc._i_quest0 == -1)
				npc.broadcastNpcSay(NpcStringId.ID_1010628);
			else if (npc._i_quest0 >= 0 && npc._i_quest0 <= 7)
			{
				if (checkWordAndGiveItems(npc, player, QUIZ_STRINGS[npc._i_quest0]))
					html = HtmCache.getInstance().getHtm("data/html/script/siegablehall/RainbowSpringsChateau/watering_manager004.htm");
				else
					html = HtmCache.getInstance().getHtm("data/html/script/siegablehall/RainbowSpringsChateau/watering_manager002.htm");
			}
		}
		else if (event.equalsIgnoreCase("showCurrentWord"))
		{
			String currentWordReply = HtmCache.getInstance().getHtm("data/html/script/siegablehall/RainbowSpringsChateau/watering_manager005.htm");
			
			if (npc._i_quest0 == -1)
				currentWordReply = currentWordReply.replace("%quizstring%", NpcStringId.ID_1010635.getMessage());
			else if (npc._i_quest0 >= 0 && npc._i_quest0 <= 7)
				currentWordReply = currentWordReply.replace("%quizstring%", QUIZ_STRINGS[npc._i_quest0]);
			
			html = currentWordReply;
		}
		else if (event.equalsIgnoreCase("howTo"))
		{
			html = HtmCache.getInstance().getHtm("data/html/script/siegablehall/RainbowSpringsChateau/watering_manager003.htm");
		}
		
		return html;
	}
	
	@Override
	public void onScriptEvent(Npc npc, int eventId, int arg1, int arg2)
	{
		if (eventId == 10002)
		{
			npc._c_ai0 = (Creature) World.getInstance().getObject(arg1);
		}
		else if (eventId == 10003)
		{
			npc._c_ai1 = (Creature) World.getInstance().getObject(arg1);
		}
		else if (eventId == 10004)
		{
			npc._c_ai2 = (Creature) World.getInstance().getObject(arg1);
		}
		else if (eventId == 10005)
		{
			npc._c_ai3 = (Creature) World.getInstance().getObject(arg1);
		}
		else if (eventId == NUMBER_OF_CIRCLE && arg1 == 30000)
		{
			npc._i_quest1 = 1;
			npc._i_quest2 = arg2;
		}
		else if (eventId == NUMBER_OF_CIRCLE && npc._i_quest1 == 1 && npc._i_quest2 == arg2)
		{
			npc._i_quest1 = 2;
			npc._i_quest3 = arg2;
			
			if (npc._i_ai0 == 1)
			{
				npc.getAI().addCastDesireHold(npc._c_quest4, 4989, 1, 10000000);
				npc._i_ai0 = 0;
			}
			else if (npc._i_ai1 == 1)
			{
				npc.getAI().addCastDesireHold(npc._c_quest4, 4990, 1, 10000000);
				npc._i_ai1 = 0;
			}
			else if (npc._i_quest0 == 1 && npc._i_quest3 == 0)
			{
				broadcastScriptEventEx(npc, npc._i_quest2, (int) npc._c_quest4.getStatus().getHp(), 0, 10000);
			}
			else if (npc._i_quest1 == 2)
			{
				int i1 = (int) npc._c_quest4.getStatus().getHp();
				npc._c_quest4.getStatus().setHp(npc._i_quest3);
				
				if (npc._i_quest3 <= 1)
					npc._c_quest4.doDie(npc._c_ai4);
				
				broadcastScriptEventEx(npc, npc._i_quest2, 30000, i1, 10000);
				npc._i_quest1 = 0;
				npc._i_quest2 = 0;
				npc._i_quest3 = 0;
			}
		}
		else if (eventId == NUMBER_OF_CIRCLE && arg1 == 20001)
		{
			// TODO Area
			// gg::Area_SetOnOff(AreaData_DeBuff,1);
			startQuestTimer("5001", npc, null, 60000);
		}
		else if (eventId == 1 && arg1 == 10000)
		{
			npc._i_quest0 = arg2;
			
			npc.broadcastOnScreen(10000, QUIZ_STRINGS[npc._i_quest0]);
		}
		else if (eventId == 5 && arg1 == 40000)
		{
			final RainbowSpringsChateau rsc = (RainbowSpringsChateau) ClanHallManager.getInstance().getSiegableHall(62).getSiege();
			if (npc._i_quest4 != 0)
			{
				final Creature c0 = (Creature) World.getInstance().getObject(npc._i_quest4);
				if (c0 != null && npc.distance2D(c0) < 1000)
				{
					rsc.setMiniGameWinner((Npc) npc._c_quest4, (Player) c0);
					broadcastScriptEvent(npc, 0, 20002, 8000);
				}
				else
				{
					for (Player pl : _playerList)
					{
						rsc.setMiniGameWinner((Npc) npc._c_quest4, pl);
						broadcastScriptEvent(npc, 0, 20002, 8000);
						return;
					}
				}
			}
			else
			{
				final Creature c0 = (Creature) World.getInstance().getObject(npc._i_ai1);
				if (c0 != null && npc.distance2D(c0) < 1000)
				{
					rsc.setMiniGameWinner((Npc) npc._c_quest4, (Player) c0);
					broadcastScriptEvent(npc, 0, 20002, 8000);
				}
				else
				{
					for (Player pl : _playerList)
					{
						rsc.setMiniGameWinner((Npc) npc._c_quest4, pl);
						broadcastScriptEvent(npc, 0, 20002, 8000);
						return;
					}
				}
			}
		}
		else if (arg1 == 20002)
		{
			// TODO Area
			// gg::Area_SetOnOff(AreaData_DeBuff,0);
		}
		else if (eventId == 5 && arg1 == 40000)
		{
			broadcastScriptEvent(npc, NUMBER_OF_CIRCLE, 50000, 1000);
			
			npc._i_quest4 = arg2;
			
			startQuestTimer("5005", npc, null, 1000);
		}
		else if (eventId == NUMBER_OF_CIRCLE && arg1 == 50000)
		{
			npc.getAI().addCastDesire(npc, 5039, 1, 1000000);
		}
		else if (eventId == NUMBER_OF_CIRCLE && arg1 == 30003)
		{
			if (npc._c_quest4 != null)
				npc.getAI().addCastDesireHold(npc._c_quest4, 4990, 1, 1000000);
		}
	}
	
	@Override
	public String onTimer(String name, Npc npc, Player player)
	{
		if (name.equalsIgnoreCase("5001"))
		{
			// TODO Area
			// gg::Area_SetOnOff(AreaData_DeBuff,0);
		}
		else if (name.equalsIgnoreCase("5002"))
		{
			if (npc._i_ai0 == 1)
			{
				npc.getAI().addCastDesireHold(npc._c_quest4, 4989, 1, 10000000);
				npc._i_ai0 = 0;
			}
			else if (npc._i_ai1 == 1)
			{
				npc.getAI().addCastDesireHold(npc._c_quest4, 4990, 1, 10000000);
				npc._i_ai1 = 0;
			}
			else if (npc._i_quest0 == 1 && npc._i_quest3 == 0)
			{
				broadcastScriptEventEx(npc, npc._i_quest2, (int) npc._c_quest4.getStatus().getHp(), 0, 10000);
			}
			else if (npc._i_quest1 == 2)
			{
				int i1 = (int) npc._c_quest4.getStatus().getHp();
				npc._c_quest4.getStatus().setHp(npc._i_quest3);
				
				if (npc._i_quest3 <= 1)
					npc._c_quest4.doDie(npc._c_ai4);
				
				broadcastScriptEventEx(npc, npc._i_quest2, 30000, i1, 10000);
				
				npc._i_quest1 = 0;
				npc._i_quest2 = 0;
				npc._i_quest3 = 0;
			}
		}
		else if (name.equalsIgnoreCase("5005"))
		{
			broadcastScriptEventEx(npc, NUMBER_OF_CIRCLE, 40000, 0, 1000);
		}
		else if (name.equalsIgnoreCase("1001"))
		{
			npc.lookNeighbor();
			
			if (GAME_MASTER == 1)
			{
				npc.broadcastNpcShout(NpcStringId.ID_1010633);
				startQuestTimer("2001", npc, player, 1000);
			}
		}
		else if (name.equalsIgnoreCase("9999"))
		{
			npc.lookNeighbor();
			startQuestTimer("9999", npc, null, 1000);
		}
		else if (name.equalsIgnoreCase("2001"))
		{
			// Avoids game managers to do this 4 times, just 1 is enough since it's being broadcasted
			if (NUMBER_OF_CIRCLE != 1)
				return null;
			
			final int i0 = Rnd.get(8);
			
			npc.broadcastOnScreen(10000, QUIZ_STRINGS[i0]);
			npc._i_quest0 = i0;
			
			broadcastScriptEventEx(npc, 1, 10000, i0, 10000);
			
			startQuestTimer("2001", npc, null, ((3 * 60) * 1000));
		}
		else if (name.equalsIgnoreCase("1004"))
		{
			npc.lookNeighbor();
			
			if (npc._c_quest4 != null && npc._c_ai4 != null)
			{
				final Party party0 = npc._c_ai4.getParty();
				if (party0 != null)
				{
					final int i2 = (int) (npc._c_quest4.getStatus().getHpRatio() * 100);
					npc.broadcastNpcShout(NpcStringId.ID_1010634, party0.getLeader().getName(), i2);
				}
			}
			startQuestTimer("1004", npc, null, ((3 * 60) * 1000));
		}
		else if (name.equalsIgnoreCase("1005"))
		{
			npc.lookNeighbor();
		}
		return null;
	}
	
	@Override
	public void onSpelled(Npc npc, Player caster, L2Skill skill)
	{
		Party party0 = caster.getParty();
		if (party0 != null && npc._i_ai2 == 0)
		{
			_playerList.addAll(party0.getMembers());
			
			npc._i_ai2 = 1;
		}
		
		if (skill.getId() == 2240)
		{
			final int i0 = Rnd.get(100);
			if (i0 < 90)
			{
				npc._i_ai0 = 1;
				
				if (npc._i_ai0 == 1)
				{
					npc.getAI().addCastDesireHold(npc._c_quest4, 4989, 1, 10000000);
					npc._i_ai0 = 0;
				}
				else if (npc._i_ai1 == 1)
				{
					npc.getAI().addCastDesireHold(npc._c_quest4, 4990, 1, 10000000);
					npc._i_ai1 = 0;
				}
				else if (npc._i_quest0 == 1 && npc._i_quest3 == 0)
				{
					broadcastScriptEventEx(npc, npc._i_quest2, (int) npc._c_quest4.getStatus().getHp(), 0, 10000);
				}
				else if (npc._i_quest1 == 2)
				{
					final int i1 = (int) npc._c_quest4.getStatus().getHp();
					
					npc._c_quest4.getStatus().setHp(npc._i_quest3);
					
					if (npc._i_quest3 <= 1)
						npc._c_quest4.doDie(npc._c_ai4);
					
					broadcastScriptEventEx(npc, npc._i_quest2, 30000, i1, 10000);
					
					npc._i_quest1 = 0;
					npc._i_quest2 = 0;
					npc._i_quest3 = 0;
				}
				npc._i_ai1 = caster.getObjectId();
			}
			else
			{
				createOnePrivateEx(npc, 35592, npc.getX() + 10, npc.getY() + 10, npc.getZ(), 0, 0, false);
			}
		}
		else if (skill.getId() == 2241)
		{
			for (int i2 = 1; i2 < 5; i2++)
			{
				if (i2 != NUMBER_OF_CIRCLE)
				{
					broadcastScriptEventEx(npc, i2, 30003, 0, 10000);
				}
			}
		}
		else if (skill.getId() == 2242)
		{
			npc.lookNeighbor();
			
			if (npc._c_ai4 != null)
			{
				party0 = npc._c_ai4.getParty();
				if (party0 != null)
					npc.broadcastNpcShout(NpcStringId.ID_1010641, party0.getLeader().getName());
			}
			
			int i0 = (Rnd.get(4) + 1);
			int i4 = 0;
			int i5 = 0;
			int i6 = 0;
			
			for (int i2 = 1; i2 < 5; i2++)
			{
				if (i2 != NUMBER_OF_CIRCLE)
				{
					if (i4 == 0)
						i4 = i2;
					else if (i5 == 0)
						i5 = i2;
					else if (i6 == 0)
						i6 = i2;
				}
				
				final int i7 = Rnd.get(100);
				if (i7 < 33)
					i0 = i4;
				else if (i7 < 66)
					i0 = i5;
				else
					i0 = i6;
			}
			
			Creature c0 = null;
			
			if (i0 == 1)
			{
				if (npc._c_ai0 != null)
					c0 = npc._c_ai0;
			}
			else if (i0 == 2)
			{
				if (npc._c_ai1 != null)
					c0 = npc._c_ai1;
			}
			else if (i0 == 3)
			{
				if (npc._c_ai2 != null)
					c0 = npc._c_ai2;
			}
			else if (i0 == 4)
			{
				if (npc._c_ai3 != null)
					c0 = npc._c_ai3;
			}
			
			if (c0 != null && npc._c_quest4 != null)
			{
				npc._c_quest4.getStatus().setHp(c0.getStatus().getHp());
				c0.getStatus().setHp(npc._c_quest4.getStatus().getHp());
				
				if (npc._c_quest4.getStatus().getHp() <= 1)
					npc._c_quest4.doDie(npc._c_ai4);
				
				if (c0.getStatus().getHp() <= 1)
					c0.doDie(npc._c_ai4);
			}
		}
		else if (skill.getId() == 2243)
		{
			npc.lookNeighbor();
			
			if (npc._c_ai4 != null)
			{
				party0 = npc._c_ai4.getParty();
				if (party0 != null)
					npc.broadcastNpcShout(NpcStringId.ID_1010640, party0.getLeader().getName());
			}
			
			for (int i2 = 1; i2 < 5; i2++)
			{
				if (i2 != NUMBER_OF_CIRCLE)
					broadcastScriptEventEx(npc, i2, 20001, 0, 10000);
			}
		}
	}
	
	@Override
	public void onSeeCreature(Npc npc, Creature creature)
	{
		if (creature instanceof Npc npcCreature && npc._c_quest4 == null)
		{
			final int npcId = npcCreature.getNpcId();
			if (npcId >= 35588 && npcId <= 35591)
				npc._c_quest4 = creature;
		}
		
		if (creature instanceof Player)
			npc._c_ai4 = creature;
	}
	
	private static boolean checkWordAndGiveItems(Npc npc, Player player, String word)
	{
		// Use a temporary Map.
		final Map<Integer, Integer> tempMap = new HashMap<>();
		
		// For each character from the word, put if not existing (set to 1), or if already existing, increment.
		for (char ch : word.toCharArray())
		{
			final int itemId = CHAR_ITEM_MAP.get(String.valueOf(ch));
			
			tempMap.merge(itemId, 1, (k, v) -> k + v);
		}
		
		final Set<Entry<Integer, Integer>> entries = tempMap.entrySet();
		
		// For each couple of itemId / count, verify if items exist in Player's inventory.
		for (Entry<Integer, Integer> entry : entries)
		{
			if (player.getInventory().getItemCount(entry.getKey()) < entry.getValue())
				return false;
		}
		
		// Delete items from Player's inventory.
		for (Entry<Integer, Integer> entry : entries)
			takeItems(player, entry.getKey(), entry.getValue());
		
		final int i0 = Rnd.get(100);
		
		if (npc._i_quest0 >= 1 && npc._i_quest0 <= 6)
		{
			if (i0 < 70)
				giveItems(player, 8030, 1);
			else if (i0 < 80)
				giveItems(player, 8031, 1);
			else if (i0 < 90)
				giveItems(player, 8032, 1);
			else
				giveItems(player, 8033, 1);
		}
		else
		{
			if (i0 < 10)
				giveItems(player, 8030, 1);
			else if (i0 < 40)
				giveItems(player, 8031, 1);
			else if (i0 < 70)
				giveItems(player, 8032, 1);
			else
				giveItems(player, 8033, 1);
		}
		return true;
	}
}