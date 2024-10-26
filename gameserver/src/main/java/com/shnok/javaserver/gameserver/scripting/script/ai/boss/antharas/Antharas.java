package com.shnok.javaserver.gameserver.scripting.script.ai.boss.antharas;

import net.sf.l2j.commons.math.MathUtil;
import net.sf.l2j.commons.random.Rnd;

import net.sf.l2j.gameserver.data.SkillTable;
import net.sf.l2j.gameserver.enums.IntentionType;
import net.sf.l2j.gameserver.enums.actors.ClassId;
import net.sf.l2j.gameserver.model.actor.Creature;
import net.sf.l2j.gameserver.model.actor.Npc;
import net.sf.l2j.gameserver.model.actor.Player;
import net.sf.l2j.gameserver.model.location.Location;
import net.sf.l2j.gameserver.model.memo.GlobalMemo;
import net.sf.l2j.gameserver.network.NpcStringId;
import net.sf.l2j.gameserver.network.serverpackets.Earthquake;
import net.sf.l2j.gameserver.network.serverpackets.PlaySound;
import net.sf.l2j.gameserver.network.serverpackets.SpecialCamera;
import net.sf.l2j.gameserver.scripting.script.ai.individual.DefaultNpc;
import net.sf.l2j.gameserver.skills.L2Skill;
import net.sf.l2j.gameserver.taskmanager.GameTimeTaskManager;

public class Antharas extends DefaultNpc
{
	public Antharas()
	{
		super("ai/boss/antharas");
	}
	
	public Antharas(String descr)
	{
		super(descr);
	}
	
	protected final int[] _npcIds =
	{
		29066, // antharas1 (antharas_min)
		29067, // antharas2 (antharas_normal)
		29068 // antharas3 (antharas_max)
	};
	
	@Override
	public void onCreated(Npc npc)
	{
		npc._i_ai0 = 0;
		
		startQuestTimerAtFixedRate("2001", npc, null, 2 * 60 * 1000L, 2 * 60 * 1000L);
		
		if (!npc.getSpawn().getDBLoaded())
		{
			npc.getSpawn().getSpawnData().setDBValue(0);
			npc.broadcastPacket(new Earthquake(npc, 20, 10, true));
		}
		
		npc._i_ai1 = GameTimeTaskManager.getInstance().getCurrentTick();
		npc._i_ai2 = 0;
		npc._i_ai3 = 0;
		npc._i_ai4 = 0;
		
		final int GM_ID = getNpcIntAIParam(npc, "GM_ID");
		if (GM_ID != 0)
		{
			final int i0 = GlobalMemo.getInstance().getInteger(String.valueOf(GM_ID), -1);
			if (i0 == -1)
				GlobalMemo.getInstance().set(String.valueOf(GM_ID), npc.getObjectId());
		}
		
		final int dbValue = npc.getSpawn().getSpawnData().getDBValue();
		if (dbValue == 1)
			startQuestTimer("1001", npc, null, 10 * 60 * 1000L);
		else if (dbValue == 2)
		{
			npc.teleportTo(185452, 114835, -8221, 0);
			npc.getAI().addMoveToDesire(new Location(181911, 114835, -7678), 100000);
			npc.broadcastPacket(new PlaySound(1, "BS02_A", npc));
		}
		else if (dbValue == 3)
		{
			startQuestTimer("1002", npc, null, 60 * 1000L);
			
			npc._i_ai1 = GameTimeTaskManager.getInstance().getCurrentTick();
			npc.getAI().addWanderDesire(5, 5);
		}
	}
	
	@Override
	public void onNoDesire(Npc npc)
	{
		final int dbValue = npc.getSpawn().getSpawnData().getDBValue();
		if (dbValue == 3)
			npc.getAI().addWanderDesire(5, 5);
		else if (dbValue == 0)
		{
			npc.removeAllDesire();
		}
	}
	
	@Override
	public void onScriptEvent(Npc npc, int eventId, int arg1, int arg2)
	{
		if (npc.getSpawn().getSpawnData().getDBValue() == 0)
		{
			startQuestTimer("1001", npc, null, 10 * 1000L);
			
			npc.getSpawn().getSpawnData().setDBValue(1);
		}
	}
	
