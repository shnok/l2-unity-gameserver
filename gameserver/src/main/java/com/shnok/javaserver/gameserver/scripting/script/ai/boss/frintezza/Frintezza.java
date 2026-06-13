package com.shnok.javaserver.gameserver.scripting.script.ai.boss.frintezza;

import com.shnok.javaserver.commons.random.Rnd;

import com.shnok.javaserver.Config;
import com.shnok.javaserver.gameserver.data.SkillTable;
import com.shnok.javaserver.gameserver.data.manager.SpawnManager;
import com.shnok.javaserver.gameserver.model.actor.Creature;
import com.shnok.javaserver.gameserver.model.actor.Npc;
import com.shnok.javaserver.gameserver.model.actor.Player;
import com.shnok.javaserver.gameserver.model.memo.GlobalMemo;
import com.shnok.javaserver.gameserver.model.spawn.NpcMaker;
import com.shnok.javaserver.gameserver.network.NpcStringId;
import com.shnok.javaserver.gameserver.scripting.script.ai.individual.DefaultNpc;
import com.shnok.javaserver.gameserver.skills.L2Skill;
import com.shnok.javaserver.gameserver.taskmanager.GameTimeTaskManager;

public class Frintezza extends DefaultNpc
{
	private static final L2Skill MUSIC = SkillTable.getInstance().getInfo(5006, 1);
	private static final L2Skill MUSIC_OF_HEAL = SkillTable.getInstance().getInfo(5007, 1);
	private static final L2Skill MUSIC_OF_RAMPAGE = SkillTable.getInstance().getInfo(5007, 2);
	private static final L2Skill MUSIC_OF_POWER = SkillTable.getInstance().getInfo(5007, 3);
	private static final L2Skill MUSIC_OF_PLAGUE = SkillTable.getInstance().getInfo(5007, 4);
	private static final L2Skill MUSIC_OF_PSYCHO = SkillTable.getInstance().getInfo(5007, 5);
	
	// private static final String areadata_heal1 = "25_15_frintessa_01_01";
	// private static final String areadata_heal2 = "25_15_frintessa_02_01";
	// private static final String areadata_power1 = "25_15_frintessa_01_02";
	// private static final String areadata_power2 = "25_15_frintessa_02_02";
	// private static final String areadata_psycho1 = "25_15_frintessa_01_03";
	// private static final String areadata_psycho2 = "25_15_frintessa_02_03";
	// private static final String areadata_rampage1 = "25_15_frintessa_01_04";
	// private static final String areadata_rampage2 = "25_15_frintessa_02_04";
	// private static final String areadata_plague1 = "25_15_frintessa_01_05";
	// private static final String areadata_plague2 = "25_15_frintessa_02_05";
	
	private static final String T_DOOR_NAME_1 = "grave_pathway_1";
	private static final String T_DOOR_NAME_2 = "grave_pathway_2";
	private static final String T_DOOR_NAME_3 = "grave_pathway_3";
	private static final String T_DOOR_NAME_4 = "grave_pathway_4";
	private static final String WALL_DOOR_NAME_1A = "wall_door_a_center_1";
	private static final String WALL_DOOR_NAME_2A = "wall_door_a_center_2";
	private static final String WALL_DOOR_NAME_3A = "wall_door_a_center_3";
	private static final String WALL_DOOR_NAME_4A = "wall_door_a_center_4";
	private static final String WALL_DOOR_NAME_5A = "wall_door_a_center_5";
	private static final String WALL_DOOR_NAME_6A = "wall_door_a_center_6";
	private static final String WALL_DOOR_NAME_7A = "wall_door_a_center_7";
	private static final String WALL_DOOR_NAME_8A = "wall_door_a_center_8";
	private static final String WALL_DOOR_NAME_1B = "wall_door_b_left_1";
	private static final String WALL_DOOR_NAME_2B = "wall_door_b_left_2";
	private static final String WALL_DOOR_NAME_3B = "wall_door_b_left_3";
	private static final String WALL_DOOR_NAME_4B = "wall_door_b_left_4";
	private static final String WALL_DOOR_NAME_5B = "wall_door_b_left_5";
	private static final String WALL_DOOR_NAME_6B = "wall_door_b_right_1";
	private static final String WALL_DOOR_NAME_7B = "wall_door_b_right_2";
	private static final String WALL_DOOR_NAME_8B = "wall_door_b_right_3";
	private static final String WALL_DOOR_NAME_9B = "wall_door_b_right_4";
	private static final String WALL_DOOR_NAME_10B = "wall_door_b_right_5";
	
	private static final int GM_ID = 4;
	
	public Frintezza()
	{
		super("ai/boss/frintezza");
	}
	
	public Frintezza(String descr)
	{
		super(descr);
	}
	
	protected final int[] _npcIds =
	{
		29045 // frintessa
	};
	
