package com.shnok.javaserver.gameserver.scripting.script.ai.boss.valakas;

import java.util.List;

import net.sf.l2j.commons.math.MathUtil;
import net.sf.l2j.commons.random.Rnd;

import net.sf.l2j.Config;
import net.sf.l2j.gameserver.data.SkillTable;
import net.sf.l2j.gameserver.enums.IntentionType;
import net.sf.l2j.gameserver.enums.actors.ClassId;
import net.sf.l2j.gameserver.model.actor.Creature;
import net.sf.l2j.gameserver.model.actor.Npc;
import net.sf.l2j.gameserver.model.actor.Player;
import net.sf.l2j.gameserver.model.location.Location;
import net.sf.l2j.gameserver.model.memo.GlobalMemo;
import net.sf.l2j.gameserver.network.NpcStringId;
import net.sf.l2j.gameserver.network.serverpackets.PlaySound;
import net.sf.l2j.gameserver.network.serverpackets.SpecialCamera;
import net.sf.l2j.gameserver.scripting.script.ai.individual.DefaultNpc;
import net.sf.l2j.gameserver.skills.L2Skill;
import net.sf.l2j.gameserver.taskmanager.GameTimeTaskManager;

public class Valakas extends DefaultNpc
{
	private static final String GM_ID = "3";
	
	private static final L2Skill POWER_UP = SkillTable.getInstance().getInfo(4680, 1);
	private static final L2Skill NORMAL_ATTACK_LEFT = SkillTable.getInstance().getInfo(4681, 1);
	private static final L2Skill NORMAL_ATTACK_RIGHT = SkillTable.getInstance().getInfo(4682, 1);
	private static final L2Skill BREATH_LOW = SkillTable.getInstance().getInfo(4683, 1);
	private static final L2Skill BREATH_HIGH = SkillTable.getInstance().getInfo(4684, 1);
	private static final L2Skill REAR_STRIKE = SkillTable.getInstance().getInfo(4685, 1);
	private static final L2Skill READY_REAR_ATTACK = SkillTable.getInstance().getInfo(4687, 1);
	private static final L2Skill REAR_THROW = SkillTable.getInstance().getInfo(4688, 1);
	private static final L2Skill FEAR = SkillTable.getInstance().getInfo(4689, 1);
	private static final L2Skill METEOR = SkillTable.getInstance().getInfo(4690, 1);
	
	private static final L2Skill VALAKAS_REGEN_1 = SkillTable.getInstance().getInfo(4691, 1);
	private static final L2Skill VALAKAS_REGEN_2 = SkillTable.getInstance().getInfo(4691, 2);
	private static final L2Skill VALAKAS_REGEN_3 = SkillTable.getInstance().getInfo(4691, 3);
	private static final L2Skill VALAKAS_REGEN_4 = SkillTable.getInstance().getInfo(4691, 4);
	
	private static final Location[] PRIVATE_POS_LIST_1 =
	{
		new Location(211936, -117568, -1264),
		new Location(212224, -117696, -1264),
		new Location(212484, -117860, -1264),
		new Location(212800, -118080, -1199),
		new Location(213124, -118088, -1280),
		new Location(213424, -118160, -1264),
		new Location(213732, -118192, -1248),
		new Location(214656, -117792, -1183),
		new Location(214916, -117552, -1264),
		new Location(215104, -117344, -1280),
		new Location(215364, -117248, -1264),
		new Location(215440, -117072, -1248),
		new Location(215512, -116868, -1264),
		new Location(215560, -116736, -1264),
		new Location(215684, -116616, -1232),
		new Location(215824, -116464, -1199),
		new Location(215956, -116304, -1199),
		new Location(216048, -116128, -1240),
		new Location(216112, -115904, -1228),
		new Location(214000, -118240, -1251),
		new Location(214160, -118240, -1248),
		new Location(216208, -115680, -1252),
		new Location(216288, -115440, -1232),
		new Location(216288, -115120, -1244),
		new Location(216352, -114816, -1199),
		new Location(216328, -114472, -1199),
		new Location(216288, -114128, -1199),
		new Location(216248, -113760, -1256),
		new Location(216172, -113476, -1244),
		new Location(216144, -113232, -1254),
		new Location(215904, -113088, -1228),
		new Location(215840, -112688, -1248),
		new Location(215728, -112384, -1199),
		new Location(215480, -112288, -1248),
		new Location(215260, -112192, -1264),
		new Location(215184, -111920, -1261),
		new Location(214896, -111712, -1264),
		new Location(214640, -111520, -1199),
		new Location(214400, -111472, -1392),
		new Location(214176, -111408, -1264),
		new Location(213944, -111360, -1248),
		new Location(213728, -111216, -1264),
		new Location(213464, -111160, -1264),
		new Location(213288, -111328, -1248),
		new Location(213132, -111504, -1264),
		new Location(212928, -111608, -1280),
		new Location(212680, -111632, -1264),
		new Location(212492, -111392, -1199),
		new Location(212240, -111264, -1258),
		new Location(211996, -111308, -1251),
		new Location(211744, -111412, -1248),
		new Location(211472, -111392, -1248),
		new Location(211272, -111512, -1232),
		new Location(211268, -111768, -1199),
		new Location(211296, -112048, -1280),
		new Location(211120, -112072, -1264),
		new Location(210868, -112184, -1276),
		new Location(210608, -112292, -1248),
		new Location(210200, -112576, -1280),
		new Location(210055, -112568, -1264),
		new Location(209880, -112924, -1312),
		new Location(209744, -113216, -1309),
		new Location(209528, -113404, -1312),
		new Location(209616, -113712, -1323),
		new Location(209656, -114044, -1328),
		new Location(209760, -114240, -1328),
		new Location(209880, -114472, -1312),
		new Location(209792, -114752, -1296),
		new Location(209728, -115008, -1296),
		new Location(209824, -115232, -1296),
		new Location(209948, -115504, -1280),
		new Location(210128, -115760, -1280),
		new Location(210228, -116064, -1248),
		new Location(210448, -116272, -1248),
		new Location(210660, -116416, -1248),
		new Location(210800, -116624, -1199),
		new Location(210588, -116772, -1199),
		new Location(210720, -117072, -1232),
		new Location(210912, -117248, -1216),
		new Location(211104, -117408, -1223),
		new Location(211252, -117552, -1227),
		new Location(211488, -117632, -1199),
		new Location(214256, -118240, -1248),
		new Location(214432, -118176, -1242),
		new Location(214640, -118064, -1183),
		new Location(214704, -117976, -1244)
	};
	
