package com.shnok.javaserver.gameserver.scripting.script.ai.individual.Monster.RaidBoss.RaidBossParty;

import net.sf.l2j.commons.random.Rnd;

import net.sf.l2j.gameserver.data.manager.SpawnManager;
import net.sf.l2j.gameserver.enums.IntentionType;
import net.sf.l2j.gameserver.enums.actors.NpcSkillType;
import net.sf.l2j.gameserver.model.World;
import net.sf.l2j.gameserver.model.actor.Creature;
import net.sf.l2j.gameserver.model.actor.Npc;
import net.sf.l2j.gameserver.model.actor.Playable;
import net.sf.l2j.gameserver.model.actor.Player;
import net.sf.l2j.gameserver.model.location.Location;
import net.sf.l2j.gameserver.model.spawn.NpcMaker;
import net.sf.l2j.gameserver.skills.L2Skill;
import net.sf.l2j.gameserver.taskmanager.GameTimeTaskManager;

public class RaidBossType4AndreasAggressive extends RaidBossType4
{
	public RaidBossType4AndreasAggressive()
	{
		super("ai/individual/Monster/RaidBoss/RaidBossAlone/RaidBossParty/RaidBossType4");
	}
	
	public RaidBossType4AndreasAggressive(String descr)
	{
		super(descr);
	}
	
	protected final int[] _npcIds =
	{
		29062
	};
	
	@Override
	public void onCreated(Npc npc)
	{
		npc._i_ai2 = 0;
		npc._i_ai3 = 0;
		npc._i_ai4 = 0;
		npc._i_quest0 = 0;
		npc._i_quest1 = 0;
		npc._c_ai0 = npc;
		
		broadcastScriptEvent(npc, 0, npc.getObjectId(), getNpcIntAIParamOrDefault(npc, "BroadcastRange", 4000) * 2);
		
		startQuestTimer("6008", npc, null, 5 * 60 * 60000L);
		
		final int private1_silhouette = getNpcIntAIParam(npc, "private1_silhouette");
		final int private2_silhouette = getNpcIntAIParam(npc, "private2_silhouette");
		final int private3_silhouette = getNpcIntAIParam(npc, "private3_silhouette");
		final int private4_silhouette = getNpcIntAIParam(npc, "private4_silhouette");
		final int private1_pos_x = getNpcIntAIParamOrDefault(npc, "private1_pos_x", -91);
		final int private2_pos_x = getNpcIntAIParamOrDefault(npc, "private2_pos_x", -48);
		final int private3_pos_x = getNpcIntAIParamOrDefault(npc, "private3_pos_x", -91);
		final int private4_pos_x = getNpcIntAIParamOrDefault(npc, "private4_pos_x", -48);
		final int private1_pos_y = getNpcIntAIParamOrDefault(npc, "private1_pos_y", -95);
		final int private2_pos_y = getNpcIntAIParamOrDefault(npc, "private2_pos_y", -130);
		final int private3_pos_y = getNpcIntAIParamOrDefault(npc, "private3_pos_y", 95);
		final int private4_pos_y = getNpcIntAIParamOrDefault(npc, "private4_pos_y", 130);
		final int Start_x = getNpcIntAIParamOrDefault(npc, "Start_x", -16385);
		final int Start_y = getNpcIntAIParamOrDefault(npc, "Start_y", -53268);
		final int Start_z = getNpcIntAIParamOrDefault(npc, "Start_z", -10439);
		
		createOnePrivateEx(npc, private1_silhouette, Start_x + private1_pos_x, Start_y + private1_pos_y, Start_z, 0, 0, true, 0, 0, 0);
		createOnePrivateEx(npc, private2_silhouette, Start_x + private2_pos_x, Start_y + private2_pos_y, Start_z, 0, 0, true, 0, 0, 0);
		createOnePrivateEx(npc, private3_silhouette, Start_x + private3_pos_x, Start_y + private3_pos_y, Start_z, 0, 0, true, 0, 0, 0);
		createOnePrivateEx(npc, private4_silhouette, Start_x + private4_pos_x, Start_y + private4_pos_y, Start_z, 0, 0, true, 0, 0, 0);
	}
	