	@Override
	public void onCreated(Npc npc)
	{
		int i_ai1 = npc._i_ai1;
		
		final Creature c0 = GlobalMemo.getInstance().getCreature(String.valueOf(GM_ID));
		if (c0 == null)
			GlobalMemo.getInstance().set(String.valueOf(GM_ID), npc.getObjectId());
		
		if (!npc.getSpawn().getDBLoaded())
		{
			npc.getSpawn().getSpawnData().setDBValue(0);
			
			final NpcMaker maker0 = SpawnManager.getInstance().getNpcMaker("frintessa_2515_m01");
			if (maker0 != null)
				maker0.getMaker().onMakerScriptEvent("1001", maker0, 0, 0);
			// TODO Zones
			// gg::Area_SetOnOff(areadata_heal1,0);
			// gg::Area_SetOnOff(areadata_heal2,0);
			// gg::Area_SetOnOff(areadata_power1,0);
			// gg::Area_SetOnOff(areadata_power2,0);
			// gg::Area_SetOnOff(areadata_psycho1,0);
			// gg::Area_SetOnOff(areadata_psycho2,0);
			// gg::Area_SetOnOff(areadata_rampage1,0);
			// gg::Area_SetOnOff(areadata_rampage2,0);
			// gg::Area_SetOnOff(areadata_plague1,0);
			// gg::Area_SetOnOff(areadata_plague2,0);
			
			openCloseDoor(T_DOOR_NAME_1, 1);
			openCloseDoor(T_DOOR_NAME_2, 1);
			openCloseDoor(T_DOOR_NAME_3, 0);
			openCloseDoor(T_DOOR_NAME_4, 0);
			openCloseDoor(WALL_DOOR_NAME_1A, 1);
			openCloseDoor(WALL_DOOR_NAME_2A, 1);
			openCloseDoor(WALL_DOOR_NAME_3A, 1);
			openCloseDoor(WALL_DOOR_NAME_4A, 1);
			openCloseDoor(WALL_DOOR_NAME_5A, 1);
			openCloseDoor(WALL_DOOR_NAME_6A, 1);
			openCloseDoor(WALL_DOOR_NAME_7A, 1);
			openCloseDoor(WALL_DOOR_NAME_8A, 1);
			openCloseDoor(WALL_DOOR_NAME_1B, 1);
			openCloseDoor(WALL_DOOR_NAME_2B, 1);
			openCloseDoor(WALL_DOOR_NAME_3B, 1);
			openCloseDoor(WALL_DOOR_NAME_4B, 1);
			openCloseDoor(WALL_DOOR_NAME_5B, 1);
			openCloseDoor(WALL_DOOR_NAME_6B, 1);
			openCloseDoor(WALL_DOOR_NAME_7B, 1);
			openCloseDoor(WALL_DOOR_NAME_8B, 1);
			openCloseDoor(WALL_DOOR_NAME_9B, 1);
			openCloseDoor(WALL_DOOR_NAME_10B, 1);
			
			Npc c1 = (Npc) GlobalMemo.getInstance().getCreature("5");
			if (c1 != null)
				c1.sendScriptEvent(npc.getObjectId(), 0, 0);
			
			c1 = (Npc) GlobalMemo.getInstance().getCreature("6");
			if (c1 != null)
				c1.sendScriptEvent(npc.getObjectId(), 0, 0);
		}
		else if (npc.getSpawn().getSpawnData().getDBValue() == 1)
		{
			final NpcMaker maker0 = SpawnManager.getInstance().getNpcMaker("frintessa_2515_m01");
			if (maker0 != null)
				maker0.getMaker().onMakerScriptEvent("1001", maker0, 0, 0);
		}
		else if (npc.getSpawn().getSpawnData().getDBValue() == 2)
		{
			startQuestTimer("1001", npc, null, 600000);
			
			final NpcMaker maker0 = SpawnManager.getInstance().getNpcMaker("frintessa_2515_m01");
			if (maker0 != null)
				maker0.getMaker().onMakerScriptEvent("1000", maker0, 0, 0);
		}
		else if (npc.getSpawn().getSpawnData().getDBValue() > 2 && npc.getSpawn().getSpawnData().getDBValue() < 8)
		{
			openCloseDoor(T_DOOR_NAME_1, 1);
			openCloseDoor(T_DOOR_NAME_2, 1);
			openCloseDoor(T_DOOR_NAME_3, 1);
			openCloseDoor(T_DOOR_NAME_4, 1);
			openCloseDoor(WALL_DOOR_NAME_1A, 1);
			openCloseDoor(WALL_DOOR_NAME_2A, 1);
			openCloseDoor(WALL_DOOR_NAME_3A, 1);
			openCloseDoor(WALL_DOOR_NAME_4A, 1);
			openCloseDoor(WALL_DOOR_NAME_5A, 1);
			openCloseDoor(WALL_DOOR_NAME_6A, 1);
			openCloseDoor(WALL_DOOR_NAME_7A, 1);
			openCloseDoor(WALL_DOOR_NAME_8A, 1);
			openCloseDoor(WALL_DOOR_NAME_1B, 1);
			openCloseDoor(WALL_DOOR_NAME_2B, 1);
			openCloseDoor(WALL_DOOR_NAME_3B, 1);
			openCloseDoor(WALL_DOOR_NAME_4B, 1);
			openCloseDoor(WALL_DOOR_NAME_5B, 1);
			openCloseDoor(WALL_DOOR_NAME_6B, 1);
			openCloseDoor(WALL_DOOR_NAME_7B, 1);
			openCloseDoor(WALL_DOOR_NAME_8B, 1);
			openCloseDoor(WALL_DOOR_NAME_9B, 1);
			openCloseDoor(WALL_DOOR_NAME_10B, 1);
			
			createOnePrivateEx(npc, 29052, 174239, -89809, -5016, 16384, 0, false);
			
			if (npc.getSpawn().getSpawnData().getDBValue() == 3)
				frintezzaPlaySong(npc, 0, 0, 0, 0, 0);
			else if (npc.getSpawn().getSpawnData().getDBValue() == 4)
				frintezzaPlaySong(npc, 0, 0, 0, 0, 0);
			else if (npc.getSpawn().getSpawnData().getDBValue() == 5)
				frintezzaPlaySong(npc, 2000, 2000, 0, 0, 0);
			else if (npc.getSpawn().getSpawnData().getDBValue() == 6)
			{
				if (i_ai1 == 0)
					frintezzaPlaySong(npc, 1500, 1500, 1000, 2000, 0);
				else if (i_ai1 == 30010)
					frintezzaPlaySong(npc, 1000, 2000, 1000, 2000, 0);
			}
			else if (npc.getSpawn().getSpawnData().getDBValue() == 7)
			{
				if (i_ai1 == 0)
					frintezzaPlaySong(npc, 0, 1500, 625, 2500, 0);
				else if (i_ai1 == 30021)
					frintezzaPlaySong(npc, 1500, 0, 625, 3000, 0);
				else if (i_ai1 == 30022)
				{
					if (getElapsedTicks(npc._i_quest2) > (15 * 60))
						frintezzaPlaySong(npc, 0, 1500, 0, 3000, 1500);
					else
						frintezzaPlaySong(npc, 1500, 0, 1500, 3000, 0);
				}
				else if (i_ai1 == 30023)
				{
					if (getElapsedTicks(npc._i_quest2) > (15 * 60))
						frintezzaPlaySong(npc, 1500, 0, 625, 3000, 1500);
					else
						frintezzaPlaySong(npc, 1500, 0, 1500, 3000, 0);
				}
				else if (i_ai1 == 30024)
					frintezzaPlaySong(npc, 1500, 1500, 0, 3000, 0);
			}
			
			startQuestTimer("3000", npc, null, 1000);
			
			final NpcMaker maker0 = SpawnManager.getInstance().getNpcMaker("frintessa_2515_m01");
			if (maker0 != null)
				maker0.getMaker().onMakerScriptEvent("1000", maker0, 0, 0);
		}
		
		npc._i_ai0 = 0;
		npc._i_ai1 = 0;
		npc._i_ai2 = 0;
		npc._i_ai3 = 0;
		npc._i_ai4 = 0;
		npc._i_quest0 = 0;
		npc._i_quest1 = 0;
		npc._i_quest2 = 0;
		npc._i_quest3 = 0;
		
		super.onCreated(npc);
	}
	
