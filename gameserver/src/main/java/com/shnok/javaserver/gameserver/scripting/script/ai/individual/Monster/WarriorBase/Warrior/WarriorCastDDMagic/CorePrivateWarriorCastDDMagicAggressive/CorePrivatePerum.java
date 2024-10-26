package com.shnok.javaserver.gameserver.scripting.script.ai.individual.Monster.WarriorBase.Warrior.WarriorCastDDMagic.CorePrivateWarriorCastDDMagicAggressive;

import net.sf.l2j.commons.random.Rnd;

import net.sf.l2j.gameserver.model.actor.Npc;
import net.sf.l2j.gameserver.model.actor.Player;

public class CorePrivatePerum extends CorePrivateWarriorCastDDMagicAggressive
{
	public CorePrivatePerum()
	{
		super("ai/individual/Monster/WarriorBase/Warrior/WarriorCastDDMagic/CorePrivateWarriorCastDDMagicAggressive");
	}
	
	public CorePrivatePerum(String descr)
	{
		super(descr);
	}
	
	protected final int[] _npcIds =
	{
		29012, // b02_perum
	};
	
	@Override
	public void onCreated(Npc npc)
	{
		startQuestTimer("4001", npc, null, 90000L + Rnd.get(240000));
		
		super.onCreated(npc);
	}
	
	@Override
	public String onTimer(String name, Npc npc, Player player)
	{
		if (name.equalsIgnoreCase("4001"))
		{
			if (Rnd.get(100) < 66)
				npc.getAI().getAggroList().randomizeAttack();
			
			startQuestTimer("4001", npc, null, 90000L + Rnd.get(240000));
		}
		
		return super.onTimer(name, npc, player);
	}
}