	@Override
	public void onNoDesire(Npc npc)
	{
		if (npc._i_ai3 != 0 || npc._i_ai4 != 0)
		{
			npc._i_ai2 = 0;
			if (npc._c_ai0 != null)
				startQuestTimer("6005", npc, null, 5000);
		}
		else if (npc._i_ai3 < 3 || npc._i_ai3 >= 7)
		{
			final int Start_x = getNpcIntAIParamOrDefault(npc, "Start_x", -16385);
			final int Start_y = getNpcIntAIParamOrDefault(npc, "Start_y", -53268);
			final int Start_z = getNpcIntAIParamOrDefault(npc, "Start_z", -10439);
			npc.getAI().addMoveToDesire(new Location(Start_x, Start_y, Start_z), 30);
		}
	}
	
	@Override
	public void onAttacked(Npc npc, Creature attacker, int damage, L2Skill skill)
	{
		super.onAttacked(npc, attacker, damage, skill);
		
		final Creature topDesireTarget = npc.getAI().getTopDesireTarget();
		
		if (topDesireTarget != null)
			npc._c_ai0 = topDesireTarget;
		else
			npc._c_ai0 = attacker;
		
		npc._i_quest1 = GameTimeTaskManager.getInstance().getCurrentTick();
		
		if (Rnd.get(100) < getNpcIntAIParamOrDefault(npc, "AttackPartyPointRate", 33))
			broadcastScriptEvent(npc, 10002, npc._c_ai0.getObjectId(), getNpcIntAIParamOrDefault(npc, "SeekRange", 400));
		
		final double hpRatio = npc.getStatus().getHpRatio();
		if (hpRatio < 0.1)
		{
			if (npc._i_ai4 == 2)
			{
				npc.removeAllAttackDesire();
				npc.getAI().addCastDesire(npc, getNpcSkillByType(npc, NpcSkillType.TELEPORT_EFFECT), 1000000);
			}
		}
		else if (hpRatio < 0.3)
		{
			if (npc._i_ai4 == 1)
			{
				npc.removeAllAttackDesire();
				npc.getAI().addCastDesire(npc, getNpcSkillByType(npc, NpcSkillType.TELEPORT_EFFECT), 1000000);
			}
		}
		else if (hpRatio < 0.6)
		{
			if (npc._i_ai4 == 0)
			{
				npc.removeAllAttackDesire();
				npc.getAI().addCastDesire(npc, getNpcSkillByType(npc, NpcSkillType.TELEPORT_EFFECT), 1000000);
			}
		}
		
		super.onAttacked(npc, attacker, damage, skill);
	}
	