	@Override
	public String onTimer(String name, Npc npc, Player player)
	{
		int i_ai1 = npc._i_ai1;
		
		if (name.equalsIgnoreCase("1000"))
		{
			createOnePrivateEx(npc, 29052, 174239, -89809, -5016, 16384, 0, false);
			
			startQuestTimer("1001", npc, null, 4000);
		}
		else if (name.equalsIgnoreCase("1001"))
		{
			npc.getPosition().setHeading(16384);
			
			npc.teleportTo(174239, -89808, -5016, 0);
			
			openCloseDoor(T_DOOR_NAME_1, 1);
			openCloseDoor(T_DOOR_NAME_2, 1);
			openCloseDoor(T_DOOR_NAME_3, 1);
			openCloseDoor(T_DOOR_NAME_4, 1);
			openCloseDoor(WALL_DOOR_NAME_1A, 1);
			openCloseDoor(WALL_DOOR_NAME_2A, 1);
			openCloseDoor(WALL_DOOR_NAME_3A, 1);
			openCloseDoor(WALL_DOOR_NAME_4A, 1);
			openCloseDoor(WALL_DOOR_NAME_5A, 1);
			openCloseDoor(WALL_DOOR_NAME_6A, 1);
			openCloseDoor(WALL_DOOR_NAME_7A, 1);
			openCloseDoor(WALL_DOOR_NAME_8A, 1);
			openCloseDoor(WALL_DOOR_NAME_1B, 1);
			openCloseDoor(WALL_DOOR_NAME_2B, 1);
			openCloseDoor(WALL_DOOR_NAME_3B, 1);
			openCloseDoor(WALL_DOOR_NAME_4B, 1);
			openCloseDoor(WALL_DOOR_NAME_5B, 1);
			openCloseDoor(WALL_DOOR_NAME_6B, 1);
			openCloseDoor(WALL_DOOR_NAME_7B, 1);
			openCloseDoor(WALL_DOOR_NAME_8B, 1);
			openCloseDoor(WALL_DOOR_NAME_9B, 1);
			openCloseDoor(WALL_DOOR_NAME_10B, 1);
			
			npc._i_quest1 = GameTimeTaskManager.getInstance().getCurrentTick();
			
			startQuestTimer("1002", npc, null, 6000);
		}
		else if (name.equalsIgnoreCase("1002"))
		{
			startQuestTimer("1003", npc, null, 2000);
		}
		else if (name.equalsIgnoreCase("1003"))
		{
			npc.getAI().addSocialDesire(1, 7000, 1000);
			
			startQuestTimer("1004", npc, null, 7000);
		}
		else if (name.equalsIgnoreCase("1004"))
		{
			startQuestTimer("1005", npc, null, 1500);
		}
		else if (name.equalsIgnoreCase("1005"))
		{
			final NpcMaker nm = SpawnManager.getInstance().getNpcMaker("frintessa_evilate_maker1");
			if (nm != null)
				nm.getMaker().onMakerScriptEvent("1001", nm, 0, 0);
			
			startQuestTimer("1006", npc, null, 500);
		}
		else if (name.equalsIgnoreCase("1006"))
		{
			final NpcMaker nm = SpawnManager.getInstance().getNpcMaker("frintessa_evilate_maker2");
			if (nm != null)
				nm.getMaker().onMakerScriptEvent("1001", nm, 0, 0);
			
			startQuestTimer("1007", npc, null, 500);
		}
		else if (name.equalsIgnoreCase("1007"))
		{
			npc.getAI().addSocialDesire(3, 6600, 1000);
			
			startQuestTimer("1008", npc, null, 6600);
		}
		else if (name.equalsIgnoreCase("1008"))
		{
			startQuestTimer("1009", npc, null, 1000);
		}
		else if (name.equalsIgnoreCase("1009"))
		{
			npc._i_quest1 = GameTimeTaskManager.getInstance().getCurrentTick();
			
			if (npc.getSpawn().getSpawnData().getDBValue() == 3)
				frintezzaPlaySong(npc, 0, 0, 0, 0, 0);
			else if (npc.getSpawn().getSpawnData().getDBValue() == 4)
				frintezzaPlaySong(npc, 0, 0, 0, 0, 0);
			else if (npc.getSpawn().getSpawnData().getDBValue() == 5)
				frintezzaPlaySong(npc, 2000, 2000, 0, 0, 0);
			else if (npc.getSpawn().getSpawnData().getDBValue() == 6)
			{
				if (i_ai1 == 0)
					frintezzaPlaySong(npc, 1500, 1500, 1000, 2000, 0);
				else if (i_ai1 == 30010)
					frintezzaPlaySong(npc, 1000, 2000, 1000, 2000, 0);
			}
			else if (npc.getSpawn().getSpawnData().getDBValue() == 7)
			{
				if (i_ai1 == 0)
					frintezzaPlaySong(npc, 0, 1500, 625, 2500, 0);
				else if (i_ai1 == 30021)
					frintezzaPlaySong(npc, 1500, 0, 625, 3000, 0);
				else if (i_ai1 == 30022)
				{
					if (getElapsedTicks(npc._i_quest2) > (15 * 60))
						frintezzaPlaySong(npc, 0, 1500, 0, 3000, 1500);
					else
						frintezzaPlaySong(npc, 1500, 0, 1500, 3000, 0);
				}
				else if (i_ai1 == 30023)
				{
					if (getElapsedTicks(npc._i_quest2) > (15 * 60))
						frintezzaPlaySong(npc, 1500, 0, 625, 3000, 1500);
					else
						frintezzaPlaySong(npc, 1500, 0, 1500, 3000, 0);
				}
				else if (i_ai1 == 30024)
					frintezzaPlaySong(npc, 1500, 1500, 0, 3000, 0);
			}
			
			startQuestTimer("1010", npc, null, 10000);
		}
		else if (name.equalsIgnoreCase("1010"))
		{
			startQuestTimer("1011", npc, null, 3500);
		}
		else if (name.equalsIgnoreCase("1011"))
		{
			final Npc c0 = (Npc) GlobalMemo.getInstance().getCreature("5");
			if (c0 != null)
			{
				c0.sendScriptEvent(npc.getObjectId(), 4, 0);
				
				npc.getSpawn().getSpawnData().setDBValue(4);
			}
			
			startQuestTimer("1012", npc, null, 1000);
		}
		else if (name.equalsIgnoreCase("1012"))
		{
			npc._i_quest1 = GameTimeTaskManager.getInstance().getCurrentTick();
			
			if (npc.getSpawn().getSpawnData().getDBValue() == 3)
				frintezzaPlaySong(npc, 0, 0, 0, 0, 0);
			else if (npc.getSpawn().getSpawnData().getDBValue() == 4)
				frintezzaPlaySong(npc, 0, 0, 0, 0, 0);
			else if (npc.getSpawn().getSpawnData().getDBValue() == 5)
				frintezzaPlaySong(npc, 2000, 2000, 0, 0, 0);
			else if (npc.getSpawn().getSpawnData().getDBValue() == 6)
			{
				if (i_ai1 == 0)
					frintezzaPlaySong(npc, 1500, 1500, 1000, 2000, 0);
				else if (i_ai1 == 30010)
					frintezzaPlaySong(npc, 1000, 2000, 1000, 2000, 0);
			}
			else if (npc.getSpawn().getSpawnData().getDBValue() == 7)
			{
				if (i_ai1 == 0)
					frintezzaPlaySong(npc, 0, 1500, 625, 2500, 0);
				else if (i_ai1 == 30021)
					frintezzaPlaySong(npc, 1500, 0, 625, 3000, 0);
				else if (i_ai1 == 30022)
				{
					if (getElapsedTicks(npc._i_quest2) > (15 * 60))
						frintezzaPlaySong(npc, 0, 1500, 0, 3000, 1500);
					else
						frintezzaPlaySong(npc, 1500, 0, 1500, 3000, 0);
				}
				else if (i_ai1 == 30023)
				{
					if (getElapsedTicks(npc._i_quest2) > (15 * 60))
						frintezzaPlaySong(npc, 1500, 0, 625, 3000, 1500);
					else
						frintezzaPlaySong(npc, 1500, 0, 1500, 3000, 0);
				}
				else if (i_ai1 == 30024)
					frintezzaPlaySong(npc, 1500, 1500, 0, 3000, 0);
			}
			
			startQuestTimer("1013", npc, null, 3000);
		}
		else if (name.equalsIgnoreCase("1500"))
		{
			npc.sendScriptEvent(npc.getObjectId(), 3, 0);
			
			final Npc c0 = (Npc) GlobalMemo.getInstance().getCreature("5");
			if (c0 != null)
				c0.sendScriptEvent(npc.getObjectId(), 3, 0);
		}
		else if (name.equalsIgnoreCase("2000"))
		{
			npc.removeAllDesire();
			
			npc.getAI().addSocialDesire(4, 7000, 10000000);
			
			startQuestTimer("2001", npc, null, 7000);
		}
		else if (name.equalsIgnoreCase("2001"))
		{
			startQuestTimer("2002", npc, null, 1000);
		}
		else if (name.equalsIgnoreCase("2002"))
		{
			npc.removeAllDesire();
			
			frintezzaPlaySong(npc, 0, 0, 0, 10000, 0);
			
			startQuestTimer("2003", npc, null, 4000);
		}
		else if (name.equalsIgnoreCase("5000"))
		{
			npc.getAI().addSocialDesire(3, 100, 50);
		}
		else if (name.equalsIgnoreCase("6000"))
		{
			npc.sendScriptEvent(npc.getObjectId(), 3, 0);
		}
		else if (name.equalsIgnoreCase("7000"))
		{
			npc.doDie(npc);
		}
		else if (name.equalsIgnoreCase("8000"))
		{
			npc.getSpawn().getSpawnData().setDBValue(0);
			
			npc.getPosition().setHeading(16384);
			npc.teleportTo(-105200, -253104, -15264, 0);
			
			openCloseDoor(T_DOOR_NAME_1, 1);
			openCloseDoor(T_DOOR_NAME_2, 1);
			openCloseDoor(T_DOOR_NAME_3, 0);
			openCloseDoor(T_DOOR_NAME_4, 0);
			openCloseDoor(WALL_DOOR_NAME_1A, 1);
			openCloseDoor(WALL_DOOR_NAME_2A, 1);
			openCloseDoor(WALL_DOOR_NAME_3A, 1);
			openCloseDoor(WALL_DOOR_NAME_4A, 1);
			openCloseDoor(WALL_DOOR_NAME_5A, 1);
			openCloseDoor(WALL_DOOR_NAME_6A, 1);
			openCloseDoor(WALL_DOOR_NAME_7A, 1);
			openCloseDoor(WALL_DOOR_NAME_8A, 1);
			openCloseDoor(WALL_DOOR_NAME_1B, 1);
			openCloseDoor(WALL_DOOR_NAME_2B, 1);
			openCloseDoor(WALL_DOOR_NAME_3B, 1);
			openCloseDoor(WALL_DOOR_NAME_4B, 1);
			openCloseDoor(WALL_DOOR_NAME_5B, 1);
			openCloseDoor(WALL_DOOR_NAME_6B, 1);
			openCloseDoor(WALL_DOOR_NAME_7B, 1);
			openCloseDoor(WALL_DOOR_NAME_8B, 1);
			openCloseDoor(WALL_DOOR_NAME_9B, 1);
			openCloseDoor(WALL_DOOR_NAME_10B, 1);
			
			final NpcMaker maker0 = SpawnManager.getInstance().getNpcMaker("frintessa_2515_m01");
			if (maker0 != null)
				maker0.getMaker().onMakerScriptEvent("1000", maker0, 0, 0);
		}
		else if (name.equalsIgnoreCase("3000"))
		{
			npc._i_ai4 = 0;
			
			if (npc.getSpawn().getSpawnData().getDBValue() == 3)
				frintezzaPlaySong(npc, 0, 0, 0, 0, 0);
			else if (npc.getSpawn().getSpawnData().getDBValue() == 4)
				frintezzaPlaySong(npc, 0, 0, 0, 0, 0);
			else if (npc.getSpawn().getSpawnData().getDBValue() == 5)
				frintezzaPlaySong(npc, 2, 2000, 0, 0, 0);
			else if (npc.getSpawn().getSpawnData().getDBValue() == 6)
			{
				if (i_ai1 == 0)
					frintezzaPlaySong(npc, 1, 1500, 1000, 2000, 0);
				else if (i_ai1 == 30010)
					frintezzaPlaySong(npc, 1, 2000, 1000, 2000, 0);
			}
			else if (npc.getSpawn().getSpawnData().getDBValue() == 7)
			{
				if (i_ai1 == 0)
					frintezzaPlaySong(npc, 0, 1500, 625, 2500, 0);
				else if (i_ai1 == 30021)
					frintezzaPlaySong(npc, 1, 0, 625, 3000, 0);
				else if (i_ai1 == 30022)
				{
					if (getElapsedTicks(npc._i_quest2) > (15 * 60))
						frintezzaPlaySong(npc, 0, 1500, 0, 3000, 1500);
					else
						frintezzaPlaySong(npc, 1, 0, 1500, 3000, 0);
				}
				else if (i_ai1 == 30023)
				{
					if (getElapsedTicks(npc._i_quest2) > (15 * 60))
						frintezzaPlaySong(npc, 1, 0, 625, 3000, 1500);
					else
						frintezzaPlaySong(npc, 1, 0, 1500, 3000, 0);
				}
				else if (i_ai1 == 30024)
					frintezzaPlaySong(npc, 1, 1500, 0, 3000, 0);
			}
		}
		else if (name.equalsIgnoreCase("3001"))
		{
			npc._i_ai4 = 0;
			
			if (npc.getSpawn().getSpawnData().getDBValue() == 3)
				frintezzaPlaySong(npc, 0, 0, 0, 0, 0);
			else if (npc.getSpawn().getSpawnData().getDBValue() == 4)
				frintezzaPlaySong(npc, 0, 0, 0, 0, 0);
			else if (npc.getSpawn().getSpawnData().getDBValue() == 5)
				frintezzaPlaySong(npc, 2, 2000, 0, 0, 0);
			else if (npc.getSpawn().getSpawnData().getDBValue() == 6)
			{
				if (i_ai1 == 0)
					frintezzaPlaySong(npc, 1, 1500, 1000, 2000, 0);
				else if (i_ai1 == 30010)
					frintezzaPlaySong(npc, 1, 2000, 1000, 2000, 0);
			}
			else if (npc.getSpawn().getSpawnData().getDBValue() == 7)
			{
				if (i_ai1 == 0)
					frintezzaPlaySong(npc, 0, 1500, 625, 2500, 0);
				else if (i_ai1 == 30021)
					frintezzaPlaySong(npc, 1, 0, 625, 3000, 0);
				else if (i_ai1 == 30022)
				{
					if (getElapsedTicks(npc._i_quest2) > (15 * 60))
						frintezzaPlaySong(npc, 0, 1500, 0, 3000, 1500);
					else
						frintezzaPlaySong(npc, 1, 0, 1500, 3000, 0);
				}
				else if (i_ai1 == 30023)
				{
					if (getElapsedTicks(npc._i_quest2) > (15 * 60))
						frintezzaPlaySong(npc, 1, 0, 625, 3000, 1500);
					else
						frintezzaPlaySong(npc, 1, 0, 1500, 3000, 0);
				}
				else if (i_ai1 == 30024)
					frintezzaPlaySong(npc, 1, 1500, 0, 3000, 0);
			}
		}
		
		npc._i_ai1 = 0;
		
		return super.onTimer(name, npc, null);
	}
	