	@Override
	public String onTimer(String name, Npc npc, Player player)
	{
		if (name.equalsIgnoreCase("2001"))
		{
			if (npc._i_ai0 == 0 && npc.getStatus().getHpRatio() < 0.5)
			{
				createOnePrivateEx(npc, 29069, npc.getX(), npc.getY(), npc.getZ(), 0, 0, false, getNpcIntAIParam(npc, "GM_ID"), 0, 0);
				
				npc._i_ai0 = 1;
				
				startQuestTimer("2002", npc, player, 240 * 60 * 1000L);
			}
		}
		else if (name.equalsIgnoreCase("2002"))
		{
			npc._i_ai0 = 0;
		}
		else if (name.equalsIgnoreCase("1001"))
		{
			npc.getSpawn().getSpawnData().setDBValue(2);
			npc.teleportTo(185452, 114835, -8221, 0);
			npc.getAI().addMoveToDesire(new Location(181911, 114835, -7678), 100000);
			npc.broadcastPacket(new PlaySound(1, "BS02_A", npc));
		}
		else if (name.equalsIgnoreCase("1201"))
		{
			npc.broadcastPacket(new SpecialCamera(npc.getObjectId(), 700, 13, -19, 0, 20000, 0, 0, 0, 0));
			
			startQuestTimer("1202", npc, player, 3000);
		}
		else if (name.equalsIgnoreCase("1202"))
		{
			npc.broadcastPacket(new SpecialCamera(npc.getObjectId(), 700, 13, 0, 6000, 20000, 0, 0, 0, 0));
			
			startQuestTimer("1203", npc, player, 10000);
		}
		else if (name.equalsIgnoreCase("1203"))
		{
			npc.broadcastPacket(new SpecialCamera(npc.getObjectId(), 3700, 0, -3, 0, 10000, 0, 0, 0, 0));
			npc.getAI().addSocialDesire(1, 8000, 10000000);
			npc.getAI().addSocialDesire(2, 6000, 5000000);
			
			startQuestTimer("1204", npc, player, 200);
		}
		else if (name.equalsIgnoreCase("1204"))
		{
			npc.broadcastPacket(new SpecialCamera(npc.getObjectId(), 1100, 0, -3, 22000, 30000, 0, 0, 0, 0));
			
			startQuestTimer("1205", npc, player, 10800);
		}
		else if (name.equalsIgnoreCase("1205"))
		{
			npc.broadcastPacket(new SpecialCamera(npc.getObjectId(), 1100, 0, -3, 300, 7000, 0, 0, 0, 0));
			npc.getSpawn().getSpawnData().setDBValue(3);
			
			startQuestTimer("1002", npc, player, 60000);
			
			npc._i_ai1 = GameTimeTaskManager.getInstance().getCurrentTick();
			
			final Player heroPlayer = npc.getNeighborHero(4000);
			if (heroPlayer != null)
				npc.broadcastOnScreen(3000, NpcStringId.ID_1000520);
			
			npc.getAI().addCastDesire(npc, 4125, 1, 4000000);
			
			castAntharasSkill(npc);
			
			npc.getAI().addWanderDesire(5, 5);
		}
		else if (name.equalsIgnoreCase("1002"))
		{
			if (getElapsedTicks(npc._i_ai1) > (15 * 60))
			{
				npc.getSpawn().getSpawnData().setDBValue(0);
				npc.removeAllDesire();
				npc.getSpawn().instantTeleportInMyTerritory(80464, 152294, -3534, 100);
				npc.teleportTo(-105200, -253104, -15264, 0);
				
				npc._i_ai2 = 0;
				npc._i_ai3 = 0;
				npc._i_ai4 = 0;
				npc._i_quest0 = 0;
			}
			else
				startQuestTimer("1002", npc, player, 60000);
			
			final double hpRatio = npc.getStatus().getHpRatio();
			if (hpRatio < 0.25)
			{
				if (getAbnormalLevel(npc, 4241, 1) < 14)
					npc.getAI().addCastDesire(npc, 4241, 1, 4000000);
			}
			else if (hpRatio < 0.5)
			{
				if (getAbnormalLevel(npc, 4240, 1) < 13)
					npc.getAI().addCastDesire(npc, 4240, 1, 4000000);
			}
			else if (hpRatio < 0.75)
			{
				if (getAbnormalLevel(npc, 4239, 1) < 12)
					npc.getAI().addCastDesire(npc, 4239, 1, 4000000);
			}
			else if (getAbnormalLevel(npc, 4125, 1) < 11)
				npc.getAI().addCastDesire(npc, 4125, 1, 4000000);
			
			if (npc._i_ai2 > 10)
				npc._i_ai2 -= Rnd.get(9) + 1;
			
			if (npc._i_ai3 > 10)
				npc._i_ai3 -= Rnd.get(9) + 1;
			
			if (npc._i_ai4 > 10)
				npc._i_ai4 -= Rnd.get(9) + 1;
			
			IntentionType currentIntentionType = npc.getAI().getCurrentIntention().getType();
			
			if (npc.getSpawn().getSpawnData().getDBValue() == 3 && (currentIntentionType == IntentionType.IDLE || currentIntentionType == IntentionType.WANDER))
				castAntharasSkill(npc);
		}
		
		return super.onTimer(name, npc, player);
	}
	
	@Override
	public void onMoveToFinished(Npc npc, int x, int y, int z)
	{
		startQuestTimer("1201", npc, null, 2000);
	}
	
