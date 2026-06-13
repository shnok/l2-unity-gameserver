package com.shnok.javaserver.gameserver.scripting.script.ai.individual.Monster.WarriorBase.Warrior.WarriorPhysicalSpecial;

import com.shnok.javaserver.gameserver.data.SkillTable;
import com.shnok.javaserver.gameserver.model.actor.Creature;
import com.shnok.javaserver.gameserver.model.actor.Npc;
import com.shnok.javaserver.gameserver.model.actor.Playable;
import com.shnok.javaserver.gameserver.model.actor.Player;
import com.shnok.javaserver.gameserver.skills.L2Skill;

public class ZakenPrivatePirateZombie extends WarriorPhysicalSpecialAggressive
{
	public ZakenPrivatePirateZombie()
	{
		super("ai/individual/Monster/WarriorBase/Warrior/WarriorPhysicalSpecial");
	}
	
	public ZakenPrivatePirateZombie(String descr)
	{
		super(descr);
	}
	
	protected final int[] _npcIds =
	{
		29026, // pirates_zombie_b
		29027, // pirates_zombie_captain_b
	};
	
	@Override
	public void onCreated(Npc npc)
	{
		npc._weightPoint = npc.getX();
		npc._respawnTime = npc.getY();
		npc._flag = npc.getZ();
	}
	
	@Override
	public void onSeeCreature(Npc npc, Creature creature)
	{
		if (creature.getZ() > (npc.getZ() - 100) && creature.getZ() < (npc.getZ() + 100))
		{
			if (!(creature instanceof Playable))
				return;
			
			if (npc.isInMyTerritory())
				npc.getAI().addAttackDesire(creature, 200);
			
			makeAttackEvent(npc, creature, 9, true);
		}
	}
	
	@Override
	public void onAttacked(Npc npc, Creature attacker, int damage, L2Skill skill)
	{
		if (attacker.getStatus().getLevel() > (npc.getStatus().getLevel() + 8))
		{
			final L2Skill raidCurse = SkillTable.getInstance().getInfo(4515, 1);
			npc.getAI().addCastDesire(attacker, raidCurse, 1000000);
		}
		
		super.onAttacked(npc, attacker, damage, skill);
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
}