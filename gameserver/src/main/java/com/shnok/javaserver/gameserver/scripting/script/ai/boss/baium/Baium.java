package com.shnok.javaserver.gameserver.scripting.script.ai.boss.baium;

import com.shnok.javaserver.commons.random.Rnd;

import com.shnok.javaserver.gameserver.data.SkillTable;
import com.shnok.javaserver.gameserver.enums.IntentionType;
import com.shnok.javaserver.gameserver.enums.actors.ClassType;
import com.shnok.javaserver.gameserver.model.World;
import com.shnok.javaserver.gameserver.model.actor.Creature;
import com.shnok.javaserver.gameserver.model.actor.Npc;
import com.shnok.javaserver.gameserver.model.actor.Playable;
import com.shnok.javaserver.gameserver.model.actor.Player;
import com.shnok.javaserver.gameserver.model.memo.GlobalMemo;
import com.shnok.javaserver.gameserver.network.NpcStringId;
import com.shnok.javaserver.gameserver.network.serverpackets.Earthquake;
import com.shnok.javaserver.gameserver.network.serverpackets.PlaySound;
import com.shnok.javaserver.gameserver.network.serverpackets.SocialAction;
import com.shnok.javaserver.gameserver.scripting.script.ai.individual.DefaultNpc;
import com.shnok.javaserver.gameserver.skills.L2Skill;
import com.shnok.javaserver.gameserver.taskmanager.GameTimeTaskManager;

public class Baium extends DefaultNpc
{
	private static final String GM_ID = "2";
	
	public Baium()
	{
		super("ai/boss/baium");
	}
	
	public Baium(String descr)
	{
		super(descr);
	}
	
	protected final int[] _npcIds =
	{
		29020 // baium
	};
	
	@Override
	public void onNoDesire(Npc npc)
	{
		if (npc._i_ai3 == 1)
		{
			npc.getAI().addWanderDesire(5, 5);
			npc.lookNeighbor();
		}
	}
	
	@Override
	public void onScriptEvent(Npc npc, int eventId, int arg1, int arg2)
	{
		npc._i_quest3 = 1;
		npc._flag = 1;
		
		npc.getPosition().setHeading(-25348);
		npc.teleportTo(116033, 17447, 10107, 0);
		
		npc.getSpawn().getSpawnData().setDBValue(1);
		
		npc._param1 = arg1;
		
		startQuestTimer("2004", npc, null, 11500);
		startQuestTimer("2006", npc, null, 2000);
		startQuestTimer("2002", npc, null, 60000);
		
		npc._i_ai2 = GameTimeTaskManager.getInstance().getCurrentTick();
		
		startQuestTimer("2003", npc, null, ((Rnd.get(3) + 2) + (60 * 1000)));
		
		npc._i_ai3 = 0;
	}
	
	@Override
	public void onCreated(Npc npc)
	{
		npc._i_quest3 = 0;
		
		GlobalMemo.getInstance().set(GM_ID, npc.getObjectId());
		npc.getSpawn().getSpawnData().setDBValue(0);
		
		if (npc.isInMyTerritory())
		{
			npc.getSpawn().getSpawnData().setDBValue(1);
			
			npc._i_quest3 = 1;
			npc._i_ai2 = GameTimeTaskManager.getInstance().getCurrentTick();
			
			startQuestTimer("2001", npc, null, 5000);
			startQuestTimer("2002", npc, null, 60000);
			startQuestTimer("2003", npc, null, ((Rnd.get(3) + 2) + (60 * 1000)));
		}
		else
			createOnePrivateEx(npc, 29025, 116033, 17447, 10107, -25348, 0, false);
		
		npc._flag = 1;
		npc._i_ai0 = 1;
		npc._i_ai1 = 1;
		npc._i_ai3 = 0;
	}
	
	@Override
	public void onMyDying(Npc npc, Creature killer)
	{
		createOnePrivateEx(npc, 31842, 115017, 15549, 10090, 0, 0, false);
		
		npc.broadcastPacket(new PlaySound(1, "BS01_D", npc));
		
		GlobalMemo.getInstance().remove(GM_ID);
	}
	
