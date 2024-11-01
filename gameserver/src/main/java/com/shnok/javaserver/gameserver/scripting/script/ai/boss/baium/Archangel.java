package com.shnok.javaserver.gameserver.scripting.script.ai.boss.baium;

import com.shnok.javaserver.commons.random.Rnd;

import com.shnok.javaserver.gameserver.enums.IntentionType;
import com.shnok.javaserver.gameserver.model.actor.Creature;
import com.shnok.javaserver.gameserver.model.actor.Npc;
import com.shnok.javaserver.gameserver.model.actor.Playable;
import com.shnok.javaserver.gameserver.model.actor.Player;
import com.shnok.javaserver.gameserver.scripting.script.ai.individual.DefaultNpc;
import com.shnok.javaserver.gameserver.skills.L2Skill;

public class Archangel extends DefaultNpc
{
	public Archangel()
	{
		super("ai/boss/baium");
	}
	
	public Archangel(String descr)
	{
		super(descr);
	}
	
	protected final int[] _npcIds =
	{
		29021 // baium
	};
	
	@Override
	public void onNoDesire(Npc npc)
	{
		npc.getAI().addWanderDesire(5, 5);
	}
	
	@Override
	public void onCreated(Npc npc)
	{
		npc._i_ai0 = npc._param1;
		npc._i_ai1 = 1;
		npc._i_ai2 = 100;
		npc._i_ai3 = 0;
		
		startQuestTimer("2001", npc, null, 5000);
		startQuestTimer("2002", npc, null, 5000);
	}
	
	@Override
	public String onTimer(String name, Npc npc, Player player)
	{
		if (name.equalsIgnoreCase("2001"))
		{
			startQuestTimer("2002", npc, null, 5000);
			startQuestTimer("2001", npc, null, 5000L + (Rnd.get(5) * 1000));
			
			final Npc master = npc.getMaster();
			if (master == null)
				return super.onTimer(name, npc, player);
			
			final Creature topDesireTarget = npc.getAI().getTopDesireTarget();
			
			if (master._flag == 0)
			{
				if (!(topDesireTarget instanceof Player))
				{
					npc.getAI().getAggroList().stopHate(master);
					npc.lookNeighbor();
				}
				
				npc._i_ai1 = 0;
				
				if (npc._c_ai2 != null && !npc._c_ai2.isDead())
					npc.getAI().addAttackDesire(npc._c_ai2, ((npc._i_ai2 / npc.getStatus().getMaxHp()) / 0.05) * 100);
			}
			else
			{
				npc._i_ai1 = 0;
				
				if (master._flag == 1 && (npc._i_ai0 == 1 || npc._i_ai0 == 2))
					npc._i_ai1 = 1;
				else if (master._flag == 2 && (npc._i_ai0 == 3 || npc._i_ai0 == 4))
					npc._i_ai1 = 1;
				else if (master._flag == 3 && (npc._i_ai0 == 1 || npc._i_ai0 == 3))
					npc._i_ai1 = 1;
				else if (master._flag == 4 && (npc._i_ai0 == 2 || npc._i_ai0 == 4 || npc._i_ai0 == 5))
					npc._i_ai1 = 1;
				
				if (npc._i_ai1 == 0)
				{
					if (topDesireTarget != null)
					{
						if (!(topDesireTarget instanceof Playable))
							npc.getAI().getAggroList().stopHate(master);
						
						if (npc._c_ai2 != null)
							npc.getAI().addAttackDesire(npc._c_ai2, ((1.0 * npc._i_ai2) / (npc.getStatus().getLevel() + 7)) * 100);
					}
				}
				else if (topDesireTarget instanceof Playable)
					npc.removeAllAttackDesire();
				
				npc.getAI().addAttackDesire(master, 10000);
			}
		}
		else if (name.equalsIgnoreCase("2002"))
		{
			final Npc master = npc.getMaster();
			if (master == null || master.isDead() || master._flag == 6)
				npc.deleteMe();
			else
			{
				startQuestTimer("2002", npc, null, 5000);
				
				if (npc._i_ai1 == 1)
				{
					npc.getAI().addAttackDesire(master, 10000);
				}
				else if (npc._i_ai3 == 3)
				{
					npc._i_ai3 = 0;
					npc.getAI().addAttackDesire(master, 1000);
				}
				else if (npc.getAI().getCurrentIntention().getType() != IntentionType.ATTACK)
				{
					npc._i_ai3++;
					npc.lookNeighbor();
				}
			}
		}
		
		return super.onTimer(name, npc, player);
	}
	
	@Override
	public void onAttacked(Npc npc, Creature attacker, int damage, L2Skill skill)
	{
		Creature c0 = null;
		
		if (attacker instanceof Player)
		{
			npc._c_ai2 = attacker;
			npc._i_ai2 = damage;
		}
		
		if (npc._i_ai1 == 0)
		{
			c0 = attacker;
			if (attacker instanceof Playable)
			{
				if (damage == 0)
					damage = 1;
				
				npc.getAI().addAttackDesire(attacker, ((1.0 * damage) / (npc.getStatus().getLevel() + 7)) * 100);
			}
		}
		else
			c0 = npc.getMaster();
		
		if (Rnd.get(100) < 10)
			npc.getAI().addCastDesire(c0, 4132, 1, 1000000);
		
		if (Rnd.get(100) < 5 && npc.getStatus().getHpRatio() < 0.5)
			npc.getAI().addCastDesire(c0, 4133, 1, 1000000);
	}
	
	@Override
	public void onSeeCreature(Npc npc, Creature creature)
	{
		if (!(creature instanceof Playable) || creature.isDead() || !npc.getSpawn().isInMyTerritory(creature))
			return;
		
		if (npc._i_ai1 == 0)
		{
			final Creature topDesireTarget = npc.getAI().getTopDesireTarget();
			if (!(topDesireTarget instanceof Playable))
				npc.getAI().getAggroList().stopHate(npc.getMaster());
			
			npc.getAI().addAttackDesire(creature, 200);
		}
		
		if (npc._c_ai2 == null || npc._c_ai2.isDead())
		{
			npc._c_ai2 = creature;
			npc._i_ai2 = 100;
		}
	}
}