	@Override
	public void onScriptEvent(Npc npc, int eventId, int arg1, int arg2)
	{
		Npc c0 = (Npc) GlobalMemo.getInstance().getCreature("5");
		
		if (c0 != null && eventId == c0.getObjectId())
		{
			if (arg1 == 5)
			{
				npc.getSpawn().getSpawnData().setDBValue(5);
				
				npc._i_ai0 = 5;
				npc._i_ai1 = 0;
			}
			else if (arg1 == 30000)
			{
				npc._i_ai1 = 30000;
			}
			else if (arg1 == 6)
			{
				npc.getSpawn().getSpawnData().setDBValue(6);
				
				npc._i_ai0 = 6;
				npc._i_ai1 = 0;
			}
			else if (arg1 == 30010)
			{
				npc._i_ai1 = 30010;
			}
		}
		else if (arg1 == 7)
		{
			npc.getSpawn().getSpawnData().setDBValue(7);
			
			npc._i_ai0 = 7;
			npc._i_ai1 = 0;
			
			npc._i_quest2 = GameTimeTaskManager.getInstance().getCurrentTick();
			
			startQuestTimer("5000", npc, null, 1000);
		}
		else if (arg1 == 30021 || arg1 == 30022 || arg1 == 30023 || arg1 == 30024 || arg1 == 30025)
		{
			npc._i_ai1 = arg1;
		}
		
		if (arg1 == 0)
		{
			final Npc c2 = (Npc) GlobalMemo.getInstance().getCreature("5");
			final Npc c3 = (Npc) GlobalMemo.getInstance().getCreature("6");
			
			if (c2 == null || c3 == null)
				return;
			
			openCloseDoor(T_DOOR_NAME_1, 1);
			openCloseDoor(T_DOOR_NAME_2, 1);
			openCloseDoor(T_DOOR_NAME_3, 0);
			openCloseDoor(T_DOOR_NAME_4, 0);
			openCloseDoor(WALL_DOOR_NAME_1A, 1);
			openCloseDoor(WALL_DOOR_NAME_2A, 1);
			openCloseDoor(WALL_DOOR_NAME_3A, 1);
			openCloseDoor(WALL_DOOR_NAME_4A, 1);
			openCloseDoor(WALL_DOOR_NAME_5A, 1);
			openCloseDoor(WALL_DOOR_NAME_6A, 1);
			openCloseDoor(WALL_DOOR_NAME_7A, 1);
			openCloseDoor(WALL_DOOR_NAME_8A, 1);
			openCloseDoor(WALL_DOOR_NAME_1B, 1);
			openCloseDoor(WALL_DOOR_NAME_2B, 1);
			openCloseDoor(WALL_DOOR_NAME_3B, 1);
			openCloseDoor(WALL_DOOR_NAME_4B, 1);
			openCloseDoor(WALL_DOOR_NAME_5B, 1);
			openCloseDoor(WALL_DOOR_NAME_6B, 1);
			openCloseDoor(WALL_DOOR_NAME_7B, 1);
			openCloseDoor(WALL_DOOR_NAME_8B, 1);
			openCloseDoor(WALL_DOOR_NAME_9B, 1);
			openCloseDoor(WALL_DOOR_NAME_10B, 1);
			
			// gg::Area_SetOnOff(areadata_heal1,0);
			// gg::Area_SetOnOff(areadata_heal2,0);
			// gg::Area_SetOnOff(areadata_power1,0);
			// gg::Area_SetOnOff(areadata_power2,0);
			// gg::Area_SetOnOff(areadata_psycho1,0);
			// gg::Area_SetOnOff(areadata_psycho2,0);
			// gg::Area_SetOnOff(areadata_rampage1,0);
			// gg::Area_SetOnOff(areadata_rampage2,0);
			// gg::Area_SetOnOff(areadata_plague1,0);
			// gg::Area_SetOnOff(areadata_plague2,0);
			
			npc.getSpawn().getSpawnData().setDBValue(0);
			
			npc.getPosition().setHeading(16384);
			npc.teleportTo(-105200, -253104, -15264, 0);
			
			final NpcMaker maker0 = SpawnManager.getInstance().getNpcMaker("frintessa_2515_m01");
			if (maker0 != null)
				maker0.getMaker().onMakerScriptEvent("1001", maker0, 0, 0);
			
		}
		else if (arg1 == 1)
		{
			npc.getSpawn().getSpawnData().setDBValue(1);
			
			c0 = (Npc) GlobalMemo.getInstance().getCreature("5");
			if (c0 != null)
				c0.sendScriptEvent(npc.getObjectId(), 1, 0);
		}
		else if (arg1 == 2)
		{
			npc.getSpawn().getSpawnData().setDBValue(2);
			
			c0 = (Npc) GlobalMemo.getInstance().getCreature("5");
			if (c0 != null)
				c0.sendScriptEvent(npc.getObjectId(), 2, 0);
			
			final NpcMaker maker0 = SpawnManager.getInstance().getNpcMaker("frintessa_2515_m01");
			if (maker0 != null)
				maker0.getMaker().onMakerScriptEvent("1000", maker0, 0, 0);
			
			startQuestTimer("1500", npc, null, Config.WAIT_TIME_FRINTEZZA);
		}
		else if (arg1 == 3)
		{
			npc.getSpawn().getSpawnData().setDBValue(3);
			
			startQuestTimer("1000", npc, null, 4000);
		}
		else if (arg1 == 8)
		{
			startQuestTimer("7000", npc, null, 7500);
		}
		else if (arg1 == 50000)
		{
			startQuestTimer("2000", npc, null, 500);
		}
		super.onScriptEvent(npc, eventId, arg1, arg2);
	}
	