	@Override
	public void onAttacked(Npc npc, Creature attacker, int damage, L2Skill skill)
	{
		if (npc._i_ai3 != 1)
			return;
		
		int i1 = 0;
		
		final L2Skill hinderStrider = SkillTable.getInstance().getInfo(4258, 1);
		if (attacker.isRiding() && getAbnormalLevel(attacker, hinderStrider) <= 0)
			npc.getAI().addCastDesire(attacker, hinderStrider, 1000000);
		
		if (attacker instanceof Playable)
		{
			if (attacker instanceof Player)
			{
				npc._i_ai0 += damage;
				npc._i_ai2 = GameTimeTaskManager.getInstance().getCurrentTick();
				
				if (skill == null)
				{
					if (attacker == npc._c_quest0)
					{
						if (((damage * 1000) + 1000) > npc._i_quest0)
							npc._i_quest0 = ((damage * 1000) + Rnd.get(3000));
					}
					else if (attacker == npc._c_quest1)
					{
						if (((damage * 1000) + 1000) > npc._i_quest1)
							npc._i_quest1 = ((damage * 1000) + Rnd.get(3000));
					}
					else if (attacker == npc._c_quest2)
					{
						if (((damage * 1000) + 1000) > npc._i_quest2)
							npc._i_quest2 = ((damage * 1000) + Rnd.get(3000));
					}
					else if (npc._i_quest0 > npc._i_quest1)
						i1 = 3;
					else if (npc._i_quest0 == npc._i_quest1)
						i1 = (Rnd.nextBoolean()) ? 2 : 3;
					else if (npc._i_quest0 < npc._i_quest1)
						i1 = 2;
					
					if (i1 == 2)
					{
						if (npc._i_quest0 > npc._i_quest2)
							i1 = 4;
						else if (npc._i_quest0 == npc._i_quest2)
							i1 = (Rnd.nextBoolean()) ? 2 : 4;
						else if (npc._i_quest0 < npc._i_quest2)
							i1 = 2;
					}
					else if (i1 == 3)
					{
						if (npc._i_quest1 > npc._i_quest2)
							i1 = 4;
						else if (npc._i_quest1 == npc._i_quest2)
							i1 = (Rnd.nextBoolean()) ? 3 : 4;
						else if (npc._i_quest1 < npc._i_quest2)
							i1 = 3;
					}
					
					switch (i1)
					{
						case 2:
							npc._i_quest0 = ((damage * 1000) + Rnd.get(3000));
							npc._c_quest0 = attacker;
							break;
						
						case 3:
							npc._i_quest1 = ((damage * 1000) + Rnd.get(3000));
							npc._c_quest1 = attacker;
							break;
						
						case 4:
							npc._i_quest2 = ((damage * 1000) + Rnd.get(3000));
							npc._c_quest2 = attacker;
							break;
					}
				}
				else if (npc.getStatus().getHp() < ((npc.getStatus().getMaxHp() * 1.0) / 4.0))
				{
					if (attacker == npc._c_quest0)
					{
						if ((((damage / 30) * 1000) + 1000) > npc._i_quest0)
							npc._i_quest0 = (((damage / 30) * 1000) + Rnd.get(3000));
					}
					else if (attacker == npc._c_quest1)
					{
						if ((((damage / 30) * 1000) + 1000) > npc._i_quest1)
							npc._i_quest1 = (((damage / 30) * 1000) + Rnd.get(3000));
					}
					else if (attacker == npc._c_quest2)
					{
						if ((((damage / 30) * 1000) + 1000) > npc._i_quest2)
							npc._i_quest2 = (((damage / 30) * 1000) + Rnd.get(3000));
					}
					else if (npc._i_quest0 > npc._i_quest1)
						i1 = 3;
					else if (npc._i_quest0 == npc._i_quest1)
						i1 = (Rnd.nextBoolean()) ? 2 : 3;
					else if (npc._i_quest0 < npc._i_quest1)
						i1 = 2;
					
					if (i1 == 2)
					{
						if (npc._i_quest0 > npc._i_quest2)
							i1 = 4;
						else if (npc._i_quest0 == npc._i_quest2)
							i1 = (Rnd.nextBoolean()) ? 2 : 4;
						else if (npc._i_quest0 < npc._i_quest2)
							i1 = 2;
					}
					else if (i1 == 3)
					{
						if (npc._i_quest1 > npc._i_quest2)
							i1 = 4;
						else if (npc._i_quest1 == npc._i_quest2)
							i1 = (Rnd.nextBoolean()) ? 3 : 4;
						else if (npc._i_quest1 < npc._i_quest2)
							i1 = 3;
					}
					
					switch (i1)
					{
						case 2:
							npc._i_quest0 = (((damage / 30) * 1000) + Rnd.get(3000));
							npc._c_quest0 = attacker;
							break;
						
						case 3:
							npc._i_quest1 = (((damage / 30) * 1000) + Rnd.get(3000));
							npc._c_quest1 = attacker;
							break;
						
						case 4:
							npc._i_quest2 = (((damage / 30) * 1000) + Rnd.get(3000));
							npc._c_quest2 = attacker;
							break;
					}
				}
				else if (npc.getStatus().getHp() < ((npc.getStatus().getMaxHp() * 2.0) / 4.0))
				{
					if (attacker == npc._c_quest0)
					{
						if ((((damage / 50) * 1000) + 1000) > npc._i_quest0)
							npc._i_quest0 = (((damage / 50) * 1000) + Rnd.get(3000));
					}
					else if (attacker == npc._c_quest1)
					{
						if ((((damage / 50) * 1000) + 1000) > npc._i_quest1)
							npc._i_quest1 = (((damage / 50) * 1000) + Rnd.get(3000));
					}
					else if (attacker == npc._c_quest2)
					{
						if ((((damage / 50) * 1000) + 1000) > npc._i_quest2)
							npc._i_quest2 = (((damage / 50) * 1000) + Rnd.get(3000));
					}
					else if (npc._i_quest0 > npc._i_quest1)
						i1 = 3;
					else if (npc._i_quest0 == npc._i_quest1)
						i1 = (Rnd.nextBoolean()) ? 2 : 3;
					else if (npc._i_quest0 < npc._i_quest1)
						i1 = 2;
					
					if (i1 == 2)
					{
						if (npc._i_quest0 > npc._i_quest2)
							i1 = 4;
						else if (npc._i_quest0 == npc._i_quest2)
							i1 = (Rnd.nextBoolean()) ? 2 : 4;
						else if (npc._i_quest0 < npc._i_quest2)
							i1 = 2;
					}
					else if (i1 == 3)
					{
						if (npc._i_quest1 > npc._i_quest2)
							i1 = 4;
						else if (npc._i_quest1 == npc._i_quest2)
							i1 = (Rnd.nextBoolean()) ? 3 : 4;
						else if (npc._i_quest1 < npc._i_quest2)
							i1 = 3;
					}
					
					switch (i1)
					{
						case 2:
							npc._i_quest0 = (((damage / 50) * 1000) + Rnd.get(3000));
							npc._c_quest0 = attacker;
							break;
						
						case 3:
							npc._i_quest1 = (((damage / 50) * 1000) + Rnd.get(3000));
							npc._c_quest1 = attacker;
							break;
						
						case 4:
							npc._i_quest2 = (((damage / 50) * 1000) + Rnd.get(3000));
							npc._c_quest2 = attacker;
							break;
					}
				}
				else if (npc.getStatus().getHp() < ((npc.getStatus().getMaxHp() * 3.0) / 4.0))
				{
					if (attacker == npc._c_quest0)
					{
						if ((((damage / 100) * 1000) + 1000) > npc._i_quest0)
							npc._i_quest0 = (((damage / 100) * 1000) + Rnd.get(3000));
					}
					else if (attacker == npc._c_quest1)
					{
						if ((((damage / 100) * 1000) + 1000) > npc._i_quest1)
							npc._i_quest1 = (((damage / 100) * 1000) + Rnd.get(3000));
					}
					else if (attacker == npc._c_quest2)
					{
						if ((((damage / 100) * 1000) + 1000) > npc._i_quest2)
							npc._i_quest2 = (((damage / 100) * 1000) + Rnd.get(3000));
					}
					else if (npc._i_quest0 > npc._i_quest1)
						i1 = 3;
					else if (npc._i_quest0 == npc._i_quest1)
						i1 = (Rnd.nextBoolean()) ? 2 : 3;
					else if (npc._i_quest0 < npc._i_quest1)
						i1 = 2;
					
					if (i1 == 2)
					{
						if (npc._i_quest0 > npc._i_quest2)
							i1 = 4;
						else if (npc._i_quest0 == npc._i_quest2)
							i1 = (Rnd.nextBoolean()) ? 2 : 4;
						else if (npc._i_quest0 < npc._i_quest2)
							i1 = 2;
					}
					else if (i1 == 3)
					{
						if (npc._i_quest1 > npc._i_quest2)
							i1 = 4;
						else if (npc._i_quest1 == npc._i_quest2)
							i1 = (Rnd.nextBoolean()) ? 3 : 4;
						else if (npc._i_quest1 < npc._i_quest2)
							i1 = 3;
					}
					
					switch (i1)
					{
						case 2:
							npc._i_quest0 = (((damage / 100) * 1000) + Rnd.get(3000));
							npc._c_quest0 = attacker;
							break;
						
						case 3:
							npc._i_quest1 = (((damage / 100) * 1000) + Rnd.get(3000));
							npc._c_quest1 = attacker;
							break;
						
						case 4:
							npc._i_quest2 = (((damage / 100) * 1000) + Rnd.get(3000));
							npc._c_quest2 = attacker;
							break;
					}
				}
				else if (attacker == npc._c_quest0)
				{
					if ((((damage / 150) * 1000) + 1000) > npc._i_quest0)
						npc._i_quest0 = (((damage / 150) * 1000) + Rnd.get(3000));
				}
				else if (attacker == npc._c_quest1)
				{
					if ((((damage / 150) * 1000) + 1000) > npc._i_quest1)
						npc._i_quest1 = (((damage / 150) * 1000) + Rnd.get(3000));
				}
				else if (attacker == npc._c_quest2)
				{
					if ((((damage / 150) * 1000) + 1000) > npc._i_quest2)
						npc._i_quest2 = (((damage / 150) * 1000) + Rnd.get(3000));
				}
				else if (npc._i_quest0 > npc._i_quest1)
					i1 = 3;
				else if (npc._i_quest0 == npc._i_quest1)
					i1 = (Rnd.nextBoolean()) ? 2 : 3;
				else if (npc._i_quest0 < npc._i_quest1)
					i1 = 2;
				
				if (i1 == 2)
				{
					if (npc._i_quest0 > npc._i_quest2)
						i1 = 4;
					else if (npc._i_quest0 == npc._i_quest2)
						i1 = (Rnd.nextBoolean()) ? 2 : 4;
					else if (npc._i_quest0 < npc._i_quest2)
						i1 = 2;
				}
				else if (i1 == 3)
				{
					if (npc._i_quest1 > npc._i_quest2)
						i1 = 4;
					else if (npc._i_quest1 == npc._i_quest2)
						i1 = (Rnd.nextBoolean()) ? 3 : 4;
					else if (npc._i_quest1 < npc._i_quest2)
						i1 = 3;
				}
				
				switch (i1)
				{
					case 2:
						npc._i_quest0 = (((damage / 150) * 1000) + Rnd.get(3000));
						npc._c_quest0 = attacker;
						break;
					
					case 3:
						npc._i_quest1 = (((damage / 150) * 1000) + Rnd.get(3000));
						npc._c_quest1 = attacker;
						break;
					
					case 4:
						npc._i_quest2 = (((damage / 150) * 1000) + Rnd.get(3000));
						npc._c_quest2 = attacker;
						break;
				}
			}
		}
		else
			npc._c_ai3 = attacker;
		
		if (npc.getAI().getCurrentIntention().getType() == IntentionType.WANDER && npc._i_ai3 != 0)
			castBaiumSkill(npc, i1);
	}
	
