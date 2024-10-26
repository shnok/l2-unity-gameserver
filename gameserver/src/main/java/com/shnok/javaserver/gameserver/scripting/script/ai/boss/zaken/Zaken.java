package com.shnok.javaserver.gameserver.scripting.script.ai.boss.zaken;

import net.sf.l2j.commons.pool.ThreadPool;
import net.sf.l2j.commons.random.Rnd;

import net.sf.l2j.gameserver.data.SkillTable;
import net.sf.l2j.gameserver.enums.IntentionType;
import net.sf.l2j.gameserver.model.actor.Creature;
import net.sf.l2j.gameserver.model.actor.Npc;
import net.sf.l2j.gameserver.model.actor.Playable;
import net.sf.l2j.gameserver.model.actor.Player;
import net.sf.l2j.gameserver.model.location.SpawnLocation;
import net.sf.l2j.gameserver.network.serverpackets.PlaySound;
import net.sf.l2j.gameserver.scripting.script.ai.individual.DefaultNpc;
import net.sf.l2j.gameserver.skills.L2Skill;
import net.sf.l2j.gameserver.taskmanager.GameTimeTaskManager;

public class Zaken extends DefaultNpc
{
	public Zaken()
	{
		super("ai/boss/zaken");
	}
	
	public Zaken(String descr)
	{
		super(descr);
	}
	
	protected final int[] _npcIds =
	{
		29022 // zaken
	};
	
	@Override
	public void onNoDesire(Npc npc)
	{
		npc.getAI().addWanderDesire(5, 5);
	}
	
	@Override
	public void onCreated(Npc npc)
	{
		npc.broadcastPacket(new PlaySound(1, "BS01_A", npc));
		
		npc._i_ai0 = 0;
		npc._i_ai1 = npc.getX();
		npc._i_ai2 = npc.getY();
		npc._i_ai3 = npc.getZ();
		npc._i_quest0 = 0;
		
		if (!npc.getSpawn().getDBLoaded())
			npc._i_quest2 = 3;
		
		npc._i_quest1 = 0;
		
		if (npc.isInMyTerritory())
		{
			npc._i_ai4 = 1;
			
			startQuestTimer("1003", npc, null, 1700);
		}
		
		startQuestTimerAtFixedRate("1001", npc, null, 1000, 30000);
	}
	
