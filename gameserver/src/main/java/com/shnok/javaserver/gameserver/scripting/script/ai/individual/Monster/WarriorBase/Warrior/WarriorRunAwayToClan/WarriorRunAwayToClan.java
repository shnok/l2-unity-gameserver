package com.shnok.javaserver.gameserver.scripting.script.ai.individual.Monster.WarriorBase.Warrior.WarriorRunAwayToClan;

import com.shnok.javaserver.commons.random.Rnd;

import com.shnok.javaserver.gameserver.enums.IntentionType;
import com.shnok.javaserver.gameserver.model.World;
import com.shnok.javaserver.gameserver.model.actor.Creature;
import com.shnok.javaserver.gameserver.model.actor.Npc;
import com.shnok.javaserver.gameserver.model.actor.Player;
import com.shnok.javaserver.gameserver.model.location.Location;
import com.shnok.javaserver.gameserver.network.NpcStringId;
import com.shnok.javaserver.gameserver.scripting.script.ai.individual.Monster.WarriorBase.Warrior.Warrior;
import com.shnok.javaserver.gameserver.skills.L2Skill;

public class WarriorRunAwayToClan extends Warrior
{
	public WarriorRunAwayToClan()
	{
		super("ai/individual/Monster/WarriorBase/Warrior/WarriorRunAwayToClan");
	}
	
	public WarriorRunAwayToClan(String descr)
	{
		super(descr);
	}
	
	protected final int[] _npcIds =
	{
		20058,
		20207,
		20439,
		20208,
		20437,
		20210,
		20436,
		20499,
		20500,
		20498,
		20494
	};
	
	@Override
	public void onCreated(Npc npc)
	{
		npc._i_ai0 = 0;
		npc._i_ai4 = 0;
		
		super.onCreated(npc);
	}
	
	@Override
	public void onAttacked(Npc npc, Creature attacker, int damage, L2Skill skill)
	{
		if (npc._i_ai4 == 0)
			npc._i_ai4 = 1;
		else
		{
			final Location fleeLoc = npc.getSpawn().getFleeLocation();
			if (fleeLoc != null && npc._i_ai0 == 0 && Rnd.get(100) < 10)
			{
				final double npcHpRatio = npc.getStatus().getHpRatio();
				final double attackerHpRatio = attacker.getStatus().getHpRatio();
				
				if (npcHpRatio < 0.5 && npcHpRatio > 0.33 && attackerHpRatio > 0.25)
				{
					final int i5 = Rnd.get(100);
					if (i5 < 7)
						npc.broadcastNpcSay(NpcStringId.ID_1000007);
					else if (i5 < 14)
						npc.broadcastNpcSay(NpcStringId.ID_1000008);
					else if (i5 < 21)
						npc.broadcastNpcSay(NpcStringId.ID_1000009);
					else if (i5 < 28)
						npc.broadcastNpcSay(NpcStringId.ID_1000010);
					else if (i5 < 35)
						npc.broadcastNpcSay(NpcStringId.ID_1000011);
					else if (i5 < 42)
						npc.broadcastNpcSay(NpcStringId.ID_1000012);
					else if (i5 < 49)
						npc.broadcastNpcSay(NpcStringId.ID_1000013);
					else if (i5 < 56)
						npc.broadcastNpcSay(NpcStringId.ID_1000014);
					else if (i5 < 63)
						npc.broadcastNpcSay(NpcStringId.ID_1000015);
					else if (i5 < 70)
						npc.broadcastNpcSay(NpcStringId.ID_1000016);
					else if (i5 < 77)
						npc.broadcastNpcSay(NpcStringId.ID_1000017);
					else if (i5 < 79)
						npc.broadcastNpcSay(NpcStringId.ID_1000018);
					else if (i5 < 81)
						npc.broadcastNpcSay(NpcStringId.ID_1000019);
					else if (i5 < 83)
						npc.broadcastNpcSay(NpcStringId.ID_1000020);
					else if (i5 < 85)
						npc.broadcastNpcSay(NpcStringId.ID_1000021);
					else if (i5 < 87)
						npc.broadcastNpcSay(NpcStringId.ID_1000022);
					else if (i5 < 89)
						npc.broadcastNpcSay(NpcStringId.ID_1000023);
					else if (i5 < 91)
						npc.broadcastNpcSay(NpcStringId.ID_1000024);
					else if (i5 < 93)
						npc.broadcastNpcSay(NpcStringId.ID_1000025);
					else if (i5 < 95)
						npc.broadcastNpcSay(NpcStringId.ID_1000026);
					else
						npc.broadcastNpcSay(NpcStringId.ID_1000027);
					
					npc.getAI().addMoveToDesire(fleeLoc, 100000000);
					
					npc._i_ai0 = 1;
					npc._c_ai0 = attacker;
				}
			}
		}
		super.onAttacked(npc, attacker, damage, skill);
	}
	
	@Override
	public void onSeeCreature(Npc npc, Creature creature)
	{
		// Do nothing.
	}
	
	@Override
	public void onMoveToFinished(Npc npc, int x, int y, int z)
	{
		if (npc._i_ai0 == 1)
		{
			final Location fleeLoc = npc.getSpawn().getFleeLocation();
			if (fleeLoc.equals(x, y, z))
			{
				startQuestTimer("2001", npc, null, 15000);
				
				npc.getAI().getDesireQueue().removeIf(d -> d.getType() == IntentionType.MOVE_TO && d.getLoc() == fleeLoc);
				npc.getAI().addWanderDesire(5, 50);
				
				npc._i_ai0 = 2;
				
				broadcastScriptEvent(npc, 10000, npc._c_ai0.getObjectId(), 400);
			}
			else
				npc.getAI().addMoveToDesire(fleeLoc, 100000000);
		}
		else if (npc._i_ai0 == 3)
		{
			if (npc.getSpawnLocation().equals(x, y, z))
			{
				npc.getAI().addWanderDesire(5, 1000);
				
				npc._i_ai0 = 0;
			}
		}
	}
	
	@Override
	public String onTimer(String name, Npc npc, Player player)
	{
		if (name.equalsIgnoreCase("2001"))
		{
			if (npc.getAI().getCurrentIntention().getType() != IntentionType.ATTACK)
			{
				if (npc.getStatus().getHpRatio() < 0.7 && npc._i_ai0 == 2)
				{
					npc.getAI().getDesireQueue().removeIf(d -> d.getType() == IntentionType.WANDER);
					npc.getAI().addMoveToDesire(npc.getSpawnLocation(), 1000000);
					
					npc._i_ai0 = 3;
				}
				else
				{
					npc.removeAllDesire();
					npc.getAI().addMoveToDesire(npc.getSpawnLocation(), 50);
					
					npc._i_ai0 = 0;
				}
			}
		}
		return super.onTimer(name, npc, player);
	}
	
	@Override
	public void onScriptEvent(Npc npc, int eventId, int arg1, int arg2)
	{
		if (npc.isDead())
			return;
		
		if (eventId == 10000)
		{
			if (npc.getAI().getCurrentIntention().getType() != IntentionType.ATTACK)
			{
				final Creature creature = (Creature) World.getInstance().getObject(arg1);
				if (creature != null)
					npc.getAI().addAttackDesire(creature, 1000000);
				
				npc._i_ai3 = 0;
			}
		}
	}
}