	@Override
	public void onPartyAttacked(Npc caller, Npc called, Creature target, int damage)
	{
		if (caller != called && target != called)
			called._i_ai1 += damage;
	}
	
	@Override
	public void onSeeCreature(Npc npc, Creature creature)
	{
		if (!npc.getSpawn().isInMyTerritory(creature))
			return;
		
		int i1 = 0;
		
		final Player player = creature.getActingPlayer();
		if (player != null && npc.getAI().getLifeTime() < 10)
			npc._c_ai1 = creature;
		
		if (player != null && player.getClassId().getType() == ClassType.PRIEST)
		{
			final double hpRatio = npc.getStatus().getHpRatio();
			if (hpRatio < 0.25)
			{
				if (creature == npc._c_quest0)
				{
					if (((10 * 1000) + 1000) > npc._i_quest0)
						npc._i_quest0 = ((10 * 1000) + Rnd.get(3000));
				}
				else if (creature == npc._c_quest1)
				{
					if (((10 * 1000) + 1000) > npc._i_quest1)
						npc._i_quest1 = ((10 * 1000) + Rnd.get(3000));
				}
				else if (creature == npc._c_quest2)
				{
					if (((10 * 1000) + 1000) > npc._i_quest2)
						npc._i_quest2 = ((10 * 1000) + Rnd.get(3000));
				}
				else if (npc._i_quest0 > npc._i_quest1)
					i1 = 3;
				else if (npc._i_quest0 == npc._i_quest1)
					i1 = (Rnd.nextBoolean()) ? 2 : 3;
				else if (npc._i_quest0 < npc._i_quest1)
					i1 = 2;
				
				if (i1 == 2)
				{
					if (npc._i_quest0 > npc._i_quest2)
						i1 = 4;
					else if (npc._i_quest0 == npc._i_quest2)
						i1 = (Rnd.nextBoolean()) ? 2 : 4;
					else if (npc._i_quest0 < npc._i_quest2)
						i1 = 2;
				}
				else if (i1 == 3)
				{
					if (npc._i_quest1 > npc._i_quest2)
						i1 = 4;
					else if (npc._i_quest1 == npc._i_quest2)
						i1 = (Rnd.nextBoolean()) ? 3 : 4;
					else if (npc._i_quest1 < npc._i_quest2)
						i1 = 3;
				}
				
				switch (i1)
				{
					case 2:
						npc._i_quest0 = ((10 * 1000) + Rnd.get(3000));
						npc._c_quest0 = creature;
						break;
					
					case 3:
						npc._i_quest1 = ((10 * 1000) + Rnd.get(3000));
						npc._c_quest1 = creature;
						break;
					
					case 4:
						npc._i_quest2 = ((10 * 1000) + Rnd.get(3000));
						npc._c_quest2 = creature;
						break;
				}
			}
			else if (hpRatio < 0.5)
			{
				if (creature == npc._c_quest0)
				{
					if (((6 * 1000) + 1000) > npc._i_quest0)
						npc._i_quest0 = ((6 * 1000) + Rnd.get(3000));
				}
				else if (creature == npc._c_quest1)
				{
					if (((6 * 1000) + 1000) > npc._i_quest1)
						npc._i_quest1 = ((6 * 1000) + Rnd.get(3000));
				}
				else if (creature == npc._c_quest2)
				{
					if (((6 * 1000) + 1000) > npc._i_quest2)
						npc._i_quest2 = ((6 * 1000) + Rnd.get(3000));
				}
				else if (npc._i_quest0 > npc._i_quest1)
					i1 = 3;
				else if (npc._i_quest0 == npc._i_quest1)
					i1 = (Rnd.nextBoolean()) ? 2 : 3;
				else if (npc._i_quest0 < npc._i_quest1)
					i1 = 2;
				
				if (i1 == 2)
				{
					if (npc._i_quest0 > npc._i_quest2)
						i1 = 4;
					else if (npc._i_quest0 == npc._i_quest2)
						i1 = (Rnd.nextBoolean()) ? 2 : 4;
					else if (npc._i_quest0 < npc._i_quest2)
						i1 = 2;
				}
				else if (i1 == 3)
				{
					if (npc._i_quest1 > npc._i_quest2)
						i1 = 4;
					else if (npc._i_quest1 == npc._i_quest2)
						i1 = (Rnd.nextBoolean()) ? 3 : 4;
					else if (npc._i_quest1 < npc._i_quest2)
						i1 = 3;
				}
				
				switch (i1)
				{
					case 2:
						npc._i_quest0 = ((6 * 1000) + Rnd.get(3000));
						npc._c_quest0 = creature;
						break;
					
					case 3:
						npc._i_quest1 = ((6 * 1000) + Rnd.get(3000));
						npc._c_quest1 = creature;
						break;
					
					case 4:
						npc._i_quest2 = ((6 * 1000) + Rnd.get(3000));
						npc._c_quest2 = creature;
						break;
				}
			}
			else if (hpRatio < 0.75)
			{
				if (creature == npc._c_quest0)
				{
					if (((3 * 1000) + 1000) > npc._i_quest0)
						npc._i_quest0 = ((3 * 1000) + Rnd.get(3000));
				}
				else if (creature == npc._c_quest1)
				{
					if (((3 * 1000) + 1000) > npc._i_quest1)
						npc._i_quest1 = ((3 * 1000) + Rnd.get(3000));
				}
				else if (creature == npc._c_quest2)
				{
					if (((3 * 1000) + 1000) > npc._i_quest2)
						npc._i_quest2 = ((3 * 1000) + Rnd.get(3000));
				}
				else if (npc._i_quest0 > npc._i_quest1)
					i1 = 3;
				else if (npc._i_quest0 == npc._i_quest1)
					i1 = (Rnd.nextBoolean()) ? 2 : 3;
				else if (npc._i_quest0 < npc._i_quest1)
					i1 = 2;
				
				if (i1 == 2)
				{
					if (npc._i_quest0 > npc._i_quest2)
						i1 = 4;
					else if (npc._i_quest0 == npc._i_quest2)
						i1 = (Rnd.nextBoolean()) ? 2 : 4;
					else if (npc._i_quest0 < npc._i_quest2)
						i1 = 2;
				}
				else if (i1 == 3)
				{
					if (npc._i_quest1 > npc._i_quest2)
						i1 = 4;
					else if (npc._i_quest1 == npc._i_quest2)
						i1 = (Rnd.nextBoolean()) ? 3 : 4;
					else if (npc._i_quest1 < npc._i_quest2)
						i1 = 3;
				}
				
				switch (i1)
				{
					case 2:
						npc._i_quest0 = ((3 * 1000) + Rnd.get(3000));
						npc._c_quest0 = creature;
						break;
					
					case 3:
						npc._i_quest1 = ((3 * 1000) + Rnd.get(3000));
						npc._c_quest1 = creature;
						break;
					
					case 4:
						npc._i_quest2 = ((3 * 1000) + Rnd.get(3000));
						npc._c_quest2 = creature;
						break;
				}
			}
			else if (creature == npc._c_quest0)
			{
				if (((2 * 1000) + 1000) > npc._i_quest0)
					npc._i_quest0 = ((2 * 1000) + Rnd.get(3000));
			}
			else if (creature == npc._c_quest1)
			{
				if (((2 * 1000) + 1000) > npc._i_quest1)
					npc._i_quest1 = ((2 * 1000) + Rnd.get(3000));
			}
			else if (creature == npc._c_quest2)
			{
				if (((2 * 1000) + 1000) > npc._i_quest2)
					npc._i_quest2 = ((2 * 1000) + Rnd.get(3000));
			}
			else if (npc._i_quest0 > npc._i_quest1)
				i1 = 3;
			else if (npc._i_quest0 == npc._i_quest1)
				i1 = (Rnd.nextBoolean()) ? 2 : 3;
			else if (npc._i_quest0 < npc._i_quest1)
				i1 = 2;
			
			if (i1 == 2)
			{
				if (npc._i_quest0 > npc._i_quest2)
					i1 = 4;
				else if (npc._i_quest0 == npc._i_quest2)
					i1 = (Rnd.nextBoolean()) ? 2 : 4;
				else if (npc._i_quest0 < npc._i_quest2)
					i1 = 2;
			}
			else if (i1 == 3)
			{
				if (npc._i_quest1 > npc._i_quest2)
					i1 = 4;
				else if (npc._i_quest1 == npc._i_quest2)
					i1 = (Rnd.nextBoolean()) ? 3 : 4;
				else if (npc._i_quest1 < npc._i_quest2)
					i1 = 3;
			}
			
			switch (i1)
			{
				case 2:
					npc._i_quest0 = ((2 * 1000) + Rnd.get(3000));
					npc._c_quest0 = creature;
					break;
				
				case 3:
					npc._i_quest1 = ((2 * 1000) + Rnd.get(3000));
					npc._c_quest1 = creature;
					break;
				
				case 4:
					npc._i_quest2 = ((2 * 1000) + Rnd.get(3000));
					npc._c_quest2 = creature;
					break;
			}
		}
		else if (creature == npc._c_quest0)
		{
			if (((1 * 1000) + 1000) > npc._i_quest0)
				npc._i_quest0 = ((1 * 1000) + Rnd.get(3000));
		}
		else if (creature == npc._c_quest1)
		{
			if (((1 * 1000) + 1000) > npc._i_quest1)
				npc._i_quest1 = ((1 * 1000) + Rnd.get(3000));
		}
		else if (creature == npc._c_quest2)
		{
			if (((1 * 1000) + 1000) > npc._i_quest2)
				npc._i_quest2 = ((1 * 1000) + Rnd.get(3000));
		}
		else if (npc._i_quest0 > npc._i_quest1)
			i1 = 3;
		else if (npc._i_quest0 == npc._i_quest1)
			i1 = (Rnd.nextBoolean()) ? 2 : 3;
		else if (npc._i_quest0 < npc._i_quest1)
			i1 = 2;
		
		if (i1 == 2)
		{
			if (npc._i_quest0 > npc._i_quest2)
				i1 = 4;
			else if (npc._i_quest0 == npc._i_quest2)
				i1 = (Rnd.nextBoolean()) ? 2 : 4;
			else if (npc._i_quest0 < npc._i_quest2)
				i1 = 2;
		}
		else if (i1 == 3)
		{
			if (npc._i_quest1 > npc._i_quest2)
				i1 = 4;
			else if (npc._i_quest1 == npc._i_quest2)
				i1 = (Rnd.nextBoolean()) ? 3 : 4;
			else if (npc._i_quest1 < npc._i_quest2)
				i1 = 3;
		}
		
		switch (i1)
		{
			case 2:
				npc._i_quest0 = ((1 * 1000) + Rnd.get(3000));
				npc._c_quest0 = creature;
				break;
			
			case 3:
				npc._i_quest1 = ((1 * 1000) + Rnd.get(3000));
				npc._c_quest1 = creature;
				break;
			
			case 4:
				npc._i_quest2 = ((1 * 1000) + Rnd.get(3000));
				npc._c_quest2 = creature;
				break;
		}
		
		if (npc.getAI().getCurrentIntention().getType() == IntentionType.WANDER && npc._i_ai3 != 0)
			castBaiumSkill(npc, i1);
	}
	