	@Override
	public void onAttacked(Npc npc, Creature attacker, int damage, L2Skill skill)
	{
		int i1 = 0;
		
		if (npc.getSpawn().getSpawnData().getDBValue() == 0 && !npc.isDead())
			npc.getSpawn().instantTeleportInMyTerritory(80464, 152294, -3534, 100);
		
		npc._i_ai1 = GameTimeTaskManager.getInstance().getCurrentTick();
		
		final L2Skill hinderStrider = SkillTable.getInstance().getInfo(4258, 1);
		if (attacker.isRiding() && getAbnormalLevel(attacker, hinderStrider) <= 0)
			npc.getAI().addCastDesire(attacker, hinderStrider, 1000000);
		
		if (skill == null)
		{
			if (npc._c_ai2 != null && attacker == npc._c_ai2)
			{
				if (((damage * 1000) + 1000) > npc._i_ai2)
					npc._i_ai2 = ((damage * 1000) + Rnd.get(3000));
			}
			else if (npc._c_ai3 != null && attacker == npc._c_ai3)
			{
				if (((damage * 1000) + 1000) > npc._i_ai3)
					npc._i_ai3 = ((damage * 1000) + Rnd.get(3000));
			}
			else if (npc._c_ai4 != null && attacker == npc._c_ai4)
			{
				if (((damage * 1000) + 1000) > npc._i_ai4)
					npc._i_ai4 = ((damage * 1000) + Rnd.get(3000));
			}
			else if (npc._i_ai2 > npc._i_ai3)
				i1 = 3;
			else if (npc._i_ai2 == npc._i_ai3)
				i1 = (Rnd.nextBoolean()) ? 2 : 3;
			else if (npc._i_ai2 < npc._i_ai3)
				i1 = 2;
			
			if (i1 == 2)
			{
				if (npc._i_ai2 > npc._i_ai4)
					i1 = 4;
				else if (npc._i_ai2 == npc._i_ai4)
					i1 = (Rnd.nextBoolean()) ? 2 : 4;
				else if (npc._i_ai2 < npc._i_ai4)
					i1 = 2;
			}
			else if (i1 == 3)
			{
				if (npc._i_ai3 > npc._i_ai4)
					i1 = 4;
				else if (npc._i_ai3 == npc._i_ai4)
					i1 = (Rnd.nextBoolean()) ? 3 : 4;
				else if (npc._i_ai3 < npc._i_ai4)
					i1 = 3;
			}
			
			switch (i1)
			{
				case 2:
					npc._i_ai2 = ((damage * 1000) + Rnd.get(3000));
					npc._c_ai2 = attacker;
					break;
				
				case 3:
					npc._i_ai3 = ((damage * 1000) + Rnd.get(3000));
					npc._c_ai3 = attacker;
					break;
				
				case 4:
					npc._i_ai4 = ((damage * 1000) + Rnd.get(3000));
					npc._c_ai4 = attacker;
					break;
			}
		}
		else if (npc.getStatus().getHp() < ((npc.getStatus().getMaxHp() * 1.0) / 4.0))
		{
			if (npc._c_ai2 != null && attacker == npc._c_ai2)
			{
				if ((((damage / 30.0) * 1000) + 1000) > npc._i_ai2)
					npc._i_ai2 = (int) ((damage / 30.0) * 1000) + Rnd.get(3000);
			}
			else if (npc._c_ai3 != null && attacker == npc._c_ai3)
			{
				if ((((damage / 30.0) * 1000) + 1000) > npc._i_ai3)
					npc._i_ai3 = (int) ((damage / 30.0) * 1000) + Rnd.get(3000);
			}
			else if (npc._c_ai4 != null && attacker == npc._c_ai4)
			{
				if ((((damage / 30.0) * 1000) + 1000) > npc._i_ai4)
					npc._i_ai4 = (int) ((damage / 30.0) * 1000) + Rnd.get(3000);
			}
			else if (npc._i_ai2 > npc._i_ai3)
				i1 = 3;
			else if (npc._i_ai2 == npc._i_ai3)
				i1 = (Rnd.nextBoolean()) ? 2 : 3;
			else if (npc._i_ai2 < npc._i_ai3)
				i1 = 2;
			
			if (i1 == 2)
			{
				if (npc._i_ai2 > npc._i_ai4)
					i1 = 4;
				else if (npc._i_ai2 == npc._i_ai4)
					i1 = (Rnd.nextBoolean()) ? 2 : 4;
				else if (npc._i_ai2 < npc._i_ai4)
					i1 = 2;
			}
			else if (i1 == 3)
			{
				if (npc._i_ai3 > npc._i_ai4)
					i1 = 4;
				else if (npc._i_ai3 == npc._i_ai4)
					i1 = (Rnd.nextBoolean()) ? 3 : 4;
				else if (npc._i_ai3 < npc._i_ai4)
					i1 = 3;
			}
			
			switch (i1)
			{
				case 2:
					npc._i_ai2 = (int) ((damage / 30.0) * 1000) + Rnd.get(3000);
					npc._c_ai2 = attacker;
					break;
				
				case 3:
					npc._i_ai3 = (int) ((damage / 30.0) * 1000) + Rnd.get(3000);
					npc._c_ai3 = attacker;
					break;
				
				case 4:
					npc._i_ai4 = (int) ((damage / 30.0) * 1000) + Rnd.get(3000);
					npc._c_ai4 = attacker;
					break;
			}
		}
		else if (npc.getStatus().getHp() < ((npc.getStatus().getMaxHp() * 2.0) / 4.0))
		{
			if (npc._c_ai2 != null && attacker == npc._c_ai2)
			{
				if ((((damage / 50.0) * 1000) + 1000) > npc._i_ai2)
					npc._i_ai2 = (int) ((damage / 50.0) * 1000) + Rnd.get(3000);
			}
			else if (npc._c_ai3 != null && attacker == npc._c_ai3)
			{
				if ((((damage / 50.0) * 1000) + 1000) > npc._i_ai3)
					npc._i_ai3 = (int) ((damage / 50.0) * 1000) + Rnd.get(3000);
			}
			else if (npc._c_ai4 != null && attacker == npc._c_ai4)
			{
				if ((((damage / 50.0) * 1000) + 1000) > npc._i_ai4)
					npc._i_ai4 = (int) ((damage / 50.0) * 1000) + Rnd.get(3000);
			}
			else if (npc._i_ai2 > npc._i_ai3)
				i1 = 3;
			else if (npc._i_ai2 == npc._i_ai3)
				i1 = (Rnd.nextBoolean()) ? 2 : 3;
			else if (npc._i_ai2 < npc._i_ai3)
				i1 = 2;
			
			if (i1 == 2)
			{
				if (npc._i_ai2 > npc._i_ai4)
					i1 = 4;
				else if (npc._i_ai2 == npc._i_ai4)
					i1 = (Rnd.nextBoolean()) ? 2 : 4;
				else if (npc._i_ai2 < npc._i_ai4)
					i1 = 2;
			}
			else if (i1 == 3)
			{
				if (npc._i_ai3 > npc._i_ai4)
					i1 = 4;
				else if (npc._i_ai3 == npc._i_ai4)
					i1 = (Rnd.nextBoolean()) ? 3 : 4;
				else if (npc._i_ai3 < npc._i_ai4)
					i1 = 3;
			}
			
			switch (i1)
			{
				case 2:
					npc._i_ai2 = (int) ((damage / 50.0) * 1000) + Rnd.get(3000);
					npc._c_ai2 = attacker;
					break;
				
				case 3:
					npc._i_ai3 = (int) ((damage / 50.0) * 1000) + Rnd.get(3000);
					npc._c_ai3 = attacker;
					break;
				
				case 4:
					npc._i_ai4 = (int) ((damage / 50.0) * 1000) + Rnd.get(3000);
					npc._c_ai4 = attacker;
					break;
			}
		}
		else if (npc.getStatus().getHp() < ((npc.getStatus().getMaxHp() * 3.0) / 4.0))
		{
			if (npc._c_ai2 != null && attacker == npc._c_ai2)
			{
				if ((((damage / 100.0) * 1000) + 1000) > npc._i_ai2)
					npc._i_ai2 = (int) ((damage / 100.0) * 1000) + Rnd.get(3000);
			}
			else if (npc._c_ai3 != null && attacker == npc._c_ai3)
			{
				if ((((damage / 100.0) * 1000) + 1000) > npc._i_ai3)
					npc._i_ai3 = (int) ((damage / 100.0) * 1000) + Rnd.get(3000);
			}
			else if (npc._c_ai4 != null && attacker == npc._c_ai4)
			{
				if ((((damage / 100.0) * 1000) + 1000) > npc._i_ai4)
					npc._i_ai4 = (int) ((damage / 100.0) * 1000) + Rnd.get(3000);
			}
			else if (npc._i_ai2 > npc._i_ai3)
				i1 = 3;
			else if (npc._i_ai2 == npc._i_ai3)
				i1 = (Rnd.nextBoolean()) ? 2 : 3;
			else if (npc._i_ai2 < npc._i_ai3)
				i1 = 2;
			
			if (i1 == 2)
			{
				if (npc._i_ai2 > npc._i_ai4)
					i1 = 4;
				else if (npc._i_ai2 == npc._i_ai4)
					i1 = (Rnd.nextBoolean()) ? 2 : 4;
				else if (npc._i_ai2 < npc._i_ai4)
					i1 = 2;
			}
			else if (i1 == 3)
			{
				if (npc._i_ai3 > npc._i_ai4)
					i1 = 4;
				else if (npc._i_ai3 == npc._i_ai4)
					i1 = (Rnd.nextBoolean()) ? 3 : 4;
				else if (npc._i_ai3 < npc._i_ai4)
					i1 = 3;
			}
			
			switch (i1)
			{
				case 2:
					npc._i_ai2 = (int) ((damage / 100.0) * 1000) + Rnd.get(3000);
					npc._c_ai2 = attacker;
					break;
				
				case 3:
					npc._i_ai3 = (int) ((damage / 100.0) * 1000) + Rnd.get(3000);
					npc._c_ai3 = attacker;
					break;
				
				case 4:
					npc._i_ai4 = (int) ((damage / 100.0) * 1000) + Rnd.get(3000);
					npc._c_ai4 = attacker;
					break;
			}
		}
		else if (npc._c_ai2 != null && attacker == npc._c_ai2)
		{
			if ((((damage / 150.0) * 1000) + 1000) > npc._i_ai2)
				npc._i_ai2 = (int) ((damage / 150.0) * 1000) + Rnd.get(3000);
		}
		else if (npc._c_ai3 != null && attacker == npc._c_ai3)
		{
			if ((((damage / 150.0) * 1000) + 1000) > npc._i_ai3)
				npc._i_ai3 = (int) ((damage / 150.0) * 1000) + Rnd.get(3000);
		}
		else if (npc._c_ai4 != null && attacker == npc._c_ai4)
		{
			if ((((damage / 150.0) * 1000) + 1000) > npc._i_ai4)
				npc._i_ai4 = (int) ((damage / 150.0) * 1000) + Rnd.get(3000);
		}
		else if (npc._i_ai2 > npc._i_ai3)
			i1 = 3;
		else if (npc._i_ai2 == npc._i_ai3)
			i1 = (Rnd.nextBoolean()) ? 2 : 3;
		else if (npc._i_ai2 < npc._i_ai3)
			i1 = 2;
		
		if (i1 == 2)
		{
			if (npc._i_ai2 > npc._i_ai4)
				i1 = 4;
			else if (npc._i_ai2 == npc._i_ai4)
				i1 = (Rnd.nextBoolean()) ? 2 : 4;
			else if (npc._i_ai2 < npc._i_ai4)
				i1 = 2;
		}
		else if (i1 == 3)
		{
			if (npc._i_ai3 > npc._i_ai4)
				i1 = 4;
			else if (npc._i_ai3 == npc._i_ai4)
				i1 = (Rnd.nextBoolean()) ? 3 : 4;
			else if (npc._i_ai3 < npc._i_ai4)
				i1 = 3;
		}
		
		switch (i1)
		{
			case 2:
				npc._i_ai2 = (int) ((damage / 150.0) * 1000) + Rnd.get(3000);
				npc._c_ai2 = attacker;
				break;
			
			case 3:
				npc._i_ai3 = (int) ((damage / 150.0) * 1000) + Rnd.get(3000);
				npc._c_ai3 = attacker;
				break;
			
			case 4:
				npc._i_ai4 = (int) ((damage / 150.0) * 1000) + Rnd.get(3000);
				npc._c_ai4 = attacker;
				break;
		}
		
		IntentionType currentIntention = npc.getAI().getCurrentIntention().getType();
		
		if (npc.getSpawn().getSpawnData().getDBValue() == 3 && (currentIntention == IntentionType.WANDER || currentIntention == IntentionType.IDLE))
			castAntharasSkill(npc);
	}
	