	@Override
	public void onAttacked(Npc npc, Creature attacker, int damage, L2Skill skill)
	{
		if (skill != null && skill.getId() == 2234)
		{
			npc._i_ai4 = 1;
			
			npc.removeAllDesire();
			
			startQuestTimer("3000", npc, null, 30000);
		}
	}
	
	@Override
	public void onUseSkillFinished(Npc npc, Creature creature, L2Skill skill, boolean success)
	{
		if (npc._i_ai4 == 0)
		{
			if (!success)
			{
				// TODO Zones
				// gg::Area_SetOnOff(areadata_heal1,0);
				// gg::Area_SetOnOff(areadata_heal2,0);
				// gg::Area_SetOnOff(areadata_power1,0);
				// gg::Area_SetOnOff(areadata_power2,0);
				// gg::Area_SetOnOff(areadata_psycho1,0);
				// gg::Area_SetOnOff(areadata_psycho2,0);
				// gg::Area_SetOnOff(areadata_rampage1,0);
				// gg::Area_SetOnOff(areadata_rampage2,0);
				// gg::Area_SetOnOff(areadata_plague1,0);
				// gg::Area_SetOnOff(areadata_plague2,0);
				
				npc._i_ai4 = 1;
				
				npc.removeAllDesire();
				
				startQuestTimer("3001", npc, null, 30000);
			}
			else
			{
				int i_ai1 = npc._i_ai1;
				
				// gg::Area_SetOnOff(areadata_heal1,0);
				// gg::Area_SetOnOff(areadata_heal2,0);
				// gg::Area_SetOnOff(areadata_power1,0);
				// gg::Area_SetOnOff(areadata_power2,0);
				// gg::Area_SetOnOff(areadata_psycho1,0);
				// gg::Area_SetOnOff(areadata_psycho2,0);
				// gg::Area_SetOnOff(areadata_rampage1,0);
				// gg::Area_SetOnOff(areadata_rampage2,0);
				// gg::Area_SetOnOff(areadata_plague1,0);
				// gg::Area_SetOnOff(areadata_plague2,0);
				
				if (npc.getSpawn().getSpawnData().getDBValue() == 3)
					frintezzaPlaySong(npc, 0, 0, 0, 0, 0);
				else if (npc.getSpawn().getSpawnData().getDBValue() == 4)
					frintezzaPlaySong(npc, 0, 0, 0, 0, 0);
				else if (npc.getSpawn().getSpawnData().getDBValue() == 5)
					frintezzaPlaySong(npc, 2, 2000, 0, 0, 0);
				else if (npc.getSpawn().getSpawnData().getDBValue() == 6)
				{
					if (i_ai1 == 0)
						frintezzaPlaySong(npc, 1, 1500, 1000, 2000, 0);
					else if (i_ai1 == 30010)
						frintezzaPlaySong(npc, 1, 2000, 1000, 2000, 0);
				}
				else if (npc.getSpawn().getSpawnData().getDBValue() == 7)
				{
					if (i_ai1 == 0)
						frintezzaPlaySong(npc, 0, 1500, 625, 2500, 0);
					else if (i_ai1 == 30021)
						frintezzaPlaySong(npc, 1, 0, 625, 3000, 0);
					else if (i_ai1 == 30022)
					{
						if (getElapsedTicks(npc._i_quest2) > (15 * 60))
							frintezzaPlaySong(npc, 0, 1500, 0, 3000, 1500);
						else
							frintezzaPlaySong(npc, 1, 0, 1500, 3000, 0);
					}
					else if (i_ai1 == 30023)
					{
						if (getElapsedTicks(npc._i_quest2) > (15 * 60))
							frintezzaPlaySong(npc, 1, 0, 625, 3000, 1500);
						else
							frintezzaPlaySong(npc, 1, 0, 1500, 3000, 0);
					}
					else if (i_ai1 == 30024)
						frintezzaPlaySong(npc, 1, 1500, 0, 3000, 0);
				}
			}
		}
		else
		{
			frintezzaPlaySong(npc, 0, 0, 0, 10000, 0);
			
			npc._i_ai4 = 0;
		}
	}
	
