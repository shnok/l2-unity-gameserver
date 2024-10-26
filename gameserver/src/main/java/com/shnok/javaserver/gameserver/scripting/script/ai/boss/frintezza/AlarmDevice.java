package com.shnok.javaserver.gameserver.scripting.script.ai.boss.frintezza;

import net.sf.l2j.commons.random.Rnd;

import net.sf.l2j.gameserver.data.manager.SpawnManager;
import net.sf.l2j.gameserver.model.actor.Creature;
import net.sf.l2j.gameserver.model.actor.Npc;
import net.sf.l2j.gameserver.model.spawn.NpcMaker;
import net.sf.l2j.gameserver.network.NpcStringId;
import net.sf.l2j.gameserver.scripting.script.ai.individual.DefaultNpc;

public class AlarmDevice extends DefaultNpc
{
	private static final String T_DOOR_NAME_1 = "grave_pathway_1";
	private static final String T_DOOR_NAME_2 = "grave_pathway_2";
	
	private static final String WALL_DOOR_NAME_1 = "wall_door_a_center_1";
	private static final String WALL_DOOR_NAME_2 = "wall_door_a_center_2";
	private static final String WALL_DOOR_NAME_3 = "wall_door_a_center_3";
	private static final String WALL_DOOR_NAME_4 = "wall_door_a_center_4";
	private static final String WALL_DOOR_NAME_5 = "wall_door_a_center_5";
	private static final String WALL_DOOR_NAME_6 = "wall_door_a_center_6";
	private static final String WALL_DOOR_NAME_7 = "wall_door_a_center_7";
	private static final String WALL_DOOR_NAME_8 = "wall_door_a_center_8";
	
	// private static final String DMG_ZONE = "25_15_frintessaE_01";
	
	private static final String MAKER_NAME_SELF_DES = "godard32_2515_09m1";
	private static final String MAKER_NAME_GUARD = "godard32_2515_06m1";
	private static final String MAKER_NAME_PATROL = "godard32_2515_05m1";
	private static final String MAKER_NAME_CAPTAIN = "godard32_2515_07m1";
	private static final String MAKER_NAME_WIZARD_1 = "godard32_2515_01m1";
	private static final String MAKER_NAME_WIZARD_2 = "godard32_2515_02m1";
	private static final String MAKER_NAME_WIZARD_3 = "godard32_2515_03m1";
	private static final String MAKER_NAME_WIZARD_4 = "godard32_2515_04m1";
	private static final String MAKER_NAME_WIZARD_5 = "godard32_2515_08m1";
	private static final String MAKER_NAME_ALARM_1 = "godard32_2515_22m1";
	private static final String MAKER_NAME_ALARM_2 = "godard32_2515_19m1";
	private static final String MAKER_NAME_ALARM_3 = "godard32_2515_20m1";
	private static final String MAKER_NAME_ALARM_4 = "godard32_2515_21m1";
	
	public AlarmDevice()
	{
		super("ai/boss/frintezza");
	}
	
	public AlarmDevice(String descr)
	{
		super(descr);
	}
	
	protected final int[] _npcIds =
	{
		18328 // hall_alarm_device
	};
	
	@Override
	public void onCreated(Npc npc)
	{
		npc._i_ai0 = 0;
		
		// TODO Zones
		// gg::Area_SetOnOff(dmgzone,1);
		
		npc.broadcastNpcShout(NpcStringId.ID_1010630);
	}
	
	@Override
	public void onScriptEvent(Npc npc, int eventId, int arg1, int arg2)
	{
		if (eventId == 10025)
		{
			npc._i_ai0 = arg1;
		}
	}
	