	private static final Location[] PRIVATE_POS_LIST_2 =
	{
		new Location(212256, -118096, -1280),
		new Location(212576, -118272, -1280),
		new Location(212896, -118384, -1289),
		new Location(213200, -118400, -1296),
		new Location(213584, -118496, -1276),
		new Location(213872, -118448, -1264),
		new Location(214224, -118416, -1285),
		new Location(214480, -118416, -1270),
		new Location(214704, -118304, -1274),
		new Location(214960, -118048, -1264),
		new Location(215424, -117984, -1255),
		new Location(215264, -117568, -1264),
		new Location(215824, -117504, -1248),
		new Location(215600, -117648, -1255),
		new Location(215728, -117056, -1258),
		new Location(216000, -116752, -1248),
		new Location(216160, -116320, -1264),
		new Location(216384, -116000, -1264),
		new Location(216368, -115696, -1288),
		new Location(216512, -115312, -1284),
		new Location(216496, -114976, -1289),
		new Location(216496, -114560, -1242),
		new Location(216592, -114224, -1277),
		new Location(216464, -113984, -1289),
		new Location(216416, -113616, -1285),
		new Location(216352, -113216, -1280),
		new Location(216192, -112944, -1259),
		new Location(216112, -112560, -1280),
		new Location(215904, -112288, -1264),
		new Location(215680, -111936, -1248),
		new Location(215408, -111888, -1271),
		new Location(215440, -111488, -1258),
		new Location(214976, -111440, -1264),
		new Location(214672, -111136, -1280),
		new Location(214304, -111248, -1280),
		new Location(214032, -110848, -1264),
		new Location(213440, -110768, -1260),
		new Location(213760, -110640, -1240),
		new Location(213056, -111104, -1273),
		new Location(212672, -111008, -1264),
		new Location(212256, -111088, -1280),
		new Location(211920, -111168, -1287),
		new Location(211504, -111200, -1283),
		new Location(210912, -111536, -1264),
		new Location(211008, -111888, -1280),
		new Location(210528, -111728, -1275),
		new Location(210320, -112160, -1272),
		new Location(209888, -112048, -1291),
		new Location(209904, -112528, -1296),
		new Location(209536, -112880, -1328),
		new Location(209280, -113280, -1328),
		new Location(209296, -113696, -1344),
		new Location(209280, -114016, -1344),
		new Location(209360, -114272, -1338),
		new Location(209376, -114512, -1322),
		new Location(209376, -114752, -1306),
		new Location(209456, -115104, -1296),
		new Location(209296, -115520, -1264),
		new Location(209600, -115680, -1273),
		new Location(209600, -116048, -1264),
		new Location(209856, -116128, -1272),
		new Location(209648, -116352, -1264),
		new Location(209952, -116336, -1264),
		new Location(210112, -116416, -1264)
	};
	
	private static final Location[] FLEE_POINTS =
	{
		new Location(212496, -114480, -1636),
		new Location(213360, -114144, -1636),
		new Location(213424, -114816, -1636),
		new Location(212560, -115264, -1636),
		new Location(212896, -114592, -1636),
		new Location(215488, -115216, -1648),
		new Location(215424, -113984, -1636),
		new Location(211792, -112112, -1636),
		new Location(210608, -112816, -1636),
		new Location(211520, -116816, -1636),
		new Location(212608, -117280, -1636)
	};
	
	public Valakas()
	{
		super("ai/boss/valakas");
	}
	
	public Valakas(String descr)
	{
		super(descr);
	}
	
	protected final int[] _npcIds =
	{
		29028, // valakas
	};
	
	@Override
	public void onNoDesire(Npc npc)
	{
		if (npc.getSpawn().getSpawnData().getDBValue() == 3)
		{
			npc.getAI().addWanderDesire(5, 5);
		}
		else if (npc.getSpawn().getSpawnData().getDBValue() == 0)
		{
			npc.removeAllDesire();
		}
	}
	
	@Override
	public void onCreated(Npc npc)
	{
		final int i0 = GlobalMemo.getInstance().getInteger(String.valueOf(GM_ID), -1);
		if (i0 == -1)
			GlobalMemo.getInstance().set(String.valueOf(GM_ID), npc.getObjectId());
		
		if (!npc.getSpawn().getDBLoaded())
			npc.getSpawn().getSpawnData().setDBValue(0);
		else if (npc.getSpawn().getSpawnData().getDBValue() == 1)
			startQuestTimer("1001", npc, null, 600000);
		
		npc._i_ai0 = 0;
		npc._i_ai1 = 0;
		npc._i_ai2 = 0;
		npc._i_ai3 = 0;
		npc._i_ai4 = 0;
		npc._i_quest0 = 0;
		npc._i_quest1 = GameTimeTaskManager.getInstance().getCurrentTick();
		
		if (npc.getSpawn().getSpawnData().getDBValue() == 3)
		{
			npc.getAI().addWanderDesire(5, 5);
			
			startQuestTimer("1002", npc, null, 60000);
		}
		
		startQuestTimer("1003", npc, null, 600000);
		
		super.onCreated(npc);
	}
	
