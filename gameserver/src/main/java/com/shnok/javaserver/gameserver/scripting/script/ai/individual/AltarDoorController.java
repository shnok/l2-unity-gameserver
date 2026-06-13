package com.shnok.javaserver.gameserver.scripting.script.ai.individual;

import com.shnok.javaserver.gameserver.data.manager.SpawnManager;
import com.shnok.javaserver.gameserver.model.actor.Creature;
import com.shnok.javaserver.gameserver.model.actor.Npc;
import com.shnok.javaserver.gameserver.model.actor.Player;
import com.shnok.javaserver.gameserver.model.spawn.NpcMaker;
import com.shnok.javaserver.gameserver.network.NpcStringId;

public class AltarDoorController extends DefaultNpc
{
	private static final String MAKER_NAME_UNIQUE_NPC_2ND_FLOOR = "altar_start_02";
	private static final String MAKER_NAME_JAKO_2ND_FLOOR = "altar_start_01";
	private static final String MAKER_NAME_STAGE_CON = "rune14_andreas_m2";
	
	public AltarDoorController()
	{
		super("ai/individual");
		
		addFirstTalkId(_npcIds);
	}
	
	public AltarDoorController(String descr)
	{
		super(descr);
		
		addFirstTalkId(_npcIds);
	}
	
	protected final int[] _npcIds =
	{
		32051
	};
	
	@Override
	public void onCreated(Npc npc)
	{
		npc._i_ai0 = 0;
		npc._i_ai1 = 0;
	}
	
	@Override
	public String onFirstTalk(Npc npc, Player player)
	{
		return null;
	}
	
	@Override
	public void onSeeCreature(Npc npc, Creature creature)
	{
		if (npc._i_ai0 == 0)
			broadcastScriptEvent(npc, 1, creature.getObjectId(), 4000);
		else if (npc._i_ai0 == 2)
			broadcastScriptEvent(npc, 3, creature.getObjectId(), 4000);
	}
	
	@Override
	public void onScriptEvent(Npc npc, int eventId, int arg1, int arg2)
	{
		final String doorName = getNpcStringAIParam(npc, "DoorName");
		final String doorName3rdFloor = getNpcStringAIParam(npc, "DoorName3rdFloor");
		
		if (eventId == 0)
		{
			if (doorName != null)
				openCloseDoor(doorName, 0);
			
			if (doorName3rdFloor != null)
				openCloseDoor(doorName3rdFloor, 1);
		}
		else if (eventId == 1)
		{
			NpcMaker maker0 = SpawnManager.getInstance().getNpcMaker(MAKER_NAME_UNIQUE_NPC_2ND_FLOOR);
			if (maker0 != null)
				maker0.getMaker().onMakerScriptEvent("1001", maker0, 0, 0);
			
			maker0 = SpawnManager.getInstance().getNpcMaker(MAKER_NAME_JAKO_2ND_FLOOR);
			if (maker0 != null)
				maker0.getMaker().onMakerScriptEvent("1001", maker0, 0, 0);
			
			maker0 = SpawnManager.getInstance().getNpcMaker(MAKER_NAME_STAGE_CON);
			if (maker0 != null)
				maker0.getMaker().onMakerScriptEvent("1001", maker0, 0, 0);
			
			startQuestTimer("2001", npc, null, 3 * 60 * 1000L);
		}
		else if (eventId == 2)
		{
			npc.broadcastNpcShout(NpcStringId.ID_10079);
			if (doorName3rdFloor != null)
				openCloseDoor(doorName3rdFloor, 0);
			
			startQuestTimer("2002", npc, null, 3 * 60 * 1000L);
		}
		else if (eventId == 3)
		{
			npc._i_ai1 = 1;
			startQuestTimer("2003", npc, null, 2 * 60 * 1000L);
		}
		else if (eventId == 5)
		{
			if (doorName3rdFloor != null)
				openCloseDoor(doorName3rdFloor, 0);
			
			if (doorName != null)
				openCloseDoor(doorName, 0);
			
			NpcMaker maker0 = SpawnManager.getInstance().getNpcMaker(MAKER_NAME_STAGE_CON);
			if (maker0 != null)
				maker0.getMaker().onMakerScriptEvent("1000", maker0, 0, 0);
			
			startQuestTimer("2004", npc, null, 3 * 60 * 1000L);
		}
		else if (eventId == 6)
		{
			if (doorName3rdFloor != null)
				openCloseDoor(doorName3rdFloor, 1);
			
			startQuestTimer("2005", npc, null, 3 * 60 * 1000L);
		}
		else if (eventId == 7)
		{
			if (doorName3rdFloor != null)
				openCloseDoor(doorName3rdFloor, 0);
			
			startQuestTimer("2006", npc, null, 60 * 60 * 1000L);
		}
		if (eventId < 9)
		{
			npc._i_ai0 = eventId;
		}
	}
	
	@Override
	public String onTimer(String name, Npc npc, Player player)
	{
		final String doorName = getNpcStringAIParam(npc, "DoorName");
		final String doorName3rdFloor = getNpcStringAIParam(npc, "DoorName3rdFloor");
		
		if (name.equalsIgnoreCase("2001"))
		{
			if (doorName != null)
				openCloseDoor(doorName, 1);
		}
		else if (name.equalsIgnoreCase("2002"))
		{
			if (npc._i_ai1 == 0 && doorName3rdFloor != null)
				openCloseDoor(doorName3rdFloor, 1);
		}
		else if (name.equalsIgnoreCase("2003"))
		{
			npc._i_ai1 = 0;
			startQuestTimer("2002", npc, player, 60000);
		}
		else if (name.equalsIgnoreCase("2004"))
		{
			broadcastScriptEvent(npc, 6, npc.getObjectId(), 4000);
		}
		else if (name.equalsIgnoreCase("2005"))
		{
			npc.deleteMe();
		}
		else if (name.equalsIgnoreCase("2006"))
		{
			NpcMaker maker0 = SpawnManager.getInstance().getNpcMaker(MAKER_NAME_UNIQUE_NPC_2ND_FLOOR);
			if (maker0 != null)
				maker0.getMaker().onMakerScriptEvent("1001", maker0, 0, 0);
			
			maker0 = SpawnManager.getInstance().getNpcMaker(MAKER_NAME_STAGE_CON);
			if (maker0 != null)
				maker0.getMaker().onMakerScriptEvent("1001", maker0, 0, 0);
			
			if (doorName != null)
				openCloseDoor(doorName, 0);
			
			npc._i_ai0 = 0;
			broadcastScriptEvent(npc, 0, npc.getObjectId(), 4000);
		}
		
		return super.onTimer(name, npc, player);
	}
}