	@Override
	public void onMyDying(Npc npc, Creature killer)
	{
		GlobalMemo.getInstance().remove(String.valueOf(GM_ID));
		// TODO Zones
		// gg::Area_SetOnOff(areadata_heal1,0);
		// gg::Area_SetOnOff(areadata_heal2,0);
		// gg::Area_SetOnOff(areadata_power1,0);
		// gg::Area_SetOnOff(areadata_power2,0);
		// gg::Area_SetOnOff(areadata_psycho1,0);
		// gg::Area_SetOnOff(areadata_psycho2,0);
		// gg::Area_SetOnOff(areadata_rampage1,0);
		// gg::Area_SetOnOff(areadata_rampage2,0);
		// gg::Area_SetOnOff(areadata_plague1,0);
		// gg::Area_SetOnOff(areadata_plague2,0);
		
		openCloseDoor(T_DOOR_NAME_1, 1);
		openCloseDoor(T_DOOR_NAME_2, 1);
		openCloseDoor(T_DOOR_NAME_3, 0);
		openCloseDoor(T_DOOR_NAME_4, 0);
		openCloseDoor(WALL_DOOR_NAME_1A, 1);
		openCloseDoor(WALL_DOOR_NAME_2A, 1);
		openCloseDoor(WALL_DOOR_NAME_3A, 1);
		openCloseDoor(WALL_DOOR_NAME_4A, 1);
		openCloseDoor(WALL_DOOR_NAME_5A, 1);
		openCloseDoor(WALL_DOOR_NAME_6A, 1);
		openCloseDoor(WALL_DOOR_NAME_7A, 1);
		openCloseDoor(WALL_DOOR_NAME_8A, 1);
		openCloseDoor(WALL_DOOR_NAME_1B, 1);
		openCloseDoor(WALL_DOOR_NAME_2B, 1);
		openCloseDoor(WALL_DOOR_NAME_3B, 1);
		openCloseDoor(WALL_DOOR_NAME_4B, 1);
		openCloseDoor(WALL_DOOR_NAME_5B, 1);
		openCloseDoor(WALL_DOOR_NAME_6B, 1);
		openCloseDoor(WALL_DOOR_NAME_7B, 1);
		openCloseDoor(WALL_DOOR_NAME_8B, 1);
		openCloseDoor(WALL_DOOR_NAME_9B, 1);
		openCloseDoor(WALL_DOOR_NAME_10B, 1);
		
		npc.getSpawn().getSpawnData().setDBValue(0);
		
		final NpcMaker maker0 = SpawnManager.getInstance().getNpcMaker("frintessa_teleportcuve");
		if (maker0 != null)
			maker0.getMaker().onMakerScriptEvent("1001", maker0, 0, 0);
	}
	