	@Override
	public void onAttacked(Npc npc, Creature attacker, int damage, L2Skill skill)
	{
		int i0 = 0;
		int i1 = 0;
		
		if (npc.getSpawn().getSpawnData().getDBValue() == 0 && !npc.isDead() && attacker instanceof Player)
			attacker.teleportTo(150037, -57255, -2976, 0);
		
		npc._i_quest1 = GameTimeTaskManager.getInstance().getCurrentTick();
		
		if (attacker.getZ() < (npc.getZ() + 200))
		{
			if (npc._i_ai2 == 0)
				npc._i_ai1 += damage;
			
			if (npc._i_quest0 == 0)
				npc._i_ai4 += damage;
			
			if (MathUtil.calculateAngleFrom(npc, attacker) > (180 - 30) && MathUtil.calculateAngleFrom(npc, attacker) < (180 + 30))
			{
				if (npc._i_quest0 == 0)
					npc._i_ai3 += damage;
			}
		}
		else if (npc._i_ai2 == 0)
			npc._i_ai0 += damage;
		
		if (Rnd.get(100) < 2)
		{
			List<Location> plist0 = attacker.getPosition().getClosestPositionList(PRIVATE_POS_LIST_1, 2);
			if (!plist0.isEmpty())
			{
				Location pos0 = plist0.get(0);
				createOnePrivateEx(npc, 29029, pos0.getX(), pos0.getY(), pos0.getZ(), 0, 0, false, attacker.getObjectId(), 0, 0);
				
				if (Rnd.get(100) < 50)
				{
					pos0 = plist0.get(1);
					createOnePrivateEx(npc, 29029, pos0.getX(), pos0.getY(), pos0.getZ(), 0, 0, false, attacker.getObjectId(), 0, 0);
				}
			}
			
			plist0 = attacker.getPosition().getClosestPositionList(PRIVATE_POS_LIST_2, 2);
			if (!plist0.isEmpty())
			{
				Location pos0 = plist0.get(0);
				createOnePrivateEx(npc, 29029, pos0.getX(), pos0.getY(), pos0.getZ(), 0, 0, false, attacker.getObjectId(), 0, 0);
				
				if (Rnd.get(100) < 50)
				{
					pos0 = plist0.get(1);
					createOnePrivateEx(npc, 29029, pos0.getX(), pos0.getY(), pos0.getZ(), 0, 0, false, attacker.getObjectId(), 0, 0);
				}
			}
		}
		
		if (npc._i_ai2 == 0)
		{
			i0 = npc._i_ai0 + npc._i_ai1;
			i1 = (i0 / npc.getStatus().getMaxHp()) * 100;
			
			if ((i1 > 3 && npc._i_ai0 > (npc._i_ai1 * 2)) || i1 > 5)
			{
				npc._i_ai0 = 0;
				npc._i_ai1 = 0;
				npc._i_ai2 = 1;
				
				npc.removeAllAttackDesire();
				
				final Location pos0 = npc.getPosition().getClosestPosition(FLEE_POINTS);
				if (pos0 != null)
				{
					i0 = Rnd.get(3);
					if (i0 == 2)
						i0 = Rnd.get(3);
					
					npc.getAI().addMoveToDesire(pos0, 100000000);
				}
			}
		}
		
		if (npc._i_quest0 == 0)
		{
			if (((npc._i_ai4 / npc.getStatus().getMaxHp()) * 100) > 1)
			{
				if (npc._i_ai3 > (npc._i_ai4 - npc._i_ai3))
				{
					npc._i_ai3 = 0;
					npc._i_ai4 = 0;
					npc.getAI().addCastDesire(npc, READY_REAR_ATTACK, 1000000);
					npc._i_quest0 = 1;
				}
			}
		}
		
		final L2Skill hinderStrider = SkillTable.getInstance().getInfo(4258, 1);
		if (attacker.isRiding() && getAbnormalLevel(attacker, hinderStrider) <= 0)
			npc.getAI().addCastDesire(attacker, hinderStrider, 1000000);
		
		if (skill == null)
		{
			if (attacker == npc._c_quest2)
			{
				if (((damage * 1000) + 1000) > npc._i_quest2)
					npc._i_quest2 = (damage * 1000) + Rnd.get(3000);
			}
			else if (attacker == npc._c_quest3)
			{
				if (((damage * 1000) + 1000) > npc._i_quest3)
					npc._i_quest3 = (damage * 1000) + Rnd.get(3000);
			}
			else if (attacker == npc._c_quest4)
			{
				if (((damage * 1000) + 1000) > npc._i_quest4)
					npc._i_quest4 = (damage * 1000) + Rnd.get(3000);
			}
			else if (npc._i_quest2 > npc._i_quest3)
				i1 = 3;
			else if (npc._i_quest2 == npc._i_quest3)
				i1 = (Rnd.nextBoolean()) ? 2 : 3;
			else if (npc._i_quest2 < npc._i_quest3)
				i1 = 2;
			
			if (i1 == 2)
			{
				if (npc._i_quest2 > npc._i_quest4)
					i1 = 4;
				else if (npc._i_quest2 == npc._i_quest4)
					i1 = (Rnd.nextBoolean()) ? 2 : 4;
				else if (npc._i_quest2 < npc._i_quest4)
					i1 = 2;
			}
			else if (i1 == 3)
			{
				if (npc._i_quest3 > npc._i_quest4)
					i1 = 4;
				else if (npc._i_quest3 == npc._i_quest4)
					i1 = (Rnd.nextBoolean()) ? 3 : 4;
				else if (npc._i_quest3 < npc._i_quest4)
					i1 = 3;
			}
			
			switch (i1)
			{
				case 2:
					npc._i_quest2 = (damage * 1000) + Rnd.get(3000);
					npc._c_quest2 = attacker;
					break;
				
				case 3:
					npc._i_quest3 = (damage * 1000) + Rnd.get(3000);
					npc._c_quest3 = attacker;
					break;
				
				case 4:
					npc._i_quest4 = (damage * 1000) + Rnd.get(3000);
					npc._c_quest4 = attacker;
					break;
			}
		}
		else if (npc.getStatus().getHp() < ((npc.getStatus().getMaxHp() * 1.0) / 4.0))
		{
			if (attacker == npc._c_quest2)
			{
				if ((((damage / 30.0) * 1000) + 1000) > npc._i_quest2)
					npc._i_quest2 = (int) ((damage / 30.0) * 1000) + Rnd.get(3000);
			}
			else if (attacker == npc._c_quest3)
			{
				if ((((damage / 30.0) * 1000) + 1000) > npc._i_quest3)
					npc._i_quest3 = (int) ((damage / 30.0) * 1000) + Rnd.get(3000);
			}
			else if (attacker == npc._c_quest4)
			{
				if ((((damage / 30.0) * 1000) + 1000) > npc._i_quest4)
					npc._i_quest4 = (int) ((damage / 30.0) * 1000) + Rnd.get(3000);
			}
			else if (npc._i_quest2 > npc._i_quest3)
				i1 = 3;
			else if (npc._i_quest2 == npc._i_quest3)
				i1 = (Rnd.nextBoolean()) ? 2 : 3;
			else if (npc._i_quest2 < npc._i_quest3)
				i1 = 2;
			
			if (i1 == 2)
			{
				if (npc._i_quest2 > npc._i_quest4)
					i1 = 4;
				else if (npc._i_quest2 == npc._i_quest4)
					i1 = (Rnd.nextBoolean()) ? 2 : 4;
				else if (npc._i_quest2 < npc._i_quest4)
					i1 = 2;
			}
			else if (i1 == 3)
			{
				if (npc._i_quest3 > npc._i_quest4)
					i1 = 4;
				else if (npc._i_quest3 == npc._i_quest4)
					i1 = (Rnd.nextBoolean()) ? 3 : 4;
				else if (npc._i_quest3 < npc._i_quest4)
					i1 = 3;
			}
			
			switch (i1)
			{
				case 2:
					npc._i_quest2 = (int) ((damage / 30.0) * 1000) + Rnd.get(3000);
					npc._c_quest2 = attacker;
					break;
				
				case 3:
					npc._i_quest3 = (int) ((damage / 30.0) * 1000) + Rnd.get(3000);
					npc._c_quest3 = attacker;
					break;
				
				case 4:
					npc._i_quest4 = (int) ((damage / 30.0) * 1000) + Rnd.get(3000);
					npc._c_quest4 = attacker;
					break;
			}
		}
		else if (npc.getStatus().getHp() < ((npc.getStatus().getMaxHp() * 2.0) / 4.0))
		{
			if (attacker == npc._c_quest2)
			{
				if ((((damage / 50.0) * 1000) + 1000) > npc._i_quest2)
					npc._i_quest2 = (int) ((damage / 50.0) * 1000) + Rnd.get(3000);
			}
			else if (attacker == npc._c_quest3)
			{
				if ((((damage / 50.0) * 1000) + 1000) > npc._i_quest3)
					npc._i_quest3 = (int) ((damage / 50.0) * 1000) + Rnd.get(3000);
			}
			else if (attacker == npc._c_quest4)
			{
				if ((((damage / 50.0) * 1000) + 1000) > npc._i_quest4)
					npc._i_quest4 = (int) ((damage / 50.0) * 1000) + Rnd.get(3000);
			}
			else if (npc._i_quest2 > npc._i_quest3)
				i1 = 3;
			else if (npc._i_quest2 == npc._i_quest3)
				i1 = (Rnd.nextBoolean()) ? 2 : 3;
			else if (npc._i_quest2 < npc._i_quest3)
				i1 = 2;
			
			if (i1 == 2)
			{
				if (npc._i_quest2 > npc._i_quest4)
					i1 = 4;
				else if (npc._i_quest2 == npc._i_quest4)
					i1 = (Rnd.nextBoolean()) ? 2 : 4;
				else if (npc._i_quest2 < npc._i_quest4)
					i1 = 2;
			}
			else if (i1 == 3)
			{
				if (npc._i_quest3 > npc._i_quest4)
					i1 = 4;
				else if (npc._i_quest3 == npc._i_quest4)
					i1 = (Rnd.nextBoolean()) ? 3 : 4;
				else if (npc._i_quest3 < npc._i_quest4)
					i1 = 3;
			}
			
			switch (i1)
			{
				case 2:
					npc._i_quest2 = (int) ((damage / 50.0) * 1000) + Rnd.get(3000);
					npc._c_quest2 = attacker;
					break;
				
				case 3:
					npc._i_quest3 = (int) ((damage / 50.0) * 1000) + Rnd.get(3000);
					npc._c_quest3 = attacker;
					break;
				
				case 4:
					npc._i_quest4 = (int) ((damage / 50.0) * 1000) + Rnd.get(3000);
					npc._c_quest4 = attacker;
					break;
			}
		}
		else if (npc.getStatus().getHp() < ((npc.getStatus().getMaxHp() * 3.0) / 4.0))
		{
			if (attacker == npc._c_quest2)
			{
				if ((((damage / 100.0) * 1000) + 1000) > npc._i_quest2)
					npc._i_quest2 = (int) ((damage / 100.0) * 1000) + Rnd.get(3000);
			}
			else if (attacker == npc._c_quest3)
			{
				if ((((damage / 100.0) * 1000) + 1000) > npc._i_quest3)
					npc._i_quest3 = (int) ((damage / 100.0) * 1000) + Rnd.get(3000);
			}
			else if (attacker == npc._c_quest4)
			{
				if ((((damage / 100.0) * 1000) + 1000) > npc._i_quest4)
					npc._i_quest4 = (int) ((damage / 100.0) * 1000) + Rnd.get(3000);
			}
			else if (npc._i_quest2 > npc._i_quest3)
				i1 = 3;
			else if (npc._i_quest2 == npc._i_quest3)
				i1 = (Rnd.nextBoolean()) ? 2 : 3;
			else if (npc._i_quest2 < npc._i_quest3)
				i1 = 2;
			
			if (i1 == 2)
			{
				if (npc._i_quest2 > npc._i_quest4)
					i1 = 4;
				else if (npc._i_quest2 == npc._i_quest4)
					i1 = (Rnd.nextBoolean()) ? 2 : 4;
				else if (npc._i_quest2 < npc._i_quest4)
					i1 = 2;
			}
			else if (i1 == 3)
			{
				if (npc._i_quest3 > npc._i_quest4)
					i1 = 4;
				else if (npc._i_quest3 == npc._i_quest4)
					i1 = (Rnd.nextBoolean()) ? 3 : 4;
				else if (npc._i_quest3 < npc._i_quest4)
					i1 = 3;
			}
			
			switch (i1)
			{
				case 2:
					npc._i_quest2 = (int) ((damage / 100.0) * 1000) + Rnd.get(3000);
					npc._c_quest2 = attacker;
					break;
				
				case 3:
					npc._i_quest3 = (int) ((damage / 100.0) * 1000) + Rnd.get(3000);
					npc._c_quest3 = attacker;
					break;
				
				case 4:
					npc._i_quest4 = (int) ((damage / 100.0) * 1000) + Rnd.get(3000);
					npc._c_quest4 = attacker;
					break;
			}
		}
		else if (attacker == npc._c_quest2)
		{
			if ((((damage / 150.0) * 1000) + 1000) > npc._i_quest2)
				npc._i_quest2 = (int) ((damage / 150.0) * 1000) + Rnd.get(3000);
		}
		else if (attacker == npc._c_quest3)
		{
			if ((((damage / 150.0) * 1000) + 1000) > npc._i_quest3)
				npc._i_quest3 = (int) ((damage / 150.0) * 1000) + Rnd.get(3000);
		}
		else if (attacker == npc._c_quest4)
		{
			if ((((damage / 150.0) * 1000) + 1000) > npc._i_quest4)
				npc._i_quest4 = (int) ((damage / 150.0) * 1000) + Rnd.get(3000);
		}
		else if (npc._i_quest2 > npc._i_quest3)
			i1 = 3;
		else if (npc._i_quest2 == npc._i_quest3)
			i1 = (Rnd.nextBoolean()) ? 2 : 3;
		else if (npc._i_quest2 < npc._i_quest3)
			i1 = 2;
		
		if (i1 == 2)
		{
			if (npc._i_quest2 > npc._i_quest4)
				i1 = 4;
			else if (npc._i_quest2 == npc._i_quest4)
				i1 = (Rnd.nextBoolean()) ? 2 : 4;
			else if (npc._i_quest2 < npc._i_quest4)
				i1 = 2;
		}
		else if (i1 == 3)
		{
			if (npc._i_quest3 > npc._i_quest4)
				i1 = 4;
			else if (npc._i_quest3 == npc._i_quest4)
				i1 = (Rnd.nextBoolean()) ? 3 : 4;
			else if (npc._i_quest3 < npc._i_quest4)
				i1 = 3;
		}
		
		switch (i1)
		{
			case 2:
				npc._i_quest2 = (int) ((damage / 150.0) * 1000) + Rnd.get(3000);
				npc._c_quest2 = attacker;
				break;
			
			case 3:
				npc._i_quest3 = (int) ((damage / 150.0) * 1000) + Rnd.get(3000);
				npc._c_quest3 = attacker;
				break;
			
			case 4:
				npc._i_quest4 = (int) ((damage / 150.0) * 1000) + Rnd.get(3000);
				npc._c_quest4 = attacker;
				break;
		}
		
		IntentionType currentIntentionType = npc.getAI().getCurrentIntention().getType();
		
		if (npc.getSpawn().getSpawnData().getDBValue() == 3 && (currentIntentionType == IntentionType.WANDER || currentIntentionType == IntentionType.IDLE))
		{
			int i2 = 0;
			Creature c2 = null;
			
			if (npc._c_quest2 == null || npc.distance3D(npc._c_quest2) > 5000 || npc._c_quest2.isDead())
				npc._i_quest2 = 0;
			
			if (npc._c_quest3 == null || npc.distance3D(npc._c_quest3) > 5000 || npc._c_quest3.isDead())
				npc._i_quest3 = 0;
			
			if (npc._c_quest4 == null || npc.distance3D(npc._c_quest4) > 5000 || npc._c_quest4.isDead())
				npc._i_quest4 = 0;
			
			if (npc._i_quest2 > npc._i_quest3)
			{
				i1 = 2;
				i2 = npc._i_quest2;
				c2 = npc._c_quest2;
			}
			else
			{
				i1 = 3;
				i2 = npc._i_quest3;
				c2 = npc._c_quest3;
			}
			
			if (npc._i_quest4 > i2)
			{
				i1 = 4;
				i2 = npc._i_quest4;
				c2 = npc._c_quest4;
			}
			
			if (i2 > 0)
			{
				if (Rnd.get(100) < 70)
				{
					switch (i1)
					{
						case 2:
							npc._i_quest2 = 500;
							break;
						
						case 3:
							npc._i_quest3 = 500;
							break;
						
						case 4:
							npc._i_quest4 = 500;
							break;
					}
				}
				
				final double hpRatio = npc.getStatus().getHpRatio();
				if (hpRatio < 0.25)
					valakasCastSkills(npc, c2, 20, 15, 15, 10, 35, 20, 15);
				else if (hpRatio < 0.5)
					valakasCastSkills(npc, c2, 5, 10, 10, 10, 20, 5, 10);
				else if (hpRatio < 0.75)
					valakasCastSkills(npc, c2, 0, 5, 5, 10, 15, 0, 5);
				else
					valakasCastSkills(npc, c2, 0, 10, 5, 10, 15, 0, 10);
			}
		}
	}
	
