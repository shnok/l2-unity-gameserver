package com.shnok.javaserver.gameserver.scripting.script.ai.individual.Monster.RaidBoss.RaidBossAlone.RaidBossType1;

import net.sf.l2j.gameserver.model.actor.Creature;
import net.sf.l2j.gameserver.model.actor.Npc;
import net.sf.l2j.gameserver.model.actor.Playable;

public class RaidBossAloneSummonPrivate extends RaidBossType1
{
	private static final int MAX_PRIVATE_NUMBER = 32;
	
	public RaidBossAloneSummonPrivate()
	{
		super("ai/individual/Monster/RaidBoss/RaidBossAlone/RaidBossType1/RaidBossAloneSummonPrivate");
	}
	
	public RaidBossAloneSummonPrivate(String descr)
	{
		super(descr);
	}
	
	protected final int[] _npcIds =
	{
		29040
	};
	
	@Override
	public void onCreated(Npc npc)
	{
		npc._weightPoint = 10;
		
		createPrivates(npc);
		
		npc._i_ai0 = 0;
		
		super.onCreated(npc);
	}
	
	@Override
	public void onPartyAttacked(Npc caller, Npc called, Creature target, int damage)
	{
		if (target instanceof Playable)
		{
			if (damage == 0)
				damage = 1;
			
			called.getAI().addAttackDesire(target, (int) (((1.0 * damage) / (called.getStatus().getLevel() + 7)) * 100));
		}
		
		super.onPartyAttacked(caller, called, target, damage);
	}
	
	@Override
	public void onPartyDied(Npc caller, Npc called)
	{
		if (called._i_ai0 < MAX_PRIVATE_NUMBER && caller != called && !called.isDead())
		{
			caller.scheduleRespawn((caller.getSpawn().getRespawnDelay() * 1000));
			called._i_ai0++;
		}
	}
}