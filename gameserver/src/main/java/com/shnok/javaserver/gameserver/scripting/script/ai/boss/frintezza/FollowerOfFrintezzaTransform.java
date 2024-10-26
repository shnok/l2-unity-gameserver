package com.shnok.javaserver.gameserver.scripting.script.ai.boss.frintezza;

import net.sf.l2j.commons.random.Rnd;

import net.sf.l2j.gameserver.data.SkillTable;
import net.sf.l2j.gameserver.data.manager.SpawnManager;
import net.sf.l2j.gameserver.model.actor.Creature;
import net.sf.l2j.gameserver.model.actor.Npc;
import net.sf.l2j.gameserver.model.actor.Playable;
import net.sf.l2j.gameserver.model.actor.Player;
import net.sf.l2j.gameserver.model.memo.GlobalMemo;
import net.sf.l2j.gameserver.model.spawn.NpcMaker;
import net.sf.l2j.gameserver.network.serverpackets.PlaySound;
import net.sf.l2j.gameserver.network.serverpackets.SpecialCamera;
import net.sf.l2j.gameserver.scripting.script.ai.individual.DefaultNpc;
import net.sf.l2j.gameserver.skills.L2Skill;
import net.sf.l2j.gameserver.taskmanager.GameTimeTaskManager;

public class FollowerOfFrintezzaTransform extends DefaultNpc
{
	private static final L2Skill SWING = SkillTable.getInstance().getInfo(5014, 1);
	private static final L2Skill DASH = SkillTable.getInstance().getInfo(5015, 3);
	private static final L2Skill DASH_ALL = SkillTable.getInstance().getInfo(5015, 6);
	private static final L2Skill ANTI_GRAVITY = SkillTable.getInstance().getInfo(5016, 1);
	private static final L2Skill MAGIC_CIRCLE = SkillTable.getInstance().getInfo(5018, 2);
	private static final L2Skill VAMPIRIC = SkillTable.getInstance().getInfo(5019, 1);
	private static final int GM_ID = 6;
	
	public FollowerOfFrintezzaTransform()
	{
		super("ai/boss/frintezza");
	}
	
	public FollowerOfFrintezzaTransform(String descr)
	{
		super(descr);
	}
	
	protected final int[] _npcIds =
	{
		29047 // follower_of_frintessa_tr (Scarlet van Halisha (Transform))
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
			
			npc.getSpawn().getSpawnData().setDBValue(5);
			
			c1 = (Npc) GlobalMemo.getInstance().getCreature("6");
			if (c1 != null)
				c1.sendScriptEvent(npc.getObjectId(), 0, 0);
		}
		else if (npc.getSpawn().getSpawnData().getDBValue() == 7)
		{
			c0 = (Npc) GlobalMemo.getInstance().getCreature("4");
			
			final double hpRatio = npc.getStatus().getHpRatio();
			if (hpRatio < 0.75)
			{
				if (c0 != null)
					c0.sendScriptEvent(npc.getObjectId(), 30021, 0);
				
				npc._i_ai4 = 30021;
			}
			else if (hpRatio < 0.5)
			{
				if (c0 != null)
					c0.sendScriptEvent(npc.getObjectId(), 30022, 0);
				
				npc._i_ai4 = 30022;
			}
			else if (hpRatio < 0.25)
			{
				if (c0 != null)
					c0.sendScriptEvent(npc.getObjectId(), 30023, 0);
				
				npc._i_ai4 = 30023;
			}
			else if (hpRatio < 0.1)
			{
				if (c0 != null)
					c0.sendScriptEvent(npc.getObjectId(), 30024, 0);
				
				npc._i_ai4 = 30024;
			}
			else
			{
				if (c0 != null)
					c0.sendScriptEvent(npc.getObjectId(), 7, 0);
				
				npc._i_ai4 = 7;
			}
			startQuestTimer("1001", npc, null, 2000);
		}
		
		npc._i_ai1 = 0;
		npc._i_ai3 = 0;
		npc._i_ai4 = 0;
		npc._i_ai2 = 0;
		npc._i_quest0 = 0;
		npc._i_quest1 = GameTimeTaskManager.getInstance().getCurrentTick();
		
		if (npc._i_ai0 == 7)
			startQuestTimer("3000", npc, null, 60000);
		