	@Override
	public void onMoveToFinished(Npc npc, int x, int y, int z)
	{
		if (npc._i_ai2 == 1)
		{
			npc._i_ai2 = 0;
			
			npc.getAI().addCastDesire(npc, POWER_UP, 1000000);
		}
	}
	
	@Override
	public void onScriptEvent(Npc npc, int eventId, int arg1, int arg2)
	{
		if (npc.getSpawn().getSpawnData().getDBValue() == 0)
		{
			startQuestTimer("1001", npc, null, Config.WAIT_TIME_VALAKAS);
			
			npc.getSpawn().getSpawnData().setDBValue(1);
		}
		
		super.onScriptEvent(npc, eventId, arg1, arg2);
	}
	
	@Override
	public String onTimer(String name, Npc npc, Player player)
	{
		if (name.equalsIgnoreCase("1001"))
		{
			npc._i_quest1 = GameTimeTaskManager.getInstance().getCurrentTick();
			
			npc.getSpawn().getSpawnData().setDBValue(2);
			npc.teleportTo(212852, -114842, -1632, 0);
			npc.broadcastPacket(new PlaySound(1, "B03_A", npc));
			
			startQuestTimer("1004", npc, null, 2000);
		}
		else if (name.equalsIgnoreCase("1002"))
		{
			if (npc.getSpawn().getSpawnData().getDBValue() == 3)
			{
				if (getElapsedTicks(npc._i_quest1) > (15 * 60))
				{
					npc.getSpawn().getSpawnData().setDBValue(0);
					npc.removeAllDesire();
					npc.getSpawn().instantTeleportInMyTerritory(150037, -57255, -2976, 150);
					npc.teleportTo(-105200, -253104, -15264, 0);
					
					npc._i_quest2 = 0;
					npc._i_quest3 = 0;
					npc._i_quest4 = 0;
				}
				else if (npc.getStatus().getHp() < ((npc.getStatus().getMaxHp() * 1.0) / 4.0))
				{
					if (getAbnormalLevel(npc, VALAKAS_REGEN_4) < 14)
						npc.getAI().addCastDesire(npc, VALAKAS_REGEN_4, 4000000);
				}
				else if (npc.getStatus().getHp() < ((npc.getStatus().getMaxHp() * 2.0) / 4.0))
				{
					if (getAbnormalLevel(npc, VALAKAS_REGEN_3) < 13)
						npc.getAI().addCastDesire(npc, VALAKAS_REGEN_3, 4000000);
				}
				else if (npc.getStatus().getHp() < ((npc.getStatus().getMaxHp() * 3.0) / 4.0))
				{
					if (getAbnormalLevel(npc, VALAKAS_REGEN_2) < 12)
						npc.getAI().addCastDesire(npc, VALAKAS_REGEN_2, 4000000);
				}
				else if (getAbnormalLevel(npc, VALAKAS_REGEN_1) < 11)
					npc.getAI().addCastDesire(npc, VALAKAS_REGEN_1, 4000000);
				
				startQuestTimer("1002", npc, player, 60000);
			}
		}
		else if (name.equalsIgnoreCase("1003"))
		{
			IntentionType currentIntentionType = npc.getAI().getCurrentIntention().getType();
			
			if (npc.getSpawn().getSpawnData().getDBValue() == 3 && (currentIntentionType == IntentionType.WANDER || currentIntentionType == IntentionType.IDLE))
			{
				int i1 = 0;
				int i2 = 0;
				
				Creature c2 = null;
				
				if (npc._c_quest2 == null || npc.distance3D(npc._c_quest2) > 5000 || npc._c_quest2.isDead())
					npc._i_quest2 = 0;
				
				if (npc._c_quest3 == null || npc.distance3D(npc._c_quest3) > 5000 || npc._c_quest3.isDead())
					npc._i_quest3 = 0;
				
				if (npc._c_quest4 == null || npc.distance3D(npc._c_quest4) > 5000 || npc._c_quest4.isDead())
					npc._i_quest4 = 0;
				
				if (npc._i_quest2 > npc._i_quest3)
				{
					i1 = 2;
					i2 = npc._i_quest2;
					c2 = npc._c_quest2;
				}
				else
				{
					i1 = 3;
					i2 = npc._i_quest3;
					c2 = npc._c_quest3;
				}
				
				if (npc._i_quest4 > i2)
				{
					i1 = 4;
					i2 = npc._i_quest4;
					c2 = npc._c_quest4;
				}
				
				if (i2 > 0)
				{
					if (Rnd.get(100) < 70)
					{
						switch (i1)
						{
							case 2:
								npc._i_quest2 = 500;
								break;
							
							case 3:
								npc._i_quest3 = 500;
								break;
							
							case 4:
								npc._i_quest4 = 500;
								break;
						}
					}
					
					final double hpRatio = npc.getStatus().getHpRatio();
					if (hpRatio < 0.25)
						valakasCastSkills(npc, c2, 20, 15, 15, 10, 35, 20, 15);
					else if (hpRatio < 0.5)
						valakasCastSkills(npc, c2, 5, 10, 10, 10, 20, 5, 10);
					else if (hpRatio < 0.75)
						valakasCastSkills(npc, c2, 0, 5, 5, 10, 15, 0, 5);
					else
						valakasCastSkills(npc, c2, 0, 10, 5, 10, 15, 0, 10);
				}
			}
			
			startQuestTimer("1003", npc, player, 60000);
		}
		else if (name.equalsIgnoreCase("1004"))
		{
			npc.broadcastPacket(new SpecialCamera(npc.getObjectId(), 1800, 180, -1, 1500, 10000, 0, 0, 1, 0));
			
			startQuestTimer("1102", npc, player, 1500);
		}
		else if (name.equalsIgnoreCase("1102"))
		{
			npc.broadcastPacket(new SpecialCamera(npc.getObjectId(), 1300, 180, -5, 3000, 10000, 0, -5, 1, 0));
			
			startQuestTimer("1103", npc, player, 3300);
		}
		else if (name.equalsIgnoreCase("1103"))
		{
			npc.broadcastPacket(new SpecialCamera(npc.getObjectId(), 500, 180, -8, 600, 10000, 0, 60, 1, 0));
			
			startQuestTimer("1104", npc, player, 2900);
		}
		else if (name.equalsIgnoreCase("1104"))
		{
			npc.broadcastPacket(new SpecialCamera(npc.getObjectId(), 800, 180, -8, 2700, 10000, 0, 30, 1, 0));
			
			startQuestTimer("1105", npc, player, 2700);
		}
		else if (name.equalsIgnoreCase("1105"))
		{
			npc.broadcastPacket(new SpecialCamera(npc.getObjectId(), 200, 250, 70, 0, 10000, 30, 80, 1, 0));
			
			startQuestTimer("1106", npc, player, 1);
		}
		else if (name.equalsIgnoreCase("1106"))
		{
			npc.broadcastPacket(new SpecialCamera(npc.getObjectId(), 1100, 250, 70, 2500, 10000, 30, 80, 1, 0));
			
			startQuestTimer("1107", npc, player, 3200);
		}
		else if (name.equalsIgnoreCase("1107"))
		{
			npc.broadcastPacket(new SpecialCamera(npc.getObjectId(), 700, 150, 30, 0, 10000, -10, 60, 1, 0));
			
			startQuestTimer("1108", npc, player, 1400);
		}
		else if (name.equalsIgnoreCase("1108"))
		{
			npc.broadcastPacket(new SpecialCamera(npc.getObjectId(), 1200, 150, 20, 2900, 10000, -10, 30, 1, 0));
			
			startQuestTimer("1109", npc, player, 6700);
		}
		else if (name.equalsIgnoreCase("1109"))
		{
			npc.broadcastPacket(new SpecialCamera(npc.getObjectId(), 750, 170, -10, 3400, 4000, 10, -15, 1, 0));
			
			startQuestTimer("1110", npc, player, 5700);
		}
		else if (name.equalsIgnoreCase("1110"))
		{
			npc.getAI().addCastDesire(npc, 4691, 1, 4000000);
			npc.getSpawn().getSpawnData().setDBValue(3);
			
			startQuestTimer("1002", npc, player, 60000);
			
			final Player heroPlayer = npc.getNeighborHero(4000);
			if (heroPlayer != null)
				npc.broadcastOnScreen(3000, NpcStringId.ID_1000519);
			
			int i1 = 0;
			int i2 = 0;
			
			Creature c2 = null;
			
			if (npc._c_quest2 == null || npc.distance3D(npc._c_quest2) > 5000 || npc._c_quest2.isDead())
				npc._i_quest2 = 0;
			
			if (npc._c_quest3 == null || npc.distance3D(npc._c_quest3) > 5000 || npc._c_quest3.isDead())
				npc._i_quest3 = 0;
			
			if (npc._c_quest4 == null || npc.distance3D(npc._c_quest4) > 5000 || npc._c_quest4.isDead())
				npc._i_quest4 = 0;
			
			if (npc._i_quest2 > npc._i_quest3)
			{
				i1 = 2;
				i2 = npc._i_quest2;
				c2 = npc._c_quest2;
			}
			else
			{
				i1 = 3;
				i2 = npc._i_quest3;
				c2 = npc._c_quest3;
			}
			
			if (npc._i_quest4 > i2)
			{
				i1 = 4;
				i2 = npc._i_quest4;
				c2 = npc._c_quest4;
			}
			
			if (i2 > 0)
			{
				if (Rnd.get(100) < 70)
				{
					switch (i1)
					{
						case 2:
							npc._i_quest2 = 500;
							break;
						
						case 3:
							npc._i_quest3 = 500;
							break;
						
						case 4:
							npc._i_quest4 = 500;
							break;
					}
				}
				
				final double hpRatio = npc.getStatus().getHpRatio();
				if (hpRatio < 0.25)
					valakasCastSkills(npc, c2, 20, 15, 15, 10, 35, 20, 15);
				else if (hpRatio < 0.5)
					valakasCastSkills(npc, c2, 5, 10, 10, 10, 20, 5, 10);
				else if (hpRatio < 0.75)
					valakasCastSkills(npc, c2, 0, 5, 5, 10, 15, 0, 5);
				else
					valakasCastSkills(npc, c2, 0, 10, 5, 10, 15, 0, 10);
			}
		}
		else if (name.equalsIgnoreCase("1111"))
		{
			npc.broadcastPacket(new SpecialCamera(npc.getObjectId(), 1100, 210, -5, 3000, 10000, -13, 0, 1, 1));
			
			startQuestTimer("1112", npc, player, 3500);
		}
		else if (name.equalsIgnoreCase("1112"))
		{
			npc.broadcastPacket(new SpecialCamera(npc.getObjectId(), 1300, 200, -8, 3000, 10000, 0, 15, 1, 1));
			
			startQuestTimer("1113", npc, player, 4500);
		}
		else if (name.equalsIgnoreCase("1113"))
		{
			npc.broadcastPacket(new SpecialCamera(npc.getObjectId(), 1000, 190, 0, 500, 10000, 0, 10, 1, 1));
			
			startQuestTimer("1114", npc, player, 500);
		}
		else if (name.equalsIgnoreCase("1114"))
		{
			npc.broadcastPacket(new SpecialCamera(npc.getObjectId(), 1700, 120, 0, 2500, 10000, 12, 40, 1, 1));
			
			startQuestTimer("1115", npc, player, 4600);
		}
		else if (name.equalsIgnoreCase("1115"))
		{
			npc.broadcastPacket(new SpecialCamera(npc.getObjectId(), 1700, 20, 0, 700, 10000, 10, 10, 1, 1));
			
			startQuestTimer("1116", npc, player, 750);
		}
		else if (name.equalsIgnoreCase("1116"))
		{
			npc.broadcastPacket(new SpecialCamera(npc.getObjectId(), 1700, 10, 0, 1000, 10000, 20, 70, 1, 1));
			
			startQuestTimer("1117", npc, player, 2500);
		}
		else if (name.equalsIgnoreCase("1117"))
		{
			npc.broadcastPacket(new SpecialCamera(npc.getObjectId(), 1700, 10, 0, 300, 250, 20, -20, 1, 1));
		}
		
		return super.onTimer(name, npc, player);
	}
	
