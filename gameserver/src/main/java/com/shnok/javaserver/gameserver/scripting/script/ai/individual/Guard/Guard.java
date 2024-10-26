package com.shnok.javaserver.gameserver.scripting.script.ai.individual.Guard;

import net.sf.l2j.commons.random.Rnd;

import net.sf.l2j.gameserver.enums.IntentionType;
import net.sf.l2j.gameserver.model.actor.Creature;
import net.sf.l2j.gameserver.model.actor.Npc;
import net.sf.l2j.gameserver.model.actor.Player;
import net.sf.l2j.gameserver.scripting.script.ai.individual.DefaultNpc;
import net.sf.l2j.gameserver.skills.L2Skill;

public class Guard extends DefaultNpc
{
	public Guard()
	{
		super("ai/individual/Guard");
	}
	
	public Guard(String descr)
	{
		super(descr);
	}
	
	@Override
	public void onCreated(Npc npc)
	{
		final int moveAroundSocial1 = getNpcIntAIParam(npc, "MoveAroundSocial1");
		final int moveAroundSocial = getNpcIntAIParam(npc, "MoveAroundSocial");
		
		if (moveAroundSocial > 0 || moveAroundSocial1 > 0)
			startQuestTimerAtFixedRate("1671", npc, null, 10000, 10000);
		
		startQuestTimerAtFixedRate("9903", npc, null, 60000, 60000);
		
		super.onCreated(npc);
	}
	
	@Override
	public void onAttacked(Npc npc, Creature attacker, int damage, L2Skill skill)
	{
		npc.getAI().addAttackDesire(attacker, 2000);
	}
	
	@Override
	public void onSeeCreature(Npc npc, Creature creature)
	{
		if (creature instanceof Player && creature.getActingPlayer().getKarma() > 0)
			npc.getAI().addAttackDesire(creature, 1500);
	}
	
	@Override
	public String onTimer(String name, Npc npc, Player player)
	{
		if (name.equalsIgnoreCase("1671"))
		{
			if (npc.getStatus().getHpRatio() > 0.4 && !npc.isDead() && npc.getAI().getCurrentIntention().getType() != IntentionType.ATTACK)
			{
				final int moveAroundSocial1 = getNpcIntAIParam(npc, "MoveAroundSocial1");
				final int moveAroundSocial = getNpcIntAIParam(npc, "MoveAroundSocial");
				
				if (moveAroundSocial > 0 || moveAroundSocial1 > 0)
				{
					if (moveAroundSocial > 0 && Rnd.get(100) < 40)
						npc.getAI().addSocialDesire(3, (moveAroundSocial * 1000) / 30, 50);
					else if (moveAroundSocial1 > 0 && Rnd.get(100) < 40)
						npc.getAI().addSocialDesire(3, (moveAroundSocial1 * 1000) / 30, 50);
				}
			}
		}
		
		if (name.equalsIgnoreCase("9903"))
		{
			if (!npc.isInMyTerritory())
				npc.teleportTo(npc.getSpawnLocation(), 0);
		}
		
		return super.onTimer(name, npc, player);
	}
}