	@Override
	public String onTimer(String name, Npc npc, Player player)
	{
		if (name.equalsIgnoreCase("1001"))
		{
			int i0 = 0;
			int i2 = 0;
			
			final L2Skill zakenNightToDay = SkillTable.getInstance().getInfo(4223, 1);
			final L2Skill zakenDayToNight = SkillTable.getInstance().getInfo(4224, 1);
			final L2Skill zakenRegeneration = SkillTable.getInstance().getInfo(4227, 1);
			
			if (GameTimeTaskManager.getInstance().getGameHour() < 5)
			{
				if (getAbnormalLevel(npc, zakenNightToDay) == 1)
				{
					npc.getAI().addCastDesire(npc, zakenDayToNight, 10000000);
					npc._i_ai1 = npc.getX();
					npc._i_ai2 = npc.getY();
					npc._i_ai3 = npc.getZ();
				}
				
				IntentionType currentIntention = npc.getAI().getCurrentIntention().getType();
				
				final Creature topDesireTarget = npc.getAI().getTopDesireTarget();
				
				if (getAbnormalLevel(npc, zakenRegeneration) == -1)
				{
					npc.getAI().addCastDesire(npc, zakenRegeneration, 10000000);
				}
				if (currentIntention == IntentionType.ATTACK && npc._i_ai0 == 0)
				{
					int i1 = 1;
					if (topDesireTarget != null)
					{
						if ((((topDesireTarget.getX() - npc._i_ai1) * (topDesireTarget.getX() - npc._i_ai1)) + ((topDesireTarget.getY() - npc._i_ai2) * (topDesireTarget.getY() - npc._i_ai2))) > (1500 * 1500))
						{
							i0 = 1;
						}
						else
						{
							i0 = 0;
						}
					}
					if (i0 == 0)
					{
						i1 = 0;
					}
					if (npc._i_quest0 > 0)
					{
						if (npc._c_quest0 == null || (((npc._c_quest0.getX() - npc._i_ai1) * (npc._c_quest0.getX() - npc._i_ai1)) + ((npc._c_quest0.getY() - npc._i_ai2) * (npc._c_quest0.getY() - npc._i_ai2))) > (1500 * 1500))
						{
							i0 = 1;
						}
						else
						{
							i0 = 0;
						}
						if (i0 == 0)
						{
							i1 = 0;
						}
					}
					if (npc._i_quest0 > 1)
					{
						if (npc._c_quest1 == null || (((npc._c_quest1.getX() - npc._i_ai1) * (npc._c_quest1.getX() - npc._i_ai1)) + ((npc._c_quest1.getY() - npc._i_ai2) * (npc._c_quest1.getY() - npc._i_ai2))) > (1500 * 1500))
						{
							i0 = 1;
						}
						else
						{
							i0 = 0;
						}
						if (i0 == 0)
						{
							i1 = 0;
						}
					}
					if (npc._i_quest0 > 2)
					{
						if (npc._c_quest2 == null || (((npc._c_quest2.getX() - npc._i_ai1) * (npc._c_quest2.getX() - npc._i_ai1)) + ((npc._c_quest2.getY() - npc._i_ai2) * (npc._c_quest2.getY() - npc._i_ai2))) > (1500 * 1500))
						{
							i0 = 1;
						}
						else
						{
							i0 = 0;
						}
						if (i0 == 0)
						{
							i1 = 0;
						}
					}
					if (npc._i_quest0 > 3)
					{
						if (npc._c_quest3 == null || (((npc._c_quest3.getX() - npc._i_ai1) * (npc._c_quest3.getX() - npc._i_ai1)) + ((npc._c_quest3.getY() - npc._i_ai2) * (npc._c_quest3.getY() - npc._i_ai2))) > (1500 * 1500))
						{
							i0 = 1;
						}
						else
						{
							i0 = 0;
						}
						if (i0 == 0)
						{
							i1 = 0;
						}
					}
					if (npc._i_quest0 > 4)
					{
						if (npc._c_quest4 == null || (((npc._c_quest4.getX() - npc._i_ai1) * (npc._c_quest4.getX() - npc._i_ai1)) + ((npc._c_quest4.getY() - npc._i_ai2) * (npc._c_quest4.getY() - npc._i_ai2))) > (1500 * 1500))
						{
							i0 = 1;
						}
						else
						{
							i0 = 0;
						}
						if (i0 == 0)
						{
							i1 = 0;
						}
					}
					if (i1 == 1)
					{
						npc._i_quest0 = 0;
						i2 = (Rnd.get(14) + 1);
						if (i2 == 1)
						{
							npc._i_ai1 = 53950;
							npc._i_ai2 = 219860;
							npc._i_ai3 = -3488;
							npc._i_ai1 = (npc._i_ai1 + Rnd.get(650));
							npc._i_ai2 = (npc._i_ai2 + Rnd.get(650));
						}
						else if (i2 == 2)
						{
							npc._i_ai1 = 55980;
							npc._i_ai2 = 219820;
							npc._i_ai3 = -3488;
							npc._i_ai1 = (npc._i_ai1 + Rnd.get(650));
							npc._i_ai2 = (npc._i_ai2 + Rnd.get(650));
						}
						else if (i2 == 3)
						{
							npc._i_ai1 = 54950;
							npc._i_ai2 = 218790;
							npc._i_ai3 = -3488;
							npc._i_ai1 = (npc._i_ai1 + Rnd.get(650));
							npc._i_ai2 = (npc._i_ai2 + Rnd.get(650));
						}
						else if (i2 == 4)
						{
							npc._i_ai1 = 55970;
							npc._i_ai2 = 217770;
							npc._i_ai3 = -3488;
							npc._i_ai1 = (npc._i_ai1 + Rnd.get(650));
							npc._i_ai2 = (npc._i_ai2 + Rnd.get(650));
						}
						else if (i2 == 5)
						{
							npc._i_ai1 = 53930;
							npc._i_ai2 = 217760;
							npc._i_ai3 = -3488;
							npc._i_ai1 = (npc._i_ai1 + Rnd.get(650));
							npc._i_ai2 = (npc._i_ai2 + Rnd.get(650));
						}
						else if (i2 == 6)
						{
							npc._i_ai1 = 55970;
							npc._i_ai2 = 217770;
							npc._i_ai3 = -3216;
							npc._i_ai1 = (npc._i_ai1 + Rnd.get(650));
							npc._i_ai2 = (npc._i_ai2 + Rnd.get(650));
						}
						else if (i2 == 7)
						{
							npc._i_ai1 = 55980;
							npc._i_ai2 = 219920;
							npc._i_ai3 = -3216;
							npc._i_ai1 = (npc._i_ai1 + Rnd.get(650));
							npc._i_ai2 = (npc._i_ai2 + Rnd.get(650));
						}
						else if (i2 == 8)
						{
							npc._i_ai1 = 54960;
							npc._i_ai2 = 218790;
							npc._i_ai3 = -3216;
							npc._i_ai1 = (npc._i_ai1 + Rnd.get(650));
							npc._i_ai2 = (npc._i_ai2 + Rnd.get(650));
						}
						else if (i2 == 9)
						{
							npc._i_ai1 = 53950;
							npc._i_ai2 = 219860;
							npc._i_ai3 = -3216;
							npc._i_ai1 = (npc._i_ai1 + Rnd.get(650));
							npc._i_ai2 = (npc._i_ai2 + Rnd.get(650));
						}
						else if (i2 == 10)
						{
							npc._i_ai1 = 53930;
							npc._i_ai2 = 217760;
							npc._i_ai3 = -3216;
							npc._i_ai1 = (npc._i_ai1 + Rnd.get(650));
							npc._i_ai2 = (npc._i_ai2 + Rnd.get(650));
						}
						else if (i2 == 11)
						{
							npc._i_ai1 = 55970;
							npc._i_ai2 = 217770;
							npc._i_ai3 = -2944;
							npc._i_ai1 = (npc._i_ai1 + Rnd.get(650));
							npc._i_ai2 = (npc._i_ai2 + Rnd.get(650));
						}
						else if (i2 == 12)
						{
							npc._i_ai1 = 55980;
							npc._i_ai2 = 219920;
							npc._i_ai3 = -2944;
							npc._i_ai1 = (npc._i_ai1 + Rnd.get(650));
							npc._i_ai2 = (npc._i_ai2 + Rnd.get(650));
						}
						else if (i2 == 13)
						{
							npc._i_ai1 = 54960;
							npc._i_ai2 = 218790;
							npc._i_ai3 = -2944;
							npc._i_ai1 = (npc._i_ai1 + Rnd.get(650));
							npc._i_ai2 = (npc._i_ai2 + Rnd.get(650));
						}
						else if (i2 == 14)
						{
							npc._i_ai1 = 53950;
							npc._i_ai2 = 219860;
							npc._i_ai3 = -2944;
							npc._i_ai1 = (npc._i_ai1 + Rnd.get(650));
							npc._i_ai2 = (npc._i_ai2 + Rnd.get(650));
						}
						else if (i2 == 15)
						{
							npc._i_ai1 = 53930;
							npc._i_ai2 = 217760;
							npc._i_ai3 = -2944;
							npc._i_ai1 = (npc._i_ai1 + Rnd.get(650));
							npc._i_ai2 = (npc._i_ai2 + Rnd.get(650));
						}
						npc.getAI().addCastDesire(npc, 4222, 1, 10000000);
					}
				}
				
				if (Rnd.get(20) < 1 && npc._i_ai0 == 0)
				{
					npc._i_ai1 = npc.getX();
					npc._i_ai2 = npc.getY();
					npc._i_ai3 = npc.getZ();
				}
				
				if (currentIntention == IntentionType.ATTACK)
				{
					if (npc._i_quest1 == 0)
					{
						if (topDesireTarget != null)
						{
							npc._c_ai0 = topDesireTarget;
							npc._i_quest1 = 1;
						}
					}
					else
					{
						if (topDesireTarget != null)
						{
							if (npc._c_ai0 == topDesireTarget)
							{
								npc._i_quest1++;
							}
							else
							{
								npc._i_quest1 = 1;
								npc._c_ai0 = topDesireTarget;
							}
						}
					}
				}
				
				if (currentIntention == IntentionType.WANDER)
					npc._i_quest1 = 0;
				
				if (npc._i_quest1 > 5)
				{
					npc.getAI().getAggroList().stopHate(npc._c_ai0);
					npc._i_quest1 = 0;
				}
			}
			else if (getAbnormalLevel(npc, zakenNightToDay) == -1)
			{
				npc.getAI().addCastDesire(npc, zakenNightToDay, 10000000);
				npc._i_quest2 = 3;
			}
			if (getAbnormalLevel(npc, zakenRegeneration) == 11)
			{
				npc.getAI().addCastDesire(npc, 4242, 1, 10000000);
			}
			if (Rnd.get(40) < 1)
			{
				i2 = (Rnd.get(14) + 1);
				if (i2 == 1)
				{
					npc._i_ai1 = 53950;
					npc._i_ai2 = 219860;
					npc._i_ai3 = -3488;
					npc._i_ai1 = (npc._i_ai1 + Rnd.get(650));
					npc._i_ai2 = (npc._i_ai2 + Rnd.get(650));
				}
				else if (i2 == 2)
				{
					npc._i_ai1 = 55980;
					npc._i_ai2 = 219820;
					npc._i_ai3 = -3488;
					npc._i_ai1 = (npc._i_ai1 + Rnd.get(650));
					npc._i_ai2 = (npc._i_ai2 + Rnd.get(650));
				}
				else if (i2 == 3)
				{
					npc._i_ai1 = 54950;
					npc._i_ai2 = 218790;
					npc._i_ai3 = -3488;
					npc._i_ai1 = (npc._i_ai1 + Rnd.get(650));
					npc._i_ai2 = (npc._i_ai2 + Rnd.get(650));
				}
				else if (i2 == 4)
				{
					npc._i_ai1 = 55970;
					npc._i_ai2 = 217770;
					npc._i_ai3 = -3488;
					npc._i_ai1 = (npc._i_ai1 + Rnd.get(650));
					npc._i_ai2 = (npc._i_ai2 + Rnd.get(650));
				}
				else if (i2 == 5)
				{
					npc._i_ai1 = 53930;
					npc._i_ai2 = 217760;
					npc._i_ai3 = -3488;
					npc._i_ai1 = (npc._i_ai1 + Rnd.get(650));
					npc._i_ai2 = (npc._i_ai2 + Rnd.get(650));
				}
				else if (i2 == 6)
				{
					npc._i_ai1 = 55970;
					npc._i_ai2 = 217770;
					npc._i_ai3 = -3216;
					npc._i_ai1 = (npc._i_ai1 + Rnd.get(650));
					npc._i_ai2 = (npc._i_ai2 + Rnd.get(650));
				}
				else if (i2 == 7)
				{
					npc._i_ai1 = 55980;
					npc._i_ai2 = 219920;
					npc._i_ai3 = -3216;
					npc._i_ai1 = (npc._i_ai1 + Rnd.get(650));
					npc._i_ai2 = (npc._i_ai2 + Rnd.get(650));
				}
				else if (i2 == 8)
				{
					npc._i_ai1 = 54960;
					npc._i_ai2 = 218790;
					npc._i_ai3 = -3216;
					npc._i_ai1 = (npc._i_ai1 + Rnd.get(650));
					npc._i_ai2 = (npc._i_ai2 + Rnd.get(650));
				}
				else if (i2 == 9)
				{
					npc._i_ai1 = 53950;
					npc._i_ai2 = 219860;
					npc._i_ai3 = -3216;
					npc._i_ai1 = (npc._i_ai1 + Rnd.get(650));
					npc._i_ai2 = (npc._i_ai2 + Rnd.get(650));
				}
				else if (i2 == 10)
				{
					npc._i_ai1 = 53930;
					npc._i_ai2 = 217760;
					npc._i_ai3 = -3216;
					npc._i_ai1 = (npc._i_ai1 + Rnd.get(650));
					npc._i_ai2 = (npc._i_ai2 + Rnd.get(650));
				}
				else if (i2 == 11)
				{
					npc._i_ai1 = 55970;
					npc._i_ai2 = 217770;
					npc._i_ai3 = -2944;
					npc._i_ai1 = (npc._i_ai1 + Rnd.get(650));
					npc._i_ai2 = (npc._i_ai2 + Rnd.get(650));
				}
				else if (i2 == 12)
				{
					npc._i_ai1 = 55980;
					npc._i_ai2 = 219920;
					npc._i_ai3 = -2944;
					npc._i_ai1 = (npc._i_ai1 + Rnd.get(650));
					npc._i_ai2 = (npc._i_ai2 + Rnd.get(650));
				}
				else if (i2 == 13)
				{
					npc._i_ai1 = 54960;
					npc._i_ai2 = 218790;
					npc._i_ai3 = -2944;
					npc._i_ai1 = (npc._i_ai1 + Rnd.get(650));
					npc._i_ai2 = (npc._i_ai2 + Rnd.get(650));
				}
				else if (i2 == 14)
				{
					npc._i_ai1 = 53950;
					npc._i_ai2 = 219860;
					npc._i_ai3 = -2944;
					npc._i_ai1 = (npc._i_ai1 + Rnd.get(650));
					npc._i_ai2 = (npc._i_ai2 + Rnd.get(650));
				}
				else if (i2 == 15)
				{
					npc._i_ai1 = 53930;
					npc._i_ai2 = 217760;
					npc._i_ai3 = -2944;
					npc._i_ai1 = (npc._i_ai1 + Rnd.get(650));
					npc._i_ai2 = (npc._i_ai2 + Rnd.get(650));
				}
				npc.getAI().addCastDesire(npc, 4222, 1, 10000000);
			}
		}
		else if (name.equalsIgnoreCase("1002"))
		{
			npc._i_quest0 = 0;
			npc.getAI().addCastDesire(npc, 4222, 1, 10000000);
			npc._i_ai0 = 0;
		}
		else if (name.equalsIgnoreCase("1003"))
		{
			int i2 = 0;
			int i3 = 0;
			int i4 = 0;
			
			if (npc._i_ai4 == 1)
			{
				for (int i0 = 1; i0 <= 15; i0++)
				{
					if (i0 == 1)
					{
						i2 = 53950;
						i3 = 219860;
						i4 = -3488;
						i2 = (i2 + Rnd.get(650));
						i3 = (i3 + Rnd.get(650));
					}
					else if (i0 == 2)
					{
						i2 = 55980;
						i3 = 219820;
						i4 = -3488;
						i2 = (i2 + Rnd.get(650));
						i3 = (i3 + Rnd.get(650));
					}
					else if (i0 == 3)
					{
						i2 = 54950;
						i3 = 218790;
						i4 = -3488;
						i2 = (i2 + Rnd.get(650));
						i3 = (i3 + Rnd.get(650));
					}
					else if (i0 == 4)
					{
						i2 = 55970;
						i3 = 217770;
						i4 = -3488;
						i2 = (i2 + Rnd.get(650));
						i3 = (i3 + Rnd.get(650));
					}
					else if (i0 == 5)
					{
						i2 = 53930;
						i3 = 217760;
						i4 = -3488;
						i2 = (i2 + Rnd.get(650));
						i3 = (i3 + Rnd.get(650));
					}
					else if (i0 == 6)
					{
						i2 = 55970;
						i3 = 217770;
						i4 = -3216;
						i2 = (i2 + Rnd.get(650));
						i3 = (i3 + Rnd.get(650));
					}
					else if (i0 == 7)
					{
						i2 = 55980;
						i3 = 219920;
						i4 = -3216;
						i2 = (i2 + Rnd.get(650));
						i3 = (i3 + Rnd.get(650));
					}
					else if (i0 == 8)
					{
						i2 = 54960;
						i3 = 218790;
						i4 = -3216;
						i2 = (i2 + Rnd.get(650));
						i3 = (i3 + Rnd.get(650));
					}
					else if (i0 == 9)
					{
						i2 = 53950;
						i3 = 219860;
						i4 = -3216;
						i2 = (i2 + Rnd.get(650));
						i3 = (i3 + Rnd.get(650));
					}
					else if (i0 == 10)
					{
						i2 = 53930;
						i3 = 217760;
						i4 = -3216;
						i2 = (i2 + Rnd.get(650));
						i3 = (i3 + Rnd.get(650));
					}
					else if (i0 == 11)
					{
						i2 = 55970;
						i3 = 217770;
						i4 = -2944;
						i2 = (i2 + Rnd.get(650));
						i3 = (i3 + Rnd.get(650));
					}
					else if (i0 == 12)
					{
						i2 = 55980;
						i3 = 219920;
						i4 = -2944;
						i2 = (i2 + Rnd.get(650));
						i3 = (i3 + Rnd.get(650));
					}
					else if (i0 == 13)
					{
						i2 = 54960;
						i3 = 218790;
						i4 = -2944;
						i2 = (i2 + Rnd.get(650));
						i3 = (i3 + Rnd.get(650));
					}
					else if (i0 == 14)
					{
						i2 = 53950;
						i3 = 219860;
						i4 = -2944;
						i2 = (i2 + Rnd.get(650));
						i3 = (i3 + Rnd.get(650));
					}
					else if (i0 == 15)
					{
						i2 = 53930;
						i3 = 217760;
						i4 = -2944;
						i2 = (i2 + Rnd.get(650));
						i3 = (i3 + Rnd.get(650));
					}
					createOnePrivateEx(npc, 29026, i2, i3, i4, Rnd.get(360), 0, true);
				}
				
				npc._i_ai4 = 2;
				
				startQuestTimer("1003", npc, player, 1700);
			}
			else if (npc._i_ai4 == 2)
			{
				for (int i0 = 1; i0 <= 15; i0 = (i0 + 1))
				{
					if (i0 == 1)
					{
						i2 = 53950;
						i3 = 219860;
						i4 = -3488;
						i2 = (i2 + Rnd.get(650));
						i3 = (i3 + Rnd.get(650));
					}
					else if (i0 == 2)
					{
						i2 = 55980;
						i3 = 219820;
						i4 = -3488;
						i2 = (i2 + Rnd.get(650));
						i3 = (i3 + Rnd.get(650));
					}
					else if (i0 == 3)
					{
						i2 = 54950;
						i3 = 218790;
						i4 = -3488;
						i2 = (i2 + Rnd.get(650));
						i3 = (i3 + Rnd.get(650));
					}
					else if (i0 == 4)
					{
						i2 = 55970;
						i3 = 217770;
						i4 = -3488;
						i2 = (i2 + Rnd.get(650));
						i3 = (i3 + Rnd.get(650));
					}
					else if (i0 == 5)
					{
						i2 = 53930;
						i3 = 217760;
						i4 = -3488;
						i2 = (i2 + Rnd.get(650));
						i3 = (i3 + Rnd.get(650));
					}
					else if (i0 == 6)
					{
						i2 = 55970;
						i3 = 217770;
						i4 = -3216;
						i2 = (i2 + Rnd.get(650));
						i3 = (i3 + Rnd.get(650));
					}
					else if (i0 == 7)
					{
						i2 = 55980;
						i3 = 219920;
						i4 = -3216;
						i2 = (i2 + Rnd.get(650));
						i3 = (i3 + Rnd.get(650));
					}
					else if (i0 == 8)
					{
						i2 = 54960;
						i3 = 218790;
						i4 = -3216;
						i2 = (i2 + Rnd.get(650));
						i3 = (i3 + Rnd.get(650));
					}
					else if (i0 == 9)
					{
						i2 = 53950;
						i3 = 219860;
						i4 = -3216;
						i2 = (i2 + Rnd.get(650));
						i3 = (i3 + Rnd.get(650));
					}
					else if (i0 == 10)
					{
						i2 = 53930;
						i3 = 217760;
						i4 = -3216;
						i2 = (i2 + Rnd.get(650));
						i3 = (i3 + Rnd.get(650));
					}
					else if (i0 == 11)
					{
						i2 = 55970;
						i3 = 217770;
						i4 = -2944;
						i2 = (i2 + Rnd.get(650));
						i3 = (i3 + Rnd.get(650));
					}
					else if (i0 == 12)
					{
						i2 = 55980;
						i3 = 219920;
						i4 = -2944;
						i2 = (i2 + Rnd.get(650));
						i3 = (i3 + Rnd.get(650));
					}
					else if (i0 == 13)
					{
						i2 = 54960;
						i3 = 218790;
						i4 = -2944;
						i2 = (i2 + Rnd.get(650));
						i3 = (i3 + Rnd.get(650));
					}
					else if (i0 == 14)
					{
						i2 = 53950;
						i3 = 219860;
						i4 = -2944;
						i2 = (i2 + Rnd.get(650));
						i3 = (i3 + Rnd.get(650));
					}
					else if (i0 == 15)
					{
						i2 = 53930;
						i3 = 217760;
						i4 = -2944;
						i2 = (i2 + Rnd.get(650));
						i3 = (i3 + Rnd.get(650));
					}
					createOnePrivateEx(npc, 29023, i2, i3, i4, Rnd.get(360), 0, true);
				}
				
				npc._i_ai4 = 3;
				
				startQuestTimer("1003", npc, player, 1700);
			}
			else if (npc._i_ai4 == 3)
			{
				for (int i0 = 1; i0 <= 15; i0 = (i0 + 1))
				{
					for (int i1 = 1; i1 <= 2; i1 = (i1 + 1))
					{
						if (i0 == 1)
						{
							i2 = 53950;
							i3 = 219860;
							i4 = -3488;
							i2 = (i2 + Rnd.get(650));
							i3 = (i3 + Rnd.get(650));
						}
						else if (i0 == 2)
						{
							i2 = 55980;
							i3 = 219820;
							i4 = -3488;
							i2 = (i2 + Rnd.get(650));
							i3 = (i3 + Rnd.get(650));
						}
						else if (i0 == 3)
						{
							i2 = 54950;
							i3 = 218790;
							i4 = -3488;
							i2 = (i2 + Rnd.get(650));
							i3 = (i3 + Rnd.get(650));
						}
						else if (i0 == 4)
						{
							i2 = 55970;
							i3 = 217770;
							i4 = -3488;
							i2 = (i2 + Rnd.get(650));
							i3 = (i3 + Rnd.get(650));
						}
						else if (i0 == 5)
						{
							i2 = 53930;
							i3 = 217760;
							i4 = -3488;
							i2 = (i2 + Rnd.get(650));
							i3 = (i3 + Rnd.get(650));
						}
						else if (i0 == 6)
						{
							i2 = 55970;
							i3 = 217770;
							i4 = -3216;
							i2 = (i2 + Rnd.get(650));
							i3 = (i3 + Rnd.get(650));
						}
						else if (i0 == 7)
						{
							i2 = 55980;
							i3 = 219920;
							i4 = -3216;
							i2 = (i2 + Rnd.get(650));
							i3 = (i3 + Rnd.get(650));
						}
						else if (i0 == 8)
						{
							i2 = 54960;
							i3 = 218790;
							i4 = -3216;
							i2 = (i2 + Rnd.get(650));
							i3 = (i3 + Rnd.get(650));
						}
						else if (i0 == 9)
						{
							i2 = 53950;
							i3 = 219860;
							i4 = -3216;
							i2 = (i2 + Rnd.get(650));
							i3 = (i3 + Rnd.get(650));
						}
						else if (i0 == 10)
						{
							i2 = 53930;
							i3 = 217760;
							i4 = -3216;
							i2 = (i2 + Rnd.get(650));
							i3 = (i3 + Rnd.get(650));
						}
						else if (i0 == 11)
						{
							i2 = 55970;
							i3 = 217770;
							i4 = -2944;
							i2 = (i2 + Rnd.get(650));
							i3 = (i3 + Rnd.get(650));
						}
						else if (i0 == 12)
						{
							i2 = 55980;
							i3 = 219920;
							i4 = -2944;
							i2 = (i2 + Rnd.get(650));
							i3 = (i3 + Rnd.get(650));
						}
						else if (i0 == 13)
						{
							i2 = 54960;
							i3 = 218790;
							i4 = -2944;
							i2 = (i2 + Rnd.get(650));
							i3 = (i3 + Rnd.get(650));
						}
						else if (i0 == 14)
						{
							i2 = 53950;
							i3 = 219860;
							i4 = -2944;
							i2 = (i2 + Rnd.get(650));
							i3 = (i3 + Rnd.get(650));
						}
						else if (i0 == 15)
						{
							i2 = 53930;
							i3 = 217760;
							i4 = -2944;
							i2 = (i2 + Rnd.get(650));
							i3 = (i3 + Rnd.get(650));
						}
						createOnePrivateEx(npc, 29024, i2, i3, i4, Rnd.get(360), 0, true);
					}
				}
				npc._i_ai4 = 4;
				startQuestTimer("1003", npc, player, 1700);
			}
			else if (npc._i_ai4 == 4)
			{
				for (int i0 = 1; i0 <= 15; i0 = (i0 + 1))
				{
					for (int i1 = 1; i1 <= 5; i1 = (i1 + 1))
					{
						if (i0 == 1)
						{
							i2 = 53950;
							i3 = 219860;
							i4 = -3488;
							i2 = (i2 + Rnd.get(650));
							i3 = (i3 + Rnd.get(650));
						}
						else if (i0 == 2)
						{
							i2 = 55980;
							i3 = 219820;
							i4 = -3488;
							i2 = (i2 + Rnd.get(650));
							i3 = (i3 + Rnd.get(650));
						}
						else if (i0 == 3)
						{
							i2 = 54950;
							i3 = 218790;
							i4 = -3488;
							i2 = (i2 + Rnd.get(650));
							i3 = (i3 + Rnd.get(650));
						}
						else if (i0 == 4)
						{
							i2 = 55970;
							i3 = 217770;
							i4 = -3488;
							i2 = (i2 + Rnd.get(650));
							i3 = (i3 + Rnd.get(650));
						}
						else if (i0 == 5)
						{
							i2 = 53930;
							i3 = 217760;
							i4 = -3488;
							i2 = (i2 + Rnd.get(650));
							i3 = (i3 + Rnd.get(650));
						}
						else if (i0 == 6)
						{
							i2 = 55970;
							i3 = 217770;
							i4 = -3216;
							i2 = (i2 + Rnd.get(650));
							i3 = (i3 + Rnd.get(650));
						}
						else if (i0 == 7)
						{
							i2 = 55980;
							i3 = 219920;
							i4 = -3216;
							i2 = (i2 + Rnd.get(650));
							i3 = (i3 + Rnd.get(650));
						}
						else if (i0 == 8)
						{
							i2 = 54960;
							i3 = 218790;
							i4 = -3216;
							i2 = (i2 + Rnd.get(650));
							i3 = (i3 + Rnd.get(650));
						}
						else if (i0 == 9)
						{
							i2 = 53950;
							i3 = 219860;
							i4 = -3216;
							i2 = (i2 + Rnd.get(650));
							i3 = (i3 + Rnd.get(650));
						}
						else if (i0 == 10)
						{
							i2 = 53930;
							i3 = 217760;
							i4 = -3216;
							i2 = (i2 + Rnd.get(650));
							i3 = (i3 + Rnd.get(650));
						}
						else if (i0 == 11)
						{
							i2 = 55970;
							i3 = 217770;
							i4 = -2944;
							i2 = (i2 + Rnd.get(650));
							i3 = (i3 + Rnd.get(650));
						}
						else if (i0 == 12)
						{
							i2 = 55980;
							i3 = 219920;
							i4 = -2944;
							i2 = (i2 + Rnd.get(650));
							i3 = (i3 + Rnd.get(650));
						}
						else if (i0 == 13)
						{
							i2 = 54960;
							i3 = 218790;
							i4 = -2944;
							i2 = (i2 + Rnd.get(650));
							i3 = (i3 + Rnd.get(650));
						}
						else if (i0 == 14)
						{
							i2 = 53950;
							i3 = 219860;
							i4 = -2944;
							i2 = (i2 + Rnd.get(650));
							i3 = (i3 + Rnd.get(650));
						}
						else if (i0 == 15)
						{
							i2 = 53930;
							i3 = 217760;
							i4 = -2944;
							i2 = (i2 + Rnd.get(650));
							i3 = (i3 + Rnd.get(650));
						}
						createOnePrivateEx(npc, 29027, i2, i3, i4, Rnd.get(360), 0, true);
					}
				}
				npc._i_ai4 = 5;
				startQuestTimer("1003", npc, player, 1700);
			}
			else if (npc._i_ai4 == 5)
			{
				createOnePrivateEx(npc, 29023, 52675, 219371, -3290, Rnd.get(360), 0, true);
				createOnePrivateEx(npc, 29023, 52687, 219596, -3368, Rnd.get(360), 0, true);
				createOnePrivateEx(npc, 29023, 52672, 219740, -3418, Rnd.get(360), 0, true);
				createOnePrivateEx(npc, 29027, 52857, 219992, -3488, Rnd.get(360), 0, true);
				createOnePrivateEx(npc, 29026, 52959, 219997, -3488, Rnd.get(360), 0, true);
				createOnePrivateEx(npc, 29024, 53381, 220151, -3488, Rnd.get(360), 0, true);
				createOnePrivateEx(npc, 29026, 54236, 220948, -3488, Rnd.get(360), 0, true);
				createOnePrivateEx(npc, 29027, 54885, 220144, -3488, Rnd.get(360), 0, true);
				createOnePrivateEx(npc, 29027, 55264, 219860, -3488, Rnd.get(360), 0, true);
				createOnePrivateEx(npc, 29026, 55399, 220263, -3488, Rnd.get(360), 0, true);
				createOnePrivateEx(npc, 29027, 55679, 220129, -3488, Rnd.get(360), 0, true);
				createOnePrivateEx(npc, 29024, 56276, 220783, -3488, Rnd.get(360), 0, true);
				createOnePrivateEx(npc, 29024, 57173, 220234, -3488, Rnd.get(360), 0, true);
				createOnePrivateEx(npc, 29027, 56267, 218826, -3488, Rnd.get(360), 0, true);
				createOnePrivateEx(npc, 29023, 56294, 219482, -3488, Rnd.get(360), 0, true);
				createOnePrivateEx(npc, 29026, 56094, 219113, -3488, Rnd.get(360), 0, true);
				createOnePrivateEx(npc, 29023, 56364, 218967, -3488, Rnd.get(360), 0, true);
				createOnePrivateEx(npc, 29027, 57113, 218079, -3488, Rnd.get(360), 0, true);
				createOnePrivateEx(npc, 29023, 56186, 217153, -3488, Rnd.get(360), 0, true);
				createOnePrivateEx(npc, 29027, 55440, 218081, -3488, Rnd.get(360), 0, true);
				createOnePrivateEx(npc, 29026, 55202, 217940, -3488, Rnd.get(360), 0, true);
				createOnePrivateEx(npc, 29027, 55225, 218236, -3488, Rnd.get(360), 0, true);
				createOnePrivateEx(npc, 29027, 54973, 218075, -3488, Rnd.get(360), 0, true);
				createOnePrivateEx(npc, 29026, 53412, 218077, -3488, Rnd.get(360), 0, true);
				createOnePrivateEx(npc, 29024, 54226, 218797, -3488, Rnd.get(360), 0, true);
				createOnePrivateEx(npc, 29024, 54394, 219067, -3488, Rnd.get(360), 0, true);
				createOnePrivateEx(npc, 29027, 54139, 219253, -3488, Rnd.get(360), 0, true);
				createOnePrivateEx(npc, 29023, 54262, 219480, -3488, Rnd.get(360), 0, true);
				npc._i_ai4 = 6;
				startQuestTimer("1003", npc, player, 1700);
			}
			else if (npc._i_ai4 == 6)
			{
				createOnePrivateEx(npc, 29027, 53412, 218077, -3488, Rnd.get(360), 0, true);
				createOnePrivateEx(npc, 29024, 54413, 217132, -3488, Rnd.get(360), 0, true);
				createOnePrivateEx(npc, 29023, 54841, 217132, -3488, Rnd.get(360), 0, true);
				createOnePrivateEx(npc, 29023, 55372, 217128, -3343, Rnd.get(360), 0, true);
				createOnePrivateEx(npc, 29023, 55893, 217122, -3488, Rnd.get(360), 0, true);
				createOnePrivateEx(npc, 29026, 56282, 217237, -3216, Rnd.get(360), 0, true);
				createOnePrivateEx(npc, 29024, 56963, 218080, -3216, Rnd.get(360), 0, true);
				createOnePrivateEx(npc, 29027, 56267, 218826, -3216, Rnd.get(360), 0, true);
				createOnePrivateEx(npc, 29023, 56294, 219482, -3216, Rnd.get(360), 0, true);
				createOnePrivateEx(npc, 29026, 56094, 219113, -3216, Rnd.get(360), 0, true);
				createOnePrivateEx(npc, 29023, 56364, 218967, -3216, Rnd.get(360), 0, true);
				createOnePrivateEx(npc, 29024, 56276, 220783, -3216, Rnd.get(360), 0, true);
				createOnePrivateEx(npc, 29024, 57173, 220234, -3216, Rnd.get(360), 0, true);
				createOnePrivateEx(npc, 29027, 54885, 220144, -3216, Rnd.get(360), 0, true);
				createOnePrivateEx(npc, 29027, 55264, 219860, -3216, Rnd.get(360), 0, true);
				createOnePrivateEx(npc, 29026, 55399, 220263, -3216, Rnd.get(360), 0, true);
				createOnePrivateEx(npc, 29027, 55679, 220129, -3216, Rnd.get(360), 0, true);
				createOnePrivateEx(npc, 29026, 54236, 220948, -3216, Rnd.get(360), 0, true);
				createOnePrivateEx(npc, 29026, 54464, 219095, -3216, Rnd.get(360), 0, true);
				createOnePrivateEx(npc, 29024, 54226, 218797, -3216, Rnd.get(360), 0, true);
				createOnePrivateEx(npc, 29024, 54394, 219067, -3216, Rnd.get(360), 0, true);
				createOnePrivateEx(npc, 29027, 54139, 219253, -3216, Rnd.get(360), 0, true);
				createOnePrivateEx(npc, 29023, 54262, 219480, -3216, Rnd.get(360), 0, true);
				createOnePrivateEx(npc, 29026, 53412, 218077, -3216, Rnd.get(360), 0, true);
				createOnePrivateEx(npc, 29027, 55440, 218081, -3216, Rnd.get(360), 0, true);
				createOnePrivateEx(npc, 29026, 55202, 217940, -3216, Rnd.get(360), 0, true);
				createOnePrivateEx(npc, 29027, 55225, 218236, -3216, Rnd.get(360), 0, true);
				createOnePrivateEx(npc, 29027, 54973, 218075, -3216, Rnd.get(360), 0, true);
				npc._i_ai4 = 7;
				startQuestTimer("1003", npc, player, 1700);
			}
			else if (npc._i_ai4 == 7)
			{
				createOnePrivateEx(npc, 29027, 54228, 217504, -3216, Rnd.get(360), 0, true);
				createOnePrivateEx(npc, 29024, 54181, 217168, -3216, Rnd.get(360), 0, true);
				createOnePrivateEx(npc, 29023, 54714, 217123, -3168, Rnd.get(360), 0, true);
				createOnePrivateEx(npc, 29023, 55298, 217127, -3073, Rnd.get(360), 0, true);
				createOnePrivateEx(npc, 29023, 55787, 217130, -2993, Rnd.get(360), 0, true);
				createOnePrivateEx(npc, 29026, 56284, 217216, -2944, Rnd.get(360), 0, true);
				createOnePrivateEx(npc, 29024, 56963, 218080, -2944, Rnd.get(360), 0, true);
				createOnePrivateEx(npc, 29027, 56267, 218826, -2944, Rnd.get(360), 0, true);
				createOnePrivateEx(npc, 29023, 56294, 219482, -2944, Rnd.get(360), 0, true);
				createOnePrivateEx(npc, 29026, 56094, 219113, -2944, Rnd.get(360), 0, true);
				createOnePrivateEx(npc, 29023, 56364, 218967, -2944, Rnd.get(360), 0, true);
				createOnePrivateEx(npc, 29024, 56276, 220783, -2944, Rnd.get(360), 0, true);
				createOnePrivateEx(npc, 29024, 57173, 220234, -2944, Rnd.get(360), 0, true);
				createOnePrivateEx(npc, 29027, 54885, 220144, -2944, Rnd.get(360), 0, true);
				createOnePrivateEx(npc, 29027, 55264, 219860, -2944, Rnd.get(360), 0, true);
				createOnePrivateEx(npc, 29026, 55399, 220263, -2944, Rnd.get(360), 0, true);
				createOnePrivateEx(npc, 29027, 55679, 220129, -2944, Rnd.get(360), 0, true);
				createOnePrivateEx(npc, 29026, 54236, 220948, -2944, Rnd.get(360), 0, true);
				createOnePrivateEx(npc, 29026, 54464, 219095, -2944, Rnd.get(360), 0, true);
				createOnePrivateEx(npc, 29024, 54226, 218797, -2944, Rnd.get(360), 0, true);
				createOnePrivateEx(npc, 29024, 54394, 219067, -2944, Rnd.get(360), 0, true);
				createOnePrivateEx(npc, 29027, 54139, 219253, -2944, Rnd.get(360), 0, true);
				createOnePrivateEx(npc, 29023, 54262, 219480, -2944, Rnd.get(360), 0, true);
				createOnePrivateEx(npc, 29026, 53412, 218077, -2944, Rnd.get(360), 0, true);
				createOnePrivateEx(npc, 29026, 54280, 217200, -2944, Rnd.get(360), 0, true);
				createOnePrivateEx(npc, 29027, 55440, 218081, -2944, Rnd.get(360), 0, true);
				createOnePrivateEx(npc, 29026, 55202, 217940, -2944, Rnd.get(360), 0, true);
				createOnePrivateEx(npc, 29027, 55225, 218236, -2944, Rnd.get(360), 0, true);
				createOnePrivateEx(npc, 29027, 54973, 218075, -2944, Rnd.get(360), 0, true);
				
				npc._i_ai4 = 8;
			}
		}
		
		return null;
	}
	