	@Override
	public void onSeeCreature(Npc npc, Creature creature)
	{
		final Player player = creature.getActingPlayer();
		if (player == null)
			return;
		
		int i1 = 0;
		int i2 = 0;
		
		Creature c2 = null;
		
		if (npc.getSpawn().getSpawnData().getDBValue() == 3 && ClassId.isSameOccupation(player, "@cleric_group"))
		{
			final double hpRatio = npc.getStatus().getHpRatio();
			if (hpRatio < 0.25)
			{
				if (creature == npc._c_quest2)
				{
					if (((10 * 1000) + 1000) > npc._i_quest2)
						npc._i_quest2 = (10 * 1000) + Rnd.get(3000);
				}
				else if (creature == npc._c_quest3)
				{
					if (((10 * 1000) + 1000) > npc._i_quest3)
						npc._i_quest3 = (10 * 1000) + Rnd.get(3000);
				}
				else if (creature == npc._c_quest4)
				{
					if (((10 * 1000) + 1000) > npc._i_quest4)
						npc._i_quest4 = (10 * 1000) + Rnd.get(3000);
				}
				else if (npc._i_quest2 > npc._i_quest3)
					i1 = 3;
				else if (npc._i_quest2 == npc._i_quest3)
					i1 = (Rnd.nextBoolean()) ? 2 : 3;
				else if (npc._i_quest2 < npc._i_quest3)
					i1 = 2;
				
				if (i1 == 2)
				{
					if (npc._i_quest2 > npc._i_quest4)
						i1 = 4;
					else if (npc._i_quest2 == npc._i_quest4)
						i1 = (Rnd.nextBoolean()) ? 2 : 4;
					else if (npc._i_quest2 < npc._i_quest4)
						i1 = 2;
				}
				else if (i1 == 3)
				{
					if (npc._i_quest3 > npc._i_quest4)
						i1 = 4;
					else if (npc._i_quest3 == npc._i_quest4)
						i1 = (Rnd.nextBoolean()) ? 3 : 4;
					else if (npc._i_quest3 < npc._i_quest4)
						i1 = 3;
				}
				
				switch (i1)
				{
					case 2:
						npc._i_quest2 = (10 * 1000) + Rnd.get(3000);
						npc._c_quest2 = creature;
						break;
					
					case 3:
						npc._i_quest3 = (10 * 1000) + Rnd.get(3000);
						npc._c_quest3 = creature;
						break;
					
					case 4:
						npc._i_quest4 = (10 * 1000) + Rnd.get(3000);
						npc._c_quest4 = creature;
						break;
				}
			}
			else if (hpRatio < 0.5)
			{
				if (creature == npc._c_quest2)
				{
					if (((6 * 1000) + 1000) > npc._i_quest2)
						npc._i_quest2 = (6 * 1000) + Rnd.get(3000);
				}
				else if (creature == npc._c_quest3)
				{
					if (((6 * 1000) + 1000) > npc._i_quest3)
						npc._i_quest3 = (6 * 1000) + Rnd.get(3000);
				}
				else if (creature == npc._c_quest4)
				{
					if (((6 * 1000) + 1000) > npc._i_quest4)
						npc._i_quest4 = (6 * 1000) + Rnd.get(3000);
				}
				else if (npc._i_quest2 > npc._i_quest3)
					i1 = 3;
				else if (npc._i_quest2 == npc._i_quest3)
					i1 = (Rnd.nextBoolean()) ? 2 : 3;
				else if (npc._i_quest2 < npc._i_quest3)
					i1 = 2;
				
				if (i1 == 2)
				{
					if (npc._i_quest2 > npc._i_quest4)
						i1 = 4;
					else if (npc._i_quest2 == npc._i_quest4)
						i1 = (Rnd.nextBoolean()) ? 2 : 4;
					else if (npc._i_quest2 < npc._i_quest4)
						i1 = 2;
				}
				else if (i1 == 3)
				{
					if (npc._i_quest3 > npc._i_quest4)
						i1 = 4;
					else if (npc._i_quest3 == npc._i_quest4)
						i1 = (Rnd.nextBoolean()) ? 3 : 4;
					else if (npc._i_quest3 < npc._i_quest4)
						i1 = 3;
				}
				
				switch (i1)
				{
					case 2:
						npc._i_quest2 = (6 * 1000) + Rnd.get(3000);
						npc._c_quest2 = creature;
						break;
					
					case 3:
						npc._i_quest3 = (6 * 1000) + Rnd.get(3000);
						npc._c_quest3 = creature;
						break;
					
					case 4:
						npc._i_quest4 = (6 * 1000) + Rnd.get(3000);
						npc._c_quest4 = creature;
						break;
				}
			}
			else if (hpRatio < 0.75)
			{
				if (creature == npc._c_quest2)
				{
					if (((3 * 1000) + 1000) > npc._i_quest2)
						npc._i_quest2 = (3 * 1000) + Rnd.get(3000);
				}
				else if (creature == npc._c_quest3)
				{
					if (((3 * 1000) + 1000) > npc._i_quest3)
						npc._i_quest3 = (3 * 1000) + Rnd.get(3000);
				}
				else if (creature == npc._c_quest4)
				{
					if (((3 * 1000) + 1000) > npc._i_quest4)
						npc._i_quest4 = (3 * 1000) + Rnd.get(3000);
				}
				else if (npc._i_quest2 > npc._i_quest3)
					i1 = 3;
				else if (npc._i_quest2 == npc._i_quest3)
					i1 = (Rnd.nextBoolean()) ? 2 : 3;
				else if (npc._i_quest2 < npc._i_quest3)
					i1 = 2;
				
				if (i1 == 2)
				{
					if (npc._i_quest2 > npc._i_quest4)
						i1 = 4;
					else if (npc._i_quest2 == npc._i_quest4)
						i1 = (Rnd.nextBoolean()) ? 2 : 4;
					else if (npc._i_quest2 < npc._i_quest4)
						i1 = 2;
				}
				else if (i1 == 3)
				{
					if (npc._i_quest3 > npc._i_quest4)
						i1 = 4;
					else if (npc._i_quest3 == npc._i_quest4)
						i1 = (Rnd.nextBoolean()) ? 3 : 4;
					else if (npc._i_quest3 < npc._i_quest4)
						i1 = 3;
				}
				
				switch (i1)
				{
					case 2:
						npc._i_quest2 = (3 * 1000) + Rnd.get(3000);
						npc._c_quest2 = creature;
						break;
					
					case 3:
						npc._i_quest3 = (3 * 1000) + Rnd.get(3000);
						npc._c_quest3 = creature;
						break;
					
					case 4:
						npc._i_quest4 = (3 * 1000) + Rnd.get(3000);
						npc._c_quest4 = creature;
						break;
				}
			}
			else if (creature == npc._c_quest2)
			{
				if (((2 * 1000) + 1000) > npc._i_quest2)
					npc._i_quest2 = (2 * 1000) + Rnd.get(3000);
			}
			else if (creature == npc._c_quest3)
			{
				if (((2 * 1000) + 1000) > npc._i_quest3)
					npc._i_quest3 = (2 * 1000) + Rnd.get(3000);
			}
			else if (creature == npc._c_quest4)
			{
				if (((2 * 1000) + 1000) > npc._i_quest4)
					npc._i_quest4 = (2 * 1000) + Rnd.get(3000);
			}
			else if (npc._i_quest2 > npc._i_quest3)
				i1 = 3;
			else if (npc._i_quest2 == npc._i_quest3)
				i1 = (Rnd.nextBoolean()) ? 2 : 3;
			else if (npc._i_quest2 < npc._i_quest3)
				i1 = 2;
			
			if (i1 == 2)
			{
				if (npc._i_quest2 > npc._i_quest4)
					i1 = 4;
				else if (npc._i_quest2 == npc._i_quest4)
					i1 = (Rnd.nextBoolean()) ? 2 : 4;
				else if (npc._i_quest2 < npc._i_quest4)
					i1 = 2;
			}
			else if (i1 == 3)
			{
				if (npc._i_quest3 > npc._i_quest4)
					i1 = 4;
				else if (npc._i_quest3 == npc._i_quest4)
					i1 = (Rnd.nextBoolean()) ? 3 : 4;
				else if (npc._i_quest3 < npc._i_quest4)
					i1 = 3;
			}
			
			switch (i1)
			{
				case 2:
					npc._i_quest2 = (2 * 1000) + Rnd.get(3000);
					npc._c_quest2 = creature;
					break;
				
				case 3:
					npc._i_quest3 = (2 * 1000) + Rnd.get(3000);
					npc._c_quest3 = creature;
					break;
				
				case 4:
					npc._i_quest4 = (2 * 1000) + Rnd.get(3000);
					npc._c_quest4 = creature;
					break;
			}
		}
		else if (creature == npc._c_quest2)
		{
			if (((1 * 1000) + 1000) > npc._i_quest2)
				npc._i_quest2 = (1 * 1000) + Rnd.get(3000);
		}
		else if (creature == npc._c_quest3)
		{
			if (((1 * 1000) + 1000) > npc._i_quest3)
				npc._i_quest3 = (1 * 1000) + Rnd.get(3000);
		}
		else if (creature == npc._c_quest4)
		{
			if (((1 * 1000) + 1000) > npc._i_quest4)
				npc._i_quest4 = (1 * 1000) + Rnd.get(3000);
		}
		else if (npc._i_quest2 > npc._i_quest3)
			i1 = 3;
		else if (npc._i_quest2 == npc._i_quest3)
			i1 = (Rnd.nextBoolean()) ? 2 : 3;
		else if (npc._i_quest2 < npc._i_quest3)
			i1 = 2;
		
		if (i1 == 2)
		{
			if (npc._i_quest2 > npc._i_quest4)
				i1 = 4;
			else if (npc._i_quest2 == npc._i_quest4)
				i1 = (Rnd.nextBoolean()) ? 2 : 4;
			else if (npc._i_quest2 < npc._i_quest4)
				i1 = 2;
		}
		else if (i1 == 3)
		{
			if (npc._i_quest3 > npc._i_quest4)
				i1 = 4;
			else if (npc._i_quest3 == npc._i_quest4)
				i1 = (Rnd.nextBoolean()) ? 3 : 4;
			else if (npc._i_quest3 < npc._i_quest4)
				i1 = 3;
		}
		
		switch (i1)
		{
			case 2:
				npc._i_quest2 = (1 * 1000) + Rnd.get(3000);
				npc._c_quest2 = creature;
				break;
			
			case 3:
				npc._i_quest3 = (1 * 1000) + Rnd.get(3000);
				npc._c_quest3 = creature;
				break;
			
			case 4:
				npc._i_quest4 = (1 * 1000) + Rnd.get(3000);
				npc._c_quest4 = creature;
				break;
		}
		
		IntentionType currentIntentionType = npc.getAI().getCurrentIntention().getType();
		
		if (npc.getSpawn().getSpawnData().getDBValue() == 3 && (currentIntentionType == IntentionType.WANDER || currentIntentionType == IntentionType.IDLE))
		{
			if (npc._c_quest2 == null || npc.distance3D(npc._c_quest2) > 5000 || npc._c_quest2.isDead())
				npc._i_quest2 = 0;
			
			if (npc._c_quest3 == null || npc.distance3D(npc._c_quest3) > 5000 || npc._c_quest3.isDead())
				npc._i_quest3 = 0;
			
			if (npc._c_quest4 == null || npc.distance3D(npc._c_quest4) > 5000 || npc._c_quest4.isDead())
				npc._i_quest4 = 0;
			
			if (npc._i_quest2 > npc._i_quest3)
			{
				i1 = 2;
				i2 = npc._i_quest2;
				c2 = npc._c_quest2;
			}
			else
			{
				i1 = 3;
				i2 = npc._i_quest3;
				c2 = npc._c_quest3;
			}
			
			if (npc._i_quest4 > i2)
			{
				i1 = 4;
				i2 = npc._i_quest4;
				c2 = npc._c_quest4;
			}
			
			if (i2 > 0)
			{
				if (Rnd.get(100) < 70)
				{
					switch (i1)
					{
						case 2:
							npc._i_quest2 = 500;
							break;
						
						case 3:
							npc._i_quest3 = 500;
							break;
						
						case 4:
							npc._i_quest4 = 500;
							break;
					}
				}
				
				final double hpRatio = npc.getStatus().getHpRatio();
				if (hpRatio < 0.25)
					valakasCastSkills(npc, c2, 20, 15, 15, 10, 35, 20, 15);
				else if (hpRatio < 0.5)
					valakasCastSkills(npc, c2, 5, 10, 10, 10, 20, 5, 10);
				else if (hpRatio < 0.75)
					valakasCastSkills(npc, c2, 0, 5, 5, 10, 15, 0, 5);
				else
					valakasCastSkills(npc, c2, 0, 10, 5, 10, 15, 0, 10);
			}
		}
	}
	
