package com.shnok.javaserver.gameserver.scripting.script.ai.boss.orfen;

import com.shnok.javaserver.commons.random.Rnd;

import com.shnok.javaserver.gameserver.model.actor.Creature;
import com.shnok.javaserver.gameserver.model.actor.Npc;
import com.shnok.javaserver.gameserver.model.actor.Player;
import com.shnok.javaserver.gameserver.scripting.script.ai.individual.DefaultNpc;
import com.shnok.javaserver.gameserver.skills.L2Skill;

public class OrfenPrivateRibaIren extends DefaultNpc
{
	public OrfenPrivateRibaIren()
	{
		super("ai/boss/orfen");
	}
	
	public OrfenPrivateRibaIren(String descr)
	{
		super(descr);
	}
	
	protected final int[] _npcIds =
	{
		29018 // riba_iren
	};
	
	@Override
	public void onNoDesire(Npc npc)
	{
		npc.getAI().addWanderDesire(5, 5);
	}
	
	@Override
	public void onAttacked(Npc npc, Creature attacker, int damage, L2Skill skill)
	{
		if (attacker.getStatus().getLevel() > (npc.getStatus().getLevel() + 8))
			npc.getAI().addCastDesire(attacker, 4515, 1, 1000000);
		
		if (npc.getStatus().getHpRatio() < 0.5)
			npc.getAI().addCastDesire(npc, 4516, 1, 1000000);
	}
	
	@Override
	public void onClanAttacked(Npc caller, Npc called, Creature attacker, int damage, L2Skill skill)
	{
		if (caller.getNpcId() == 29018)
			return;
		
		if (caller.getStatus().getHpRatio() < 0.5 && Rnd.get(100) < ((caller.getNpcId() == 29014) ? 90 : 10))
			called.getAI().addCastDesire(caller, 4516, 1, 1000000);
	}
	
	@Override
	public void onSeeSpell(Npc npc, Player caster, L2Skill skill, Creature[] targets, boolean isPet)
	{
		if (caster.getStatus().getLevel() > (npc.getStatus().getLevel() + 8))
		{
			npc.getAI().addCastDesire(caster, 4215, 1, 1000000);
			return;
		}
		
		super.onSeeSpell(npc, caster, skill, targets, isPet);
	}
}