	@Override
	public void onUseSkillFinished(Npc npc, Creature creature, L2Skill skill, boolean success)
	{
		final L2Skill instantMove = SkillTable.getInstance().getInfo(4222, 1);
		final L2Skill scatterEnemy = SkillTable.getInstance().getInfo(4216, 1);
		final L2Skill unk1 = SkillTable.getInstance().getInfo(4217, 1);
		
		int i0 = 0;
		int i1 = 0;
		int i2 = 0;
		int i3 = 0;
		int i4 = 0;
		
		if (skill == instantMove)
		{
			npc.teleportTo(npc._i_ai1, npc._i_ai2, npc._i_ai3, 0);
			npc.removeAllAttackDesire();
		}
		else if (skill == scatterEnemy)
		{
			i1 = (Rnd.get(14) + 1);
			if (i1 == 1)
			{
				i2 = 53950;
				i3 = 219860;
				i4 = -3488;
				i2 = (i2 + Rnd.get(650));
				i3 = (i3 + Rnd.get(650));
			}
			else if (i1 == 2)
			{
				i2 = 55980;
				i3 = 219820;
				i4 = -3488;
				i2 = (i2 + Rnd.get(650));
				i3 = (i3 + Rnd.get(650));
			}
			else if (i1 == 3)
			{
				i2 = 54950;
				i3 = 218790;
				i4 = -3488;
				i2 = (i2 + Rnd.get(650));
				i3 = (i3 + Rnd.get(650));
			}
			else if (i1 == 4)
			{
				i2 = 55970;
				i3 = 217770;
				i4 = -3488;
				i2 = (i2 + Rnd.get(650));
				i3 = (i3 + Rnd.get(650));
			}
			else if (i1 == 5)
			{
				i2 = 53930;
				i3 = 217760;
				i4 = -3488;
				i2 = (i2 + Rnd.get(650));
				i3 = (i3 + Rnd.get(650));
			}
			else if (i1 == 6)
			{
				i2 = 55970;
				i3 = 217770;
				i4 = -3216;
				i2 = (i2 + Rnd.get(650));
				i3 = (i3 + Rnd.get(650));
			}
			else if (i1 == 7)
			{
				i2 = 55980;
				i3 = 219920;
				i4 = -3216;
				i2 = (i2 + Rnd.get(650));
				i3 = (i3 + Rnd.get(650));
			}
			else if (i1 == 8)
			{
				i2 = 54960;
				i3 = 218790;
				i4 = -3216;
				i2 = (i2 + Rnd.get(650));
				i3 = (i3 + Rnd.get(650));
			}
			else if (i1 == 9)
			{
				i2 = 53950;
				i3 = 219860;
				i4 = -3216;
				i2 = (i2 + Rnd.get(650));
				i3 = (i3 + Rnd.get(650));
			}
			else if (i1 == 10)
			{
				i2 = 53930;
				i3 = 217760;
				i4 = -3216;
				i2 = (i2 + Rnd.get(650));
				i3 = (i3 + Rnd.get(650));
			}
			else if (i1 == 11)
			{
				i2 = 55970;
				i3 = 217770;
				i4 = -2944;
				i2 = (i2 + Rnd.get(650));
				i3 = (i3 + Rnd.get(650));
			}
			else if (i1 == 12)
			{
				i2 = 55980;
				i3 = 219920;
				i4 = -2944;
				i2 = (i2 + Rnd.get(650));
				i3 = (i3 + Rnd.get(650));
			}
			else if (i1 == 13)
			{
				i2 = 54960;
				i3 = 218790;
				i4 = -2944;
				i2 = (i2 + Rnd.get(650));
				i3 = (i3 + Rnd.get(650));
			}
			else if (i1 == 14)
			{
				i2 = 53950;
				i3 = 219860;
				i4 = -2944;
				i2 = (i2 + Rnd.get(650));
				i3 = (i3 + Rnd.get(650));
			}
			else if (i1 == 15)
			{
				i2 = 53930;
				i3 = 217760;
				i4 = -2944;
				i2 = (i2 + Rnd.get(650));
				i3 = (i3 + Rnd.get(650));
			}
			creature.teleportTo(i2, i3, i4, 0);
			npc.getAI().getAggroList().stopHate(creature);
		}
		else if (skill == unk1)
		{
			i1 = (Rnd.get(14) + 1);
			if (i1 == 1)
			{
				i2 = 53950;
				i3 = 219860;
				i4 = -3488;
				i2 = (i2 + Rnd.get(650));
				i3 = (i3 + Rnd.get(650));
			}
			else if (i1 == 2)
			{
				i2 = 55980;
				i3 = 219820;
				i4 = -3488;
				i2 = (i2 + Rnd.get(650));
				i3 = (i3 + Rnd.get(650));
			}
			else if (i1 == 3)
			{
				i2 = 54950;
				i3 = 218790;
				i4 = -3488;
				i2 = (i2 + Rnd.get(650));
				i3 = (i3 + Rnd.get(650));
			}
			else if (i1 == 4)
			{
				i2 = 55970;
				i3 = 217770;
				i4 = -3488;
				i2 = (i2 + Rnd.get(650));
				i3 = (i3 + Rnd.get(650));
			}
			else if (i1 == 5)
			{
				i2 = 53930;
				i3 = 217760;
				i4 = -3488;
				i2 = (i2 + Rnd.get(650));
				i3 = (i3 + Rnd.get(650));
			}
			else if (i1 == 6)
			{
				i2 = 55970;
				i3 = 217770;
				i4 = -3216;
				i2 = (i2 + Rnd.get(650));
				i3 = (i3 + Rnd.get(650));
			}
			else if (i1 == 7)
			{
				i2 = 55980;
				i3 = 219920;
				i4 = -3216;
				i2 = (i2 + Rnd.get(650));
				i3 = (i3 + Rnd.get(650));
			}
			else if (i1 == 8)
			{
				i2 = 54960;
				i3 = 218790;
				i4 = -3216;
				i2 = (i2 + Rnd.get(650));
				i3 = (i3 + Rnd.get(650));
			}
			else if (i1 == 9)
			{
				i2 = 53950;
				i3 = 219860;
				i4 = -3216;
				i2 = (i2 + Rnd.get(650));
				i3 = (i3 + Rnd.get(650));
			}
			else if (i1 == 10)
			{
				i2 = 53930;
				i3 = 217760;
				i4 = -3216;
				i2 = (i2 + Rnd.get(650));
				i3 = (i3 + Rnd.get(650));
			}
			else if (i1 == 11)
			{
				i2 = 55970;
				i3 = 217770;
				i4 = -2944;
				i2 = (i2 + Rnd.get(650));
				i3 = (i3 + Rnd.get(650));
			}
			else if (i1 == 12)
			{
				i2 = 55980;
				i3 = 219920;
				i4 = -2944;
				i2 = (i2 + Rnd.get(650));
				i3 = (i3 + Rnd.get(650));
			}
			else if (i1 == 13)
			{
				i2 = 54960;
				i3 = 218790;
				i4 = -2944;
				i2 = (i2 + Rnd.get(650));
				i3 = (i3 + Rnd.get(650));
			}
			else if (i1 == 14)
			{
				i2 = 53950;
				i3 = 219860;
				i4 = -2944;
				i2 = (i2 + Rnd.get(650));
				i3 = (i3 + Rnd.get(650));
			}
			else if (i1 == 15)
			{
				i2 = 53930;
				i3 = 217760;
				i4 = -2944;
				i2 = (i2 + Rnd.get(650));
				i3 = (i3 + Rnd.get(650));
			}
			
			creature.teleportTo(i2, i3, i4, 0);
			npc.getAI().getAggroList().stopHate(creature);
			
			if (npc._c_quest0 != null && npc._i_quest0 > 0 && npc._c_quest0 != creature && npc._c_quest0.getZ() > (creature.getZ() - 100) && npc._c_quest0.getZ() < (creature.getZ() + 100))
			{
				if ((((npc._c_quest0.getX() - creature.getX()) * (npc._c_quest0.getX() - creature.getX())) + ((npc._c_quest0.getY() - creature.getY()) * (npc._c_quest0.getY() - creature.getY()))) > (250 * 250))
				{
					i0 = 1;
				}
				else
				{
					i0 = 0;
				}
				if (i0 == 0)
				{
					i1 = (Rnd.get(14) + 1);
					if (i1 == 1)
					{
						i2 = 53950;
						i3 = 219860;
						i4 = -3488;
						i2 = (i2 + Rnd.get(650));
						i3 = (i3 + Rnd.get(650));
					}
					else if (i1 == 2)
					{
						i2 = 55980;
						i3 = 219820;
						i4 = -3488;
						i2 = (i2 + Rnd.get(650));
						i3 = (i3 + Rnd.get(650));
					}
					else if (i1 == 3)
					{
						i2 = 54950;
						i3 = 218790;
						i4 = -3488;
						i2 = (i2 + Rnd.get(650));
						i3 = (i3 + Rnd.get(650));
					}
					else if (i1 == 4)
					{
						i2 = 55970;
						i3 = 217770;
						i4 = -3488;
						i2 = (i2 + Rnd.get(650));
						i3 = (i3 + Rnd.get(650));
					}
					else if (i1 == 5)
					{
						i2 = 53930;
						i3 = 217760;
						i4 = -3488;
						i2 = (i2 + Rnd.get(650));
						i3 = (i3 + Rnd.get(650));
					}
					else if (i1 == 6)
					{
						i2 = 55970;
						i3 = 217770;
						i4 = -3216;
						i2 = (i2 + Rnd.get(650));
						i3 = (i3 + Rnd.get(650));
					}
					else if (i1 == 7)
					{
						i2 = 55980;
						i3 = 219920;
						i4 = -3216;
						i2 = (i2 + Rnd.get(650));
						i3 = (i3 + Rnd.get(650));
					}
					else if (i1 == 8)
					{
						i2 = 54960;
						i3 = 218790;
						i4 = -3216;
						i2 = (i2 + Rnd.get(650));
						i3 = (i3 + Rnd.get(650));
					}
					else if (i1 == 9)
					{
						i2 = 53950;
						i3 = 219860;
						i4 = -3216;
						i2 = (i2 + Rnd.get(650));
						i3 = (i3 + Rnd.get(650));
					}
					else if (i1 == 10)
					{
						i2 = 53930;
						i3 = 217760;
						i4 = -3216;
						i2 = (i2 + Rnd.get(650));
						i3 = (i3 + Rnd.get(650));
					}
					else if (i1 == 11)
					{
						i2 = 55970;
						i3 = 217770;
						i4 = -2944;
						i2 = (i2 + Rnd.get(650));
						i3 = (i3 + Rnd.get(650));
					}
					else if (i1 == 12)
					{
						i2 = 55980;
						i3 = 219920;
						i4 = -2944;
						i2 = (i2 + Rnd.get(650));
						i3 = (i3 + Rnd.get(650));
					}
					else if (i1 == 13)
					{
						i2 = 54960;
						i3 = 218790;
						i4 = -2944;
						i2 = (i2 + Rnd.get(650));
						i3 = (i3 + Rnd.get(650));
					}
					else if (i1 == 14)
					{
						i2 = 53950;
						i3 = 219860;
						i4 = -2944;
						i2 = (i2 + Rnd.get(650));
						i3 = (i3 + Rnd.get(650));
					}
					else if (i1 == 15)
					{
						i2 = 53930;
						i3 = 217760;
						i4 = -2944;
						i2 = (i2 + Rnd.get(650));
						i3 = (i3 + Rnd.get(650));
					}
					npc._c_quest0.teleportTo(i2, i3, i4, 0);
					npc.getAI().getAggroList().stopHate(npc._c_quest0);
				}
			}
			if (npc._c_quest1 != null && npc._i_quest0 > 1 && npc._c_quest1 != creature && npc._c_quest1.getZ() > (creature.getZ() - 100) && npc._c_quest1.getZ() < (creature.getZ() + 100))
			{
				if ((((npc._c_quest1.getX() - creature.getX()) * (npc._c_quest1.getX() - creature.getX())) + ((npc._c_quest1.getY() - creature.getY()) * (npc._c_quest1.getY() - creature.getY()))) > (250 * 250))
				{
					i0 = 1;
				}
				else
				{
					i0 = 0;
				}
				if (i0 == 0)
				{
					i1 = (Rnd.get(14) + 1);
					if (i1 == 1)
					{
						i2 = 53950;
						i3 = 219860;
						i4 = -3488;
						i2 = (i2 + Rnd.get(650));
						i3 = (i3 + Rnd.get(650));
					}
					else if (i1 == 2)
					{
						i2 = 55980;
						i3 = 219820;
						i4 = -3488;
						i2 = (i2 + Rnd.get(650));
						i3 = (i3 + Rnd.get(650));
					}
					else if (i1 == 3)
					{
						i2 = 54950;
						i3 = 218790;
						i4 = -3488;
						i2 = (i2 + Rnd.get(650));
						i3 = (i3 + Rnd.get(650));
					}
					else if (i1 == 4)
					{
						i2 = 55970;
						i3 = 217770;
						i4 = -3488;
						i2 = (i2 + Rnd.get(650));
						i3 = (i3 + Rnd.get(650));
					}
					else if (i1 == 5)
					{
						i2 = 53930;
						i3 = 217760;
						i4 = -3488;
						i2 = (i2 + Rnd.get(650));
						i3 = (i3 + Rnd.get(650));
					}
					else if (i1 == 6)
					{
						i2 = 55970;
						i3 = 217770;
						i4 = -3216;
						i2 = (i2 + Rnd.get(650));
						i3 = (i3 + Rnd.get(650));
					}
					else if (i1 == 7)
					{
						i2 = 55980;
						i3 = 219920;
						i4 = -3216;
						i2 = (i2 + Rnd.get(650));
						i3 = (i3 + Rnd.get(650));
					}
					else if (i1 == 8)
					{
						i2 = 54960;
						i3 = 218790;
						i4 = -3216;
						i2 = (i2 + Rnd.get(650));
						i3 = (i3 + Rnd.get(650));
					}
					else if (i1 == 9)
					{
						i2 = 53950;
						i3 = 219860;
						i4 = -3216;
						i2 = (i2 + Rnd.get(650));
						i3 = (i3 + Rnd.get(650));
					}
					else if (i1 == 10)
					{
						i2 = 53930;
						i3 = 217760;
						i4 = -3216;
						i2 = (i2 + Rnd.get(650));
						i3 = (i3 + Rnd.get(650));
					}
					else if (i1 == 11)
					{
						i2 = 55970;
						i3 = 217770;
						i4 = -2944;
						i2 = (i2 + Rnd.get(650));
						i3 = (i3 + Rnd.get(650));
					}
					else if (i1 == 12)
					{
						i2 = 55980;
						i3 = 219920;
						i4 = -2944;
						i2 = (i2 + Rnd.get(650));
						i3 = (i3 + Rnd.get(650));
					}
					else if (i1 == 13)
					{
						i2 = 54960;
						i3 = 218790;
						i4 = -2944;
						i2 = (i2 + Rnd.get(650));
						i3 = (i3 + Rnd.get(650));
					}
					else if (i1 == 14)
					{
						i2 = 53950;
						i3 = 219860;
						i4 = -2944;
						i2 = (i2 + Rnd.get(650));
						i3 = (i3 + Rnd.get(650));
					}
					else if (i1 == 15)
					{
						i2 = 53930;
						i3 = 217760;
						i4 = -2944;
						i2 = (i2 + Rnd.get(650));
						i3 = (i3 + Rnd.get(650));
					}
					npc._c_quest1.teleportTo(i2, i3, i4, 0);
					npc.getAI().getAggroList().stopHate(npc._c_quest1);
				}
			}
			if (npc._c_quest2 != null && npc._i_quest0 > 2 && npc._c_quest2 != creature && npc._c_quest2.getZ() > (creature.getZ() - 100) && npc._c_quest2.getZ() < (creature.getZ() + 100))
			{
				if ((((npc._c_quest2.getX() - creature.getX()) * (npc._c_quest2.getX() - creature.getX())) + ((npc._c_quest2.getY() - creature.getY()) * (npc._c_quest2.getY() - creature.getY()))) > (250 * 250))
				{
					i0 = 1;
				}
				else
				{
					i0 = 0;
				}
				if (i0 == 0)
				{
					i1 = (Rnd.get(14) + 1);
					if (i1 == 1)
					{
						i2 = 53950;
						i3 = 219860;
						i4 = -3488;
						i2 = (i2 + Rnd.get(650));
						i3 = (i3 + Rnd.get(650));
					}
					else if (i1 == 2)
					{
						i2 = 55980;
						i3 = 219820;
						i4 = -3488;
						i2 = (i2 + Rnd.get(650));
						i3 = (i3 + Rnd.get(650));
					}
					else if (i1 == 3)
					{
						i2 = 54950;
						i3 = 218790;
						i4 = -3488;
						i2 = (i2 + Rnd.get(650));
						i3 = (i3 + Rnd.get(650));
					}
					else if (i1 == 4)
					{
						i2 = 55970;
						i3 = 217770;
						i4 = -3488;
						i2 = (i2 + Rnd.get(650));
						i3 = (i3 + Rnd.get(650));
					}
					else if (i1 == 5)
					{
						i2 = 53930;
						i3 = 217760;
						i4 = -3488;
						i2 = (i2 + Rnd.get(650));
						i3 = (i3 + Rnd.get(650));
					}
					else if (i1 == 6)
					{
						i2 = 55970;
						i3 = 217770;
						i4 = -3216;
						i2 = (i2 + Rnd.get(650));
						i3 = (i3 + Rnd.get(650));
					}
					else if (i1 == 7)
					{
						i2 = 55980;
						i3 = 219920;
						i4 = -3216;
						i2 = (i2 + Rnd.get(650));
						i3 = (i3 + Rnd.get(650));
					}
					else if (i1 == 8)
					{
						i2 = 54960;
						i3 = 218790;
						i4 = -3216;
						i2 = (i2 + Rnd.get(650));
						i3 = (i3 + Rnd.get(650));
					}
					else if (i1 == 9)
					{
						i2 = 53950;
						i3 = 219860;
						i4 = -3216;
						i2 = (i2 + Rnd.get(650));
						i3 = (i3 + Rnd.get(650));
					}
					else if (i1 == 10)
					{
						i2 = 53930;
						i3 = 217760;
						i4 = -3216;
						i2 = (i2 + Rnd.get(650));
						i3 = (i3 + Rnd.get(650));
					}
					else if (i1 == 11)
					{
						i2 = 55970;
						i3 = 217770;
						i4 = -2944;
						i2 = (i2 + Rnd.get(650));
						i3 = (i3 + Rnd.get(650));
					}
					else if (i1 == 12)
					{
						i2 = 55980;
						i3 = 219920;
						i4 = -2944;
						i2 = (i2 + Rnd.get(650));
						i3 = (i3 + Rnd.get(650));
					}
					else if (i1 == 13)
					{
						i2 = 54960;
						i3 = 218790;
						i4 = -2944;
						i2 = (i2 + Rnd.get(650));
						i3 = (i3 + Rnd.get(650));
					}
					else if (i1 == 14)
					{
						i2 = 53950;
						i3 = 219860;
						i4 = -2944;
						i2 = (i2 + Rnd.get(650));
						i3 = (i3 + Rnd.get(650));
					}
					else if (i1 == 15)
					{
						i2 = 53930;
						i3 = 217760;
						i4 = -2944;
						i2 = (i2 + Rnd.get(650));
						i3 = (i3 + Rnd.get(650));
					}
					npc._c_quest2.teleportTo(i2, i3, i4, 0);
					npc.getAI().getAggroList().stopHate(npc._c_quest2);
				}
			}
			if (npc._c_quest3 != null && npc._i_quest0 > 3 && npc._c_quest3 != creature && npc._c_quest3.getZ() > (creature.getZ() - 100) && npc._c_quest3.getZ() < (creature.getZ() + 100))
			{
				if ((((npc._c_quest3.getX() - creature.getX()) * (npc._c_quest3.getX() - creature.getX())) + ((npc._c_quest3.getY() - creature.getY()) * (npc._c_quest3.getY() - creature.getY()))) > (250 * 250))
				{
					i0 = 1;
				}
				else
				{
					i0 = 0;
				}
				if (i0 == 0)
				{
					i1 = (Rnd.get(14) + 1);
					if (i1 == 1)
					{
						i2 = 53950;
						i3 = 219860;
						i4 = -3488;
						i2 = (i2 + Rnd.get(650));
						i3 = (i3 + Rnd.get(650));
					}
					else if (i1 == 2)
					{
						i2 = 55980;
						i3 = 219820;
						i4 = -3488;
						i2 = (i2 + Rnd.get(650));
						i3 = (i3 + Rnd.get(650));
					}
					else if (i1 == 3)
					{
						i2 = 54950;
						i3 = 218790;
						i4 = -3488;
						i2 = (i2 + Rnd.get(650));
						i3 = (i3 + Rnd.get(650));
					}
					else if (i1 == 4)
					{
						i2 = 55970;
						i3 = 217770;
						i4 = -3488;
						i2 = (i2 + Rnd.get(650));
						i3 = (i3 + Rnd.get(650));
					}
					else if (i1 == 5)
					{
						i2 = 53930;
						i3 = 217760;
						i4 = -3488;
						i2 = (i2 + Rnd.get(650));
						i3 = (i3 + Rnd.get(650));
					}
					else if (i1 == 6)
					{
						i2 = 55970;
						i3 = 217770;
						i4 = -3216;
						i2 = (i2 + Rnd.get(650));
						i3 = (i3 + Rnd.get(650));
					}
					else if (i1 == 7)
					{
						i2 = 55980;
						i3 = 219920;
						i4 = -3216;
						i2 = (i2 + Rnd.get(650));
						i3 = (i3 + Rnd.get(650));
					}
					else if (i1 == 8)
					{
						i2 = 54960;
						i3 = 218790;
						i4 = -3216;
						i2 = (i2 + Rnd.get(650));
						i3 = (i3 + Rnd.get(650));
					}
					else if (i1 == 9)
					{
						i2 = 53950;
						i3 = 219860;
						i4 = -3216;
						i2 = (i2 + Rnd.get(650));
						i3 = (i3 + Rnd.get(650));
					}
					else if (i1 == 10)
					{
						i2 = 53930;
						i3 = 217760;
						i4 = -3216;
						i2 = (i2 + Rnd.get(650));
						i3 = (i3 + Rnd.get(650));
					}
					else if (i1 == 11)
					{
						i2 = 55970;
						i3 = 217770;
						i4 = -2944;
						i2 = (i2 + Rnd.get(650));
						i3 = (i3 + Rnd.get(650));
					}
					else if (i1 == 12)
					{
						i2 = 55980;
						i3 = 219920;
						i4 = -2944;
						i2 = (i2 + Rnd.get(650));
						i3 = (i3 + Rnd.get(650));
					}
					else if (i1 == 13)
					{
						i2 = 54960;
						i3 = 218790;
						i4 = -2944;
						i2 = (i2 + Rnd.get(650));
						i3 = (i3 + Rnd.get(650));
					}
					else if (i1 == 14)
					{
						i2 = 53950;
						i3 = 219860;
						i4 = -2944;
						i2 = (i2 + Rnd.get(650));
						i3 = (i3 + Rnd.get(650));
					}
					else if (i1 == 15)
					{
						i2 = 53930;
						i3 = 217760;
						i4 = -2944;
						i2 = (i2 + Rnd.get(650));
						i3 = (i3 + Rnd.get(650));
					}
					npc._c_quest3.teleportTo(i2, i3, i4, 0);
					npc.getAI().getAggroList().stopHate(npc._c_quest3);
				}
			}
			if (npc._c_quest4 != null && npc._i_quest0 > 4 && npc._c_quest4 != creature && npc._c_quest4.getZ() > (creature.getZ() - 100) && npc._c_quest4.getZ() < (creature.getZ() + 100))
			{
				if ((((npc._c_quest4.getX() - creature.getX()) * (npc._c_quest4.getX() - creature.getX())) + ((npc._c_quest4.getY() - creature.getY()) * (npc._c_quest4.getY() - creature.getY()))) > (250 * 250))
				{
					i0 = 1;
				}
				else
				{
					i0 = 0;
				}
				if (i0 == 0)
				{
					i1 = (Rnd.get(14) + 1);
					if (i1 == 1)
					{
						i2 = 53950;
						i3 = 219860;
						i4 = -3488;
						i2 = (i2 + Rnd.get(650));
						i3 = (i3 + Rnd.get(650));
					}
					else if (i1 == 2)
					{
						i2 = 55980;
						i3 = 219820;
						i4 = -3488;
						i2 = (i2 + Rnd.get(650));
						i3 = (i3 + Rnd.get(650));
					}
					else if (i1 == 3)
					{
						i2 = 54950;
						i3 = 218790;
						i4 = -3488;
						i2 = (i2 + Rnd.get(650));
						i3 = (i3 + Rnd.get(650));
					}
					else if (i1 == 4)
					{
						i2 = 55970;
						i3 = 217770;
						i4 = -3488;
						i2 = (i2 + Rnd.get(650));
						i3 = (i3 + Rnd.get(650));
					}
					else if (i1 == 5)
					{
						i2 = 53930;
						i3 = 217760;
						i4 = -3488;
						i2 = (i2 + Rnd.get(650));
						i3 = (i3 + Rnd.get(650));
					}
					else if (i1 == 6)
					{
						i2 = 55970;
						i3 = 217770;
						i4 = -3216;
						i2 = (i2 + Rnd.get(650));
						i3 = (i3 + Rnd.get(650));
					}
					else if (i1 == 7)
					{
						i2 = 55980;
						i3 = 219920;
						i4 = -3216;
						i2 = (i2 + Rnd.get(650));
						i3 = (i3 + Rnd.get(650));
					}
					else if (i1 == 8)
					{
						i2 = 54960;
						i3 = 218790;
						i4 = -3216;
						i2 = (i2 + Rnd.get(650));
						i3 = (i3 + Rnd.get(650));
					}
					else if (i1 == 9)
					{
						i2 = 53950;
						i3 = 219860;
						i4 = -3216;
						i2 = (i2 + Rnd.get(650));
						i3 = (i3 + Rnd.get(650));
					}
					else if (i1 == 10)
					{
						i2 = 53930;
						i3 = 217760;
						i4 = -3216;
						i2 = (i2 + Rnd.get(650));
						i3 = (i3 + Rnd.get(650));
					}
					else if (i1 == 11)
					{
						i2 = 55970;
						i3 = 217770;
						i4 = -2944;
						i2 = (i2 + Rnd.get(650));
						i3 = (i3 + Rnd.get(650));
					}
					else if (i1 == 12)
					{
						i2 = 55980;
						i3 = 219920;
						i4 = -2944;
						i2 = (i2 + Rnd.get(650));
						i3 = (i3 + Rnd.get(650));
					}
					else if (i1 == 13)
					{
						i2 = 54960;
						i3 = 218790;
						i4 = -2944;
						i2 = (i2 + Rnd.get(650));
						i3 = (i3 + Rnd.get(650));
					}
					else if (i1 == 14)
					{
						i2 = 53950;
						i3 = 219860;
						i4 = -2944;
						i2 = (i2 + Rnd.get(650));
						i3 = (i3 + Rnd.get(650));
					}
					else if (i1 == 15)
					{
						i2 = 53930;
						i3 = 217760;
						i4 = -2944;
						i2 = (i2 + Rnd.get(650));
						i3 = (i3 + Rnd.get(650));
					}
					npc._c_quest4.teleportTo(i2, i3, i4, 0);
					npc.getAI().getAggroList().stopHate(npc._c_quest4);
				}
			}
		}
	}
	
