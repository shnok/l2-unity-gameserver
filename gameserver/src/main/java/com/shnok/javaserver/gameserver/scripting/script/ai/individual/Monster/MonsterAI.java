package com.shnok.javaserver.gameserver.scripting.script.ai.individual.Monster;

import com.shnok.javaserver.gameserver.model.actor.Creature;
import com.shnok.javaserver.gameserver.model.actor.Npc;
import com.shnok.javaserver.gameserver.model.actor.Player;
import com.shnok.javaserver.gameserver.model.actor.Summon;
import com.shnok.javaserver.gameserver.scripting.script.ai.individual.DefaultNpc;

public class MonsterAI extends DefaultNpc
{
	public MonsterAI()
	{
		super("ai");
	}
	
	public MonsterAI(String descr)
	{
		super(descr);
	}
	
	@Override
	public void onAttackFinished(Npc npc, Creature target)
	{
		if (target instanceof Summon && target.isDead())
		{
			final Player player = target.getActingPlayer();
			if (player != null)
				npc.getAI().addAttackDesire(player, 500);
		}
	}
}