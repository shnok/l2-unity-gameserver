package com.shnok.javaserver.gameserver.scripting.script.ai.boss.frintezza;

import net.sf.l2j.commons.random.Rnd;

import net.sf.l2j.gameserver.data.SkillTable;
import net.sf.l2j.gameserver.data.manager.SpawnManager;
import net.sf.l2j.gameserver.enums.IntentionType;
import net.sf.l2j.gameserver.model.actor.Creature;
import net.sf.l2j.gameserver.model.actor.Npc;
import net.sf.l2j.gameserver.model.actor.Player;
import net.sf.l2j.gameserver.model.location.Location;
import net.sf.l2j.gameserver.model.memo.GlobalMemo;
import net.sf.l2j.gameserver.model.spawn.NpcMaker;
import net.sf.l2j.gameserver.network.serverpackets.SpecialCamera;
import net.sf.l2j.gameserver.scripting.script.ai.individual.DefaultNpc;
import net.sf.l2j.gameserver.skills.L2Skill;
import net.sf.l2j.gameserver.taskmanager.GameTimeTaskManager;

public class FollowerOfFrintezza extends DefaultNpc
{
	private static final L2Skill SWING_VER_1 = SkillTable.getInstance().getInfo(5014, 1);
	private static final L2Skill SWING_VER_2 = SkillTable.getInstance().getInfo(5014, 2);
	private static final L2Skill DASH_VER_2 = SkillTable.getInstance().getInfo(5015, 2);
	// private static final L2Skill DASH_ALL_VER1 = SkillTable.getInstance().getInfo(5015, 3);
	private static final L2Skill DASH_ALL_VER2 = SkillTable.getInstance().getInfo(5015, 4);
	private static final L2Skill ANTI_GRAVITY = SkillTable.getInstance().getInfo(5016, 1);
	private static final L2Skill MAGIC_CIRCLE = SkillTable.getInstance().getInfo(5018, 1);
	private static final L2Skill CHANGE_BODY_SKILL = SkillTable.getInstance().getInfo(5017, 1);
	
	private static final int CHANGE_WEAPON = 7903;
	private static final int CHANGE_WEAPON_START = 8204;
	private static final int GM_ID = 5;
	
	public FollowerOfFrintezza()
	{
		super("ai/boss/frintezza");
	}
	
	public FollowerOfFrintezza(String descr)
	{
		super(descr);
	}
	
	protected final int[] _npcIds =
	{
		29046 // follower_of_frintessa (Scarlet van Halisha)
	};
	
	@Override
	public void onCreated(Npc npc)
	{
		Npc c0 = (Npc) GlobalMemo.getInstance().getCreature(String.valueOf(GM_ID));
		if (c0 == null)
			GlobalMemo.getInstance().set(String.valueOf(GM_ID), npc.getObjectId());
		
		if (!npc.getSpawn().getDBLoaded())
		{
			npc.getSpawn().getSpawnData().setDBValue(0);
			
			Npc c1 = (Npc) GlobalMemo.getInstance().getCreature("4");
			if (c1 != null)
				c1.sendScriptEvent(npc.getObjectId(), 0, 0);
			
			c1 = (Npc) GlobalMemo.getInstance().getCreature("6");
			if (c1 != null)
				c1.sendScriptEvent(npc.getObjectId(), 0, 0);
		}
		else if (npc.getSpawn().getSpawnData().getDBValue() == 5)
		{
			NpcMaker nm = SpawnManager.getInstance().getNpcMaker("frintessa_evilate_maker1");
			if (nm != null)
				nm.getMaker().onMakerScriptEvent("1001", nm, 0, 0);
			
			nm = SpawnManager.getInstance().getNpcMaker("frintessa_evilate_maker2");
			if (nm != null)
				nm.getMaker().onMakerScriptEvent("1001", nm, 0, 0);
			
			startQuestTimer("3000", npc, null, 1000);
		}
		else if (npc.getSpawn().getSpawnData().getDBValue() == 6)
		{
			NpcMaker nm = SpawnManager.getInstance().getNpcMaker("frintessa_evilate_maker1");
			if (nm != null)
				nm.getMaker().onMakerScriptEvent("1001", nm, 0, 0);
			
			nm = SpawnManager.getInstance().getNpcMaker("frintessa_evilate_maker2");
			if (nm != null)
				nm.getMaker().onMakerScriptEvent("1001", nm, 0, 0);
			
			npc.removeAllDesire();
			
			npc._i_ai4 = 1;
			npc._i_quest2 = 0;
			npc._i_quest3 = 0;
			npc._i_quest4 = 0;
			
			npc.getAI().addCastDesire(npc, CHANGE_BODY_SKILL, 1000000);
			
			npc.getSpawn().getSpawnData().setDBValue(6);
			
			c0 = (Npc) GlobalMemo.getInstance().getCreature("4");
			if (c0 != null)
				c0.sendScriptEvent(npc.getObjectId(), 6, 0);
			
			npc._i_ai3 = 6;
			
			startQuestTimer("4000", npc, null, 2000);
			
			final double hpRatio = npc.getStatus().getHpRatio();
			if (hpRatio >= 0.5)
				npc._i_ai3 = 6;
			else if (hpRatio > 0.2)
				npc._i_ai3 = 30010;
			else
				npc._i_ai3 = 30011;
		}
		
		npc.setWalkOrRun(true);
		
		npc._i_ai0 = 0;
		npc._i_ai1 = 0;
		npc._i_ai3 = 5;
		npc._i_ai4 = 0;
		npc._i_ai2 = 0;
		npc._i_quest0 = 0;
		npc._i_quest1 = GameTimeTaskManager.getInstance().getCurrentTick();
		
		if (npc.getSpawn().getSpawnData().getDBValue() == 5 || npc.getSpawn().getSpawnData().getDBValue() == 6)
		{
			npc.getAI().addWanderDesire(5, 5);
			
			startQuestTimer("3000", npc, null, 60000);
		}
		
		startQuestTimer("3001", npc, null, 60000);
		
		super.onCreated(npc);
	}
	