	@Override
	public void onMyDying(Npc npc, Creature killer)
	{
		int i0 = 0;
		
		if (npc._i_ai0 == 1)
		{
			final NpcMaker maker0 = SpawnManager.getInstance().getNpcMaker(MAKER_NAME_CAPTAIN);
			if (maker0 != null)
			{
				if (Rnd.get(100) < 10)
					i0 = 100;
				else
					maker0.getMaker().onMakerScriptEvent("1001", maker0, 0, 0);
			}
		}
		else if (npc._i_ai0 == 2)
		{
			final NpcMaker maker0 = SpawnManager.getInstance().getNpcMaker(MAKER_NAME_SELF_DES);
			if (maker0 != null)
			{
				if (Rnd.get(100) < 20)
					i0 = 100;
				else
					maker0.getMaker().onMakerScriptEvent("1001", maker0, 0, 0);
			}
		}
		else if (npc._i_ai0 == 3)
		{
			final NpcMaker maker0 = SpawnManager.getInstance().getNpcMaker(MAKER_NAME_GUARD);
			if (maker0 != null)
			{
				if (Rnd.get(100) < 30)
					i0 = 100;
				else
					maker0.getMaker().onMakerScriptEvent("1001", maker0, 0, 0);
			}
		}
		else if (npc._i_ai0 == 4)
			i0 = 100;
		
		if (i0 == 100)
		{
			npc.broadcastNpcShout(NpcStringId.ID_1010631);
			
			NpcMaker maker0 = SpawnManager.getInstance().getNpcMaker(MAKER_NAME_ALARM_1);
			if (maker0 != null)
				maker0.getMaker().onMakerScriptEvent("1000", maker0, 0, 0);
			
			maker0 = SpawnManager.getInstance().getNpcMaker(MAKER_NAME_ALARM_2);
			if (maker0 != null)
				maker0.getMaker().onMakerScriptEvent("1000", maker0, 0, 0);
			
			maker0 = SpawnManager.getInstance().getNpcMaker(MAKER_NAME_ALARM_3);
			if (maker0 != null)
				maker0.getMaker().onMakerScriptEvent("1000", maker0, 0, 0);
			
			maker0 = SpawnManager.getInstance().getNpcMaker(MAKER_NAME_ALARM_4);
			if (maker0 != null)
				maker0.getMaker().onMakerScriptEvent("1000", maker0, 0, 0);
			
			maker0 = SpawnManager.getInstance().getNpcMaker(MAKER_NAME_CAPTAIN);
			if (maker0 != null)
				maker0.getMaker().onMakerScriptEvent("1000", maker0, 0, 0);
			
			maker0 = SpawnManager.getInstance().getNpcMaker(MAKER_NAME_GUARD);
			if (maker0 != null)
				maker0.getMaker().onMakerScriptEvent("1000", maker0, 0, 0);
			
			maker0 = SpawnManager.getInstance().getNpcMaker(MAKER_NAME_PATROL);
			if (maker0 != null)
				maker0.getMaker().onMakerScriptEvent("1000", maker0, 0, 0);
			
			maker0 = SpawnManager.getInstance().getNpcMaker(MAKER_NAME_WIZARD_1);
			if (maker0 != null)
				maker0.getMaker().onMakerScriptEvent("1000", maker0, 0, 0);
			
			maker0 = SpawnManager.getInstance().getNpcMaker(MAKER_NAME_WIZARD_2);
			if (maker0 != null)
				maker0.getMaker().onMakerScriptEvent("1000", maker0, 0, 0);
			
			maker0 = SpawnManager.getInstance().getNpcMaker(MAKER_NAME_WIZARD_3);
			if (maker0 != null)
				maker0.getMaker().onMakerScriptEvent("1000", maker0, 0, 0);
			
			maker0 = SpawnManager.getInstance().getNpcMaker(MAKER_NAME_WIZARD_4);
			if (maker0 != null)
				maker0.getMaker().onMakerScriptEvent("1000", maker0, 0, 0);
			
			maker0 = SpawnManager.getInstance().getNpcMaker(MAKER_NAME_WIZARD_5);
			if (maker0 != null)
				maker0.getMaker().onMakerScriptEvent("1000", maker0, 0, 0);
			
			maker0 = SpawnManager.getInstance().getNpcMaker(MAKER_NAME_SELF_DES);
			if (maker0 != null)
				maker0.getMaker().onMakerScriptEvent("1000", maker0, 0, 0);
				
			// TODO: Zones
			// gg::Area_SetOnOff(dmgzone,0);
			
			openCloseDoor(T_DOOR_NAME_1, 0);
			openCloseDoor(T_DOOR_NAME_2, 0);
			openCloseDoor(WALL_DOOR_NAME_1, 0);
			openCloseDoor(WALL_DOOR_NAME_2, 0);
			openCloseDoor(WALL_DOOR_NAME_3, 0);
			openCloseDoor(WALL_DOOR_NAME_4, 0);
			openCloseDoor(WALL_DOOR_NAME_5, 0);
			openCloseDoor(WALL_DOOR_NAME_6, 0);
			openCloseDoor(WALL_DOOR_NAME_7, 0);
			openCloseDoor(WALL_DOOR_NAME_8, 0);
		}
	}
}