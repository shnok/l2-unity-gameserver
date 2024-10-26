package com.shnok.javaserver.gameserver.scripting.script.ai.individual.Monster.WarriorBase.Warrior.PartyLeaderWarrior;

import net.sf.l2j.gameserver.model.actor.Creature;
import net.sf.l2j.gameserver.model.actor.Npc;
import net.sf.l2j.gameserver.model.actor.Playable;

public class PartyLeaderCastDDAggressive extends PartyLeaderCastDD
{
	public PartyLeaderCastDDAggressive()
	{
		super("ai/individual/Monster/WarriorBase/Warrior/PartyLeaderWarrior");
	}
	
	public PartyLeaderCastDDAggressive(String descr)
	{
		super(descr);
	}
	
	protected final int[] _npcIds =
	{
		20963,
		20749,
		20969,
		20944,
		20761,
		20747,
		20771,
		20977,
		22028
	};
	
	@Override
	public void onSeeCreature(Npc npc, Creature creature)
	{
		if (!(creature instanceof Playable))
			return;
		
		tryToAttack(npc, creature);
		
		super.onSeeCreature(npc, creature);
	}
}