		startQuestTimer("3001", npc, null, 60000);
		
		super.onCreated(npc);
	}
	
	@Override
	public void onAttacked(Npc npc, Creature attacker, int damage, L2Skill skill)
	{
		int i1 = 0;
		
		npc._i_quest1 = GameTimeTaskManager.getInstance().getCurrentTick();
		
		Npc c0 = (Npc) GlobalMemo.getInstance().getCreature("4");
		
		final double hpRatio = npc.getStatus().getHpRatio();
		if (hpRatio < 0.75)
		{
			if (c0 != null)
				c0.sendScriptEvent(npc.getObjectId(), 30021, 0);
			
			npc._i_ai4 = 30021;
		}
		else if (hpRatio < 0.5)
		{
			if (c0 != null)
				c0.sendScriptEvent(npc.getObjectId(), 30022, 0);
			
			npc._i_ai4 = 30022;
		}
		else if (hpRatio < 0.25)
		{
			if (c0 != null)
				c0.sendScriptEvent(npc.getObjectId(), 30023, 0);
			
			npc._i_ai4 = 30023;
		}
		else
		{
			if (c0 != null)
				c0.sendScriptEvent(npc.getObjectId(), 30024, 0);
			
			npc._i_ai4 = 30024;
		}
		
		final L2Skill hinderStrider = SkillTable.getInstance().getInfo(4258, 1);
		if (attacker.isRiding() && getAbnormalLevel(attacker, hinderStrider) <= 0)
			npc.getAI().addCastDesire(attacker, hinderStrider, 1000000);
		
		if (attacker == npc._c_quest2)
		{
			if (npc._i_quest2 < (damage * 1000) + 1000)
				npc._i_quest2 = (damage * 1000) + Rnd.get(3000);
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
			npc._i_ai2 = 1;
			
			if (npc._i_ai2 > 0 && npc._i_ai2 < 3)
			{
				npc.getAI().addCastDesire(attacker, DASH, 10000);
				
				npc._i_ai2 = (npc._i_ai2 + 1);
			}
			else if (npc._i_ai2 == 3)
			{
				npc.getAI().addCastDesire(attacker, DASH_ALL, 10000);
				
				npc._i_ai2 = 0;
			}
			else
				npc._i_ai2 = 0;
		}
		
		doAttack(npc);
	}
	
	// TODO Desire Manipulation
	// EventHandler DESIRE_MANIPULATION(speller, desire)
	// {
	// myself::MakeAttackEvent(speller, (desire * 2), 0);
	// }
	
	@Override
	public void onSeeCreature(Npc npc, Creature creature)
	{
		if (npc._i_ai4 == 0)
			return;
		
		if (!(creature instanceof Playable))
			return;
		
		int i1 = 0;
		
		if (creature == npc._c_quest2)
		{
			if (npc._i_quest2 < 2000)
				npc._i_quest2 = 1000 + Rnd.get(3000);
		}
		else if (creature == npc._c_quest3)
		{
			if (npc._i_quest3 < 2000)
				npc._i_quest3 = 1000 + Rnd.get(3000);
		}
		else if (creature == npc._c_quest4)
		{
			if (npc._i_quest4 < 2000)
				npc._i_quest4 = 1000 + Rnd.get(3000);
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
				npc._i_quest2 = 1000 + Rnd.get(3000);
				npc._c_quest2 = creature;
				break;
			
			case 3:
				npc._i_quest3 = 1000 + Rnd.get(3000);
				npc._c_quest3 = creature;
				break;
			
			case 4:
				npc._i_quest4 = 1000 + Rnd.get(3000);
				npc._c_quest4 = creature;
				break;
		}
		
		doAttack(npc);
	}
	
	@Override
	public void onUseSkillFinished(Npc npc, Creature creature, L2Skill skill, boolean success)
	{
		if (skill == DASH_ALL && success && npc._i_ai2 == 3)
			npc._i_ai2 = 0;
		
		doAttack(npc);
	}
	
	@Override
	public void onScriptEvent(Npc npc, int eventId, int arg1, int arg2)
	{
		if (eventId == 7)
		{
			startQuestTimer("1001", npc, null, 2000);
			startQuestTimer("1001", npc, null, 7000);
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
			npc._i_quest1 = GameTimeTaskManager.getInstance().getCurrentTick();
			
			final Npc c3 = (Npc) GlobalMemo.getInstance().getCreature("4");
			if (c3 != null)
				c3.sendScriptEvent(npc.getObjectId(), 7, 0);
			
			startQuestTimer("1900", npc, null, 2000);
		}
		else if (name.equalsIgnoreCase("1900"))
		{
			startQuestTimer("5001", npc, null, 4000);
		}
		else if (name.equalsIgnoreCase("5001"))
		{
			npc._i_ai0 = 30020;
			
			npc.getAI().addSocialDesire(2, 5000, 10000000);
			
			startQuestTimer("5002", npc, null, 5000);
		}
		else if (name.equalsIgnoreCase("5002"))
		{
			npc.lookNeighbor();
			
			doAttack(npc);
			
			startQuestTimer("5004", npc, null, 2000);
		}
		else if (name.equalsIgnoreCase("5004"))
		{
			NpcMaker evilate_maker1 = SpawnManager.getInstance().getNpcMaker("frintessa_evilate_maker1");
			NpcMaker evilate_maker2 = SpawnManager.getInstance().getNpcMaker("frintessa_evilate_maker2");
			
			if (evilate_maker1 != null)
				evilate_maker1.getMaker().onMakerScriptEvent("1000", evilate_maker1, 0, 0);
			
			if (evilate_maker2 != null)
				evilate_maker2.getMaker().onMakerScriptEvent("1000", evilate_maker2, 0, 0);
			
			broadcastScriptEvent(npc, 0, 20000, 8000);
			
			npc._i_ai0 = 7;
			npc._i_ai4 = 1;
			
			doAttack(npc);
			
			npc._i_quest1 = GameTimeTaskManager.getInstance().getCurrentTick();
			
			startQuestTimer("3000", npc, null, 60000);
		}
		else if (name.equalsIgnoreCase("2000"))
		{
			npc.broadcastPacket(new SpecialCamera(npc.getObjectId(), 430, 300, 80, 0, 10000, 0, 0, 1, 1));
			startQuestTimer("2001", npc, null, 100);
		}
		else if (name.equalsIgnoreCase("2001"))
		{
			npc.broadcastPacket(new PlaySound(1, "BS05_D", npc));
			npc.broadcastPacket(new SpecialCamera(npc.getObjectId(), 430, 300, 80, 0, 10000, 0, 0, 1, 1));
			startQuestTimer("2002", npc, null, 400);
		}
		else if (name.equalsIgnoreCase("2002"))
		{
			npc.broadcastPacket(new SpecialCamera(npc.getObjectId(), 0, 180, 80, 4000, 6000, 0, 0, 1, 1));
			startQuestTimer("2003", npc, null, 6000);
		}
		else if (name.equalsIgnoreCase("3000"))
		{
			if (npc._i_ai0 == 7)
			{
				if (getElapsedTicks(npc._i_quest1) > (15 * 60))
				{
					npc.getSpawn().getSpawnData().setDBValue(0);
					
					Npc c0 = (Npc) GlobalMemo.getInstance().getCreature("4");
					if (c0 != null)
						c0.sendScriptEvent(npc.getObjectId(), 0, 0);
					
					c0 = (Npc) GlobalMemo.getInstance().getCreature("5");
					if (c0 != null)
						c0.sendScriptEvent(npc.getObjectId(), 0, 0);
					
					npc.removeAllDesire();
					npc.getSpawn().instantTeleportInMyTerritory(150037, -57255, -2976, 150);
					npc.getPosition().setHeading(16384);
					npc.teleportTo(-105200, -253104, -15264, 0);
					
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
		else if (name.equalsIgnoreCase("3001"))
		{
			if (npc._i_ai0 == 7)
				doAttack(npc);
			
			npc.lookNeighbor();
			
			startQuestTimer("3001", npc, null, 60000);
		}
		
		return super.onTimer(name, npc, player);
	}
	
	@Override
	public void onMyDying(Npc npc, Creature killer)
	{
		GlobalMemo.getInstance().remove(String.valueOf(GM_ID));
		
		broadcastScriptEvent(npc, npc.getObjectId(), 8, 8000);
		
		startQuestTimer("2000", npc, null, 1);
	}
	
	@Override
	public void onNoDesire(Npc npc)
	{
		npc.lookNeighbor();
	}
	
	private static void doAttack(Npc npc)
	{
		int i1 = 0;
		int i2 = 0;
		
		Creature c2 = null;
		
		if (npc._c_quest2 == null || (npc.distance2D(npc._c_quest2) > 5000 || npc._c_quest2.isDead()))
			npc._i_quest2 = 0;
		
		if (npc._c_quest3 == null || (npc.distance2D(npc._c_quest3) > 5000 || npc._c_quest3.isDead()))
			npc._i_quest3 = 0;
		
		if (npc._c_quest4 == null || (npc.distance2D(npc._c_quest4) > 5000 || npc._c_quest4.isDead()))
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
				if (npc._i_ai2 > 0)
				{
					if (npc._i_ai2 > 0 && npc._i_ai2 < 3)
					{
						npc.getAI().addCastDesire(c2, DASH, 10000, false);
						
						npc._i_ai2 = (npc._i_ai2 + 1);
					}
					else if (npc._i_ai2 == 3)
					{
						npc.getAI().addCastDesire(c2, DASH_ALL, 10000, false);
						
						npc._i_ai2 = 0;
					}
					else
						npc._i_ai2 = 0;
				}
				else if (npc.getStatus().getHpRatio() > 0.75)
				{
					if (Rnd.get(10000) < 2000)
					{
						// TODO zones
						// if( IsInThisTerritory("25_15_frintessa_NoCharge01") == 1 ) {
						// if( c2 != null) {
						// npc.getAI().addCastDesire(c2,DASH_ALL,10000);
						// }
						// }
					}
					else if (Rnd.get(10000) < 1000)
					{
						npc._i_ai2 = 1;
						if (npc._i_ai2 > 0 && npc._i_ai2 < 3)
						{
							npc.getAI().addCastDesire(c2, DASH, 10000, false);
							
							npc._i_ai2 = (npc._i_ai2 + 1);
						}
						else if (npc._i_ai2 == 3)
						{
							npc.getAI().addCastDesire(c2, DASH_ALL, 10000, false);
							
							npc._i_ai2 = 0;
						}
						else
							npc._i_ai2 = 0;
					}
					else if (Rnd.get(10000) < 1000)
						npc.getAI().addCastDesire(c2, ANTI_GRAVITY, 10000, false);
					else if (Rnd.get(10000) < 0)
						npc.getAI().addCastDesire(c2, MAGIC_CIRCLE, 10000, false);
					else if (Rnd.get(10000) < 0)
						npc.getAI().addCastDesire(c2, VAMPIRIC, 10000, false);
					else
						npc.getAI().addCastDesire(c2, SWING, 10000, false);
				}
				else if (npc.getStatus().getHpRatio() > 0.5)
				{
					if (Rnd.get(10000) < 2000)
					{
						// TODO zones
						// if( IsInThisTerritory("25_15_frintessa_NoCharge01") == 1 ) {
						// if( c2 != null) {
						// npc.getAI().addCastDesire(c2,DASH_ALL,10000);
						// }
						// }
					}
					else if (Rnd.get(10000) < 1000)
					{
						npc._i_ai2 = 1;
						if (npc._i_ai2 > 0 && npc._i_ai2 < 3)
						{
							npc.getAI().addCastDesire(c2, DASH, 10000, false);
							
							npc._i_ai2 = (npc._i_ai2 + 1);
						}
						else if (npc._i_ai2 == 3)
						{
							npc.getAI().addCastDesire(c2, DASH_ALL, 10000, false);
							
							npc._i_ai2 = 0;
						}
						else
							npc._i_ai2 = 0;
					}
					else if (Rnd.get(10000) < 750)
						npc.getAI().addCastDesire(c2, ANTI_GRAVITY, 10000, false);
					else if (Rnd.get(10000) < 500)
						npc.getAI().addCastDesire(c2, MAGIC_CIRCLE, 10000, false);
					else if (Rnd.get(10000) < 500)
						npc.getAI().addCastDesire(c2, VAMPIRIC, 10000, false);
					else
						npc.getAI().addCastDesire(c2, SWING, 10000, false);
				}
				else if (npc.getStatus().getHpRatio() > 0.25)
				{
					if (Rnd.get(10000) < 2000)
					{
						// TODO Zones
						// if( IsInThisTerritory("25_15_frintessa_NoCharge01") == 1 ) {
						// if( c2 != null) {
						// npc.getAI().addCastDesire(c2,DASH_ALL,10000);
						// }
						// }
					}
					else if (Rnd.get(10000) < 1000)
					{
						npc._i_ai2 = 1;
						if (npc._i_ai2 > 0 && npc._i_ai2 < 3)
						{
							npc.getAI().addCastDesire(c2, DASH, 10000, false);
							
							npc._i_ai2 = (npc._i_ai2 + 1);
						}
						else if (npc._i_ai2 == 3)
						{
							npc.getAI().addCastDesire(c2, DASH_ALL, 10000, false);
							
							npc._i_ai2 = 0;
						}
						else
							npc._i_ai2 = 0;
					}
					else if (Rnd.get(10000) < 750)
						npc.getAI().addCastDesire(c2, ANTI_GRAVITY, 10000, false);
					else if (Rnd.get(10000) < 1000)
						npc.getAI().addCastDesire(c2, MAGIC_CIRCLE, 10000, false);
					else if (Rnd.get(10000) < 500)
						npc.getAI().addCastDesire(c2, VAMPIRIC, 10000, false);
					else
						npc.getAI().addCastDesire(c2, SWING, 10000, false);
				}
				else if (npc.getStatus().getHpRatio() > 0.1)
				{
					if (Rnd.get(10000) < 2000)
					{
						// TODO Zones
						// if( IsInThisTerritory("25_15_frintessa_NoCharge01") == 1 ) {
						// if( c2 != null) {
						// npc.getAI().addCastDesire(c2,DASH_ALL,10000);
						// }
						// }
					}
					else if (Rnd.get(10000) < 1000)
					{
						npc._i_ai2 = 1;
						if (npc._i_ai2 > 0 && npc._i_ai2 < 3)
						{
							npc.getAI().addCastDesire(c2, DASH, 10000, false);
							
							npc._i_ai2 = (npc._i_ai2 + 1);
						}
						else if (npc._i_ai2 == 3)
						{
							npc.getAI().addCastDesire(c2, DASH_ALL, 10000, false);
							
							npc._i_ai2 = 0;
						}
						else
							npc._i_ai2 = 0;
					}
					else if (Rnd.get(10000) < 1000)
						npc.getAI().addCastDesire(c2, ANTI_GRAVITY, 10000, false);
					else if (Rnd.get(10000) < 1000)
						npc.getAI().addCastDesire(c2, MAGIC_CIRCLE, 10000, false);
					else if (Rnd.get(10000) < 1000)
						npc.getAI().addCastDesire(c2, VAMPIRIC, 10000, false);
					else
						npc.getAI().addCastDesire(c2, SWING, 10000, false);
				}
				else if (Rnd.get(10000) < 2000)
				{
					// TODO Zones
					// if( IsInThisTerritory("25_15_frintessa_NoCharge01") == 1 ) {
					// if( c2 != null) {
					// npc.getAI().addCastDesire(c2,DASH_ALL,10000);
					// }
					// }
				}
				else if (Rnd.get(10000) < 1000)
				{
					npc._i_ai2 = 1;
					if (npc._i_ai2 > 0 && npc._i_ai2 < 3)
					{
						npc.getAI().addCastDesire(c2, DASH, 10000, false);
						
						npc._i_ai2 = (npc._i_ai2 + 1);
					}
					else if (npc._i_ai2 == 3)
					{
						npc.getAI().addCastDesire(c2, DASH_ALL, 10000, false);
						
						npc._i_ai2 = 0;
					}
					else
						npc._i_ai2 = 0;
				}
				else if (Rnd.get(10000) < 500)
					npc.getAI().addCastDesire(c2, ANTI_GRAVITY, 10000, false);
				else if (Rnd.get(10000) < 500)
					npc.getAI().addCastDesire(c2, MAGIC_CIRCLE, 10000, false);
				else if (Rnd.get(10000) < 0)
					npc.getAI().addCastDesire(c2, VAMPIRIC, 10000, false);
				else
					npc.getAI().addCastDesire(c2, SWING, 10000, false);
			}
		}
	}
}