	@Override
	public void onPartyAttacked(Npc caller, Npc called, Creature target, int damage)
	{
		if (GameTimeTaskManager.getInstance().getGameHour() < 5)
		{
			if (called.getAI().getCurrentIntention().getType() == IntentionType.WANDER && called._i_ai0 == 0 && damage < 10 && Rnd.get((30 * 15)) < 1)
			{
				called._i_ai0 = 0;
				called._i_ai1 = caller.getX();
				called._i_ai2 = caller.getY();
				called._i_ai3 = caller.getZ();
				
				startQuestTimer("1002", called, null, 300);
			}
		}
	}
	
	@Override
	public void onPartyDied(Npc caller, Npc called)
	{
		if (called != caller)
		{
			final SpawnLocation zakenLoc = called.getPosition().clone();
			zakenLoc.setHeading(Rnd.get(360));
			
			ThreadPool.schedule(() ->
			{
				if (caller.getSpawn() != null)
					caller.setXYZ(zakenLoc);
				caller.getSpawn().doRespawn(caller);
			}, (30 + Rnd.get(60)) * 1000L);
		}
	}
	
	@Override
	public void onSeeCreature(Npc npc, Creature creature)
	{
		if (creature.getZ() > (npc.getZ() - 100) && creature.getZ() < (npc.getZ() + 100))
		{
			if (!(creature instanceof Playable))
				return;
			
			if (npc.isInMyTerritory())
				npc.getAI().addAttackDesire(creature, 200);
			
			if (npc._i_quest0 < 5 && Rnd.get(3) < 1)
			{
				if (npc._i_quest0 == 0)
					npc._c_quest0 = creature;
				else if (npc._i_quest0 == 1)
					npc._c_quest1 = creature;
				else if (npc._i_quest0 == 2)
					npc._c_quest2 = creature;
				else if (npc._i_quest0 == 3)
					npc._c_quest3 = creature;
				else if (npc._i_quest0 == 4)
					npc._c_quest4 = creature;
				
				npc._i_quest0 = (npc._i_quest0 + 1);
			}
			
			if (Rnd.get(15) < 1)
			{
				final Creature topDesireTarget = npc.getAI().getTopDesireTarget();
				
				final int i0 = Rnd.get(15 * 15);
				if (i0 < 1)
					npc.getAI().addCastDesire(creature, 4216, 1, 1000000);
				else if (i0 < 2)
					npc.getAI().addCastDesire(creature, 4217, 1, 1000000);
				else if (i0 < 4)
					npc.getAI().addCastDesire(creature, 4219, 1, 1000000);
				else if (i0 < 8)
					npc.getAI().addCastDesire(creature, 4218, 1, 1000000);
				else if (i0 < 15)
				{
					if (creature != topDesireTarget && npc.distance3D(creature) < 100)
						npc.getAI().addCastDesire(npc, 4221, 1, 1000000);
				}
				
				if (creature == topDesireTarget && Rnd.get(2) < 1)
					npc.getAI().addCastDesire(creature, 1080321, 1, 1000000);
			}
		}
	}
	