	@Override
	public void onSeeCreature(Npc npc, Creature creature)
	{
		int i1 = 0;
		
		if (creature instanceof Player playerCreature && npc.getSpawn().getSpawnData().getDBValue() == 3 && ClassId.isSameOccupation(playerCreature, "@cleric_group"))
		{
			final double hpRatio = npc.getStatus().getHpRatio();
			if (hpRatio < 0.25)
			{
				if (npc._c_ai2 != null && creature == npc._c_ai2)
				{
					if (((10 * 1000) + 1000) > npc._i_ai2)
						npc._i_ai2 = ((10 * 1000) + Rnd.get(3000));
				}
				else if (npc._c_ai3 != null && creature == npc._c_ai3)
				{
					if (((10 * 1000) + 1000) > npc._i_ai3)
						npc._i_ai3 = ((10 * 1000) + Rnd.get(3000));
				}
				else if (npc._c_ai4 != null && creature == npc._c_ai4)
				{
					if (((10 * 1000) + 1000) > npc._i_ai4)
						npc._i_ai4 = ((10 * 1000) + Rnd.get(3000));
				}
				else if (npc._i_ai2 > npc._i_ai3)
					i1 = 3;
				else if (npc._i_ai2 == npc._i_ai3)
					i1 = (Rnd.nextBoolean()) ? 2 : 3;
				else if (npc._i_ai2 < npc._i_ai3)
					i1 = 2;
				
				if (i1 == 2)
				{
					if (npc._i_ai2 > npc._i_ai4)
						i1 = 4;
					else if (npc._i_ai2 == npc._i_ai4)
						i1 = (Rnd.nextBoolean()) ? 2 : 4;
					else if (npc._i_ai2 < npc._i_ai4)
						i1 = 2;
				}
				else if (i1 == 3)
				{
					if (npc._i_ai3 > npc._i_ai4)
						i1 = 4;
					else if (npc._i_ai3 == npc._i_ai4)
						i1 = (Rnd.nextBoolean()) ? 3 : 4;
					else if (npc._i_ai3 < npc._i_ai4)
						i1 = 3;
				}
				
				switch (i1)
				{
					case 2:
						npc._i_ai2 = ((10 * 1000) + Rnd.get(3000));
						npc._c_ai2 = creature;
						break;
					
					case 3:
						npc._i_ai3 = ((10 * 1000) + Rnd.get(3000));
						npc._c_ai3 = creature;
						break;
					
					case 4:
						npc._i_ai4 = ((10 * 1000) + Rnd.get(3000));
						npc._c_ai4 = creature;
						break;
				}
			}
			else if (hpRatio < 0.5)
			{
				if (npc._c_ai2 != null && creature == npc._c_ai2)
				{
					if (((6 * 1000) + 1000) > npc._i_ai2)
						npc._i_ai2 = ((6 * 1000) + Rnd.get(3000));
				}
				else if (npc._c_ai3 != null && creature == npc._c_ai3)
				{
					if (((6 * 1000) + 1000) > npc._i_ai3)
						npc._i_ai3 = ((6 * 1000) + Rnd.get(3000));
				}
				else if (npc._c_ai4 != null && creature == npc._c_ai4)
				{
					if (((6 * 1000) + 1000) > npc._i_ai4)
						npc._i_ai4 = ((6 * 1000) + Rnd.get(3000));
				}
				else if (npc._i_ai2 > npc._i_ai3)
					i1 = 3;
				else if (npc._i_ai2 == npc._i_ai3)
					i1 = (Rnd.nextBoolean()) ? 2 : 3;
				else if (npc._i_ai2 < npc._i_ai3)
					i1 = 2;
				
				if (i1 == 2)
				{
					if (npc._i_ai2 > npc._i_ai4)
						i1 = 4;
					else if (npc._i_ai2 == npc._i_ai4)
						i1 = (Rnd.nextBoolean()) ? 2 : 4;
					else if (npc._i_ai2 < npc._i_ai4)
						i1 = 2;
				}
				else if (i1 == 3)
				{
					if (npc._i_ai3 > npc._i_ai4)
						i1 = 4;
					else if (npc._i_ai3 == npc._i_ai4)
						i1 = (Rnd.nextBoolean()) ? 3 : 4;
					else if (npc._i_ai3 < npc._i_ai4)
						i1 = 3;
				}
				
				switch (i1)
				{
					case 2:
						npc._i_ai2 = ((6 * 1000) + Rnd.get(3000));
						npc._c_ai2 = creature;
						break;
					
					case 3:
						npc._i_ai3 = ((6 * 1000) + Rnd.get(3000));
						npc._c_ai3 = creature;
						break;
					
					case 4:
						npc._i_ai4 = ((6 * 1000) + Rnd.get(3000));
						npc._c_ai4 = creature;
						break;
				}
			}
			else if (hpRatio < 0.75)
			{
				if (npc._c_ai2 != null && creature == npc._c_ai2)
				{
					if (((3 * 1000) + 1000) > npc._i_ai2)
						npc._i_ai2 = ((3 * 1000) + Rnd.get(3000));
				}
				else if (npc._c_ai3 != null && creature == npc._c_ai3)
				{
					if (((3 * 1000) + 1000) > npc._i_ai3)
						npc._i_ai3 = ((3 * 1000) + Rnd.get(3000));
				}
				else if (npc._c_ai4 != null && creature == npc._c_ai4)
				{
					if (((3 * 1000) + 1000) > npc._i_ai4)
						npc._i_ai4 = ((3 * 1000) + Rnd.get(3000));
				}
				else if (npc._i_ai2 > npc._i_ai3)
					i1 = 3;
				else if (npc._i_ai2 == npc._i_ai3)
					i1 = (Rnd.nextBoolean()) ? 2 : 3;
				else if (npc._i_ai2 < npc._i_ai3)
					i1 = 2;
				
				if (i1 == 2)
				{
					if (npc._i_ai2 > npc._i_ai4)
						i1 = 4;
					else if (npc._i_ai2 == npc._i_ai4)
						i1 = (Rnd.nextBoolean()) ? 2 : 4;
					else if (npc._i_ai2 < npc._i_ai4)
						i1 = 2;
				}
				else if (i1 == 3)
				{
					if (npc._i_ai3 > npc._i_ai4)
						i1 = 4;
					else if (npc._i_ai3 == npc._i_ai4)
						i1 = (Rnd.nextBoolean()) ? 3 : 4;
					else if (npc._i_ai3 < npc._i_ai4)
						i1 = 3;
				}
				
				switch (i1)
				{
					case 2:
						npc._i_ai2 = ((3 * 1000) + Rnd.get(3000));
						npc._c_ai2 = creature;
						break;
					
					case 3:
						npc._i_ai3 = ((3 * 1000) + Rnd.get(3000));
						npc._c_ai3 = creature;
						break;
					
					case 4:
						npc._i_ai4 = ((3 * 1000) + Rnd.get(3000));
						npc._c_ai4 = creature;
						break;
				}
			}
			else if (npc._c_ai2 != null && creature == npc._c_ai2)
			{
				if (((2 * 1000) + 1000) > npc._i_ai2)
					npc._i_ai2 = ((2 * 1000) + Rnd.get(3000));
			}
			else if (npc._c_ai3 != null && creature == npc._c_ai3)
			{
				if (((2 * 1000) + 1000) > npc._i_ai3)
					npc._i_ai3 = ((2 * 1000) + Rnd.get(3000));
			}
			else if (npc._c_ai4 != null && creature == npc._c_ai4)
			{
				if (((2 * 1000) + 1000) > npc._i_ai4)
					npc._i_ai4 = ((2 * 1000) + Rnd.get(3000));
			}
			else if (npc._i_ai2 > npc._i_ai3)
				i1 = 3;
			else if (npc._i_ai2 == npc._i_ai3)
				i1 = (Rnd.nextBoolean()) ? 2 : 3;
			else if (npc._i_ai2 < npc._i_ai3)
				i1 = 2;
			
			if (i1 == 2)
			{
				if (npc._i_ai2 > npc._i_ai4)
					i1 = 4;
				else if (npc._i_ai2 == npc._i_ai4)
					i1 = (Rnd.nextBoolean()) ? 2 : 4;
				else if (npc._i_ai2 < npc._i_ai4)
					i1 = 2;
			}
			else if (i1 == 3)
			{
				if (npc._i_ai3 > npc._i_ai4)
					i1 = 4;
				else if (npc._i_ai3 == npc._i_ai4)
					i1 = (Rnd.nextBoolean()) ? 3 : 4;
				else if (npc._i_ai3 < npc._i_ai4)
					i1 = 3;
			}
			
			switch (i1)
			{
				case 2:
					npc._i_ai2 = ((2 * 1000) + Rnd.get(3000));
					npc._c_ai2 = creature;
					break;
				
				case 3:
					npc._i_ai3 = ((2 * 1000) + Rnd.get(3000));
					npc._c_ai3 = creature;
					break;
				
				case 4:
					npc._i_ai4 = ((2 * 1000) + Rnd.get(3000));
					npc._c_ai4 = creature;
					break;
			}
		}
		else if (npc._c_ai2 != null && creature == npc._c_ai2)
		{
			if (((1 * 1000) + 1000) > npc._i_ai2)
				npc._i_ai2 = ((1 * 1000) + Rnd.get(3000));
		}
		else if (npc._c_ai3 != null && creature == npc._c_ai3)
		{
			if (((1 * 1000) + 1000) > npc._i_ai3)
				npc._i_ai3 = ((1 * 1000) + Rnd.get(3000));
		}
		else if (npc._c_ai4 != null && creature == npc._c_ai4)
		{
			if (((1 * 1000) + 1000) > npc._i_ai4)
				npc._i_ai4 = ((1 * 1000) + Rnd.get(3000));
		}
		else if (npc._i_ai2 > npc._i_ai3)
			i1 = 3;
		else if (npc._i_ai2 == npc._i_ai3)
			i1 = (Rnd.nextBoolean()) ? 2 : 3;
		else if (npc._i_ai2 < npc._i_ai3)
			i1 = 2;
		
		if (i1 == 2)
		{
			if (npc._i_ai2 > npc._i_ai4)
				i1 = 4;
			else if (npc._i_ai2 == npc._i_ai4)
				i1 = (Rnd.nextBoolean()) ? 2 : 4;
			else if (npc._i_ai2 < npc._i_ai4)
				i1 = 2;
		}
		else if (i1 == 3)
		{
			if (npc._i_ai3 > npc._i_ai4)
				i1 = 4;
			else if (npc._i_ai3 == npc._i_ai4)
				i1 = (Rnd.nextBoolean()) ? 3 : 4;
			else if (npc._i_ai3 < npc._i_ai4)
				i1 = 3;
		}
		
		switch (i1)
		{
			case 2:
				npc._i_ai2 = ((1 * 1000) + Rnd.get(3000));
				npc._c_ai2 = creature;
				break;
			
			case 3:
				npc._i_ai3 = ((1 * 1000) + Rnd.get(3000));
				npc._c_ai3 = creature;
				break;
			
			case 4:
				npc._i_ai4 = ((1 * 1000) + Rnd.get(3000));
				npc._c_ai4 = creature;
				break;
		}
		
		final IntentionType currentIntention = npc.getAI().getCurrentIntention().getType();
		
		if (npc.getSpawn().getSpawnData().getDBValue() == 3 && (currentIntention == IntentionType.WANDER || currentIntention == IntentionType.IDLE))
			castAntharasSkill(npc);
	}
	
