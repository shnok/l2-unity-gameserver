package com.shnok.javaserver.gameserver.scripting.script.ai.individual.Monster.RaidBoss.RaidBossAlone.RaidBossType1.RaidBossType1Aggressive;

import net.sf.l2j.gameserver.model.actor.Creature;
import net.sf.l2j.gameserver.model.actor.Npc;
import net.sf.l2j.gameserver.model.actor.Playable;
import net.sf.l2j.gameserver.scripting.script.ai.individual.Monster.RaidBoss.RaidBossAlone.RaidBossType1.RaidBossType1;

public class RaidBossType1Aggressive extends RaidBossType1
{
	public RaidBossType1Aggressive()
	{
		super("ai/individual/Monster/RaidBoss/RaidBossAlone/RaidBossType1/RaidBossType1Aggressive");
	}
	
	public RaidBossType1Aggressive(String descr)
	{
		super(descr);
	}
	
	@Override
	public void onSeeCreature(Npc npc, Creature creature)
	{
		if (creature instanceof Playable && npc.isInMyTerritory())
			npc.getAI().addAttackDesire(creature, 200);
	}
}