	@Override
	public void onAttacked(Npc npc, Creature attacker, int damage, L2Skill skill)
	{
		final L2Skill striderSlow = SkillTable.getInstance().getInfo(4258, 1);
		if (attacker.isRiding() && getAbnormalLevel(attacker, striderSlow) <= 0)
			npc.getAI().addCastDesire(attacker, striderSlow, 1000000);
		
		if (attacker instanceof Playable)
			npc.getAI().addAttackDesire(attacker, ((((double) damage) / npc.getStatus().getMaxHp()) / 0.050000) * 20000);
		
		if (Rnd.get(10) < 1 && attacker instanceof Playable)
		{
			final Creature topDesireTarget = npc.getAI().getTopDesireTarget();
			
			final int i0 = Rnd.get(15 * 15);
			if (i0 < 1)
				npc.getAI().addCastDesire(attacker, 4216, 1, 1000000);
			else if (i0 < 2)
				npc.getAI().addCastDesire(attacker, 4217, 1, 1000000);
			else if (i0 < 4)
				npc.getAI().addCastDesire(attacker, 4219, 1, 1000000);
			else if (i0 < 8)
				npc.getAI().addCastDesire(attacker, 4218, 1, 1000000);
			else if (i0 < 15)
			{
				if (attacker != topDesireTarget && npc.distance3D(attacker) < 100)
					npc.getAI().addCastDesire(npc, 4221, 1, 1000000);
			}
			
			if (attacker == topDesireTarget && Rnd.get(2) < 1)
				npc.getAI().addCastDesire(attacker, 4220, 1, 1000000);
		}
		
		if (GameTimeTaskManager.getInstance().getGameHour() >= 5 && (npc.getStatus().getHp() < ((npc.getStatus().getMaxHp() * npc._i_quest2) / 4)))
		{
			npc._i_quest2--;
			
			final int i2 = (Rnd.get(14) + 1);
			if (i2 == 1)
			{
				npc._i_ai1 = 53950;
				npc._i_ai2 = 219860;
				npc._i_ai3 = -3488;
				npc._i_ai1 = (npc._i_ai1 + Rnd.get(650));
				npc._i_ai2 = (npc._i_ai2 + Rnd.get(650));
			}
			else if (i2 == 2)
			{
				npc._i_ai1 = 55980;
				npc._i_ai2 = 219820;
				npc._i_ai3 = -3488;
				npc._i_ai1 = (npc._i_ai1 + Rnd.get(650));
				npc._i_ai2 = (npc._i_ai2 + Rnd.get(650));
			}
			else if (i2 == 3)
			{
				npc._i_ai1 = 54950;
				npc._i_ai2 = 218790;
				npc._i_ai3 = -3488;
				npc._i_ai1 = (npc._i_ai1 + Rnd.get(650));
				npc._i_ai2 = (npc._i_ai2 + Rnd.get(650));
			}
			else if (i2 == 4)
			{
				npc._i_ai1 = 55970;
				npc._i_ai2 = 217770;
				npc._i_ai3 = -3488;
				npc._i_ai1 = (npc._i_ai1 + Rnd.get(650));
				npc._i_ai2 = (npc._i_ai2 + Rnd.get(650));
			}
			else if (i2 == 5)
			{
				npc._i_ai1 = 53930;
				npc._i_ai2 = 217760;
				npc._i_ai3 = -3488;
				npc._i_ai1 = (npc._i_ai1 + Rnd.get(650));
				npc._i_ai2 = (npc._i_ai2 + Rnd.get(650));
			}
			else if (i2 == 6)
			{
				npc._i_ai1 = 55970;
				npc._i_ai2 = 217770;
				npc._i_ai3 = -3216;
				npc._i_ai1 = (npc._i_ai1 + Rnd.get(650));
				npc._i_ai2 = (npc._i_ai2 + Rnd.get(650));
			}
			else if (i2 == 7)
			{
				npc._i_ai1 = 55980;
				npc._i_ai2 = 219920;
				npc._i_ai3 = -3216;
				npc._i_ai1 = (npc._i_ai1 + Rnd.get(650));
				npc._i_ai2 = (npc._i_ai2 + Rnd.get(650));
			}
			else if (i2 == 8)
			{
				npc._i_ai1 = 54960;
				npc._i_ai2 = 218790;
				npc._i_ai3 = -3216;
				npc._i_ai1 = (npc._i_ai1 + Rnd.get(650));
				npc._i_ai2 = (npc._i_ai2 + Rnd.get(650));
			}
			else if (i2 == 9)
			{
				npc._i_ai1 = 53950;
				npc._i_ai2 = 219860;
				npc._i_ai3 = -3216;
				npc._i_ai1 = (npc._i_ai1 + Rnd.get(650));
				npc._i_ai2 = (npc._i_ai2 + Rnd.get(650));
			}
			else if (i2 == 10)
			{
				npc._i_ai1 = 53930;
				npc._i_ai2 = 217760;
				npc._i_ai3 = -3216;
				npc._i_ai1 = (npc._i_ai1 + Rnd.get(650));
				npc._i_ai2 = (npc._i_ai2 + Rnd.get(650));
			}
			else if (i2 == 11)
			{
				npc._i_ai1 = 55970;
				npc._i_ai2 = 217770;
				npc._i_ai3 = -2944;
				npc._i_ai1 = (npc._i_ai1 + Rnd.get(650));
				npc._i_ai2 = (npc._i_ai2 + Rnd.get(650));
			}
			else if (i2 == 12)
			{
				npc._i_ai1 = 55980;
				npc._i_ai2 = 219920;
				npc._i_ai3 = -2944;
				npc._i_ai1 = (npc._i_ai1 + Rnd.get(650));
				npc._i_ai2 = (npc._i_ai2 + Rnd.get(650));
			}
			else if (i2 == 13)
			{
				npc._i_ai1 = 54960;
				npc._i_ai2 = 218790;
				npc._i_ai3 = -2944;
				npc._i_ai1 = (npc._i_ai1 + Rnd.get(650));
				npc._i_ai2 = (npc._i_ai2 + Rnd.get(650));
			}
			else if (i2 == 14)
			{
				npc._i_ai1 = 53950;
				npc._i_ai2 = 219860;
				npc._i_ai3 = -2944;
				npc._i_ai1 = (npc._i_ai1 + Rnd.get(650));
				npc._i_ai2 = (npc._i_ai2 + Rnd.get(650));
			}
			else if (i2 == 15)
			{
				npc._i_ai1 = 53930;
				npc._i_ai2 = 217760;
				npc._i_ai3 = -2944;
				npc._i_ai1 = (npc._i_ai1 + Rnd.get(650));
				npc._i_ai2 = (npc._i_ai2 + Rnd.get(650));
			}
			
			npc.getAI().addCastDesire(npc, 4222, 1, 10000000);
		}
	}
	