	@Override
	public void onUseSkillFinished(Npc npc, Creature creature, L2Skill skill, boolean success)
	{
		if (npc._i_ai3 == 0)
			return;
		
		castBaiumSkill(npc, 0);
	}
	
	@Override
	public String onTimer(String name, Npc npc, Player player)
	{
		if (npc._i_quest3 == 0)
			return super.onTimer(name, npc, player);
		
		if (name.equalsIgnoreCase("2001"))
		{
			createOnePrivateEx(npc, 29021, 115792, 16608, 10136, 0, 0, false, 1, 0, 0);
			createOnePrivateEx(npc, 29021, 115168, 17200, 10136, 0, 0, false, 2, 0, 0);
			createOnePrivateEx(npc, 29021, 115780, 15564, 10136, 13620, 0, false, 3, 0, 0);
			createOnePrivateEx(npc, 29021, 114880, 16236, 10136, 5400, 0, false, 4, 0, 0);
			createOnePrivateEx(npc, 29021, 114239, 17168, 10136, -1992, 0, false, 5, 0, 0);
			
			npc._i_ai3 = 1;
			
			castBaiumSkill(npc, 0);
		}
		else if (name.equalsIgnoreCase("2002"))
		{
			final int i0 = getElapsedTicks(npc._i_ai2);
			if (i0 > (30 * 60))
			{
				npc.removeAllDesire();
				npc.getSpawn().getSpawnData().setDBValue(0);
				npc.getSpawn().instantTeleportInMyTerritory(120112, 18208, -5152, 900);
				npc.teleportTo(-105200, -253104, -15264, 0);
				
				createOnePrivateEx(npc, 29025, 116026, 17435, 10107, -25348, 0, false);
				
				npc._i_quest3 = 0;
				npc._i_ai3 = 0;
				npc._flag = 6;
			}
			else if (i0 > (5 * 60) && npc.getStatus().getHpRatio() < 0.75)
			{
				startQuestTimer("2002", npc, null, 60000);
				
				npc.getAI().addCastDesire(npc, 4135, 1, 1000000);
			}
			else
				startQuestTimer("2002", npc, null, 60000);
		}
		else if (name.equalsIgnoreCase("2003"))
		{
			int i0 = 0;
			int i1 = 0;
			
			npc._i_ai1 = (int) (npc._i_ai1 * 2.73);
			
			i0 = (npc._i_ai0 + npc._i_ai1);
			
			if (npc._i_ai0 > npc._i_ai1)
				i1 = (npc._i_ai0 - npc._i_ai1);
			else
				i1 = (npc._i_ai1 - npc._i_ai0);
			
			if ((i1 * 10) > (3 * i0))
				npc._flag = 0;
			else
				npc._flag = 1 + Rnd.get(4);
			
			i0 = ((Rnd.get(3) + 2) + (60 * 1000));
			
			startQuestTimer("2003", npc, null, 30000);
			
			npc._i_ai0 = 1;
			npc._i_ai1 = 1;
		}
		else if (name.equalsIgnoreCase("2004"))
		{
			npc.broadcastPacket(new SocialAction(npc, 3));
			
			startQuestTimer("2005", npc, null, 7300);
		}
		else if (name.equalsIgnoreCase("2005"))
		{
			npc.broadcastPacket(new SocialAction(npc, 1));
			
			final Creature c0 = (npc._param1 != 0) ? (Creature) World.getInstance().getObject(npc._param1) : null;
			if (c0 != null)
			{
				if (npc.distance2D(c0) < 16000)
				{
					npc.broadcastNpcSay(NpcStringId.ID_22937);
					c0.teleportTo(115910, 17337, 10105, 0);
					npc.getAI().addCastDesire(c0, 4136, 1, 1100000);
				}
			}
			else if (npc._c_ai1 != null)
			{
				npc.broadcastNpcSay(NpcStringId.ID_22937);
				npc._c_ai1.teleportTo(115910, 17337, 10105, 0);
				npc.getAI().addCastDesire(npc._c_ai1, 4136, 1, 1100000);
			}
			
			final Player heroPlayer = npc.getNeighborHero(4000);
			if (heroPlayer != null)
				npc.broadcastOnScreen(3000, NpcStringId.ID_1000521, heroPlayer.getName());
			
			startQuestTimer("2001", npc, null, 8000);
		}
		else if (name.equalsIgnoreCase("2006"))
		{
			npc.broadcastPacket(new Earthquake(npc, 40, 10, true));
			npc.broadcastPacket(new PlaySound(1, "BS02_A", npc));
		}
		
		return super.onTimer(name, npc, player);
	}
	