	@Override
	public String onTimer(String name, Npc npc, Player player)
	{
		if (name.equalsIgnoreCase("6008"))
		{
			if (npc._i_ai2 == 0 && npc._i_ai3 == 0 && npc._i_ai4 == 0 && npc._c_ai0 == npc && npc.getAI().getCurrentIntention().getType() != IntentionType.ATTACK && npc.getAI().getCurrentIntention().getType() != IntentionType.CAST)
			{
				NpcMaker maker0 = SpawnManager.getInstance().getNpcMaker(getNpcStringAIParamOrDefault(npc, "MidSpawnMakerName11", "altar_combat1_01"));
				if (maker0 != null)
				{
					maker0.getMaker().onMakerScriptEvent("1000", maker0, 0, 0);
				}
				maker0 = SpawnManager.getInstance().getNpcMaker(getNpcStringAIParamOrDefault(npc, "MidSpawnMakerName12", "altar_combat1_02"));
				if (maker0 != null)
				{
					maker0.getMaker().onMakerScriptEvent("1000", maker0, 0, 0);
				}
				maker0 = SpawnManager.getInstance().getNpcMaker(getNpcStringAIParamOrDefault(npc, "MidSpawnMakerName13", "altar_combat1_03"));
				if (maker0 != null)
				{
					maker0.getMaker().onMakerScriptEvent("1000", maker0, 0, 0);
				}
				maker0 = SpawnManager.getInstance().getNpcMaker(getNpcStringAIParamOrDefault(npc, "MidSpawnMakerName21", "altar_combat2_01"));
				if (maker0 != null)
				{
					maker0.getMaker().onMakerScriptEvent("1000", maker0, 0, 0);
				}
				maker0 = SpawnManager.getInstance().getNpcMaker(getNpcStringAIParamOrDefault(npc, "MidSpawnMakerName22", "altar_combat2_02"));
				if (maker0 != null)
				{
					maker0.getMaker().onMakerScriptEvent("1000", maker0, 0, 0);
				}
				maker0 = SpawnManager.getInstance().getNpcMaker(getNpcStringAIParamOrDefault(npc, "MidSpawnMakerName23", "altar_combat2_03"));
				if (maker0 != null)
				{
					maker0.getMaker().onMakerScriptEvent("1000", maker0, 0, 0);
				}
				npc._i_ai2 = 0;
				npc._i_ai3 = 0;
				npc._i_ai4 = 0;
				npc._c_ai0 = npc;
				
				broadcastScriptEvent(npc, 6, npc.getObjectId(), getNpcIntAIParamOrDefault(npc, "BroadcastRange", 4000) * 2);
				
				npc.deleteMe();
			}
			else
			{
				startQuestTimer("6008", npc, player, 30 * 60 * 1000L);
			}
		}
		else if (name.equalsIgnoreCase("6007"))
		{
			npc.removeAllDesire();
			npc.getAI().getHateList().cleanAllHate();
			npc._i_quest0 = 1;
		}
		else if (name.equalsIgnoreCase("6006"))
		{
			final int i0 = getElapsedTicks(npc._i_quest1);
			if (i0 > (30 * 60))
			{
				npc.removeAllAttackDesire();
				broadcastScriptEvent(npc, 7, 0, getNpcIntAIParamOrDefault(npc, "BroadcastRange", 4000));
			}
			else
			{
				startQuestTimer("6006", npc, player, 60000);
			}
		}
		else if (name.equalsIgnoreCase("6005"))
		{
			if (npc._c_ai0 != null)
			{
				npc._i_quest0 = 0;
				if (npc._c_ai0 instanceof Playable)
				{
					npc.getAI().addAttackDesire(npc._c_ai0, 700);
				}
				broadcastScriptEvent(npc, 10002, npc._c_ai0.getObjectId(), getNpcIntAIParamOrDefault(npc, "SeekRange", 400));
			}
		}
		else if (name.equalsIgnoreCase("6004") || name.equalsIgnoreCase("6003"))
		{
			npc._i_quest0 = 0;
			npc.lookNeighbor();
		}
		else if (name.equalsIgnoreCase("6099"))
		{
			npc.getAI().addCastDesire(npc, 4183, 1, 1000000);
		}
		
		return super.onTimer(name, npc, player);
	}
	
	@Override
	public void onSeeCreature(Npc npc, Creature creature)
	{
		npc._i_ai2 = 1;
		if (creature instanceof Player)
		{
			if (npc._c_ai0 == npc)
			{
				npc.getAI().addAttackDesire(creature, 700);
			}
			else if (npc._c_ai0 instanceof Playable)
			{
				npc.getAI().addAttackDesire(npc._c_ai0, 700);
			}
		}
	}
	
