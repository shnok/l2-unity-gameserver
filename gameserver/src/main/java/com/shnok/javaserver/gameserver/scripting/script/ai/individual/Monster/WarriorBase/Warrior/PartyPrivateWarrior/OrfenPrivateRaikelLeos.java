package com.shnok.javaserver.gameserver.scripting.script.ai.individual.Monster.WarriorBase.Warrior.PartyPrivateWarrior;

import com.shnok.javaserver.commons.random.Rnd;

import com.shnok.javaserver.gameserver.data.SkillTable;
import com.shnok.javaserver.gameserver.model.actor.Creature;
import com.shnok.javaserver.gameserver.model.actor.Npc;
import com.shnok.javaserver.gameserver.model.actor.Player;
import com.shnok.javaserver.gameserver.skills.L2Skill;

public class OrfenPrivateRaikelLeos extends PartyPrivateWarrior
{
	public OrfenPrivateRaikelLeos()
	{
		super("ai/individual/Monster/WarriorBase/Warrior/PartyPrivateWarrior");
	}
	
	public OrfenPrivateRaikelLeos(String descr)
	{
		super(descr);
	}
	
	protected final int[] _npcIds =
	{
		29016 // raikel_leos
	};
	
	@Override
	public void onCreated(Npc npc)
	{
		startQuestTimerAtFixedRate("2001", npc, null, 10000, 10000);
		
		final Npc master = npc.getMaster();
		if (master != null)
			npc._i_ai0 = master._flag;
		
		npc._i_ai1 = 1;
	}
	
	@Override
	public String onTimer(String name, Npc npc, Player player)
	{
		if (name.equalsIgnoreCase("2001"))
		{
			final Npc master = npc.getMaster();
			if (master != null && !master.isDead() && npc._i_ai1 == 1)
			{
				final int orfenFlag = master._flag;
				
				if (orfenFlag != npc._i_ai0 || npc.distance2D(master) > 3000)
				{
					npc._i_ai0 = orfenFlag;
					npc.teleportTo(master.getPosition().clone(), 0);
					npc.removeAllAttackDesire();
				}
			}
		}
		
		return super.onTimer(name, npc, player);
	}
	
	@Override
	public void onAttacked(Npc npc, Creature attacker, int damage, L2Skill skill)
	{
		if (attacker.getStatus().getLevel() > (npc.getStatus().getLevel() + 8))
		{
			final L2Skill raidCurse = SkillTable.getInstance().getInfo(4515, 1);
			npc.getAI().addCastDesire(attacker, raidCurse, 1000000);
		}
	}
	
	@Override
	public void onSeeSpell(Npc npc, Player caster, L2Skill skill, Creature[] targets, boolean isPet)
	{
		if (caster.getStatus().getLevel() > (npc.getStatus().getLevel() + 8))
		{
			final L2Skill raidMute = SkillTable.getInstance().getInfo(4215, 1);
			
			npc.getAI().addCastDesire(caster, raidMute, 1000000);
			
			return;
		}
		super.onSeeSpell(npc, caster, skill, targets, isPet);
	}
	
	@Override
	public void onPartyAttacked(Npc caller, Npc called, Creature target, int damage)
	{
		if (Rnd.get(100) < 5)
		{
			final L2Skill mortalBlow = SkillTable.getInstance().getInfo(4067, 4);
			
			called.getAI().addCastDesire(target, mortalBlow, 1000000);
		}
		super.onPartyAttacked(caller, called, target, damage);
	}
	
	@Override
	public void onPartyDied(Npc caller, Npc called)
	{
		if (called.hasMaster() && caller == called.getMaster())
			called._i_ai1 = 0;
	}
}