	@Override
	public void onNoDesire(Npc npc)
	{
		int i_ai1 = npc._i_ai1;
		
		if (npc.getSpawn().getSpawnData().getDBValue() < 4)
			return;
		
		if (npc._i_ai4 == 0)
		{
			if (npc.getSpawn().getSpawnData().getDBValue() == 3)
				frintezzaPlaySong(npc, 0, 0, 0, 0, 0);
			else if (npc.getSpawn().getSpawnData().getDBValue() == 4)
				frintezzaPlaySong(npc, 0, 0, 0, 0, 0);
			else if (npc.getSpawn().getSpawnData().getDBValue() == 5)
				frintezzaPlaySong(npc, 2, 2000, 0, 0, 0);
			else if (npc.getSpawn().getSpawnData().getDBValue() == 6)
			{
				if (i_ai1 == 0)
					frintezzaPlaySong(npc, 1, 1500, 1000, 2000, 0);
				else if (i_ai1 == 30010)
					frintezzaPlaySong(npc, 1, 2000, 1000, 2000, 0);
			}
			else if (npc.getSpawn().getSpawnData().getDBValue() == 7)
			{
				if (i_ai1 == 0)
					frintezzaPlaySong(npc, 0, 1500, 625, 2500, 0);
				else if (i_ai1 == 30021)
					frintezzaPlaySong(npc, 1, 0, 625, 3000, 0);
				else if (i_ai1 == 30022)
				{
					if (getElapsedTicks(npc._i_quest2) > (15 * 60))
						frintezzaPlaySong(npc, 0, 1500, 0, 3000, 1500);
					else
						frintezzaPlaySong(npc, 1, 0, 1500, 3000, 0);
				}
				else if (i_ai1 == 30023)
				{
					if (getElapsedTicks(npc._i_quest2) > (15 * 60))
						frintezzaPlaySong(npc, 1, 0, 625, 3000, 1500);
					else
						frintezzaPlaySong(npc, 1, 0, 1500, 3000, 0);
				}
				else if (i_ai1 == 30024)
					frintezzaPlaySong(npc, 1, 1500, 0, 3000, 0);
			}
		}
	}
	