	@Override
	public void onSeeSpell(Npc npc, Player caster, L2Skill skill, Creature[] targets, boolean isPet)
	{
		if (skill.getAggroPoints() > 0 && !skill.isOffensive())
			npc.getAI().addAttackDesire(caster, ((skill.getAggroPoints() / npc.getStatus().getMaxHp()) * 10) * 150);
		
		if (Rnd.get(12) < 1)
		{
			final Creature topDesireTarget = npc.getAI().getTopDesireTarget();
			
			final int i0 = Rnd.get(15 * 15);
			if (i0 < 1)
				npc.getAI().addCastDesire(caster, 4216, 1, 1000000);
			else if (i0 < 2)
				npc.getAI().addCastDesire(caster, 4217, 1, 1000000);
			else if (i0 < 4)
				npc.getAI().addCastDesire(caster, 4219, 1, 1000000);
			else if (i0 < 8)
				npc.getAI().addCastDesire(caster, 4218, 1, 1000000);
			else if (i0 < 15)
			{
				if (caster != topDesireTarget && npc.distance3D(caster) < 100)
					npc.getAI().addCastDesire(npc, 4221, 1, 1000000);
			}
			
			if (caster == topDesireTarget && Rnd.get(2) < 1)
				npc.getAI().addCastDesire(caster, 4220, 1, 1000000);
		}
	}
	
	@Override
	public void onMyDying(Npc npc, Creature killer)
	{
		npc.broadcastPacket(new PlaySound(1, "BS02_D", npc));
	}
}