	@Override
	public void onUseSkillFinished(Npc npc, Creature creature, L2Skill skill, boolean success)
	{
		int i1 = 0;
		int i2 = 0;
		
		Creature c2 = null;
		
		if (npc._c_quest2 == null || npc.distance3D(npc._c_quest2) > 5000 || npc._c_quest2.isDead())
			npc._i_quest2 = 0;
		
		if (npc._c_quest3 == null || npc.distance3D(npc._c_quest3) > 5000 || npc._c_quest3.isDead())
			npc._i_quest3 = 0;
		
		if (npc._c_quest4 == null || npc.distance3D(npc._c_quest4) > 5000 || npc._c_quest4.isDead())
			npc._i_quest4 = 0;
		
		if (npc._i_quest2 > npc._i_quest3)
		{
			i1 = 2;
			i2 = npc._i_quest2;
			c2 = npc._c_quest2;
		}
		else
		{
			i1 = 3;
			i2 = npc._i_quest3;
			c2 = npc._c_quest3;
		}
		
		if (npc._i_quest4 > i2)
		{
			i1 = 4;
			i2 = npc._i_quest4;
			c2 = npc._c_quest4;
		}
		
		if (i2 > 0)
		{
			if (Rnd.get(100) < 70)
			{
				switch (i1)
				{
					case 2:
						npc._i_quest2 = 500;
						break;
					
					case 3:
						npc._i_quest3 = 500;
						break;
					
					case 4:
						npc._i_quest4 = 500;
						break;
				}
			}
			
			final double hpRatio = npc.getStatus().getHpRatio();
			if (hpRatio < 0.25)
				valakasCastSkills(npc, c2, 20, 15, 15, 10, 35, 20, 15);
			else if (hpRatio < 0.5)
				valakasCastSkills(npc, c2, 5, 10, 10, 10, 20, 5, 10);
			else if (hpRatio < 0.75)
				valakasCastSkills(npc, c2, 0, 5, 5, 10, 15, 0, 5);
			else
				valakasCastSkills(npc, c2, 0, 10, 5, 10, 15, 0, 10);
		}
	}
	