	private static void frintezzaPlaySong(Npc npc, int chance1, int chance2, int chance3, int chance4, int chance5)
	{
		if (Rnd.get(10000) < chance1)
		{
			npc.getAI().addCastDesire(npc, MUSIC_OF_HEAL, 10000, false);
			npc.broadcastOnScreen(3000, NpcStringId.ID_1000522);
			// TODO Zones
			// gg::Area_SetOnOff(areadata_heal1,1);
			// gg::Area_SetOnOff(areadata_heal2,1);
		}
		else if (Rnd.get(10000) < chance2)
		{
			npc.getAI().addCastDesire(npc, MUSIC_OF_POWER, 10000, false);
			npc.broadcastOnScreen(3000, NpcStringId.ID_1000523);
			// TODO Zones
			// gg::Area_SetOnOff(areadata_power1,1);
			// gg::Area_SetOnOff(areadata_power2,1);
		}
		else if (Rnd.get(10000) < chance3)
		{
			npc.getAI().addCastDesire(npc, MUSIC_OF_PSYCHO, 10000, false);
			npc.broadcastOnScreen(3000, NpcStringId.ID_1000525);
			// TODO Zones
			// gg::Area_SetOnOff(areadata_psycho1,1);
			// gg::Area_SetOnOff(areadata_psycho2,1);
		}
		else if (Rnd.get(10000) < chance4)
		{
			npc.getAI().addCastDesire(npc, MUSIC_OF_RAMPAGE, 10000, false);
			npc.broadcastOnScreen(3000, NpcStringId.ID_1000524);
			// TODO Zones
			// gg::Area_SetOnOff(areadata_rampage1,1);
			// gg::Area_SetOnOff(areadata_rampage2,1);
		}
		else if (Rnd.get(10000) < chance5)
		{
			npc.getAI().addCastDesire(npc, MUSIC_OF_PLAGUE, 10000, false);
			npc.broadcastOnScreen(3000, NpcStringId.ID_1000526);
			// TODO Zones
			// gg::Area_SetOnOff(areadata_plague1,1);
			// gg::Area_SetOnOff(areadata_plague2,1);
		}
		else
		{
			npc.getAI().addCastDesire(npc, MUSIC, 10000, false);
			npc.broadcastOnScreen(3000, NpcStringId.ID_1000527);
		}
	}
}