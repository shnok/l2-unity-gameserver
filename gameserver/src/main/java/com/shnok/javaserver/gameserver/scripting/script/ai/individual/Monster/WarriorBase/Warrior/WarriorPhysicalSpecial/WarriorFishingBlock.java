package com.shnok.javaserver.gameserver.scripting.script.ai.individual.Monster.WarriorBase.Warrior.WarriorPhysicalSpecial;

import com.shnok.javaserver.commons.random.Rnd;

import com.shnok.javaserver.gameserver.enums.IntentionType;
import com.shnok.javaserver.gameserver.model.actor.Creature;
import com.shnok.javaserver.gameserver.model.actor.Npc;
import com.shnok.javaserver.gameserver.model.actor.Player;
import com.shnok.javaserver.gameserver.network.NpcStringId;
import com.shnok.javaserver.gameserver.skills.L2Skill;

/**
 * Fishing monster behavior, occuring at 5% of a successful fishing action.
 */
public class WarriorFishingBlock extends WarriorPhysicalSpecial
{
	public WarriorFishingBlock()
	{
		super("ai/group");
	}
	
	protected final int[] _npcIds =
	{
		18319,
		18320,
		18321,
		18322,
		18323,
		18324,
		18325,
		18326
	};
	
	@Override
	public String onTimer(String name, Npc npc, Player player)
	{
		if (name.equalsIgnoreCase("3000"))
		{
			final IntentionType type = npc.getAI().getCurrentIntention().getType();
			if (type != IntentionType.ATTACK && type != IntentionType.CAST)
				npc.deleteMe();
		}
		return null;
	}
	
	@Override
	public void onAttacked(Npc npc, Creature attacker, int damage, L2Skill skill)
	{
		if (Rnd.get(100) < 33)
			npc.broadcastNpcSay(retrieveNpcStringId(npc, 3), attacker.getName());
		
		super.onAttacked(npc, attacker, damage, skill);
	}
	
	@Override
	public void onCreated(Npc npc)
	{
		if (npc._summoner == null)
			npc.deleteMe();
		else
		{
			npc.getAI().addAttackDesire(npc._summoner, 2000);
			npc.broadcastNpcSay(retrieveNpcStringId(npc, 0), npc._summoner.getName());
			
			startQuestTimerAtFixedRate("3000", npc, null, 50000, 50000);
		}
		super.onCreated(npc);
	}
	
	@Override
	public void onMyDying(Npc npc, Creature killer)
	{
		npc.broadcastNpcSay(retrieveNpcStringId(npc, 6), killer.getName());
		
		super.onMyDying(npc, killer);
	}
	
	private static final NpcStringId retrieveNpcStringId(Npc npc, int index)
	{
		return NpcStringId.get(1010400 + index + ((npc.getNpcId() - 18319) * 9) + Rnd.get(3));
	}
}