	@Override
	public void onUseSkillFinished(Npc npc, Creature creature, L2Skill skill, boolean success)
	{
		castAntharasSkill(npc);
	}
	
	@Override
	public void onMyDying(Npc npc, Creature killer)
	{
		npc.broadcastPacket(new SpecialCamera(npc.getObjectId(), 1200, 20, -10, 0, 13000, 0, 0, 0, 0));
		npc.broadcastPacket(new PlaySound(1, "BS01_D", npc));
		npc.getSpawn().getSpawnData().setDBValue(0);
		
		int GM_ID = getNpcIntAIParam(npc, "GM_ID");
		GlobalMemo.getInstance().remove(String.valueOf(GM_ID));
		
		addSpawn(31859, 177615, 114941, -7709, 0, false, 900000, true);
	}
	
	private static void castAntharasSkill(Npc npc)
	{
		int i1 = 0;
		int i2 = 0;
		
		Creature c2 = null;
		
		if (npc._c_ai2 == null || npc.distance3D(npc._c_ai2) > 9000 || npc._c_ai2.isDead())
			npc._i_ai2 = 0;
		
		if (npc._c_ai3 == null || npc.distance3D(npc._c_ai3) > 9000 || npc._c_ai3.isDead())
			npc._i_ai3 = 0;
		
		if (npc._c_ai4 == null || npc.distance3D(npc._c_ai4) > 9000 || npc._c_ai4.isDead())
			npc._i_ai4 = 0;
		
		if (npc._i_ai2 > npc._i_ai3)
		{
			i1 = 2;
			i2 = npc._i_ai2;
			c2 = npc._c_ai2;
		}
		else
		{
			i1 = 3;
			i2 = npc._i_ai3;
			c2 = npc._c_ai3;
		}
		
		if (npc._i_ai4 > i2)
		{
			i1 = 4;
			i2 = npc._i_ai4;
			c2 = npc._c_ai4;
		}
		
		if (i2 > 0 && c2 != null)
		{
			if (Rnd.get(100) < 70)
			{
				switch (i1)
				{
					case 2:
						npc._i_ai2 = 500;
						break;
					
					case 3:
						npc._i_ai3 = 500;
						break;
					
					case 4:
						npc._i_ai4 = 500;
						break;
				}
			}
			
			final double hpRatio = npc.getStatus().getHpRatio();
			if (hpRatio < 0.25)
			{
				if (Rnd.get(100) < 30)
					npc.getAI().addCastDesire(c2, 4110, 1, 1000000, false);
				else if (Rnd.get(100) < 80 && ((npc.distance3D(c2) < 1423 && MathUtil.calculateAngleFrom(npc, c2) < (180 + 8) && MathUtil.calculateAngleFrom(npc, c2) > (180 - 8)) || (npc.distance3D(c2) < 802 && MathUtil.calculateAngleFrom(npc, c2) < (180 + 14) && MathUtil.calculateAngleFrom(npc, c2) > (180 - 14))))
					npc.getAI().addCastDesire(npc, 4107, 1, 1000000, false);
				else if (Rnd.get(100) < 40 && ((npc.distance3D(c2) < 850 && MathUtil.calculateAngleFrom(npc, c2) < (180 + 30) && MathUtil.calculateAngleFrom(npc, c2) > (180 - 30)) || (npc.distance3D(c2) < 425 && MathUtil.calculateAngleFrom(npc, c2) < (180 + 90) && MathUtil.calculateAngleFrom(npc, c2) > (180 - 90))))
					npc.getAI().addCastDesire(npc, 4107, 1, 1000000, false);
				else if (Rnd.get(100) < 10 && npc.distance3D(c2) < 1100)
					npc.getAI().addCastDesire(npc, 4106, 1, 1000000, false);
				else if (Rnd.get(100) < 10)
					npc.getAI().addCastDesire(c2, 4108, 1, 1000000, false);
				else if (Rnd.get(100) < 6)
					npc.getAI().addCastDesire(c2, 4111, 1, 1000000, false);
				else if (Rnd.get(100) < 50)
					npc.getAI().addCastDesire(c2, 4112, 1, 1000000, false);
				else
					npc.getAI().addCastDesire(c2, 4113, 1, 1000000, false);
			}
			else if (hpRatio < 0.5)
			{
				if (Rnd.get(100) < 80 && ((npc.distance3D(c2) < 1423 && MathUtil.calculateAngleFrom(npc, c2) < (180 + 8) && MathUtil.calculateAngleFrom(npc, c2) > (180 - 8)) || (npc.distance3D(c2) < 802 && MathUtil.calculateAngleFrom(npc, c2) < (180 + 14) && MathUtil.calculateAngleFrom(npc, c2) > (180 - 14))))
					npc.getAI().addCastDesire(npc, 4107, 1, 1000000, false);
				else if (Rnd.get(100) < 40 && ((npc.distance3D(c2) < 850 && MathUtil.calculateAngleFrom(npc, c2) < (180 + 30) && MathUtil.calculateAngleFrom(npc, c2) > (180 - 30)) || (npc.distance3D(c2) < 425 && MathUtil.calculateAngleFrom(npc, c2) < (180 + 90) && MathUtil.calculateAngleFrom(npc, c2) > (180 - 90))))
					npc.getAI().addCastDesire(npc, 4107, 1, 1000000, false);
				else if (Rnd.get(100) < 10 && npc.distance3D(c2) < 1100)
					npc.getAI().addCastDesire(npc, 4106, 1, 1000000, false);
				else if (Rnd.get(100) < 10)
					npc.getAI().addCastDesire(c2, 4108, 1, 1000000, false);
				else if (Rnd.get(100) < 6)
					npc.getAI().addCastDesire(c2, 4111, 1, 1000000, false);
				else if (Rnd.get(100) < 50)
					npc.getAI().addCastDesire(c2, 4112, 1, 1000000, false);
				else
					npc.getAI().addCastDesire(c2, 4113, 1, 1000000, false);
			}
			else if (hpRatio < 0.75)
			{
				if (Rnd.get(100) < 80 && ((npc.distance3D(c2) < 1423 && MathUtil.calculateAngleFrom(npc, c2) < (180 + 8) && MathUtil.calculateAngleFrom(npc, c2) > (180 - 8)) || (npc.distance3D(c2) < 802 && MathUtil.calculateAngleFrom(npc, c2) < (180 + 14) && MathUtil.calculateAngleFrom(npc, c2) > (180 - 14))))
					npc.getAI().addCastDesire(npc, 4107, 1, 1000000, false);
				else if (Rnd.get(100) < 10 && npc.distance3D(c2) < 1100)
					npc.getAI().addCastDesire(npc, 4106, 1, 1000000, false);
				else if (Rnd.get(100) < 10)
					npc.getAI().addCastDesire(c2, 4108, 1, 1000000, false);
				else if (Rnd.get(100) < 6)
					npc.getAI().addCastDesire(c2, 4111, 1, 1000000, false);
				else if (Rnd.get(100) < 50)
					npc.getAI().addCastDesire(c2, 4112, 1, 1000000, false);
				else
					npc.getAI().addCastDesire(c2, 4113, 1, 1000000, false);
			}
			else if (Rnd.get(100) < 80 && ((npc.distance3D(c2) < 1423 && MathUtil.calculateAngleFrom(npc, c2) < (180 + 8) && MathUtil.calculateAngleFrom(npc, c2) > (180 - 8)) || (npc.distance3D(c2) < 802 && MathUtil.calculateAngleFrom(npc, c2) < (180 + 14) && MathUtil.calculateAngleFrom(npc, c2) > (180 - 14))))
				npc.getAI().addCastDesire(npc, 4107, 1, 1000000, false);
			else if (Rnd.get(100) < 10)
				npc.getAI().addCastDesire(c2, 4108, 1, 1000000, false);
			else if (Rnd.get(100) < 6)
				npc.getAI().addCastDesire(c2, 4111, 1, 1000000, false);
			else if (Rnd.get(100) < 50)
				npc.getAI().addCastDesire(c2, 4112, 1, 1000000, false);
			else
				npc.getAI().addCastDesire(c2, 4113, 1, 1000000, false);
		}
	}
}