	@Override
	public void onUseSkillFinished(Npc npc, Creature creature, L2Skill skill, boolean success)
	{
		if (skill == getNpcSkillByType(npc, NpcSkillType.TELEPORT_EFFECT))
		{
			final int TelPosX1 = getNpcIntAIParamOrDefault(npc, "TelPosX1", -16393);
			final int TelPosY1 = getNpcIntAIParamOrDefault(npc, "TelPosY1", -53433);
			final int TelPosZ1 = getNpcIntAIParamOrDefault(npc, "TelPosZ1", -10439);
			final int TelPosX2 = getNpcIntAIParamOrDefault(npc, "TelPosX2", -17150);
			final int TelPosY2 = getNpcIntAIParamOrDefault(npc, "TelPosY2", -54064);
			final int TelPosZ2 = getNpcIntAIParamOrDefault(npc, "TelPosZ2", -10439);
			final int TelPosX3 = getNpcIntAIParamOrDefault(npc, "TelPosX3", -15690);
			final int TelPosY3 = getNpcIntAIParamOrDefault(npc, "TelPosY3", -54030);
			final int TelPosZ3 = getNpcIntAIParamOrDefault(npc, "TelPosZ3", -10439);
			
			if (npc._i_ai4 == 2)
			{
				if (success)
					npc.teleportTo(TelPosX1, TelPosY1, TelPosZ1, 0);
				
				npc._i_ai4 = 3;
				startQuestTimer("6007", npc, null, 500);
				startQuestTimer("6005", npc, null, 10000);
			}
			else if (npc._i_ai4 == 1)
			{
				if (success)
				{
					int i0 = Rnd.get(3);
					if (i0 == 0)
						npc.teleportTo(TelPosX1, TelPosY1, TelPosZ1, 0);
					else if (i0 == 1)
						npc.teleportTo(TelPosX2, TelPosY2, TelPosZ2, 0);
					else
						npc.teleportTo(TelPosX3, TelPosY3, TelPosZ3, 0);
				}
				
				npc._i_ai4 = 2;
				
				NpcMaker maker0 = SpawnManager.getInstance().getNpcMaker(getNpcStringAIParamOrDefault(npc, "MidSpawnMakerName21", "altar_combat2_01"));
				if (maker0 != null)
					maker0.getMaker().onMakerScriptEvent("1001", maker0, 0, 0);
				
				maker0 = SpawnManager.getInstance().getNpcMaker(getNpcStringAIParamOrDefault(npc, "MidSpawnMakerName22", "altar_combat2_02"));
				if (maker0 != null)
					maker0.getMaker().onMakerScriptEvent("1001", maker0, 0, 0);
				
				maker0 = SpawnManager.getInstance().getNpcMaker(getNpcStringAIParamOrDefault(npc, "MidSpawnMakerName23", "altar_combat2_03"));
				if (maker0 != null)
					maker0.getMaker().onMakerScriptEvent("1001", maker0, 0, 0);
				
				startQuestTimer("6007", npc, null, 500);
				startQuestTimer("6004", npc, null, 10000);
			}
			else if (npc._i_ai4 == 0)
			{
				if (success)
				{
					int i0 = Rnd.get(2);
					if (i0 == 0)
						npc.teleportTo(TelPosX3, TelPosY3, TelPosZ3, 0);
					else
						npc.teleportTo(TelPosX2, TelPosY2, TelPosZ2, 0);
				}
				
				npc._i_ai4 = 1;
				
				NpcMaker maker0 = SpawnManager.getInstance().getNpcMaker(getNpcStringAIParamOrDefault(npc, "MidSpawnMakerName11", "altar_combat1_01"));
				if (maker0 != null)
					maker0.getMaker().onMakerScriptEvent("1001", maker0, 0, 0);
				
				maker0 = SpawnManager.getInstance().getNpcMaker(getNpcStringAIParamOrDefault(npc, "MidSpawnMakerName12", "altar_combat1_02"));
				if (maker0 != null)
					maker0.getMaker().onMakerScriptEvent("1001", maker0, 0, 0);
				
				maker0 = SpawnManager.getInstance().getNpcMaker(getNpcStringAIParamOrDefault(npc, "MidSpawnMakerName13", "altar_combat1_03"));
				if (maker0 != null)
					maker0.getMaker().onMakerScriptEvent("1001", maker0, 0, 0);
				
				startQuestTimer("6007", npc, null, 500);
				startQuestTimer("6003", npc, null, 10000);
			}
		}
		
		super.onUseSkillFinished(npc, creature, skill, success);
	}
	