	@Override
	public void onAttacked(Npc npc, Creature attacker, int damage, L2Skill skill)
	{
		if (npc._i_ai4 == 1)
			return;
		
		final L2Skill hinderStrider = SkillTable.getInstance().getInfo(4258, 1);
		if (attacker.isRiding() && getAbnormalLevel(attacker, hinderStrider) <= 0)
			npc.getAI().addCastDesire(attacker, hinderStrider, 1000000);
		
		npc._i_quest1 = GameTimeTaskManager.getInstance().getCurrentTick();
		
		final double hpRatio = npc.getStatus().getHpRatio();
		
		if (npc.getSpawn().getSpawnData().getDBValue() == 5 && hpRatio < 0.6)
		{
			npc.removeAllDesire();
			
			npc._i_ai4 = 1;
			npc._i_quest2 = 0;
			npc._i_quest3 = 0;
			npc._i_quest4 = 0;
			
			npc.getAI().addCastDesire(npc, CHANGE_BODY_SKILL, 1000000);
			
			npc.getSpawn().getSpawnData().setDBValue(6);
			
			final Npc c0 = (Npc) GlobalMemo.getInstance().getCreature("4");
			if (c0 != null)
				c0.sendScriptEvent(npc.getObjectId(), 6, 0);
			
			npc._i_ai3 = 6;
			
			startQuestTimer("4000", npc, null, 2000);
		}
		else if (npc.getSpawn().getSpawnData().getDBValue() == 6 && hpRatio < 0.5 && npc._i_ai3 == 6)
		{
			final Npc c0 = (Npc) GlobalMemo.getInstance().getCreature("4");
			if (c0 != null)
				c0.sendScriptEvent(npc.getObjectId(), 30010, 0);
			
			npc._i_ai3 = 30010;
		}
		else if (npc.getSpawn().getSpawnData().getDBValue() == 6 && hpRatio <= 0.2 && npc._i_ai3 == 30010 && npc._i_ai4 == 0)
		{
			npc.getSpawn().getSpawnData().setDBValue(50000);
			npc._i_ai3 = 30011;
			
			broadcastScriptEvent(npc, 0, 50000, 6000);
			
			npc.removeAllDesire();
			npc._i_ai4 = 1;
			
			startQuestTimer("2000", npc, null, 10000);
		}
		
		if (npc.getMove().getGeoPathFailCount() > 10 && npc.getAI().getTopDesireTarget() != null && attacker == npc.getAI().getTopDesireTarget() && hpRatio < 1.0)
			npc.teleportTo(npc.getAI().getTopDesireTarget().getPosition(), 0);
		
		int i1 = 0;
		
		if (attacker == npc._c_quest2)
		{
			if (npc._i_quest2 < (damage * 1000) + 1000)
				npc._i_quest2 = ((damage * 1000) + Rnd.get(3000));
		}
		else if (attacker == npc._c_quest3)
		{
			if (npc._i_quest3 < (damage * 1000) + 1000)
				npc._i_quest3 = (damage * 1000) + Rnd.get(3000);
		}
		else if (attacker == npc._c_quest4)
		{
			if (npc._i_quest4 < (damage * 1000) + 1000)
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
		
		if (skill != null && skill.getId() == 5008 && skill.getLevel() == 2)
		{
			NpcMaker maker0 = SpawnManager.getInstance().getNpcMaker("frintessa_evilate_maker1");
			if (maker0 != null)
				maker0.getMaker().onMakerScriptEvent("1001", maker0, 0, 0);
			
			maker0 = SpawnManager.getInstance().getNpcMaker("frintessa_evilate_maker2");
			if (maker0 != null)
				maker0.getMaker().onMakerScriptEvent("1001", maker0, 0, 0);
			
			if (npc.getSpawn().getSpawnData().getDBValue() == 6)
				npc._i_ai2 = 1;
		}
		
		final IntentionType currentIntentionType = npc.getAI().getCurrentIntention().getType();
		
		if ((npc.getSpawn().getSpawnData().getDBValue() == 5 || npc.getSpawn().getSpawnData().getDBValue() == 6) && (currentIntentionType == IntentionType.IDLE || currentIntentionType == IntentionType.WANDER))
			doAttack(npc);
		
		super.onAttacked(npc, attacker, damage, skill);
	}
	
	// TODO Desire Manipulation
	// EventHandler DESIRE_MANIPULATION(speller, desire)
	// {
	// myself::MakeAttackEvent(speller, (desire * 2), 0);
	// }
	
	@Override
	public void onSpelled(Npc npc, Player caster, L2Skill skill)
	{
		if (skill.getId() == 5008 && skill.getLevel() == 2)
		{
			NpcMaker maker0 = SpawnManager.getInstance().getNpcMaker("frintessa_evilate_maker1");
			if (maker0 != null)
				maker0.getMaker().onMakerScriptEvent("1001", maker0, 0, 0);
			
			maker0 = SpawnManager.getInstance().getNpcMaker("frintessa_evilate_maker2");
			if (maker0 != null)
				maker0.getMaker().onMakerScriptEvent("1001", maker0, 0, 0);
			
			if (npc.getSpawn().getSpawnData().getDBValue() == 6)
			{
				npc._i_ai2 = 1;
				
				doAttack(npc);
			}
		}
	}
	
	@Override
	public void onScriptEvent(Npc npc, int eventId, int arg1, int arg2)
	{
		if (arg1 == 4)
		{
			npc.getSpawn().getSpawnData().setDBValue(4);
			
			npc._i_quest1 = GameTimeTaskManager.getInstance().getCurrentTick();
			
			createOnePrivateEx(npc, 29053, 174230, -88012, -5112, 16384, 0, true);
			
			startQuestTimer("1001", npc, null, 5500);
		}
		else if (arg1 == 0)
		{
			npc.getSpawn().getSpawnData().setDBValue(0);
			npc.removeAllDesire();
			
			npc._i_quest2 = 0;
			npc._i_quest3 = 0;
			npc._i_quest4 = 0;
			
			npc.getPosition().setHeading(16384);
			npc.teleportTo(-105200, -253104, -15264, 0);
		}
		super.onScriptEvent(npc, eventId, arg1, arg2);
	}
	
	@Override
	public String onTimer(String name, Npc npc, Player player)
	{
		if (name.equalsIgnoreCase("1001"))
		{
			startQuestTimer("1002", npc, null, 500);
		}
		else if (name.equalsIgnoreCase("1002"))
		{
			npc.getPosition().setHeading(16384);
			npc.teleportTo(174230, -88012, -5112, 0);
			
			npc._i_quest1 = GameTimeTaskManager.getInstance().getCurrentTick();
			
			startQuestTimer("1003", npc, null, 8000);
		}
		else if (name.equalsIgnoreCase("1003"))
		{
			startQuestTimer("1900", npc, null, 10000);
		}
		else if (name.equalsIgnoreCase("1900"))
		{
			npc.getSpawn().getSpawnData().setDBValue(5);
			
			npc._i_ai4 = 0;
			
			npc.lookNeighbor();
			doAttack(npc);
			
			startQuestTimer("3000", npc, null, (60 * 1000));
		}
		else if (name.equalsIgnoreCase("2000"))
		{
			startQuestTimer("2002", npc, null, 2000);
		}
		else if (name.equalsIgnoreCase("2002"))
		{
			npc.getAI().addMoveToDesire(new Location(npc.getX(), npc.getY() + 50, npc.getZ()), 10000000);
			
			startQuestTimer("2003", npc, null, 100);
		}
		else if (name.equalsIgnoreCase("2003"))
		{
			npc.getAI().addMoveToDesire(new Location(npc.getX(), npc.getY() + 50, npc.getZ()), 10000000);
			
			startQuestTimer("2004", npc, null, 100);
		}
		else if (name.equalsIgnoreCase("2004"))
		{
			npc.getAI().addSocialDesire(2, 84000, 10000000);
			
			startQuestTimer("2005", npc, null, 7000);
		}
		else if (name.equalsIgnoreCase("2005"))
		{
			npc.broadcastPacket(new SpecialCamera(npc.getObjectId(), 250, 180, 20, 0, 10000, 0, 6, 1, 1));
			
			startQuestTimer("2006", npc, null, 100);
		}
		else if (name.equalsIgnoreCase("2006"))
		{
			npc.broadcastPacket(new SpecialCamera(npc.getObjectId(), 250, 180, 20, 0, 10000, 0, 6, 1, 1));
			
			startQuestTimer("2100", npc, null, 400);
		}
		else if (name.equalsIgnoreCase("2100"))
		{
			final Npc c4 = (Npc) GlobalMemo.getInstance().getCreature("6");
			if (c4 != null)
			{
				c4.getPosition().setHeading(16384);
				c4.teleportTo(npc.getX(), npc.getY(), npc.getZ(), 0);
				c4.sendScriptEvent(7, 0, 0);
			}
			startQuestTimer("2101", npc, null, 1000);
		}
		else if (name.equalsIgnoreCase("2101"))
		{
			npc.getAI().addSocialDesire(3, 50000, 10000000);
			
			startQuestTimer("2102", npc, null, 100);
		}
		else if (name.equalsIgnoreCase("2102"))
		{
			npc.broadcastPacket(new SpecialCamera(npc.getObjectId(), 250, 180, 20, 0, 10000, 0, 6, 1, 1));
			
			startQuestTimer("2103", npc, null, 100);
		}
		else if (name.equalsIgnoreCase("2103"))
		{
			npc.broadcastPacket(new SpecialCamera(npc.getObjectId(), 250, 180, 20, 0, 10000, 0, 6, 1, 1));
			
			startQuestTimer("2104", npc, null, 400);
		}
		else if (name.equalsIgnoreCase("2104"))
		{
			npc.broadcastPacket(new SpecialCamera(npc.getObjectId(), 300, 220, 0, 3000, 10000, 0, 0, 1, 1));
			
			startQuestTimer("2105", npc, null, 3000);
		}
		else if (name.equalsIgnoreCase("2105"))
		{
			npc.broadcastPacket(new SpecialCamera(npc.getObjectId(), 100, 150, -3, 0, 10000, 0, 0, 1, 1));
			
			startQuestTimer("2106", npc, null, 100);
		}
		else if (name.equalsIgnoreCase("2106"))
		{
			npc.broadcastPacket(new SpecialCamera(npc.getObjectId(), 100, 150, -3, 0, 10000, 0, 0, 1, 1));
			
			startQuestTimer("2107", npc, null, 400);
		}
		else if (name.equalsIgnoreCase("2107"))
		{
			npc.broadcastPacket(new SpecialCamera(npc.getObjectId(), 100, 150, 10, 3000, 5000, 0, 15, 1, 1));
			
			startQuestTimer("2108", npc, null, 4000);
		}
		else if (name.equalsIgnoreCase("2108"))
		{
			npc.broadcastPacket(new SpecialCamera(npc.getObjectId(), 300, 180, 10, 2000, 2000, 0, 5, 1, 1));
			
			startQuestTimer("2109", npc, null, 2000);
		}
		else if (name.equalsIgnoreCase("2109"))
		{
			GlobalMemo.getInstance().remove(String.valueOf(GM_ID));
			
			npc.getSpawn().getSpawnData().setDBValue(0);
			npc.deleteMe();
		}
		else if (name.equalsIgnoreCase("3000"))
		{
			if (npc.getSpawn().getSpawnData().getDBValue() == 5 || npc.getSpawn().getSpawnData().getDBValue() == 6)
			{
				if (getElapsedTicks(npc._i_quest1) > (15 * 60))
				{
					npc.getSpawn().getSpawnData().setDBValue(0);
					
					Npc c0 = (Npc) GlobalMemo.getInstance().getCreature("4");
					if (c0 != null)
						c0.sendScriptEvent(npc.getObjectId(), 0, 0);
					
					c0 = (Npc) GlobalMemo.getInstance().getCreature("6");
					if (c0 != null)
						c0.sendScriptEvent(npc.getObjectId(), 0, 0);
					
					broadcastScriptEvent(npc, 0, 40000, 8000);
					
					npc.removeAllDesire();
					
					npc.getSpawn().instantTeleportInMyTerritory(150037, -57255, -2976, 150);
					npc.teleportTo(-105200, -253104, -15264, 0);
					npc.getPosition().setHeading(16384);
					
					npc.equipItem(CHANGE_WEAPON_START, 0);
					
					NpcMaker maker0 = SpawnManager.getInstance().getNpcMaker("frintessa_evilate_maker1");
					if (maker0 != null)
						maker0.getMaker().onMakerScriptEvent("1000", maker0, 0, 0);
					
					maker0 = SpawnManager.getInstance().getNpcMaker("frintessa_evilate_maker2");
					if (maker0 != null)
						maker0.getMaker().onMakerScriptEvent("1000", maker0, 0, 0);
					
					npc._i_quest2 = 0;
					npc._i_quest3 = 0;
					npc._i_quest4 = 0;
				}
				else
				{
					npc.lookNeighbor();
					
					startQuestTimer("3000", npc, null, 60000);
				}
			}
		}
		else if (name.equalsIgnoreCase("4000"))
		{
			npc.removeAllDesire();
			npc.equipItem(CHANGE_WEAPON, 0);
			
			startQuestTimer("4001", npc, null, 3000);
		}
		else if (name.equalsIgnoreCase("4001"))
		{
			npc._i_ai4 = 0;
			
			startQuestTimer("3000", npc, null, 1000);
		}
		else if (name.equalsIgnoreCase("3001"))
		{
			IntentionType currentIntentionType = npc.getAI().getCurrentIntention().getType();
			
			if ((npc.getSpawn().getSpawnData().getDBValue() == 5 || npc.getSpawn().getSpawnData().getDBValue() == 6) && (currentIntentionType == IntentionType.IDLE || currentIntentionType == IntentionType.WANDER))
				doAttack(npc);
			
			startQuestTimer("3001", npc, null, 60000);
		}
		else if (name.equalsIgnoreCase("8000"))
		{
			doAttack(npc);
		}
		return super.onTimer(name, npc, player);
	}
	
	@Override
	public void onSeeCreature(Npc npc, Creature creature)
	{
		if (npc._i_ai4 == 1)
			return;
		
		final int dbValue = npc.getSpawn().getSpawnData().getDBValue();
		
		if (creature.getActingPlayer() == null)
		{
			if (Rnd.get(100) < 1 && dbValue == 6)
			{
				((Npc) creature).sendScriptEvent(0, 20000, 0);
				
				npc.removeAllDesire();
				
				npc.getAI().addSocialDesire(4, 1500, 10000000);
			}
			return;
		}
		
		int i1 = 0;
		
		if (dbValue == 5 || dbValue == 6)
		{
			if (creature == npc._c_quest2)
			{
				if (npc._i_quest2 < ((1 * 1000) + 1000))
					npc._i_quest2 = (1 * 1000) + Rnd.get(3000);
			}
			else if (creature == npc._c_quest3)
			{
				if (npc._i_quest3 < ((1 * 1000) + 1000))
					npc._i_quest3 = (1 * 1000) + Rnd.get(3000);
			}
			else if (creature == npc._c_quest4)
			{
				if (npc._i_quest4 < ((1 * 1000) + 1000))
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
			
			doAttack(npc);
		}
	}
	
	@Override
	public void onUseSkillFinished(Npc npc, Creature creature, L2Skill skill, boolean success)
	{
		if (skill == DASH_ALL_VER2 && success && npc._i_ai2 == 3)
			npc._i_ai2 = 0;
		
		doAttack(npc);
	}
	
	@Override
	public void onMyDying(Npc npc, Creature killer)
	{
		final Npc c0 = (Npc) GlobalMemo.getInstance().getCreature("4");
		if (c0 != null)
			c0.sendScriptEvent(npc.getObjectId(), 30011, 0);
		
		GlobalMemo.getInstance().remove(String.valueOf(GM_ID));
		
		npc.getSpawn().getSpawnData().setDBValue(0);
		
		startQuestTimer("2000", npc, null, 500);
	}
	
	@Override
	public void onNoDesire(Npc npc)
	{
		final int dbValue = npc.getSpawn().getSpawnData().getDBValue();
		if ((dbValue == 5 || dbValue == 6) && npc._i_ai4 == 0)
			npc.lookNeighbor();
		else if (dbValue == 0)
			npc.removeAllDesire();
	}
	
	private static void doAttack(Npc npc)
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
			if (Rnd.get(100) < 5)
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
			
			if (c2 != null)
			{
				if (npc.getSpawn().getSpawnData().getDBValue() == 5)
				{
					if (Rnd.get(10000) < 2000)
					{
						// TODO: Zones
						// if( npc. IsInThisTerritory("25_15_frintessa_NoCharge01") == 1 ) {
						// if( c2 != null ) {
						// npc.getAI().addCastDesire(c2,DashAllVer1,10000);
						// }
						// }
					}
					else if (Rnd.get(10000) < 500)
						npc.getAI().addCastDesire(c2, ANTI_GRAVITY, 10000, false);
					else
						npc.getAI().addCastDesire(c2, SWING_VER_1, 10000, false);
				}
				else if (npc.getSpawn().getSpawnData().getDBValue() == 6)
				{
					if (npc._i_ai2 > 0)
					{
						if (npc._i_ai2 > 0 && npc._i_ai2 < 3)
						{
							npc.getAI().addCastDesire(c2, DASH_VER_2, 10000, false);
							
							npc._i_ai2 = (npc._i_ai2 + 1);
						}
						else if (npc._i_ai2 == 3)
						{
							npc.getAI().addCastDesire(c2, DASH_ALL_VER2, 10000, false);
							
							npc._i_ai2 = 0;
						}
						else
							npc._i_ai2 = 0;
					}
					else if (npc.getStatus().getHpRatio() > 0.5)
					{
						if (Rnd.get(10000) < 2000)
						{
							// TODO: Zones
							// if( IsInThisTerritory("25_15_frintessa_NoCharge01") == 1 ) {
							// if( c2 != null ) {
							// npc.getAI().addCastDesire(c2,DASH_ALL_VER2,10000);
							// }
							// }
						}
						else if (Rnd.get(10000) < 500)
						{
							npc._i_ai2 = 1;
							
							if (npc._i_ai2 > 0 && npc._i_ai2 < 3)
							{
								npc.getAI().addCastDesire(c2, DASH_VER_2, 10000, false);
								
								npc._i_ai2 = (npc._i_ai2 + 1);
							}
							else if (npc._i_ai2 == 3)
							{
								npc.getAI().addCastDesire(c2, DASH_ALL_VER2, 10000, false);
								
								npc._i_ai2 = 0;
							}
							else
								npc._i_ai2 = 0;
						}
						else if (Rnd.get(10000) < 500)
							npc.getAI().addCastDesire(c2, ANTI_GRAVITY, 10000, false);
						else if (Rnd.get(10000) < 500)
							npc.getAI().addCastDesire(c2, MAGIC_CIRCLE, 10000, false);
						else
							npc.getAI().addCastDesire(c2, SWING_VER_2, 10000, false);
					}
					else if (Rnd.get(10000) < 2000)
					{
						// TODO: Zones
						// if( IsInThisTerritory("25_15_frintessa_NoCharge01") == 1 ) {
						// if( c2 != null ) {
						// npc.getAI().addCastDesire(c2,DASH_ALL_VER2,10000);
						// }
						// }
					}
					else if (Rnd.get(10000) < 1500)
					{
						npc._i_ai2 = 1;
						
						if (npc._i_ai2 > 0 && npc._i_ai2 < 3)
						{
							npc.getAI().addCastDesire(c2, DASH_VER_2, 10000, false);
							
							npc._i_ai2 = (npc._i_ai2 + 1);
						}
						else if (npc._i_ai2 == 3)
						{
							npc.getAI().addCastDesire(c2, DASH_ALL_VER2, 10000, false);
							
							npc._i_ai2 = 0;
						}
						else
							npc._i_ai2 = 0;
					}
					else if (Rnd.get(10000) < 1500)
						npc.getAI().addCastDesire(c2, ANTI_GRAVITY, 10000, false);
					else if (Rnd.get(10000) < 1000)
						npc.getAI().addCastDesire(c2, MAGIC_CIRCLE, 10000, false);
					else
						npc.getAI().addCastDesire(c2, SWING_VER_2, 10000, false);
				}
			}
		}
	}
}