	private static void castBaiumSkill(Npc npc, int i1)
	{
		Creature c2 = null;
		int i2 = 0;
		
		if (npc._c_ai3 != null && npc._flag == 0 && Rnd.get(100) < 10)
			c2 = npc._c_ai3;
		else if (npc._c_quest0 == null || npc.distance2D(npc._c_quest0) > 9000 || npc._c_quest0.isDead())
			npc._i_quest0 = 0;
		
		if (npc._c_quest1 == null || npc.distance2D(npc._c_quest1) > 9000 || npc._c_quest1.isDead())
			npc._i_quest1 = 0;
		
		if (npc._c_quest2 == null || npc.distance2D(npc._c_quest2) > 9000 || npc._c_quest2.isDead())
			npc._i_quest2 = 0;
		
		if (npc._i_quest0 > npc._i_quest1)
		{
			i1 = 0;
			i2 = npc._i_quest0;
			c2 = npc._c_quest0;
		}
		else
		{
			i1 = 1;
			i2 = npc._i_quest1;
			c2 = npc._c_quest1;
		}
		
		if (npc._i_quest2 > i2)
		{
			i1 = 2;
			i2 = npc._i_quest2;
			c2 = npc._c_quest2;
		}
		
		if (i2 > 0 && Rnd.get(100) < 70)
		{
			switch (i1)
			{
				case 0:
					npc._i_quest0 = 500;
					break;
				
				case 1:
					npc._i_quest1 = 500;
					break;
				
				case 2:
					npc._i_quest2 = 500;
					break;
			}
		}
		
		if (c2 == null || i2 == 0)
			c2 = npc._c_ai3;
		
		if (c2 != null && !c2.isDead())
		{
			final double hpRatio = npc.getStatus().getHpRatio();
			if (hpRatio > 0.75)
			{
				if (Rnd.get(100) < 10)
					npc.getAI().addCastDesire(c2, 4128, 1, 1000000);
				else if (Rnd.get(100) < 10)
					npc.getAI().addCastDesire(c2, 4129, 1, 1000000);
				else
					npc.getAI().addCastDesire(c2, 4127, 1, 1000000);
			}
			else if (hpRatio > 0.5)
			{
				if (Rnd.get(100) < 10)
					npc.getAI().addCastDesire(c2, 4131, 1, 1000000);
				else if (Rnd.get(100) < 10)
					npc.getAI().addCastDesire(c2, 4128, 1, 1000000);
				else if (Rnd.get(100) < 10)
					npc.getAI().addCastDesire(c2, 4129, 1, 1000000);
				else
					npc.getAI().addCastDesire(c2, 4127, 1, 1000000);
			}
			else if (hpRatio > 0.25)
			{
				if (Rnd.get(100) < 10)
					npc.getAI().addCastDesire(c2, 4130, 1, 1000000);
				else if (Rnd.get(100) < 10)
					npc.getAI().addCastDesire(c2, 4131, 1, 1000000);
				else if (Rnd.get(100) < 10)
					npc.getAI().addCastDesire(c2, 4128, 1, 1000000);
				else if (Rnd.get(100) < 10)
					npc.getAI().addCastDesire(c2, 4129, 1, 1000000);
				else
					npc.getAI().addCastDesire(c2, 4127, 1, 1000000);
			}
			else if (Rnd.get(100) < 10)
				npc.getAI().addCastDesire(c2, 4130, 1, 1000000);
			else if (Rnd.get(100) < 10)
				npc.getAI().addCastDesire(c2, 4131, 1, 1000000);
			else if (Rnd.get(100) < 10)
				npc.getAI().addCastDesire(c2, 4128, 1, 1000000);
			else if (Rnd.get(100) < 10)
				npc.getAI().addCastDesire(c2, 4129, 1, 1000000);
			else
				npc.getAI().addCastDesire(c2, 4127, 1, 1000000);
		}
	}
}