	@Override
	public void onScriptEvent(Npc npc, int eventId, int arg1, int arg2)
	{
		try
		{
			if (eventId < 9)
			{
				npc._i_ai3 = eventId;
				if (eventId == 1)
					startQuestTimer("6006", npc, null, 60000);
				if (eventId > 0)
					npc._i_quest1 = GameTimeTaskManager.getInstance().getCurrentTick();
			}
			if (eventId == 4)
			{
				if (arg1 != 0)
				{
					broadcastScriptEvent(npc, 10020, arg1, getNpcIntAIParamOrDefault(npc, "SeekRange", 400));
					final Creature c0 = (Creature) World.getInstance().getObject(arg1);
					if (c0 instanceof Playable)
						npc.getAI().addAttackDesire(c0, 700);
				}
			}
			else if (eventId == 7)
			{
				npc._i_ai2 = 0;
				npc._i_ai3 = 0;
				npc._i_ai4 = 0;
				npc._c_ai0 = npc;
				final int Start_x = getNpcIntAIParamOrDefault(npc, "Start_x", -16385);
				final int Start_y = getNpcIntAIParamOrDefault(npc, "Start_y", -53268);
				final int Start_z = getNpcIntAIParamOrDefault(npc, "Start_z", -10439);
				npc.getAI().addMoveToDesire(new Location(Start_x, Start_y, Start_z), 30);
			}
			else if (eventId == 3)
			{
				startQuestTimer("6099", npc, null, 6700);
			}
		}
		catch (NumberFormatException e)
		{
			// Do nothing.
		}
	}
	
	@Override
	public void onMyDying(Npc npc, Creature killer)
	{
		NpcMaker maker0 = SpawnManager.getInstance().getNpcMaker(getNpcStringAIParamOrDefault(npc, "MidSpawnMakerName11", "altar_combat1_01"));
		if (maker0 != null)
			maker0.getMaker().onMakerScriptEvent("1000", maker0, 0, 0);
		maker0 = SpawnManager.getInstance().getNpcMaker(getNpcStringAIParamOrDefault(npc, "MidSpawnMakerName12", "altar_combat1_02"));
		if (maker0 != null)
			maker0.getMaker().onMakerScriptEvent("1000", maker0, 0, 0);
		maker0 = SpawnManager.getInstance().getNpcMaker(getNpcStringAIParamOrDefault(npc, "MidSpawnMakerName13", "altar_combat1_03"));
		if (maker0 != null)
			maker0.getMaker().onMakerScriptEvent("1000", maker0, 0, 0);
		maker0 = SpawnManager.getInstance().getNpcMaker(getNpcStringAIParamOrDefault(npc, "MidSpawnMakerName21", "altar_combat2_01"));
		if (maker0 != null)
			maker0.getMaker().onMakerScriptEvent("1000", maker0, 0, 0);
		maker0 = SpawnManager.getInstance().getNpcMaker(getNpcStringAIParamOrDefault(npc, "MidSpawnMakerName22", "altar_combat2_02"));
		if (maker0 != null)
			maker0.getMaker().onMakerScriptEvent("1000", maker0, 0, 0);
		maker0 = SpawnManager.getInstance().getNpcMaker(getNpcStringAIParamOrDefault(npc, "MidSpawnMakerName23", "altar_combat2_03"));
		if (maker0 != null)
			maker0.getMaker().onMakerScriptEvent("1000", maker0, 0, 0);
		
		npc._i_ai2 = 0;
		npc._i_ai3 = 0;
		npc._i_ai4 = 0;
		npc._c_ai0 = npc;
		
		broadcastScriptEvent(npc, 5, npc.getObjectId(), getNpcIntAIParamOrDefault(npc, "BroadcastRange", 4000) * 2);
		
		super.onMyDying(npc, killer);
	}
}