	@Override
	public void onMyDying(Npc npc, Creature killer)
	{
		GlobalMemo.getInstance().remove(String.valueOf(GM_ID));
		
		npc.getSpawn().getSpawnData().setDBValue(0);
		npc.broadcastPacket(new PlaySound(1, "B03_D", npc));
		npc.broadcastPacket(new SpecialCamera(npc.getObjectId(), 2000, 130, -1, 0, 10000, 0, 0, 1, 1));
		
		startQuestTimer("1111", npc, null, 500);
	}
	
	private static void valakasCastSkills(Npc npc, Creature target, int chance1, int chance2, int chance3, int chance4, int chance5, int chance6, int chance7)
	{
		if (npc == null || target == null)
			return;
		
		final double targetDirection = MathUtil.calculateAngleFrom(npc, target);
		final double distanceBetween = npc.distance3D(target);
		
		int i0 = 0;
		if ((distanceBetween < 1423 && targetDirection < (180 + 8) && targetDirection > (180 - 8)) || (distanceBetween < 802 && targetDirection < (180 + 14) && targetDirection > (180 - 14)))
			i0 = 1;
		
		int i1 = 0;
		if ((distanceBetween < 1423 && targetDirection < (180 + 8) && targetDirection > (180 - 8)) || (distanceBetween < 802 && targetDirection < (180 + 14) && targetDirection > (180 - 14)))
			i1 = 1;
		
		if (target.getZ() < (npc.getZ() + 200))
		{
			if (Rnd.get(100) < chance1)
				npc.getAI().addCastDesire(npc, METEOR, 1000000);
			else if (Rnd.get(100) < chance2)
				npc.getAI().addCastDesire(target, FEAR, 1000000);
			else if (Rnd.get(100) < chance3 && i0 == 1 && npc._i_quest0 == 1)
				npc.getAI().addCastDesire(target, REAR_STRIKE, 1000000);
			else if (Rnd.get(100) < chance4 && i1 == 1)
				npc.getAI().addCastDesire(target, REAR_THROW, 1000000);
			else if (Rnd.get(100) < chance5)
				npc.getAI().addCastDesire(target, BREATH_LOW, 1000000);
			else if (targetDirection > 0 && targetDirection <= 180)
				npc.getAI().addCastDesire(target, NORMAL_ATTACK_RIGHT, 1000000);
			else
				npc.getAI().addCastDesire(target, NORMAL_ATTACK_LEFT, 1000000);
		}
		else if (Rnd.get(100) < chance6)
			npc.getAI().addCastDesire(npc, METEOR, 1000000);
		else if (Rnd.get(100) < chance7)
			npc.getAI().addCastDesire(target, FEAR, 1000000);
		else
			npc.getAI().addCastDesire(target, BREATH_HIGH, 1000000);
	}
}